package br.ufc.great.syssu.ubicentre.controllers;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2InvalidMessageException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Message;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2MethodNotFoundException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Notification;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2ParseException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Request;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Response;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Subscription;
import br.ufc.great.syssu.net.NetworkMessageReceived;
import br.ufc.great.syssu.net.interfaces.INetworkObserver;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;

public class FrontController implements INetworkObserver {

    private static FrontController instance;

    private FrontController() {
    }

    public static FrontController getInstance() {
        if (instance == null) {
            instance = new FrontController();
        }
        return instance;
    }

    @Override
    public String process(NetworkMessageReceived message) {

        JSONRPC2Message msn = null;
        String response = null;

        try {
            msn = JSONRPC2Message.parse(message.getMessage());
            Logger.getLogger("UbiCentre").log(Level.INFO, "Message received: " + message.getMessage());
        } catch (JSONRPC2ParseException ex) {
        	Logger.getLogger("UbiCentre").log(Level.SEVERE, "Error in message process.", ex);
        	JSONRPC2Error error = JSONRPC2Error.PARSE_ERROR;
        	error.setData(ex.getMessage());
            response = (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (JSONRPC2InvalidMessageException ex) {
        	Logger.getLogger("UbiCentre").log(Level.SEVERE, "Error in message process.", ex);
        	JSONRPC2Error error = JSONRPC2Error.INVALID_REQUEST;
        	error.setData(ex.getMessage());
        	response =  (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        }

        if (msn instanceof JSONRPC2Request) {
        	response =  processRequest((JSONRPC2Request) msn);
        } else if (msn instanceof JSONRPC2Subscription) {
        	response =  proecessSubscription((JSONRPC2Subscription) msn, message.getInetAddres());
        } else if (msn instanceof JSONRPC2Notification) {
        	response =  processNotification((JSONRPC2Notification) msn);
        }
        
        Logger.getLogger("UbiCentre").log(Level.INFO, "Result: " + response);

        return response;
    }

    private String processRequest(JSONRPC2Request req) {
        try {
            return RequestController.getInstance().process(req);
        } catch (JSONRPC2MethodNotFoundException ex) {
        	JSONRPC2Error error = JSONRPC2Error.METHOD_NOT_FOUND;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (InvalidParamsException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INVALID_PARAMS;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (OperationException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INTERNAL_ERROR;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        }
    }

    private String processNotification(JSONRPC2Notification not) {
        try {
            return NotificationController.getInstance().process(not);
        } catch (JSONRPC2MethodNotFoundException ex) {
        	JSONRPC2Error error = JSONRPC2Error.METHOD_NOT_FOUND;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (InvalidParamsException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INVALID_PARAMS;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (OperationException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INTERNAL_ERROR;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        }
    }

    private String proecessSubscription(JSONRPC2Subscription sub, InetAddress address) {
        try {
            SubscriptionController cont = SubscriptionController.getInstance();
            cont.setAddress(address);
            return cont.process(sub);
        } catch (JSONRPC2MethodNotFoundException ex) {
        	JSONRPC2Error error = JSONRPC2Error.METHOD_NOT_FOUND;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (InvalidParamsException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INVALID_PARAMS;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        } catch (OperationException ex) {
        	JSONRPC2Error error = JSONRPC2Error.INTERNAL_ERROR;
        	error.setData(ex.getMessage());
            return (new JSONRPC2Response(error, null)).toJSON().toJSONString();
        }
    }
}
