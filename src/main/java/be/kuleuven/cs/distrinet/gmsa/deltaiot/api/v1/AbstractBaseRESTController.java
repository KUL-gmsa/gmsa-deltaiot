package be.kuleuven.cs.distrinet.gmsa.deltaiot.api.v1;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.UnauthorizedException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;

@RestController
public abstract class AbstractBaseRESTController {
	
	public static final String TOKEN_HEADER_NAME = "DeltaIoT-Token";

	@Autowired
	private AccountService accountService;
	
	/** 
	 * Validate if the given API token belongs to an admin account.
	 * Returns the account if true, throws an UnauthorizedException otherwise
	 **/ 
    protected @NonNull Account validateAdminApiToken(String token) {
    	var account = validateApiToken(token);
    	if (account.isAdmin()) {
    		return account;
    	}
    	throw new UnauthorizedException("Must provide an administrator API token");
	}
    
	/** 
	 * Validate if the given API token is a valid token.
	 * Returns the asociated account if true, throws an UnauthorizedException otherwise
	 **/ 
 
    protected @NonNull Account validateApiToken(String token) {
    	var account = findAccountFromApiToken(token); 
    	if (account.isEmpty()) {
    		throw new UnauthorizedException("Must provide a valid API token");
    	}
    	return account.get();
	}

    /** Get the account that belongs to the given API token, if any */
	private Optional<Account> findAccountFromApiToken(String token) {
		if (token == null || token.isEmpty()) {
			throw new UnauthorizedException("Must provide a token");
		}
		return accountService.findByAPIToken(token);
	}
}
