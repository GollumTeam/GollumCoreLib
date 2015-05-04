package com.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Builder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class BlockCommandBlockBuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, int x, int y, int z, Block block) {
		return block instanceof BlockCommandBlock;
	}
	
	@Override
	public void applyExtra(
		Block block,
		World world,
		Random random, 
		int x, int y, int z, 
		HashMap<String, String> extra,
		int initX, int initY, int initZ, 
		int rotate,
		int dx, int dz,
		int maxX, int maxZ
	) {
		
		TileEntity te  = world.getTileEntity (x, y, z);
		if (te instanceof TileEntityCommandBlock) {
			
			String command = ""; try { command = extra.get("command"); } catch (Exception e) {} command = (command != null) ? command : "";
			
			
			int varX = 0; try { varX = Integer.parseInt(extra.get("x")); } catch (Exception e) {}
			int varY = 0; try { varY = Integer.parseInt(extra.get("y")); } catch (Exception e) {}
			int varZ = 0; try { varZ = Integer.parseInt(extra.get("z")); } catch (Exception e) {}
			
			command = command.replace("{$x}", ""+(Builder.getRotatedX(varX, varZ, rotate, maxX, maxZ)*dx + initX));
			command = command.replace("{$y}", ""+ (varY + initY));
			command = command.replace("{$z}", ""+(Builder.getRotatedZ(varX, varZ, rotate, maxX, maxZ)*dz + initZ));
			ModGollumCoreLib.log.info("command : "+command);
			
			((TileEntityCommandBlock) te).func_145993_a().func_145752_a(command);
				
		}
	}
	
}
