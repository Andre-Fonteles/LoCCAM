package br.ufc.loccam.cacmanager;

import java.util.List;
import java.util.Map;

import br.ufc.loccam.adaptation.model.Component;

public interface ICACManager {

		/**
		 * Inicia um CAC.
		 * @param symbolicName - Symbolic-Name do CAC a ser inicializado
		 */
		public void startCAC(Component cac);

		/**
		 * Para um CAC.
		 * @param symbolicName - Symbolic-Name do CAC a ser parado
		 */
		public void stopCAC(Component cac);
		
		/**
		 * Instala um bundle baseado no nome do seu arquivo
		 * @param fileName
		 */
		public void installCAC(Component cac);
		
		/**
		 * Desinstala um bundle baseado no seu Symbolic-Name
		 * @param bundle
		 */
		public void uninstallCAC(Component cac);
		
		/**
		 * Retorna a lista de todos os componentes CAC disponíveis no repositório local
		 * @return lista de todos os componentes CAC disponíveis no repositório local
		 */
		public Map<String, List<Component>> getListOfAvailableCACs();
}
