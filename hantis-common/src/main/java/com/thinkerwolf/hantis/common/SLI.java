package com.thinkerwolf.hantis.common;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SLI {
	
	String value();
	
}
