package com.gollum.core.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.gollum.core.common.blocks.IBlockDisplayInfos;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registered.RegisteredObjects;

public class ItemInfos extends HItem {
	
	public ItemInfos(int id, String registerName) {
		super(id, registerName);

		this.setFull3D();
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		
		if (player != null && world != null) {
			
			int id = world.getBlockId(x, y, z);
			Block block = Block.blocksList[id];
			
			if (block != null) {
				int metadata = world.getBlockMetadata (x, y, z);
				
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Block : pos="+x+"x"+y+"x"+z+", id="+id+", metadata="+EnumChatFormatting.RED+metadata+EnumChatFormatting.WHITE+", rname="+RegisteredObjects.instance().getRegisterName(block)+", name="+block.getUnlocalizedName()));
				if (block instanceof IBlockDisplayInfos) {
					String info;
					if ((info = ((IBlockDisplayInfos)block).displayDebugInfos(world, x, y, z)) != null) {
						player.sendChatToPlayer(ChatMessageComponent.createFromText(info));
					}
				}
			} else {
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Block : pos="+x+"x"+y+"x"+z+", id=0, rname=minecraft:air"));
			}
		}
		
		return true;
	}

}
