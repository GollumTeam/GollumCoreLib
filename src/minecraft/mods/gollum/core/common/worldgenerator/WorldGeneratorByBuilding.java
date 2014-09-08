package mods.gollum.core.common.worldgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import mods.gollum.core.common.building.Builder;
import mods.gollum.core.common.building.Building;
import mods.gollum.core.common.building.Building.DimentionSpawnInfos;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;


public class WorldGeneratorByBuilding implements IWorldGenerator {
	
	private final static int ARROUND_CHUNK_NOBUILDING = 6;
	private static ArrayList<String> chunkHasABuilding = new ArrayList<String>();
	
	private Builder builder = new Builder();
	
	/**
	 * Spawn global de tous les batiment de cette instance de worldGenerator
	 */
	ArrayList<Integer> globalSpawnRate = new ArrayList<Integer> ();
	
	/**
	 * [DIMENTION:[ID_GROUP[Building]]]
	 */
	private HashMap<Integer, HashMap<Integer, ArrayList<Building>>> buildings  = new HashMap<Integer, HashMap<Integer, ArrayList<Building>>> ();
	
	/**
	 * Ajoute un groupe de spawn
	 * @param groupSpawnRate
	 * @return
	 */
	public int addGroup(int groupSpawnRate) {
		int id = globalSpawnRate.size ();
		this.globalSpawnRate.add (groupSpawnRate);
		
		return id;
	}
	
	/**
	 * Ajoute un batiment 
	 * @param idGroup
	 * @param buildingMercenary1
	 * @param mercenaryBuilding1SpawnRate
	 * @param dimensionIdSurface
	 */
	public void addBuilding(int idGroup, Building building) {
		
		for (Entry<Integer, DimentionSpawnInfos> entry : building.dimentionsInfos.entrySet()) {
			
			int dimensionId = entry.getKey();
			
			if (!this.buildings.containsKey (dimensionId)) {
				this.buildings.put (dimensionId, new HashMap<Integer, ArrayList<Building>>());
			}
			if (!this.buildings.get (dimensionId).containsKey(idGroup)) {
				this.buildings.get (dimensionId).put (idGroup, new ArrayList<Building>());
			}
			
			this.buildings.get (dimensionId).get(idGroup).add(building);
		}
	}
	
	/**
	 * Le chunk exist
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean chunkHasBuilding (int chunkX, int chunkZ) {
		return WorldGeneratorByBuilding.chunkHasABuilding.contains(chunkX+"x"+chunkZ);
	}
	
	/**
	 * Il y a une construction autour
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean hasBuildingArround (int chunkX, int chunkZ) {

		for (int x = chunkX - ARROUND_CHUNK_NOBUILDING; x < chunkX + ARROUND_CHUNK_NOBUILDING; x++) {
			for (int z = chunkZ - ARROUND_CHUNK_NOBUILDING; z < chunkZ + ARROUND_CHUNK_NOBUILDING; z++) {
				if (this.chunkHasBuilding (x, z)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Charge le batiment de maniere aléatoire en fonction du ratio
	 * @param buildings
	 * @param totalRate
	 * @return
	 */
	private Building getBuildingByRate(ArrayList<Building> buildings, int dimention, Random random) {
		
		ArrayList<Building>buildingsForRate = new ArrayList<Building>();
		
		for (Building building : buildings) {
			
			if (building.dimentionsInfos.containsKey(dimention)) {
				DimentionSpawnInfos dimentionsInfos = building.dimentionsInfos.get(dimention);
				for (int i = 0; i < dimentionsInfos.spawnRate; i++) {
					buildingsForRate.add(building);
				}
			}
			
		}
		if (buildingsForRate.isEmpty()) {
			return null;
		}
		
		
		return buildingsForRate.get(random.nextInt(buildingsForRate.size()));
	}
	
	/**
	 * Methode de generation
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		int dimention = world.provider.dimensionId;
		
		if (this.buildings.containsKey(dimention)) {
			
			HashMap<Integer, ArrayList<Building>> buildingsByDim = this.buildings.get(dimention);
			
			ArrayList<Integer> randomGenerate = new ArrayList<Integer>();
			for (int i : buildingsByDim.keySet()) { // Parcour tous les groupes
				randomGenerate.add (i);
			}
			Collections.shuffle(randomGenerate);
			
			for (int i: randomGenerate) {
				if (this.generateBuilding(world, random, chunkX, chunkZ, buildingsByDim.get(i), this.globalSpawnRate.get(i), dimention)) {
					break;
				}
			}
			
		}
	}
	
	/**
	 * Genere le batiment dans le terrain correspondant
	 * @param world
	 * @param random
	 * @param wolrdX
	 * @param wolrdZ
	 * @param buildings
	 * @param random
	 */
	private boolean generateBuilding(World world, Random random, int chunkX, int chunkZ, ArrayList<Building> buildings, int groupSpawnRate, int dimention) {
		
		if (buildings.size() == 0) {
			return false;
		}
		
		// test du Spawn global du groupe si echoue apsse au group suivant
		float multiplicateur = 2;
		if (random.nextInt((int)((Math.pow (10 , multiplicateur) * ((float)this.globalSpawnRate.size())/1.5f))) < (Math.pow (Math.min (groupSpawnRate, 10) , multiplicateur)) ) {
			
			
			int rotate = random.nextInt(Building.ROTATED_360);
			Building building = this.getBuildingByRate (buildings, dimention, random);
			if (building == null) {
				return false;
			}
			DimentionSpawnInfos dimentionsInfos = building.dimentionsInfos.get(dimention);
			
			// Position initial de la génération en hauteur
			int worldY = dimentionsInfos.spawnHeight;
			
			// Position initiale du batiment
			int initX = chunkX * 16 + random.nextInt(8) - random.nextInt(8);
			int initY = worldY      + random.nextInt(8) - random.nextInt(8);
			int initZ = chunkZ * 16 + random.nextInt(8) - random.nextInt(8);
			
			if (!this.hasBuildingArround (chunkX, chunkZ)) {
				
				int blockId = world.getBlockId(initX + 3, initY, initZ + 3);
				
				//Test si on est sur de la terre (faudrais aps que le batiment vol)
				if (blockId != 0 && world.isAirBlock(initX + 3, initY+1, initZ + 3) && dimentionsInfos.blocksSpawn.contains(Block.blocksList[blockId])) {
					
					// Auteur initiale du batiment 
					initY += 1;
					
					// Garde en mémoire que le chunk à généré un batiment (évite que tous se monte dessus)
					// N'est pas sauvegardé enc as d'arret du serveur mais ca devrais pas dérangé
					WorldGeneratorByBuilding.chunkHasABuilding.add(chunkX+"x"+chunkZ);
					
					builder.build(world, rotate, building, initX, initY, initZ);
					
					return true;
				}
			}
		}
		
		return false;
	}
}
