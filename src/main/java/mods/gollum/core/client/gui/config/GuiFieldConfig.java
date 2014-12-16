package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.element.FieldElement;
import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;

public class GuiFieldConfig extends GuiConfig {

	private String category;
	private CategoryEntry parentEntry;
	private ConfigLoad configLoad;
	
	public GuiFieldConfig(CategoryEntry parentEntry) {
		super(parentEntry.parent.parent);
		this.parentEntry = parentEntry;
		this.titleLine2  = parentEntry.getLabel();	
		this.category    = parentEntry.configElement.getName();
		log.debug ("Category : "+this.category);
	}
	
	@Override
	protected void initConfigElement() {
		
		this.configLoad = ConfigLoader.configLoaded.get(this.mod);
		
		try {
			for (Field f : this.configLoad.config.getClass().getDeclaredFields()) {
				
				ConfigProp prop = f.getAnnotation(ConfigProp.class);
				if (prop != null) {
					
					String category = prop.group();
					if (category == null || category.equals("")) {
						category = "General";
					}
					if (this.category.equals(category)) {
						this.configElements.add(new FieldElement(f, prop));
					}
				}
			}
		} catch (Exception e)  {
			e.printStackTrace();
		}
		
	}
	
}
