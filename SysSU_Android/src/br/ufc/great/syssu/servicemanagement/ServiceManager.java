package br.ufc.great.syssu.servicemanagement;

import java.util.*;

public class ServiceManager {

	private static ServiceManager instance;
	private Map<String, IService> services;
	private Map<String, IService> eventServices;

	private ServiceManager() {
		services = new HashMap<String, IService>();
		eventServices = new HashMap<String, IService>();
	}

	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}

	public void addService(IService service) {
		if (service != null) {
			String name = service.getName();
			if (name == null || name.equals("")) {
				throw new IllegalArgumentException("No service name defined.");
			}
			if (services.containsKey(name)) {
				services.remove(service.getName());
			}
			services.put(name, service);
		} else {
			throw new IllegalArgumentException("No service defined.");
		}
	}

	public void addEventService(IService service) {
		if (service != null) {
			String name = service.getName();
			if (name == null || name.equals("")) {
				throw new IllegalArgumentException("No event service name defined.");
			}
			if (eventServices.containsKey(name)) {
				services.remove(service.getName());
			}
			eventServices.put(name, service);
		} else {
			throw new IllegalArgumentException("No service defined.");
		}
	}

	public void removeService(String name) {
		if (name != null && !name.equals("") && services.containsKey(name)) {
			services.remove(name);
		}
	}

	public void removeEventService(String name) {
		if (name != null && !name.equals("") && eventServices.containsKey(name)) {
			eventServices.remove(name);
		}
	}

	public void removeService(IService service) {
		if (service != null) {
			String name = service.getName();
			if (name == null || name.equals("")) {
				throw new IllegalArgumentException("No service name defined.");
			}
			removeService(name);
		}
	}

	public void removeEventService(IService service) {
		if (service != null) {
			String name = service.getName();
			if (name == null || name.equals("")) {
				throw new IllegalArgumentException("No event service name defined.");
			}
			removeEventService(name);
		}
	}

	public IService getService(String name) {
		if (name != null && services.containsKey(name)) {
			return services.get(name);
		}
		return null;
	}

	public IService getEventService(String name) {
		if (name != null && eventServices.containsKey(name)) {
			return eventServices.get(name);
		}
		return null;
	}
}
