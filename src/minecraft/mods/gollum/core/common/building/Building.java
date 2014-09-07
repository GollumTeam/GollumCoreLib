package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Building {
	
	public static final int ROTATED_0  = 0;
	public static final int ROTATED_90 = 1;
	public static final int ROTATED_180 = 2;
	public static final int ROTATED_270 = 3;
	public static final int ROTATED_360 = 4;
	
	
	public static class Position3D implements Comparable {
		
		private Building building;
		private int x;
		private int y;
		private int z;
		
		public Position3D (Building building, int x, int y, int z) {
			this.building = building;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public int x(int rotate) {
			return (rotate == ROTATED_90 || rotate == ROTATED_270) ? building.maxZ(rotate) - z - 1 : x;
		}
		public int y(int rotate) {
			return y;
		}
		public int z(int rotate) {
			return (rotate == ROTATED_90 || rotate == ROTATED_270) ? x : building.maxZ(rotate) - z - 1;
		}
		
		public boolean equal (Object o) {

			Position3D position3D = (Position3D)o;
			
			return x == position3D.x || y == position3D.y || z == position3D.z;
		}

		@Override
		public int compareTo(Object o) {
			
			Position3D position3D = (Position3D)o;
			
			if (x < position3D.x) { return -1; }
			if (x > position3D.x) { return 1; }
			if (y < position3D.y) { return -1; }
			if (y > position3D.y) { return 1; }
			if (z < position3D.z) { return -1; }
			if (z > position3D.z) { return 1; }
			
			return 0;
		}
		
	}
	
	/**
	 * Un element de lamatrice building
	 */
	public static class Unity {
		
		public static final int ORIENTATION_NONE   = 0;
		public static final int ORIENTATION_UP     = 1;
		public static final int ORIENTATION_DOWN   = 2;
		public static final int ORIENTATION_LEFT   = 3;
		public static final int ORIENTATION_RIGTH  = 4;
		public static final int ORIENTATION_TOP_HORIZONTAL    = 5;
		public static final int ORIENTATION_BOTTOM_HORIZONTAL = 6;
		public static final int ORIENTATION_TOP_VERTICAL      = 7;
		public static final int ORIENTATION_BOTTOM_VERTICAL   = 8;
		
		/**
		 * Contenu d'un objet (des Item uniquement pour le moment)
		 */
		static public class Content {
			
			public static final int TYPE_ITEM  = 0;
			public static final int TYPE_BLOCK = 1;
			
			public Item item = null;
			public int  min = 1;
			public int  max = 1;
			public int  metadata = -1;
			public int  type;
			
		}
		
		public Block block     = null;
		public int metadata    = 0;
		public int orientation = Unity.ORIENTATION_NONE;
		public ArrayList<ArrayList<Content>> contents = new ArrayList();
		public HashMap<String, String> extra = new HashMap<String, String>();
		
	}
	
	public static class DimentionSpawnInfos {

		public int spawnRate = 0;
		public int spawnHeight = 0;
		public ArrayList<Block> blocksSpawn = new ArrayList<Block>();
		

		public DimentionSpawnInfos() {
		}
		
		public DimentionSpawnInfos(int spawnRate, int spawnHeight, ArrayList<Block> blocksSpawn) {
			this.spawnRate   = spawnRate;
			this.spawnHeight = spawnHeight;
			this.blocksSpawn = blocksSpawn;
		}
		
	}
	
	public static class SubBuilding {
		public int x = 0;
		public int y = 0;
		public int z = 0;
		public int orientation;
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
	public LinkedHashMap<Position3D, Unity> unities = new LinkedHashMap<Position3D, Unity>();
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
	public int maxX(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxZ : maxX; }
	public int maxZ(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxX : maxZ; }

	public void setNull (int x, int y, int z) {
		
		maxX = Math.max(maxX, x+1);
		maxY = Math.max(maxY, y+1);
		maxZ = Math.max(maxZ, z+1);
		
		Position3D position3D = new Position3D(this, x, y, z);
		if (this.unities.containsKey(position3D)) {
			this.unities.remove(position3D);
		}
	}
	
	public void set (int x, int y, int z, Unity unity) {
		
		this.setNull(x, y, z);
		
		if (unity != null) {
			this.unities.put(new Position3D(this, x, y, z), unity);
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
