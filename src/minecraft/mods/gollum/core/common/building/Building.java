package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.block.Block;

public class Building implements Cloneable {


	public static final int ROTATED_0  = 0;
	public static final int ROTATED_90 = 1;
	public static final int ROTATED_180 = 2;
	public static final int ROTATED_270 = 3;
	public static final int ROTATED_360 = 4;
	
	public int maxX;
	public int maxY;
	public int maxZ;
	
	public int height = -1;
	public String name;
	
	/**
	 * Liste des block de la constuction
	 */
	private ArrayList<ArrayList<ArrayList<Unity>>> blocks = new ArrayList<ArrayList<ArrayList<Unity>>>();
	/**
	 * Liste des blocks posés aléatoirements
	 */
	private ArrayList<ArrayList<Building>> groupsRandomBlocks = new ArrayList<ArrayList<Building>>();
	
	/**
	 * Element chargé apres
	 */
	public Building after;
	
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
	
	public Building(String name) {
		this.name = name;
	}

	public Building() {
		this("");
	}

	/**
	 * Renvoie la matrice retourné de l'angle en parametre
	 * @param enumAngle
	 * @return
	 */
	public Building getRotatetedBuilding (int enumAngle) {
		if (enumAngle == Building.ROTATED_90) {
			Building rotatedBuilding = new Building (this.name);
			
			for (int x = 0; x < this.maxX; x++) {
				for (int y = 0; y < this.maxY; y++) {
					for (int z = 0; z < this.maxZ; z++) {
						rotatedBuilding.set (z, y, x, this.get(x, y, z));
					}
				}
			}
			
			rotatedBuilding.reverseByX(false);
			
			for (ArrayList<Building> groupBlock: this.groupsRandomBlocks) {
				ArrayList<Building> newGroupsRandomBlocks = new ArrayList<Building>();
				for (Building blocks: groupBlock) {
					newGroupsRandomBlocks.add (blocks.getRotatetedBuilding (enumAngle));
				}
				rotatedBuilding.addRandomBlock(newGroupsRandomBlocks);
			}
			
			rotatedBuilding.height = this.height;
			
			return rotatedBuilding;
		}
		if (enumAngle == Building.ROTATED_180) {
			return this.getRotatetedBuilding(Building.ROTATED_90).getRotatetedBuilding(Building.ROTATED_90);
		}
		if (enumAngle == Building.ROTATED_270) {
			return this.getRotatetedBuilding(Building.ROTATED_180).getRotatetedBuilding(Building.ROTATED_90);
		}
		
		return (Building)this.clone ();
	}
	
	/**
	 * Clone l'objet
	 */
	public Object clone() {
		Building o = new Building (this.name);
		
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
		return o;
	}

	public Building reverseByX () { return this.reverseByX(true); }
	public Building reverseByY () { return this.reverseByY(true); }
	public Building reverseByZ () { return this.reverseByZ(true); }
	
	/**
	 * Renverse la matrice par X
	 */
	public Building reverseByX (boolean reverseRandom) {
		Collections.reverse(this.blocks);
		
		if (reverseRandom) {
			for (ArrayList<Building> groupBlock: this.groupsRandomBlocks) {
				for (Building blocks: groupBlock) {
					blocks.reverseByX ();
				}
			}
		}
		return this;
	}
	
	/**
	 * Renverse la matrice par Y
	 */
	public Building reverseByY (boolean reverseRandom) {
		for (ArrayList<ArrayList<Unity>> list: this.blocks) {
			Collections.reverse(list);
		}
		
		if (reverseRandom) {
			for (ArrayList<Building> groupBlock: this.groupsRandomBlocks) {
				for (Building blocks: groupBlock) {
					blocks.reverseByY ();
				}
			}
		}
		return this;
	}
	
	/**
	 * Renverse la matrice par Z
	 */
	public Building reverseByZ (boolean reverseRandom) {
		for (ArrayList<ArrayList<Unity>> list: this.blocks) {
			for (ArrayList<Unity> list2: list) {
				Collections.reverse(list2);
			}
		}
		
		if (reverseRandom) {
			for (ArrayList<Building> groupBlock: this.groupsRandomBlocks) {
				for (Building blocks: groupBlock) {
					blocks.reverseByZ ();
				}
			}
		}
		return this;
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
				this.blocks.get(x).get(y).add(new Unity ());
			}
			maxZ = Math.max (maxZ, this.blocks.get(x).get(y).size());
		}
		
		this.blocks.get(x).get(y).set(z, unity);
	}
	
	public Unity get (int x, int y, int z) {
		try {
			return this.blocks.get(x).get(y).get(z);
		} catch (Exception e) {
			return new Unity();
		}
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
