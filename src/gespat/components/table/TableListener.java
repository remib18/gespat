package components.table;

import models.Data;

public interface TableListener<T extends Data> {

    /**
     * Fournit la donnée issue de la ligne selectionnée
     * @param data
     */
    public void getDataOnRowSelected(T data);

    /**
     * Informe chaque fois que le tableau est mis à jour
     */
    public void onTableUpdate();
}
