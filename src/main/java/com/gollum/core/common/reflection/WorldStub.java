package com.gollum.core.common.reflection;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import com.gollum.core.utils.reflection.DeobfuscateName;

public class WorldStub extends World {
	
	private WorldStub() {
		super(null, null, new WorldSettings(null),  null, null);
	}

	@DeobfuscateName (value="getChunkFromChunkCoords")
	public Chunk getChunkFromChunkCoords(int p_72964_1_, int p_72964_2_) {
		return null;
	}
	
	@Override
	@DeobfuscateName (value="scheduleBlockUpdateWithPriority")
	public void scheduleBlockUpdateWithPriority(int p_147454_1_, int p_147454_2_, int p_147454_3_, Block p_147454_4_, int p_147454_5_, int p_147454_6_) {
	}

	// 1.7.10 : func_147446_b
	// 1.7.2  : func_147446_b
	// 1.6.4  : scheduleBlockUpdateFromLoad
	@Override
	@DeobfuscateName (value="func_147446_b")
	public void func_147446_b(int p_147446_1_, int p_147446_2_, int p_147446_3_, Block p_147446_4_, int p_147446_5_, int p_147446_6_) {
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
	@Override
	@DeobfuscateName ("func_152379_p")
	protected int func_152379_p() {
		return 0;
	}
	
	@Override
	@DeobfuscateName ("getEntityByID")
	public Entity getEntityByID(int p_73045_1_) {
		return null;
	}
	
	@Override
	@DeobfuscateName ("addBlockEvent")
	public void addBlockEvent(int p_147452_1_, int p_147452_2_, int p_147452_3_, Block p_147452_4_, int p_147452_5_, int p_147452_6_) {
	}
	
	@Override
	@DeobfuscateName ("updateEntities")
	public void updateEntities() {
	}
}
