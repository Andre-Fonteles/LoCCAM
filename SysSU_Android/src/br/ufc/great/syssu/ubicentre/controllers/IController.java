package br.ufc.great.syssu.ubicentre.controllers;

import br.ufc.great.syssu.jsonrpc2.*;
import br.ufc.great.syssu.servicemanagement.*;

public interface IController {
    public String process(JSONRPC2Message message) 
            throws JSONRPC2MethodNotFoundException, InvalidParamsException, OperationException;
}
