package com.wuwind.corelibrary.network.download;

class DLException extends Exception {
    DLException() {
        super();
    }

    DLException(String detailMessage) {
        super(detailMessage);
    }

    DLException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    DLException(Throwable throwable) {
        super(throwable);
    }
}