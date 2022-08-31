package com.gollum.core.common.building;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.GroupSubBuildings;
import com.gollum.core.common.building.Building.ListSubBuildings;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.building.Building.Unity.Content;
import com.gollum.core.common.resources.ResourceLoader;
import com.gollum.core.tools.registered.RegisteredObjects;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.property.PropertyFloat;

public class BuildingParser {
	
	private static final String NAME_IMG       = "structure.png";
	private static final String NAME_JSON      = "infos.json";
	private static TreeMap<String, Building> parsed = new TreeMap<String, Building>();
	
	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	private String modID;
	
	public static TreeMap<String, Building> getBuildingsList () {
		return parsed;
	}


	public static void reloadAll() {
		TreeMap<String, String> oldParsed = (TreeMap<String, String>) parsed.clone();
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
			String blockStr   = type.getStringValue ("block");
			String metadata   = "0"   ; try { metadata  = type.getNumberValue ("metadata"); } catch (Exception e) { }
			boolean after     = false ; try { after     = type.getBooleanValue ("after");   } catch (Exception e) { }
			JsonNode contents = null  ; try { contents  = type.getNode("contents");         } catch (Exception e) { }
			
			unity.state = null;
			Block block = RegisteredObjects.instance().getBlock(blockStr);
			if (block != null) {
				unity.state = this.getBlockState(block.getDefaultState(), type);
			}
			
			unity.after = after;
			
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
	
	private IBlockState getBlockState(IBlockState state, JsonNode json) {
		try {
			JsonNode stateNode = json.getNode("states");
			try {
				for (IProperty prop: state.getProperties().keySet()) {
					String name = prop.getName();
					if (prop instanceof PropertyBool) {
						Boolean value = null;
						try {
							value = stateNode.getBooleanValue(name);
						} catch (Exception e) {
						}
						if (value != null) {
							state = state.withProperty(prop, value);
						}
					} else
					if (prop instanceof PropertyInteger) {
						Integer value = null;
						try {
							value = Integer.parseInt(stateNode.getNumberValue(name));
						} catch (Exception e) {
						}
						if (value != null) {
							state = state.withProperty(prop, value);
						}
					} else
					if (prop instanceof PropertyFloat) {
						Float value = null;
						try {
							value = Float.parseFloat(stateNode.getNumberValue(name));
						} catch (Exception e) {
						}
						if (value != null) {
							state = state.withProperty(prop, value);
						}
					} else
					if (prop instanceof PropertyEnum) {
						String value = null;
						try {
							value = stateNode.getStringValue(name);
						} catch (Exception e) {
						}
						if (value != null) {
							PropertyEnum propEnum = (PropertyEnum)prop;
							for (Object obj: propEnum.getAllowedValues()) {
								Enum enumValue = (Enum)obj;
								if (enumValue instanceof IStringSerializable) {
									if (((IStringSerializable)enumValue).getName().toLowerCase().equals(value.toLowerCase())) {
										state = state.withProperty(prop, enumValue);
										break;
									}
								} else
								if (enumValue.name().toLowerCase().equals(value.toLowerCase())) {
									state = state.withProperty(prop, enumValue);
									break;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
		return state;
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
