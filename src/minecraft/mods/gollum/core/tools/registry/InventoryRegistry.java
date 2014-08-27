package mods.gollum.core.tools.registry;

import java.util.Hashtable;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.gui.GCLGuiContainer;
import mods.gollum.core.common.container.GCLContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;


public class InventoryRegistry {
	
	public static class GuiContainerInventoryClass {
		public Class<? extends Container> classContainer;
		public Class<? extends GuiContainer> classGuiContainer;
		public int numColumns;
		
		public GuiContainerInventoryClass (Class<? extends Container> classContainer, Class<? extends GuiContainer> classGuiContainer, int numColumns) {
			this.classContainer = classContainer;
			this.classGuiContainer = classGuiContainer;
			this.numColumns = numColumns;
		}
	}
	
	private static InventoryRegistry instance = new InventoryRegistry();
	
	private Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList = new Hashtable<Integer, GuiContainerInventoryClass>();
	
	public static InventoryRegistry instance () {
		return instance;
	}
	
	public static void register (int guiId, int numColumns) {
		register(guiId, GCLContainer.class, GCLGuiContainer.class, numColumns);
	}
	
	public static void register (int guiId, Class<? extends Container> classContainer, Class<? extends GuiContainer> classGuiContainer) {
		register(guiId, classContainer, classGuiContainer, -1);
	}
	
	private static void register(int guiId, Class<? extends Container> classContainer, Class<? extends GuiContainer> classGuiContainer, int numColumns) {
		GuiContainerInventoryClass guiContainerInventory = new GuiContainerInventoryClass (classContainer, classGuiContainer, numColumns);
		if (instance().guiInventoryList.containsKey(guiId)) {
			ModGollumCoreLib.log.warning("Override registry gui id : "+guiId);
		}
		instance().guiInventoryList.put(guiId, guiContainerInventory);
	}
	
	public Hashtable<Integer, GuiContainerInventoryClass> getGuiInventoryList () {
		Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList = this.guiInventoryList;
		this.guiInventoryList = new Hashtable<Integer, GuiContainerInventoryClass>();
		return guiInventoryList;
	}
	
}
