package com.gollum.core.utils.math;



public class Integer2d implements Cloneable, Comparable {
	
	public int x = 0;
	public int y = 0;

	public Integer2d () {
	}
	
	public Integer2d (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Integer2d) {
			return
				this.x == ((Integer2d)o).x && 
				this.y == ((Integer2d)o).y
			;
		}
		return false;
	}
	
	@Override
	public Object clone () {
		return new Integer2d(this.x, this.y);
	}
	
	@Override
	public int compareTo(Object o) {
		
		Integer2d int2d = (Integer2d)o;
		
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
