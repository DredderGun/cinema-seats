package dev.avyguzov.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Please note that you have to add a class with this annotation to RoutesMapping yet.
 *
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlerPath {
    String method();
    String value();
    String acceptType() default "application/json";
}
