FROM centos:7
RUN yum -y update && \
    yum -y install httpd php
WORKDIR /var/www/html
ADD ./code /var/www/html/