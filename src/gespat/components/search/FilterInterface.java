package components.search;

import models.AbstractData;

import java.util.List;

public interface FilterInterface<T extends AbstractData> {

    List<T> newFilter(List<T> data);
}
