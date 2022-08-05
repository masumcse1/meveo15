
FROM maven:3.6-jdk-11-slim AS build-servlets

# Install git packages necessary

RUN apt-get -y update
RUN apt-get -y install git
 
ARG SCM="scm:git:ssh://git@github.com:masumcse1/meveo15.git"

WORKDIR /usr/src/meveo15

COPY . .

RUN mvn clean package -Dscm.url=${SCM} -DskipTests

####################################


FROM openjdk:11.0.7-jdk-slim-buster

###################

### ------------------------- base ------------------------- ###
# Install packages necessary
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        apt-transport-https ca-certificates curl jsvc unzip \
# procps : for 'free' command
        procps \
# iputils-ping : for 'ping' command
        iputils-ping \
# iproute2 : for 'ip' command
        iproute2 \
# postgresql-client : for 'pg_isready' command
        postgresql-client \
# java.lang.UnsatisfiedLinkError: /usr/local/openjdk-11/lib/libfontmanager.so: libfreetype.so.6: cannot open shared object file: No such file or directory
# java.lang.NoClassDefFoundError: Could not initialize class sun.awt.X11FontManager
# https://github.com/docker-library/openjdk/pull/235#issuecomment-424466077
        fontconfig libfreetype6 \
    && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*
##########

RUN mkdir /opt/jboss

RUN mkdir /opt/jboss/wildfy


WORKDIR /opt/jboss/wildfly
RUN curl -O https://download.jboss.org/wildfly/18.0.1.Final/wildfly-18.0.1.Final.tar.gz
RUN tar xvfz wildfly*.tar.gz
RUN mv wildfly-18.0.1.Final/* /opt/jboss/wildfly


WORKDIR /opt/jboss/wildfly/standalone/deployments

COPY  --from=build-servlets /usr/src/meveo15/meveo-ear/target/meveo.ear  meveo.ear
                                    


EXPOSE 8080
WORKDIR /opt/jboss/wildfly/bin


RUN /opt/jboss/wildfly/bin/add-user.sh admin welcome1 --silent
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]