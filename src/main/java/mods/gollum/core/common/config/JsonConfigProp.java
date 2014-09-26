package mods.gollum.core.common.config;

import java.lang.annotation.Annotation;

import mods.gollum.core.tools.simplejson.IJsonComplement;

public class JsonConfigProp implements ConfigProp, IJsonComplement {

	private String   name              = "";
	private String   info              = "info";
	private String   group             = "";
	private String[] validValues       = new String[0];
	private String   minValue          = "";
	private String   maxValue          = "";
	private boolean  isListLengthFixed = false;
	private String   maxListLength     = "";
	private boolean  mcRestart         = false;
	private boolean  worldRestart      = false;
	private String   pattern           = "";
	private Type     type              = Type.DEFAULT;

	@Override public String   name()              { return this.name; };
	@Override public String   info()              { return this.info; };
	@Override public String   group()             { return this.group; };
	@Override public String[] validValues()       { return this.validValues; };
	@Override public String   minValue()          { return this.minValue; };
	@Override public String   maxValue()          { return this.maxValue; };
	@Override public boolean  isListLengthFixed() { return this.isListLengthFixed; };
	@Override public String   maxListLength()     { return this.maxListLength; };
	@Override public boolean  mcRestart()         { return this.mcRestart; };
	@Override public boolean  worldRestart()      { return this.worldRestart; };
	@Override public String   pattern()           { return this.pattern; };
	@Override public Type     type()              { return this.type; }

	public JsonConfigProp name              (String   name             ) { this.name              = name             ; return this; }
	public JsonConfigProp info              (String   info             ) { this.info              = info             ; return this; }
	public JsonConfigProp group             (String   group            ) { this.group             = group            ; return this; }
	public JsonConfigProp validValues       (String[] validValues      ) { this.validValues       = validValues      ; return this; }
	public JsonConfigProp minValue          (String   minValue         ) { this.minValue          = minValue         ; return this; }
	public JsonConfigProp maxValue          (String   maxValue         ) { this.maxValue          = maxValue         ; return this; }
	public JsonConfigProp isListLengthFixed (boolean  isListLengthFixed) { this.isListLengthFixed = isListLengthFixed; return this; }
	public JsonConfigProp maxListLength     (String   maxListLength    ) { this.maxListLength     = maxListLength    ; return this; }
	public JsonConfigProp mcRestart         (boolean  mcRestart        ) { this.mcRestart         = mcRestart        ; return this; }
	public JsonConfigProp worldRestart      (boolean  worldRestart     ) { this.worldRestart      = worldRestart     ; return this; }
	public JsonConfigProp pattern           (String   pattern          ) { this.pattern           = pattern          ; return this; }
	public JsonConfigProp type              (Type     type             ) { this.type              = type             ; return this; }
	
	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}
}
