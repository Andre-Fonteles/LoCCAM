package br.ufc.great.syssu.ubicentre.controllers;

import br.ufc.great.syssu.jsonrpc2.JSONRPC2Message;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2MethodNotFoundException;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Notification;
import br.ufc.great.syssu.servicemanagement.IService;
import br.ufc.great.syssu.servicemanagement.InvalidParamsException;
import br.ufc.great.syssu.servicemanagement.OperationException;
import br.ufc.great.syssu.servicemanagement.ServiceManager;

public class NotificationController implements IController {

    private static NotificationController instance;

    private NotificationController() {
    }

    public static NotificationController getInstance() {
        if (instance == null) {
            instance = new NotificationController();
        }
        return instance;
    }

    @Override
    public String process(JSONRPC2Message message)
            throws JSONRPC2MethodNotFoundException, InvalidParamsException, OperationException {

        JSONRPC2Notification request = (JSONRPC2Notification) message;

        IService service = ServiceManager.getInstance().getService(request.getMethod());

        if (service == null) {
            throw new JSONRPC2MethodNotFoundException("Invalid method: " + request.getMethod());
        }

        service.doService(request.getParams());

        return null;
    }
}
