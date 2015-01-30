package mods.gollum.proxyblock.common.blocks;

import mods.gollum.core.tools.helper.blocks.HBlockContainer;
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
	
	private Block[] targets = new Block[16];
	
	public BlockProxy(String registerName) {
		super(registerName, Material.grass);
		this.helper.setItemBlockClass(ItemProxy.class);
	}
	
	public BlockProxy setTarget(Block target) {
		for (int i = 0; i < 16; i++) {
			this.setTarget(target, i);
		}
		return this;
	}
	public BlockProxy setTarget(Block target, int metadata) {
		this.targets[metadata] = target;
		return this;
	}
	
	public Block getTarget() {
		return this.getTarget(0);
	}
	public Block getTarget(int metadata) {
		if (this.targets[metadata] != null) {
			return this.targets[metadata];
		}
		return Blocks.air;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if (this.getTarget(metadata) instanceof ITileEntityProvider) {
			return ((ITileEntityProvider) this.getTarget(metadata)).createNewTileEntity(world, metadata);
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
		return this.getTarget().getMaterial();
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return this.getTarget(metadata).getIcon(side, metadata);
	}
	
	////////////
	// Events //
	////////////
	
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack is) {
		Block t = this.getTarget(w.getBlockMetadata(x, y, z));
		w.setBlock(x, y, z, t);
		t.onBlockPlacedBy(w, x, y, z, e, is);
	}
	
}
