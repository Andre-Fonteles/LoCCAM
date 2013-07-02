package br.ufc.great.syssu.base;

import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import android.os.RemoteException;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.utils.JSONTuple;

public class TupleFilter {
	
	// Realiza o filtro usando JavaScript
	public static boolean doFilter(Tuple tuple, String filter) {
		if (tuple != null) {
			if (filter != null && !filter.equals("")) {
				// Create an execution environment.
		        Context cx = Context.enter();
		        // Turn compilation off.
		        cx.setOptimizationLevel(-1);

		        try 
		        {
		            Scriptable scope = cx.initStandardObjects();

		            Object wrappedOut = Context.javaToJS(new JSONTuple(tuple).getJSON(), scope);
		            ScriptableObject.putProperty(scope, "json", wrappedOut);
		            
					cx.evaluateString(
							scope,
							"tuple = eval('(' + json + ')')",
							"filter:", 1, null); 
					
					cx.evaluateString(
							scope, 
							filter,
							"filter:", 1, null);
					// Código para acesso ao arquivo dentro do JAR
					cx.evaluateReader(
							scope, 
							new InputStreamReader(TupleFilter.class.getClassLoader().getResourceAsStream("json2.js")),
							"filter:", 1, null);
					
					Object result = cx.evaluateString( 
							scope, 
							"filter(tuple);",
							"filter:", 1, null);

					return Boolean.valueOf(Context.toString(result));
				} catch (Exception e) {
					e.printStackTrace();
				}   
		        finally  
		        {
		            Context.exit();
		        }
			}
			return true;
		}
		return false;
	}
	
	// Realiza o filtro usando Java
	public static boolean doFilter(Tuple tuple, IFilter filter) {
		if (tuple != null) {
			if (filter != null) {
				try {
					return filter.filter(tuple);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
}
