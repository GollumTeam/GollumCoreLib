package com.gollum.core.common.building.handler;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockCommandBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockCommandBlockBuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return 
			unity.state.getBlock() instanceof BlockCommandBlock
		;
	}
	
	@Override
	protected void applyExtra(
		World world,
		BlockPos pos,
		Unity unity,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		TileEntity te  = world.getTileEntity (pos);
		if (te instanceof TileEntityCommandBlock) {
			
			String command = ""; try { command = unity.extra.get("command"); } catch (Exception e) {} command = (command != null) ? command : "";
			
			
			int varX = 0; try { varX = Integer.parseInt(unity.extra.get("x")); } catch (Exception e) {}
			int varY = 0; try { varY = Integer.parseInt(unity.extra.get("y")); } catch (Exception e) {}
			int varZ = 0; try { varZ = Integer.parseInt(unity.extra.get("z")); } catch (Exception e) {}
			
			command = command.replace("{$x}", ""+(Builder.getRotatedX(varX, varZ, rotate, maxX, maxZ)*rotate.dx + initPos.getX()));
			command = command.replace("{$y}", ""+ (varY + initPos.getY()));
			command = command.replace("{$z}", ""+(Builder.getRotatedZ(varX, varZ, rotate, maxX, maxZ)*rotate.dz + initPos.getZ()));
			ModGollumCoreLib.log.info("command : "+command);
			
			((TileEntityCommandBlock) te).getCommandBlockLogic().setCommand(command);
				
		}
	}
	
}
