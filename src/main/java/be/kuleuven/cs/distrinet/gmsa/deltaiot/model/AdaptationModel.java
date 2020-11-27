package be.kuleuven.cs.distrinet.gmsa.deltaiot.model;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.AdaptationAPI;

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
		String code = this.getCode();
		File sourceFile = new File("/tmp/compileSimulation/" + this.getFilename());
		sourceFile.getParentFile().mkdirs();
		Files.write(sourceFile.toPath(), code.getBytes(StandardCharsets.UTF_8));

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		compiler.run(null, null, null, sourceFile.getPath());

		URLClassLoader classLoader = new URLClassLoader(
				new URL[] { new File("/tmp/compileSimulation").toURI().toURL() }, AdaptationAPI.class.getClassLoader());
		Class<? extends AdaptationAPI> cls = Class.forName(this.getClassName(), true, classLoader)
				.asSubclass(AdaptationAPI.class);
		AdaptationAPI instance = cls.getDeclaredConstructor().newInstance();
		return instance;
	}
	
	

}
