package br.ufc.great.syssu.servicemanagement.services;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;
import br.ufc.great.syssu.base.interfaces.IDomain;

public class TakeOneService extends AbstractQueryService {

    @Override
    public String getName() {
        return "takeone";
    }

	@Override
	protected List<Tuple> query(IDomain domain, Pattern pattern, String restriction, String key, long timeout)
		throws TupleSpaceException, TupleSpaceSecurityException {
		List<Tuple> tuples = new ArrayList<Tuple>();
        tuples.add(domain.takeOne(pattern, restriction, key));
        return tuples;
	}
}
