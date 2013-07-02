package br.ufc.great.syssu.ubicentre.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.utils.MapTuple;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Message;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2MethodNotFoundException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Request;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Response;
import br.ufc.great.syssu.servicemanagement.IService;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;
import br.ufc.great.syssu.servicemanagement.ServiceManager;

public class RequestController implements IController {

	private static RequestController instance;

	private RequestController() {
	}

	public static RequestController getInstance() {
		if (instance == null) {
			instance = new RequestController();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String process(JSONRPC2Message message) throws JSONRPC2MethodNotFoundException,
		InvalidParamsException, OperationException {
		String response = null;

		JSONRPC2Request request = (JSONRPC2Request) message;

		IService service = ServiceManager.getInstance().getService(request.getMethod());

		if (service == null) {
			throw new JSONRPC2MethodNotFoundException("Invalid method: " + request.getMethod());
		}

		Object result = service.doService(request.getParams());

		if (result != null) {
			if (result instanceof Tuple) {
				result = new MapTuple((Tuple) result).getMap();
			} else if (result instanceof List) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (Tuple tuple : (List<Tuple>) result) {
					list.add(new MapTuple(tuple).getMap());
				}
				result = list;
			} else {
				throw new OperationException("Invalid service implementation.");
			}

			JSONRPC2Response r = new JSONRPC2Response(result, request.getID());
			response = r.toJSON().toJSONString();
		}

		return response;
	}
}
