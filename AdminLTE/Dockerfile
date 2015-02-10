FROM nginx:latest
MAINTAINER Lijian Qiu <lijian@yunsu.co>

RUN apt-get update && apt-get install zip curl -y

RUN mkdir /var/yunsoo -p
RUN curl -o /var/yunsoo/dev.zip -L https://github.com/dataDeathKnight/yunsoo/archive/dev.zip
RUN cd /var/yunsoo && unzip dev.zip && mv yunsoo-dev/* . && rm -rf yunsoo-dev dev.zip

RUN cd /usr/share/nginx/html && rm -rf *
RUN cp -r /var/yunsoo/AdminLTE/src/app/* /usr/share/nginx/html

RUN rm -rf /var/yunsoo

EXPOSE 80
