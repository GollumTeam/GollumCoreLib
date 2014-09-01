package mods.gollum.core.items;

import mods.gollum.core.tools.helper.items.HItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemKey extends HItem {
	
	public ItemKey(int id, String registerName) {
		super(id, registerName);
	}
	
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.addChatMessage("serveur : side " + side);
			player.addChatMessage("serveur : metadata " + world.getBlockMetadata(x, y, z));
		}
		int id = world.getBlockId(x, y, z);
		
		if (id != 0 && Block.blocksList[id].rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
			return true;
		}
		return false;
	}
}
