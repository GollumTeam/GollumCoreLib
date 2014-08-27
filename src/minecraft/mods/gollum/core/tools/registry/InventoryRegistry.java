package mods.gollum.core.tools.registry;

import java.util.Hashtable;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.gui.GCLGuiContainer;
import mods.gollum.core.common.container.GCLContainer;
import mods.jammyfurniture.client.gui.GuiCraftingSide;
import mods.jammyfurniture.common.containers.ContainerCraftingSide;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;


public class InventoryRegistry {
	
	public static class GuiContainerInventoryClass {
		public Class<? extends Container> classContainer;
		public Class classGuiContainer = null;
		public int numColumns;
		
		public GuiContainerInventoryClass (Class<? extends Container> classContainer, int numColumns) {
			this.classContainer = classContainer;
			this.numColumns = numColumns;
		}
	}
	
	private static InventoryRegistry instance = new InventoryRegistry();
	
	private Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList = new Hashtable<Integer, GuiContainerInventoryClass>();
	
	public static InventoryRegistry instance () {
		return instance;
	}
	
	// le int doit devenir un object
	public static void register (int guiId, int numColumns) {
		registerContainer(guiId, GCLContainer.class, numColumns);
		if (ModGollumCoreLib.proxy.isRemote()) {
			registerGui(guiId, GCLGuiContainer.class);
		}
	}
	
	public Hashtable<Integer, GuiContainerInventoryClass> getGuiInventoryList () {
		Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList = this.guiInventoryList;
		this.guiInventoryList = new Hashtable<Integer, GuiContainerInventoryClass>();
		return guiInventoryList;
	}

	public static void registerContainer(int guiId, Class<? extends Container> classContainer) {
		registerContainer(guiId, classContainer, -1);
	}

	public static void registerContainer(int guiId, Class<? extends Container> classContainer, int numColumns) {
		GuiContainerInventoryClass guiContainerInventory = new GuiContainerInventoryClass (classContainer, numColumns);
		if (instance().guiInventoryList.containsKey(guiId)) {
			ModGollumCoreLib.log.warning("Override registry Container in id : "+guiId);
		}
		instance().guiInventoryList.put(guiId, guiContainerInventory);
	}

	public static void registerGui(int guiId, Class<? extends GuiContainer> classGuiContainer) {
		if (!instance().guiInventoryList.containsKey(guiId)) {
			ModGollumCoreLib.log.severe ("Not foud register Container in id : "+guiId);
			return;
		}
		instance().guiInventoryList.get(guiId).classGuiContainer = classGuiContainer;
	}
	
}
