package mods.gollum.core.common.config.container;

import java.util.List;

import mods.gollum.core.common.config.IConfigJsonClass;
import mods.gollum.core.tools.registered.RegisteredObjects;
import net.minecraft.item.ItemStack;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;

public class ItemStackConfigType implements IConfigJsonClass {
	
	private String registerName;
	private int metadata;
	private int number;
	
	public ItemStackConfigType() throws Exception {
		this ("", 0, 0);
	}
	
	public ItemStackConfigType(String registerName) {
		this(registerName, 1);
	}
	
	public ItemStackConfigType(String registerName, int number) {
		this(registerName, number, 0);
	}
	
	public ItemStackConfigType(String registerName, int number, int metadata) {
		this.registerName = registerName;
		this.number = number;
		this.metadata = metadata;
	}

	@Override
	public void readConfig(JsonNode json)  throws Exception {
		
		List<JsonNode> list = json.getElements();
		
		this.registerName = list.get(0).getText();
		this.metadata     = Integer.parseInt (list.get(1).getNumberValue());
		this.number       = Integer.parseInt (list.get(2).getNumberValue());
	}

	@Override
	public JsonRootNode writeConfig() {
		return JsonNodeFactories.array(
			JsonNodeFactories.string(this.registerName),
			JsonNodeFactories.number(this.metadata),
			JsonNodeFactories.number(this.number)
		);
	}
	
	public ItemStack getItemStak () {
		return new ItemStack (RegisteredObjects.instance().getItem(registerName), number, metadata);
	}

	public boolean equals (ItemStackConfigType obj) {
		return
			this.registerName.equals(obj.registerName) &&
			this.metadata == obj.metadata &&
			this.number   == obj.number
		;
	}
}
