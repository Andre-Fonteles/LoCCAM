package br.ufc.great.syssu.base.interfaces;

import java.util.List;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;

public interface IDomain extends IDomainComposite {

	public String getName();
	
	public void put(Tuple tuple, String key) 
		throws TupleSpaceException, TupleSpaceSecurityException;

	/*
	 *  Interfaces com filter em JavaScript
	 */
	
	public List<Tuple> read(Pattern pattern, String restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> readSync(Pattern pattern, String restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple readOne(Pattern pattern, String restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple readOneSync(Pattern pattern, String restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> take(Pattern pattern, String restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public List<Tuple> takeSync(Pattern pattern, String restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple takeOne(Pattern pattern, String restriction, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Tuple takeOneSync(Pattern pattern, String restriction, String key, long timeout) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public Object subscribe(IReaction reaction, String event, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;

	public void unsubscribe(Object reactionId, String key) 
			throws TupleSpaceException, TupleSpaceSecurityException;
}
