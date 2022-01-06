package components.table;

import models.AbstractData;

/**
 * Permet la transition de donnée
 * @param <T>
 */
public interface TableRowsFunctionsInterface<T extends AbstractData> {

	/**
	 * @param data la donnée recherchée
	 * @return l'index de la donnée
	 */
	int getRowIndex(T data);

	/**
	 * @param index du tableau de la donnée recherchée
	 * @return la donnée
	 */
	T getData(int index);
}
