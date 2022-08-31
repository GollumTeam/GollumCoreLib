package com.gollum.core.tools.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHelper implements IItemHelper {
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helper
	// Une sorte de config
	// Par defaut le helper vas enregistrer l'item, charger des texture perso ...
	public boolean vanillaRegister      = false;
	public boolean vanillaTexture       = false;
	
	protected GollumMod mod;
	protected Item parent;
	protected String registerName;

	public ItemHelper (Item item, String registerName) {
		this.parent       = item;
		this.registerName = registerName;
		this.mod          = ModContext.instance().getCurrent();
		
		if (!vanillaRegister) ItemRegistry.instance().add((IItemHelper) this.parent);
	}

	@Override
	public ItemHelper getGollumHelper() {
		return this;
	}
	
	@Override
	public String getRegisterName() {
		return this.registerName;
	}
	
	@Override
	public void register () {
		if(vanillaRegister) return;
		this.parent.setUnlocalizedName(this.registerName);
		this.parent.setRegistryName(this.getRegisterName());
		ForgeRegistries.ITEMS.register(this.parent);
	}

	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IItemHelper)this.parent).getSubNames(map);
		TreeSet<Integer> registered  = new TreeSet<Integer>();
		
		if (map.isEmpty()) {
			this.registerRender(0);
		} else {
			for (Entry<Integer, String> entry :map.entrySet()) {
				if (!registered.contains(entry.getKey())) {
					ModelResourceLocation model = this.getModelResourceLocation(entry.getValue());
					ModelBakery.registerItemVariants(this.parent, model);
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
		this.registerRender(metadata, this.getRegisterName(), true);
	}
	
	public void registerRender (int metadata, String renderKey, boolean trace) {
		if (trace) ModGollumCoreLib.log.message("Auto register render: "+RegisteredObjects.instance().getRegisterName(this.parent)+":"+metadata+":"+renderKey);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this.parent, metadata, this.getModelResourceLocation(renderKey));
	}
	
	protected ModelResourceLocation getModelResourceLocation (String renderKey) {
		return new ModelResourceLocation(this.mod.getModId()+":"+renderKey, "inventory");
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IItemHelper)this.parent).getSubNames(map);
		if (map.size() <= 1) {
			return this.parent.getUnlocalizedName();
		}
		
		int metadata = ((IItemHelper)this.parent).getEnabledMetadata(stack.getItemDamage());
		return this.parent.getUnlocalizedName() + "." + metadata;
	}
	

	@Override
	public int getEnabledMetadata (int dammage) {
		int lastSubblock = -1;
		TreeMap<Integer, String> map = new TreeMap<Integer, String>();
		((IItemHelper)this.parent).getSubNames(map);
		
		for (Entry<Integer, String> entry: map.entrySet()) {
			if (entry.getKey()  > dammage) {
				break;
			}
			lastSubblock = entry.getKey();			
		}
		return (lastSubblock == -1) ? dammage : lastSubblock;
	}

	public void getSubNames(Map<Integer, String> list) {
		((IItemHelper)this.parent).getSubNames(list);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		((IItemHelper)this.parent).getSubNames(map);
		
		if (map.isEmpty()) {
			items.add(new ItemStack(this.parent, 1, 0));
			return;
		}
		
		for (Entry<Integer, String> entry: map.entrySet()) {
			items.add(new ItemStack(this.parent, 1, entry.getKey()));
		}
	}
}