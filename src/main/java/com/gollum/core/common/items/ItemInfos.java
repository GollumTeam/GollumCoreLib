package com.gollum.core.common.items;

import java.util.Map.Entry;

import com.gollum.core.common.blocks.IBlockDisplayInfos;
import com.gollum.core.inits.ModCreativeTab;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemInfos extends HItem {
	
	public ItemInfos(String registerName) {
		super(registerName);

		this.setFull3D();
		this.setMaxStackSize(1);
		this.setCreativeTab(ModCreativeTab.tabDevTools);
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
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		
		if (player != null && world != null) {
			
			IBlockState state = world.getBlockState(pos);
			
			if (state != null) {
				Block block = state.getBlock();
				player.sendMessage(new TextComponentString("=========="));
				player.sendMessage(new TextComponentString(pos.toString()));
				player.sendMessage(new TextComponentString("  rname: "+TextFormatting.RED+RegisteredObjects.instance().getRegisterName(block)+TextFormatting.WHITE));
				player.sendMessage(new TextComponentString("  id: "+TextFormatting.RED+Block.getIdFromBlock(block)+TextFormatting.WHITE));
				player.sendMessage(new TextComponentString("  metadata: "+TextFormatting.BLUE+block.getMetaFromState(state)+TextFormatting.WHITE));
				player.sendMessage(new TextComponentString("  name: "+TextFormatting.BLUE+block.getUnlocalizedName()+TextFormatting.WHITE));
				player.sendMessage(new TextComponentString("  state: "));
				for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
					IProperty prop = entry.getKey();
					player.sendMessage(new TextComponentString("     "+prop.getName()+": "+TextFormatting.GREEN+state.getValue(prop)+TextFormatting.WHITE));
				}
				if (block instanceof IBlockDisplayInfos) {
					String info;
					if ((info = ((IBlockDisplayInfos)block).displayDebugInfos(world, pos)) != null) {
						player.sendMessage(new TextComponentString(info));
					}
				}
				player.sendMessage(new TextComponentString("=========="));
			} else {
				player.sendMessage(new TextComponentString("Block : "+pos+", id=0, rname=minecraft:air"));
			}
		}
		
		return EnumActionResult.SUCCESS;
	}

}
