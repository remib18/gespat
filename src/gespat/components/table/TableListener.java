package components.table;

import models.AbstractData;

public interface TableListener<T extends AbstractData> {

	/**
	 * Fournit la donnée issue de la ligne selectionnée
	 *
	 * @param data
	 */
	void getDataOnRowSelected(T data);

	/**
	 * Informe chaque fois que le tableau est mis à jour
	 */
	void onTableUpdate();
}
