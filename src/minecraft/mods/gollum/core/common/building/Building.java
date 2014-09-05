package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.Block;

public class Building implements Cloneable {
	
	public static final int ROTATED_0  = 0;
	public static final int ROTATED_90 = 1;
	public static final int ROTATED_180 = 2;
	public static final int ROTATED_270 = 3;
	public static final int ROTATED_360 = 4;
	
	/**
	 * Un element de lamatrice building
	 */
	static public class Unity implements Cloneable {
		
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
		static public class Content implements Cloneable {
			
			public static final int TYPE_ITEM  = 0;
			public static final int TYPE_BLOCK = 1;
			
			public int id = 0;
			public int min = 1;
			public int max = 1;
			public int metadata = -1;
			public int type;
			
			public Object clone() {
				Content o  = new Content ();
				o.id       = this.id;
				o.min      = this.min;
				o.max      = this.max;
				o.metadata = this.metadata;
				o.type = this.type;
				return o;
			}
			
		}
		
		public Block block     = null;
		public int metadata    = 0;
		public int orientation = Unity.ORIENTATION_NONE;
		public ArrayList<ArrayList<Content>> contents = new ArrayList();
		public HashMap<String, String> extra = new HashMap<String, String>();
		
		/**
		 * Clone l'objet
		 */
		public Object clone() {
			Unity o = new Unity ();
			o.block       = this.block;
			o.metadata    = this.metadata;
			o.orientation = this.orientation;
			o.extra       = new HashMap<String, String>();
			for (String key: this.extra.keySet()) {
				o.extra.put(key, this.extra.get(key));
			}
			
			for (ArrayList<Content> groupEl : this.contents) {
				
				ArrayList<Content> newGroupEl = new ArrayList();
				for (Content el: groupEl) {
					newGroupEl.add ((Content) el.clone ());
				}
				
				o.contents.add(newGroupEl);
			}
			
			return o;
		}
	}
	
	static public class DimentionSpawnInfos {

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
		
		/**
		 * Clone l'objet
		 */
		public Object clone() {
			DimentionSpawnInfos o = new DimentionSpawnInfos ();
			o.spawnHeight = this.spawnHeight;
			o.spawnRate   = this.spawnRate;
			for (Block block : this.blocksSpawn) {
				o.blocksSpawn.add (block);
			}
			return o;
		}
	}
	
	private int maxX;
	private int maxY;
	private int maxZ;
	
	public int height = -1;
	public String name = "";
	public HashMap<Integer, DimentionSpawnInfos> dimentionsInfos = new HashMap<Integer, DimentionSpawnInfos>();
	
	/**
	 * Liste des block de la constuction
	 */
	private ArrayList<ArrayList<ArrayList<Unity>>> blocks = new ArrayList<ArrayList<ArrayList<Unity>>>();
	/**
	 * Liste des blocks posés aléatoirements
	 */
	private ArrayList<ArrayList<Building>> groupsRandomBlocks = new ArrayList<ArrayList<Building>>();
	
	public Building(String name) {
		this.name = name;
	}

	public Building() {
	}
	
	/**
	 * Clone l'objet
	 */
	public Object clone() {
		Building o = new Building (this.name);
		
		o.maxX = this.maxX;
		o.maxY = this.maxY;
		o.maxZ = this.maxZ;
		
		for (int x = 0; x < this.maxX; x++) {
			for (int y = 0; y < this.maxY; y++) {
				for (int z = 0; z < this.maxZ; z++) {
					o.set (x, y, z, this.get(x, y, z));
				}
			}
		}
		
		ArrayList<ArrayList<Building>> newGroupsRandomBlocks = new ArrayList();
		for (ArrayList<Building> groupBlock: this.groupsRandomBlocks) {
			
			ArrayList<Building> newGroupBlock = new ArrayList();
			for (Building blocks: groupBlock) {
				newGroupBlock.add (blocks);
			}
			newGroupsRandomBlocks.add(newGroupBlock);
		}
		o.groupsRandomBlocks = newGroupsRandomBlocks;
		o.height = this.height;
		
		for (Entry<Integer, DimentionSpawnInfos> entry : this.dimentionsInfos.entrySet()) {
			o.dimentionsInfos.put(entry.getKey(), (DimentionSpawnInfos) entry.getValue().clone());
		}
		
		return o;
	}
	
	public int maxX() { return maxX; }
	public int maxY() { return maxY; }
	public int maxZ() { return maxZ; }
	public int maxX(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxZ : maxX; }
	public int maxZ(int rotate) { return (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) ? maxX : maxZ; }
	
	public void syncMax(Building building) {
		this.maxX = Math.max(this.maxX, building.maxX);
		this.maxY = Math.max(this.maxY, building.maxY);
		this.maxZ = Math.max(this.maxZ, building.maxZ);
		building.maxX = Math.max(this.maxX, building.maxX);
		building.maxY = Math.max(this.maxY, building.maxY);
		building.maxZ = Math.max(this.maxZ, building.maxZ);
	}
	
	public void set (int x, int y, int z, Unity unity) {
		
		// Redimention de l'axe x
		if (this.blocks.size() <= x) {
			for (int i = this.blocks.size(); i <= x; i++) {
				this.blocks.add(new ArrayList<ArrayList<Unity>> ());
			}
			maxX = this.blocks.size();
		}
		
		// Redimention de l'axe y
		if (this.blocks.get(x).size() <= y) {
			for (int i = this.blocks.get(x).size(); i <= y; i++) {
				this.blocks.get(x).add(new ArrayList<Unity> ());
			}
			maxY = Math.max (maxY, this.blocks.get(x).size());
		}
		
		// Redimention de l'axe z
		if (this.blocks.get(x).get(y).size() <= z) {
			for (int i = this.blocks.get(x).get(y).size(); i <= z; i++) {
				this.blocks.get(x).get(y).add(null);
			}
			maxZ = Math.max (maxZ, this.blocks.get(x).get(y).size());
		}
		
		this.blocks.get(x).get(y).set(z, unity);
	}
	
	public Unity get (int x, int y, int z) {
		try {
			return this.blocks.get(x).get(y).get(z);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Unity get (int x, int y, int z, int rotate) {
		try {
			if (rotate == this.ROTATED_90 || rotate == this.ROTATED_270) {
				return this.get(z, y, this.maxX(rotate) - x - 1);
			} else {
				return this.get(x, y, this.maxZ(rotate) - z - 1);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean isEraseBlock (int x, int y, int z, int rotate) {
		return this.get(x, y, z, rotate) != null;
	}
	
	/**
	 * Ajoute un groups de block aléatoire
	 * @param listGroupRandomBlocks
	 */
	public void addRandomBlock(ArrayList<Building> listGroupRandomBlocks) {
		this.groupsRandomBlocks.add (listGroupRandomBlocks);
		
	}
	
	/**
	 * Renvoie la liste des groupes
	 * @return
	 */
	public ArrayList<ArrayList<Building>> getRandomBlocksGroup () {
		return this.groupsRandomBlocks;
	}
	
}
