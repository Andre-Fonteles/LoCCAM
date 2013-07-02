package br.ufc.great.syssu.base;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import br.ufc.great.syssu.base.interfaces.IMatcheable;

public class Tuple extends AbstractFieldCollection<TupleField> implements IMatcheable, Parcelable, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long timeToLive;
	private long putTime;

	public Tuple() {
		super();
	}

	public Tuple(long timeToLive) {
		super();
		setTimeToLive(timeToLive);
	}

	@Override
	public TupleField createField(String name, Object value) {
		return new TupleField(name, value);
	}

	@Override
	public boolean matches(Query query) throws FilterException {
		if(query == null || query.getPattern() == null  || query.getPattern().isEmpty() || this.isEmpty()) {
			return true;
		}
		
		if(query.getJavaScriptFilter() != null && !query.getJavaScriptFilter().isEmpty()) {
			return associatesAll(query.getPattern()) && TupleFilter.doFilter(this, query.getJavaScriptFilter());
		} else {
			return associatesAll(query.getPattern()) && TupleFilter.doFilter(this, query.getJavaFilter());
		}
		
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		if(timeToLive < 0) throw new InvalidParameterException("Negative time");
		this.timeToLive = timeToLive;
	}

	public long getPutTime() {
		return putTime;
	}

	public void setPutTime(long putTime) {
		if(putTime < 0) throw new InvalidParameterException("Negative time");
		this.putTime = putTime;
	}

	// If timeToLive is not defined in the constructor, its value is zero and the tuple will always be alive. 
	// Otherwise, is calculated how much time the tuple was inserted.
	public boolean isAlive() {
		return timeToLive == 0 || System.currentTimeMillis() - putTime < timeToLive;
	}

	private boolean associatesAll(Pattern pattern) {
		boolean matches = true;

		for (PatternField pField : pattern) {
			matches = (matches) ? associatesOne(pField) : false;
		}
		return matches;
	}

	private boolean associatesOne(PatternField pField) {
		for (TupleField tField : this) {
			if (tField.associates(pField)) {
				return true;
			}
		}
		return false;
	}
	
	public void writeToParcel(Parcel parcel, int flags) {
		
		parcel.writeInt(size());
		
		for (int i = 0; i < size(); i++) {
			TupleField tf = getField(i);
			
			if (tf.getType().equals("?boolean")) {
				parcel.writeString("?boolean");
				parcel.writeString(tf.getName());
				parcel.writeString(tf.getValue().toString());
	        } else if (tf.getType().equals("?integer")) {
				parcel.writeString("?integer");
				parcel.writeString(tf.getName());
				parcel.writeInt((Integer)tf.getValue());
	        } else if (tf.getType().equals("?float")) {
	        	parcel.writeString("?float");
				parcel.writeString(tf.getName());
				parcel.writeFloat((Float)tf.getValue());
	        } else if (tf.getType().equals("?string")) {
	        	parcel.writeString("?string");
				parcel.writeString(tf.getName());
				parcel.writeString(tf.getValue().toString());
	        } else if (tf.getType().equals("?object")) {
	        	parcel.writeString("?object");
				parcel.writeString(tf.getName());
				parcel.writeParcelable((Tuple)tf.getValue(), flags);
	        } else if (tf.getType().equals("?array")) { // Suporta apenas array de Strings e de no maximo lengh = 10
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>)tf.getValue();
	        	String [] array = new String[list.size()];
	        	
	        	for(int j = 0; j < list.size(); j ++)
	        		array[j] = list.get(j);
	        	
	        	parcel.writeString("?array");
				parcel.writeString(tf.getName());
				parcel.writeInt(array.length);
				parcel.writeStringArray(array);
	        }	
		}
	}
	
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Tuple> CREATOR = new Parcelable.Creator<Tuple>() {
		public Tuple createFromParcel(Parcel parcel) {
			Tuple t = new Tuple();
			
			int size = parcel.readInt();
			
			String type;
			String name;
			boolean bValue;
			int iValue;
			float fValue;
			String sValue;
			Tuple tValue;
			String[] lValue;
			
			for (int i = 0; i < size; i++) {
				type = parcel.readString();
				name = parcel.readString();
				
				if (type.equals("?boolean")) {
					bValue = new Boolean(parcel.readString());
					t = (Tuple) t.addField(name, bValue);
		        } else if (type.equals("?integer")) { 
		        	iValue = parcel.readInt();
					t = (Tuple) t.addField(name, iValue);
		        } else if (type.equals("?float")) {
		        	fValue = parcel.readFloat();
					t = (Tuple) t.addField(name, fValue);
		        } else if (type.equals("?string")) {
		        	sValue = parcel.readString();
					t = (Tuple) t.addField(name, sValue);
		        } else if (type.equals("?object")) {
		        	tValue = (Tuple)parcel.readParcelable(Tuple.class.getClassLoader());
					t = (Tuple) t.addField(name, tValue);
		        } else if (type.equals("?array")) { // Suporta apenas array de Strings
		        	lValue = new String[parcel.readInt()];
		        	parcel.readStringArray(lValue);
		        	t = (Tuple) t.addField(name, Arrays.asList(lValue));
		        }	
				
			}
			
			return t;
		}

		public Tuple[] newArray(int size) {
			return new Tuple[size];
		}
	};
}
