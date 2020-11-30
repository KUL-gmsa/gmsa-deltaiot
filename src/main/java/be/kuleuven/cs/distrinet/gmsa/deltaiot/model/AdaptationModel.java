package be.kuleuven.cs.distrinet.gmsa.deltaiot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI;
import be.kuleuven.cs.distrinet.gmsa.deltaiot.util.InMemoryCompiler;

@Entity
public class AdaptationModel {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) private int id;
	private String owner;
	private String name;
	private String filename;
	@Lob private String code;
	private String classname;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String toString() {
		return String.format("Model %d (%s, %s) owned by %s", id, name, filename, owner);
	}

	public String getClassName() {
		return this.classname;
	}
	
	public void setClassName(String classname) {
		this.classname = classname;
	}
	
	public AdaptationAPI compileAndCreateInstance() throws Exception {
		return InMemoryCompiler.compileAndCreateInstance(this);
	}
	
	

}
