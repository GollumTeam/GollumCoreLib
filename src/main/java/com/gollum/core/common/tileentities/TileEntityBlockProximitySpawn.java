package com.gollum.core.common.tileentities;

import static com.gollum.core.ModGollumCoreLib.log;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBlockProximitySpawn extends TileEntity implements IUpdatePlayerListBox {
	
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
		
		if (this.mobID != null && !this.worldObj.isRemote) {
			
			if (this.delay > 0) {
				this.delay--;
				return;
			}
			
			Entity entity = EntityList.createEntityByName(this.mobID, this.worldObj);
			
			// L'entity n'existe pas
			if (entity == null) {
				log.warning("This mob "+this.mobID+" isn't  register");
				this.worldObj.setBlockToAir(this.pos);
				return;
			}
			
			this.worldObj.setBlockToAir(this.pos);
			
			double x = (double)this.pos.getX() + 0.5D;
			double y = (double)this.pos.getY();// + this.worldObj.rand.nextInt(3) - 1);
			double z = (double)this.pos.getZ() + 0.5D;
			EntityLiving entityLiving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
			entity.setLocationAndAngles(x, y, z, this.worldObj.rand.nextFloat() * 360.0F, this.worldObj.rand.nextFloat() * 360.0F);
			this.worldObj.spawnEntityInWorld(entity);
			
			if (entityLiving == null || entityLiving.getCanSpawnHere()) {
				
				this.worldObj.playSoundEffect (this.pos.getX()+0.5f, this.pos.getY()+0.5f, this.pos.getZ()+0.5f, "dig.stone", 0.5F, this.worldObj.rand.nextFloat() * 0.25F + 0.6F);
				
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
	public void writeToNBT(NBTTagCompound var1) {
		super.writeToNBT(var1);
		String mobID = (this.mobID != null) ?this.mobID : "";
		var1.setString("mobID", mobID);
		var1.setShort("delay", delay);
	}
}
