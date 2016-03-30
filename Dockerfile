FROM maven:3.3.3-jdk-8

ADD src /usr/src/app/src

ADD pom.xml /usr/src/app/

ADD etc/jetty /usr/src/app/etc/jetty

ADD etc/cas.properties /etc/cas/
ADD etc/log4j2.xml /etc/cas/

#ADD etc/* /etc/

ADD cmd.sh /usr/local/bin/

CMD ["cmd.sh"]

