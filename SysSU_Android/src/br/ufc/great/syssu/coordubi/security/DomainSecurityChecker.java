package br.ufc.great.syssu.coordubi.security;

import java.util.HashMap;
import java.util.Map;

public class DomainSecurityChecker {
	
	private static DomainSecurityChecker instance;
	private Map<String, Map<String, String>> domainKeys;
	
	private DomainSecurityChecker() {
		domainKeys = new HashMap<String, Map<String,String>>();
	}
	
	public static DomainSecurityChecker getInstance() {
		if(instance == null) {
			instance = new DomainSecurityChecker();
		}
		return instance;
	}
	
	public boolean canRead(String domainName, String key) {
		Map<String, String> keys = domainKeys.get(domainName);
		return keys == null || keys.isEmpty()
			|| (keys.containsKey("put") && keys.get("put").equals(key))
			|| (keys.containsKey("take") && keys.get("take").equals(key))
			|| (keys.containsKey("read") && keys.get("read").equals(key));
	}
	
	public boolean canTake(String domainName, String key) {
		Map<String, String> keys = domainKeys.get(domainName);
		return keys == null || keys.isEmpty()
			|| (keys.containsKey("put") && keys.get("put").equals(key))
			|| (keys.containsKey("take") && keys.get("take").equals(key));
	}
	
	public boolean canPut(String domainName, String key) {
		Map<String, String> keys = domainKeys.get(domainName);
		return keys == null || keys.isEmpty()
			|| (keys.containsKey("put") && keys.get("put").equals(key));
	}

}
