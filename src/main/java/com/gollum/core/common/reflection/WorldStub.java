package com.gollum.core.common.reflection;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.gollum.core.utils.reflection.DeobfuscateName;

public class WorldStub extends World {
	
	private WorldStub() {
		super(null, null, new WorldSettings(null),  null, null, null);
	}
	
	@Override
	@DeobfuscateName (value="scheduleBlockUpdateWithPriority")
	public void scheduleBlockUpdateWithPriority(int par1, int par2, int par3, int par4, int par5, int par6) {
	}

	// 1.7.10 : func_147446_b
	// 1.7.2  : func_147446_b
	// 1.6.4  : scheduleBlockUpdateFromLoad
	@Override
	@DeobfuscateName (value="func_147446_b")
	public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6) {
	}
	
	@Override
	@DeobfuscateName (value="tick")
	public void tick(){
	}
	
	@Override
	@DeobfuscateName (value="tickUpdates")
	public boolean tickUpdates(boolean p_72955_1_) {
		return false;
	}
	
	@Override
	@DeobfuscateName (value="getPendingBlockUpdates")
	public List getPendingBlockUpdates(Chunk p_72920_1_, boolean p_72920_2_) {
		return null;
	}

	@Override
	@DeobfuscateName (value="initialize")
	protected void initialize(WorldSettings p_72963_1_){
	}
	
	@Override
	@DeobfuscateName (value="createChunkProvider")
	protected IChunkProvider createChunkProvider() {
		return null;
	}

//	// 1.7.10 : func_152379_p ?
//	// 1.7.2  : NOT EXIST
//	// 1.6.4  : NOT EXIST
//	@Override
//	@DeobfuscateName ("func_152379_p")
//	protected int func_152379_p() {
//		return 0;
//	}
	
	@Override
	@DeobfuscateName ("getEntityByID")
	public Entity getEntityByID(int p_73045_1_) {
		return null;
	}
	
	@Override
	@DeobfuscateName ("addBlockEvent")
	public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6) {
	}
	
	@Override
	@DeobfuscateName ("updateEntities")
	public void updateEntities() {
	}
}
