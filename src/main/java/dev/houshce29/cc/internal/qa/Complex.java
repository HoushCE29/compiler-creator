package dev.houshce29.cc.internal.qa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as being complex by nature.
 *
 * Use of this annotation is for maintenance purposes
 * only -- to mark potentially problematic code as being
 * something to look out for in the case of logged defects.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Complex {
}


