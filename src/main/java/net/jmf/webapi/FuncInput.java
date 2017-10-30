package net.jmf.webapi;

public class FuncInput {
	private String function;
	private String value;
	
	public FuncInput() {
	}
	
	public FuncInput(String function, String value) {
		this.function = function;
		this.value = value;
	}
	
	public String getFunction() {
		return function;
	}
	
	public String getValue() {
		return value;
	}
}
