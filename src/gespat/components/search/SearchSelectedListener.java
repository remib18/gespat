package components.search;

import components.table.TableRowsFunctionsInterface;
import models.AbstractData;

public interface SearchSelectedListener<T extends AbstractData> {

	/**
	 * Fournit la donnée issue de la sélection du tableau des resultats de recherche.
	 *
	 * @param data
	 */
	void onSelectionChange(T data, TableRowsFunctionsInterface<T> getRowIndex);
}


