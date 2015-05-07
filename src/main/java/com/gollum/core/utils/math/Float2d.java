package com.gollum.core.utils.math;



public class Float2d implements Cloneable, Comparable {
	
	public float x = 0.0F;
	public float y = 0.0F;

	public Float2d () {
	}
	
	public Float2d (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Float2d) {
			return
				this.x == ((Float2d)o).x && 
				this.y == ((Float2d)o).y
			;
		}
		return false;
	}
	
	@Override
	public Object clone () {
		return new Float2d(this.x, this.y);
	}
	
	@Override
	public int compareTo(Object o) {
		
		Float2d int2d = (Float2d)o;
		
		if (this.x < int2d.x) { return -1; }
		if (this.x > int2d.x) { return  1; }
		if (this.y < int2d.y) { return -1; }
		if (this.y > int2d.y) { return  1; }
		
		return 0;
	}
	
	public String toString () {
		return this.x + ", " + this.y;
	}
}
