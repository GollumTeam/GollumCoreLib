package mods.gollum.core.client.gui.config.entry;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static mods.gollum.core.ModGollumCoreLib.log;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiListConfig;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.ListElement;
import mods.gollum.core.common.config.ConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.gui.ForgeGuiFactory.ForgeConfigGui.ModIDEntry;

public class ListSlotEntry extends ConfigEntry {

	protected static RenderItem itemRender = new RenderItem();
	public ItemStack itemStackIcon = null;
	
	public ListSlotEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		int x1 = this.parent.controlX;
		int x2 = this.parent.controlX + this.parent.controlWidth;
		int y1 = y + this.parent.getSlotHeight ();
		int y2 = y;
		
//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		if (this.itemStackIcon != null) {
			try {
				
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				tessellator.startDrawingQuads();
				tessellator.setColorOpaque_I(0xDDDDDD);
				tessellator.addVertexWithUV((double)x1-18, (double)(y+18), 0.0D, 0.0D, 1.0D);
				tessellator.addVertexWithUV((double)x1-2, (double)(y+18), 0.0D, 1.0D, 1.0D);
				tessellator.addVertexWithUV((double)x1-2, (double)(y+2), 0.0D, 1.0D, 0.0D);
				tessellator.addVertexWithUV((double)x1-18, (double)(y+2), 0.0D, 0.0D, 0.0D);
				tessellator.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				
				this.itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), this.itemStackIcon, x1-18, y+2);
			} catch (Exception e) {
			}
		}
		
		if (this.isSelected()) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(0x808080);
			tessellator.addVertexWithUV((double)x1, (double)(y1), 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)x2, (double)(y1), 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)x2, (double)(y2), 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)x1, (double)(y2), 0.0D, 0.0D, 0.0D);
			tessellator.setColorOpaque_I(0x000000);
			tessellator.addVertexWithUV((double)(x1 + 1), (double)(y1-1), 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV((double)(x2 - 1), (double)(y1-1), 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV((double)(x2 - 1), (double)(y2+1), 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV((double)(x1 + 1), (double)(y2+1), 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		
		this.mc.fontRenderer.drawString(((ListElement)this.configElement).label, this.parent.controlX + 7, y+6, this.isSelected() ? 0xFFFFFF : 0x888888);
	}
	
	public boolean isSelected() {
		return ((GuiListConfig)this.parent.parent).currentValue.equals(this.getValue());
	}

	@Override
	public Object getValue() {
		return this.configElement.getValue();
	}

	@Override
	public ConfigEntry setValue(Object value) {
		return this;
	}
	
	@Override
	public void setSlot (int slotIndex) {
		((GuiListConfig)this.parent.parent).currentValue = (String) this.getValue();
	}
}
