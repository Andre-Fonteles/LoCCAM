package br.ufc.great.syssu.net.interfaces;

public interface INetworkSubject {
    void setNetworkObserver(INetworkObserver observer);
    INetworkObserver getNetworkObserver();
}
