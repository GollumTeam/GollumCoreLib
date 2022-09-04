package com.gollum.core.tools.registry;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.util.ObjectIntIdentityMap;
//import net.minecraft.util.RegistryNamespaced;
//import net.minecraft.util.RegistrySimple;
//import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
//import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;


public class ItemRegistry {

	private static ItemRegistry instance = new ItemRegistry();

	private HashMap<GollumMod, ArrayList<IItemHelper>> items = new HashMap<GollumMod, ArrayList<IItemHelper>>();
	
	public static ItemRegistry instance () {
		return instance;
	}
	
	public void add (IItemHelper item) {
		GollumMod mod = ModContext.instance().getCurrent();
		if (!this.items.containsKey(mod)) {
			this.items.put(mod, new ArrayList<IItemHelper>());
		}
		ArrayList<IItemHelper> items = this.items.get(mod);
		if (!items.contains(item)) {
			items.add(item);
		}
	}
	
	public void registerAll () {
		GollumMod mod = ModContext.instance().getCurrent();
		if (this.items.containsKey(mod)) {
			ArrayList<IItemHelper> items = this.items.get(mod);
			for (IItemHelper item : items) {
				item.register();
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerRenders() {
		GollumMod mod = ModContext.instance().getCurrent();
		if (this.items.containsKey(mod)) {
			ArrayList<IItemHelper> items = this.items.get(mod);
			for (IItemHelper item : items) {
				item.registerRender();
			}
		}
	}
	
	public void overrideItemsClassField (Item vanillaItem, Item item) {
		
		try {
			
			Field field = null;
			
			for (Field f : Items.class.getFields()) {
				f.setAccessible(true);
				if (Modifier.isStatic(f.getModifiers()) && f.get(null) == vanillaItem) {
					field = f;
				}
			}
			if (field == null) {
				throw new Exception("This item \""+vanillaItem.getClass().getName()+"\" not found in fields "+Items.class.getName());
			}
			
			this.overrideItemsClassField(field.getName(), item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideItemsClassField (String fieldName, Item item) {
		try {
			Reflection.setFinalStatic(Items.class.getDeclaredField(fieldName), item);
			logger.message("Override Items field fieldName=\""+fieldName+"\" by "+item.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void overrideRegistered (String registerName, Item item) {
		
//		try {
//			
//			Item vanillaItem = RegisteredObjects.instance().getItem(registerName);
//			
//			if (vanillaItem != null) {
//				
//				log.message("Override registery item \""+registerName+"\" by "+item.getClass().getSimpleName());
//				
//				FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();
//				Field f1 = null;
//				try {
//					f1 = RegistrySimple.class.getDeclaredField("field_82596_a");
//				} catch (Exception e) {
//					log.message("Unofuscate property RegistrySimple : field_82596_a => registryObjects");
//					f1 = RegistrySimple.class.getDeclaredField("registryObjects");
//				}
//				f1.setAccessible(true);
//				Map registryObjects = (Map)f1.get(itemRegistry);
//				registryObjects.put(registerName,item);
//				log.debug (" 1 - Replace \""+registerName+"\" registery : registryObjects");
//				
//				Field f2 = null;
//				try {
//					f2 = RegistryNamespaced.class.getDeclaredField("field_148759_a");
//				} catch (Exception e) {
//					log.message("Unofuscate property RegistrySimple : field_148759_a => underlyingIntegerMap");
//					f2 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
//				}
//				f2.setAccessible(true);
//				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f2.get(itemRegistry);
//				
//				int id = underlyingIntegerMap.get(vanillaItem);
//				if (id == -1) {
//					throw new Exception(" 2 - Replace \""+registerName+"\" registery is KO because id not found");
//				}
//				underlyingIntegerMap.put(item, id);
//				log.debug (" 2 - Replace \""+registerName+"\" registery : underlyingIntegerMap id="+id);
//				
//				overrideStatItem(StatList.objectMineStats , item, vanillaItem);
//				overrideStatItem(StatList.itemStats       , item, vanillaItem);
//				overrideStatItem(StatList.objectBreakStats, item, vanillaItem);
//				log.debug (" 3 - Replace \""+registerName+"\" stats");
//				
//				overrideCraftings(item, vanillaItem);
//				log.debug (" 4 - Replace \""+registerName+"\" crafting");
//				
//			} else {
//				log.severe("The original item \""+registerName+"\" not found for replace registery.");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}

	private void overrideCraftings(Item item, Item vanillaItem) {
//		Iterator it = CraftingManager.getInstance().getRecipeList().iterator();
//		while (it.hasNext()) {
//			Object o = it.next();
//			if (o instanceof ShapedRecipes) {
//				ShapedRecipes recipes = (ShapedRecipes)o;
//				if (recipes.getRecipeOutput().getItem() == vanillaItem) {
//					log.debug (" 4 - Block found in ShapedRecipes out");
//					recipes.getRecipeOutput().setItem(item);
//				}
//				for (int i = 0; i < recipes.recipeItems.length; i++) {
//					ItemStack is = recipes.recipeItems[i];
//					if (is != null && is.getItem() == vanillaItem) {
//						log.debug (" 4 - Block found in ShapedRecipes");
//						is.setItem(item);
//					}
//				}
//			} else
//			if (o instanceof ShapelessRecipes) {
//				ShapelessRecipes recipes = (ShapelessRecipes)o;
//				if (recipes.getRecipeOutput().getItem() == vanillaItem) {
//					recipes.getRecipeOutput().setItem(item);
//					log.debug (" 4 - Block found in ShapelessRecipes out");
//				}
//				Iterator subIt = recipes.recipeItems.iterator();
//				while (subIt.hasNext()) {
//					Object subO = subIt.next();
//					if (subO instanceof ItemStack) {
//						ItemStack is = (ItemStack)subO;
//						if (is != null && is.getItem() == vanillaItem) {
//							log.debug (" 4 - Block found in ShapelessRecipes");
//							is.setItem(item);
//						}
//					}
//				}
//			} else if (o instanceof ShapedOreRecipe) {
//				ShapedOreRecipe recipes = (ShapedOreRecipe)o;
//				if (recipes.getRecipeOutput().getItem() == vanillaItem) {
//					recipes.getRecipeOutput().setItem(item);
//					log.debug (" 4 - Block found in ShapedOreRecipe out");
//				}
//				 Object[] subList = recipes.getInput();
//				for (int i = 0; i < subList.length; i++) {
//					if (subList[i] instanceof ItemStack) {
//						ItemStack is = (ItemStack) subList[i];
//						if (is != null && is.getItem() == vanillaItem) {
//							log.debug (" 4 - Block found in ShapedOreRecipe");
//							is.setItem(item);
//						}
//					}
//				}
//			} else if (o instanceof ShapelessOreRecipe) {
//				ShapelessOreRecipe recipes = (ShapelessOreRecipe)o;
//				if (recipes.getRecipeOutput().getItem() == vanillaItem) {
//					recipes.getRecipeOutput().setItem(item);
//					log.debug (" 4 - Block found in ShapelessOreRecipe out");
//				}
//				Iterator subIt = recipes.getInput().iterator();
//				while (subIt.hasNext()) {
//					Object subO = subIt.next();
//					if (subO instanceof ItemStack) {
//						ItemStack is = (ItemStack)subO;
//						if (is != null && is.getItem() == vanillaItem) {
//							log.debug (" 4 - Block found in ShapelessOreRecipe");
//							is.setItem(item);
//						}
//					}
//				}
//			}
//		}
	}
	
	private void overrideStatItem(StatBase[] itemStats, Item item, Item vanillaItem) throws Exception {
		for(int i = 0; i < itemStats.length; i++) {
			if (itemStats[i] instanceof StatCrafting) {
				StatCrafting stat = (StatCrafting) itemStats[i];
				Field f = StatCrafting.class.getDeclaredField("field_150960_a");
				f.setAccessible(true);
				Item subItem = (Item)f.get(stat);
				if (subItem == vanillaItem) {
					
					// 1.6.4  ?
					// 1.7.2  field_150960_a
					// 1.7.10 field_150960_a
					f.set(stat, item);
				}
			}
		}
		
	}

	private void overrideStatItem(List itemStats, Item item, Item vanillaItem) throws Exception {
		Iterator it = itemStats.iterator();
		
		while(it.hasNext()) {
			StatCrafting stat = (StatCrafting) it.next();
			Field f = StatCrafting.class.getDeclaredField("field_150960_a");
			f.setAccessible(true);
			Item subItem = (Item)f.get(stat);
			if (subItem == vanillaItem) {
				
				// 1.6.4  ?
				// 1.7.2  field_150960_a
				// 1.7.10 field_150960_a
				f.set(stat, item);
			}
		}
	}
	
}
