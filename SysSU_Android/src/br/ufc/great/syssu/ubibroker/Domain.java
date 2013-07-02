package br.ufc.great.syssu.ubibroker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.base.utils.MapPattern;
import br.ufc.great.syssu.base.utils.MapTuple;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2InvalidMessageException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Notification;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2ParseException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Request;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Response;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Subscription;

public class Domain implements IDomain {

    private String name;
    private UbiBroker ubiBroker;
    private static int id = 0;

    Domain(String domainName, UbiBroker ubiBroker) {
        this.name = domainName;
        this.ubiBroker = ubiBroker;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void put(Tuple tuple, String key) throws TupleSpaceException {
        try {
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("domain", getName());
            params.put("tuple", new MapTuple(tuple).getMap());
            params.put("key", key);
            params.put("timetolive", tuple.getTimeToLive());

            JSONRPC2Notification not = new JSONRPC2Notification("put", params);

            String result = ubiBroker.sendMessage(not.toJSON().toJSONString());

            if (result != null && !result.equals("")) {
                try {
                    JSONRPC2Response error = JSONRPC2Response.parse(result);
                    if (!error.indicatesSuccess()) {
                        throw new TupleSpaceException(error.getError().getMessage());
                    }
                } catch (JSONRPC2ParseException ex) {
                    throw new TupleSpaceException(ex);
                } catch (JSONRPC2InvalidMessageException ex) {
                    throw new TupleSpaceException(ex);
                }
            }
        } catch (IOException ex) {
            throw new TupleSpaceException(ex);
        }
    }

    @Override
    public List<Tuple> read(Pattern pattern, String restriction, String key) throws TupleSpaceException {
        return getMany(pattern, restriction, "read", key, 0);
    }

    @Override
    public List<Tuple> readSync(Pattern pattern, String restriction, String key, long timeout) 
    	throws TupleSpaceException {
        return getMany(pattern, restriction, "readsync", key, timeout);
    }

    @Override
    public Tuple readOne(Pattern pattern, String restriction, String key) throws TupleSpaceException {
        return getOne(pattern, restriction, "readone", key, 0);
    }

    @Override
    public Tuple readOneSync(Pattern pattern, String restriction, String key, long timeout) 
    	throws TupleSpaceException {
        return getOne(pattern, restriction, "readonesync", key, timeout);
    }

    @Override
    public List<Tuple> take(Pattern pattern, String restriction, String key) throws TupleSpaceException {
        return getMany(pattern, restriction, "take", key, 0);
    }

    @Override
    public List<Tuple> takeSync(Pattern pattern, String restriction, String key, long timeout) 
    	throws TupleSpaceException {
        return getMany(pattern, restriction, "takesync", key, timeout);
    }

    @Override
    public Tuple takeOne(Pattern pattern, String restriction, String key) throws TupleSpaceException {
        return getOne(pattern, restriction, "takeone", key, 0);
    }

    @Override
    public Tuple takeOneSync(Pattern pattern, String restriction, String key, long timeout) 
    	throws TupleSpaceException {
        return getOne(pattern, restriction, "takeonesync", key, timeout);
    }

    public IDomain getDomain(String name) throws TupleSpaceException {
        return new Domain(this.name + "." + name, ubiBroker);
    }

    @Override
    public Object subscribe(IReaction reaction, String event, String key) throws TupleSpaceException {
        try {
        	Map<String, Object> params = buildParams(
        		reaction.getPattern(), reaction.getJavaScriptFilter(), key, 0);
        	params.remove("timeout");

            JSONRPC2Subscription not = new JSONRPC2Subscription(event, ubiBroker.getReactionsPort(), params, reaction.getId());

            String result = ubiBroker.sendMessage(not.toJSON().toJSONString());

            if (result != null && !result.equals("")) {
                try {
                    JSONRPC2Response response = JSONRPC2Response.parse(result);
                    if (!response.indicatesSuccess()) {
                        throw new TupleSpaceException(response.getError().getMessage());
                    } else {                    	
                    	Object id = response.getResult();
                    	reaction.setId(id);
                    	ubiBroker.addReaction(reaction);
                    	return id;
                    }
                    
                } catch (JSONRPC2ParseException ex) {
                    throw new TupleSpaceException(ex);
                } catch (JSONRPC2InvalidMessageException ex) {
                    throw new TupleSpaceException(ex);
                }
            } else {
            	throw new TupleSpaceException("UbiBroker not respond.");
            }
        } catch (IOException ex) {
            throw new TupleSpaceException(ex);
        }     
    }
    
    @Override
	public void unsubscribe(Object reactionId, String key) throws TupleSpaceException {
		// TODO Auto-generated method stub
	}

    @SuppressWarnings("unchecked")
	private Tuple getOne(Pattern pattern, String restriction, String service, String key, long timeout) 
    	throws TupleSpaceException {
        try {
            Map<String, Object> params = buildParams(pattern, restriction, key, timeout);

            JSONRPC2Request not = new JSONRPC2Request(service, params, id++);

            String result = ubiBroker.sendMessage(not.toJSON().toJSONString());

            if (result != null && !result.equals("")) {
                try {
                    JSONRPC2Response response = JSONRPC2Response.parse(result);
                    if (!response.indicatesSuccess()) {
                        throw new TupleSpaceException(response.getError().getMessage());
                    } else {
                        JSONArray res = (JSONArray) response.getResult();
                        JSONObject obj = (JSONObject) res.get(0);
                        if (obj instanceof Map) {
                            return new MapTuple((Map<String, Object>) obj).getObject();
                        }
                    }
                } catch (JSONRPC2ParseException ex) {
                    throw new TupleSpaceException(ex);
                } catch (JSONRPC2InvalidMessageException ex) {
                    throw new TupleSpaceException(ex);
                }
            }
            return null;
        } catch (IOException ex) {
            throw new TupleSpaceException(ex);
        }
    }

    @SuppressWarnings("unchecked")
	private List<Tuple> getMany(Pattern pattern, String restriction, String service, String key, long timeout) 
		throws TupleSpaceException {
        try {
            Map<String, Object> params = buildParams(pattern, restriction, key, timeout);

            JSONRPC2Request not = new JSONRPC2Request(service, params, id++);

            String result = ubiBroker.sendMessage(not.toJSON().toJSONString());

            if (result != null && !result.equals("")) {
                try {
                    JSONRPC2Response response = JSONRPC2Response.parse(result);
                    if (!response.indicatesSuccess()) {
                        throw new TupleSpaceException(response.getError().getMessage());
                    } else {
                        List<Tuple> tuples = new ArrayList<Tuple>();
                        Object res = response.getResult();
                        if (res instanceof List) {
                            List<JSONObject> list = (List<JSONObject>) response.getResult();
                            for (Object obj : list) {
                                if (obj instanceof Map) {
                                    tuples.add(new MapTuple((Map<String, Object>) obj).getObject());
                                }
                            }
                            return tuples;
                        }
                    }
                } catch (JSONRPC2ParseException ex) {
                    throw new TupleSpaceException(ex);
                } catch (JSONRPC2InvalidMessageException ex) {
                    throw new TupleSpaceException(ex);
                }
            }
            return null;
        } catch (IOException ex) {
            throw new TupleSpaceException(ex);
        }
    }

    private Map<String, Object> buildParams(Pattern pattern, String restriction, String key, long timeout) {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("domain", getName());
        params.put("pattern", new MapPattern(pattern).getMap());
        params.put("filter", restriction);
        params.put("key", key);
        params.put("timeout", timeout);
        return params;
    }

}
