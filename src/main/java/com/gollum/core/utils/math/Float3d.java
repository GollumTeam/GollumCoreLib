package com.gollum.core.utils.math;

import com.gollum.core.common.building.Building.Unity3D;


public class Float3d implements Cloneable, Comparable {
	
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public Float3d () {
	}
	
	public Float3d (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Float3d) {
			return
				this.x == ((Float3d)o).x && 
				this.y == ((Float3d)o).y && 
				this.z == ((Float3d)o).z
			;
		}
		return false;
	}

	@Override
	public Object clone () {
		return new Float3d(this.x, this.y, this.z);
	}
	
	@Override
	public int compareTo(Object o) {
		
		Float3d int3d = (Float3d)o;
		
		if (this.x < int3d.x) { return -1; }
		if (this.x > int3d.x) { return  1; }
		if (this.y < int3d.y) { return -1; }
		if (this.y > int3d.y) { return  1; }
		if (this.z < int3d.z) { return -1; }
		if (this.z > int3d.z) { return  1; }
		
		return 0;
	}
	
	public String toString () {
		return this.x + ", " + this.y + ", " + this.z;
	}
}
