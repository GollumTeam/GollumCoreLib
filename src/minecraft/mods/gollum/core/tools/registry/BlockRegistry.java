package mods.gollum.core.tools.registry;

import java.util.ArrayList;

import mods.gollum.core.tools.helper.IBlockHelper;


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
			block.getGollumHelper ().register();
		}
		this.blocks.clear();
	}
	
}
