package models;

import utils.File;

public abstract class AbstractData implements Comparable<AbstractData> {

	/** Séparateur de colonne pour la conversion en <code>String</code> */
	protected final String fs = File.COLUMN_SEPARATOR;

	protected int id;

	/**
	 * @return le paramètre représentant l'identifiant de la donnée
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return le paramètre sélectionné pour la recherche
	 */
	public abstract String getSearchableFields();

	/**
	 * @return la donnée sous forme de chaine de caractères
	 */
	@Override
	public abstract String toString();

	/**
	 * Permet d'effectuer la comparaison
	 */
	@Override
	public int compareTo(AbstractData obj) {
		return this.getSearchableFields().compareTo(obj.getSearchableFields());
	}

}
