package mods.gollum.core.client.keys;

import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class GollumKeybinds extends KeyHandler {
	
	static KeyBinding KeyBindBlockInfos = new KeyBinding("Gollum Block infos", 62);
	
	public GollumKeybinds() {
		super(new KeyBinding[] { KeyBindBlockInfos }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "GollumBlockInfos";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		
		if (kb.keyDescription == KeyBindBlockInfos.keyDescription)  {
			
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world = Minecraft.getMinecraft().theWorld;
			MovingObjectPosition mouse = Minecraft.getMinecraft().objectMouseOver;
			
			
			if (player != null && world != null && mouse != null) {

				int x = mouse.blockX;
				int y = mouse.blockY;
				int z = mouse.blockZ;
				int id = world.getBlockId(x, y, z);
				if (id != 0) {
					int metadata = world.getBlockMetadata (x, y, z);
					Block block = Block.blocksList[id];
					
					player.addChatMessage("Block : pos="+x+"x"+y+"x"+z+" id="+EnumChatFormatting.RED+id+EnumChatFormatting.WHITE+", metadata="+EnumChatFormatting.RED+metadata+EnumChatFormatting.WHITE+", name="+block.getUnlocalizedName());
				} else {
					player.addChatMessage("Block : pos="+x+"x"+y+"x"+z+" id="+EnumChatFormatting.RED+0);
				}
			}
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
