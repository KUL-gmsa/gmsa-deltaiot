package be.kuleuven.cs.distrinet.gmsa.deltaiot.service;

import java.util.Collection;
import java.util.Optional;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;

public interface AccountService {

	Collection<Account> listAllAccounts();

	Account addHardwareAuthorization(String username);

	Account findAccountById(String username);
	
	Optional<Account> findByAPIToken(String token);

	Account removeHardwareAuthorization(String username);

	Account updateAccountInformation(Account updatedAccount);

	Account createNewAccount(Account newAccount);

	Account recordLogin(String username);

	String refreshApiToken(String username);

	Collection<Account> getAccountsPendingApproval();

	boolean isAvailable(String username);

	boolean checkAuthorization(String username);

	Account makeAdmin(String username, boolean admin);

}
