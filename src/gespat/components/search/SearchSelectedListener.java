package components.search;

import components.table.TableRowsFunctionsInterface;
import models.Data;

public interface SearchSelectedListener<T extends Data>{

    /**
     * Fournit la donnée issue de la sélection du tableau des resultats de recherche.
     * @param data
     */
    public void onSelectionChange(T data, TableRowsFunctionsInterface<T> getRowIndex);
}


