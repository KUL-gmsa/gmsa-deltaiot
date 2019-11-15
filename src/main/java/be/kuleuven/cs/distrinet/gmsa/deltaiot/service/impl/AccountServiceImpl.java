package be.kuleuven.cs.distrinet.gmsa.deltaiot.service.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.controlleradvice.NotFoundException;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.repository.AccountRepository;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;

	AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Collection<Account> listAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public boolean isAvailable(String username) {
		return findAccountById(username) == null;
	}

	@Override
	public Collection<Account> getAccountsPendingApproval() {
		return listAllAccounts().stream().filter(a -> a.isPendingAuthorization()).collect(Collectors.toList());
	}

	@Override
	public Account findAccountById(String username) {
		return accountRepository.findById(username).orElse(null);
	}

	@Override
	public Optional<Account> findByAPIToken(String token) {
		return accountRepository.findAccountByApiToken(token);
	}

	@Override
	public Account addHardwareAuthorization(String username) {
		Account account = accountRepository.findById(username)
				.orElseThrow(() -> new NotFoundException("No account with username " + username));
		account.setHardwareAuthorized(true);
		account.setHardwareAuthorizationDecision(Instant.now());
		accountRepository.save(account);
		return account;
	}

	@Override
	public Account removeHardwareAuthorization(String username) {
		Account account = accountRepository.findById(username)
				.orElseThrow(() -> new NotFoundException("No account with username " + username));
		account.setHardwareAuthorized(false);
		account.setHardwareAuthorizationDecision(Instant.now());
		accountRepository.save(account);
		return account;
	}
	
	@Override
	public Account makeAdmin(String username, boolean admin) {
		Account account = accountRepository.findById(username)
				.orElseThrow(() -> new NotFoundException("No account with username " + username));
		account.setAdmin(admin);
		accountRepository.save(account);
		return account;
	}
	
	@Override
	public boolean checkAuthorization(String username) {
		Account account = accountRepository.findById(username)
				.orElseThrow(() -> new NotFoundException("No account with username " + username));
		return account.isHardwareAuthorized();
	}

	@Override
	public Account createNewAccount(Account info) {
		Account newAccount = Account.create(info.getUsername(), info.getEmail(), info.getPassword(), info.isAdmin());
		return accountRepository.save(newAccount);
	}

	@Override
	public Account updateAccountInformation(Account updatedAccount) {
		return accountRepository.save(updatedAccount);
	}

	@Override
	public Account recordLogin(String username) {
		var account = findAccountById(username);
		if (account != null) {
			account.setLastLogin(Instant.now());
		}
		return account;
	}

	@Override
	public String refreshApiToken(String username) {
		Account account = accountRepository.findById(username)
				.orElseThrow(() -> new NotFoundException("No account with username " + username));
		account.generateAPIToken();
		accountRepository.save(account);
		return account.getApiToken();
	}

}
