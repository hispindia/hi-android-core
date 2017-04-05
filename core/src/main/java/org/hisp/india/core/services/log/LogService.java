package org.hisp.india.core.services.log;

import android.content.Context;

import java.io.IOException;

/**
 * Created by nhancao on 5/5/17.
 */
public interface LogService {
    void init(Context context,
              boolean useHttpPost,
              boolean useSsl,
              boolean isUsingDataHub,
              String dataHubAddr,
              int dataHubPort,
              String token,
              boolean logHostName) throws IOException;

    void init(Context context, String token) throws IOException;

    void log(String message);
}
