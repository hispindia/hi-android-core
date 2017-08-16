package org.hisp.india.core.bus;

/**
 * Created by nhancao on 7/11/17.
 */

public class ProgressBus {
    private Class service;
    private long bytesRead;
    private long contentLength;
    private boolean done;

    public ProgressBus(Class service, long bytesRead, long contentLength, boolean done) {
        this.service = service;
        this.bytesRead = bytesRead;
        this.contentLength = contentLength;
        this.done = done;
    }

    public Class getService() {
        return service;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "ProgressBus{" +
               "bytesRead=" + bytesRead +
               ", contentLength=" + contentLength +
               ", done=" + done +
               '}';
    }
}
