package com.gollum.core.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ConfigProp {
	
	enum Type {
		DEFAULT,
		ITEM,
		BLOCK,
		BIOME,
		MOD,
		SLIDER,
		LIST_INLINE,
	}
	
	public abstract boolean show() default true;
	
	public abstract String info() default "";
	
	public abstract String group() default "";

	public abstract String newValue() default "";
	
	public abstract String[] validValues() default "";
	
	public abstract String minValue() default "";
	public abstract String maxValue() default "";
	
	public abstract boolean isListLengthFixed() default false;
	public abstract String  minListLength()     default "";
	public abstract String  maxListLength()     default "";
	
	public abstract boolean mcRestart()    default false;
	public abstract boolean worldRestart() default false;
	
	public abstract String pattern() default "";
	
	public abstract Type type() default Type.DEFAULT;
	public abstract String entryClass() default "";
	
	public abstract boolean dev() default false;

}
