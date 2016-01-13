package com.gollum.core.tools.helper;

import java.util.ArrayList;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		
		if (!vanillaRegister) ItemRegistry.instance().add((HItem) this.parent);
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
		this.parent.setUnlocalizedName(this.registerName);
		GameRegistry.registerItem (this.parent, this.getRegisterName (), this.mod.getModId());
	}

	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		this.parent.getSubItems(this.parent, (CreativeTabs)null, list);
		TreeSet<Integer> registered  = new TreeSet<Integer>();

		registered.add(0);
		this.registerRender(0);
		for (ItemStack is :list) {
			if (!registered.contains(is.getItemDamage())) {
				registered.add(is.getItemDamage());
				this.registerRender(is.getItemDamage());
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
		if (trace) ModGollumCoreLib.log.message("Auto register render: "+this.mod.getModId()+":"+renderKey+':'+metadata);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this.parent, metadata, new ModelResourceLocation(this.mod.getModId()+":"+renderKey, "inventory"));
	}
}