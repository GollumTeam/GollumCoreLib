package mods.gollum.core.common.entities;

import static mods.gollum.core.ModGollumCoreLib.log;

import mods.gollum.core.common.building.Builder;
import mods.gollum.core.common.building.BuildingParser;
import mods.gollum.core.common.building.Builder.Progress;
import mods.gollum.core.common.building.Building.SubBuilding;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BuidingEntity extends Entity {
	
	private static Builder        builder = new Builder();
	private static BuildingParser parser  = new BuildingParser();
	
	private SubBuilding subBuilding = null;
	private Progress    progress    = new Progress();
	
	public BuidingEntity(World world) {
		super(world);
	}

	public BuidingEntity(World world, SubBuilding subBuilding) {
		super(world);
		this.subBuilding = subBuilding;
		this.posX = subBuilding.x;
		this.posY = subBuilding.y;
		this.posZ = subBuilding.z;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
		
		SubBuilding subBuilding = new SubBuilding();
		
		subBuilding.orientation = nbtTagCompound.getInteger("orientation");
		subBuilding.x           = nbtTagCompound.getInteger("x");
		subBuilding.y           = nbtTagCompound.getInteger("y");
		subBuilding.z           = nbtTagCompound.getInteger("z");
		
		String building = nbtTagCompound.getString("building");
		String modId    = nbtTagCompound.getString("modId");
		
		subBuilding.building = this.parser.parse(building, modId);
		
		if (subBuilding.building != null) {
			this.subBuilding = subBuilding;
		}
		
		this.progress.readEntityFromNBT (nbtTagCompound.getCompoundTag("progress"));
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {
		
		nbtTagCompound.setInteger ("orientation", this.subBuilding.orientation);
		nbtTagCompound.setInteger ("x"          , this.subBuilding.x);
		nbtTagCompound.setInteger ("y"          , this.subBuilding.y);
		nbtTagCompound.setInteger ("z"          , this.subBuilding.z);
		
		nbtTagCompound.setString ("building", this.subBuilding.building.name);
		nbtTagCompound.setString ("modId"   , this.subBuilding.building.modId);
		
		nbtTagCompound.setTag("progress", this.progress.writeEntityToNBT ());
		
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (!this.worldObj.isRemote && this.subBuilding != null) {
			builder.build(this.worldObj, this.subBuilding, progress);
			if (builder.isFinish(this.subBuilding, progress)) {
				this.setDead();
			} else {
				log.debug ("Build progress : "+ Math.round(( ((double)progress.iteration) / ((double)this.subBuilding.building.unities.size()*2) ) * 10000D) / 100D + "%");
			}
		}
		
	}
	

}
