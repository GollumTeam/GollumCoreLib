package mods.gollum.proxyblock.common.blocks;

import static mods.gollum.proxyblock.ModGollumProxyBlock.log;
import mods.gollum.core.tools.helper.blocks.HBlockContainer;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.proxyblock.common.items.ItemProxy;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockProxy extends HBlockContainer {
	
	private ItemStack[] targets = new ItemStack[16];
	
	public BlockProxy(String registerName) {
		super(registerName, Material.grass);
		this.helper.setItemBlockClass(ItemProxy.class);
	}

	public BlockProxy setTarget(ItemStack target) {
		for (int i = 0; i < 16; i++) {
			this.setTarget(target, i);
		}
		return this;
	}
	public BlockProxy setTarget(ItemStack target, int metadata) {
		this.targets[metadata] = target;
		return this;
	}
	public ItemStack getTarget() {
		return this.getTarget(0);
	}
	public ItemStack getTarget(int metadata) {
		if (this.targets[metadata] != null) {
			return this.targets[metadata];
		}
		return new ItemStack(Blocks.grass);
	}

	public Block getTargetBlock() {
		return this.getTargetBlock(0);
	}
	public Block getTargetBlock(int metadata) {
		ItemStack is = this.getTarget(metadata);
		return Block.getBlockFromItem(is.getItem());
	}

	public int getTargetMetadata() {
		return this.getTargetMetadata(0);
	}
	public int getTargetMetadata(int metadata) {
		metadata = this.getTarget(metadata).getItemDamage();
		if (metadata > 0xF) {
			metadata = 0xF;
		}
		return metadata;
	}
	
	private void trace () {
		this.trace(0);
	}
	private void trace (int metadata) {
		log.message("Block transform \""+RegisteredObjects.instance().getRegisterName(this)+"\":"+metadata+" => \""+RegisteredObjects.instance().getRegisterName(this.getTargetBlock(metadata))+"\":"+this.getTargetMetadata(metadata)+"");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if (this.getTargetBlock(metadata) instanceof ITileEntityProvider) {
			return ((ITileEntityProvider) this.getTargetBlock(metadata)).createNewTileEntity(world, metadata);
		}
		return null;
	}
	
	//////////////////////////
	// Gestion des textures //
	//////////////////////////
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	}
	
	@Override
	public Material getMaterial() {
		return this.getTargetBlock().getMaterial();
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return this.getTargetBlock(metadata).getIcon(side, metadata);
	}
	
	////////////
	// Events //
	////////////
	
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack is) {
		int   metadata  = w.getBlockMetadata(x, y, z);
		Block tBlock    = this.getTargetBlock(metadata);
		int   tMetadata = this.getTargetMetadata(metadata);
		w.setBlock(x, y, z, tBlock, tMetadata, 2);
		this.trace(metadata);
	}
	
}
