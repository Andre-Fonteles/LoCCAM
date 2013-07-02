package br.ufc.great.syssu.base.interfaces;

import java.util.List;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;

public interface ILocalDomain extends IDomain {

	/*
	 * Interfaces com Filtro em Java
	 */
	public List<Tuple> read(Pattern pattern, IFilter restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> readSync(Pattern pattern, IFilter restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple readOne(Pattern pattern, IFilter restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple readOneSync(Pattern pattern, IFilter restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> take(Pattern pattern, IFilter restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> takeSync(Pattern pattern, IFilter restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple takeOne(Pattern pattern, IFilter restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple takeOneSync(Pattern pattern, IFilter restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;
}
