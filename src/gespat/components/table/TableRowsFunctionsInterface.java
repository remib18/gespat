package components.table;

import models.Data;

public interface TableRowsFunctionsInterface<T extends Data> {

    public int getRowIndex(T data);

    public T getData(int index);
}
