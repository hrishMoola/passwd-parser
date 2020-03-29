package com.challenge;

import java.io.File;

public class Main {

//username:x:UID:GID:comment:home_directory:login_shell /etc/passwd
//groupname:x:GID:userid1,userid2,userid3  /etc/group

    public static void main(String[] args) {
//        String passwordsPath = "passwords.txt", groupsPath = "groups.txt";
        String groupsPath = "/etc/group", passwordsPath = "/etc/passwd";
        int i =0;
        PasswordParser passwordParser = new PasswordParser();
        while( i < args.length){
            if(args[i].contains("-g"))
                groupsPath = args[++i];
            else if(args[i].contains("-p"))
                passwordsPath = args[++i];
            i++;
        }
        try {
            if (checkFiles(passwordsPath, groupsPath)) {
                passwordParser.getUserDetailsAndMerge(passwordsPath, groupsPath);
                passwordParser.printAllUserInfo();
            } else {
                throw new Exception("One/both of the files doesn't/don't exist.");
            }
        }  catch (Exception e) {
            System.out.println("Exception : " + e.getLocalizedMessage());
            System.out.println("Terminating!");
        }
    }


    private static boolean checkFiles(String passwordsPath, String groupsPath){
        return new File(passwordsPath).exists() && new File(groupsPath).exists();
    }
}
