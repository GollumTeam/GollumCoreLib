package com.gollum.core.common.command;

import java.util.List;

import javax.annotation.Nullable;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.BuildingParser;
import com.gollum.core.inits.ModItems;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandBuilding extends CommandBase {



	@Override
	public String getName() {
		return "building";
	}

	
	@Override
	public String getUsage(ICommandSender sender) {
		return "building [reload, rebuild]";
	}
	
	public String getCommandUsageRebuild(ICommandSender sender)
	{
		return "building rebuild <num last building>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] arguments) throws CommandException {
		
		if(arguments.length <= 0) {
			throw new WrongUsageException(this.getUsage(sender));
		
		} else if(arguments[0].matches("reload")) {

			
			sender.sendMessage(new TextComponentString("Reload all buildings"));
			
			BuildingParser.reloadAll ();
			
		}  else if(arguments[0].matches("rebuild")) {
			try {
				int last = 0;
				
				if(arguments.length >= 2) {
					last = Integer.parseInt(arguments[1]);
				}
				
				SubBuilding subBuilding = ModItems.BUILDING.getLastBuild (last);
				sender.sendMessage(new TextComponentString("Rebuild last building "+subBuilding.building.modId+":"+subBuilding.building.name));
				
				subBuilding.building = new BuildingParser().parse(subBuilding.building.name, subBuilding.building.modId);
				Builder.instance().build(sender.getEntityWorld(), subBuilding, true);
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new WrongUsageException(this.getCommandUsageRebuild(sender));
			}
			
		} else {
			throw new WrongUsageException(this.getUsage(sender));
		}
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[] {"reload", "rebuild"});
        }
        
        return super.getTabCompletions(server, sender, args, targetPos);
    }
	
	public List addTabCompletionOptions(ICommandSender sender, String[] arguments) {
		
		List rtn = null;

		rtn = (arguments.length == 1) ? getListOfStringsMatchingLastWord(arguments, new String[] {"reload", "rebuild"}) : rtn;
		
		return rtn;
	}

}
