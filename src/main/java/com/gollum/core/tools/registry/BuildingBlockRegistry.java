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

	private static BuildingBlockRegistry instance = null;
	
	private ArrayList<BuildingBlockHandler>   handlers    = new ArrayList<BuildingBlockHandler>();
	private ArrayList<Class<? extends Block>> aftersBlock = new ArrayList<Class<? extends Block>>();
	
	public synchronized static BuildingBlockRegistry instance () {
		if (instance == null) {
			instance = new BuildingBlockRegistry();
		}
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
		this.handlers.add(new BlockSignBuildingHandler());
		this.handlers.add(new BlockDirectionalBuildingHandler());
		this.handlers.add(new BlockDirectionalWithNoneBuildingHandler());
		this.handlers.add(new BlockDirectionalWithBit1BuildingHandler());
		this.handlers.add(new BlockTrapDoorBuildingHandler());
		this.handlers.add(new BlockLeverBuildingHandler());
		this.handlers.add(new BlockDoorBuildingHandler());
		this.handlers.add(new BlockStairsBuildingHandler());
		this.handlers.add(new BlockCommandBlockBuildingHandler());
		this.handlers.add(new BlockProximitySpawnBuildingHandler());
		this.handlers.add(new BlockMobSpawnerBuildingHandler());
		
		this.aftersBlock.add(BlockDoor.class);
		this.aftersBlock.add(BlockBed.class);
		this.aftersBlock.add(BlockChest.class);
		this.aftersBlock.add(BlockTorch.class);
		this.aftersBlock.add(BlockLever.class);
		this.aftersBlock.add(BlockSign.class);
	}
	
	public ArrayList<BuildingBlockHandler> getHandlers () {
		return this.handlers;
	}
	
	public boolean isAfterBlock (Block block) {
		
		for (Class clazz : this.aftersBlock) {
			if (block != null && clazz.isAssignableFrom(block.getClass())) {
				return true;
			}
		}
		
		return false;
	}
	
}
