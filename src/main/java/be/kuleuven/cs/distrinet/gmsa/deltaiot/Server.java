package be.kuleuven.cs.distrinet.gmsa.deltaiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application, running an embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class Server {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

}
