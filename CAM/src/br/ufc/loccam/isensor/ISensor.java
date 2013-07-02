package br.ufc.loccam.isensor;

import br.ufc.loccam.iandroidcontextprovider.IAndroidContextProvider;
import br.ufc.loccam.ipublisher.IPublisher;

public interface ISensor {
	
	public static final String PHYSICAL_TYPE = "Physical";
	public static final String LOGICAL_TYPE = "Logical";
	public static final String WEBSERVICE_TYPE = "Webservice";
	
	/**
	 * Inicia o sensoriamento
	 * @param platform - Plataforma de onde é retirado o contexto do Android utilizado pelos sensores
	 * @param publisher - Classe de utilizada para publicar informação contextual
	 */
	public void start(IAndroidContextProvider platform, IPublisher publisher);
	
	/**
	 * Para o sensoreamento.
	 */
	public void stop();
	
	/**
	 * Altera o valor de uma propriedade(configuração) do sensor.
	 * @param key - Chave correspondende a propriedade.
	 * @param value - Valor que será inserido na propriedade.
	 */
	public void setProperty(String key, String value);
	
	/**
	 * Recupera o valor atual de uma propriedade(configuração) do sensor.
	 * @param key - Chave correspondente a propriedade requerida.
	 * @return Valor atual da propriedade.
	 */
	public String getProperty(String key);
	
	/**
	 * Retorna as chaves de todas as propriedades de configuração do sensor.
	 * @return chaves das propriedades.
	 */
	public String[] getPropertiesKeys();
	
	/**
	 * Informa se o sensor está em funcionamento.
	 * @return boolean que informa se o sensor está em funcionamento.
	 */
	public boolean isRunning();
}