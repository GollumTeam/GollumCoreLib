package mods.gollum.core.common.config.container;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.IConfigJsonClass;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;

public class MobCapacitiesConfigType implements IConfigJsonClass {
	
	public double moveSpeed      = 0D;
	public double maxHealt       = 0.D;
	public double attackStrength = 0.D;
	public double followRange    = 0.D;
	public double timeRange      = 0.D;
	
	public MobCapacitiesConfigType () {
	}
	
	public MobCapacitiesConfigType (double moveSpeed, double maxHealt, double attackStrength, double followRange) {
		this (moveSpeed, maxHealt, attackStrength, followRange, 0.D);
	}

	public MobCapacitiesConfigType (double moveSpeed, double maxHealt, double attackStrength, double followRange, double timeRange) {
		this.moveSpeed = moveSpeed;
		this.maxHealt = maxHealt;
		this.attackStrength = attackStrength;
		this.followRange = followRange;
		this.timeRange = timeRange;
	}
	
	@Override
	public void readConfig(JsonNode json)  throws Exception {

		try { this.moveSpeed      = Double.parseDouble(json.getNumberValue("moveSpeed"));      } catch (Exception e) { ModGollumCoreLib.log.severe("Read config : can't parse json on capacity moveSpeed"); }
		try { this.maxHealt       = Double.parseDouble(json.getNumberValue("maxHealt"));       } catch (Exception e) { ModGollumCoreLib.log.severe("Read config : can't parse json on capacity maxHealt"); }
		try { this.attackStrength = Double.parseDouble(json.getNumberValue("attackStrength")); } catch (Exception e) { ModGollumCoreLib.log.severe("Read config : can't parse json on capacity attackStrength"); }
		try { this.followRange    = Double.parseDouble(json.getNumberValue("followRange"));    } catch (Exception e) { ModGollumCoreLib.log.severe("Read config : can't parse json on capacity followRange"); }
		try { this.timeRange      = Double.parseDouble(json.getNumberValue("timeRange"));      } catch (Exception e) { ModGollumCoreLib.log.severe("Read config : can't parse json on capacity timeRange"); }
		
	}

	@Override
	public JsonRootNode writeConfig() {
		return JsonNodeFactories.object(
			JsonNodeFactories.field ("moveSpeed"     , JsonNodeFactories.number(new Double (this.moveSpeed).toString())),
			JsonNodeFactories.field ("maxHealt"      , JsonNodeFactories.number(new Double (this.maxHealt).toString())),
			JsonNodeFactories.field ("attackStrength", JsonNodeFactories.number(new Double (this.attackStrength).toString())),
			JsonNodeFactories.field ("followRange"   , JsonNodeFactories.number(new Double (this.followRange).toString())),
			JsonNodeFactories.field ("timeRange"     , JsonNodeFactories.number(new Double (this.timeRange).toString()))
		);
	}
	
	public boolean equals (MobCapacitiesConfigType obj) {
		return
			this.moveSpeed      == obj.moveSpeed &&
			this.maxHealt       == obj.maxHealt  &&
			this.attackStrength == obj.attackStrength &&
			this.followRange    == obj.followRange &&
			this.timeRange      == obj.timeRange
		;
	}
	
	/**
	 * @return Vitesse du mod
	 */
	public double getMoveSpeed () { return this.moveSpeed;}
	/**
	 * @return Point de vie du mod
	 */
	public double getHealt () { return this.maxHealt; }
	/**
	 * @return Point de vie du mod
	 */
	public double getAttackStrength () { return this.attackStrength; }
	/**
	 * @return Zone de detection du mod
	 */
	public double getFollowRange () { return this.followRange; }
	/**
	 * @return Vitesse de tir du mod
	 */
	public double getTimeRange() { return this.timeRange; }
}
