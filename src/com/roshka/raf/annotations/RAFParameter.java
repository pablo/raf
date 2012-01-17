package com.roshka.raf.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.roshka.raf.params.Globals;

@Retention(RetentionPolicy.RUNTIME)
public @interface RAFParameter {
	boolean mandatory() default false;
	String defaultValue() default "[unassigned]";
	String dateFormat() default Globals.DEFAULT_DATE_FORMAT;
	String name();
}
