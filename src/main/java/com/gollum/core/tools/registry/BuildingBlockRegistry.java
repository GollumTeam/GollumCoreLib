package com.gollum.core.tools.registry;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;

import com.gollum.core.common.building.handler.BlockCommandBlockBuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalBuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalWithBit1BuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalWithNoneBuildingHandler;
import com.gollum.core.common.building.handler.BlockDoorBuildingHandler;
import com.gollum.core.common.building.handler.BlockLeverBuildingHandler;
import com.gollum.core.common.building.handler.BlockMobSpawnerBuildingHandler;
import com.gollum.core.common.building.handler.BlockProximitySpawnBuildingHandler;
import com.gollum.core.common.building.handler.BlockSignBuildingHandler;
import com.gollum.core.common.building.handler.BlockStairsBuildingHandler;
import com.gollum.core.common.building.handler.BlockTrapDoorBuildingHandler;
import com.gollum.core.common.building.handler.BuildingBlockHandler;

public class BuildingBlockRegistry {

	private static BuildingBlockRegistry instance = new BuildingBlockRegistry();
	
	private ArrayList<BuildingBlockHandler>   handlers    = new ArrayList<BuildingBlockHandler>();
	private ArrayList<Class<? extends Block>> aftersBlock = new ArrayList<Class<? extends Block>>();
	
	public static BuildingBlockRegistry instance () {
		return instance;
	}
	
	public static void register (BuildingBlockHandler handler) {
		if (!instance().handlers.contains(handler)) {
			instance().handlers.add(handler);
		}
	}
	
	public static void registerAfterBlock (Class<? extends Block> classBlock) {
		instance().aftersBlock.add(classBlock);
	}
	
	protected BuildingBlockRegistry () {
		BuildingBlockRegistry.register(new BlockSignBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalWithNoneBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalWithBit1BuildingHandler());
		BuildingBlockRegistry.register(new BlockTrapDoorBuildingHandler());
		BuildingBlockRegistry.register(new BlockLeverBuildingHandler());
		BuildingBlockRegistry.register(new BlockDoorBuildingHandler());
		BuildingBlockRegistry.register(new BlockStairsBuildingHandler());
		BuildingBlockRegistry.register(new BlockCommandBlockBuildingHandler());
		BuildingBlockRegistry.register(new BlockProximitySpawnBuildingHandler());
		BuildingBlockRegistry.register(new BlockMobSpawnerBuildingHandler());
		
		BuildingBlockRegistry.registerAfterBlock(BlockDoor.class);
		BuildingBlockRegistry.registerAfterBlock(BlockBed.class);
		BuildingBlockRegistry.registerAfterBlock(BlockChest.class);
		BuildingBlockRegistry.registerAfterBlock(BlockTorch.class);
		BuildingBlockRegistry.registerAfterBlock(BlockLever.class);
		BuildingBlockRegistry.registerAfterBlock(BlockSign.class);
	}
	
	public ArrayList<BuildingBlockHandler> getHandlers () {
		return this.handlers;
	}
	
	public boolean isAfterBlock (Block block) {
		
		for (Class clazz : this.aftersBlock) {
			if (block.getClass().isAssignableFrom(clazz)) {
				return true;
			}
		}
		
		return false;
	}
	
}
