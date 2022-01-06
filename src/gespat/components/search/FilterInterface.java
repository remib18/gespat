package components.search;

import models.AbstractData;

import java.util.List;

/**
 * Permet la transmission d'un filtre de recherche
 *
 * @param <T>
 */
public interface FilterInterface<T extends AbstractData> {

	/**
	 * Filtre d'une liste de données de type T
	 *
	 * @param data la liste de données à filtrer
	 * @return la liste de données filtrées
	 */
	List<T> newFilter(List<T> data);
}
