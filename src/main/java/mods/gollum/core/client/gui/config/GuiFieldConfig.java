package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.entries.GollumCategoryEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.GollumProperty;
import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;

public class GuiFieldConfig extends GuiGollumConfig {
	

	private static HashMap<Field, IConfigElement> fieldElements;

	GollumMod mod;
	ConfigLoad configLoad;
	private String currentCategory = null;
	
	public GuiFieldConfig(GuiScreen parent) {
		super(parent, getFields (parent, null), getModName (parent));
		
		this.init(parent);
	}

	public GuiFieldConfig(GuiConfig parent, String category) {
		super(parent, getFields (parent, category), parent.title);
		
		this.init(parent);
		
		this.titleLine2 = category;
		this.currentCategory = category;
	}
	
	private void init (GuiScreen parent) {
		this.mod              = this.getMod(parent);
		this.configLoad       = ConfigLoader.configLoaded.get(mod);
		
		log.debug ("Config mod : " + mod.getModId() + " with "+this.fieldElements.size()+" fields.");
	}
	
	private static List<IConfigElement> getFields(GuiScreen parent, String currentCategory) {
		
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		GollumMod mod = getMod(parent);
		ConfigLoad configLoad = ConfigLoader.configLoaded.get(mod);
		if (currentCategory == null) {
			fieldElements = new HashMap<Field, IConfigElement>();
		}
		
		
		if (configLoad != null) {
			
			ArrayList<String> categories = configLoad.getCategories();
			
			
			if (categories.size() > 1 && currentCategory == null) {

				HashMap<Field, IConfigElement> tmpFieldElements = new HashMap<Field, IConfigElement>();
				for (String category : categories) {
					fields.add(new DummyCategoryElement(category, mod.i18n().trans("config.category."+category), GollumCategoryEntry.class));
				}
				
			} else {
				
				if (currentCategory == null || currentCategory.equals("General")) {
					currentCategory = "";
				}
				
				try {
					for (Field f : configLoad.config.getClass().getDeclaredFields()) {
						
						GollumProperty prop   = new GollumProperty (f, configLoad, currentCategory);
						IConfigElement element = prop.createConfigElement ();
						
						if (element != null) {
							fieldElements.put(f, element);
							fields.add(element);
						}
					}
				} catch (Exception e)  {
					e.printStackTrace();
				}
			}
		}
		
		
		return fields;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			
			if (this.currentCategory != null) {
				this.mc.displayGuiScreen(this.parentScreen);
				return;
			}
			
			boolean requiresMcRestart = this.entryList.saveConfigElements();
			
			for (Entry<Field, IConfigElement> entry: this.fieldElements.entrySet()) {
				Field         f  = entry.getKey();
				IConfigElement el = entry.getValue();
				
				log.warning ("Save "+f.getName ()+"="+el.get());
				
				this.setField(f, el);
			}
			
			
			new ConfigLoader(configLoad.config, false).writeConfig();
			
			if (requiresMcRestart) {
				mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle", new ChatComponentText(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
			} else {
				this.mc.displayGuiScreen(this.parentScreen);
			}
			
		} else {
			super.actionPerformed(button);
		}
	}

	private void setField(Field f, IConfigElement el) {
		
		Object o = null;
		
		f.setAccessible(true);
		try {
			
			o = el.get();
			
			if (f.getType().isArray()) {
				
				Class subClass = f.getType().getComponentType();
				
				Object[] oList =  el.getList();
				o = Array.newInstance(subClass, Array.getLength(oList));
				for (int i = 0; i < oList.length; i++) {
					
					Object subO = oList[i];
					
					if (subClass.isAssignableFrom(String.class)                                             ) { subO = subO.toString()                      ; } else
					if (subClass.isAssignableFrom(Long.class)    || subClass.isAssignableFrom(Long.TYPE)    ) { subO = Long   .parseLong   (subO.toString()); } else
					if (subClass.isAssignableFrom(Integer.class) || subClass.isAssignableFrom(Integer.TYPE) ) { subO = Integer.parseInt    (subO.toString()); } else
					if (subClass.isAssignableFrom(Short.class)   || subClass.isAssignableFrom(Short.TYPE)   ) { subO = Short  .parseShort  (subO.toString()); } else
					if (subClass.isAssignableFrom(Byte.class)    || subClass.isAssignableFrom(Byte.TYPE)    ) { subO = Byte   .parseByte   (subO.toString()); } else
					if (subClass.isAssignableFrom(Double.class)  || subClass.isAssignableFrom(Double.TYPE)  ) { subO = Double .parseDouble (subO.toString()); } else
					if (subClass.isAssignableFrom(Float.class)   || subClass.isAssignableFrom(Float.TYPE)   ) { subO = Float  .parseFloat  (subO.toString()); } else
					if (subClass.isAssignableFrom(Boolean.class) || subClass.isAssignableFrom(Boolean.TYPE) ) { subO = Boolean.parseBoolean(subO.toString()); }
					
					Array.set(o, i, subO);
				}
				
				f.set(this.configLoad.config, o);
				
			} else {
				
				if (f.getType().isAssignableFrom(String.class)                                                ) { f.set(this.configLoad.config, o.toString()                      ); }
				if (f.getType().isAssignableFrom(Long.class)    || f.getType().isAssignableFrom(Long.TYPE)    ) { f.set(this.configLoad.config, Long   .parseLong   (o.toString())); }
				if (f.getType().isAssignableFrom(Integer.class) || f.getType().isAssignableFrom(Integer.TYPE) ) { f.set(this.configLoad.config, Integer.parseInt    (o.toString())); }
				if (f.getType().isAssignableFrom(Short.class)   || f.getType().isAssignableFrom(Short.TYPE)   ) { f.set(this.configLoad.config, Short  .parseShort  (o.toString())); }
				if (f.getType().isAssignableFrom(Byte.class)    || f.getType().isAssignableFrom(Byte.TYPE)    ) { f.set(this.configLoad.config, Byte   .parseByte   (o.toString())); }
				if (f.getType().isAssignableFrom(Double.class)  || f.getType().isAssignableFrom(Double.TYPE)  ) { f.set(this.configLoad.config, Double .parseDouble (o.toString())); }
				if (f.getType().isAssignableFrom(Float.class)   || f.getType().isAssignableFrom(Float.TYPE)   ) { f.set(this.configLoad.config, Float  .parseFloat  (o.toString())); }
				if (f.getType().isAssignableFrom(Boolean.class) || f.getType().isAssignableFrom(Boolean.TYPE) ) { f.set(this.configLoad.config, Boolean.parseBoolean(o.toString())); }
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}