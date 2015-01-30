package mods.gollum.proxyblock.inits;

import mods.gollum.proxyblock.common.blocks.BlockProxy;

public class ModBlocks {

	/////////////////////
	// Liste des blocs //
	/////////////////////
	
	public static BlockProxy blockProxy1;
	
	/**
	 * Initialisation des blocks
	 */
	public static void init() {
		ModBlocks.blockProxy1 = new BlockProxy("blockProxy1");
	}
}
