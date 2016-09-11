package com.cloudlabs.server.dehibernator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* Dehibernate Annotation
* 
* @author pedro-tavares
* @since Jan 2015  
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Dehibernate {
}
