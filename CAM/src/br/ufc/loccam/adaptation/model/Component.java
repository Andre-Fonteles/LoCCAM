package br.ufc.loccam.adaptation.model;

import java.util.HashSet;
import java.util.Set;

public class Component {

	private String id;
	private String fileName;
	
	private Set<String> interestZone;
	private String contextProvided;

	private Set<Component> dependencies;
	private Set<Component> usedBy;
	
	private boolean application;
	
	public Component(String id, boolean application) {
		this(id, application, null);
	}
	
	public Component(String id, boolean application, String observableContext) {
		this.id = id;
		this.contextProvided = observableContext;
		this.application = application;
		
		dependencies = new HashSet<Component>();
		usedBy = new HashSet<Component>();
		
		interestZone = new HashSet<String>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Set<String> getInterestZone() {
		return interestZone;
	}

	public void addInterestZoneElement(String contextKey) {
		interestZone.add(contextKey);
	}
	
	public boolean removeInterestZoneElement(String contextKey) {
		return interestZone.remove(contextKey);
	}
	
	public String getContextProvided() {
		return contextProvided;
	}

	public void setContextProvided(String contextProvided) {
		this.contextProvided = contextProvided;
	}
	
	public void addDependencies(Component c) {
		dependencies.add(c);
	}
	
	public Set<Component> getDependencies() {
		return dependencies;
	}

	public void addUsedBy(Component c) {
		usedBy.add(c);
	}

	public Set<Component> getUsedBy() {
		return usedBy;
	}
	
	public void setApplication(boolean application) {
		this.application = application;
	}
	
	public boolean isApplication() {
		return application;
	}
}
