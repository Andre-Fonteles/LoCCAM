package br.ufc.loccam.cacmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;

import org.apache.felix.framework.Felix;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import android.content.Context;
import android.os.FileObserver;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import br.ufc.great.loccam.util.FelixConfig;
import br.ufc.loccam.adaptation.model.Component;
import br.ufc.loccam.iandroidcontextprovider.AndroidContextProvider;
import br.ufc.loccam.ipublisher.IPublisher;
import br.ufc.loccam.ipublisher.Publisher;
import br.ufc.loccam.isensor.ISensor;

public class CACManager implements ICACManager {

	private static CACManager instance = null;
	
	private Felix felix;
	
	@SuppressWarnings("rawtypes")
	private ServiceTracker serviceTracker;

    private File availableBundlesDir; 
    private File cacheDir;

    private IPublisher sysSU;

    private Context context;
    
 	private Map<String, List<Component>> availableCACs;
 	
 	// File observer que verifica quais CACs estão disponíveis no diretório dos CACs
 	private FileObserver fileObserver;
 	
	private String TAG = "CACManager";
	
	public static CACManager getInstance(Context context) {
		if(instance == null)
			instance = new CACManager(context);
		
		return instance;
	}
	
	private CACManager(Context context) {
		this.context = context;
		
		String SDCardsPath = "/sdcard";
		
		// Creates felix cache dir
        cacheDir = new File( SDCardsPath + FelixConfig.CACHE_PATH );
        if (!cacheDir.exists()) {
        	if (!cacheDir.mkdirs()) {
        		throw new IllegalStateException("Unable to create felixcache dir");
        	}
        }
        
        try{
        	// Loads Properties from class FelixConfig
    		Properties felixProperties = new FelixConfig(SDCardsPath).getConfigProps();
            // Creates an instance of the framework with our configuration properties.
            felix = new Felix(felixProperties);
            // Starts Felix instance.
            felix.start();
            
            // Starts a service listener
            initServiceTracker();
        }
        catch (Exception ex){
            System.out.println("Could not create framework: " + ex);
            ex.printStackTrace();
        }
		
		sysSU = new Publisher();
		
		availableCACs = new HashMap<String, List<Component>>();
		
        // Configura o diretório usado como repositório de CACs e lê todos os CACs disponíveis
        availableBundlesDir = new File(SDCardsPath + FelixConfig.AVAILABLE_BUNDLES_PATH);
        if (!availableBundlesDir.exists()) {
        	if (!availableBundlesDir.mkdirs()) {
        		throw new IllegalStateException("Unable to create availableBundlesDir dir");
        	}
        } else {
        	discoverCACsFromLocalRepositoty();
        }
        
        fileObserver = new FileObserver(availableBundlesDir.getAbsolutePath()) {
            @Override
            public void onEvent(int event, String file) {
                if(event == FileObserver.CLOSE_WRITE){
                	Log.e(TAG, "New CAC Added" + file);
                	Component addedCac = readJar(new File(availableBundlesDir.getAbsolutePath() + "/" +file));
                	addAvailableCAC(addedCac);
                }
            }
        };
        
        fileObserver.startWatching();
	}

	public void startCAC(Component cac) {
		Log.d(TAG, "starting CAC " + cac.getId());
		
		Bundle bundle = findBundle(cac);
		
		// Verifica se foi encontrado um bundle com aquele SymbolicName
		if (bundle == null) {
			Log.e(TAG, "can't find CAC " + cac.getId());
			return;
		}
		
		try {
			// Inicia o bundle de fato
			bundle.start();
			
			Log.d(TAG, "CAC " + bundle.getSymbolicName() + "/" + bundle.getBundleId() + "/" + bundle + " started");
		} catch (BundleException be) {
			Log.e(TAG, be.toString());
		}
	}

	public void stopCAC(Component cac) {
		Log.d(TAG, "stopping CAC " + cac.getId());
		
		Bundle bundle = findBundle(cac);
		
		// Verifica se foi encontrado um bundle com aquele Id
		if (bundle == null) {
			Log.e(TAG, "can't find CAC " + cac.getId());
			return;
		}
		
		try {
			// Inicia o bundle de fato
			bundle.stop();
			
			Log.d(TAG, "CAC " + bundle.getSymbolicName() + "/" + bundle.getBundleId() + "/" + bundle + " stopped");
		} catch (BundleException be) {
			Log.e(TAG, be.toString());
		}
	}
	
	public void installCAC(Component cac) {
		Log.d(TAG, "installing CAC " + cac.getFileName());
		
		InputStream inputStream;
		
		// Abre um inputStream do bundle
		try {
			inputStream = new FileInputStream(availableBundlesDir.getAbsolutePath() + "/" + cac.getFileName());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			return;
		}
		
		// Instala o bundle de fato
		try {
			felix.getBundleContext().installBundle(cac.getFileName(), inputStream);
			
			Log.d(TAG, "CAC " + cac.getFileName() + " installed");
		} catch (BundleException be) {
			Log.e(TAG, be.toString());
			be.printStackTrace();
		}
		
		// Fecha o inputStream do bundle
		try {
			inputStream.close();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} 
	}
	
	public void uninstallCAC(Component cac) {
		Log.d(TAG, "unistalling CAC " + cac.getId());
		
		Bundle bundle = findBundle(cac);
		
		// Verifica se foi encontrado um bundle com aquele Id
		if (bundle == null) {
			Log.e(TAG, "can't find CAC " + cac.getId());
			return;
		}
		
		try {
			// Inicia o bundle de fato
			bundle.uninstall();
			
			Log.d(TAG, "CAC " + bundle.getSymbolicName() + "/" + bundle.getBundleId() + "/" + bundle + " uninstalled");
		} catch (BundleException be) {
			Log.e(TAG, be.toString());
		}
	}
	
	public Map<String, List<Component>> getListOfAvailableCACs() {
		return availableCACs;
	}
	
	/**
	 * Método que garante que a instância de ISensor dentro de um bundle OSGi é iniciada juntamente com o bundle.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initServiceTracker(){
		
		// Cria um listener que habilita services de bundles que forem iniciados
		try {
			Filter filter = felix.getBundleContext().createFilter("(" + Constants.OBJECTCLASS + "=" + ISensor.class.getName() + ")");

			serviceTracker = new ServiceTracker(felix.getBundleContext(), filter, new ServiceTrackerCustomizer() {

						private ISensor sensor;
				
						public Object addingService(ServiceReference ref) {
							sensor = (ISensor) felix.getBundleContext().getService(ref);
							
						    if (sensor != null) {
						    	new Thread( new Runnable() {
									public void run() {
										Looper.prepare();
										sensor.start( new AndroidContextProvider(context) , sysSU);
										Looper.loop();
									}
								}).start();									
							}	
							return sensor;
						}

						public void modifiedService(ServiceReference ref, Object service) {
							removedService(ref, service);
							addingService(ref);
						}

						public void removedService(ServiceReference ref, Object service) {
							felix.getBundleContext().ungetService(ref);
						}
					});
			
			serviceTracker.open();
			
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método que verifica todos os CACs disponíveis no diretório de repositório local e os adiciona à lista availableCACs
	 */
	private void discoverCACsFromLocalRepositoty() {
		File[] files = availableBundlesDir.listFiles();
    	
		Component cac;
		
		for (File file : files) {
    		cac = readJar(file);
			
			addAvailableCAC(cac);
		}
		
	}
	
	private void addAvailableCAC(Component cac) {
		// Verifica se já existe uma lista com componentes desse tipo e cria uma caso não exista
		List<Component> componentes = availableCACs.get(cac.getContextProvided());
		if(componentes == null) {
			componentes = new ArrayList<Component>();
			availableCACs.put(cac.getContextProvided(), componentes);
		}
		
		// Adiciona o CAC a lista de componentes que proveem o mesmo tipo de contexto
		componentes.add(cac);
		
		// Instala o bundle do CAC caso ele ainda não esteja instalado
		Bundle bundle = findBundle(cac);
		if(bundle == null) {
			installCAC(cac);
		}
	}
	
	private Component readJar(File file) {
		Component cac = null;
		String contextKey;
		String symbolicName;
		String interestZone;
		String fileName;
		
		try {
			JarFile jarFile = new JarFile(file);
			
			contextKey = jarFile.getManifest().getMainAttributes().getValue("Context-Provided");
			symbolicName = jarFile.getManifest().getMainAttributes().getValue("Bundle-SymbolicName");
			interestZone = jarFile.getManifest().getMainAttributes().getValue("Interest-Zone");
			
			fileName = file.getName();
			
			cac = new Component(symbolicName, false, contextKey);
			cac.setFileName(fileName);
			
			if(interestZone != null)
				for (String interestElement : interestZone.split(",")) {
					cac.addInterestZoneElement(interestElement);
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cac;
	}
	
	private Bundle findBundle(Component cac) {
		Bundle[] bundles = felix.getBundleContext().getBundles();
		long bundleId = -1;
		
		for (int i = 0; bundles != null && i < bundles.length; i++) {
			if (cac.getId().equals(bundles[i].getSymbolicName())) {
				bundleId = bundles[i].getBundleId();
			}
		}
		
		Bundle bundle = felix.getBundleContext().getBundle(bundleId);
		
		return bundle;
	}
	
	/*
	 *  TESTE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 */
	public void configurationTest(View v) {
		Bundle[] bundles = felix.getBundleContext().getBundles();
		
		System.out.println("bundles: " + bundles);
		System.out.println("bundles size: " + bundles.length);
		
		for (Bundle bundle : bundles) {
			System.out.println("Bundle: " + bundle.getSymbolicName() + " - " + bundle.getState());
		}
	}
	
	public String printBundlesState() {
		String r = "";
		Bundle[] bundles = felix.getBundleContext().getBundles();

		Log.i("ADAPT TEST", "-----------------------------------------------------------------------------------------");
		Log.i("ADAPT TEST", "[[ACTUAL BUNDLES STATE]]");
		r = "[[ACTUAL BUNDLES STATE]]\n";
		for (Bundle bundle : bundles) {
			Log.i("ADAPT TEST", (bundle.getSymbolicName() + ", CONTEXT_DATA:" + bundle.getHeaders().get("Context-Data") + " - " + bundle.getState()));
			r += bundle.getSymbolicName() + ", CONTEXT_DATA:" + bundle.getHeaders().get("Context-Data") + " - " + bundle.getState() + "\n";
		}
		
		return r;
	}
}
