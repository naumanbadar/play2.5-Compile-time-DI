FROM openjdk:8-jre

MAINTAINER Nauman Badar <naumanb@kth.se>

# set correct time zone. In my case it is Stockholm. This is a temporary fix until docker fixes the issue of always getting container with UTC timezone.
RUN echo Europe/Stockholm | tee /etc/timezone && dpkg-reconfigure --frontend noninteractive tzdata

# some usefull command aliases
RUN echo "alias ll='ls -la'" >> /root/.bashrc \
 && echo "alias l=ls" >> /root/.bashrc

# sbt
#~~~~

# this is required otherwise it will give the error 'E: The method driver /usr/lib/apt/methods/https could not be found.'
# when trying to install sbt in next step.
RUN apt-get update \
&& apt-get install -y apt-transport-https \
# now install sbt as per intructions on its website.
&& echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list \
&& apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 \
&& apt-get update \
&& apt-get install -y sbt

WORKDIR /project
