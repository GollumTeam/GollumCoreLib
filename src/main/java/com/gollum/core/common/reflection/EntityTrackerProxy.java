package com.gollum.core.common.reflection;

import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class EntityTrackerProxy extends EntityTracker {
	
	private EntityTracker entityTracker;
	private Object lock = new Object();
	
	public EntityTrackerProxy(WorldServer world, EntityTracker entityTracker) {
		super(world);
		
		this.entityTracker = entityTracker;
	}
	
	@Override
	public void addEntityToTracker(Entity p_72786_1_) {
		synchronized (lock) {
			entityTracker.addEntityToTracker(p_72786_1_);
		}
	}

	@Override
	public void addEntityToTracker(Entity p_72791_1_, int p_72791_2_, int p_72791_3_) {
		synchronized (lock) {
			entityTracker.addEntityToTracker(p_72791_1_, p_72791_2_, p_72791_3_);
		}
	}

	@Override
	public void addEntityToTracker(Entity p_72785_1_, int p_72785_2_, final int p_72785_3_, boolean p_72785_4_) {
		synchronized (lock) {
			entityTracker.addEntityToTracker(p_72785_1_, p_72785_2_, p_72785_3_, p_72785_4_);
		}
	}
	
	@Override
	public void removeEntityFromAllTrackingPlayers(Entity p_72790_1_) {
		synchronized (lock) {
			entityTracker.removeEntityFromAllTrackingPlayers(p_72790_1_);
		}
	}
	
	@Override
	public void updateTrackedEntities() {
		synchronized (lock) {
			entityTracker.updateTrackedEntities ();
		}
	}
	
	@Override
	public void func_151247_a(Entity p_151247_1_, Packet p_151247_2_) {
		synchronized (lock) {
			entityTracker.func_151247_a(p_151247_1_, p_151247_2_);
		}
	}

	@Override
	public void func_151248_b(Entity p_151248_1_, Packet p_151248_2_) {
		synchronized (lock) {
			entityTracker.func_151248_b(p_151248_1_, p_151248_2_);
		}
	}
	
	@Override
	public void removePlayerFromTrackers(EntityPlayerMP p_72787_1_) {
		synchronized (lock) {
			entityTracker.removePlayerFromTrackers(p_72787_1_);
		}
	}
	
	@Override
	public void func_85172_a(EntityPlayerMP p_85172_1_, Chunk p_85172_2_) {
		synchronized (lock) {
			entityTracker.func_85172_a(p_85172_1_, p_85172_2_);
		}
	}
	
	@Override
	public Set<net.minecraft.entity.player.EntityPlayer> getTrackingPlayers(Entity entity) {
		synchronized (lock) {
			return entityTracker.getTrackingPlayers(entity);
		}
	}

}
