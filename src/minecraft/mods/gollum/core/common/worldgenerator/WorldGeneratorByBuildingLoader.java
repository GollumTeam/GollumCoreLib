package mods.gollum.core.common.worldgenerator;

import java.io.IOException;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.BuildingParser;
import mods.gollum.core.common.building.ModBuildingParser;
import mods.gollum.core.common.config.ConfigProp;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;

public class WorldGeneratorByBuildingLoader {
	


	// TODO doit disparaitre
	// Ratio de building de chaque type
	@ConfigProp(group = "Spawn rate group [0-10]") public int castleSpawnRate    = 5;
	@ConfigProp(group = "Spawn rate group [0-10]") public int mercenarySpawnRate = 5;
	
	// Ratio de building entre les batiments d'un meme type
	@ConfigProp(group = "Spawn rate between mercenary building") public int mercenaryBuilding1SpawnRate = 7;
	@ConfigProp(group = "Spawn rate between mercenary building") public int mercenaryBuilding2SpawnRate = 6;
	@ConfigProp(group = "Spawn rate between mercenary building") public int mercenaryBuilding3SpawnRate = 1;
	@ConfigProp(group = "Spawn rate between mercenary building") public int mercenaryBuilding4SpawnRate = 1;
	
	@ConfigProp(group = "Spawn rate between castle building") public int castleBuilding1SpawnRate = 6;
	@ConfigProp(group = "Spawn rate between castle building") public int castleBuilding2SpawnRate = 6;
	@ConfigProp(group = "Spawn rate between castle building") public int castleBuilding3SpawnRate = 3;
	@ConfigProp(group = "Spawn rate between castle building") public int castleBuilding4SpawnRate = 2;

	public WorldGeneratorByBuilding load() {
		
		WorldGeneratorByBuilding worldGeneratorByBuilding = new WorldGeneratorByBuilding ();
		
		this.loadBuildingList();
//		
//		int idGroupMercenary = worldGeneratorByBuilding.addGroup (this.config.mercenarySpawnRate);
//		int idGroupCastle    = worldGeneratorByBuilding.addGroup (this.config.castleSpawnRate);
//		
//		// Ajout des batiments
//		worldGeneratorByBuilding.addBuilding (idGroupMercenary, this.buildingMercenary1, this.config.mercenaryBuilding1SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupMercenary, this.buildingMercenary2, this.config.mercenaryBuilding2SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupMercenary, this.buildingMercenary3, this.config.mercenaryBuilding3SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupMercenary, this.buildingMercenary4, this.config.mercenaryBuilding4SpawnRate);
//		
//		worldGeneratorByBuilding.addBuilding (idGroupCastle, this.buildingCastle1, this.config.castleBuilding1SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupCastle, this.buildingCastle2, this.config.castleBuilding2SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupCastle, this.buildingCastle3, this.config.castleBuilding3SpawnRate);
//		worldGeneratorByBuilding.addBuilding (idGroupCastle, this.buildingCastle4, this.config.castleBuilding4SpawnRate);
		
		
		return worldGeneratorByBuilding;
	}
	
	public void loadBuildingList () {
		
		
		ModBuildingParser parser = new ModBuildingParser ();
		
		for (String modId : Loader.instance().getIndexedModList().keySet()) {
			ModGollumCoreLib.log.info("Search buildings in mod : "+modId);
			
			parser.parse (modId);
		}
		
	}
	
	/**
	 * Enregistre les générateur de terrain
	 * @throws IOException 
	 */
	private void initBuildings () {
		
		BuildingParser parser = new BuildingParser ();
//		
//		this.buildingMercenary1 = parser.parse ("mercenary1", this.getModId());
//		this.buildingMercenary2 = parser.parse ("mercenary2", this.getModId());
//		this.buildingMercenary3 = parser.parse ("mercenary3", this.getModId());
//		this.buildingMercenary4 = parser.parse ("mercenary4", this.getModId());
//		this.buildingCastle1    = parser.parse ("castle1", this.getModId());
//		this.buildingCastle2    = parser.parse ("castle2", this.getModId());
//		this.buildingCastle3    = parser.parse ("castle3", this.getModId());
//		this.buildingCastle4    = parser.parse ("castle4", this.getModId());
	}
	
}
