package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;

public class GuiConfigEntries extends GuiListExtended {
	
	private ArrayList<ConfigEntry> entries;
	private GuiConfigMod parent;
	
	public GuiConfigEntries(GuiConfigMod parent, Minecraft mc, ConfigLoad configLoad) {
		
		super(mc, parent.width, parent.height, 23, parent.height - 32, 20);
		this.parent = parent;
		
		this.entries = new ArrayList<ConfigEntry>();
		
		for (Field f : configLoad.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			
			ConfigProp anno = f.getAnnotation(ConfigProp.class);
			if (anno != null) {
				
				// TODO généraliser aux autres champs
				if (
					f.getType().isAssignableFrom(Long.TYPE) ||
					f.getType().isAssignableFrom(Integer.TYPE)
				) {
					entries.add(new ConfigEntry(f, anno));
				}
			}
			
		}
		
	}

	@Override
	public IGuiListEntry getListEntry(int i) {
		return this.entries.get(i);
	}

	@Override
	protected int getSize() {
		return this.entries.size();
	}
	
	protected void initGui() {
	}

	public void updateScreen() {
		
	}
	
	public class ConfigEntry implements IGuiListEntry {
		
		Field f;
		ConfigProp config;
		
		public ConfigEntry (Field f, ConfigProp config) {
			this.f = f;
			this.config = config;
		}

		@Override
		public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
