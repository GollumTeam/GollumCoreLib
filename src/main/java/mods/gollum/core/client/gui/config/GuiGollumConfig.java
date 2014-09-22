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
import java.util.regex.Pattern;

import mods.gollum.core.common.config.Config;
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
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
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
	private String currentCategory = null;
	private HashMap<Field, ConfigElement> fieldElements;

	
	public GuiGollumConfig(GuiScreen parent) {
		super(parent, getFields (parent, null), getModId (parent), false, false, getModName (parent));
		
		this.init(parent);
	}

	public GuiGollumConfig(GuiScreen parent, String category) {
		super(parent, getFields (parent, category), getModId (parent), false, false, getModName (parent));
		
		this.init(parent);
		this.currentCategory = category;
	}
	
	private void init (GuiScreen parent) {
		this.mod = this.getMod(parent);
		configLoad = ConfigLoader.configLoaded.get(mod);
		this.fieldElements = initFieldElements;
		
		log.debug ("Config mod : " + mod.getModId() + " with "+this.fieldElements.size()+" fields.");
	}
	
	private static List<IConfigElement> getFields(GuiScreen parent, String currentCategory) {
		
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		GollumMod mod = getMod(parent);
		ConfigLoad configLoad = ConfigLoader.configLoaded.get(mod);
		initFieldElements = new HashMap<Field, ConfigElement>();
		
		if (configLoad != null) {
			
			ArrayList<String> categories = configLoad.getCategories();
			
			
			if (categories.size() > 1 && currentCategory == null) {
				
				for (String category : categories) {
					fields.add(new DummyCategoryElement(category, mod.i18n().trans("config.category."+category), GollumCategoryEntry.class));
				}
			} else {
				
				if (currentCategory == null || currentCategory.equals("General")) {
					currentCategory = "";
				}
				
				try {
					for (Field f : configLoad.config.getClass().getDeclaredFields()) {
						f.setAccessible(true);
						
						String label = mod.i18n().trans("config."+f.getName());
						
						ConfigProp anno = f.getAnnotation(ConfigProp.class);
						if (anno != null && anno.group().equals (currentCategory)) {
							
							
							Property.Type type = null;
							
							// TODO généraliser aux autres champs
							if (
								f.getType().isAssignableFrom(String.class) ||
								f.getType().isAssignableFrom(String[].class)
							) {
								type = Property.Type.STRING;
							}
							if (
								f.getType().isAssignableFrom(Long.TYPE      ) ||
								f.getType().isAssignableFrom(Integer.TYPE   ) ||
								f.getType().isAssignableFrom(Short.TYPE     ) ||
								f.getType().isAssignableFrom(Byte.TYPE      ) ||
								f.getType().isAssignableFrom(Long.class     ) ||
								f.getType().isAssignableFrom(Integer.class  ) ||
								f.getType().isAssignableFrom(Short.class    ) ||
								f.getType().isAssignableFrom(Byte.class     ) ||
								
								f.getType().isAssignableFrom(long[].class   ) ||
								f.getType().isAssignableFrom(int[].class    ) ||
								f.getType().isAssignableFrom(short[].class  ) ||
								f.getType().isAssignableFrom(byte[].class   ) ||
								f.getType().isAssignableFrom(Long[].class   ) ||
								f.getType().isAssignableFrom(Integer[].class) ||
								f.getType().isAssignableFrom(Short[].class  ) ||
								f.getType().isAssignableFrom(Byte[].class   )
							) {
								type = Property.Type.INTEGER;
							}
							if (
								f.getType().isAssignableFrom(Float.TYPE    ) ||
								f.getType().isAssignableFrom(Double.TYPE   ) ||
								f.getType().isAssignableFrom(Float.class   ) ||
								f.getType().isAssignableFrom(Double.class  ) ||
								
								f.getType().isAssignableFrom(float[].class ) ||
								f.getType().isAssignableFrom(double[].class) ||
								f.getType().isAssignableFrom(Float[].class ) ||
								f.getType().isAssignableFrom(Double[].class) 
							) {
								type = Property.Type.DOUBLE;
							}
							if (
								f.getType().isAssignableFrom(Boolean.TYPE   ) ||
								f.getType().isAssignableFrom(Boolean.class  ) ||
								
								f.getType().isAssignableFrom(boolean[].class) ||
								f.getType().isAssignableFrom(Boolean[].class)
							) {
								type = Property.Type.BOOLEAN;
							}
							
							Object value = f.get(configLoad.config);
							String[] values = null;
							Object valueDefault = f.get(configLoad.configDefault);
							String[] valuesDefault = null;
							
							if (value.getClass().isArray()) {
								values = new String[Array.getLength(value)];
								for (int i = 0; i < Array.getLength(value); i++) {
									values[i] = Array.get(value, i).toString();
								}
								valuesDefault = new String[Array.getLength(valueDefault)];
								for (int i = 0; i < Array.getLength(valueDefault); i++) {
									valuesDefault[i] = Array.get(valueDefault, i).toString();
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
								prop
									.setRequiresMcRestart(anno.mcRestart())
									.setRequiresWorldRestart(anno.worldRestart())
									.setIsListLengthFixed(anno.isListLengthFixed ())
								;
								
								if (!anno.minValue ().equals("")) {
									try {
										prop.setMinValue(Integer.parseInt(anno.minValue ()));
									} catch (Exception e) {
										try { prop.setMinValue(Double.parseDouble(anno.minValue())); } catch (Exception e2) {}
									}
								}
								
								if (!anno.maxValue ().equals("")) {
									try {
										prop.setMaxValue(Integer.parseInt(anno.maxValue ()));
									} catch (Exception e) {
										try { prop.setMaxValue(Double.parseDouble(anno.maxValue())); } catch (Exception e2) {}
									}
								}
								
								if (!anno.maxListLength ().equals("")) {
									try { prop.setMaxListLength(Integer.parseInt(anno.maxListLength())); } catch (Exception e) {}
								}
								if (!anno.pattern ().equals("")) {
									try { prop.setValidationPattern(Pattern.compile(anno.pattern())); } catch (Exception e) {}
								}
								if (
									anno.validValues ().length > 1 ||
									(
										anno.validValues ().length == 1 && 
										!anno.validValues ()[0].equals("")
									)
								) {
									prop.setValidValues(anno.validValues());
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
		if (parent instanceof GuiGollumConfig) {
			return getMod(((GuiGollumConfig) parent).parentScreen);
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