package net.jmf.webapi;

public class ObjectStats {
	public ObjectStats(String v) {
		value = v;
		hash = v.hashCode();
		length = v.length();
	}
	
	public String value;
	public int hash;
	public int length;
}
