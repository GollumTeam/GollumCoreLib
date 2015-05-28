package com.gollum.core.tools.registry;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.item.Item;

import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.utils.reflection.Reflection;


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
			
			for (Field f : Item.class.getFields()) {
				f.setAccessible(true);
				if (Modifier.isStatic(f.getModifiers()) && f.get(null) == vanillaItem) {
					field = f;
				}
			}
			if (field == null) {
				throw new Exception("This item \""+vanillaItem.getClass().getName()+"\" not found in fields "+Item.class.getName());
			}
			
			this.overrideItemsClassField(field.getName(), item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideItemsClassField (String fieldName, Item item) {
		try {
			Reflection.setFinalStatic(Item.class.getDeclaredField(fieldName), item);
			log.message("Override Items field fieldName=\""+fieldName+"\" by "+item.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideItemId(Item vanillaItem, Item item) {
		
		int newId = vanillaItem.itemID;
		int oldId = item.itemID;
		
		try {
			
			Field f = null;
			try {
				f = Item.class.getDeclaredField("=======");
			} catch (Exception e) {
				log.message("Unofuscate property RegistrySimple : ======= => blockId");
				f = Item.class.getDeclaredField("itemID");
			}
			Reflection.setFinalField(f, item, newId);
			log.message("Override Item field fieldName=\"itemID by "+newId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Item.itemsList[newId] = item;
		Item.itemsList[oldId] = null;
		
	}
	
//	
//	
//	public void overrideRegistered (String registerName, Item item) {
//		
//		try {
//			
//			Item vanillaItem = RegisteredObjects.instance().getItem(registerName);
//			
//			if (vanillaItem != null) {
//				
//				log.message("Override registery item \""+registerName+"\" by "+item.getClass().getSimpleName());
//				
//				FMLControlledNamespacedRegistry<Item> blockRegistry = GameData.getItemRegistry();
//				Field f1 = RegistrySimple.class.getDeclaredField("registryObjects");
//				f1.setAccessible(true);
//				Map registryObjects = (Map)f1.get(blockRegistry);
//				registryObjects.put(registerName,item);
//				log.debug (" 1 - Replace \""+registerName+"\" registery : registryObjects");
//				
//
//				Field f2 = RegistryNamespaced.class.getDeclaredField("underlyingIntegerMap");
//				f2.setAccessible(true);
//				ObjectIntIdentityMap underlyingIntegerMap = (ObjectIntIdentityMap)f2.get(blockRegistry);
//				
//				int id = underlyingIntegerMap.func_148747_b(vanillaItem);
//				if (id == -1) {
//					throw new Exception(" 2 - Replace \""+registerName+"\" registery is KO because id not found");
//				}
//				underlyingIntegerMap.func_148746_a(item, id);
//				log.debug (" 2 - Replace \""+registerName+"\" registery : underlyingIntegerMap id="+id);
//				
//			} else {
//				log.severe("The original item \""+registerName+"\" not found for replace registery.");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}
	
}
