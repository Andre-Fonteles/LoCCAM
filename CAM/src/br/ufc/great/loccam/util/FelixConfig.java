package br.ufc.great.loccam.util;

import java.util.Collection;
import java.util.Properties;

import org.twdata.pkgscanner.ExportPackage;
import org.twdata.pkgscanner.PackageScanner;

/**
 * Because it is somehow hard to use conf/config.properties file in android, 
 * I build up properties object hard coded for now
 * @author romulogmlima
 */

public class FelixConfig {

	public static final String AVAILABLE_BUNDLES_PATH = "/felix/availablebundles"; 
	public static final String NEWE_BUNDLES_PATH = "/felix/newbundle"; 
    public static final String CACHE_PATH = "/felix/cache";
	
	private Properties configProps;
	
	public FelixConfig(String absFilePath){
		
		// Analyze the classpath / classloader 
		analyzeClassPath();

		configProps = new Properties();
		
		//org.osgi.framework.storage=${felix.cache.rootdir}/felixcache
		configProps.put("org.osgi.framework.storage", absFilePath + CACHE_PATH);
		
		// felix.cache.rootdir=${user.dir} 
		configProps.put("felix.cache.rootdir", absFilePath + "/felix");
		
		// fileinstall watch dir
		configProps.put("felix.fileinstall.dir", absFilePath + NEWE_BUNDLES_PATH); 
		
		//"felix.fileinstall.dir";
		configProps.put("felix.fileinstall.debug", "1"); //"felix.fileinstall.debug";
		
		// Exports packages to provide them for the bundles
		configProps.put("org.osgi.framework.system.packages.extra", ANDROID_FRAMEWORK_PACKAGES_ext);
		
		// felix.log.level=4
		configProps.put("felix.log.level", "4");
		
		// felix.startlevel.bundle=1
		configProps.put("felix.startlevel.bundle", "1");
		
		// org.osgi.service.http.port=-1
		// A negative port number has the same effect as setting org.apache.felix.http.enable to false
		configProps.put("org.osgi.service.http.port", "-1");
		
	}

	public Properties getConfigProps() {
		return configProps;
	}
	
	// package scanner
	private void analyzeClassPath(){
		
		PackageScanner pkgScanner = new PackageScanner();
		
		// set usage of classloader to avoid NPE in internal scanner of PackageScanner
		pkgScanner.useClassLoader(PackageScanner.class.getClassLoader().getParent());
		//FelixConfig.class.getClassLoader()   ClassLoader.getSystemClassLoader()
		//Collection<ExportPackage> exports = pkgScanner.scan();
		
        Collection<ExportPackage> exports = pkgScanner
        .select
        (
        	PackageScanner.jars(
            		PackageScanner.include
            		(
                            "*.jar"),
                            PackageScanner.exclude(
                            "felix.jar",
                            "package*.jar")
                    ),
                            
            PackageScanner.packages(
            		PackageScanner.include
            		(
                            "org.*", "com.*",
                            "javax.*", "android",
                            "android.*", "com.android.*",
                            "dalvik.*", "java.*",
                            "junit.*", "org.apache.*",
                            "org.json", "org.xml.*",
                            "org.xmlpull.*", "org.w3c.*")
                    )
        ).scan();
		
        System.out.println("HIER: "+exports.size());
        // now fill analyzedExportString
        while (exports.iterator().hasNext()){
        	System.out.println("exports: "+ exports.iterator().next().getPackageName());
        }
		
	}
	
	private static final String ANDROID_FRAMEWORK_PACKAGES_ext = (
			"org.osgi.framework; version=1.4.0," +
            "org.osgi.service.packageadmin; version=1.2.0," +
            "org.osgi.service.startlevel; version=1.0.0," +
            "org.osgi.service.url; version=1.0.0," +
            "org.osgi.util.tracker," +
            
            // ANDROID (here starts semicolon as separator -> Why?
            "android; " + 
            "android.app;" + 
            "android.content;" + 
            "android.database;" + 
            "android.database.sqlite;" + 
            "android.graphics; " + 
            "android.graphics.drawable; " + 
            "android.graphics.glutils; " + 
            "android.hardware; " + 
            "android.location; " + 
            "android.media; " + 
            "android.net; " + 
            "android.opengl; " + 
            "android.os; " + 
            "android.provider; " + 
            "android.sax; " + 
            "android.speech.recognition; " + 
            "android.telephony; " + 
            "android.telephony.gsm; " + 
            "android.text; " + 
            "android.text.method; " + 
            "android.text.style; " + 
            "android.text.util; " + 
            "android.util; " +  
            "android.view; " + 
            "android.view.animation; " + 
            "android.webkit; " + 
            "android.widget;" +
            "android.media; " + 
            "android.util; " +
            
			"org.apache.http; " + 
			"org.apache.http.client; " + 
			"org.apache.http.impl.client; " +
			"org.apache.http.client.methods; " + 

            // Android OS Version?? ->her ends semicolon as seperator -> Why?
            "version=1.5.0.r3," +
            
            // MY OWN
			"br.ufc.loccam.iandroidcontextprovider," + 
            "br.ufc.loccam.isensor," +
            "br.ufc.great.syssu.base," +
            "br.ufc.great.syssu.base.interfaces," + 
			"br.ufc.loccam.ipublisher"
            
			).intern();
}
