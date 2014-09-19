package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiConfigEntries extends GuiListExtended {

	private ArrayList<Field> listFlieds = new ArrayList<Field>();

	public GuiConfigEntries(ConfigModConfigGui parent, Minecraft mc, ConfigLoad configLoad) {
		
		super(mc, parent.width, parent.height, 23, parent.height - 32, 20);
		
	}

	@Override
	public IGuiListEntry getListEntry(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getSize() {
		return this.listFlieds.size();
	}
	
	protected void initGui() {
	}
}
