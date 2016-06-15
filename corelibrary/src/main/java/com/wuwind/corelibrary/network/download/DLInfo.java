package com.wuwind.corelibrary.network.download;


import com.wuwind.corelibrary.network.download.interfaces.IDListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载实体类
 * Download entity.
 */
public class DLInfo {
    public int totalBytes;
    public int currentBytes;
    public String fileName;
    public String dirPath;
    public String baseUrl;
    public String realUrl;
    public transient boolean loadAnyway;

    int redirect;
    boolean hasListener;
    boolean isResume;
    boolean isStop;
    String mimeType;
    String eTag;
    String disposition;
    String location;
    List<DLHeader> requestHeaders;
    final List<DLThreadInfo> threads;
    IDListener listener;
    File file;

    DLInfo() {
        threads = new ArrayList<>();
    }

    synchronized void addDLThread(DLThreadInfo info) {
        threads.add(info);
    }

    synchronized void removeDLThread(DLThreadInfo info) {
        threads.remove(info);
    }
}