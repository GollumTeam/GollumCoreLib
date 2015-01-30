package mods.gollum.proxyblock.common.items;

import static mods.gollum.proxyblock.ModGollumProxyBlock.log;
import mods.gollum.proxyblock.common.blocks.BlockProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemProxy extends ItemBlock {

	BlockProxy block;

	public ItemProxy(Block block) {
		super(block);
		this.block = (BlockProxy) block;
	}

	private Item getTarget () {
		return this.getTarget(0);
	}
	private Item getTarget (int damage) {
		
		if (damage > 15) {
			damage = 0;
		}
		
		Item i = Item.getItemFromBlock(this.block.getTarget(damage));
		if (i != null) {
			return i;
		}
		return Items.apple;
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getTarget().getUnlocalizedName()+"d";
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		Item i = this.getTarget(is.getItemDamage());
		ItemStack nIs = new ItemStack(i, is.stackSize, is.getItemDamage());
		return i.getUnlocalizedName(nIs)+"d";
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer e, World w, int x, int y, int z, int side, float posX, float posY, float posZ) {
		Item i = this.getTarget(is.getItemDamage());
		ItemStack nIs = new ItemStack(i, is.stackSize, is.getItemDamage());
		return i.onItemUse(is, e, w, x, y, z, side, posX, posY, posZ);
//		return super.onItemUse(is, e, w, x, y, z, side, posX, posY, posZ);
	}
	
	@Override
	public void onUpdate(ItemStack is, World w, Entity e, int slot, boolean b) {
		
		Item i = this.getTarget(is.getItemDamage());
		ItemStack nIs = new ItemStack(i, is.stackSize, is.getItemDamage());
		
		if (e instanceof EntityPlayer) {
			((EntityPlayer) e).inventory.setInventorySlotContents(slot, nIs);
		}
		i.onUpdate(is, w, e, slot, b);
	}
}
