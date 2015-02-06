FROM ubuntu:latest
# dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>

RUN apt-get update
RUN apt-get install default-jre -y
RUN apt-get install default-jdk -y
RUN apt-get install curl -y
RUN apt-get install zip -y

#install gradle
#RUN gradle_version=2.21
#RUN wget -N http://services.gradle.org/distributions/gradle-2.2.1-all.zip
#RUN sudo unzip -foq gradle-${gradle_version}-all.zip -d /opt/gradle
#RUN sudo ln -sfn gradle-${gradle_version} /opt/gradle/latest
#RUN sudo printf "export GRADLE_HOME=/opt/gradle/latest\nexport PATH=\$PATH:\$GRADLE_HOME/bin" > /etc/profile.d/gradle.sh
#. /etc/profile.d/gradle.sh

RUN mkdir /var/yunsoo
RUN curl -o  /var/yunsoo/dev.zip -L https://github.com/dataDeathKnight/yunsoo/archive/dev.zip
RUN cd /var/yunsoo/ && unzip dev.zip && mv yunsoo-dev/* . && rm -rf yunsoo-dev dev.zip

#ADD src/ /app/src/
#WORKDIR /app/

#RUN gradle -v
RUN gradlew bootRepackage

EXPOSE 80

CMD java -jar /var/yunsoo/dataAPI/build/libs/dataAPI-1.0.jar
#ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar