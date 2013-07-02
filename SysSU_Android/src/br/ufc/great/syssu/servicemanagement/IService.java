package br.ufc.great.syssu.servicemanagement;

public interface IService {
    public String getName();
    public Object doService(Object params) throws InvalidParamsException, OperationException;
}
