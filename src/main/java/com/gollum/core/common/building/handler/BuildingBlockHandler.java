package com.gollum.core.common.building.handler;

import java.util.Random;

import com.gollum.core.common.building.Building;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 * @param metadata2 
	 * @return TODO
	 */
	
	public final int getMetadata(World world, int x, int y, int z, int metadata, Unity unity, int rotate) {
		if (this.mustApply(world, x, y, z, unity)) {
			metadata = this.applyMetadata(world, x, y, z, metadata, unity, rotate);
		}
		return metadata;
	}

	public final void setExtra(
		Block block,
		World world,
		Random random, 
		int x, int y, int z, 
		Unity unity,
		int initX, int initY, int initZ, 
		int rotate,
		int dx, int dz,
		int maxX, int maxZ
	) {
		if (this.mustApply(world, x, y, z, unity)) {
			this.applyExtra(block, world, random, x, y, z, unity, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
		}
	}
	
	protected boolean mustApply (World world, int x, int y, int z, Unity unity) {
		return false;
	}

	protected int applyMetadata(World world, int x, int y, int z, int metadata, Unity unity, int rotate) {
		return metadata;
	}
	
	protected void applyExtra(
		Block block,
		World world,
		Random random, 
		int x, int y, int z,
		Unity unity,
		int initX, int initY, int initZ, 
		int rotate,
		int dx, int dz,
		int maxX, int maxZ
	) {
	}
	
	/**
	 * Retourne l'orientation retourner en fonction de la rotation
	 * @param rotate
	 * @param orientation
	 * @return
	 */
	protected int rotateOrientation(int rotate, int orientation) {
		if (rotate == Building.ROTATED_90) {
			
			switch (orientation) { 
				case Unity.ORIENTATION_UP:
					return Unity.ORIENTATION_RIGTH;
				case Unity.ORIENTATION_RIGTH:
					return Unity.ORIENTATION_DOWN;
				case Unity.ORIENTATION_DOWN:
					return Unity.ORIENTATION_LEFT;
				case Unity.ORIENTATION_LEFT:
					return Unity.ORIENTATION_UP;
					
				case Unity.ORIENTATION_TOP_HORIZONTAL:
					return Unity.ORIENTATION_TOP_VERTICAL;
				case Unity.ORIENTATION_TOP_VERTICAL:
					return Unity.ORIENTATION_TOP_HORIZONTAL;
					
				case Unity.ORIENTATION_BOTTOM_HORIZONTAL:
					return Unity.ORIENTATION_BOTTOM_VERTICAL;
				case Unity.ORIENTATION_BOTTOM_VERTICAL:
					return Unity.ORIENTATION_BOTTOM_HORIZONTAL;
					
				default:
					return Unity.ORIENTATION_NONE;
			}
		}
		if (rotate == Building.ROTATED_180) {
			return this.rotateOrientation(Building.ROTATED_90, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		if (rotate == Building.ROTATED_270) {
			return this.rotateOrientation(Building.ROTATED_180, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		return orientation;
	}
	
}
