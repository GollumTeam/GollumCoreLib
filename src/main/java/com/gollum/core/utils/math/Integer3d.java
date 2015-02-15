package com.gollum.core.utils.math;

import com.gollum.core.common.building.Building.Unity3D;


public class Integer3d implements Cloneable, Comparable {
	
	public int x = 0;
	public int y = 0;
	public int z = 0;

	public Integer3d () {
	}
	
	public Integer3d (int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Integer3d) {
			return
				this.x == ((Integer3d)o).x && 
				this.y == ((Integer3d)o).y && 
				this.z == ((Integer3d)o).z
			;
		}
		return false;
	}

	@Override
	public Object clone () {
		return new Integer3d(this.x, this.y, this.z);
	}
	
	@Override
	public int compareTo(Object o) {
		
		Integer3d int3d = (Integer3d)o;
		
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
