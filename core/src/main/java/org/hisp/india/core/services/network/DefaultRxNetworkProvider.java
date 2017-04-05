package org.hisp.india.core.services.network;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.hisp.india.core.common.HttpLoggingInterceptor;
import org.hisp.india.core.common.JodaDateTimeDeserializer;
import org.hisp.india.core.exceptions.ApiException;
import org.hisp.india.core.exceptions.ErrorCodes;
import org.hisp.india.core.services.RestMessageResponse;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 5/5/17.
 */

public class DefaultRxNetworkProvider extends AbstractNetworkProvider implements RxNetworkProvider {

    private boolean isDebug;
    private Map<String, String> headers;

    public DefaultRxNetworkProvider(Context context, boolean isDebug) {
        super(context);
        this.isDebug = isDebug;
        this.headers = new HashMap<>();
    }

    @Override
    public Gson gson() {
        return createBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public GsonBuilder createBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(new TypeToken<DateTime>() {
        }.getType(), new JodaDateTimeDeserializer());
        return gsonBuilder;
    }

    @Override
    public RxNetworkProvider addDefaultHeader() {
        addHeader("Content-Type", "application/json");
        return this;
    }

    @Override
    public RxNetworkProvider addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public <T> T provideApi(String baseUrl, Class<T> service) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.addInterceptor(chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            if (headers == null || headers.size() == 0) {
                addDefaultHeader();
            }
            for (Map.Entry<String, String> keyValueEntry : headers.entrySet()) {
                requestBuilder.addHeader(keyValueEntry.getKey(), keyValueEntry.getValue());
            }
            return chain.proceed(requestBuilder.build());
        });

        if (isDebug()) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        OkHttpClient okHttpClient = builder.build();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return restAdapter.create(service);
    }

    @Override
    public <TResponse extends RestMessageResponse<TResult>, TResult> Observable<TResult> transformResponse(Observable<TResponse> call) {

        return call
                .observeOn(Schedulers.computation())
                .onErrorResumeNext(throwable -> {
                    if (!DefaultRxNetworkProvider.this.isNetworkAvailable()) {
                        return Observable.error(ApiException.put(ErrorCodes.NETWORK_NOT_AVAILABLE_ERROR, "Network is not available"));
                    }

                    if (throwable instanceof HttpException) {
                        ResponseBody failedResponse = ((HttpException) throwable).response().errorBody();
                        if (failedResponse == null) {
                            return Observable.error(ApiException.put(ErrorCodes.GENERAL_ERROR, "Response Error Body is empty"));
                        } else {
                            String rawString = "";
                            try {
                                rawString = failedResponse.string();
                                return Observable.error(ApiException.put(ErrorCodes.GENERAL_ERROR, rawString));
                            } catch (Exception ex) {
                                return Observable.error(ApiException.put(ErrorCodes.GENERAL_ERROR, ex.getMessage()));
                            }
                        }
                    } else {
                        return Observable.error(throwable);
                    }
                })
                .flatMap(tResponse -> Observable.just(tResponse.getData()));
    }
}
