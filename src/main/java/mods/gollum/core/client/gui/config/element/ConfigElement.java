package mods.gollum.core.client.gui.config.element;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.client.gui.config.entry.StringEntry;
import mods.gollum.core.common.config.ConfigProp;

public abstract class ConfigElement {
	
	private String name;
	
	public ConfigElement (String name) {
		this.name = name;
	}
	
	public abstract ConfigProp getConfigProp();

	public Class< ? extends ConfigEntry> getEntryClass() {
		
		Class clazz = this.getType();
		
//		if (this.anno.type() == ConfigProp.Type.ITEM)      { return ItemEntry          .class; }
//		if (this.anno.type() == ConfigProp.Type.BLOCK)     { return BlockEntry         .class; }
////		if (this.anno.type() == ConfigProp.Type.SLIDER)    { return SliderEntry        .class; }
//		if (Json           .class.isAssignableFrom(clazz)) { return JsonEntry          .class; }
//		if (IConfigJsonType.class.isAssignableFrom(clazz)) { return ConfigJsonTypeEntry.class; }
		if (String         .class.isAssignableFrom(clazz)) { return StringEntry        .class; }
//		if (Long           .class.isAssignableFrom(clazz) || Long     .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
//		if (Integer        .class.isAssignableFrom(clazz) || Integer  .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
//		if (Short          .class.isAssignableFrom(clazz) || Short    .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
//		if (Integer        .class.isAssignableFrom(clazz) || Byte     .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
//		if (Character      .class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
//		if (Double         .class.isAssignableFrom(clazz) || Double   .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
//		if (Float          .class.isAssignableFrom(clazz) || Float    .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
//		if (Boolean        .class.isAssignableFrom(clazz) || Boolean  .TYPE.isAssignableFrom(clazz)) { return BooleanEntry.class; }
//		
//		if (clazz.isArray()) {
//			
//			Class subType = clazz.getComponentType();
//			
//			if (this.anno.type() == ConfigProp.Type.ITEM)      { return ItemEntry          .class; }
//			if (this.anno.type() == ConfigProp.Type.BLOCK)     { return BlockEntry         .class; }
////				if (this.anno.type() == ConfigProp.Type.SLIDER)    { return SliderEntry        .class; }
//			if (Json           .class.isAssignableFrom(subType)) { return JsonEntry          .class; }
//			if (IConfigJsonType.class.isAssignableFrom(subType)) { return ConfigJsonTypeEntry.class; }
//			if (String         .class.isAssignableFrom(subType)) { return StringEntry        .class; }
//			if (Long           .class.isAssignableFrom(subType) || Long     .TYPE.isAssignableFrom(subType)) { return IntegerEntry.class; }
//			if (Integer        .class.isAssignableFrom(subType) || Integer  .TYPE.isAssignableFrom(subType)) { return IntegerEntry.class; }
//			if (Short          .class.isAssignableFrom(subType) || Short    .TYPE.isAssignableFrom(subType)) { return IntegerEntry.class; }
//			if (Integer        .class.isAssignableFrom(subType) || Byte     .TYPE.isAssignableFrom(subType)) { return IntegerEntry.class; }
//			if (Character      .class.isAssignableFrom(subType) || Character.TYPE.isAssignableFrom(subType)) { return IntegerEntry.class; }
//			if (Double         .class.isAssignableFrom(subType) || Double   .TYPE.isAssignableFrom(subType)) { return DoubleEntry .class; }
//			if (Float          .class.isAssignableFrom(subType) || Float    .TYPE.isAssignableFrom(subType)) { return DoubleEntry .class; }
//			if (Boolean        .class.isAssignableFrom(subType) || Boolean  .TYPE.isAssignableFrom(subType)) { return BooleanEntry.class; }
//			
//			return ArrayEntry.class;
//		}

		
		return null;
	}
	
	protected abstract Class getType ();
	
	public String getName() {
		return this.name;
	}

}
