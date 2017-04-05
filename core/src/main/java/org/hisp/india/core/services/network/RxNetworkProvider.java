package org.hisp.india.core.services.network;

import com.google.gson.GsonBuilder;

import org.hisp.india.core.services.RestMessageResponse;

import rx.Observable;

/**
 * Created by nhancao on 5/5/17.
 */

public interface RxNetworkProvider extends NetworkProvider {

    boolean isDebug();

    GsonBuilder createBuilder();

    RxNetworkProvider addDefaultHeader();

    RxNetworkProvider addHeader(String key, String value);

    <T> T provideApi(String baseUrl, final Class<T> service);

    <TResponse extends RestMessageResponse<TResult>, TResult> Observable<TResult> transformResponse(Observable<TResponse> call);
}
