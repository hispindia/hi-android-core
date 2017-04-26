package org.hisp.india.core.services.network;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.hisp.india.core.services.filter.InterceptFilter;

import rx.Observable;

/**
 * Created by nhancao on 5/5/17.
 */

public interface NetworkProvider {

    boolean isNetworkAvailable();

    boolean isDebug();

    Context getContext();

    GsonBuilder createBuilder();

    NetworkProvider addDefaultHeader();

    NetworkProvider addHeader(String key, String value);

    NetworkProvider addFilter(InterceptFilter interceptFilter);

    NetworkProvider clearFilter();

    NetworkProvider enableFilter(boolean enableFilter);

    <T> T provideApi(String baseUrl, final Class<T> service);

    <TResponse> Observable<TResponse> transformResponse(Observable<TResponse> call);

}
