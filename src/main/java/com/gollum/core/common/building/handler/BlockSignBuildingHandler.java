package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSignBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return 
			state != null && state.getBlock() instanceof BlockTripWireHook
		;
	}
	
	@Override
	public void applyOrientation(World world, BlockPos pos, IBlockState state, EnumFacing facing, EnumRotate rotate) {
		/* FIXME
		boolean standing = true;
		try {
			// 1.7.10 field_149967_b
			// 1.7.2  field_149967_b
			// 1.6.4  b
			Field f = BlockSign.class.getDeclaredField("field_149967_b");
			f.setAccessible(true);
			standing = (Boolean) f.get(block);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (standing) {
			metadata = (metadata + 4*rotate) % 0xF;
		} else {
			if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0x8) + 0; } else 
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
			}
		}
		world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
		*/
	}
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
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
		TileEntity te = world.getTileEntity (x, y, z);
		if (te instanceof TileEntitySign) {
			
			try {
				String text1 = ""   ; try { text1 = extra.get("text1");                     } catch (Exception e) {} text1 = (text1 != null) ? text1 : "";
				String text2 = ""   ; try { text2 = extra.get("text2");                     } catch (Exception e) {} text2 = (text2 != null) ? text2 : "";
				String text3 = ""   ; try { text3 = extra.get("text3");                     } catch (Exception e) {} text3 = (text3 != null) ? text3 : "";
				String text4 = ""   ; try { text4 = extra.get("text4");                     } catch (Exception e) {} text4 = (text4 != null) ? text4 : "";
				
				((TileEntitySign) te).signText[0] = text1;
				((TileEntitySign) te).signText[1] = text2;
				((TileEntitySign) te).signText[2] = text3;
				((TileEntitySign) te).signText[3] = text4;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
}
