package com.gollum.core.common.items;

import java.util.Map.Entry;

import com.gollum.core.common.blocks.IBlockDisplayInfos;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
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
				player.addChatMessage(new ChatComponentText("=========="));
				player.addChatMessage(new ChatComponentText(pos.toString()));
				player.addChatMessage(new ChatComponentText("  rname: "+EnumChatFormatting.RED+RegisteredObjects.instance().getRegisterName(block)+EnumChatFormatting.WHITE));
				player.addChatMessage(new ChatComponentText("  id: "+EnumChatFormatting.RED+Block.getIdFromBlock(block)+EnumChatFormatting.WHITE));
				player.addChatMessage(new ChatComponentText("  metadata: "+EnumChatFormatting.BLUE+block.getMetaFromState(state)+EnumChatFormatting.WHITE));
				player.addChatMessage(new ChatComponentText("  name: "+EnumChatFormatting.BLUE+block.getUnlocalizedName()+EnumChatFormatting.WHITE));
				player.addChatMessage(new ChatComponentText("  state: "));
				for (Object o : state.getProperties().entrySet()) {
					Entry<IProperty, Comparable> entry = (Entry<IProperty, Comparable>) o;
					IProperty prop = entry.getKey();
					player.addChatMessage(new ChatComponentText("     "+prop.getName()+": "+EnumChatFormatting.GREEN+state.getValue(prop)+EnumChatFormatting.WHITE));
				}
				if (block instanceof IBlockDisplayInfos) {
					String info;
					if ((info = ((IBlockDisplayInfos)block).displayDebugInfos(world, pos)) != null) {
						player.addChatMessage(new ChatComponentText(info));
					}
				}
				player.addChatMessage(new ChatComponentText("=========="));
			} else {
				player.addChatMessage(new ChatComponentText("Block : "+pos+", id=0, rname=minecraft:air"));
			}
		}
		
		return true;
	}

}
