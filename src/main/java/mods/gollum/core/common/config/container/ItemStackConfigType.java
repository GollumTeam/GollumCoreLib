package mods.gollum.core.common.config.container;

import java.util.List;

import mods.gollum.core.common.config.IConfigJsonClass;
import net.minecraft.item.ItemStack;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;

public class ItemStackConfigType implements IConfigJsonClass {
	
	private int id;
	private int metadata;
	private int number;
	
	public ItemStackConfigType() throws Exception {
		this (0, 0, 0);
	}
	
	public ItemStackConfigType(int id) {
		this(id, 1);
	}
	
	public ItemStackConfigType(int id, int number) {
		this(id, number, 0);
	}
	
	public ItemStackConfigType(int id, int number, int metadata) {
		this.id = id;
		this.number = number;
		this.metadata = metadata;
	}

	@Override
	public void readConfig(JsonNode json)  throws Exception {
		
		List<JsonNode> list = json.getElements();
		
		this.id       = Integer.parseInt (list.get(0).getNumberValue());
		this.metadata = Integer.parseInt (list.get(1).getNumberValue());
		this.number   = Integer.parseInt (list.get(2).getNumberValue());
	}

	@Override
	public JsonRootNode writeConfig() {
		return JsonNodeFactories.array(
			JsonNodeFactories.number(this.id),
			JsonNodeFactories.number(this.metadata),
			JsonNodeFactories.number(this.number)
		);
	}
	
	public ItemStack getItemStak () {
		return new ItemStack (id, number, metadata);
	}

	public boolean equals (ItemStackConfigType obj) {
		return
			this.id       == obj.id       &&
			this.metadata == obj.metadata &&
			this.number   == obj.number
		;
	}
}
