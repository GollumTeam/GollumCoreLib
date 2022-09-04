package com.gollum.core.common.tileentities;

import static com.gollum.core.ModGollumCoreLib.logger;

import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;

public class TileEntityBlockProximitySpawn extends TileEntity implements ITickable {
	
	// Le mob
	private String mobID = "Pig";
	short delay = 30;
	
	
	public void setModId (String modId) {
		this.mobID = modId;
	}
	
	
	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void update() {
		
		if (this.mobID != null && !this.world.isRemote) {
			
			if (this.delay > 0) {
				this.delay--;
				return;
			}
			
			Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(mobID), this.world);
			
			// L'entity n'existe pas
			if (entity == null) {
				logger.warning("This mob "+this.mobID+" isn't  register");
				this.world.setBlockToAir(this.pos);
				return;
			}
			
			this.world.setBlockToAir(this.pos);
			
			double x = (double)this.pos.getX() + 0.5D;
			double y = (double)this.pos.getY();// + this.worldObj.rand.nextInt(3) - 1);
			double z = (double)this.pos.getZ() + 0.5D;
			EntityLiving entityLiving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
			entity.setLocationAndAngles(x, y, z, this.world.rand.nextFloat() * 360.0F, this.world.rand.nextFloat() * 360.0F);
			this.world.spawnEntity(entity);
			
			if (entityLiving == null || entityLiving.getCanSpawnHere()) {
				
				this.world.playSound(
					this.pos.getX()+0.5f,                                              // x           
					this.pos.getY()+0.5f,                                              // y           
					this.pos.getZ()+0.5f,                                              // z           
					RegisteredObjects.instance().getSoundEvent("dig.stone"),           // sound       
					RegisteredObjects.instance().getSoundCategoryBySound("dig.stone"), // category    
					0.5F,                                                              // volume             
					this.world.rand.nextFloat() * 0.25F + 0.6F,                        // pitch
					false                                                              // distance delay
				);
				
				if (entityLiving != null) {
					entityLiving.spawnExplosionParticle();
				}
			}
		}
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound var1) {
		super.readFromNBT(var1);
		String mobID = var1.getString("mobID");
		if (!mobID.equals("")) {
			this.mobID = mobID;
		}		
		this.delay = var1.getShort("delay");
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound var1) {
		super.writeToNBT(var1);
		String mobID = (this.mobID != null) ?this.mobID : "";
		var1.setString("mobID", mobID);
		var1.setShort("delay", delay);
		return var1;
	}
}
