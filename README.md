I created this project as a template for play 2.5 application with compile time dependency injection.

It is solely meant for writing back end services with minimal of web interface. So that I can just download and start coding rather than changing and removing stuff from play-scala template every time I start a new project.

Includes docker-compose. On first start it will build docker image named as `tester` but it can be changed to whatever you like in `docker-compose.yml` file.

Also worth noting that docker-compose needs 2 named external volumes to enable quick startup. So if the volumes are not present already, they should be created with one time command for both volumes as follow:
 
    docker volume create --name sbt
    docker volume create --name ivy2

# How to clone:

    git clone https://github.com/naumanbadar/play2.5-Compile-time-DI.git <your project name>
    
# How to start activator

#### Start in auto compile mode which will recompile on every change in sources.

    cd <your project name>
    docker-compose up
    
#### Start container with bash so you can either chose to test, compile or simply use the shell.

    cd <your project name>
    docker-compose run tester bash
    
Note that it will not bind container ports to host port as per design of docker-compose. If ports needs to be accessed with `docker-compose run` then use the flag `--service-ports`.

    cd <your project name>
    docker-compose run --service-ports tester bash
    

# Docker base image for testing

Dockerfile for `naumanbadar/sbt_docker`, referred in docker-compose.yml, can be found at <https://github.com/naumanbadar/sbt_docker> or the image itself can be pulled from <https://hub.docker.com/r/naumanbadar/sbt_docker/~/dockerfile/> 