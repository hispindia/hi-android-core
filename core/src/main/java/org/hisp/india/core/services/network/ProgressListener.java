package org.hisp.india.core.services.network;

/**
 * Created by nhancao on 7/9/17.
 */

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}

