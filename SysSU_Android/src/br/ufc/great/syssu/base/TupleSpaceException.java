package br.ufc.great.syssu.base;


@SuppressWarnings("serial")
public class TupleSpaceException extends Exception {

    public TupleSpaceException(String string) {
        super(string);
    }

	public TupleSpaceException(Throwable ex) {
		super(ex);
	}
    
}
