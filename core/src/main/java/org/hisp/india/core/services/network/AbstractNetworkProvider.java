package org.hisp.india.core.services.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

/**
 * Created by nhancao on 5/5/17.
 */

public abstract class AbstractNetworkProvider implements NetworkProvider {

    protected Context context;

    public AbstractNetworkProvider(Context context) {
        this.context = context;
    }

    protected abstract Gson gson();

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
