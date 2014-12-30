package mods.gollum.core.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.minecraft.world.WorldServer;
import mods.gollum.core.common.reflection.WorldStub;

public class Reflection {
	
	public static void setFinalStatic(Field field, Object object, Object value) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		
		field.set(object, value);
		
		modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
	}
	
	public static void enableSynchronized(Method m) throws Exception {
		
		Field modifiersField = Method.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		
		modifiersField.setInt(m, m.getModifiers() | Modifier.SYNCHRONIZED);
	}
	
	public static Method getMethodByDesobfuscate (Class clazz, String name) {
		
		for (Method m : clazz.getDeclaredMethods()) {
			DeobfuscateName anno = m.getAnnotation(DeobfuscateName.class);
			if (anno!= null && anno.value().equals(name)) {
				return m;
			}
		}
		
		return null;
	}
	
	public static String getObfuscateName (Class clazz, String name) throws Exception {
		return getMethodByDesobfuscate(clazz, name).getName();
	}

	public static Class<?>[] getObfuscateParameters(Class<?> clazz, String name) throws Exception {
		return getMethodByDesobfuscate(clazz, name).getParameterTypes();
	}

	public static Method getObfuscateMethod(Class<?> target, Class<?> stub, String name) throws Exception {
		return target.getDeclaredMethod(getObfuscateName(stub, name), getObfuscateParameters(stub, name));
	}
	
}