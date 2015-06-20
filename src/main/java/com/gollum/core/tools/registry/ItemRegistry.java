package com.gollum.core.tools.registry;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistrySimple;

import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.utils.reflection.Reflection;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;


public class ItemRegistry {

	private static ItemRegistry instance = new ItemRegistry();
	
	private ArrayList<IItemHelper> items = new ArrayList<IItemHelper>();
	
	public static ItemRegistry instance () {
		return instance;
	}
	
	public void add (IItemHelper item) {
		if (!this.items.contains(item)) {
			this.items.add(item);
		}
	}
	
	public void registerAll () {
		for (IItemHelper item : this.items) {
			item.register();
		}
		this.items.clear();
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
			log.message("Override Items field fieldName=\""+fieldName+"\" by "+item.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void overrideRegistered (String registerName, Item item) {
		
		try {
			
			Item vanillaItem = RegisteredObjects.instance().getItem(registerName);
			
			if (vanillaItem != null) {
				
				log.message("Override registery item \""+registerName+"\" by "+item.getClass().getSimpleName());
				
				FMLControlledNamespacedRegistry<Item> itemRegistry = GameData.getItemRegistry();
				Field f1 = null;
				try {
					f1 = RegistrySimple.class.getDeclaredField("field_82596_a");
				} catch (Exception e) {
					log.message("Unofuscate property RegistrySimple : field_82596_a => registryObjects");
					f1 = RegistrySimple.class.getDeclaredField("registryObjects");
				}
				f1.setAccessible(true);
				Map registryObjects = (Map)f1.get(itemRegistry);
				registryObjects.put(registerName,item);
				log.debug (" 1 - Replace \""+registerName+"\" registery : registryObjects");
				
				Field f2 = null;
				try {
					f2 = RegistryNamespaced.class.getDeclaredField("field_148759_a");
				} catch (Exception e) {
					log.message("Unofuscate property RegistrySimple : field_148759_a => underlyingIntegerMap");
					f2 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
				}
				f2.setAccessible(true);
				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f2.get(itemRegistry);
				
				int id = underlyingIntegerMap.func_148747_b(vanillaItem);
				if (id == -1) {
					throw new Exception(" 2 - Replace \""+registerName+"\" registery is KO because id not found");
				}
				underlyingIntegerMap.func_148746_a(item, id);
				log.debug (" 2 - Replace \""+registerName+"\" registery : underlyingIntegerMap id="+id);
				
				overrideStatItem(StatList.objectMineStats , item, vanillaItem);
				overrideStatItem(StatList.itemStats       , item, vanillaItem);
				overrideStatItem(StatList.objectBreakStats, item, vanillaItem);
				log.debug (" 3 - Replace \""+registerName+"\" stats");
				
			} else {
				log.severe("The original item \""+registerName+"\" not found for replace registery.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void overrideStatItem(StatBase[] itemStats, Item item, Item vanillaItem) throws Exception {
		for(int i = 0; i < itemStats.length; i++) {
			if (itemStats[i] instanceof StatCrafting) {
				StatCrafting stat = (StatCrafting) itemStats[i];
				if (stat.func_150959_a() == vanillaItem) {
					
					// 1.6.4  ?
					// 1.7.2  field_150960_a
					// 1.7.10 field_150960_a
					Field f = StatCrafting.class.getDeclaredField("field_150960_a");
					f.setAccessible(true);
					f.set(stat, item);
				}
			}
		}
		
	}

	private void overrideStatItem(List itemStats, Item item, Item vanillaItem) throws Exception {
		Iterator it = itemStats.iterator();
		while(it.hasNext()) {
			StatCrafting stat = (StatCrafting) it.next();
			if (stat.func_150959_a() == vanillaItem) {
				
				// 1.6.4  ?
				// 1.7.2  field_150960_a
				// 1.7.10 field_150960_a
				Field f = StatCrafting.class.getDeclaredField("field_150960_a");
				f.setAccessible(true);
				f.set(stat, item);
			}
		}
	}
	
}
