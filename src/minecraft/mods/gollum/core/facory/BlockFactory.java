package mods.gollum.core.facory;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockFactory {

	public Block create(Block block, String name) {
		
		block.setUnlocalizedName(name);
		GameRegistry.registerBlock(block, name);
		
		return block;
	}

}
