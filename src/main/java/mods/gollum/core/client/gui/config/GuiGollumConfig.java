package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;

public class GuiGollumConfig extends GuiConfig {
	

	private static HashMap<Field, ConfigElement> initFieldElements;

	GollumMod mod;
	ConfigLoad configLoad;
	private HashMap<Field, ConfigElement> fieldElements;
	
	public GuiGollumConfig(GuiScreen parent) {
		
		super(parent, getFields (parent), getModId (parent), false, false, getModName (parent));
		
		this.mod = this.getMod(parent);
		configLoad = ConfigLoader.configLoaded.get(mod);
		this.fieldElements = initFieldElements;
		
		log.debug ("Config mod : " + mod.getModId() + " with "+this.fieldElements.size()+" fields.");
		
	}

	private static List<IConfigElement> getFields(GuiScreen parent) {
		
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		GollumMod mod = getMod(parent);
		ConfigLoad configLoad = ConfigLoader.configLoaded.get(mod);
		initFieldElements = new HashMap<Field, ConfigElement>();
		
		if (configLoad != null) {

			try {
				for (Field f : configLoad.config.getClass().getDeclaredFields()) {
					f.setAccessible(true);
					
					String label = mod.i18n().trans("config."+f.getName());
					
					ConfigProp anno = f.getAnnotation(ConfigProp.class);
					if (anno != null) {
						
						
						Property.Type type = null;
						
						// TODO généraliser aux autres champs
						if (f.getType().isAssignableFrom(String.class)) {
							type = Property.Type.STRING;
						}
						if (
							f.getType().isAssignableFrom(Long.TYPE) ||
							f.getType().isAssignableFrom(Integer.TYPE) ||
							f.getType().isAssignableFrom(Long[].class) ||
							f.getType().isAssignableFrom(Integer[].class)
						) {
							type = Property.Type.INTEGER;
						}
						if (f.getType().isAssignableFrom(Float.TYPE) ||
							f.getType().isAssignableFrom(Double.TYPE) 
						) {
							type = Property.Type.DOUBLE;
						}
						if (f.getType().isAssignableFrom(Boolean.TYPE)) {
							type = Property.Type.BOOLEAN;
						}
						
						Object value = f.get(configLoad.config);
						String[] values = null;
						if (value instanceof Object[]) {
							Object[] ar = ((Object[]) value);
							values = new String[ar.length];
							for (int i = 0; i < ar.length; i++) {
								values[i] = ar[i].toString();
							}
						}
						Object valueDefault = f.get(configLoad.configDefault);
						String[] valuesDefault = null;
						if (valueDefault instanceof Object[]) {
							Object[] ar = ((Object[]) valueDefault);
							valuesDefault = new String[ar.length];
							for (int i = 0; i < ar.length; i++) {
								valuesDefault[i] = ar[i].toString();
							}
						}
						
						if (type != null) {
							Property prop = null;
							if (values != null) {
								prop = new Property(label, values, type);
								prop.setDefaultValues(valuesDefault);
							} else {
								prop = new Property(label, value.toString(), type);
								prop.setDefaultValue(valueDefault.toString());
							}
							
							prop.comment = anno.info();
							prop.setValidValues(anno.validValues());
							prop.setRequiresMcRestart(anno.mcRestart());
							prop.setRequiresWorldRestart(anno.worldRestart());
							
							if (!anno.minValue ().equals("")) {
								try {
									prop.setMinValue(Integer.parseInt(anno.minValue ()));
								} catch (Exception e) {
									try { prop.setMinValue(Double.parseDouble(anno.minValue ())); } catch (Exception e2) {}
								}
							}
							
							if (!anno.maxValue ().equals("")) {
								try {
									prop.setMaxValue(Integer.parseInt(anno.maxValue ()));
								} catch (Exception e) {
									try { prop.setMaxValue(Double.parseDouble(anno.maxValue ())); } catch (Exception e2) {}
								}
							}
							
							ConfigElement<Integer> element = new ConfigElement<Integer>(prop);
							
							initFieldElements.put(f, element);
							
							fields.add(element);
						}
						
					}
				}
			} catch (Throwable e)  {
				e.printStackTrace();
			}
		}
		
		
		return fields;
	}

	private static GollumMod getMod(GuiScreen parent) {
		if (parent instanceof GuiModList) {
			try {
				Field f = parent.getClass().getDeclaredField("selectedMod");
				f.setAccessible(true);
				ModContainer modContainer = (ModContainer)f.get(parent);
				return (GollumMod) modContainer.getMod();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static String getModId(GuiScreen parent) {
		return getMod(parent).getModId();
	}
	
	private static String getModName(GuiScreen parent) {
		return getMod(parent).getModName();
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		
		try {
			Field f = GuiConfig.class.getDeclaredField("chkApplyGlobally");
			f.setAccessible(true);
			((GuiCheckBox)f.get(this)).visible = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5) / 2;

		((GuiButtonExt)this.buttonList.get(0)).xPosition = this.width / 2 - buttonWidthHalf;
		((GuiButtonExt)this.buttonList.get(1)).xPosition = this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5;
		((GuiButtonExt)this.buttonList.get(2)).xPosition = this.width / 2 - buttonWidthHalf + doneWidth + 5;
		
	}
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			
			boolean requiresMcRestart = this.entryList.saveConfigElements();
			
			for (Entry<Field, ConfigElement> entry: this.fieldElements.entrySet()) {
				Field         f  = entry.getKey();
				ConfigElement el = entry.getValue();
				
				Object o = el.get();
				
				f.setAccessible(true);
				try {
					if (f.getType().isAssignableFrom(String.class)) { f.set(this.configLoad.config, o.toString()                     );  }
					if (f.getType().isAssignableFrom(Long.TYPE))    { f.set(this.configLoad.config, Long   .parseLong   (o.toString())); }
					if (f.getType().isAssignableFrom(Integer.TYPE)) { f.set(this.configLoad.config, Integer.parseInt    (o.toString())); }
					if (f.getType().isAssignableFrom(Double.TYPE))  { f.set(this.configLoad.config, Double .parseDouble (o.toString())); }
					if (f.getType().isAssignableFrom(Float.TYPE))   { f.set(this.configLoad.config, Float  .parseFloat  (o.toString())); }
					if (f.getType().isAssignableFrom(Boolean.TYPE)) { f.set(this.configLoad.config, Boolean.parseBoolean(o.toString())); }
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				log.debug("save value "+f.getName()+" : "+o);
				
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
}