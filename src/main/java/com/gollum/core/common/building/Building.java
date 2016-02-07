package com.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.gollum.core.utils.math.Integer3d;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;

public class Building {
	
	public static enum EnumRotate {
		ROTATED_0 (0, 0, -1, 1),
		ROTATED_90 (90, 1, -1, -1),
		ROTATED_180 (180, 2, 1, -1),
		ROTATED_270 (270, 3, 1, 1);
		
		public final int deg;
		public final int rotate;
		public final int dx;
		public final int dz;
		
		EnumRotate (int deg, int rotate, int dx, int dz) {
			this.deg = deg;
			this.rotate = rotate;
			this.dx = dx;
			this.dz = dz;
		}

		public static EnumRotate getByIndex(int index) {
			switch(index) {
				case 0: return EnumRotate.ROTATED_0;
				case 1: return EnumRotate.ROTATED_90;
				case 2: return EnumRotate.ROTATED_180;
				case 3: return EnumRotate.ROTATED_270;
			}
			return EnumRotate.ROTATED_0;
		}
	}
	
	public static class Unity3D implements Comparable {
		
		private Building building;
		private Integer3d position3d;
		public Unity unity;
		
		
		public Unity3D (Building building, Unity unity, int x, int y, int z) {
			this(building, unity, new Integer3d(x, y, z));
		}
		
		private Unity3D (Building building, Unity unity, Integer3d position3d) {
			this.building   = building;
			this.unity      = unity;
			this.position3d = position3d;
		}
		
		public int x(EnumRotate rotate) {
			return (rotate == EnumRotate.ROTATED_90 || rotate == EnumRotate.ROTATED_270) ? building.maxX(rotate) - this.position3d.z - 1 : this.position3d.x;
		}
		public int y(EnumRotate rotate) {
			return this.position3d.y;
		}
		public int z(EnumRotate rotate) {
			return (rotate == EnumRotate.ROTATED_90 || rotate == EnumRotate.ROTATED_270) ? this.position3d.x : building.maxZ(rotate) - this.position3d.z - 1;
		}

		@Override
		public boolean equals (Object o) {
			return this.position3d.equals(((Unity3D)o).position3d);
		}
		
		@Override
		public int compareTo(Object o) {
			return this.position3d.compareTo(((Unity3D)o).position3d);
		}
		
	}
	
	
	
	/**
	 * Un element de lamatrice building
	 */
	public static class Unity {
		
		/**
		 * Contenu d'un objet (des Item uniquement pour le moment)
		 */
		static public class Content {
			
			public Item item = null;
			public int  min = 1;
			public int  max = 1;
			public int  metadata = -1;
			
		}
		
		public IBlockState state  = null;
		public boolean after     = false;
		public ArrayList<ArrayList<Content>> contents = new ArrayList();
		public HashMap<String, String> extra = new HashMap<String, String>();
		
	}
	
	public static class DimentionSpawnInfos {

		public int spawnRate = 0;
		public int spawnMin = 3;
		public int spawnMax = 256;
		public ArrayList<Block> blocksSpawn = new ArrayList<Block>();
		public ArrayList<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
		

		public DimentionSpawnInfos() {
		}
		
		public DimentionSpawnInfos(int spawnRate, int spawnMin, int spawnMax, ArrayList<Block> blocksSpawn, ArrayList<BiomeGenBase> biomes) {
			this.spawnRate   = spawnRate;
			this.spawnMin    = spawnMin;
			this.spawnMax    = spawnMax;
			this.blocksSpawn = blocksSpawn;
			this.biomes      = biomes;
		}
		
	}
	
	public static class SubBuilding {
		public int x = 0;
		public int y = 0;
		public int z = 0;
		public EnumFacing facing;
		public Building building = new Building();
		
		public void synMax(Building building) {
			this.building.maxX = building.maxX;
			this.building.maxY = building.maxY;
			this.building.maxZ = building.maxZ;
		}
	}
	public static class ListSubBuildings extends ArrayList<SubBuilding>{}
	public static class GroupSubBuildings extends ArrayList<ListSubBuildings>{
		
		public void add(SubBuilding subBuilding) {
			ListSubBuildings listSubBuildings = new ListSubBuildings();
			listSubBuildings.add(subBuilding);
			this.add (listSubBuildings);
		}
	}
	
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public int height = 0;
	public String name = "random";
	public String modId = "";
	public HashMap<Integer, DimentionSpawnInfos> dimentionsInfos = new HashMap<Integer, DimentionSpawnInfos>();
	
	/**
	 * Liste des block de la constuction
	 */
	public TreeSet<Unity3D> unities = new TreeSet<Unity3D>();
	
	
	/**
	 * Liste des blocks posés aléatoirements
	 */
	private ArrayList<GroupSubBuildings> randomGroupSubBuildings = new ArrayList<GroupSubBuildings>();
	
	public Building(String name, String modId) {
		this.name = name;
		this.modId = modId;
	}

	public Building() {
	}
	
	public int maxX() { return maxX; }
	public int maxY() { return maxY; }
	public int maxZ() { return maxZ; }
	public int maxX(EnumRotate rotate) { return (rotate == EnumRotate.ROTATED_90 || rotate == EnumRotate.ROTATED_270) ? maxZ : maxX; }
	public int maxZ(EnumRotate rotate) { return (rotate == EnumRotate.ROTATED_90 || rotate == EnumRotate.ROTATED_270) ? maxX : maxZ; }

	public void setNull (int x, int y, int z) {
		
		maxX = Math.max(maxX, x+1);
		maxY = Math.max(maxY, y+1);
		maxZ = Math.max(maxZ, z+1);
		
		Unity3D unity3D = new Unity3D(this, null, x, y, z);
		if (this.unities.contains(unity3D)) {
			this.unities.remove(unity3D);
		}
	}
	
	public void set (int x, int y, int z, Unity unity) {
		
		this.setNull(x, y, z);
		
		if (unity != null) {
			this.unities.add (new Unity3D(this, unity, x, y, z));
		}
	}
	
	public void addRandomBuildings(GroupSubBuildings groupSubBuildings) {
		this.randomGroupSubBuildings.add(groupSubBuildings);
	}

	public void addBuilding(SubBuilding subBuilding) {
		GroupSubBuildings groupSubBuildings = new GroupSubBuildings();
		groupSubBuildings.add(subBuilding);
		this.addRandomBuildings (groupSubBuildings);
	}
	
	/**
	 * Renvoie la liste des groupes
	 * @return
	 */
	public ArrayList<GroupSubBuildings> getRandomGroupSubBuildings () {
		return this.randomGroupSubBuildings;
	}
	
}
