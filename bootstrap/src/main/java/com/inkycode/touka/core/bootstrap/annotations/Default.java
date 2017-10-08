package com.inkycode.touka.core.bootstrap.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RUNTIME)
public @interface Default {

    Class<?> value() default void.class;

    int intValue() default 0;

    long longValue() default 0L;

    double doubleValue() default 0.0;

    float floatValue() default 0.0f;

    boolean booleanValue() default false;

    String stringValue() default "";
}
