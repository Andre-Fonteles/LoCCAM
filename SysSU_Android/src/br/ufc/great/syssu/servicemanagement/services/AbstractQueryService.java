package br.ufc.great.syssu.servicemanagement.services;

import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.utils.MapPattern;
import br.ufc.great.syssu.coordubi.TupleSpace;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;
import br.ufc.great.syssu.jsonrpc2.util.NamedParamsRetriever;
import br.ufc.great.syssu.jsonrpc2.util.PositionalParamsRetriever;
import br.ufc.great.syssu.servicemanagement.IService;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;

public abstract class AbstractQueryService implements IService {

	@SuppressWarnings("unchecked")
	@Override
	public Object doService(Object params) throws InvalidParamsException, OperationException {
		String domainName = null;
		Pattern pattern = null;
		String restriction = null;
		String key = null;
		long timeout = 0;

		try {
			if (params instanceof Map) {
				NamedParamsRetriever retriever = new NamedParamsRetriever((Map<String, Object>) params);
				domainName = retriever.getString("domain");
				pattern = new MapPattern(retriever.getMap("pattern")).getObject();
				restriction = retriever.getString("filter");
				key = retriever.getOptString("key", true, "");
				timeout = retriever.getOptLong("timeout", 0);
			} else if (params instanceof List) {
				PositionalParamsRetriever retriever = new PositionalParamsRetriever((List<Object>) params);
				domainName = retriever.getString(0);
				pattern = new MapPattern(retriever.getMap(1)).getObject();
				restriction = retriever.getString(2);
				key = retriever.getOptString(3, "");
				timeout = retriever.getOptLong(4, 0);
			} else {
				throw new InvalidParamsException("Invalid params type");
			}
		} catch (JSONRPC2Error err) {
			throw new InvalidParamsException(err.getMessage());
		}

		if (domainName == null || domainName.equals("")) {
			throw new InvalidParamsException("Invalid domain name");
		}

		if (pattern == null || pattern.isEmpty()) {
			throw new InvalidParamsException("Invalid pattern");
		}

		try {
			IDomain domain = TupleSpace.getInstance().getDomain(domainName);
			return query(domain, pattern, restriction, key, timeout);
		} catch (Exception ex) {
			throw new OperationException(ex.getMessage());
		}
	}

	// Template method
	protected abstract List<Tuple> query(
		IDomain domain, Pattern pattern, String restriction, String key, long timeout)
		throws TupleSpaceException, TupleSpaceSecurityException;
}
