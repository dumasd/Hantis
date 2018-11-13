package com.thinkerwolf.hantis.orm.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface GeneratedValue {
	
	//TODO 指定自生成策略
    //GenerateStrategy value();

}
