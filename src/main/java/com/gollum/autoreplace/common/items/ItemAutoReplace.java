package com.gollum.autoreplace.common.items;

import static com.gollum.autoreplace.ModGollumAutoReplace.log;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.gollum.autoreplace.common.blocks.BlockAutoReplace;
import com.gollum.autoreplace.common.blocks.BlockAutoReplace.ReplaceBlock;
import com.gollum.core.tools.registered.RegisteredObjects;

public class ItemAutoReplace extends ItemBlock {
	
	BlockAutoReplace block;
	
	public ItemAutoReplace(Block block) {
		super(block);
		this.block = (BlockAutoReplace) block;
	}
	
	private ReplaceBlock getTarget () {
		return this.getTarget(0);
	}
	private ReplaceBlock getTarget (int damage) {
		return ReplaceBlock.get(this.block, damage);
	}
	private ItemStack replaceItemStack (ItemStack is) {
		int metadata = is.getItemDamage();
		ReplaceBlock replaceBlock = ReplaceBlock.get(this.block, metadata);
		return new ItemStack(replaceBlock.getBlock(), is.stackSize, replaceBlock.getMetadata(metadata));
	}
	
	private void trace () {
		this.trace(new ItemStack(this));
	}
	private void trace (ItemStack is) {
		ItemStack nIs = this.replaceItemStack(is);
		log.message("Block transform \""+RegisteredObjects.instance().getRegisterName(this)+"\":"+is.getItemDamage()+" => \""+RegisteredObjects.instance().getRegisterName(nIs.getItem())+"\":"+nIs.getItemDamage()+"");
	}
	
	@Override
	public IIcon getIcon(ItemStack is, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		ItemStack nIs        = this.replaceItemStack(is);
		ItemStack nUsingItem = this.replaceItemStack(usingItem);
		return nIs.getItem().getIcon(is, renderPass, player, nUsingItem, useRemaining);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getTarget().getItem().getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		ItemStack nIs = this.replaceItemStack(is);
		return nIs.getItem().getUnlocalizedName(nIs);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer e, World w, int x, int y, int z, int side, float posX, float posY, float posZ) {
		ItemStack nIs = this.replaceItemStack(is);
		this.trace(is);
		return nIs.getItem().onItemUse(is, e, w, x, y, z, side, posX, posY, posZ);
	}
	
	@Override
	public void onUpdate(ItemStack is, World w, Entity e, int slot, boolean b) {
		
		ItemStack nIs = this.replaceItemStack(is);
		
		if (e instanceof EntityPlayer) {
			((EntityPlayer) e).inventory.setInventorySlotContents(slot, nIs);
			this.trace(is);
		}
		nIs.getItem().onUpdate(is, w, e, slot, b);
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack is) {
		ItemStack nIs = this.replaceItemStack(is);
		return nIs.getItem().getContainerItem(nIs);
	}
	
}
