package mods.gollum.core.common.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ConfigProp {
	
	enum Type {
		DEFAULT,
		ITEM,
		BLOCK,
		MOD,
		SLIDER,
	}
	
	public abstract boolean show() default true; // DONE
	
	public abstract String info() default "";
	
	public abstract String group() default ""; // DONE
	
	public abstract String newValue() default ""; // DONE
	
	public abstract String[] validValues() default "";
	
	public abstract String minValue() default ""; // DONE (NUMBER, STRING)
	public abstract String maxValue() default ""; // DONE (NUMBER, STRING)
	
	public abstract boolean isListLengthFixed() default false; // DONE
	public abstract String  minListLength()     default ""; // DONE
	public abstract String  maxListLength()     default ""; // DONE
	
	public abstract boolean mcRestart()    default false;  // DONE
	public abstract boolean worldRestart() default false;  // DONE

	public abstract String pattern() default "";
	
	public abstract Type type() default Type.DEFAULT;

	public abstract boolean dev() default false;
}
