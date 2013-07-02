package br.ufc.great.syssu.net.interfaces;

import java.io.IOException;

public interface INetworkServer extends INetworkSubject {
    void start() throws IOException;
    void stop() throws IOException;
}
