package com.xsage.xsagecreditmatchservice.infrastructure.log;

public interface CustomLog {
    public void log(String msg);
    public void log(String msg, Object payload);
}
