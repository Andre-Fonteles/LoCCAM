package br.ufc.great.syssu.test;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleField;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.IReaction;

public class ReactionImplementation2 implements IReaction {

	Object id;
	Boolean isLocal = true;

	public ReactionImplementation2(Boolean isLocal){
		this.isLocal = isLocal;
	}

	public void setId(Object id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	public Object getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public Pattern getPattern() {
		// TODO Auto-generated method stub
		return (Pattern) new Pattern().addField("?","?");
		//return (Pattern) new Pattern().addField("nome","?string").addField("idade","?integer");
	}

	public String getJavaScriptFilter() {
		// TODO Auto-generated method stub
		return ""; //"function filter(tuple) {return true}";
	}

	public void react(Tuple tuple) {
		if (isLocal) {
			System.out.println(">>>Local REACTION2222<<< \n Tupla inserida:");
		} else {
			System.out.println(">>>Remote REACTION2222<<< \n Tupla inserida:");
		}

		String srtTuple = "{";
		for (TupleField tupleField : tuple) {
			srtTuple = srtTuple + "(" + tupleField.getName() +","+ tupleField.getValue() + ")";
		}
		srtTuple = srtTuple + "}";
		System.out.println(">>> " + srtTuple);
	}

	@Override
	public IFilter getJavaFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
