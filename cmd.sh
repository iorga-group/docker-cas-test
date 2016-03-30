#!/bin/bash

mkdir -p /etc/cas/jetty

keytool -genkey -noprompt -dname "CN=`hostname`, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown" -keyalg RSA -alias selfsigned -keystore /etc/cas/jetty/thekeystore -storepass changeit -keypass changeit -validity 36000 -keysize 2048

cd /usr/src/app && mvn clean jetty:run-forked
