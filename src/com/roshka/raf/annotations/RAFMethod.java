package com.roshka.raf.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.roshka.raf.route.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface RAFMethod {
	String value();
	RequestMethod[] acceptedMethods() default { RequestMethod.HttpGet };
}
