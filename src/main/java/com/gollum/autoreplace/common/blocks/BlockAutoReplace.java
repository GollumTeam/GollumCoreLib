package com.gollum.autoreplace.common.blocks;

import static com.gollum.autoreplace.ModGollumAutoReplace.log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import com.gollum.autoreplace.ModGollumAutoReplace;
import com.gollum.autoreplace.common.items.ItemAutoReplace;
import com.gollum.core.tools.helper.blocks.HBlockContainer;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.utils.reflection.DeobfuscateName;
import com.gollum.core.utils.reflection.Reflection;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAutoReplace extends HBlockContainer implements Cloneable {
	
	public static class ReplaceBlock {
		public  String  registerName;
		private Integer metadata;
		
		public ReplaceBlock(Block block) {
			this(RegisteredObjects.instance().getRegisterName(block));
		}
		
		public ReplaceBlock(Block block, Integer metadata) {
			this(RegisteredObjects.instance().getRegisterName(block), metadata);
		}
		public ReplaceBlock(String registerName) {
			this(registerName, null);
		}
		public ReplaceBlock(String registerName, Integer metadata) {
			this.registerName = registerName;
			this.metadata     = metadata;
		}
		
		public Block getBlock() {
			Block b = RegisteredObjects.instance().getBlock(this.registerName);
			if (b != null) {
				return b;
			}
			return Blocks.grass;
		}
		
		public Item getItem() {
			return Item.getItemFromBlock(this.getBlock());
		}
		
		public int getMetadata(int metadata) {
			if (this.metadata != null && this.metadata != -1) {
				metadata = this.metadata;
			}
			if (metadata > 0xF) {
				metadata = 0x0;
			}
			return metadata;
		}
		
		public static ReplaceBlock get(BlockAutoReplace proxy) {
			return get(proxy, 0);
		}
		
		public static ReplaceBlock get(Block b, int metadata) {
			
			if (!(b instanceof BlockAutoReplace)) {
				return new ReplaceBlock(b, metadata);
			}
			
			BlockAutoReplace proxy = (BlockAutoReplace) b;
			
			if (proxy.targets != null) {
				if (metadata >= proxy.targets.length) {
					metadata = 0;
				}
				if (proxy.targets[metadata] != null) {
					return proxy.targets[metadata];
				}
				
				if (proxy.targets.length > 1 && proxy.targets[0] != null) {
					return proxy.targets[0];
				}
			}
			
			return new ReplaceBlock(Blocks.grass);
		}
		
		public Object clone() {
			return new ReplaceBlock(this.registerName, this.metadata);
		}
	}
	
	private String modId;
	private ReplaceBlock[] targets = new ReplaceBlock[0xF];
	
	private boolean replaceMutex = true;

	public BlockAutoReplace(String modId, String registerName, ReplaceBlock target) {
		this(modId, registerName, new ReplaceBlock[] {
			target, target, target, target, target,
			target, target, target, target, target,
			target, target, target, target, target,
		});
	}
	
	public BlockAutoReplace(String modId, String registerName, ReplaceBlock[] targets) {
		super(registerName, Material.grass);
		
		this.modId = modId;
		
		for (int i = 0; i < 0xF && i < targets.length; i++) {
			this.setTarget(targets[i], i);
		}
	}
	
	/**
	 * Enregistrement du item. Appelé a la fin du postInit
	 */
	public void register () {
		
		ModContainer mc = Loader.instance().activeModContainer();
		HashMap<String, Object> descriptor = new HashMap<String, Object>();
		try {
			Field f = mc.getClass().getDeclaredField("descriptor");
			f.setAccessible(true);
			descriptor = (HashMap<String, Object>) f.get(mc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		descriptor.put("modid", this.modId);
		GameRegistry.registerBlock (this, ItemAutoReplace.class, getRegisterName ());
		descriptor.put("modid", ModGollumAutoReplace.MODID);
	}
	
	private void setTarget(ReplaceBlock target, int metadata) {
		this.targets[metadata] = target;
	}
	
	private void trace (int x, int y, int z) {
		this.trace(0, x, y, z);
	}
	private void trace (int metadata, int x, int y, int z) {
		log.message("Block transform x="+x+", y="+y+", z="+z+" ,\""+RegisteredObjects.instance().getRegisterName(this)+"\":"+metadata+"("+Block.getIdFromBlock(this)+") => \""+ReplaceBlock.get(this, metadata).registerName+"\":"+ReplaceBlock.get(this, metadata).getMetadata(metadata)+"("+Block.getIdFromBlock(ReplaceBlock.get(this, metadata).getBlock())+")");
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if (ReplaceBlock.get(this, metadata).getBlock() instanceof ITileEntityProvider) {
			return ((ITileEntityProvider) ReplaceBlock.get(this, metadata).getBlock()).createNewTileEntity(world, ReplaceBlock.get(this, metadata).getMetadata(metadata));
		}
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		return t.getBlock().createTileEntity(world, t.getMetadata(metadata));
	}
	
	private void replaceBlock(World w, int x, int y, int z) {
		
		if (this.replaceMutex) {
			
			this.replaceMutex = false;
			
			Block      block    = w.getBlock(x, y, z);
			int        metadata = w.getBlockMetadata(x, y, z);
			TileEntity te       = w.getTileEntity(x, y, z);
			
			ItemStack[] itemStacks = new ItemStack[0];
			
			try {
				
				if (te instanceof IInventory) {
					
					IInventory inventory = (IInventory) te;
					
					itemStacks = new ItemStack[inventory.getSizeInventory()];
					for (int i = 0; i < inventory.getSizeInventory(); i++) {
						itemStacks[i] = inventory.getStackInSlot(i);
						inventory.setInventorySlotContents(i, null);;
					}
				}
				
				ReplaceBlock target = ReplaceBlock.get(block, metadata);
				w.setBlock(x, y, z, target.getBlock(), target.getMetadata(metadata), 2);
				w.setTileEntity(x, y, z, te);
				te = w.getTileEntity(x, y, z);
				
				this.trace(metadata, x, y, z);
				
				if (te instanceof IInventory) {
					
					IInventory inventory = (IInventory) te;
					
					for (int i = 0; i < inventory.getSizeInventory() && i < itemStacks.length; i++) {
						inventory.setInventorySlotContents(i, itemStacks[i]);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			this.replaceMutex = true;
		}
	}
	
	//////////////////////////
	// Gestion des textures //
	//////////////////////////
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
	}
	
	@Override
	public Material getMaterial() {
		return ReplaceBlock.get(this).getBlock().getMaterial();
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return ReplaceBlock.get(this, metadata).getBlock().getIcon(side, ReplaceBlock.get(this,metadata).getMetadata(metadata));
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon func_149735_b(int side, int metadata) {
		return ReplaceBlock.get(this, metadata).getBlock().func_149735_b(side, ReplaceBlock.get(this,metadata).getMetadata(metadata));
	}
	
	@Override
	public Item getItemDropped(int metadata, Random r, int p_149650_3_) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		return t.getBlock().getItemDropped(t.getMetadata(metadata), r, p_149650_3_);
	}
	
	@Override
	public int damageDropped(int metadata) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		return t.getBlock().damageDropped(t.getMetadata(metadata));
	}
	
	@Override
	@DeobfuscateName("createStackedBlock")
	protected ItemStack createStackedBlock(int metadata) {

		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		try {
			Method m = Reflection.getObfuscateMethod(t.getBlock().getClass(), BlockAutoReplace.class, "createStackedBlock");
			m.setAccessible(true);
			return (ItemStack) m.invoke(t.getBlock(), t.getMetadata(metadata));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.createStackedBlock(t.getMetadata(metadata));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderColor(int metadata) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		return t.getBlock().getRenderColor(t.getMetadata(metadata));
	}
	
	@Override
	public int quantityDroppedWithBonus(int metadata, Random r) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		return t.getBlock().quantityDroppedWithBonus(t.getMetadata(metadata), r);
	}
	
	public boolean isFireSource(World w, int x, int y, int z, ForgeDirection side) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().isFireSource(w, x, y, z, side);
	}
	
	@Override
	public void fillWithRain(World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().fillWithRain(w, x, y, z);
	}
	
	@Override
	public int getComparatorInputOverride(World w, int x, int y, int z, int p_149736_5_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().getComparatorInputOverride(w, x, y, z, p_149736_5_);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World w, int x, int y, int z, int metadata, int fortune) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		w.setBlock(x, y, z, t.getBlock(), t.getMetadata(metadata), 2);
		return t.getBlock().getDrops(w, x, y, z, t.getMetadata(metadata), fortune);
	}
	
	@Override
	public boolean canSilkHarvest(World w, EntityPlayer player, int x, int y, int z, int metadata) {
		ReplaceBlock t = ReplaceBlock.get(this, metadata);
		w.setBlock(x, y, z, t.getBlock(), t.getMetadata(metadata), 2);
		return t.getBlock().canSilkHarvest(w, player, x, y, z, t.getMetadata(metadata));
	}
	
	////////////
	// Events //
	////////////

	@Override
	public void updateTick(World w, int x, int y, int z, Random r) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().updateTick(w, x, y, z, r);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().getPickBlock(target, w, x, y, z);
	}
	
	@Override
	public float getBlockHardness(World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().getBlockHardness(w, x, y, z);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World w, int x, int y, int z, int p_149664_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockDestroyedByPlayer(w, x, y, z, p_149664_5_);
	}
	
	@Override
	public void onNeighborBlockChange(World w, int x, int y, int z, Block p_149695_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onNeighborBlockChange(w, x, y, z, p_149695_5_);
	}
	
	@Override
	public void onBlockAdded(World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockAdded(w, x, y, z);
	}
	
	@Override
	public void breakBlock(World w, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().breakBlock(w, x, y, z, p_149749_5_, p_149749_6_);
	}
	
	@Override
	public void dropBlockAsItemWithChance(World w, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().dropBlockAsItemWithChance(w, x, y, z, p_149690_5_, p_149690_6_, p_149690_7_);
	}
	
	@Override
	@DeobfuscateName("dropBlockAsItem")
	protected void dropBlockAsItem(World w, int x, int y, int z, ItemStack p_149642_5_) {
		replaceBlock(w, x, y, z);
		Block b = ReplaceBlock.get(this).getBlock();
		try {
			Method m = Reflection.getObfuscateMethod(b.getClass(), BlockAutoReplace.class, "dropBlockAsItem");
			m.setAccessible(true);
			m.invoke(b, w, x, y, z, p_149642_5_);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dropXpOnBlockBreak(World w, int x, int y, int z, int p_149657_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().dropXpOnBlockBreak(w, z, y, z, p_149657_5_);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World w, int x, int y, int z, Explosion p_149723_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockDestroyedByExplosion(w, z, y, z, p_149723_5_);
		
	}
	
	@Override
	public boolean canReplace(World w, int x, int y, int z, int p_149705_5_, ItemStack p_149705_6_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().canReplace(w, z, y, z, p_149705_5_, p_149705_6_);
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World w, int x, int y, int z, int p_149707_5_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().canPlaceBlockOnSide(w, x, y, z, p_149707_5_);
	}
	
	@Override
	public boolean canPlaceBlockAt(World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().canPlaceBlockAt(w, x, y, z);
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().onBlockActivated(w, x, y, z, p_149727_5_, p_149727_6_, p_149727_7_, p_149727_8_, p_149727_9_);
	}
	
	@Override
	public void onEntityWalking(World w, int x, int y, int z, Entity p_149724_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onEntityWalking(w, x, y, z, p_149724_5_);;
	}
	
	@Override
	public int onBlockPlaced(World w, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().onBlockPlaced(w, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
	}
	
	@Override
	public void onBlockClicked(World w, int x, int y, int z, EntityPlayer p_149699_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockClicked(w, x, y, z, p_149699_5_);;
	}
	
	@Override
	public void velocityToAddToEntity(World w, int x, int y, int z, Entity p_149640_5_, Vec3 p_149640_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().velocityToAddToEntity(w, x, y, z, p_149640_5_, p_149640_6_);;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer p_149737_1_, World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().getPlayerRelativeBlockHardness(p_149737_1_, w, x, y, z);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World w, int x, int y, int z, Entity p_149670_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onEntityCollidedWithBlock(w, x, y, z, p_149670_5_);;
	}
	
	public void harvestBlock(World w, EntityPlayer p_149636_2_, int x, int y, int z, int p_149636_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().harvestBlock(w, p_149636_2_, x, y, z, p_149636_6_);;
	}
	
	@Override
	public boolean canBlockStay(World w, int x, int y, int z) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().canBlockStay(w, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockPlacedBy(w, x, y, z, p_149689_5_, p_149689_6_);
	}
	
	@Override
	public void onPostBlockPlaced(World w, int x, int y, int z, int p_149714_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onPostBlockPlaced(w, x, y, z, p_149714_5_);
	}
	
	@Override
	public boolean onBlockEventReceived(World w, int x, int y, int z, int p_149696_5_, int p_149696_6_) {
		replaceBlock(w, x, y, z);
		return ReplaceBlock.get(this).getBlock().onBlockEventReceived(w, x, y, z, p_149696_5_, p_149696_6_);
	}
	
	@Override
	public void onFallenUpon(World w, int x, int y, int z, Entity p_149746_5_, float p_149746_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onFallenUpon(w, x, y, z, p_149746_5_, p_149746_6_);
	}
	
	@Override
	public void onBlockHarvested(World w, int x, int y, int z, int p_149681_5_, EntityPlayer p_149681_6_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockHarvested(w, x, y, z, p_149681_5_, p_149681_6_);
	}
	
	@Override
	public void onBlockPreDestroy(World w, int x, int y, int z, int p_149725_5_) {
		replaceBlock(w, x, y, z);
		ReplaceBlock.get(this).getBlock().onBlockPreDestroy(w, x, y, z, p_149725_5_);
	}
	
	public void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ) {
		replaceBlock(world, x, y, z);
		ReplaceBlock.get(this).getBlock().onPlantGrow(world, x, y, z, sourceX, sourceY, sourceZ);
	}
	
	public boolean isFertile(World world, int x, int y, int z) {
		replaceBlock(world, x, y, z);
		return ReplaceBlock.get(this).getBlock().isFertile(world, x, y, z);
	}
	
	////////////////////
	// Proxy de Block //
	////////////////////
	
	public boolean func_149730_j() {
		return ReplaceBlock.get(this).getBlock().func_149730_j();
	}
	
	public int getLightOpacity() {
		return ReplaceBlock.get(this).getBlock().getLightOpacity();
	}
	
	@SideOnly(Side.CLIENT)
	public boolean getCanBlockGrass() {
		return ReplaceBlock.get(this).getBlock().getCanBlockGrass();
	}
	
	public int getLightValue() {
		return ReplaceBlock.get(this).getBlock().getLightValue();
	}
	
	public boolean getUseNeighborBrightness() {
		return ReplaceBlock.get(this).getBlock().getUseNeighborBrightness();
	}
	
	public MapColor getMapColor(int p_149728_1_) {
		return ReplaceBlock.get(this).getBlock().getMapColor(p_149728_1_);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean isBlockNormalCube() {
		return ReplaceBlock.get(this).getBlock().isBlockNormalCube();
	}
	
	public boolean isNormalCube() {
		return ReplaceBlock.get(this).getBlock().isNormalCube();
	}
	
	public boolean renderAsNormalBlock() {
		return ReplaceBlock.get(this).getBlock().renderAsNormalBlock();
	}
	
	public boolean getBlocksMovement(IBlockAccess w, int x, int y, int z) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getBlocksMovement(w, x, y, z);
	}
	
	public int getRenderType() {
		return ReplaceBlock.get(this).getBlock().getRenderType();
	}
	
	public boolean getTickRandomly() {
		return ReplaceBlock.get(this).getBlock().getTickRandomly();
	}
	
	@Deprecated //Forge: New Metadata sensitive version.
	public boolean hasTileEntity() {
		return ReplaceBlock.get(this).getBlock().hasTileEntity();
	}
	
	@SideOnly(Side.CLIENT)
	public int getMixedBrightnessForBlock(IBlockAccess w, int x, int y, int z) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getMixedBrightnessForBlock(w, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess w, int x, int y, int z, int p_149646_5_) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().shouldSideBeRendered(w, x, y, z, p_149646_5_);
	}
	
	public boolean isBlockSolid(IBlockAccess w, int x, int y, int z, int p_149747_5_) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().isBlockSolid(w, x, y, z, p_149747_5_);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess w, int x, int y, int z, int p_149673_5_) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getIcon(w, x, y, z, p_149673_5_);
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World w, int x, int y, int z) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getCollisionBoundingBoxFromPool(w, x, y, z);
	}
	
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int x, int y, int z) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getSelectedBoundingBoxFromPool(w, x, y, z);
	}
	
	public boolean isOpaqueCube() {
		return ReplaceBlock.get(this).getBlock().isOpaqueCube();
	}
	
	public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
		return ReplaceBlock.get(this).getBlock().canCollideCheck(p_149678_1_, p_149678_2_);
	}
	
	public boolean isCollidable() {
		return ReplaceBlock.get(this).getBlock().isCollidable();
	}
	
	public int tickRate(World p_149738_1_) {
		return ReplaceBlock.get(this).getBlock().tickRate(p_149738_1_);
	}
	
	public int quantityDropped(Random p_149745_1_) {
		return ReplaceBlock.get(this).getBlock().quantityDropped(p_149745_1_);
	}
	
	public float getExplosionResistance(Entity p_149638_1_) {
		return ReplaceBlock.get(this).getBlock().getExplosionResistance(p_149638_1_);
	}
	
	public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 p_149731_5_, Vec3 p_149731_6_) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().collisionRayTrace(w, x, y, z, p_149731_5_, p_149731_6_);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return ReplaceBlock.get(this).getBlock().getRenderBlockPass();
	}
	
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return ReplaceBlock.get(this).getBlock().getBlockColor();
	}
	
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_) {
		return ReplaceBlock.get(this).getBlock().colorMultiplier(p_149720_1_, p_149720_2_, p_149720_3_, p_149720_4_);
	}
	
	public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_) {
		return ReplaceBlock.get(this).getBlock().isProvidingWeakPower(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, p_149709_5_);
	}
	
	public boolean canProvidePower() {
		return ReplaceBlock.get(this).getBlock().canProvidePower();
	}
	
	public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_) {
		return ReplaceBlock.get(this).getBlock().isProvidingStrongPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
	}
	
	@DeobfuscateName("canSilkHarvest")
	protected boolean canSilkHarvest() {
		Block b = ReplaceBlock.get(this).getBlock();
		try {
			Method m = Reflection.getObfuscateMethod(b.getClass(), BlockAutoReplace.class, "canSilkHarvest");
			m.setAccessible(true);
			return (Boolean) m.invoke(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return super.canSilkHarvest();
	}
	
	public boolean getEnableStats() {
		return ReplaceBlock.get(this).getBlock().getEnableStats();
	}
	
	public int getMobilityFlag() {
		return ReplaceBlock.get(this).getBlock().getMobilityFlag();
	}
	
	@SideOnly(Side.CLIENT)
	public float getAmbientOcclusionLightValue() {
		return ReplaceBlock.get(this).getBlock().getAmbientOcclusionLightValue();
	}
	
	public int getDamageValue(World w, int x, int y, int z) {
		return ReplaceBlock.get(this, w.getBlockMetadata(x, y, z)).getBlock().getDamageValue(w, x, y, z);
	}

	@SideOnly(Side.CLIENT)
	public boolean isFlowerPot() {
		return ReplaceBlock.get(this).getBlock().isFlowerPot();
	}

	public boolean func_149698_L() {
		return ReplaceBlock.get(this).getBlock().func_149698_L();
	}
	
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return ReplaceBlock.get(this).getBlock().canDropFromExplosion(p_149659_1_);
	}
	
	public boolean isAssociatedBlock(Block p_149667_1_) {
		return ReplaceBlock.get(this).getBlock().isAssociatedBlock(p_149667_1_);
	}
	
	public boolean hasComparatorInputOverride() {
		return ReplaceBlock.get(this).getBlock().hasComparatorInputOverride();
	}
	
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this).getBlock().getLightValue();
	}

	@DeobfuscateName("getTextureName")
	protected String getTextureName() {

		ReplaceBlock t = ReplaceBlock.get(this);
		try {
			Method m = Reflection.getObfuscateMethod(t.getBlock().getClass(), BlockAutoReplace.class, "getTextureName");
			m.setAccessible(true);
			return (String) m.invoke(t.getBlock());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getTextureName();
	}
	
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isLadder(world, x, y, z, entity);
	}

	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isNormalCube(world, x, y, z);
	}
	
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isSideSolid(world, x, y, z, side);
	}
	
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isReplaceable(world, x, y, z);
	}

	public boolean isBurning(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isBurning(world, x, y, z);
	}
	
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isAir(world, x, y, z);
	}
	
	public boolean canHarvestBlock(EntityPlayer player, int meta) {
		return ReplaceBlock.get(this).getBlock().canHarvestBlock(player, meta);
	}
	
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getFlammability(world, x, y, z, face);
	}
	
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isFlammable(world, x, y, z, face);
	}
	
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getFireSpreadSpeed(world, x, y, z, face);
	}
	
	public boolean hasTileEntity(int metadata) {
		ReplaceBlock t =  ReplaceBlock.get(this, metadata);
		return t.getBlock().hasTileEntity(t.getMetadata(metadata));
	}
	
	public int quantityDropped(int meta, int fortune, Random random) {
		ReplaceBlock t =  ReplaceBlock.get(this, meta);
		return t.getBlock().quantityDropped(t.getMetadata(meta), fortune, random);
	}
	
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().canCreatureSpawn(type, world, x, y, z);
	}
	
	public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isBed(world, x, y, z, player);
	}
	
	public ChunkCoordinates getBedSpawnPosition(IBlockAccess world, int x, int y, int z, EntityPlayer player) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getBedSpawnPosition(world, x, y, z, player);
	}

	
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
	}
	

	public int getBedDirection(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getBedDirection(world, x, y, z);
	}
	

	public boolean isBedFoot(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isBedFoot(world, x, y, z);
	}

    public void beginLeavesDecay(World world, int x, int y, int z){}
	
	public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().canSustainLeaves(world, x, y, z);
	}
	
	public boolean isFoliage(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().isFoliage(world, x, y, z);
	}
	



	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().canSustainPlant(world, x, y, z, direction, plantable);
	}
	
	public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getLightOpacity(world, x, y, z);
	}
	
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().canEntityDestroy(world, x, y, z, entity);
	}
	
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return ReplaceBlock.get(this, worldObj.getBlockMetadata(x, y, z)).getBlock().isBeaconBase(worldObj, x, y, z, beaconX, beaconY, beaconZ);
	}
	
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis) {
		return ReplaceBlock.get(this, worldObj.getBlockMetadata(x, y, z)).getBlock().rotateBlock(worldObj, x, y, z, axis);
	}
	
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y, int z) {
		return ReplaceBlock.get(this, worldObj.getBlockMetadata(x, y, z)).getBlock().getValidRotations(worldObj, x, y, z);
	}
	
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getEnchantPowerBonus(world, x, y, z);
	}
	
	public boolean recolourBlock(World world, int x, int y, int z, ForgeDirection side, int colour) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().recolourBlock(world, x, y, z, side, colour);
	}
	
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		ReplaceBlock t =  ReplaceBlock.get(this, metadata);
		return t.getBlock().getExpDrop(world, t.getMetadata(metadata), fortune);
	}
	
	public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().shouldCheckWeakPower(world, x, y, z, side);
	}
	
	public boolean getWeakChanges(IBlockAccess world, int x, int y, int z) {
		return ReplaceBlock.get(this, world.getBlockMetadata(x, y, z)).getBlock().getWeakChanges(world, x, y, z);
	}
	
	public boolean isToolEffective(String type, int metadata) {
		ReplaceBlock t =  ReplaceBlock.get(this, metadata);
		return t.getBlock().isToolEffective(type, t.getMetadata(metadata));
	}
	
}
