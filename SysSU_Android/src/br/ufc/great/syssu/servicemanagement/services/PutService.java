package br.ufc.great.syssu.servicemanagement.services;

import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.utils.MapTuple;
import br.ufc.great.syssu.coordubi.TupleSpace;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;
import br.ufc.great.syssu.jsonrpc2.util.NamedParamsRetriever;
import br.ufc.great.syssu.jsonrpc2.util.PositionalParamsRetriever;
import br.ufc.great.syssu.servicemanagement.IService;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;

/*
 * "put": {
 * 		"type":"method",
 * 		"parameters": [
 *	    		{"name":"domain", "type":"string"},
 *	    		{"name":"tuple", "type":"object"},
 *				{"name":"key", "type":"string", "optional":true}
 *				{"name":"timetolive", "type":"long", "optional":true}
 *		]
 *	}
 */

public class PutService implements IService {

    @Override
    public String getName() {
        return "put";
    }

    @SuppressWarnings("unchecked")
	@Override
    public Object doService(Object params) throws InvalidParamsException, OperationException {

        String domainName = null;
        Tuple tuple = null;
        String key = null;
        long timeToLive = 0;

        try {
            if (params instanceof Map) {
                NamedParamsRetriever retriever = new NamedParamsRetriever((Map<String, Object>) params);
                domainName = retriever.getString("domain");
                tuple = new MapTuple(retriever.getMap("tuple")).getObject();
                key = retriever.getOptString("key", true, "");
                timeToLive = retriever.getOptLong("timetolive", 0);
            } else if (params instanceof List) {
                PositionalParamsRetriever retriever = new PositionalParamsRetriever((List<Object>) params);
                domainName = retriever.getString(0);
                tuple = new MapTuple(retriever.getMap(1)).getObject();
                key = retriever.getOptString(2, true, "");
                timeToLive = retriever.getOptLong(3, 0);
            } else {
                throw new InvalidParamsException("Invalid params type");
            }
        } catch (JSONRPC2Error err) {
            throw new InvalidParamsException(err.getMessage());
        }

        if (domainName == null || domainName.equals("")) {
            throw new InvalidParamsException("Invalid domain name");
        }

        if (tuple == null || tuple.isEmpty()) {
            throw new InvalidParamsException("Invalid tuple");
        }

        try {
            IDomain domain = TupleSpace.getInstance().getDomain(domainName);
            tuple.setTimeToLive(timeToLive);
            domain.put(tuple, key);
        } catch (Exception ex) {
            throw new OperationException(ex.getMessage());
        }

        return null;
    }
}
