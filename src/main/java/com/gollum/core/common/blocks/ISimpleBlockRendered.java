package com.gollum.core.common.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISimpleBlockRendered {
	
	@SideOnly(Side.CLIENT)
	int getGCLRenderType();

}
