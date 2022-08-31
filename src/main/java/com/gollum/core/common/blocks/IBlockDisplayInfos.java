package com.gollum.core.common.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockDisplayInfos {
	
	public String displayDebugInfos(World world, BlockPos pos);

}
