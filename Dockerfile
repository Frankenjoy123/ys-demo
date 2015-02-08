FROM dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>

# BELOW shows the docker file for Dockerhub auto-build.
RUN apt-get update
RUN apt-get install zip -y

RUN mkdir /var/yunsoo
RUN curl -o  /var/yunsoo/dev.zip -L https://github.com/dataDeathKnight/yunsoo/archive/dev.zip
RUN cd /var/yunsoo/ && unzip dev.zip && mv yunsoo-dev/* . && rm -rf yunsoo-dev dev.zip

#ADD src/ /app/src/
#WORKDIR /app/

RUN cd /var/yunsoo/ && chmod +x gradlew && ./gradlew bootRepackage

#dynamodb credentials
RUN echo [dynamodb] >> /var/yunsoo/credentials
RUN echo aws_access_key_id=AKIAJAMFJ5DUPOD426CQ >> /var/yunsoo/credentials
RUN echo aws_secret_access_key=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 >> /var/yunsoo/credentials

EVN AWS_CREDENTIAL_FILE /var/yunsoo/credentials

EXPOSE 8080

CMD java -jar /var/yunsoo/dataAPI/build/libs/dataAPI-1.0.jar
#ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar

# !Below shows how to deploy jar into AWS Beanstalk

#RUN mkdir /var/yunsoo
#RUN curl -o  /var/yunsoo/yunsoo-API.jar -L https://s3-us-west-2.amazonaws.com/todeploy/dataAPI-1.0.jar

#EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "/var/yunsoo/yunsoo-API.jar"]
#CMD [""]