package be.kuleuven.cs.distrinet.gmsa.deltaiot.service;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;

public interface AuthenticationService {

	public Account authenticate(String username, String password);
	
	public Account resetPassword(String username);
	
}
