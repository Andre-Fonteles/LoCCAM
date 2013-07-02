package br.ufc.cac.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import br.ufc.cac.impl.DeviceAccelerationCAC;
import br.ufc.loccam.isensor.ISensor;

public class Activator implements BundleActivator{
	
	private DeviceAccelerationCAC cac = null;
	
	@SuppressWarnings("rawtypes")
	private ServiceRegistration serviceRegistration = null;
	
	/**
     * Implements BundleActivator.start(). Create a new instance of the 
     * MobileSensor and then use the BundleContext object to register it.
     * Creates a ServiceTracker object to monitor ISysSU services
     * @param context the framework context for the bundle.
    **/	
	public void start(BundleContext bundleContext) throws Exception {
		cac = new DeviceAccelerationCAC();
		
	    serviceRegistration = bundleContext.registerService(ISensor.class.getName(), cac, null);
	}

	/**
     * Implements BundleActivator.stop(). Store the registration object to use to 
     * unregister the service when the bundle is stopped by the framework.
     * @param context the framework context for the bundle.
    **/
	public void stop(BundleContext bundleContext) throws Exception {
		cac.stop();
		cac = null;
		
		if ( serviceRegistration != null ){
			serviceRegistration.unregister();		
		}
	}
}