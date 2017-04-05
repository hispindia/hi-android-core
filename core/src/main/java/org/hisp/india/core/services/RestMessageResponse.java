package org.hisp.india.core.services;

/**
 * Created by nhancao on 5/5/17.
 */
public class RestMessageResponse<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}