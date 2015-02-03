FROM dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>
EXPOSE 8080

# the spring boot version
ENV SPRING_BOOT_VERSION 1.2.0.RELEASE

RUN \
    mkdir /opt/spring-boot && \
    cd /opt/spring-boot && \
    wget http://repo1.maven.org/maven2/org/springframework/boot/spring-boot-cli/${SPRING_BOOT_VERSION}/spring-boot-cli-${SPRING_BOOT_VERSION}-full.jar && \
    ln -s spring-boot-cli-${SPRING_BOOT_VERSION}-full.jar spring-boot && \
    echo -e '#!/bin/sh \njava -jar /opt/spring-boot/spring-boot "$@"' > /usr/bin/spring-boot && \
    chmod 751 /usr/bin/spring-boot;

ENTRYPOINT ["spring-boot"]

#install gradle
#RUN gradle_version=2.21
#RUN wget -N http://services.gradle.org/distributions/gradle-${gradle_version}-all.zip
#RUN sudo unzip -foq gradle-${gradle_version}-all.zip -d /opt/gradle
#RUN sudo ln -sfn gradle-${gradle_version} /opt/gradle/latest
#RUN sudo printf "export GRADLE_HOME=/opt/gradle/latest\nexport PATH=\$PATH:\$GRADLE_HOME/bin" > /etc/profile.d/gradle.sh
#. /etc/profile.d/gradle.sh
#RUN gradle -v
#RUN gradle wrapper
#RUN gradlew build
#CMD java -jar dataAPI-1.0.jar
#ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar