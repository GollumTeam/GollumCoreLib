package mods.gollum.core.common.config.type;

import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigProp.Type;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.Json.EntryObject;
import mods.gollum.core.tools.simplejson.IJsonObjectDisplay;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemStackConfigType implements IConfigJsonType {
	
	private String registerName;
	private int metadata;
	private int number;
	
	public ItemStackConfigType() throws Exception {
		this(RegisteredObjects.instance().getRegisterName(Items.apple));
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
	public void readConfig(Json dom) {
		this.registerName = dom.child("registerName").strValue();
		this.metadata     = dom.child("metadata")    .byteValue();
		this.number       = dom.child("number")      .intValue();
	}

	@Override
	public Json writeConfig() {
		return Json.create (
			new EntryObject ("registerName", this.registerName).addComplement (new JsonConfigProp().type(Type.ITEM)),
			new EntryObject ("metadata"    , this.metadata),
			new EntryObject ("number"      , this.number).addComplement (new JsonConfigProp().minValue("1").maxValue("64").type(Type.SLIDER))
		);
	}
	
	public ItemStack getItemStak () {
		return new ItemStack (RegisteredObjects.instance().getItem(registerName), number, metadata);
	}

	public boolean equals (Object obj) {
		
		if (obj instanceof ItemStackConfigType) {
			return
				this.registerName.equals(((ItemStackConfigType)obj).registerName) &&
				this.metadata == ((ItemStackConfigType)obj).metadata &&
				this.number   == ((ItemStackConfigType)obj).number
			;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return this.number+" "+this.registerName+":"+this.metadata;
	}
}
