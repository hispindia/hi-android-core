package org.hisp.india.core.services.network;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.hisp.india.core.common.HttpLoggingInterceptor;
import org.hisp.india.core.common.JodaDateTimeDeserializer;
import org.hisp.india.core.services.filter.ApiExceptionFilter;
import org.hisp.india.core.services.filter.FilterChain;
import org.hisp.india.core.services.filter.InterceptFilter;
import org.hisp.india.core.services.filter.NetworkFilter;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by nhancao on 5/5/17.
 */

public class DefaultNetworkProvider extends AbstractNetworkProvider implements NetworkProvider {
    private static final String TAG = DefaultNetworkProvider.class.getSimpleName();

    private boolean isDebug;
    private Map<String, String> headers;
    private FilterChain filterChain;
    private boolean enableFilter;

    public DefaultNetworkProvider(Context context, boolean isDebug) {
        super(context);
        this.isDebug = isDebug;
        this.headers = new HashMap<>();
        this.filterChain = new FilterChain();
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
    public NetworkProvider addDefaultHeader() {
        addHeader("Content-Type", "application/json");
        return this;
    }

    @Override
    public NetworkProvider addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    @Override
    public NetworkProvider addFilter(InterceptFilter interceptFilter) {
        filterChain.addFilter(interceptFilter);
        return this;
    }

    @Override
    public NetworkProvider clearFilter() {
        filterChain.clearFilter();
        return this;
    }

    @Override
    public NetworkProvider enableFilter(boolean enableFilter) {
        this.enableFilter = enableFilter;
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
    public <TResponse> Observable<TResponse> transformResponse(Observable<TResponse> call) {

        Observable<TResponse> res = call
                .observeOn(Schedulers.computation())
                .onErrorResumeNext(throwable -> new NetworkFilter<TResponse>(this).execute(throwable))
                .onErrorResumeNext(throwable -> new ApiExceptionFilter<TResponse>().execute(throwable))
                .flatMap(Observable::just);

        if (enableFilter) {
            res = filterChain.execute(res);
        }
        return res.onExceptionResumeNext(Observable.empty());

    }
}
