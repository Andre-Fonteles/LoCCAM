package br.ufc.great.syssu.coordubi;

import java.util.LinkedHashMap;
import java.util.Map;

import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.ITupleSpace;

public class TupleSpace implements ITupleSpace {

    private static TupleSpace instance;
    private Map<String, IDomain> domains;

    private TupleSpace() {
        domains = new LinkedHashMap<String, IDomain>();
    }

    public static TupleSpace getInstance() {
        if (instance == null) {
            instance = new TupleSpace();
        }
        return instance;
    }

    @Override
    public IDomain getDomain(String name) throws TupleSpaceException {
        IDomain domain = null;
        String[] s = name.split("\\.");

        if (s.length > 0) {
            String rootName = s[0];
            if (!domains.containsKey(rootName)) {
                domain = new Domain(rootName);
                domains.put(rootName, domain);
            } else {
                domain = domains.get(rootName);
            }

            for (int i = 1; i < s.length; i++) {
                domain = domain.getDomain(s[i]);
            }
        }

        return domain;
    }
}
