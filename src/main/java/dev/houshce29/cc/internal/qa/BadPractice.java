package dev.houshce29.cc.internal.qa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as containing potentially bad practice code.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BadPractice {

    /**
     * @return The things that are bad practice but
     *         are at least temporarily justified.
     */
    String value();
}
