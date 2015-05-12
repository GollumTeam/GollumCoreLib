package com.gollum.core.common.concurrent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import com.gollum.core.ModGollumCoreLib;

import net.minecraft.world.World;

public class WorldAccesssSheduler {
	
	private static WorldAccesssSheduler instance = new WorldAccesssSheduler();
	
	public static WorldAccesssSheduler instance() { return instance; };

	private final ConcurrentHashMap<World, ReentrantLock> mustBeLock = new ConcurrentHashMap<World, ReentrantLock>();
	private final ConcurrentHashMap<World, ReentrantLock> allMutex   = new ConcurrentHashMap<World, ReentrantLock>();
	
	int iiii = 0;
	
	public void observe (World world) {
		synchronized (this.mustBeLock) {
			ReentrantLock m = new ReentrantLock();
			this.mustBeLock.put(world, m);
			this.allMutex.put(world, m);
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
			try {
				lock.unlock();
			} catch (Exception e) {
			}
		}
	}
	
	public void clean () {
		synchronized (this.mustBeLock) {
			this.mustBeLock.clear();
			this.allMutex.clear();
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
			
			if (this.allMutex.containsKey(world)) {
				lock = this.allMutex.get(world);
			}
		}
		if (lock != null) {
			try {
				lock.unlock();
			} catch (Exception e) {
			}
		}
	}
	
}
