package models;

import utils.File;

public abstract class Data implements Comparable<Data> {

    protected final String fs = File.COLUMN_SEPARATOR;

    /**
     * @return le paramètre repésentant l'identifiant de la donné
     */
    public abstract int getId();

    /**
     * @return le paramètre sélectionné pour la recherche
     */
    public abstract String getSearchableFields();

    /**
     * @return la donnée sous forme de chaine de caractères
     */
    @Override
    public abstract String toString();

    @Override
    public int compareTo(Data obj) {
        return this.getSearchableFields().compareTo(obj.getSearchableFields());
    }

}
