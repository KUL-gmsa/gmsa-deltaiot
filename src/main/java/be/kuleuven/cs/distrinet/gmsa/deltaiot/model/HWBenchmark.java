package be.kuleuven.cs.distrinet.gmsa.deltaiot.model;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class HWBenchmark {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) private UUID id;
	private int adaptationModelId;
	private Instant startedAt;
	private boolean success;
	private String result;
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public int getAdaptationModelId() {
		return adaptationModelId;
	}
	
	public void setAdaptationModelId(int modelID) {
		this.adaptationModelId = modelID;
	}
	
	public Instant getStartedAt() {
		return startedAt;
	}
	
	public void setStartedAt(Instant startedAt) {
		this.startedAt = startedAt;
	}
	
	@Override
	public String toString() {
		return "Hardware benchmark " + getId() + " for model " + getAdaptationModelId();
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	@JsonIgnore
	public void setResultAndSuccess(String result, boolean success) {
		setResult(result);
		setSuccess(success);
	}
	
	public boolean isPending() {
		return startedAt == null;
	}
	
	@JsonIgnore
	public Duration getAge() {
		if (startedAt == null) {
			return null;
		}
		return Duration.between(startedAt, Instant.now());
	}
	
	@JsonIgnore
	public String getFriendlyAge() {
		var age = getAge();
		if (age.toDays() > 0) {
			return smartUnit(age.toDays(), "day");
		} else if (age.toHours() > 0) {
			return smartUnit(age.toHours(), "hour");
		} else if (age.toMinutes() > 0) {
			return smartUnit(age.toMinutes(), "minute");
		}
		return "just now";
	}
	
	private static String smartUnit(long amount, String base) {
		return amount + " " + base + (amount != 1 ? "s" : "") + " ago"; 
	}

}
