package com.gollum.core.common.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.world.World;

public class WorldAccesssSheduler {
	
	private static WorldAccesssSheduler instance = new WorldAccesssSheduler();
	
	public static WorldAccesssSheduler instance() { return instance; };
	
	private final ConcurrentHashMap<World, ReentrantLock> mustBeLock = new ConcurrentHashMap<World, ReentrantLock>();
	
	public void observe (World world) {
		synchronized (this.mustBeLock) {
			this.mustBeLock.put(world, new ReentrantLock());
		}
	}
	
	public void forget (World world) {
		ReentrantLock lock = null;
		synchronized (this.mustBeLock) {
			if (this.mustBeLock.containsKey(world)) {
				lock = this.mustBeLock.get(world);
				this.mustBeLock.remove(world);
				
			}
		}
		if (lock != null) {
			lock.unlock();
		}
	}
	
	public void lockWorld (World world) {
		
		ReentrantLock lock = null;
		synchronized (this.mustBeLock) {
			if (this.mustBeLock.containsKey(world)) {
				lock = this.mustBeLock.get(world);
			}
		}
		if (lock != null) {
			lock.lock();
		}
	}
	
	public void unlockWorld (World world) {
		ReentrantLock lock = null;
		synchronized (this.mustBeLock) {
			if (this.mustBeLock.containsKey(world)) {
				lock = this.mustBeLock.get(world);
			}
		}
		if (lock != null) {
			lock.unlock();
		}
	}
	
}
