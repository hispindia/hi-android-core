package org.hisp.india.core.services.filter;

import org.hisp.india.core.exceptions.ApiException;
import org.hisp.india.core.exceptions.ErrorCodes;
import org.hisp.india.core.services.network.NetworkProvider;

import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public class NetworkFilter<T> implements Filter<Throwable,Observable<T>> {

    private NetworkProvider networkProvider;

    public NetworkFilter(NetworkProvider networkProvider) {
        this.networkProvider = networkProvider;
    }

    @Override
    public Observable<T> execute(Throwable throwable) {
        if (!networkProvider.isNetworkAvailable()) {
            return Observable.error(ApiException.put(ErrorCodes.NETWORK_NOT_AVAILABLE_ERROR,
                                                      "Network is not available"));
        }
        return Observable.error(throwable);
    }
}
