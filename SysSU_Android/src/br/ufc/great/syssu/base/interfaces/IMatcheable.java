package br.ufc.great.syssu.base.interfaces;

import br.ufc.great.syssu.base.FilterException;
import br.ufc.great.syssu.base.Query;

public interface IMatcheable {
    boolean matches(Query query) throws FilterException ;
}
