package mods.gollum.core.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })

public @interface ConfigProp {
	
	public abstract String name() default "";
	
	public abstract String info() default "";
	
	public abstract String group() default "";
	
	public abstract String[] validValues() default "";
	
	public abstract String minValue() default "";
	public abstract String maxValue() default "";
	
	public abstract boolean isListLengthFixed() default false;
	public abstract String  maxListLength()     default "";
	
	public abstract boolean mcRestart()    default false;
	public abstract boolean worldRestart() default false;
	
	public abstract String pattern() default "";
}
