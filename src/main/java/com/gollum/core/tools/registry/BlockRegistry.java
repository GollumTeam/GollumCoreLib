package com.gollum.core.tools.registry;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.block.Block;

import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.utils.reflection.Reflection;


public class BlockRegistry {

	private static BlockRegistry instance = new BlockRegistry();
	
	private ArrayList<IBlockHelper> blocks = new ArrayList<IBlockHelper>();
	
	public static BlockRegistry instance () {
		return instance;
	}
	
	public void add (IBlockHelper block) {
		if (!this.blocks.contains(block)) {
			this.blocks.add(block);
		}
	}
	
	public void registerAll () {
		for (IBlockHelper block : this.blocks) {
			block.register();
		}
		this.blocks.clear();
	}
	
	public void overrideBlocksClassField (Block vanillaBlock, Block block) {
		
		try {
			
			Field field = null;
			
			for (Field f : Block.class.getFields()) {
				f.setAccessible(true);
				if (Modifier.isStatic(f.getModifiers()) && f.get(null) == vanillaBlock) {
					field = f;
				}
			}
			if (field == null) {
				throw new Exception("This block \""+vanillaBlock.getClass().getName()+"\" not found in fields "+Block.class.getName());
			}
			
			this.overrideBlocksClassField(field.getName(), block);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideBlocksClassField (String fieldName, Block block) {
		try {
			Reflection.setFinalStatic(Block.class.getDeclaredField(fieldName), block);
			log.message("Override Blocks field fieldName=\""+fieldName+"\" by "+block.getClass().getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void overrideBlocksId (Block vanillaBlock, Block block) {
		
		int newId = vanillaBlock.blockID;
		int oldId = block.blockID;
		
		try {
			
			Field f = null;
			try {
				f = Block.class.getDeclaredField("=======");
			} catch (Exception e) {
				log.message("Unofuscate property RegistrySimple : ======= => blockId");
				f = Block.class.getDeclaredField("blockID");
			}
			Reflection.setFinalField(f, block, newId);
			log.message("Override block field fieldName=\"blockID by "+newId);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Block.blocksList[newId] = block;
		Block.blocksList[oldId] = null;
		
	}
	
}
