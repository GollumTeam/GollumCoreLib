package mods.gollum.proxyblock.common.items;

import static mods.gollum.proxyblock.ModGollumProxyBlock.log;
import mods.gollum.core.tools.registered.RegisteredObjects;
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

//	private Item getTarget () {
//		return this.getTarget(0);
//	}
//	private Item getTarget (int damage) {
//		
//		if (damage > 15) {
//			damage = 0;
//		}
//		
//		Item i = Item.getItemFromBlock(this.block.getTarget(damage));
//		if (i != null) {
//			return i;
//		}
//		return Items.apple;
//	}

	private ItemStack replaceBlock (ItemStack is) {
		ItemStack nIs = new ItemStack(i, is.stackSize, is.getItemDamage());
		
		return this.block.getTarget(metadata);
	}
	
	private void trace () {
		this.trace(new ItemStack(this));
	}
	private void trace (ItemStack is) {
		ItemStack nIs = this.replaceBlock(is);
		log.message("Block transform \""+RegisteredObjects.instance().getRegisterName(this)+"\":"+is.getItemDamage()+" => \""+RegisteredObjects.instance().getRegisterName(nIs.getItem())+"\":"+nIs.getItemDamage()+"");
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getTarget().getUnlocalizedName()+"d";
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		ItemStack nIs = this.replaceBlock(is);
		return nIs.getItem().getUnlocalizedName(nIs);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer e, World w, int x, int y, int z, int side, float posX, float posY, float posZ) {
		ItemStack nIs = this.replaceBlock(is);
		this.trace(is);
		return nIs.getItem().onItemUse(is, e, w, x, y, z, side, posX, posY, posZ);
	}
	
	@Override
	public void onUpdate(ItemStack is, World w, Entity e, int slot, boolean b) {
		
		ItemStack nIs = this.replaceBlock(is);
		
		if (e instanceof EntityPlayer) {
			((EntityPlayer) e).inventory.setInventorySlotContents(slot, nIs);
			this.trace(is);
		}
		nIs.getItem().onUpdate(is, w, e, slot, b);
	}
}
