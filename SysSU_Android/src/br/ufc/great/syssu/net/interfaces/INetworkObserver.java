package br.ufc.great.syssu.net.interfaces;

import br.ufc.great.syssu.net.NetworkMessageReceived;

public interface INetworkObserver {
    public String process(NetworkMessageReceived message);
}
