package org.hisp.india.core.services.filter;

/**
 * Created by nhancao on 4/16/17.
 */

public interface Filter<Input, Output> {

    Output execute(Input source);

}
