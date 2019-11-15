package be.kuleuven.cs.distrinet.gmsa.deltaiot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {


	@Autowired
	private AccountService accountService;
	
	
	@Override
	public Account authenticate(String username, String password) {
		var account = accountService.findAccountById(username);
		if (password == null || account == null || account.getPassword() == null || !password.contentEquals(account.getPassword())) {
			return null;
		}
		return account;
	}


	@Override
	public Account resetPassword(String username) {
		var account = accountService.findAccountById(username);
		if (account != null) {
			// TODO: send email with instructions/link, and add code for visiting the link
			return account;
		}
		return null;
	}

}
