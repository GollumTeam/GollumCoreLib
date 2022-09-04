package com.gollum.core.tools.registry;

import java.util.ArrayList;

import com.gollum.core.common.building.handler.BlockCommandBlockBuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalBuildingHandler;
import com.gollum.core.common.building.handler.BlockLeverBuildingHandler;
import com.gollum.core.common.building.handler.BlockMobSpawnerBuildingHandler;
import com.gollum.core.common.building.handler.BlockProximitySpawnBuildingHandler;
import com.gollum.core.common.building.handler.BlockStandingSignBuildingHandler;
import com.gollum.core.common.building.handler.BlockWallSignBuildingHandler;
import com.gollum.core.common.building.handler.BuildingBlockHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockTripWireHook;
import net.minecraftforge.fluids.BlockFluidBase;

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
		this.handlers.add(new BlockWallSignBuildingHandler());
		this.handlers.add(new BlockStandingSignBuildingHandler());
		this.handlers.add(new BlockDirectionalBuildingHandler());
		this.handlers.add(new BlockLeverBuildingHandler());
		this.handlers.add(new BlockCommandBlockBuildingHandler());
		this.handlers.add(new BlockProximitySpawnBuildingHandler());
		this.handlers.add(new BlockMobSpawnerBuildingHandler());
		
		this.aftersBlock.add(BlockDoor.class);
		this.aftersBlock.add(BlockBed.class);
		this.aftersBlock.add(BlockChest.class);
		this.aftersBlock.add(BlockTorch.class);
		this.aftersBlock.add(BlockLever.class);
		this.aftersBlock.add(BlockSign.class);
		this.aftersBlock.add(BlockTripWire.class);
		this.aftersBlock.add(BlockTripWireHook.class);
		this.aftersBlock.add(BlockTrapDoor.class);
		this.aftersBlock.add(BlockBush.class);
		this.aftersBlock.add(BlockFluidBase.class);
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
