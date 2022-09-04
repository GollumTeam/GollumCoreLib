package com.gollum.core.tools.helper;

import static net.minecraft.block.BlockPistonBase.EXTENDED;
import static net.minecraft.block.BlockPistonBase.FACING;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.items.HItemBlock;
import com.gollum.core.tools.helper.states.IEnumIndexed;
import com.gollum.core.tools.helper.states.IEnumSubBlock;
import com.gollum.core.tools.registry.BlockRegistry;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHelper implements IBlockHelper {
	
	public static abstract class PropertyIndex<T extends Enum<T> & IEnumIndexed> extends PropertyEnum<T> {

		protected PropertyIndex(String name, Class<T> valueClass, Collection<T> allowedValues) {
			super(name, valueClass, allowedValues);
		}
		
		public PropertyIndex(String name, Class<T> valueClass, T[] values) {
			this(name, valueClass, Lists.newArrayList(values));
		}
		
		public PropertyIndex(String name, Class<T> valueClass) {
			this(name, valueClass, valueClass.getEnumConstants());
		}
		
		public int getIndex (T value) {
			return ((IEnumIndexed)value).getIndex();
		}
		
		public T getEnumFromMeta(int meta) {
			TreeMap<Integer, T> sortedValue = new TreeMap<Integer, T>();
			for (T value : this.getAllowedValues()) {
				sortedValue.put(this.getIndex(value), value);
			}
			
			T lastMatch = sortedValue.get(0);
			for(Entry<Integer, T> entry : sortedValue.entrySet()) {
				if (entry.getKey()  <= meta) {
					lastMatch = entry.getValue();	
				} else {
					break;
				}
			}
			
			return lastMatch;
		}
	}
	
	public static abstract class PropertySubBlock<T extends Enum<T> & IEnumIndexed> extends PropertyIndex<T> {
		
		protected PropertySubBlock(String name, Class<T> valueClass, Collection<T> allowedValues) {
			super(name, valueClass, allowedValues);
		}
		
		public PropertySubBlock(String name, Class<T> valueClass, T[] values) {
			this(name, valueClass, Lists.newArrayList(values));
		}
		
		public PropertySubBlock(String name, Class<T> valueClass) {
			this(name, valueClass, valueClass.getEnumConstants());
		}
		
		public boolean isFacingPlane (T value) {
			return ((IEnumSubBlock)value).isFacingPlane();
		}
		
		public T[] getSubBlocksList () {
			T[] ts = (T[])Array.newInstance(this.getValueClass(), 0);
			return this.getAllowedValues().toArray(ts);
		}
	}
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helpers
	// Une sorte de config
	// Par defaut le helper vas enregistrer le block, charger des texture perso ...
	public boolean vanillaRegister = false;
	public boolean vanillaRegisterRender = false;
	
	protected GollumMod mod;
	protected Block parent;
	protected String registerName;
	protected Class<? extends ItemBlock> itemBlockClass = HItemBlock.class;
	
	public BlockHelper (Block parent, String registerName) {
		this.parent       = parent;
		this.registerName = registerName;
		this.mod          = ModContext.instance().getCurrent();
		
		BlockRegistry.instance().add((IBlockHelper) this.parent);
		this.parent.setUnlocalizedName(this.registerName);
		
	}
	
	@Override
	public BlockHelper getGollumHelper() {
		return this;
	}
	
	////////////
	// States //
	////////////
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		
		int metaFacing = meta;
		IBlockState state = this.parent.getDefaultState();
		PropertySubBlock propSubBlock = ((IBlockHelper)this.parent).getPropSubBlock(state);
		PropertyDirection propFacing = ((IBlockHelper)this.parent).getPropFacing(state);
		Enum valueSubBlock = null;
		
		for (IProperty prop: state.getProperties().keySet()) {
			if (prop instanceof PropertyIndex) {
				PropertyIndex propIndex = (PropertyIndex)prop;
				Enum value = propIndex.getEnumFromMeta(meta);
				
				if (propIndex instanceof PropertySubBlock) {
					valueSubBlock = value;
				}
				
				state = state.withProperty(propIndex, value);
				metaFacing -= propIndex.getIndex(value);
			}
		}
		
		if (metaFacing < 0) {
			System.out.println("error");
		}
		
		if(propFacing != null && (propSubBlock == null || valueSubBlock == null || propSubBlock.isFacingPlane(valueSubBlock))) {
			int size = propFacing.getAllowedValues().size();
			if (size == 4) {
				state = state.withProperty(propFacing, EnumFacing.HORIZONTALS[metaFacing % 4]);
			} else {
				state = state.withProperty(propFacing, EnumFacing.VALUES[metaFacing % 6]);
			}
		}
		
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		int metadata = 0;
		PropertySubBlock propSubBlock = ((IBlockHelper)this.parent).getPropSubBlock(state);
		PropertyDirection propFacing = ((IBlockHelper)this.parent).getPropFacing(state);
		Enum valueSubBlock = null;
		
		for (IProperty prop: state.getProperties().keySet()) {
			if (prop instanceof PropertyIndex) {
				PropertyIndex propIndex = (PropertyIndex)prop;
				Enum value = (Enum)state.getValue(propIndex);
				
				if (propIndex instanceof PropertySubBlock) {
					valueSubBlock = value;
				}
				
				metadata += propIndex.getIndex(value);
			}
		}

		if(propFacing != null && (propSubBlock == null || valueSubBlock == null || propSubBlock.isFacingPlane(valueSubBlock))) {
			int size = propFacing.getAllowedValues().size();
			if (size == 4) {
				metadata += ((EnumFacing)state.getValue(propFacing)).getHorizontalIndex();
			} else {
				metadata += ((EnumFacing)state.getValue(propFacing)).getIndex();
			}
		}
		
		return metadata;
	}
	
	@Override
	public void getSubNames(Map<Integer, String> list) {
		
		IBlockState state = this.parent.getDefaultState();
		PropertySubBlock propSubBlock = ((IBlockHelper)this.parent).getPropSubBlock(state);
		
		if (propSubBlock != null) {
			for (Enum value : propSubBlock.getSubBlocksList()) {
				list.put(propSubBlock.getIndex(value), propSubBlock.getName(value));
			}
		}
	}
	
	@Override
	public PropertySubBlock getPropSubBlock(IBlockState state) {
		for (IProperty prop: state.getProperties().keySet()) {
			if (prop instanceof PropertySubBlock) {
				return (PropertySubBlock)prop;
			}
		}
		return null;
	}
	
	@Override
	public PropertyDirection getPropFacing(IBlockState state) {
		for (IProperty prop: state.getProperties().keySet()) {
			if (prop.getName() == "facing" && prop instanceof PropertyDirection) {
				return (PropertyDirection)prop;
			}
		}
		return null;
	}
	
	//////////////
	// Register //
	//////////////
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register () {
		if(vanillaRegister) return;
		this.parent.setUnlocalizedName(this.getRegisterName());
		this.parent.setRegistryName(this.getRegisterName());
		ForgeRegistries.BLOCKS.register(this.parent);
	}
	
	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		if(this.vanillaRegisterRender) return;
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IBlockHelper)this.parent).getSubNames(map);
		TreeSet<Integer> registered  = new TreeSet<Integer>();
		
		if (map.isEmpty()) {
			this.registerRender(0);
		} else {
			for (Integer metadata :map.keySet()) {
				this.registerRender(metadata);
			}
//			for (Entry<Integer, String> entry :map.entrySet()) {
//				if (!registered.contains(entry.getKey())) {
//					ModelResourceLocation model = this.getModelResourceLocation(entry.getValue());
//					ModelBakery.registerItemVariants(this.getBlockItem(), model);
//				}
//			}
//			for (Entry<Integer, String> entry :map.entrySet()) {
//				if (!registered.contains(entry.getKey())) {
//					this.registerRender(entry.getKey(), entry.getValue());
//				}
//			}
		}
	}
	
	public void registerRender (int metadata) {
		ModGollumCoreLib.logger.message("Auto register render: "+this.parent.getRegistryName());
		GollumMod mod = ModContext.instance().getCurrent(); 
	    ModelLoader.setCustomModelResourceLocation(this.getBlockItem(), metadata, new ModelResourceLocation(this.parent.getRegistryName(), "inventory"));
	}
	
//	public void registerRender (int metadata, String renderKey) {
//		this.registerRender(metadata, renderKey, true);
//	}
//	
//	public void registerRender (int metadata, String renderKey, boolean trace) {
//		if (trace) ModGollumCoreLib.logger.message("Auto register render: "+this.getRegisterName()+":"+metadata+":"+":"+renderKey);
//		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this.getBlockItem(), metadata, this.getModelResourceLocation(renderKey));
//	}
	
	protected ModelResourceLocation getModelResourceLocation (String renderKey) {
		return new ModelResourceLocation(this.mod.getModId()+":"+renderKey, "inventory");
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return registerName;
	}
	
	////////////
	// Events //
	////////////

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase player, EnumHand hand) {
		IBlockState state = parent.getStateFromMeta(meta);
		PropertyDirection propFacing = ((IBlockHelper)this.parent).getPropFacing(state);
		if (propFacing != null) {
			state = state.withProperty(propFacing, ((IBlockHelper)this.parent).getOrientationForPlayer(pos, player));
		}
		return state;
	}
	
	/**
	 * Libère les items de l'inventory
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		
		Random random = new Random();		
		TileEntity te = world.getTileEntity(pos);
		
		if (te != null && te instanceof IInventory) {
			IInventory inventory = (IInventory)te;
			
			for (int i = 0; i < inventory.getSizeInventory(); ++i) {
				ItemStack itemStack = inventory.getStackInSlot(i);
				
				if (itemStack != null) {
					float f  = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem entityItem;
					
					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemStack.getCount() > 0; world.spawnEntity(entityItem)) {
						int k1 = random.nextInt(21) + 10;
						
						if (k1 > itemStack.getCount()) {
							k1 = itemStack.getCount();
						}
						
						itemStack.setCount(itemStack.getCount() - k1);
						entityItem = new EntityItem(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));
						float f3 = 0.05F;
						entityItem.motionX = (double) ((float) random.nextGaussian() * f3);
						entityItem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
						entityItem.motionZ = (double) ((float) random.nextGaussian() * f3);
						
						if (itemStack.hasTagCompound()) {
							entityItem.getItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
						}
					}
				}
			}
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return null;
//		IItemHelper item = (IItemHelper)((IBlockHelper)this.parent).getBlockItem();
//		int matadata = item.getEnabledMetadata(this.parent.getMetaFromState(world.getBlockState(pos)));
//		return new ItemStack(this.parent, 1, matadata);
	}
	
	////////////
	// Others //
	////////////
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return Item.getItemFromBlock(this.parent);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs ctab, NonNullList<ItemStack> items) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IBlockHelper)this.parent).getSubNames(map);
		
		if (map.isEmpty()) {
			items.add(new ItemStack(this.getBlockItem(), 1, 0));
			return;
		}
		
		for (Entry<Integer, String> entry: map.entrySet()) {
			items.add(new ItemStack(this.getBlockItem(), 1, entry.getKey()));
		}
	}
	
	@Override
	public EnumFacing getOrientationForPlayer(BlockPos clickedBlock, Entity player) {
		
		IBlockState state = this.parent.getDefaultState();
		PropertyDirection propFacing = ((IBlockHelper)this.parent).getPropFacing(state);
		
		if (propFacing != null) {
			
			Collection<EnumFacing> values = propFacing.getAllowedValues();
			
			if (MathHelper.abs((float)player.posX - (float)clickedBlock.getX()) < 2.0F && MathHelper.abs((float)player.posZ - (float)clickedBlock.getZ()) < 2.0F)
			{
				double orientation = player.posY + (double)player.getEyeHeight();
				
				if (values.contains(EnumFacing.UP) && orientation - (double)clickedBlock.getY() > 2.0D) {
					return EnumFacing.UP;
				}
				
				if (values.contains(EnumFacing.UP) && (double)clickedBlock.getY() - orientation > 0.0D) {
					return EnumFacing.DOWN;
				}
			}
			EnumFacing result = player.getHorizontalFacing().getOpposite();
			if (values.contains(result)) {
				return result;
			}
		}
		
		return EnumFacing.NORTH;
	}
	
}