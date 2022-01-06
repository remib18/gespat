package components.table;

import models.AbstractData;

public interface TableListener<T extends AbstractData> {

	/**
	 * Fournit la donnée issue de la ligne sélectionnée
	 */
	void getDataOnRowSelected(T data);

	/**
	 * Informe chaque fois que le tableau est mis à jour
	 */
	void onTableUpdate();
}
