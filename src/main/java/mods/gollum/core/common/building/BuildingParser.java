package mods.gollum.core.common.building;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.GroupSubBuildings;
import mods.gollum.core.common.building.Building.ListSubBuildings;
import mods.gollum.core.common.building.Building.SubBuilding;
import mods.gollum.core.common.building.Building.Unity;
import mods.gollum.core.common.building.Building.Unity.Content;
import mods.gollum.core.common.resource.ResourceLoader;
import mods.gollum.core.tools.registered.RegisteredObjects;
import net.minecraft.item.Item;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;

public class BuildingParser {
	
	private static final String NAME_IMG       = "structure.png";
	private static final String NAME_JSON      = "infos.json";
	private static HashMap<String, Building> parsed = new HashMap<String, Building>();
	
	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	private String modID;
	
	public static HashMap<String, Building> getBuildingsList () {
		return parsed;
	}


	public static void reloadAll() {
		HashMap<String, String> oldParsed = (HashMap<String, String>) parsed.clone();
		parsed.clear();
		BuildingParser parser = new BuildingParser();
		for (String modIDName : oldParsed.keySet()) {
			parser.parse (modIDName.substring(modIDName.indexOf(":")+1), modIDName.substring(0, modIDName.indexOf(":")));
		}
	}
	
	/**
	 * Parse un dossier de construction et renvoie al construction
	 * @param name
	 * @param string 
	 * @return
	 * @throws Exception
	 */
	public Building parse (String name, String modId) {
		
		if (this.parsed.containsKey(modId+":"+name)) {
			return this.parsed.get(modId+":"+name);
		}
		
		this.modID = modId;
		
		ModGollumCoreLib.log.info ("Parse '"+name+"' building in "+modId);
		Building building = new Building (name, modId);
		
		// Liste de la correspondance couleur block
		Hashtable<Integer, Unity> colorBlockIndex = new Hashtable ();
		
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
				
				colorBlockIndex.put(color, unity);
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
							building.setNull(x, y, z);
						} else {
//							ModGollumCoreLib.log.debug("is Opaque color:", color, " xyz",x, y, z);
							Unity unityPtr = null; try { unityPtr = (Unity)colorBlockIndex.get(color); } catch (Exception e) {};
							unity = (unityPtr != null) ? (Unity)unityPtr : new Unity ();
							building.set(x, y, z, unity);
						}
						
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
					building.set(x, y, building.maxZ() - z - 1, unity);
					
				}
				
			} catch (Exception e) {
			}
			
//			building.sort(); 
			
			try {
				Map<JsonStringNode, JsonNode> map = json.getNode ("buildings").getFields();
				for (JsonStringNode key : map.keySet()) {
					String position3D[] = key.getText().split("x");
					
					SubBuilding subBuilding = new SubBuilding();
					subBuilding.building = this.parse(map.get(key).getText(), modId);
					subBuilding.x = Integer.parseInt(position3D[0]);
					subBuilding.y = Integer.parseInt(position3D[1]);
					subBuilding.z = Integer.parseInt(position3D[2]);
					building.addBuilding (subBuilding);
					
				}
			} catch (Exception e) {
			}
			
			try {
				
				for (JsonNode randomBlock: json.getArrayNode ("random")) {
					
					GroupSubBuildings groupSubBuildings = new GroupSubBuildings();
					
					for (JsonNode group: randomBlock.getElements()) {
						
						SubBuilding subBuilding = new SubBuilding();
						
						Map<JsonStringNode, JsonNode> map = group.getFields();
						for (JsonStringNode key : map.keySet()) {
							String position3D[] = key.getText().split("x");
							x = Integer.parseInt(position3D[0]);
							y = Integer.parseInt(position3D[1]);
							z = Integer.parseInt(position3D[2]);
							
							Unity unity = this.parseBlockDescription(map.get(key));
							subBuilding.building.set(x, y, building.maxZ() - z - 1, unity);
//							subBuilding.building.sort();
							subBuilding.synMax (building);
						}
						
						groupSubBuildings.add (subBuilding);
						
					}
					building.addRandomBuildings (groupSubBuildings);
				}
				
			} catch (Exception e) {
			}
			
			try {
				
				for (JsonNode randomBlock: json.getArrayNode ("randomBuildings")) {
					
					GroupSubBuildings groupSubBuildings = new GroupSubBuildings();
					
					for (JsonNode group: randomBlock.getElements()) {
						
						ListSubBuildings listSubBuildings = new ListSubBuildings();
						
						Map<JsonStringNode, JsonNode> map = group.getFields();
						for (JsonStringNode key : map.keySet()) {
							String position3D[] = key.getText().split("x");
							
							SubBuilding subBuilding = new SubBuilding();
							
							subBuilding.x = Integer.parseInt(position3D[0]);
							subBuilding.y = Integer.parseInt(position3D[1]);
							subBuilding.z = Integer.parseInt(position3D[2]);
							subBuilding.building = this.parse(map.get(key).getText(), modId);
							
							listSubBuildings.add(subBuilding);
						}
						
						groupSubBuildings.add (listSubBuildings);
						
					}
					building.addRandomBuildings (groupSubBuildings);
				}
				
			} catch (Exception e) {
			}
			
			ModGollumCoreLib.log.info ("Matrice building '"+name+"' loaded");
			
			
		} catch (IOException e) {
			ModGollumCoreLib.log.severe ("Error to read resource in jar for building :'"+name+"'");
			return null;
		} catch (InvalidSyntaxException e) {
			ModGollumCoreLib.log.severe ("Invalid json in jar for building :'"+name+"'");
			return null;
		}
		
		this.parsed.put(modId+":"+name, building);
		
		return building;
	}
	
	/**
	 * Parse une description de block
	 * @param type
	 * @return
	 */
	private Unity parseBlockDescription(JsonNode type) {
		
		Unity unity = new Unity();
		try {
			String blockStr    = type.getStringValue ("block");
			String metadata    = "0"; try { metadata = type.getNumberValue ("metadata"); } catch (Exception e) { }
			String orientation = "none"; try { orientation = type.getStringValue ("orientation"); } catch (Exception e) { }
			JsonNode contents  = null; try { contents = type.getNode("contents"); } catch (Exception e) { }
			
			unity.block       = RegisteredObjects.instance().getBlock(blockStr);
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
	 * Parse un contenu d'objet (Les chests par exemple)
	 * @param group
	 * @return
	 */
	private ArrayList<Content> parseContents(JsonNode group) {
		
		ArrayList<Content> contentsGroup = new ArrayList();
		
		for (JsonNode el: group.getElements()) {
			
			try {
				
				// Récupère l'attribut
				Class classEl;
				int type = 0;
				Item item = RegisteredObjects.instance().getItem(el.getStringValue ("element"));
				
				Content content = new Content ();

				content.item     = item;
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
