package com.gollum.core.common.building.handler;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BlockWallSignBuildingHandler extends BlockDirectionalBuildingHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return 
			unity.state.getBlock() instanceof BlockWallSign
		;
	}
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	@Override
	protected void applyExtra(
		World world,
		BlockPos pos,
		Unity unity,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		TileEntity te = world.getTileEntity (pos);
		if (te instanceof TileEntitySign) {
			
			try {
				String text1 = ""   ; try { text1 = unity.extra.get("text1");                     } catch (Exception e) {} text1 = (text1 != null) ? text1 : "";
				String text2 = ""   ; try { text2 = unity.extra.get("text2");                     } catch (Exception e) {} text2 = (text2 != null) ? text2 : "";
				String text3 = ""   ; try { text3 = unity.extra.get("text3");                     } catch (Exception e) {} text3 = (text3 != null) ? text3 : "";
				String text4 = ""   ; try { text4 = unity.extra.get("text4");                     } catch (Exception e) {} text4 = (text4 != null) ? text4 : "";
				
				((TileEntitySign) te).signText[0] = new ChatComponentText(text1);
				((TileEntitySign) te).signText[1] = new ChatComponentText(text2);
				((TileEntitySign) te).signText[2] = new ChatComponentText(text3);
				((TileEntitySign) te).signText[3] = new ChatComponentText(text4);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
