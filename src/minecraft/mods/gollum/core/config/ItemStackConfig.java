package mods.gollum.core.config;

import net.minecraft.item.ItemStack;

public class ItemStackConfig implements IConfigClass {
	
	
	
	private int id;
	private int number;
	private int metadata;
	
	public ItemStackConfig() throws Exception {
		this (0, 0, 0);
	}
	
	public ItemStackConfig(int id, int number) {
		this(id, number, 0);
	}
	
	public ItemStackConfig(int id, int number, int metadata) {
		this.id = id;
		this.number = number;
		this.metadata = metadata;
	}
	
	public String toString () {
		return this.id+":"+this.metadata+":"+this.number;
	}

	@Override
	public void readConfig(String config)  throws Exception {
		String[] cfg = config.split(":");
		this.id = Integer.parseInt (cfg[0]);
		this.number = Integer.parseInt (cfg[1]);
		this.metadata = Integer.parseInt (cfg[2]);
	}
	
	public ItemStack getItemStak () {
		return new ItemStack (id, number, metadata);
	}
	
}
