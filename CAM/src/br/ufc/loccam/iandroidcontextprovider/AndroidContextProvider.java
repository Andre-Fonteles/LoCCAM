package br.ufc.loccam.iandroidcontextprovider;

import android.content.Context;

public class AndroidContextProvider implements IAndroidContextProvider {
	
	private Context context;
	
	public AndroidContextProvider(Context context){
		this.context = context;
	}

	public Object getContext() {
		return context;
	}

}
