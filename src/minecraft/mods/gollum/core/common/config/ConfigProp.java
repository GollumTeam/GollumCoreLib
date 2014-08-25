package mods.gollum.core.common.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ConfigProp {
	public abstract String name() default "";

	public abstract String info() default "";
	
	public abstract String group() default "";
}
