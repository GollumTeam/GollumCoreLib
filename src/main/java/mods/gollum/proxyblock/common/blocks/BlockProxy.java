package mods.gollum.proxyblock.common.blocks;

import static mods.gollum.proxyblock.ModGollumProxyBlock.log;
import mods.gollum.core.tools.helper.blocks.HBlockContainer;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.proxyblock.common.items.ItemProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockProxy extends HBlockContainer implements Cloneable {
	
	public static class ReplaceBlock {
		public Block   block;
		public Integer metadata;
		public int     number;
		
		public ReplaceBlock(Block block) {
			this(block, null);
		}
		public ReplaceBlock(Block block, Integer metadata) {
			this(block, metadata, 1);
		}
		public ReplaceBlock(Block block, Integer metadata, int number) {
			block    = this.block;
			metadata = this.metadata;
			number   = this.number;
		}
		
		public Object clone () {
			return new ReplaceBlock(this.block, this.metadata, this.number);
		}
	}
	
	private ReplaceBlock[] targets = new ReplaceBlock[16];
	
	public BlockProxy(String registerName) {
		super(registerName, Material.grass);
		this.helper.setItemBlockClass(ItemProxy.class);
	}

	public BlockProxy setTarget(ReplaceBlock target) {
		for (int i = 0; i < 16; i++) {
			this.setTarget(target, i);
		}
		return this;
	}
	public BlockProxy setTarget(ReplaceBlock target, int metadata) {
		this.targets[metadata] = target;
		return this;
	}
	public ReplaceBlock getTarget() {
		return this.getTarget(0);
	}
	public ReplaceBlock getTarget(int metadata) {
		if (this.targets[metadata] != null) {
			return this.targets[metadata];
		}
		return new ReplaceBlock(Blocks.grass);
	}

	public int getTargetMetadata() {
		return this.getTargetMetadata(0);
	}
	public int getTargetMetadata(int metadata) {
		metadata = this.getTarget(metadata).metadata;
		if (metadata > 0xF) {
			metadata = 0xF;
		}
		return metadata;
	}
	
	private void trace () {
		this.trace(0);
	}
	private void trace (int metadata) {
		log.message("Block transform \""+RegisteredObjects.instance().getRegisterName(this)+"\":"+metadata+" => \""+RegisteredObjects.instance().getRegisterName(this.getTarget(metadata).block)+"\":"+this.getTarget(metadata).metadata+"");
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
		return this.getTarget().block.getMaterial();
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return this.getTarget(metadata).block.getIcon(side, metadata);
	}
	
	////////////
	// Events //
	////////////
	
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase e, ItemStack is) {
		int   metadata  = w.getBlockMetadata(x, y, z);
		ReplaceBlock target = this.getTarget(metadata);
		w.setBlock(x, y, z, target.block, target.metadata, 2);
		this.trace(metadata);
	}
	
}
