Password Parser

A simple utility to identify all the users on a linux machine and the groups they are a part of. This project has been implemented keeping the reference files from my system as examples. These files are provided as "groups.txt" and "passwords.txt". 

To run the java version, please use the fatjar created at build/libs. If you wish to create the jar again, run "gradle fatJar" in command line, and the jar with all needed dependencies (hence the name fatjar) will be generated at build/libs. 
-Run "java -jar <jarname>" This will run with default files at /etc/passwd and /etc/group. (jar name would build/libs/assignment_java-all-1.0.jar if running from home directory.)
-To run with custom files, run "java -jar <jarname> -p <path to passwordsFile> -g <path to groupsFile>". The program expects this format of input.
  
  Some features of this tool are mentioned here: 
1. If duplicate users have been found, it always keeps the latest one. (Usually linux doesn't create duplicate users, but custom files can be malformed.)
2. If the files are of incorrect format (w.r.t num of parameters per line), it throws an exception and terminates.
3. If any files are missing it terminates.
4. The parsing of files skips any commented lines and blank lines.
5. If a user/group is found in one file but not in the other, it ignores it entirely.


For the Python implementation, please look into the passwords.py file.
To run it use the command "py passwords.py". Add optional arguments the same way as above i.e. with -p and -g. 

Disclaimer: Python isn't my go-to language. Although I can work in Python, I do not have experience writing production ready python programs.
Somethings like design patterns, and best practices may be lacking in my python implementation.

I have more experience in Java so I hope the Java implementation would be considered too. I've tried to implement production-ready code there, to the best of my knowledge. 

Hope you had a good time looking at the code! Please feel free mail me at hmulabagula@dons.usfca.edu for any queries.
