FROM ubuntu:latest
# dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>

#RUN apt-get install software-properties-common -y
#RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
#RUN apt-get install default-jre -y
#RUN apt-get install oracle-java8-installer -y
RUN apt-get install curl -y
RUN apt-get install zip -y

#install java8
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

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
RUN chmod +x /var/yunsoo/gradlew
RUN cd /var/yunsoo/ && ./gradlew bootRepackage

EXPOSE 80

CMD java -jar /var/yunsoo/dataAPI/build/libs/dataAPI-1.0.jar
#ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar