package org.hisp.india.core.services.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhancao on 4/16/17.
 */

public class FilterChain<Input, Output> {

    private List<Filter<Input, Output>> filters = new ArrayList<>();
    private Filter<Input, Output> target;

    public void execute(Input source) {
        for (Filter<Input, Output> filter : filters) {
            filter.execute(source);
        }
        target.execute(source);
    }

    public void addFilter(Filter<Input, Output> filter) {
        filters.add(filter);
    }

    public void setTarget(Filter<Input, Output> target) {
        this.target = target;
    }
}
