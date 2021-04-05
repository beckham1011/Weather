#Environment require:
1, JDK, Add %JAVA_HOME% and java path to environment.
2, Maven, Add %MVN_HOME% and maven path to environment.

#Check java and maven in command
java -version
mvn -version

#Download
git clone https://github.com/beckham1011/Weather.git
git checkout SMSTest

#Compile
mvn package

#Run
mvn springboot:run



