package mods.gollum.core.common.building;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.minecraft.block.Block;
import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.resource.ResourceLoader;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;
import argo.saj.InvalidSyntaxException;

public class FieldReobfKey {
	
	private static final String PATH_REOBF_JSON = "reobf/index.json";
	
	private static HashMap<String, String> reobfArray;
	
	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	
	public Object getTarget (String key) throws Exception {

		Class classEl;
		Object obj = null;
		String[] explode   = key.split(Pattern.quote("|"));
		
		try {
			
			classEl = Class.forName(explode[0]);
			Field f = classEl.getDeclaredField(explode[1]);
			obj = f.get(null);
			
		} catch (Exception e) {
			explode = this.reobfKey (key).split(Pattern.quote("|"));
			classEl = Class.forName(explode[0]);
			Field f = classEl.getDeclaredField(explode[1]);
			obj = f.get(null);
		}
		
		return obj;
	}
	
	/**
	 * Renvoie la valeur offusquée de la class
	 * @param stringValue
	 * @return
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
	 */
	private HashMap<String, String> getReobfArray () throws Exception {
		
		if (reobfArray == null) {
			reobfArray = new HashMap<String, String> ();
			
			InputStream isJson = this.resourceLoader.asset(PATH_REOBF_JSON, ModGollumCoreLib.MODID);
			JsonRootNode json  = this.parser.parse(new InputStreamReader(isJson));
			isJson.close();
			
			Map<JsonStringNode, JsonNode> map = json.getFields();
			for (JsonStringNode key : map.keySet()) {
				reobfArray.put(key.getText(), map.get(key).getText());
			}
			
		}
		
		return reobfArray;
	}

	public String humanQualifiedName(String block) throws Exception {
		
		for (Entry<String, String> entry : this.getReobfArray().entrySet()) {
			String human = entry.getKey();
			String reobf = entry.getValue();
			if (reobf.equals(block)) {
				block = human;
				break;
			}
		}
				
		return block;
	}
}
