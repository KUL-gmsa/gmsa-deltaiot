# Quick run with Docker

If you just want to quickly run and explore the application, without downloading and compiling the source, you can use Docker:

`docker run --rm -p 127.0.0.1:8080:8080 koenyskout/gmsa-deltaiot`

and point a browser to [http://localhost:8080](http://localhost:8080).




# Prerequisites

You will need:
- Git
- Something to view Java source files (a recent Eclipse (e.g. version 2019-09) or another GUI)
- Java SDK version 12 or higher; OR Docker




# Downloading

## In Eclipse

Retrieve the project from Git: File > Import > Git > Projects from Git (with smart import) and provide the details of this repository.

## From the command line or using a Git GUI tool

Clone the current repository.




# Compiling

You can compile from within Eclipse, from the command line, or using Docker.
The Docker option does not require you to install anything else (e.g., Java, Maven) on your system (except Docker itself).
For the other options, you need at least a Java SDK version 12 or higher.

## In Eclipse

Eclipse will compile the project using Maven.

If you You can install [Spring Tools Suite](https://spring.io/tools).

Alternatively, if you want to add the Spring tools to an existing Eclipse installation, you can add "Spring Tools 4 - For Spring Boot" from the Eclipse Marketplace.

## Using Docker

`docker build -t gmsa-deltaiot .`

(don't forget the final dot)

## Using Maven from the command line

From the root folder of the project, run the Maven wrapper script:

`./mvnw package` (on Linux/MacOs)

`mvnw.cmd package` (on Windows)





# Running

The application starts a Tomcat server on your local machine, port 8080.
You can browse to the application via [http://localhost:8080](http://localhost:8080).

## From Eclipse

Select Server.java in be.kuleuven.cs.distrinet.gmsa.deltaiot, right-click and choose "Run as > Java Application".
If you have Spring tools installed, you can also right-click the project and choose "Run as > Spring Boot Application".

## From the command line

This requires the project to be built (either from Eclipse or using Maven from the command line)

`java -jar target/GMSA-DeltaIoT-Server-0.0.1-SNAPSHOT.jar`

## Using Docker

`docker run --rm -p 127.0.0.1:8080:8080 gmsa-deltaiot`

The --rm option removes the container once it is stopped.

The -p option exposes the port 8080 from the container to the host, so you can connect to it.

