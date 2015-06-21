package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.EnumSet;
import java.util.Iterator;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.events.BuildingGenerateEvent;
import com.gollum.core.utils.math.Integer3d;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;


public class WorldTickHandler implements ITickHandler {
	
	private long time = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
		if (tickData.length == 0 || !(tickData[0] instanceof World)) {
			return;
		}
		World world = (World) tickData[0];
		
		if (!world.isRemote) {
			
			if (System.currentTimeMillis() - this.time > 200) {
				this.time = System.currentTimeMillis();
					
				Iterator<BuilderRunnable> i = Builder.currentBuilds.iterator();
				while (i.hasNext()) {
					BuilderRunnable thread = i.next(); 
					if (!thread.isAlive()) {
						i.remove();
						
						BuildingGenerateEvent newEvent = new BuildingGenerateEvent.Post(world, thread.getBuilding(), thread.getRotate(), thread.getPosition());
						MinecraftForge.EVENT_BUS.post(newEvent);
						
						log.debug ("Thread "+thread.getId()+" is finish remove of pile.");
						continue;
					}
					
					try {
	//					log.debug ("WorldServer notify Builder");
						synchronized  (thread.waiter) {
							thread.waiter.notify();
						}
						Thread.sleep(10);
	//					log.debug ("WorldServer lock for builder");
						thread.lockWorld.lock();
	//					log.debug ("WorldServer is unlock");
						thread.unlockWorld();
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					break;
				}
			}
		}
		
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return ModGollumCoreLib.MODID + " - Building update tick";
	}
}
