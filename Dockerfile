FROM ubuntu:latest
# dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>

RUN apt-get update

RUN apt-get install default-jre -y

RUN apt-get install default-jdk -y

#install gradle
RUN gradle_version=2.21
RUN wget -N http://services.gradle.org/distributions/gradle-${gradle_version}-all.zip
RUN sudo unzip -foq gradle-${gradle_version}-all.zip -d /opt/gradle
RUN sudo ln -sfn gradle-${gradle_version} /opt/gradle/latest
RUN sudo printf "export GRADLE_HOME=/opt/gradle/latest\nexport PATH=\$PATH:\$GRADLE_HOME/bin" > /etc/profile.d/gradle.sh
. /etc/profile.d/gradle.sh
 
ADD src/ /app/src/

WORKDIR /app/

RUN gradle -v
#RUN gradle wrapper
RUN gradle bootRepackage

EXPOSE 8080

CMD java -jar dataAPI/build/libs/dataAPI-1.0.jar
#ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar