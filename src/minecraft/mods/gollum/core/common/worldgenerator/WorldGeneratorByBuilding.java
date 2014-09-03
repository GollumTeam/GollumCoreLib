package mods.gollum.core.common.worldgenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.blocks.BlockSpawner;
import mods.gollum.core.common.building.Building;
import mods.gollum.core.common.building.Building.Unity;
import mods.gollum.core.common.building.Building.Unity.Content;
import mods.gollum.core.common.tileentities.TileEntityBlockSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;


public class WorldGeneratorByBuilding implements IWorldGenerator {
	
	// TODO a rendre générique
	public static final int DIMENSION_ID_NETHER = -1;
	public static final int DIMENSION_ID_SURFACE = 0;
	
	private static HashMap<String, Boolean> chunkHasABuilding = new HashMap<String, Boolean>();
	
	private class BuildingAndInfos {
		Building building;
		int spawnRate;
	}
	
	/**
	 * Spawn global de tous les batiment de cette instance de worldGenerator
	 */
	ArrayList<Integer> globalSpawnRate = new ArrayList<Integer> ();

	private ArrayList<ArrayList<BuildingAndInfos>> buildingsNether  = new ArrayList<ArrayList<BuildingAndInfos>> ();
	private ArrayList<ArrayList<BuildingAndInfos>> buildingsSurface = new ArrayList<ArrayList<BuildingAndInfos>> ();
	
	/**
	 * Ajoute un groupe de spawn
	 * @param groupSpawnRate
	 * @return
	 */
	public int addGroup(int groupSpawnRate) {
		int id = globalSpawnRate.size ();
		this.globalSpawnRate.add (groupSpawnRate);
		
		ArrayList<BuildingAndInfos> group = new ArrayList<BuildingAndInfos>();
		buildingsNether.add(group);
		buildingsSurface.add(group);
		
		return id;
	}
	
	/**
	 * Ajoute un batiment 
	 * @param building
	 * @param buildingSpawnRate
	 */
	public void addBuilding(int idGroup, Building building, int buildingSpawnRate) {
		this.addBuilding(idGroup, building, buildingSpawnRate, this.DIMENSION_ID_SURFACE);
	}
	
	/**
	 * Ajoute un batiment 
	 * @param idGroup
	 * @param buildingMercenary1
	 * @param mercenaryBuilding1SpawnRate
	 * @param dimensionIdSurface
	 */
	public void addBuilding(int idGroup, Building building, int buildingSpawnRate, int dimensionId) {
		
		BuildingAndInfos buildingAndInfos = new BuildingAndInfos ();
		buildingAndInfos.building         = building;
		buildingAndInfos.spawnRate        = buildingSpawnRate;
		
		switch (dimensionId) {
			case WorldGeneratorByBuilding.DIMENSION_ID_NETHER:
				this.buildingsNether.get (idGroup).add (buildingAndInfos);
				break;
				
			case WorldGeneratorByBuilding.DIMENSION_ID_SURFACE:
				this.buildingsSurface.get (idGroup).add (buildingAndInfos);
				break;
			default:
		}
	}
	
	/**
	 * Methode de genera
	 */
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		// Generation diffenrente entre le nether et la surface
		switch (world.provider.dimensionId) {
			case WorldGeneratorByBuilding.DIMENSION_ID_NETHER:
				
				ArrayList<Integer> randomGenerateNether = new ArrayList<Integer>();
				for (int i = 0; i < this.buildingsNether.size (); i++) {
					randomGenerateNether.add (i);
				}
				Collections.shuffle(randomGenerateNether);
				
				for (int i: randomGenerateNether) {
					this.generateBuilding(world, random, chunkX, chunkZ, buildingsNether.get(i), this.globalSpawnRate.get(i));
				}
				break;
				
			case WorldGeneratorByBuilding.DIMENSION_ID_SURFACE:

				ArrayList<Integer> randomGenerateSurface = new ArrayList<Integer>();
				for (int i = 0; i < this.buildingsNether.size (); i++) {
					randomGenerateSurface.add (i);
				}
				Collections.shuffle(randomGenerateSurface);
				
				for (int i: randomGenerateSurface) {
					this.generateBuilding(world, random, chunkX, chunkZ, buildingsSurface.get(i), this.globalSpawnRate.get(i));
				}
				break;
			default:
		}
	}
	
	/**
	 * Le chunk exist
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean chunkHasBuilding (int chunkX, int chunkZ) {
		try {
			boolean hasBuilding = WorldGeneratorByBuilding.chunkHasABuilding.get(chunkX+"x"+chunkZ);
			return hasBuilding;
		}catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * Il y a une construction autour
	 * @param chunkX
	 * @param chunkZ
	 * @return
	 */
	public boolean hasBuildingArround (int chunkX, int chunkZ) {

		for (int x = chunkX - 6; x < chunkX + 6; x++) {
			for (int z = chunkZ - 6; z < chunkZ + 6; z++) {
				if (this.chunkHasBuilding (x, z)) {
					return true;
				}
			}
		}
		
		return false;
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
	private void generateBuilding(World world, Random random, int chunkX, int chunkZ, ArrayList<BuildingAndInfos> buildings, int groupSpawnRate) {
		
		if (buildings.size() == 0) {
			return;
		}
		
		
		// test du Spawn global
		float multiplicateur = 2;
		if (random.nextInt((int)((Math.pow (10 , multiplicateur) * ((float)this.globalSpawnRate.size())/1.5f))) < (Math.pow (Math.min (groupSpawnRate, 10) , multiplicateur)) ) {
			
			
			if (!this.hasBuildingArround (chunkX, chunkZ)) {
				
				// Position initial de la génération en hauteur
				int worldY = 64;
				int rotate = random.nextInt(Building.ROTATED_360);
//				rotate = Building.ROTATED_90;
				Building building = this.getBuildingInRate (buildings, random).getRotatetedBuilding (rotate);
				
				// Position initiale du batiment
				int initX = chunkX * 16 + random.nextInt(8) - random.nextInt(8);
				int initY = worldY      + random.nextInt(8) - random.nextInt(8);
				int initZ = chunkZ * 16 + random.nextInt(8) - random.nextInt(8);
//				initY = 3; // Pour test sur un superflat
				
				
				//Test si on est sur de la terre (faudrais aps que le batiment vol)
				if (
					world.getBlockId(initX + 3, initY, initZ + 3) == Block.grass.blockID
				) {
					
					// Auteur initiale du batiment 
					initY += building.height + 1;
					initY = (initY > 3) ? initY : 3;
					
					WorldGeneratorByBuilding.chunkHasABuilding.put(chunkX+"x"+chunkZ, true);
					
					ModGollumCoreLib.log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
					
					// Parcours la matrice et ajoute des blocks de stone pour les blocks qui s'accroche
					for (int x = 0; x < building.maxX; x++) {
						for (int y = 0; y < building.maxY; y++) {
							for (int z = 0; z < building.maxZ; z++) {
									// Position réél dans le monde du block
									int finalX = initX + x;
									int finalY = initY + y;
									int finalZ = initZ + z;
									world.setBlock(finalX, finalY, finalZ, Block.grass.blockID, 0, 2);
					
							}
						}
					}
					
					// Parcours la matrice et ajoute les blocks
					for (int x = 0; x < building.maxX; x++) {
						for (int y = 0; y < building.maxY; y++) {
							for (int z = 0; z < building.maxZ; z++) {
								
								Unity unity = building.get(x, y, z);
								
								// Position réél dans le monde du block
								int finalX = initX + x;
								int finalY = initY + y;
								int finalZ = initZ + z;
								
								if (unity.block != null) {
									world.setBlock(finalX, finalY, finalZ, unity.block.blockID, unity.metadata, 2);
								} else {
									world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
								}
	
								this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
								this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
								this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX, building.maxY);
							}
						}
					}
					
					
					//////////////////////////////////
					// Ajoute les blocks aléatoires //
					//////////////////////////////////
					
					for(ArrayList<Building> group: building.getRandomBlocksGroup()) {
						
						Building lisBlockRandom = group.get(random.nextInt(group.size ()));
						
						for (int x = 0; x < lisBlockRandom.maxX; x++) {
							for (int y = 0; y < lisBlockRandom.maxY; y++) {
								for (int z = 0; z < lisBlockRandom.maxZ; z++) {
									Unity unity = lisBlockRandom.get(x, y, z);
									
									// Position réél dans le monde du block
									int finalX = initX + x;
									int finalY = initY + y;
									int finalZ = initZ + z;
									
									if (unity.block != null && unity.block.blockID != 0) {
										world.setBlock(finalX, finalY, finalZ, unity.block.blockID, unity.metadata, 2);
										
										this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
										this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
										this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX, building.maxY);
									}
								}
							}
						}
					}
					
					///////////////////////////////////////////////////////////////
					// Vide tous ce qu'il qu'il y a au dessus de la construction //
					///////////////////////////////////////////////////////////////
					for (int x = 0; x < building.maxX; x++) {
						for (int y = building.maxY; y < 256; y++) {
							for (int z = 0; z < building.maxZ; z++) {
								// Position réél dans le monde du block
								int finalX = initX + x;
								int finalY = initY + y;
								int finalZ = initZ + z;
								world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
							}
						}
					}
					
					/////////////////////////////////////////////////////////////
					// Rempli en dessous du batiment pour pas que ca sois vide //
					/////////////////////////////////////////////////////////////
					
					for (int x = 0; x < building.maxX; x++) {
						for (int z = 0; z < building.maxZ; z++) {
							for (int y = -1; true; y--) {
							int finalX = initX + x;
							int finalY = initY + y;
							int finalZ = initZ + z;
								finalX = initX + x;
								finalY = initY + y;
								finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
							}
						}
					}

					///////////////////////////////////////////////////////////////////////////////
					// Crée un escalier sur les block de remplissage pour que ca sois plus jolie //
					///////////////////////////////////////////////////////////////////////////////
					
					// Les bords
					for (int z = 0; z < building.maxZ; z++) {
						for (int x = -1 ; x >= -4; x--) {
							boolean first = true;
							for (int y = -1; true; y--) {
								
								if (x < y) {
									continue;
								}
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
								first = false;
							}
							if (first) {
								break;
							}
						}
					}
					for (int z = 0; z < building.maxZ; z++) {
						for (int x = building.maxX ; x < building.maxX+4; x++) {
							boolean first = true;
							for (int y = -1; true; y--) {
								
								if (building.maxX - x <= y) {
									continue;
								}
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
								first = false;
							}
							if (first) {
								break;
							}
						}
					}
					for (int x = 0; x < building.maxX; x++) {
						for (int z = -1 ; z >= -4; z--) {
							boolean first = true;
							for (int y = -1; true; y--) {
								
								if (z < y) {
									continue;
								}
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
								first = false;
							}
							if (first) {
								break;
							}
						}
					}
					for (int x = 0; x < building.maxX; x++) {
						for (int z = building.maxZ ; z < building.maxZ+4; z++) {
							boolean first = true;
							for (int y = -1; true; y--) {
								
								if (building.maxZ - z <= y) {
									continue;
								}
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
								first = false;
							}
							if (first) {
								break;
							}
						}
					}
					
					// Les angles
					for (int x = -1 ; x >= -4; x--) {
						for (int z = -1 ; z >= -4; z--) {
							for (int y = -1; true; y--) {
								if (Math.abs(x) + Math.abs(z) >= Math.abs(y) + 1) { continue; }
								if (Math.abs(x) + Math.abs(z) >= 5) { break; }
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
							}
						}
					}
					for (int x = -1 ; x >= -4; x--) {
						for (int z = building.maxZ ; z < building.maxZ+4; z++) {
							for (int y = -1; true; y--) {
								if (Math.abs(x) + Math.abs(z - building.maxZ) >= Math.abs(y)) { continue; }
								if (Math.abs(x) + Math.abs(z - building.maxZ) >= 4) { break; }
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
							}
						}
					}
					for (int x = building.maxX ; x < building.maxX+4; x++) {
						for (int z = -1 ; z >= -4; z--) {
							for (int y = -1; true; y--) {
								if (Math.abs(x - building.maxX) + Math.abs(z) >= Math.abs(y)) { continue; }
								if (Math.abs(x - building.maxX) + Math.abs(z) >= 4) { break; }
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
							}
						}
					}
					for (int x = building.maxX ; x < building.maxX+4; x++) {
						for (int z = building.maxZ ; z < building.maxZ+4; z++) {
							for (int y = -1; true; y--) {
								if (Math.abs(x - building.maxX) + Math.abs(z - building.maxZ) >= Math.abs(y) - 1) { continue; }
								if (Math.abs(x - building.maxX) + Math.abs(z - building.maxZ) >= 3) { break; }
								
								int finalX = initX + x;
								int finalY = initY + y - building.height - 1;
								int finalZ = initZ + z;
								if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
									break;
								}
							}
						}
					}
					
					// /////////////////////
					// Notifie les blocks //
					// /////////////////////
					for (int x = 0; x < building.maxX; x++) {
						for (int y = building.maxY; y < 256; y++) {
							for (int z = 0; z < building.maxZ; z++) {
								// Position réél dans le monde du block
								int finalX = initX + x;
								int finalY = initY + y;
								int finalZ = initZ + z;
								world.setBlockMetadataWithNotify (finalX, finalY, finalZ, world.getBlockMetadata (finalX, finalY, finalZ), 2);
							}
						}
					}
				}
				
			}
		}
	}
	
	/**
	 * Place des escalier
	 */
	private boolean placeUnderBlock (World world, int finalX, int finalY, int finalZ, int profondeur) {
		if (
			world.getBlockId(finalX, finalY, finalZ) != Block.grass.blockID   &&
			world.getBlockId(finalX, finalY, finalZ) != Block.stone.blockID   &&
			world.getBlockId(finalX, finalY, finalZ) != Block.dirt.blockID    &&
			world.getBlockId(finalX, finalY, finalZ) != Block.bedrock.blockID &&
			finalY > 0
		) {
			if (profondeur > -5) {
				world.setBlock(finalX, finalY, finalZ, Block.grass.blockID, 0, 2);
			} else {
				world.setBlock(finalX, finalY, finalZ, Block.stone.blockID, 0, 2);
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * Renvoie le spawn total de tous une liste de batiment
	 * @param buildins
	 * @return
	 */
	private int totalRateSpawnByBuildingList (ArrayList<BuildingAndInfos> buildings) {
		int total = 0;
		for (BuildingAndInfos building : buildings) {
			total += building.spawnRate;
		}
		return total;
	}
	
	/**
	 * Charge le batiment de maniere aléatoire en fonction du ratio
	 * @param buildings
	 * @param totalRate
	 * @return
	 */
	private Building getBuildingInRate(ArrayList<BuildingAndInfos> buildings, Random random) {
		
		ArrayList<Building>buildingsForRate = new ArrayList<Building>();
		
		for (BuildingAndInfos buildingAndInfos : buildings) {
			for (int i = 0; i < buildingAndInfos.spawnRate; i++) {
				buildingsForRate.add(buildingAndInfos.building);
			}
		}
		
		return buildingsForRate.get(random.nextInt(this.totalRateSpawnByBuildingList (buildings)));
	}
	
	/**
	 * Retourne l'orientation retourner en fonction de la rotation
	 * @param rotate
	 * @param orientation
	 * @return
	 */
	private int rotateOrientation(int rotate, int orientation) {
		if (rotate == Building.ROTATED_90) {
			
			switch (orientation) { 
				case Unity.ORIENTATION_UP:
					return Unity.ORIENTATION_RIGTH;
				case Unity.ORIENTATION_RIGTH:
					return Unity.ORIENTATION_DOWN;
				case Unity.ORIENTATION_DOWN:
					return Unity.ORIENTATION_LEFT;
				case Unity.ORIENTATION_LEFT:
					return Unity.ORIENTATION_UP;
					
				case Unity.ORIENTATION_TOP_HORIZONTAL:
					return Unity.ORIENTATION_TOP_VERTICAL;
				case Unity.ORIENTATION_TOP_VERTICAL:
					return Unity.ORIENTATION_TOP_HORIZONTAL;
					
				case Unity.ORIENTATION_BOTTOM_HORIZONTAL:
					return Unity.ORIENTATION_BOTTOM_VERTICAL;
				case Unity.ORIENTATION_BOTTOM_VERTICAL:
					return Unity.ORIENTATION_BOTTOM_HORIZONTAL;
					
				default:
					return Unity.ORIENTATION_NONE;
			}
		}
		if (rotate == Building.ROTATED_180) {
			return this.rotateOrientation(Building.ROTATED_90, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		if (rotate == Building.ROTATED_270) {
			return this.rotateOrientation(Building.ROTATED_180, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		return orientation;
	}
	

	/**
	 * Insert les extras informations du block
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param x
	 * @param contents
	 * @param initX
	 * @param initY
	 * @param initZ
	 * @param rotate	
	 */
	private void setExtra(World world, Random random, int x, int y, int z, HashMap<String, String> extra,int initX, int initY, int initZ, int rotate, int maxX, int maxZ) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		
		if (block instanceof BlockCommandBlock) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityCommandBlock) {
				
				String command = ""; try { command = extra.get("command"); } catch (Exception e) {} command = (command != null) ? command : "";
				
				
				int varX = 0; try { varX = Integer.parseInt(extra.get("x")); } catch (Exception e) {}
				int varY = 0; try { varY = Integer.parseInt(extra.get("y")); } catch (Exception e) {}
				int varZ = 0; try { varZ = Integer.parseInt(extra.get("z")); } catch (Exception e) {}
				
				command = command.replace("{$x}", ""+(this.getRotatedX(varX, varZ, rotate, maxX, maxZ) + initX));
				command = command.replace("{$y}", ""+ (varY + initY));
				command = command.replace("{$z}", ""+(this.getRotatedZ(varX, varZ, rotate, maxX, maxZ) + initZ));
				ModGollumCoreLib.log.info("command : "+command);
				
				((TileEntityCommandBlock) te).setCommand(command);
				
			}
			
		}
		
		if (block instanceof BlockSpawner) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityBlockSpawner) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
				((TileEntityBlockSpawner) te).setModId (entity);
			}
		}
		

		if (block instanceof BlockMobSpawner) {

			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityMobSpawner) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Pigg";
				((TileEntityMobSpawner) te).getSpawnerLogic().setMobID(entity);
			}
		}
	}
	
	/**
	 * Retourne le block
	 * @param x
	 * @param z
	 * @param rotate
	 * @param maxZ
	 * @return
	 */
	private int getRotatedX(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return z;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return this.getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return this.getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		return x;
	}
	
	/**
	 * Retourne le block
	 * @param x
	 * @param z
	 * @param rotate
	 * @param maxX
	 * @return
	 */
	private int getRotatedZ(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return maxX - x -1;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return this.getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return this.getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		return z;
	}

	/**
	 * Retourne le block
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param z
	 * @param contents
	 */
	private void setContents(World world, Random random, int x, int y, int z, ArrayList<ArrayList<Content>> contents) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		
		if (block instanceof BlockContainer) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof IInventory) {
				
				for (int i = 0; i < contents.size(); i++) {
					
					ArrayList<Content> groupItem = contents.get(i);
					
					// Recupère un item aléatoirement
					Content content = groupItem.get(random.nextInt (groupItem.size()));
					// Calcule le nombre aléatoire d'item
					int diff   = content.max - content.min;
					int nombre = content.min + ((diff > 0) ? random.nextInt (diff) : 0);
					
					ItemStack itemStack;
					if (content.type == Content.TYPE_ITEM) {
						if (content.metadata == -1) {
							itemStack = new ItemStack(Item.itemsList[content.id], nombre);
						} else {
							itemStack = new ItemStack(Item.itemsList[content.id], nombre, content.metadata);
						}
					} else {
						if (content.metadata == -1) {
							itemStack = new ItemStack(Block.blocksList [content.id], nombre);
						} else {
							itemStack = new ItemStack(Block.blocksList [content.id], nombre, content.metadata);
						}
					}
					((IInventory) te).setInventorySlotContents (i, itemStack);
				}
			}
		}
		
	}
	

	/**
	 * Affecte l'orientation
	 * @param i
	 * @param j
	 * @param k
	 * @param orientation
	 * @param rotation
	 */
	private void setOrientation(World world, int x, int y, int z, int orientation) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		int metadata = world.getBlockMetadata (x, y, z);
		
		if (
			block instanceof BlockTorch ||
			block instanceof BlockButton
		) {
			
			if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0x8) + 0; } else 
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}

		if (block instanceof BlockDirectional) {
			
			if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0xC) + 2; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0xC) + 3; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0xC) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockDoor) {

			if ((metadata & 0x8) != 0x8) {
				if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x4) + 3; } else 
				if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x4) + 1; } else 
				if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x4) + 2; } else 
				if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x4) + 0; } else 
				{
					ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
				}
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockTrapDoor) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 1; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (
			block instanceof BlockLadder ||
			block instanceof BlockFurnace ||
			block instanceof BlockChest ||
			block instanceof BlockDispenser ||
			block instanceof BlockPistonBase
		) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockStairs) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0xC) + 2; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0xC) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0xC) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}

		if (block instanceof BlockLever) {
			

			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
			
			if (orientation == Unity.ORIENTATION_BOTTOM_VERTICAL)   { metadata = (metadata & 0x8) + 5; } else 
			if (orientation == Unity.ORIENTATION_BOTTOM_HORIZONTAL) { metadata = (metadata & 0x8) + 6; } else
			
			if (orientation == Unity.ORIENTATION_TOP_VERTICAL)   { metadata = (metadata & 0x8) + 7; } else 
			if (orientation == Unity.ORIENTATION_TOP_HORIZONTAL) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
	}

}
