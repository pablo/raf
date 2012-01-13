package com.roshka.raf.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.roshka.raf.route.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface RAFParameter {
	boolean mandatory() default false;
	String defaultValue() default "[unassigned]";
	String name();
}
