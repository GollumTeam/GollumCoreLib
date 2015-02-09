package mods.gollum.core.client.keys;

import mods.gollum.core.common.blocks.IBlockDisplayInfos;
import mods.gollum.core.tools.registered.RegisteredObjects;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class BlockInfosKeyHandler {
	
	KeyBinding keyBinding; 
	
	public BlockInfosKeyHandler(KeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}

	@SubscribeEvent
	public void onEvent(KeyInputEvent event) {
		
		if (keyBinding.isPressed())  {
			
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world = Minecraft.getMinecraft().theWorld;
			MovingObjectPosition mouse = Minecraft.getMinecraft().objectMouseOver;
			
			if (player != null && world != null && mouse != null) {

				int x = mouse.blockX;
				int y = mouse.blockY;
				int z = mouse.blockZ;
				Block block = world.getBlock(x, y, z);
				if (block != null) {
					int metadata = world.getBlockMetadata (x, y, z);
					
					player.addChatMessage(new ChatComponentText("Block : pos="+x+"x"+y+"x"+z+", id="+Block.getIdFromBlock(block)+", metadata="+EnumChatFormatting.RED+metadata+EnumChatFormatting.WHITE+", rname="+RegisteredObjects.instance().getRegisterName(block)+", name="+block.getUnlocalizedName()));
					if (block instanceof IBlockDisplayInfos) {
						String info;
						if ((info = ((IBlockDisplayInfos)block).displayDebugInfos(world, x, y, z)) != null) {
							player.addChatMessage(new ChatComponentText(info));
						}
					}
				} else {
					player.addChatMessage(new ChatComponentText("Block : pos="+x+"x"+y+"x"+z+", id=0, rname=minecraft:air"));
				}
			}
		}
	}
}
