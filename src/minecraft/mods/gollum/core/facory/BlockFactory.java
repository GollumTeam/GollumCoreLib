package mods.gollum.core.facory;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import mods.morepistons.common.block.BlockMorePistonsGravitational;
import net.minecraft.block.Block;

public class BlockFactory {

	public Block create(Block block, String name) {
		
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		
		return block;
	}

	public Block create(Block block, String name, String trans) {
		
		create (block, name);
		LanguageRegistry.addName(block, trans);
		
		return block;
	}

}
