package br.ufc.great.syssu.servicemanagement.services;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.utils.MapPattern;
import br.ufc.great.syssu.coordubi.TupleSpace;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;
import br.ufc.great.syssu.jsonrpc2.util.NamedParamsRetriever;
import br.ufc.great.syssu.jsonrpc2.util.PositionalParamsRetriever;
import br.ufc.great.syssu.servicemanagement.IService;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;

public abstract class AbstractEventService implements IService {
	@SuppressWarnings("unchecked")
	@Override
    public Object doService(Object param) throws InvalidParamsException, OperationException {

        Map<String, Object> map = (Map<String, Object>) param;

        InetAddress address = (InetAddress) map.get("address");
        int port = (Integer) map.get("port");
        Object params = map.get("params");

        String domainName = null;
        Pattern pattern = null;
        String restriction = null;
        String key = null;

        try {
            if (params instanceof Map) {
                NamedParamsRetriever retriever = new NamedParamsRetriever((Map<String, Object>) params);
                domainName = retriever.getString("domain");
                pattern = new MapPattern(retriever.getMap("pattern")).getObject();
                restriction = retriever.getString("filter");
                key = retriever.getOptString("key", true, "");
            } else if (params instanceof List) {
                PositionalParamsRetriever retriever = new PositionalParamsRetriever((List<Object>) params);
                domainName = retriever.getString(0);
                pattern = new MapPattern(retriever.getMap(1)).getObject();
                restriction = retriever.getString(2);
                key = retriever.getOptString(3, "");
                
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
            throw new InvalidParamsException("Invalid query");
        }

        try {
            IDomain domain = TupleSpace.getInstance().getDomain(domainName);
            return domain.subscribe(new Reaction(pattern, restriction, address, port), getName(), key);
        } catch (Exception ex) {
            throw new OperationException(ex.getMessage());
        }
	}
}
