package com.komenta.streaming.demo.constants;

import java.nio.file.Paths;

public class ApplicationConstants {
    public static final String VIDEO = Paths.get(System.getProperty("user.dir")).getFileSystem().getRootDirectories().iterator().next().toString();

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;

    private ApplicationConstants() {
    }
}