package com.gollum.core.common.worldgenerator;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Building;
import com.gollum.core.common.building.Building.DimentionSpawnInfos;
import com.gollum.core.common.events.BuildingGenerateEvent;
import com.gollum.core.utils.math.Integer3d;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.IWorldGenerator;


public class WorldGeneratorByBuilding implements IWorldGenerator {
	
	private final static int ARROUND_CHUNK_NOBUILDING = 6;
	private HashMap<Integer, ArrayList<String>> chunkHasABuilding = new HashMap<Integer, ArrayList<String>>();
	
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
	 * @param world 
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean chunkHasBuilding (World world, int chunkX, int chunkZ) {
		int wId = System.identityHashCode(world);
		return
			this.chunkHasABuilding.containsKey(wId) &&
			this.chunkHasABuilding.get(wId).contains(chunkX+"x"+chunkZ)
		;
	}
	
	/**
	 * Le chunk exist
	 * @param world 
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	protected void chunkMarkHasBuilding (World world, int chunkX, int chunkZ) {
		int wId = System.identityHashCode(world);
		if (!this.chunkHasABuilding.containsKey(wId)) {
			this.chunkHasABuilding.put(wId, new ArrayList<String>());
		}
		this.chunkHasABuilding.get(wId).add(chunkX+"x"+chunkZ);
	}
	
	/**
	 * Il y a une construction autour
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean hasBuildingArround (World world, int chunkX, int chunkZ) {

		for (int x = chunkX - ARROUND_CHUNK_NOBUILDING; x < chunkX + ARROUND_CHUNK_NOBUILDING; x++) {
			for (int z = chunkZ - ARROUND_CHUNK_NOBUILDING; z < chunkZ + ARROUND_CHUNK_NOBUILDING; z++) {
				if (this.chunkHasBuilding (world, x, z)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Methode de generation
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		int dimention = world.provider.getDimensionId();
		
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

	private ArrayList<Building> filterByBiome(BiomeGenBase biomeGenBase, ArrayList<Building> buildings, int dimention) {
		
		ArrayList<Building> rtn = new ArrayList<Building>();
		
		for (Building building: buildings) {
			DimentionSpawnInfos dim = buildings.get(dimention).dimentionsInfos.get(dimention);
			if (dim.biomes.size() == 0 && dim.biomes.contains(biomeGenBase)) {
				rtn.add(building);
			}
		}
		
		return rtn;
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
		
		buildings = this.filterByBiome(world.getBiomeGenForCoords(new BlockPos(chunkX*16, 1, chunkZ*16)), buildings, dimention);
		
		if (buildings.size() == 0) {
			return false;
		}
		
		// test du Spawn global du groupe si echoue apsse au group suivant
		float multiplicateur = 2;
		if (
			random.nextInt((int)((Math.pow (10 , multiplicateur) * ((float)this.globalSpawnRate.size())/1.5f))) < (Math.pow (Math.min (groupSpawnRate, 10) , multiplicateur))
		) {
			
			int     rotate     = random.nextInt(Building.ROTATED_360);
			Block[] blocksList = new Block[256];
			
			Collections.shuffle(buildings);
			
			if (!this.hasBuildingArround (world, chunkX, chunkZ)) {
				
				for (Building building : buildings) {
					
					if (building == null) {
						continue;
					}
					
					DimentionSpawnInfos dimentionsInfos = building.dimentionsInfos.get(dimention);
					
					int strengSpawn = random.nextInt(600);
					
					if (strengSpawn < dimentionsInfos.spawnRate) {
						
						// Position initiale du batiment
						int initX = chunkX * 16 + random.nextInt(8) - random.nextInt(8);
						int initZ = chunkZ * 16 + random.nextInt(8) - random.nextInt(8);
						
						if (dimentionsInfos.spawnMax >= dimentionsInfos.spawnMin) {
							int initY = dimentionsInfos.spawnMax;
							for (; initY >= dimentionsInfos.spawnMin;initY--) {
								
								if (initY > 255) {
									continue;
								}
								if (initY < 3) {
									continue;
								}
								
								if (blocksList[initY] == null) {
									blocksList[initY] = world.getBlockState(new BlockPos(initX + 3, initY, initZ + 3)).getBlock();
								}
								if (blocksList[initY+1] == null) {
									blocksList[initY+1] = world.getBlockState(new BlockPos(initX + 3, initY+1, initZ + 3)).getBlock();
								}
								
								Block block   = blocksList[initY];
								Block blockP1 = blocksList[initY+1];
								
								//Test si on est sur de la terre (faudrais pas que le batiment vol)
								if (
									block != null && 
									block != Blocks.air && 
									(
										blockP1 == null ||
										blockP1 == Blocks.air 
									) &&
									dimentionsInfos.blocksSpawn.contains(block)
								) {
									
									// Auteur initiale du batiment 
									initY += 1;
									
									// Garde en mémoire que le chunk à généré un batiment (évite que tous se monte dessus)
									// N'est pas sauvegardé enc as d'arret du serveur mais ca devrais pas dérangé
									this.chunkMarkHasBuilding (world, chunkX, chunkZ);
									
									BuildingGenerateEvent event = new BuildingGenerateEvent.Pre(world, building, rotate, new Integer3d(initX, initY, initZ));
									MinecraftForge.EVENT_BUS.post(event);
									if (event.isCanceled()) {
										return false;
									}
									builder.build(world, building, rotate, initX, initY, initZ);
									
									return true;
								}
							}
							if (initY > dimentionsInfos.spawnMin) {
								log.debug ("No block found for building "+building.name);
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}
