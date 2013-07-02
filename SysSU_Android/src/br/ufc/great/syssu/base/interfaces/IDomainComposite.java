package br.ufc.great.syssu.base.interfaces;

import br.ufc.great.syssu.base.TupleSpaceException;

public interface IDomainComposite {
    IDomain getDomain(String name) throws TupleSpaceException;
}
