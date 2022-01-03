package components.search;

import models.Data;

import java.util.List;

public interface FilterInterface<T extends Data> {

    public List<T> newFilter(List<T> data);
}
