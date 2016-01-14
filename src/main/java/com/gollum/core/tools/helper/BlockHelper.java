package com.gollum.core.tools.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.registry.BlockRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHelper implements IBlockHelper {
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helpers
	// Une sorte de config
	// Par defaut le helper vas enregistrer le block, charger des texture perso ...
	public boolean vanillaRegister      = false;
	public boolean vanillaPicked        = false;
	public boolean vanillaDamageDropped = false;
	
	protected GollumMod mod;
	protected Block parent;
	protected String registerName;
	protected Class<? extends ItemBlock> itemBlockClass = ItemBlock.class;
	
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
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 */
	@Override
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass) {
		this.itemBlockClass = itemClass;
		return this.parent;
	}
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register () {
		
		if(vanillaRegister) return;
		GameRegistry.registerBlock (this.parent , this.itemBlockClass , this.getRegisterName ());
	}
	
	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IBlockHelper)this.parent).getSubNames(map);
		TreeSet<Integer> registered  = new TreeSet<Integer>();
		
		if (map.isEmpty()) {
			this.registerRender(0);
		} else {
			for (Entry<Integer, String> entry :map.entrySet()) {
				if (!registered.contains(entry.getKey())) {
					ModelBakery.addVariantName(this.getBlockItem(), this.mod.getModId()+":"+entry.getValue());
				}
			}
			for (Entry<Integer, String> entry :map.entrySet()) {
				if (!registered.contains(entry.getKey())) {
					this.registerRender(entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	public void registerRender (int metadata) {
		this.registerRender(metadata, this.getRegisterName());
	}
	
	public void registerRender (int metadata, String renderKey) {
		this.registerRender(metadata, renderKey, true);
	}
	
	public void registerRender (int metadata, String renderKey, boolean trace) {
		if (trace) ModGollumCoreLib.log.message("Auto register render: "+metadata+":"+this.mod.getModId()+":"+renderKey);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this.getBlockItem(), metadata, new ModelResourceLocation(this.mod.getModId()+":"+renderKey, "inventory"));
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return registerName;
	}
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return Item.getItemFromBlock(this.parent);
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubNames(HashMap<Integer, String> list) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		this.getSubNames(map);
		for (Entry<Integer, String> entry: map.entrySet()) {
			list.add(new ItemStack(item, 1, entry.getKey()));
		}
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
					
					for (float f2 = random.nextFloat() * 0.8F + 0.1F; itemStack.stackSize > 0; world.spawnEntityInWorld(entityItem)) {
						int k1 = random.nextInt(21) + 10;
						
						if (k1 > itemStack.stackSize) {
							k1 = itemStack.stackSize;
						}
						
						itemStack.stackSize -= k1;
						entityItem = new EntityItem(world, (double) ((float) pos.getX() + f), (double) ((float) pos.getY() + f1), (double) ((float) pos.getZ() + f2), new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));
						float f3 = 0.05F;
						entityItem.motionX = (double) ((float) random.nextGaussian() * f3);
						entityItem.motionY = (double) ((float) random.nextGaussian() * f3 + 0.2F);
						entityItem.motionZ = (double) ((float) random.nextGaussian() * f3);
						
						if (itemStack.hasTagCompound()) {
							entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
						}
					}
				}
			}
		}
	}
	
}