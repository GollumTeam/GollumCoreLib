package com.gollum.core.common.items;

import com.gollum.core.common.blocks.IBlockDisplayInfos;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemInfos extends HItem {
	
	public ItemInfos(String registerName) {
		super(registerName);

		this.setFull3D();
		this.setMaxStackSize(1);
	}

    /**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @return Return true to prevent any further processing.
     */
	@Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		if (player != null && world != null) {
			
			IBlockState state = world.getBlockState(pos);
			
			if (state != null) {
				Block block = state.getBlock();
				/* FIXME TODO review displaying infos
				int metadata = world.getBlockMetadata (x, y, z);
				
				player.addChatMessage(new ChatComponentText("Block : pos="+x+"x"+y+"x"+z+", id="+Block.getIdFromBlock(block)+", metadata="+EnumChatFormatting.RED+metadata+EnumChatFormatting.WHITE+", rname="+RegisteredObjects.instance().getRegisterName(block)+", name="+block.getUnlocalizedName()));
				if (block instanceof IBlockDisplayInfos) {
					String info;
					if ((info = ((IBlockDisplayInfos)block).displayDebugInfos(world, x, y, z)) != null) {
						player.addChatMessage(new ChatComponentText(info));
					}
				}
				*/
			} else {
				player.addChatMessage(new ChatComponentText("Block : pos="+pos.toString()+", id=0, rname=minecraft:air"));
			}
		}
		
		return true;
	}

}
