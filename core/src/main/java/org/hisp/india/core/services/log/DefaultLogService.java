package org.hisp.india.core.services.log;

import android.content.Context;

import com.logentries.logger.AndroidLogger;

import java.io.IOException;

/**
 * Created by nhancao on 5/5/17.
 */
public class DefaultLogService implements LogService {

    private AndroidLogger logger;

    @Override
    public void init(Context context,
                     boolean useHttpPost,
                     boolean useSsl,
                     boolean isUsingDataHub,
                     String dataHubAddr,
                     int dataHubPort,
                     String token,
                     boolean logHostName) throws IOException {
        this.logger = AndroidLogger.createInstance(
                context,
                useHttpPost,
                useSsl,
                isUsingDataHub,
                dataHubAddr,
                dataHubPort,
                token,
                logHostName);
    }

    @Override
    public void init(Context context, String token) throws IOException {
        init(context, true, false, false, null, 0, token, false);
    }

    @Override
    public void log(String message) {
        if (this.logger != null) {
            this.logger.log(message);
        }
    }
}
