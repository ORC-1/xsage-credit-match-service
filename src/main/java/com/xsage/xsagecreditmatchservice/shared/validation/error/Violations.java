package com.xsage.xsagecreditmatchservice.shared.validation.error;

import java.util.List;

public interface Violations<V extends Violation> extends List<V> {

    boolean hasErrors();

    int getErrorCount();

}
