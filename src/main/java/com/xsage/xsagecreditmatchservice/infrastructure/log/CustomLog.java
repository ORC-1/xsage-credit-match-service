package com.xsage.xsagecreditmatchservice.infrastructure.log;

public interface CustomLog {
    void log(String msg);

    void log(String msg, Object payload);
}
