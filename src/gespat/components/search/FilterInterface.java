package components.search;

import models.AbstractData;

import java.util.List;

/**
 * Sert à filtrer pour une recherche
 *
 * @param <T>
 */
public interface FilterInterface<T extends AbstractData> {

	/**
	 * Filtre d'une liste de donnés de type T
	 *
	 * @param data
	 */
	List<T> newFilter(List<T> data);
}
