package be.kuleuven.cs.distrinet.gmsa.deltaiot.api.v1;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.UnauthorizedException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;

/**
 * REST API for the accounts
 * @author koeny
 *
 */
@RestController
@RequestMapping(AccountRESTController.PATH)
public class AccountRESTController extends AbstractBaseRESTController {
	
	public static final String PATH = "/api/v1/account";
	
	@Autowired
	private AccountService accountService;
	
    @GetMapping
    public Collection<Account> getAllAccounts(@RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);  	
    	return accountService.listAllAccounts();
    }

    @GetMapping("{username}")
    public Account getAccountInfo(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String token) {
    	return accountService.findAccountById(username);
    }

    @PutMapping("{username}")
    public Account updateAccountInfo(@RequestBody Account updatedAccountInformation, @PathVariable("username") String pathUsername, @RequestHeader(TOKEN_HEADER_NAME) String token) {
    	var accountFromToken = validateApiToken(token);
    	if (!accountFromToken.isAdmin() && !accountFromToken.getUsername().contentEquals(pathUsername)) {
    		throw new UnauthorizedException("Can only update own account");
    	}
    	// make sure the provided information refers to the user from the URL, so users cannot update other users
    	updatedAccountInformation.setUsername(pathUsername);
    	return accountService.updateAccountInformation(updatedAccountInformation);
    }
    
    @GetMapping("{username}/availability")
    public String checkAvailability(@PathVariable String username) {
    	return Boolean.toString(accountService.isAvailable(username));
    }

    @PostMapping("{username}/refreshToken")
    public String refreshToken(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String token) {
    	validateApiToken(token);
    	return accountService.refreshApiToken(username);
    }
    
    @GetMapping("{username}/hardware")
    public String checkHardwareAuthorization(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String token) {
    	validateApiToken(token);
    	return Boolean.toString(accountService.checkAuthorization(username));
    }
    
    @PutMapping("{username}/hardware")
    public Account addHardwareAuthorization(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);
    	return accountService.addHardwareAuthorization(username);
    }
    
	@DeleteMapping("{username}/hardware")
    public Account removeHardwareAuthorization(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);
    	return accountService.removeHardwareAuthorization(username);
    }

	@GetMapping("{username}/admin")
    public String isAdmin(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);
    	var account = accountService.findAccountById(username);
    	if (account != null) {
    		return Boolean.toString(account.isAdmin());
    	}
    	throw new NotFoundException("No user with username " + username);
    }

	@PutMapping("{username}/admin")
    public Account makeAdmin(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);
    	return accountService.makeAdmin(username, true);
    }
    
	@DeleteMapping("{username}/admin")
    public Account removeAdmin(@PathVariable String username, @RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
    	validateAdminApiToken(adminToken);
    	return accountService.makeAdmin(username, false);
    }

	@GetMapping("pending")
	public Collection<Account> getAllPending(@RequestHeader(TOKEN_HEADER_NAME) String adminToken) {
		validateAdminApiToken(adminToken);
		return accountService.getAccountsPendingApproval();
	}
    
}