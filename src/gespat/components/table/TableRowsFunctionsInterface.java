package components.table;

import models.AbstractData;

/**
 * @param <T> type de donnée
 */
public interface TableRowsFunctionsInterface<T extends AbstractData> {

	/**
	 * Retourne l'index de la ligne du tableau
	 *
	 * @param data liste de données
	 * @return l'index de la donnée
	 */
	int getRowIndex(T data);

	/**
	 * Retourne la donnée de la ligne du tableau
	 *
	 * @param index l'index de la donnée
	 * @return la donnée liée à l'<code>index</code>.
	 */
	T getData(int index);
}
