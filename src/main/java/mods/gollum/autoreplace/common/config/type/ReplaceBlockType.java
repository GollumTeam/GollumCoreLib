package mods.gollum.autoreplace.common.config.type;

import mods.gollum.autoreplace.common.blocks.BlockAutoReplace.ReplaceBlock;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.ConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.Json.EntryObject;
import mods.gollum.core.tools.simplejson.IJsonObjectDisplay;
import mods.gollum.core.tools.simplejson.JsonArray;
import mods.gollum.core.tools.simplejson.JsonObject;
import net.minecraft.init.Blocks;

public class ReplaceBlockType extends ConfigJsonType {
	
	private String blockRegisterName;
	private ReplaceBlock[] replaceBlocks;
	
	public ReplaceBlockType() {
		this.blockRegisterName = "ModID:blockName";
		this.replaceBlocks     = new ReplaceBlock[] { new ReplaceBlock(Blocks.grass, -1) };
	}
	
	@Override
	public void readConfig(Json json) {
		
		this.blockRegisterName = json.child("blockRegisterName").strValue();
		Json replaceBlocksJson = json.child("replaceBlocks");
		
		this.replaceBlocks = new ReplaceBlock[replaceBlocksJson.size()];
		
		for (int i = 0; i < replaceBlocksJson.size(); i++) {
			this.replaceBlocks[i] = new ReplaceBlock (
				replaceBlocksJson.child(i).child("blockReplace").strValue(),
				replaceBlocksJson.child(i).child("metadata").intValue()
			);
		}
		
	}
	
	@Override
	public Json writeConfig() {
		
		JsonArray replaceBlocksJson = (JsonArray) Json.createArray().addComplement(
			new JsonConfigProp()
				.newValue(this.replaceBlock2Json(new ReplaceBlock(Blocks.grass, -1)))
				.minListLength("1")
				.maxListLength("15")
		);
		
		for (ReplaceBlock replaceBlock : this.replaceBlocks) {
			replaceBlocksJson.add(this.replaceBlock2Json(replaceBlock));
		}
		
		return Json.create(
			new EntryObject("blockRegisterName", this.blockRegisterName).addComplement(
				new JsonConfigProp().pattern("[^:]+:[^:]+")
			),
			new EntryObject("replaceBlocks", replaceBlocksJson)
		);
	}
	
	private JsonObject replaceBlock2Json(ReplaceBlock replaceBlock) {
		return (JsonObject) Json.create(
				
			new EntryObject("blockReplace", replaceBlock.registerName).addComplement(
				new JsonConfigProp().type(ConfigProp.Type.BLOCK)
			),
			new EntryObject("metadata", replaceBlock.getMetadata(-1)).addComplement(
				new JsonConfigProp().minValue("-1").maxValue("15").type(ConfigProp.Type.SLIDER)
			)
		).addComplement(
			new IJsonObjectDisplay() {
				
				@Override
				public String display(Json json) {
					return json.child("blockReplace").strValue()+":"+ ((json.child("metadata").intValue() == -1) ? "*" : json.child("metadata").strValue());
				}
			}
		);
	}
	
	@Override
	public String toString() {
		return this.blockRegisterName;
	}


	public String getModId() {
		return blockRegisterName.split(":")[0];
	}
	
	public String getBlockName() {
		String[] split = blockRegisterName.split(":");
		return split.length > 1 ? split[1] : "";
	}

	public ReplaceBlock[] getReplaceBlocks() {
		return replaceBlocks;
	}
	
}
