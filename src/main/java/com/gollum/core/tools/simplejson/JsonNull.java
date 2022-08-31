package com.gollum.core.tools.simplejson;

public class JsonNull extends Json {
	
	public boolean isNull()      { return true; }
	
	public void setValue(Object value) {
	}
	
	public Object  value ()      { return null;  }
}
