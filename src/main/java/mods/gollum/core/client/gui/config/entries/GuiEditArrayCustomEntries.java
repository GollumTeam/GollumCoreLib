package mods.gollum.core.client.gui.config.entries;

import java.util.ArrayList;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entries.entry.JsonEntry;
import mods.gollum.core.client.gui.config.properties.JsonProperty;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;

@Deprecated
public class GuiEditArrayCustomEntries extends GuiConfigEntries {

//	public GuiEditArrayCustomEntries(GuiEditCustomArray parent, Minecraft mc, IConfigElement configElement, Object[] beforeValues, Object[] currentValues) {
//		super(parent, mc, configElement, beforeValues, currentValues);
//		 TODO Auto-generated constructor stub
//	}
	
	GuiEditCustomArray parent;
	Minecraft mc;
	IConfigElement configElement;
	Object[] beforeValues;
	Object[] currentValues;
	
	public GuiEditArrayCustomEntries(GuiEditCustomArray parent, Minecraft mc, IConfigElement configElement) {
		super(parent, mc);
		
		this.configElement = configElement;
		this.beforeValues  = configElement.getList();
		this.currentValues = configElement.getDefaults();
		this.mc            = mc;
		this.parent        = parent;
		
		this.listEntries = new ArrayList<IConfigEntry>();
		
		for (Object o: this.configElement.getList()) {
			if (o instanceof Json && this.configElement instanceof CustomElement) {
				
				GollumMod mod = ModGollumCoreLib.instance; // TODO Aller chercher le mod proprement
				
				JsonProperty properties         = new JsonProperty(mod, "", (Json)o, Json.create());
				IConfigElement subConfigElement = properties.createConfigElement();
				
				if (subConfigElement != null) {
					this.listEntries.add(new JsonEntry(this.owningScreen, this, subConfigElement));
				}
			}
		}
		
//		this.listEntries.add(new GuiConfigEntries.NumberSliderEntry(this.owningScreen, this, configElement));
//		this.listEntries.add(new GuiConfigEntries.NumberSliderEntry(this.owningScreen, this, configElement));
//		this.listEntries.add(new GuiConfigEntries.StringEntry(this.owningScreen, this, configElement));
		
	}
	

//	protected void initGui() {
//		
//		super.initGui();
//		
//		this.width = owningScreen.width;
//		this.height = owningScreen.height;
//
//		this.maxLabelTextWidth = 0;
//		for (IConfigEntry entry : this.listEntries)
//			if (entry.getLabelWidth() > this.maxLabelTextWidth)
//				this.maxLabelTextWidth = entry.getLabelWidth();
//
//		this.top = owningScreen.titleLine2 != null ? 33 : 23;
//		this.bottom = owningScreen.height - 32;
//		this.left = 0;
//		this.right = width;
//		int viewWidth = this.maxLabelTextWidth + 8 + (width / 2);
//		labelX = (this.width / 2) - (viewWidth / 2);
//		controlX = labelX + maxLabelTextWidth + 8;
//		resetX = (this.width / 2) + (viewWidth / 2) - 45;
//
//		this.maxEntryRightBound = 0;
//		for (IConfigEntry entry : this.listEntries)
//			if (entry.getEntryRightBound() > this.maxEntryRightBound)
//				this.maxEntryRightBound = entry.getEntryRightBound();
//
//		scrollBarX = this.maxEntryRightBound + 5;
//		controlWidth = maxEntryRightBound - controlX - 45;
//	}
//	
//    @Override
//    public int getSize()
//    {
//        return this.listEntries.size();
//    }
//
//    /**
//     * Gets the IGuiListEntry object for the given index
//     */
//    /**
//     * Gets the IGuiListEntry object for the given index
//     */
//    @Override
//    public IConfigEntry getListEntry(int index)
//    {
//        return this.listEntries.get(index);
//    }
//
//    @Override
//    public int getScrollBarX()
//    {
//        return scrollBarX;
//    }
//
//    /**
//     * Gets the width of the list
//     */
//    /**
//     * Gets the width of the list
//     */
//    @Override
//    public int getListWidth()
//    {
//        return owningScreen.width;
//    }
//
//	/**
//	 * This method is a pass-through for IConfigEntry objects that require
//	 * keystrokes. Called from the parent GuiConfig screen.
//	 */
//	public void keyTyped(char eventChar, int eventKey) {
//		for (IConfigEntry entry : this.listEntries) {
//			entry.keyTyped(eventChar, eventKey);
//		}
//	}
//	
//	/**
//	 * This method is a pass-through for IConfigEntry objects that contain
//	 * GuiTextField elements. Called from the parent GuiConfig screen.
//	 */
//	public void updateScreen() {
//		for (IConfigEntry entry : this.listEntries) {
//			entry.updateCursorCounter();
//		}
//	}

//    /**
//     * This method is a pass-through for IConfigEntry objects that contain GuiTextField elements. Called from the parent GuiConfig
//     * screen.
//     */
//    public void mouseClicked(int mouseX, int mouseY, int mouseEvent)
//    {
//        for (IConfigEntry entry : this.listEntries) {
//            entry.mouseClicked(mouseX, mouseY, mouseEvent);
//    }
//    
//    /**
//     * This method is a pass-through for IConfigListEntry objects that need to perform actions when the containing GUI is closed.
//     */
//    public void onGuiClosed()
//    {
//        for (IConfigEntry entry : this.listEntries)
//            entry.onGuiClosed();
//    }
//
    /**
     * Saves all properties on this screen / child screens. This method returns true if any elements were changed that require
     * a restart for proper handling.
     */
    public boolean saveConfigElements()
    {
//        boolean requiresRestart = false;
//        for (IConfigEntry entry : this.listEntries)
//            if (entry.saveConfigElement())
//                requiresRestart = true;
//        
//        return requiresRestart;
    	// TODO A revoir save
    	return false;
    }
//
//    /**
//     * Returns true if all IConfigEntry objects on this screen are set to default. If includeChildren is true sub-category
//     * objects are checked as well.
//     */
//    public boolean areAllEntriesDefault(boolean includeChildren)
//    {
////        for (IConfigEntry entry : this.listEntries)
////            if ((includeChildren || !(entry instanceof CategoryEntry)) && !entry.isDefault())
////                return false;
////
//    	// TODO
//        return true;
//    }
//
//    /**
//     * Sets all IConfigEntry objects on this screen to default. If includeChildren is true sub-category objects are set as
//     * well.
//     */
//    public void setAllToDefault(boolean includeChildren)
//    {
////        for (IConfigEntry entry : this.listEntries)
////            if ((includeChildren || !(entry instanceof CategoryEntry)))
////                entry.setToDefault();
//    //	TODO a revoir
//    }
//
//    /**
//     * Returns true if any IConfigEntry objects on this screen are changed. If includeChildren is true sub-category objects
//     * are checked as well.
//     */
//    public boolean hasChangedEntry(boolean includeChildren)
//    {
////        for (IConfigEntry entry : this.listEntries)
////            if ((includeChildren || !(entry instanceof CategoryEntry)) && entry.isChanged())
////                return true;
////
////    	TODO a revoir
//        return false;
//    }
//
////    /**
////     * Returns true if any IConfigEntry objects on this screen are enabled. If includeChildren is true sub-category objects
////     * are checked as well.
////     */
////    public boolean areAnyEntriesEnabled(boolean includeChildren)
////    {
////        for (IConfigEntry entry : this.listEntries)
////            if ((includeChildren || !(entry instanceof CategoryEntry)) && entry.enabled())
////                return true;
////
////        return false;
////    }
//
//    /**
//     * Reverts changes to all IConfigEntry objects on this screen. If includeChildren is true sub-category objects are
//     * reverted as well.
//     */
//    public void undoAllChanges(boolean includeChildren)
//    {
////        for (IConfigEntry entry : this.listEntries)
////            if ((includeChildren || !(entry instanceof CategoryEntry)))
////                entry.undoChanges();
//    	
//    	//TODO a revoir;
//    }

//    /**
//     * Calls the drawToolTip() method for all IConfigEntry objects on this screen. This is called from the parent GuiConfig screen
//     * after drawing all other elements.
//     */
//    public void drawScreenPost(int mouseX, int mouseY, float partialTicks)
//    {
//        for (IConfigEntry entry : this.listEntries)
//            entry.drawToolTip(mouseX, mouseY);
//    }

	
	
	
	
	
	
	
	

//	public void addNewEntry(int index) {
//
//		if (configElement.isList() && configElement.getType() == ConfigGuiType.BOOLEAN) {
////			listEntries.add(index, new BooleanEntry(this.parent, this, this.configElement, Boolean.valueOf(true)));
//		} else 
////		
//		if (configElement.isList() && configElement.getType() == ConfigGuiType.INTEGER) {
////			listEntries.add(index, new IntegerEntry(this.parent, this, this.configElement, 0));
//		} else
////		
//		if (configElement.isList() && configElement.getType() == ConfigGuiType.DOUBLE) {
////			listEntries.add(index, new DoubleEntry(this.parent, this, this.configElement, 0.0D));
//		} else 
////		
//		if (configElement.isList() && configElement.getType() == ConfigGuiType.STRING) {
////			listEntries.add(index, new StringEntry(this.parent, this, this.configElement, ""));
//		} else 
////		
//		if (configElement.isList()) {
////			listEntries.add(index, new StringEntry(this.parent, this, this.configElement, ""));
//		}
//		
////		this.canAddMoreEntries = !configElement.isListLengthFixed() && (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
////		keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
//	}

//	public void saveListChanges() {
//		// TODO Auto-generated method stub
//		
//	}
//
	public boolean isListSavable() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isDefault() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChanged() {
		// TODO Auto-generated method stub
		return false;
	}

}
