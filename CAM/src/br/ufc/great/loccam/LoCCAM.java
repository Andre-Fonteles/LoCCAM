package br.ufc.great.loccam;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.ILocalDomain;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.loccam.adaptation.model.Component;
import br.ufc.loccam.adaptation.reasorner.AdaptationReasoner;
import br.ufc.loccam.adaptation.reasorner.IAdaptationReasoner;
import br.ufc.loccam.adaptation.reasorner.IAdaptationReasonerObserver;
import br.ufc.loccam.cacmanager.CACManager;
import br.ufc.loccam.cacmanager.ICACManager;

public class LoCCAM implements IAdaptationReasonerObserver {
	
	private ICACManager cacManager;
	private IAdaptationReasoner adaptationReasoner;
	
	private InterestAddedReaction addedReaction;
	private InterestRemovedReaction removedReaction;
	
	public LoCCAM(ILocalDomain localDomain, ICACManager cacManager, IAdaptationReasoner adaptationReasoner) {
		this.cacManager = cacManager;
		this.adaptationReasoner = adaptationReasoner;
		
		// Configura-se para escutar mudanças nas ZO e ZI desejadas geradas pelo reasoner
		adaptationReasoner.setReasonerObservable(this);
		
		addedReaction = new InterestAddedReaction();
		removedReaction = new InterestRemovedReaction();

		try {
			localDomain.subscribe(addedReaction, "put", "");
			localDomain.subscribe(removedReaction, "take", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyObservableZoneChanged(Component[] addedComponents, Component[] removedComponents) {
		// Inicia os CACs de acordo com a nova zona de interesse desejada
		if(addedComponents != null && addedComponents.length > 0) {
			for (Component component : addedComponents) {
				cacManager.startCAC(component);
			}
		}
		
		// Para os CACs em funcionamento de acordo com a nova zona de interesse desejada
		if(removedComponents != null && removedComponents.length > 0) {
			for (Component component : removedComponents) {
				cacManager.stopCAC(component);
			}
		}
	}

	private class InterestAddedReaction implements IReaction {

		private Object reactionId;
		
		@Override
		public Object getId() {
			return reactionId;
		}

		@Override
		public Pattern getPattern() {
			return (Pattern) new Pattern().addField("AppId", "?").addField("InterestElement", "?");
		}

		@Override
		public void react(Tuple tuple) {
			String appId;
			String contextKey;
			
			if(tuple.getField(0).getName().equals("AppId")) {
				appId = tuple.getField(0).getValue().toString();
				contextKey = tuple.getField(1).getValue().toString();
			} else {
				contextKey = tuple.getField(0).getValue().toString();
				appId = tuple.getField(1).getValue().toString();
			}
			
			adaptationReasoner.addApplicationInterestElement(appId, contextKey);
		}

		@Override
		public void setId(Object reactionId) {
			this.reactionId = reactionId;
		}

		@Override
		public String getJavaScriptFilter() {
			return null;
		}

		@Override
		public IFilter getJavaFilter() {
			return null;
		}
	}

	private class InterestRemovedReaction implements IReaction {

		private Object reactionId;
		
		@Override
		public Object getId() {
			return reactionId;
		}

		@Override
		public Pattern getPattern() {
			return (Pattern) new Pattern().addField("AppId", "?").addField("InterestElement", "?");
		}

		@Override
		public void react(Tuple tuple) {
			String appId;
			String contextKey;
			
			if(tuple.getField(0).getName().equals("AppId")) {
				appId = tuple.getField(0).getValue().toString();
				contextKey = tuple.getField(1).getValue().toString();
			} else {
				contextKey = tuple.getField(0).getValue().toString();
				appId = tuple.getField(1).getValue().toString();
			}
			
			adaptationReasoner.removeApplicationInterestElement(appId, contextKey);
		}

		@Override
		public void setId(Object reactionId) {
			this.reactionId = reactionId;
		}

		@Override
		public String getJavaScriptFilter() {
			return null;
		}

		@Override
		public IFilter getJavaFilter() {
			return null;
		}
	}
	
	public String printState() {
		String r;
		r = ((CACManager)cacManager).printBundlesState();
		r += ((AdaptationReasoner)adaptationReasoner).printDesiredOZ();
		
		return r; 
	}
}
