package net.jmf.webapi;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.Test;

public class PerfTest {
	private JMeterFromExistingJMX jmeter;
	
	@Before
	public void loadJmeterTestPlan(String home, String properties) {
		jmeter = new JMeterFromExistingJMX(home, properties);
	}
    
	@Test
	public void typicalLoad(String testPlan, int latency, int delta) throws Exception {
		System.out.print("testplan: " + testPlan);
		System.out.print(" | expected median latency: " + latency);
		System.out.println(" | delta: " + delta);
		List<Long> times = new ArrayList<Long>();
		jmeter.run(testPlan, times);
		assertNotEquals("There were no samples recorded.", times.size(), 0); 
		System.out.println("# of samples: " + times.size());
		double[] timeArray = new double[times.size()];
		int arrayIndex = 0;
		for (int listIndex = 0; listIndex < timeArray.length; ++listIndex) {
			if (times.get(listIndex) != null) {
				timeArray[arrayIndex] = times.get(listIndex);
				arrayIndex++;
			}
		}
		double[] latencyArray = timeArray;
		assertNotEquals("All the samples are null.", arrayIndex, 0);
		if (arrayIndex < timeArray.length) {
			System.out.println("# of successful samples: " + arrayIndex);
			latencyArray = new double[arrayIndex];
			System.arraycopy(timeArray, 0, latencyArray, 0, arrayIndex);
		}
		int avgLatency = (int) StatUtils.mean(latencyArray);
		int medianLatency = (int) StatUtils.percentile(latencyArray, 50.0);
		int stdDeviation = (int) Math.sqrt(StatUtils.variance(latencyArray));
		System.out.print("actual median latency: " + medianLatency);
		System.out.println(" | average latency: " + avgLatency);
		System.out.print("actual min: " + StatUtils.min(latencyArray));
		System.out.println(" | actual max: " + StatUtils.max(latencyArray));
		System.out.println("actual std deviation: " + stdDeviation);
		String message = "Actual median latency " + medianLatency + " > " + 
				(latency + delta) + " (Expected median latency + deviation)";
		assertTrue(message, medianLatency < (latency + delta));
	}
}