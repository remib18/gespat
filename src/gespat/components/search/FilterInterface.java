package components.search;

import models.Data;

import java.util.List;

public interface FilterInterface<T extends Data> {

    List<T> newFilter(List<T> data);
}
