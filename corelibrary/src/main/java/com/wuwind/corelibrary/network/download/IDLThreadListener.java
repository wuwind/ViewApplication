package com.wuwind.corelibrary.network.download;

interface IDLThreadListener {
    void onProgress(int progress);

    void onStop(DLThreadInfo threadInfo);

    void onFinish(DLThreadInfo threadInfo);

    void onError(DLThreadInfo threadInfo);
}