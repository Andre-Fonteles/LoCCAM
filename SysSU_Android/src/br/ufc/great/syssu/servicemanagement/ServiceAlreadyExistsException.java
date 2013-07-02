package br.ufc.great.syssu.servicemanagement;

@SuppressWarnings("serial")
public class ServiceAlreadyExistsException extends Exception {
    
    private IService service;

    public ServiceAlreadyExistsException(String message) {
        super(message);
    }

    public ServiceAlreadyExistsException(String message, IService service) {
        super(message);
        this.service = service;
    }

    public IService getService() {
        return service;
    }
    
}

