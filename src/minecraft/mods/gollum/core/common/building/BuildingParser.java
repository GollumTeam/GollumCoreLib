package mods.gollum.core.common.building;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.Unity;
import mods.gollum.core.common.building.Building.Unity.Content;
import mods.gollum.core.common.resource.ResourceLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;

public class BuildingParser {
	
	private static final String NAME_IMG       = "structure.png";
	private static final String NAME_JSON      = "infos.json";
	private static final String PATH_REOBF_JSON      = "reobf/index.json";
	private static HashMap<String, String> reobfArray;
	private static HashMap<String, Building> parsed = new HashMap<String, Building>();
	
	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	private String modID;
	
	public static HashMap<String, Building> getBuildingsList () {
		return parsed;
	}
	
	/**
	 * Parse un dossier de construction et renvoie al construction
	 * @param name
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	public Building parse (String name, String modID) {
		
		if (this.parsed.containsKey(modID+":"+name)) {
			return this.parsed.get(modID+":"+name);
		}
		
		this.modID = modID;
		
		ModGollumCoreLib.log.info ("Parse '"+name+"' building in "+modID);
		Building building = new Building (name);
		
		// Liste de la correspondance couleur block
		Hashtable<Integer, Unity> corlorBlockIndex = new Hashtable ();
		
		try {
			
			InputStream is      = this.resourceLoader.asset (ModBuildingParser.DIR_BUILDING_ASSETS + name + "/" + NAME_IMG, this.modID);
			BufferedImage image = ImageIO.read(is);
			is.close();
			
			InputStream isJson  = this.resourceLoader.asset (ModBuildingParser.DIR_BUILDING_ASSETS + name + "/" + NAME_JSON, this.modID);
			JsonRootNode json   = this.parser.parse(new InputStreamReader(isJson));
			isJson.close();
			
			////////////////////////////////////
			//                                //
			// Hauteur par défaut du building //
			//                                //
			////////////////////////////////////
			
			try {
				building.height = Integer.parseInt(json.getNumberValue("height"));
			} catch (Exception e) {
			}

			ModGollumCoreLib.log.debug ("Color index  building '"+name+"' load...");
			
			//////////////////////////////////////////////////////////////
			//                                                          //
			// Création des clefs correspondance couleur_pixel => block //
			//                                                          //
			//////////////////////////////////////////////////////////////
			
			JsonNode colorLink = json.getNode ("color");
			for (int y = 0; y < image.getHeight(); y++) {

				int color = image.getRGB(0, y) & 0xFFFFFF;
				
				if (color == 0x000000 || color == 0xFFFFFF) {
					break;
				}
				
				JsonNode type = colorLink.getNode(y);
				
				///////////////////////////////////
				// Récupération de l'objet block //
				///////////////////////////////////
				
				Unity unity = this.parseBlockDescription (type);
				
				corlorBlockIndex.put(color, unity);
			}
			
			ModGollumCoreLib.log.info ("Color index  building '"+name+"' loaded");
			
			
			
			
			//////////////////////////////////////////
			//                                      //
			// Construction de la matrice de blocks //
			//                                     //
			//////////////////////////////////////////
			
			int slideNum = 0;
			
			int x = 0;
			int y = 0;
			int z = 0;
			
			int originXSlide = 1;
			
			// Parcours l'image pour créer la matrice de block
			while (originXSlide < image.getWidth()) {
				
				
				int xImage = originXSlide;
				for (int zImage = 0; zImage < image.getHeight(); zImage++) {
					
					for (xImage = originXSlide; xImage < image.getWidth(); xImage++) {
						
						int color = image.getRGB(xImage, zImage);
						boolean alpha = image.getTransparency() != Transparency.OPAQUE && ((color>>24) & 0xff) == 0x00;
						color = color & 0xFFFFFF;
						
						if (color == 0x000000 && !alpha) {
							break;
						}
						
						Unity unity = null;
						
						if (alpha) {
//							ModGollumCoreLib.log.debug("is Alpha color:", color, " xyz",x, y, z);
						} else {
//							ModGollumCoreLib.log.debug("is Opaque color:", color, " xyz",x, y, z);
							Unity unityPtr = null; try { unityPtr = (Unity)corlorBlockIndex.get(color); } catch (Exception e) {};
							unity = (unityPtr != null) ? (Unity)unityPtr.clone () : new Unity ();
						}
						building.set(x, y, z, unity);
						
						x++;
					}
					z++;
					x = 0;
				}
				originXSlide = xImage + 1;
				y++;
				z = 0;
			}
			
			try {
				Map<JsonStringNode, JsonNode> map = json.getNode ("sets").getFields();
				for (JsonStringNode key : map.keySet()) {
					String position3D[] = key.getText().split("x");
					x = Integer.parseInt(position3D[0]);
					y = Integer.parseInt(position3D[1]);
					z = Integer.parseInt(position3D[2]);
					
					Unity unity = this.parseBlockDescription(map.get(key));
					building.set(x, y, z, unity);
					
				}
			} catch (Exception e) {
			}
			
			try {
				
				for (JsonNode randomBlock: json.getArrayNode ("random")) {
					
					ArrayList<Building> listGroupRandomBlocks = new ArrayList();
					
					for (JsonNode group: randomBlock.getElements()) {
						
						Building randomBuilding = new Building();
							
						Map<JsonStringNode, JsonNode> map = group.getFields();
						for (JsonStringNode key : map.keySet()) {
							String position3D[] = key.getText().split("x");
							x = Integer.parseInt(position3D[0]);
							y = Integer.parseInt(position3D[1]);
							z = Integer.parseInt(position3D[2]);
							
							Unity unity = this.parseBlockDescription(map.get(key));
							randomBuilding.set(x, y, z, unity);
						}
						
						// La randomBuilding doit etre de la meme taille que building pour les transformations
						for (x = 0; x < building.maxX; x++) {
							for (y = 0; y < building.maxY; y++) {
								for (z = 0; z <building.maxZ; z++) {
									randomBuilding.set (x, y, z, randomBuilding.get(x, y, z));
								}
							}
						}
						
						listGroupRandomBlocks.add (randomBuilding);
						
					}
					building.addRandomBlock (listGroupRandomBlocks);
				}
				
			} catch (Exception e) {
			}
			
			
			//Renverse la matrice par X et par Z pour correspondre au position dans le monde
			building.reverseByX();
			building.reverseByZ();
			
			ModGollumCoreLib.log.info ("Matrice building '"+name+"' loaded");
			
			
		} catch (IOException e) {
			ModGollumCoreLib.log.severe ("Error to read resource in jar for building :'"+name+"'");
			return null;
		} catch (InvalidSyntaxException e) {
			ModGollumCoreLib.log.severe ("Invalid json in jar for building :'"+name+"'");
			return null;
		}
		
		this.parsed.put(modID+":"+name, building);
		
		return building;
	}
	
	/**
	 * Parse une description de block
	 * @param type
	 * @return
	 */
	private Unity parseBlockDescription(JsonNode type) {
		
		
		ModGollumCoreLib.log.debug ("parseBlockDescription");
		
		Unity unity = new Unity();
		try {
			// Découpe le type par ClassName|ObjetBlock ou ClassName|ObjetBlock:intMetadataOptional
			String blockStr    = type.getStringValue ("block");
			String[] explode   = blockStr.split(Pattern.quote("|"));
			String metadata    = "0"; try { metadata = type.getNumberValue ("metadata"); } catch (Exception e) { }
			String orientation = "none"; try { orientation = type.getStringValue ("orientation"); } catch (Exception e) { }
			JsonNode contents  = null; try { contents = type.getNode("contents"); } catch (Exception e) { }
			
			// Récupère l'attribut
			Class classBlock;
			Block block = null;
			try {
				
				ModGollumCoreLib.log.debug ("parseBlockDescription Add block : "+explode[0]);
				
				classBlock = Class.forName(explode[0]);
				Field f    = classBlock.getDeclaredField(explode[1]);
				block      = (Block) f.get(null);
				
				ModGollumCoreLib.log.debug ("parseBlockDescription Added : "+explode[0]);
				
			} catch (Exception e) {
				
				// Si le code est reofusqué
				try {
					
					ModGollumCoreLib.log.debug ("parseBlockDescription Test reofuscate  : "+explode[0]);
					
					explode    = this.reobfKey (blockStr).split(Pattern.quote("|"));
					classBlock = Class.forName(explode[0]);
					Field f    = classBlock.getDeclaredField(explode[1]);
					block      = (Block) f.get(null);
					
					ModGollumCoreLib.log.debug ("parseBlockDescription Added : "+explode[0]);
					
				} catch (Exception e2) {
					ModGollumCoreLib.log.severe ("parseBlockDescription Erreur load  : "+explode[0]);
				}
			}
			
			unity.block       = block;
			unity.metadata    = Integer.parseInt(metadata);
			
			if (orientation.equals("none"))              { unity.orientation = Unity.ORIENTATION_NONE;              } else 
			if (orientation.equals("up"))                { unity.orientation = Unity.ORIENTATION_UP;                } else 
			if (orientation.equals("down"))              { unity.orientation = Unity.ORIENTATION_DOWN;              } else 
			if (orientation.equals("left"))              { unity.orientation = Unity.ORIENTATION_LEFT;              } else 
			if (orientation.equals("right"))             { unity.orientation = Unity.ORIENTATION_RIGTH;             } else 
			if (orientation.equals("top_vertical"))      { unity.orientation = Unity.ORIENTATION_TOP_VERTICAL;      } else 
			if (orientation.equals("bottom_vertical"))   { unity.orientation = Unity.ORIENTATION_BOTTOM_VERTICAL;   } else 
			if (orientation.equals("top_horizontal"))    { unity.orientation = Unity.ORIENTATION_TOP_HORIZONTAL;    } else 
			if (orientation.equals("bottom_horizontal")) { unity.orientation = Unity.ORIENTATION_BOTTOM_HORIZONTAL; }
			
			if (contents != null) {
				unity.contents = new ArrayList();
				for (JsonNode group : contents.getElements()) {
					unity.contents.add (this.parseContents (group));
				}
						
			}
			
			unity.extra = new HashMap<String, String>();
			try { 
				Map<JsonStringNode, JsonNode> map = type.getNode("extra").getFields();
				for (JsonStringNode key : map.keySet()) {
					unity.extra.put(key.getText(), map.get(key).getText());
				}
			} catch (Exception e) {
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unity;
	}
	
	
	/**
	 * Renvoie la valeur offusquée de la class
	 * @param stringValue
	 * @return
	 * @deprecated
	 * TODO a supprimer
	 * @throws FileNotFoundException 
	 */
	private String reobfKey(String stringValue) throws Exception {
		HashMap<String, String> map = this.getReobfArray ();
		return map.get(stringValue);
	}
	
	/**
	 * Renvoie le tableau reobfArray
	 * @return
	 * @throws InvalidSyntaxException 
	 * @throws IOException 
	 * @deprecated
	 * TODO a supprimer
	 */
	private HashMap<String, String> getReobfArray () throws Exception {
		
		if (BuildingParser.reobfArray == null) {
			BuildingParser.reobfArray = new HashMap<String, String> ();
			
			InputStream isJson = this.resourceLoader.asset(PATH_REOBF_JSON, ModGollumCoreLib.MODID);
			JsonRootNode json  = this.parser.parse(new InputStreamReader(isJson));
			isJson.close();
			
			Map<JsonStringNode, JsonNode> map = json.getFields();
			for (JsonStringNode key : map.keySet()) {
				BuildingParser.reobfArray.put(key.getText(), map.get(key).getText());
			}
			
		}
		
		return BuildingParser.reobfArray;
	}

	/**
	 * Parse un contenu d'objet (Les chests par exemple)
	 * @param group
	 * @return
	 */
	private ArrayList<Content> parseContents(JsonNode group) {
		
		ArrayList<Content> contentsGroup = new ArrayList();
		
		for (JsonNode el: group.getElements()) {
			
			String[] explode   = el.getStringValue ("element").split(Pattern.quote("|"));
			
			try {
				
				// Récupère l'attribut
				Class classEl;
				int type = 0;
				Item item = null;
				Block block = null;
				try {
					
					classEl = Class.forName(explode[0]);
					Field f = classEl.getDeclaredField(explode[1]);
					Object o = f.get(null);
					if (o instanceof Item) {
						item = (Item)o;
						type = Content.TYPE_ITEM;
					}
					if (o instanceof Block) {
						block = (Block)o;
						type = Content.TYPE_BLOCK;
					}
					
				} catch (Exception e) {
					// Si le code est reofusqué
					explode = this.reobfKey (el.getStringValue ("element")).split(Pattern.quote("|"));
					classEl = Class.forName(explode[0]);
					Field f = classEl.getDeclaredField(explode[1]);
					Object o = f.get(null);
					if (o instanceof Item) {
						item = (Item)o;
						type = Content.TYPE_ITEM;
					}
					if (o instanceof Block) {
						block = (Block)o;
						type = Content.TYPE_BLOCK;
					}
				}
				
				
				Content content = new Content ();

				content.id       = (item != null) ? item.itemID : ((block != null) ? block.blockID : 0);
				content.type     = type;
				content.min      = 1;  try { content.min      = Integer.parseInt (el.getNumberValue ("min"));      } catch (Exception e) { }
				content.max      = 1;  try { content.max      = Integer.parseInt (el.getNumberValue ("max"));      } catch (Exception e) { }
				content.metadata = -1; try { content.metadata = Integer.parseInt (el.getNumberValue ("metadata")); } catch (Exception e) { }
				
				contentsGroup.add(content);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		return contentsGroup;
	}
	
	
}
