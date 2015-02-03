FROM dockerfile/java:oracle-java8
MAINTAINER Zhe Zhang <zhe@yunsu.co>
EXPOSE 8080
RUN gradlew build
CMD java -jar dataAPI-1.0.jar
ADD dataAPI/build/libs/dataAPI-1.0.jar /data/dataAPI-1.0.jar