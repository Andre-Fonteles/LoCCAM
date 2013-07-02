package br.ufc.loccam.adaptation.reasorner;

import br.ufc.loccam.adaptation.model.Component;

/**
 * @author Andre
 *
 */
public interface IAdaptationReasoner {

	/**
	 * Configura um objeto que será notificado sobre novas zonas de observações desejadas geradas a partir 
	 * de mudanças nas zonas de interesse
	 * @param reasonerObserver - Objeto que receberá as notificações
	 */
	public void setReasonerObservable(IAdaptationReasonerObserver reasonerObserver);
	
	/**
	 * Adiciona uma nova aplicação à lista de aplicações inscritas no LoCCAM
	 * @param application
	 */
	public void addApplication(Component application);
	
	/**
	 * Remove uma aplicação da listas de aplicações inscritas no LoCCAM
	 * @param application
	 */
	public void removeApplication(String symbolicName);
	
	/**
	 * Adiciona um novo CAC à lista de CACs disponíveis para o LoCCAM
	 * @param cac
	 */
	public void addAvailableCAC(Component cac);
	
	/**
	 * Remove um CAC da lista de CACs disponíveis para o LoCCAM
	 * @param symbolicName - Nome do componente
	 * @param observableContext - zona de observação do elemento
	 * @return false, caso não seja possível remover o CAC
	 */
	public boolean removeAvailableCAC(String symbolicName, String observableContext);
	
	
	/**
	 * Adiciona um novo interesse a uma aplicação. Caso a aplicação ainda não exista, ela é criada internamente.
	 * @param appId
	 * @param contextKey
	 */
	public void addApplicationInterestElement(String appId, String contextKey);
	
	/**
	 * Remove um interesse de uma aplicação
	 * @param appId
	 * @param contextKey
	 */
	public void removeApplicationInterestElement(String appId, String contextKey);
}
