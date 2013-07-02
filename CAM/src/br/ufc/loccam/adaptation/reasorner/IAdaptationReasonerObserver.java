package br.ufc.loccam.adaptation.reasorner;

import br.ufc.loccam.adaptation.model.Component;

public interface IAdaptationReasonerObserver {
	
	/**
	 * Notifica que houve uma alteração na configuração desejada da zona de observação 
	 * @param observableZone - Configuração da nova zona de observação desejada
	 */
	public void notifyObservableZoneChanged(Component[] addedComponents, Component[] removedComponents);
	
}
