package com.challenge;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class Users {
    private static ObjectMapper mapper;
    private Map<String, UserInformation> userInformationMap;

    Users(){
        userInformationMap = new HashMap<>();
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private static class UserInformation{
        String uid;
        String full_name;
        Set<String> groups;

        public UserInformation(String uid, String full_name, String group) {
            this.uid = uid;
            this.full_name = full_name;
            this.groups = new HashSet<>();
            if (!group.equals(""))
                this.groups.add(group);
        }

    }

    public void createNewUser(String userName, String uid, String fullName, String primaryGroup){
        userInformationMap.put(userName, new UserInformation(uid,fullName,primaryGroup));
    }

    public void addUserMapping(String userName, String primaryGroup){
        userInformationMap.get(userName).groups.add(primaryGroup);
    }

    @Override
    public String toString() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInformationMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
