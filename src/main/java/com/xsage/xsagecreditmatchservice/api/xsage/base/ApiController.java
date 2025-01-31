package com.xsage.xsagecreditmatchservice.api.xsage.base;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@RestController
public @interface ApiController {
    @AliasFor(attribute = "value", annotation = Controller.class)
    String value() default "";
}
