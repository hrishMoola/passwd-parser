package com.challenge;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordParser {
    Map<String, String[]> userDetails;
    Map<String, String[]> groupDetails;
    Users users;

    public PasswordParser() {
        userDetails = new HashMap<>();
        groupDetails = new HashMap<>();
        users = new Users();
    }

    public void getUserDetailsAndMerge(String passwordsPath, String groupsPath) throws Exception {
            getAllUsersDetails(passwordsPath);
            getAllGroupsDetails(groupsPath);
            createUsersAndUpdateGroups();
    }

    public void printAllUserInfo(){
        System.out.println(users);
    }

    private void createUsersAndUpdateGroups() {
        try{
            userDetails.forEach((userId, userDetails) -> {
                String primaryGroupName = groupDetails.getOrDefault(userDetails[1], new String[]{""})[0];
                users.createNewUser(userDetails[0], userId, userDetails[2], primaryGroupName);
            });

            groupDetails.values().forEach(groupDetails -> {
                Arrays.stream(groupDetails[1].split(",")).forEach(userId -> {
                    if(!userId.equalsIgnoreCase(""))
                        users.addUserMapping(userId, groupDetails[0]);
                });
            });
        }catch (NullPointerException e){
//            System.out.println("chill");
            e.printStackTrace();
        }

    }

    private void getAllUsersDetails(String passwordsPath) throws Exception {
        List<String> lines = FileUtils.readLines(new File(passwordsPath));
        for( String line : lines) {
            if (!line.startsWith("#") && !line.equalsIgnoreCase("")) {
                if (line.chars().filter(ch -> ch == ':').count() != 6) {
                    throw new Exception(passwordsPath + " file doesn't have right format ! " + line);
                }
                String[] fields = line.split(":");
                String[] details = new String[3];
                details[0] = fields[0];     //username
                details[1] = fields[3];     // primary groupId
                details[2] = fields[4]  ;     // The "comment" field is most likely a full-name
                String userId = fields[2];
                if (userDetails.containsKey(userId))
                    System.out.println("Duplicate user found! Overwriting existing user : " + userId);              //ideally you shouldn't have duplicate users, but Linux doesn't stop you from adding them. Also, custom files can have duplicates.
                userDetails.put(userId, details);
            }
        }

    }

    private void getAllGroupsDetails(String groupsPath) throws Exception {
            List<String> lines = FileUtils.readLines(new File(groupsPath));
            for( String line : lines) {
                if(!line.startsWith("#") && !line.equalsIgnoreCase("")){
                    if(line.chars().filter(ch -> ch == ':').count() != 3) {
                        throw new Exception(groupsPath + " file doesn't have right format ! " + line);
                    }
                    String[] fields = line.split(":");
                    String[] details = new String[2];
                    details[0] = fields[0];     //groupName
                    details[1] = (fields.length == 4) ? fields[3] : "";     //csv of userIds
                    String groupId = fields[2];
                    if(groupDetails.containsKey(groupId)) {
                        System.out.println("Duplicate user found! Overwriting existing user " + groupId);              //ideally you shouldn't have duplicate users, but Linux doesn't stop you from adding them. Also, custom files can have duplicates.
                    }
                    groupDetails.put(groupId, details);
                }
            }
    }
}
