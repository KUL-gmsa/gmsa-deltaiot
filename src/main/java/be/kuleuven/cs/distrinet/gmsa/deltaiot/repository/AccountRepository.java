package be.kuleuven.cs.distrinet.gmsa.deltaiot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.model.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

	public Optional<Account> findAccountByApiToken(String token);
}