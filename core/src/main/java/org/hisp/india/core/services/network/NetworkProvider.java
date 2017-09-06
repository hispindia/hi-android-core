package org.hisp.india.core.services.network;

import android.content.Context;

import com.google.gson.GsonBuilder;

import org.hisp.india.core.services.filter.Filter;
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

    HttpLoggingInterceptor.Level getLevel();

    int getTimeout();

    <T> T provideApi(String baseUrl, Class<T> apiClass);

    <T> T provideApi(String baseUrl, Class<T> apiClass, boolean enableProgress);

    <TResponse> Observable<TResponse> transformResponse(Observable<TResponse> call);

    <TResponse> Observable<TResponse> transformResponse(Observable<TResponse> call, boolean enableFilter);

    <TResponse> Filter<TResponse, Observable<TResponse>> getRootFilter();

    <TResponse> Filter<TResponse, Observable<TResponse>> getCommonFilter();
}
