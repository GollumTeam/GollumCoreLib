package mods.gollum.core.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Builder;
import mods.gollum.core.common.building.Building;
import mods.gollum.core.common.building.BuildingParser;
import mods.gollum.core.tools.helper.items.HItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBuilding extends HItem {
	
	private Builder builder = new Builder();
	public ArrayList<String>   nameIndex     = null;
	public ArrayList<Building> buildingIndex = null;
	
	public ItemBuilding(int id, String registerName) {
		super(id, registerName);
		
		this.setHasSubtypes(true);
		
	}
	
	private void init () {
		for (Entry<String, Building> entry : BuildingParser.getBuildingsList().entrySet()) {
			this.nameIndex    .add(entry.getKey());
			this.buildingIndex.add(entry.getValue());
		}
	}

	private ArrayList<String> getNameIndex() {
		if (this.nameIndex == null) {
			this.nameIndex     = new ArrayList<String>();
			this.buildingIndex = new ArrayList<Building>();
			this.init();
		}
		return this.nameIndex;
	}

	private ArrayList<Building> getNBuildingIndex() {
		if (this.buildingIndex == null) {
			this.nameIndex     = new ArrayList<String>();
			this.buildingIndex = new ArrayList<Building>();
			this.init();
		}
		return this.buildingIndex;
	}
	
	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		
		if (world.isRemote) {
			return true;
		}
		
		int metadata = itemStack.getItemDamage();
		ArrayList<Building> buildings = this.getNBuildingIndex(); 
		int orientation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		
		if (metadata < buildings.size()) {
			
			Building building = buildings.get(metadata);
			
			ModGollumCoreLib.log.debug("orientation = "+orientation);
			
			builder.build(world, orientation, building, x, y, z); // TODO revoir la rotation
			
		}
		
		return true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		int metadata = itemStack.getItemDamage();
		
		String name= "Building staff";
		if (metadata < this.getNameIndex().size()) {
			name += " : "+this.getNameIndex().get(metadata);
		}
		
		return name;
	}
	
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return this.getUnlocalizedNameInefficiently(par1ItemStack).trim();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(int id, CreativeTabs creativeTabs, List list) {
		for (int metadata = 0; metadata < this.getNameIndex().size(); metadata++) {
			list.add(new ItemStack(id, 1, metadata));
		}
	}
	
}
