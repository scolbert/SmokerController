Update instructions and other information

Software update instructions:
1:  Download the software from https://github.com/STGUtah/SmokerController.git
2:  mvn clean install (this will build a full spring boot executable jar file)
3:  ssh pi@{Pi IP address}
4:  kill the java smoker process
5:  scp target/smoker-*.jar pi@{Pi IP address}:.
6:  sudo java -jar smoker-*.jar

The index.html has links to swagger so you can manipulate the interface directly.  It also has a link to the h2 db console.

The white light turns on when the smoker is ready to takes commands.  The white light flashes when there is a session going on.  
The red light is on when the fan is on.

Starting the app instructions
Navigate to the root directory (SmokerController directory)
Build the app using mvn clean install
Start the app using java -jar target/smoker-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev



