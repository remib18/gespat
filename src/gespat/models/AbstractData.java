package models;

import utils.File;

public abstract class AbstractData implements Comparable<AbstractData> {

	protected final String fs = File.COLUMN_SEPARATOR;

	protected int id;

	/**
	 * @return le paramètre repentant l'identifiant de la donnée
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
	 * Permet la comparaison, et par conséquent le tri des données
	 */
	@Override
	public int compareTo(AbstractData obj) {
		return this.getSearchableFields().compareTo(obj.getSearchableFields());
	}

}
