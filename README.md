# Important disclaimer

Be aware that this application is **intentionally insecure**, and does not follow best coding practices at all.


# Quick run with Docker

If you just want to quickly run and explore the application, without downloading and compiling the source, you can use Docker.

Make sure Docker has been started, execute

`docker run --rm -p 127.0.0.1:8080:8080 koenyskout/gmsa-deltaiot`

and point a browser to [http://localhost:8080](http://localhost:8080).



# Prerequisites

You will need:
- A recent version of Eclipse (2019-09) or IntelliJ (or your IDE of choice)
- Java SDK version 11 or higher; OR Docker




# Downloading

## In Eclipse

Retrieve the project from Git: `File > Import > Git > Projects from Git (with smart import)` and provide the details of this repository. The 'smart import' part is important to make Eclipse recognize the project as a Maven project.

(Alternatively, you can import the project using `File > Import > Maven > Checkout Maven projects from SCM`.)

## In IntelliJ

Create a new project/Checkout from version Control > Git, and enter the URL of this repository to clone it.
IntelliJ will recognize the project as a Maven project, and configure itself accordingly (indexing might take a while).


## From the command line or using a Git GUI tool

Clone the current repository.




# Compiling

You can compile from within Eclipse, from the command line, or using Docker.
The Docker option does not require you to install anything else (e.g., Java, Maven) on your system (except Docker itself).
For the other options, you need at least a Java SDK version 11 or higher.

## In Eclipse

Eclipse will compile the project using Maven. You may first have to configure the Java version of the Eclipse project.
Right-click on the project, select `Build path > Configure Build Path`, and in the Libraries tab, edit the JRE System Library to point to the JavaSE-11 environment.

If you want, you can install [Spring Tools Suite](https://spring.io/tools) or add the Spring tools to an existing Eclipse installation by installing "Spring Tools 4 - For Spring Boot" from the Eclipse Marketplace. This is not necessary to view or compile the source code, but enables some additional development support. 

## In IntelliJ

IntelliJ will compile the project using Maven.

## Using Docker

In the root folder of the project, run 

`docker build -t gmsa-deltaiot .`

(don't forget the final dot)

This builds the project in a container using Maven, and prepares a self-contained Docker image that you can run.

## Using Maven from the command line

From the root folder of the project, run the Maven wrapper script:

`./mvnw -Dmaven.test.skip package` (on Linux/MacOs)

`mvnw.cmd -Dmaven.test.skip package` (on Windows)





# Running

The application starts an embedded Tomcat server on your local machine, port 8080.
You can browse to the application via [http://localhost:8080](http://localhost:8080).

## From Eclipse

Select Server.java in be.kuleuven.cs.distrinet.gmsa.deltaiot, right-click and choose "Run as > Java Application".
If you have Spring tools installed, you can also right-click the project and choose "Run as > Spring Boot Application".

## From IntelliJ

Select Server in be.kuleuven.cs.distrinet.gmsa.deltaiot, right-click and choose "Run 'Server'".


## From the command line

This requires the project to be built and packaged into a JAR file (see before).

`./mvnw spring-boot:run`

## Using Docker

`docker run --rm -p 127.0.0.1:8080:8080 gmsa-deltaiot`

The --rm option removes the container once it is stopped.

The -p option exposes the port 8080 from the container to the host, so you can connect to it.

