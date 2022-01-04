package components.table;

import models.AbstractData;

public interface TableRowsFunctionsInterface<T extends AbstractData> {

    int getRowIndex(T data);

    T getData(int index);
}
