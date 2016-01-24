package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockCommandBlockBuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return 
			state != null && state.getBlock() instanceof BlockCommandBlock
		;
	}
	
	@Override
	protected void applyExtra(
		World world,
		BlockPos pos,
		IBlockState state,
		HashMap<String, String> extra,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		/* FIXME
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
		*/
	}
	
}
