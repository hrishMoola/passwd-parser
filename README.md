Password Parser

A simple utlity to identify all the users on a linux machine and the groups they are a part of.

To run the java version, please use the fatjar created at build/libs. 
-Run "java -jar <jarname>" This will run with default files at /etc/passwd and /etc/group. 
-To run with custom files, run "java -jar <jarname> -p passwordsFile -g groupsFile". The program expects this format of input.
  
  Some features of this tool are mentioned here: 
1. If duplicate users have been found, it always keeps the latest one. (Usually linux doesn't create duplicate users, but custom files can be malformed.)
2. If the files are of incorrect format (w.r.t num of parameters per line), it throws an exception and terminates.
3. If any files are missing it terminates.
4. The parsing of files skips any commented lines and blank lines.
5. If a user/group is found in one file but not in the other, it ignores it entirely.
