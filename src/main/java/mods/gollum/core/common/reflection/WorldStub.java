package mods.gollum.core.common.reflection;

import mods.gollum.core.utils.reflection.DeobfuscateName;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldStub extends World {
	
	private WorldStub() {
		super(null, null, null, new WorldSettings(null), null);
	}
	
	@Override
	@DeobfuscateName (value="tickUpdates")
	public boolean tickUpdates(boolean p_72955_1_) {
		return false;
	}
	
	@Override
	@DeobfuscateName (value="createChunkProvider")
	protected IChunkProvider createChunkProvider() {
		return null;
	}
	
	@Override
	@DeobfuscateName ("func_152379_p")
	protected int func_152379_p() {
		return 0;
	}
	
	@Override
	@DeobfuscateName ("getEntityByID")
	public Entity getEntityByID(int p_73045_1_) {
		return null;
	}

}
