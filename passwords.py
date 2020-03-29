import sys
import os.path
import pprint
import json
from json import JSONEncoder



def main():
#    passwordsPath = 'passwords.txt';
#    groupsPath = 'groups.txt';
    groupsPath = '/etc/group';
    passwordsPath = '/etc/passwd';
    i = 0;
    length = len(sys.argv);
    while i < length:
        if sys.argv[i].find('-g') > -1:
            i = i + 1;
            groupsPath = sys.argv[i];
        elif sys.argv[i].find('-p') > -1:
            i = i + 1;
            passwordsPath = sys.argv[i];
        i = i + 1;

    if(checkFiles(passwordsPath, groupsPath)):
        getUserDetailsAndMerge(passwordsPath, groupsPath);
    else:
        print("Atleast one of the files doesn't exist!")
        print("Terminating");
    # print(passwordsPath);
    # print(groupsPath);
  

def checkFiles(passwordsPath, groupsPath):
    return os.path.isfile(passwordsPath) & os.path.isfile(groupsPath);


def getUserDetailsAndMerge(passwordsPath, groupsPath):
    groupDetails = {};
    userDetails = {};
    try:
        getAllUsersDetails(passwordsPath, userDetails);
        getAllGroupsDetails(groupsPath, groupDetails);
        createUsersAndUpdateGroups(groupDetails, userDetails);
    except NameError as err:
        print(err)

def createUsersAndUpdateGroups(groupDetailsDict, userDetailsDict):
    usersData = users();
    for userId, userDetails in userDetailsDict.items():
        primaryGroupName = groupDetailsDict.get(userDetails[1]);
        if primaryGroupName == None: 
            primaryGroupName = [""];
        usersData.createNewUser(userDetails[0], userId, userDetails[2], primaryGroupName[0]);
    for groupDetails in groupDetailsDict.values():
        for userId in groupDetails[1].split(","):
            if(userId != ''):
                usersData.addUserMapping(userId, groupDetails[0]); 
    data = str(usersData);   
    print(data.replace("[0]", '""'));


def getAllGroupsDetails(fileName, groupDetails):
    lines = [line.rstrip('\n') for line in open(fileName)];
    for line in lines:
        if(line.startswith('#') == False):
            if(len(line) >= 1):
                if(line.count(":") != 3):
                    raise NameError(fileName + " file doesn't have right format");
                fields = line.split(":");
                details = [];
                details.append(fields[0]);     #groupName
                userids = fields[3] if (len(fields) == 4) else "";
                details.append(userids);     #csv of userIds
                groupId = fields[2];
                if groupId in groupDetails.keys():
                    print("Duplicate user found! Overwriting existing user");              #ideally you shouldn't have duplicate users, but Linux doesn't stop you from adding them. Also, custom files can have duplicates.
                groupDetails[groupId] =  details;


def getAllUsersDetails(fileName, userDetails):
    lines = [line.rstrip('\n') for line in open(fileName)];

    for line in lines:
        if(line.startswith('#') == False):
            if(len(line) >= 1):
                if(line.count(":") != 6):
                    raise NameError(fileName + " file doesn't have right format");
                fields = line.split(":");
                # print(fields[0]);
                details = [];
                details.append(fields[0]);     #username
                details.append(fields[3]);     #primary groupId
                details.append(fields[4]);     #The "comment" field is most likely a full-name
                userId = fields[2];
                if userId in userDetails.keys():
                    print("Duplicate user found! Overwriting existing user");              #ideally you shouldn't have duplicate users, but Linux doesn't stop you from adding them. Also, custom files can have duplicates.
                userDetails[userId] =  details;



class users:
    def __init__(self):
        self.userInformationMap = {};

    class userInformation:
        def __init__(self, uid, full_name, group):
            self.uid = uid;
            self.full_name = full_name;
            if(group!= ""):
                self.groups = set([group]);
        
        def addGroup(self, group):
            self.groups.add(group);


    def createNewUser(self, userName, uid, fullName, primaryGroup):
        self.userInformationMap[userName] = self.userInformation(uid,fullName,primaryGroup);
    
    def addUserMapping(self, userName, primaryGroup):
        self.userInformationMap[userName].addGroup(primaryGroup);

    def __str__(self):
        class SetEncoder(json.JSONEncoder):
             def default(self, o):
                if isinstance(o, set):
                    return list(o)
                return o.__dict__
        return json.dumps(self.userInformationMap, cls = SetEncoder, sort_keys=True)

if __name__== "__main__":
  main()




