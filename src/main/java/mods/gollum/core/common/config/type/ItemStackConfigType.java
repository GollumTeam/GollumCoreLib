package mods.gollum.core.common.config.type;

import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigProp.Type;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.Json.EntryObject;
import net.minecraft.item.ItemStack;

public class ItemStackConfigType implements IConfigJsonType {
	
	private String registerName;
	private char metadata;
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
		this.metadata = (char)(metadata & 0x000000FF);
	}

	@Override
	public void readConfig(Json dom)  throws Exception {
		this.registerName = dom.child("registerName").strValue();
		this.metadata     = dom.child("metadata")    .charValue();
		this.number       = dom.child("number")      .intValue();
	}

	@Override
	public Json writeConfig() {
		return Json.create (
			new EntryObject ("registerName", this.registerName).addComplement (new JsonConfigProp().type(Type.ITEM) ),
			new EntryObject ("metadata"    , this.metadata),
			new EntryObject ("number"      , this.number)
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
