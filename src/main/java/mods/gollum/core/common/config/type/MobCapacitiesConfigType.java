package mods.gollum.core.common.config.type;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.Json.EntryObject;
import argo.jdom.JsonNodeFactories;

public class MobCapacitiesConfigType extends ConfigJsonType {
	
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
	public void readConfig(Json json) {
		
		if (json.containsKey("moveSpeed")     ) this.moveSpeed      = json.child("moveSpeed")     .doubleValue();
		if (json.containsKey("maxHealt")      ) this.maxHealt       = json.child("maxHealt")      .doubleValue();
		if (json.containsKey("attackStrength")) this.attackStrength = json.child("attackStrength").doubleValue();
		if (json.containsKey("followRange")   ) this.followRange    = json.child("followRange")   .doubleValue();
		if (json.containsKey("timeRange")     ) this.timeRange      = json.child("timeRange")     .doubleValue();
		
	}

	@Override
	public Json writeConfig() {
		return Json.create(
			new EntryObject("moveSpeed"     , Json.create(this.moveSpeed)     ),
			new EntryObject("maxHealt"      , Json.create(this.maxHealt)      ),
			new EntryObject("attackStrength", Json.create(this.attackStrength)),
			new EntryObject("followRange"   , Json.create(this.followRange)   ),
			new EntryObject("timeRange"     , Json.create(this.timeRange)     )
		);
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
