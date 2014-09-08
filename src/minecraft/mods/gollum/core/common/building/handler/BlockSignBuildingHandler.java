package mods.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.Unity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class BlockSignBuildingHandler extends BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	@Override
	public void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		
		if (block instanceof BlockSign) {
			
			if (block.blockID == 63) {
				metadata = (metadata + 4*rotate) % 0xF;
			} else {
				if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0x8) + 0; } else 
				if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
				if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
				if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
				if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
				{
					ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
				}
			}
			world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
		}
		
	}
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	@Override
	public void setExtra(
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
		if (block instanceof BlockSign) {
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntitySign) {
				
				try {
					String text1 = ""   ; try { text1 = extra.get("text1");                     } catch (Exception e) {} text1 = (text1 != null) ? text1 : "";
					String text2 = ""   ; try { text2 = extra.get("text2");                     } catch (Exception e) {} text2 = (text2 != null) ? text2 : "";
					String text3 = ""   ; try { text3 = extra.get("text3");                     } catch (Exception e) {} text3 = (text3 != null) ? text3 : "";
					String text4 = ""   ; try { text4 = extra.get("text4");                     } catch (Exception e) {} text4 = (text4 != null) ? text4 : "";
					Boolean edit = false; try { edit = Boolean.parseBoolean(extra.get("edit")); } catch (Exception e) {} edit  = (edit != null) ? edit : false;
					
					((TileEntitySign) te).signText[0] = text1;
					((TileEntitySign) te).signText[1] = text2;
					((TileEntitySign) te).signText[2] = text3;
					((TileEntitySign) te).signText[3] = text4;
					((TileEntitySign) te).setEditable(edit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
