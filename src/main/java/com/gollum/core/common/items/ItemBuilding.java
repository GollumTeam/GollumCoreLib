package com.gollum.core.common.items;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Building;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.inits.ModCreativeTab;
import com.gollum.core.common.building.BuildingParser;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.items.HItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBuilding extends HItem {
	
	private ArrayList<SubBuilding> lastBuildings = new ArrayList<Building.SubBuilding>();
	
	public SubBuilding getLastBuild(int i) {
		return this.lastBuildings.get (lastBuildings.size() - i - 1);
	}
	
	public ArrayList<String>   nameIndex     = null;
	public ArrayList<Building> buildingIndex = null;
	
	public ItemBuilding(String registerName) {
		super(registerName);
		
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
		this.setCreativeTab(ModCreativeTab.tabBuildingStaff);
		
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender(0);
		for (int metadata = 1; metadata < 255; metadata++) {
			helper.registerRender(metadata);
		}
	}
	
	private void initBuildingList () {
		boolean first = this.nameIndex == null;
		this.nameIndex     = new ArrayList<String>();
		this.buildingIndex = new ArrayList<Building>();
		for (Entry<String, Building> entry : BuildingParser.getBuildingsList().entrySet()) {
			this.nameIndex    .add(entry.getKey());
			this.buildingIndex.add(entry.getValue());
		}
	}

	private ArrayList<String> getNameIndex() {
		this.initBuildingList();
		return this.nameIndex;
	}

	private ArrayList<Building> getBuildingIndex() {
		this.initBuildingList();
		return this.buildingIndex;
	}
	

	@Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//		
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		ItemStack itemStack = player.getItemStackFromSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND);
		if (itemStack != null) {
			int metadata = itemStack.getItemDamage();
			ArrayList<Building> buildings = this.getBuildingIndex(); 
			int orientation = MathHelper.floor((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			
			if (metadata < buildings.size()) {
				
				SubBuilding subBuilding = new SubBuilding();
				subBuilding.building = buildings.get(metadata);
				subBuilding.x = pos.getX();
				subBuilding.y = pos.getY();
				subBuilding.z = pos.getZ();
				subBuilding.facing = player.getHorizontalFacing().getOpposite();
				
				ModGollumCoreLib.logger.debug("orientation = "+orientation);
				this.lastBuildings.add(subBuilding);
				Builder.instance().build(world, subBuilding, true);
				
			}
		}

		return EnumActionResult.SUCCESS;
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
	public String getUnlocalizedNameInefficiently(ItemStack itemStack) {
		int metadata = itemStack.getItemDamage();
		
		String name= ModGollumCoreLib.i18n.trans ("Building staff");
		if (metadata < this.getNameIndex().size()) {
			name += " : "+this.getNameIndex().get(metadata);
		}
		
		return name;
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		return this.getUnlocalizedNameInefficiently(itemStack);
	}

	@Override
	public void getSubNames(Map<Integer, String> list) {
		ArrayList<String> names = this.getNameIndex();
		for (int metadata = 0; metadata < names.size(); metadata++) {
			list.put(metadata, names.get(metadata));
		}
	}
	
}
