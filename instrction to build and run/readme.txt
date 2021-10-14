Pre requirment to this software
================================

1. Java 11
2. Mysql 8
3. Web Browser {chrome, firefox, edge, safari}

Pre configuration
==================
MySql
    -username should be "root"
    -password should be "12345"

How To Run
==========

1. direct to the folder which jar file is located
2. open turminal window and run following command
    "./gradlew build"
3. after there is in folder in your current directory as "build"
    go to build -> libs
    open location with command prompt -> follow below instructions to run application
4. before run the following command check whether mysql username and password check or not and run
    "java -jar {softwarename}.jar"
    - ex: java -jar contract_tracer-0.0.1.jar
5. open web browser and got to https://localhost:8080
    now your in the system
6. when you run application first time
    you have to create user for login follow below step
            1.https://localhost:8080/select -> take few times to config
            2.https://localhost:8080/select/user -> it will show your user name and password
            3.then click login button and log in to the system
7. now you are in admin window
        - create users
            1. create user for phi
                    click add user deatails in nav bar and save user
                    after click add user and set user with role (currently this software function only ADMIN and PHI)
                    save and create user
8. now you have to users in your system
9. check the functionality :)

