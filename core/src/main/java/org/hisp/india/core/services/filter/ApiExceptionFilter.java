package org.hisp.india.core.services.filter;

import org.hisp.india.core.exceptions.ApiException;
import org.hisp.india.core.exceptions.ErrorCodes;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;

/**
 * Created by nhancao on 4/16/17.
 */

public class ApiExceptionFilter<T> implements Filter<Throwable,Observable<T>> {

    @Override
    public Observable<T> execute(Throwable throwable) {

        if (throwable instanceof HttpException) {
            ResponseBody failedResponse = ((HttpException) throwable).response().errorBody();
            if (failedResponse == null) {
                return Observable.error(ApiException.put(ErrorCodes.GENERAL_ERROR,
                                                          "Response Error Body is empty"));
            } else {
                String rawString = "";
                try {
                    rawString = failedResponse.string();
                    return Observable.error(ApiException.put(ErrorCodes.GENERAL_ERROR, rawString));
                } catch (Exception ex) {
                    return Observable
                            .error(ApiException.put(ErrorCodes.GENERAL_ERROR, rawString));
                }
            }
        }
        return Observable.error(throwable);
    }

}
