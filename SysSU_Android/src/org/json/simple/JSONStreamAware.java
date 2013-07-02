package org.json.simple;

import java.io.*;
import java.io.Writer;

public interface JSONStreamAware {
    void writeJSONString(Writer out) throws IOException;
}
