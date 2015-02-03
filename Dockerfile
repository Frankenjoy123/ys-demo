FROM dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>
EXPOSE 8080
RUN gradle_version=2.21
RUN wget -N http://services.gradle.org/distributions/gradle-${gradle_version}-all.zip
RUN sudo unzip -foq gradle-${gradle_version}-all.zip -d /opt/gradle
RUN sudo ln -sfn gradle-${gradle_version} /opt/gradle/latest
RUN sudo printf "export GRADLE_HOME=/opt/gradle/latest\nexport PATH=\$PATH:\$GRADLE_HOME/bin" > /etc/profile.d/gradle.sh
. /etc/profile.d/gradle.sh
RUN gradle -v
RUN gradle wrapper
RUN gradlew build
CMD java -jar dataAPI-1.0.jar
ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar