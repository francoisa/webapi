package net.jmf.webapi;

public class SampleData {
	private long endTime;
	private long respTime;
	
	public SampleData(Long endTime, Long respTime) {
		this.endTime = endTime;
		this.respTime = respTime;
	}
	
	public Long getEndTime() {
		return endTime;
	}
	
	public Long getRespTime() {
		return respTime;
	}
}
