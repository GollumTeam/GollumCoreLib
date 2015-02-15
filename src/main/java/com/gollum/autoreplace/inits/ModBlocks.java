package com.gollum.autoreplace.inits;

import static com.gollum.autoreplace.ModGollumAutoReplace.config;
import static com.gollum.autoreplace.ModGollumAutoReplace.log;

import java.util.TreeMap;

import com.gollum.autoreplace.ModGollumAutoReplace;
import com.gollum.autoreplace.common.blocks.BlockAutoReplace;
import com.gollum.autoreplace.common.blocks.BlockAutoReplace.ReplaceBlock;
import com.gollum.autoreplace.common.config.type.ReplaceBlockType;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ModBlocks {

	/////////////////////
	// Liste des blocs //
	/////////////////////
	
	public static TreeMap<String, BlockAutoReplace> blockAutoReplaceList = new TreeMap<String, BlockAutoReplace>();
	
	/**
	 * Initialisation des blocks
	 */
	public static void init() {
		
		for (ReplaceBlockType blockReplaceType : config.blockReplaceList) {
			
			String modId     = blockReplaceType.getModId();
			String blockName = blockReplaceType.getBlockName();
			
			if (modId.equals("")) {
				log.severe("The mold block mod ID must be not NULL.");
				continue;
			}
			if (blockName.equals("")) {
				log.severe("The mold block name must be not NULL.");
				continue;
			}
			if (blockAutoReplaceList.containsKey(modId+":"+blockName)) {
				log.severe("The old block "+modId+":"+blockName+" already registered.");
				continue;
			}
			
			BlockAutoReplace b = new BlockAutoReplace(modId, blockName, blockReplaceType.getReplaceBlocks());
			
			ModBlocks.blockAutoReplaceList.put(modId+":"+blockName, b);
			log.info("Register old block "+modId+":"+blockName+" success.");
		}
		
	}
}
