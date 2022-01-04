package components.table;

import models.Data;

public interface TableRowsFunctionsInterface<T extends Data> {

    int getRowIndex(T data);

    T getData(int index);
}
