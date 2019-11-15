package be.kuleuven.cs.distrinet.gmsa.deltaiot.model;

import java.time.Instant;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.util.Crypto;

@Entity
public class Account {

	private @Id String username;
	private String email;
	private Instant createdAt;
	private Instant lastLogin;

	private boolean isAdmin = false;
	private boolean hardwareAuthorized = false;
	private Instant hardwareAuthorizationDecision = null;
	private String encryptedPassword;
	@Column(unique = true) private String apiToken;

	Account() {
	}

	public Account(String username, String email, String password) {
		this(username, email, password, false);
	}
	
	public Account(String username, String email, String password, boolean isAdmin) {
		this.username = username;
		this.email = email; 
		this.isAdmin = isAdmin;
		setPassword(password);
	}
	
	// Seeded to return same token on each start
	private static final Random apiTokenRandom = new Random(123);
	
	public void generateAPIToken() {
		apiToken = getUsername() + "_" + DigestUtils.sha1Hex(getUsername() + apiTokenRandom.nextInt(100)).substring(0, 10);
	}
	
	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private static final String PASSWORD_ENCRYPTION_KEY = "3NCRyP710nK3Y";

	@JsonIgnore
	public String getPassword() {
		return Crypto.decrypt(encryptedPassword, PASSWORD_ENCRYPTION_KEY);
	}
	
	@JsonIgnore
	public void setPassword(String password) {
		this.encryptedPassword = Crypto.encrypt(password, PASSWORD_ENCRYPTION_KEY);
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}
	
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public boolean isHardwareAuthorized() {
		return hardwareAuthorized;
	}

	public void setHardwareAuthorized(boolean hardwareAuthorized) {
		this.hardwareAuthorized = hardwareAuthorized;
	}

	public String getApiToken() {
		return apiToken;
	}

	public void setApiToken(String token) {
		this.apiToken = token;
	}
	
	public Instant getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public Instant getHardwareAuthorizationDecision() {
		return hardwareAuthorizationDecision;
	}
	
	public void setHardwareAuthorizationDecision(Instant instant) {
		this.hardwareAuthorizationDecision = instant;
	}
	
	public boolean isPendingAuthorization() {
		return getHardwareAuthorizationDecision() == null;
	}
	
	
	@Override
	public String toString() {
		return "Account " + username + " (" + email + ") " + (isAdmin ? " [admin]" : "");
	}

	public static Account create(String username, String email, String password, boolean isAdmin) {
		var result = new Account(username, email, password, isAdmin);
		result.setCreatedAt(Instant.now());
		result.generateAPIToken();
		return result;
	}


}