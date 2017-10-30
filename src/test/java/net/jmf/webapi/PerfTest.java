package net.jmf.webapi;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.StatUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PerfTest {
	private JMeterFromExistingJMX jmeter;
	private static String testPlan;
	private static int expectedMedianRespTime; 
	private static int delta;
	private static String home;
	private static String properties;

	@BeforeClass
	public static void initislize() {
		home = "src/test/resources/".replace('/', File.separatorChar);
		testPlan = home + "testPlan.jmx";
		properties = home + "jmeter.properties";
		System.setProperty("org.apache.logging.log4j.simplelog.StatusLogger.level", "INFO");
		System.setProperty("log4j.configuration", home + "log4j.xml");
		
	}
	
	@Before
	public void loadJmeterTestPlan() {
		jmeter = new JMeterFromExistingJMX(home, properties);
	}

	@Test
	public void typicalLoad() throws Exception {
		expectedMedianRespTime = 100;
		delta = 30;
		System.out.print("testplan: " + testPlan);
		System.out.print(" | expected median latency: " + expectedMedianRespTime);
		System.out.println(" | delta: " + delta);
		List<SampleData> sampleList = new LinkedList<>();
		jmeter.run(testPlan, sampleList);
		assertNotEquals("There were no samples recorded.", sampleList.size(), 0); 
		System.out.println("# of samples: " + sampleList.size());
		sampleList = sampleList
				.parallelStream()
				.filter(s -> (s.getEndTime() != null && s.getRespTime() != null))
				.collect(Collectors.toList());
		int i = 0;
		double[] actualRespTimeArray = new double[sampleList.size()];
		double[] actualEndTimeArray = new double[sampleList.size()];
		for (SampleData s : sampleList) {
			actualRespTimeArray[i] = s.getRespTime();
			actualEndTimeArray[i] = s.getRespTime();
			i++;
		}
		long avgRespTime = (long) StatUtils.mean(actualRespTimeArray);
		long actualMedianRespTime = (long) StatUtils.percentile(actualRespTimeArray, 50.0);
		long stdDeviation = (long) Math.sqrt(StatUtils.variance(actualRespTimeArray));
		String stats = "actual response time median: %d | average: %d | min: %.0f | max: %.0f ";
		System.out.println(String.format(stats, actualMedianRespTime, avgRespTime, StatUtils.min(actualRespTimeArray), 
				StatUtils.max(actualRespTimeArray)));
		System.out.println("actual std deviation: " + stdDeviation);
		double maxEndTime = StatUtils.max(actualEndTimeArray);
		double minEndTime = StatUtils.min(actualEndTimeArray);
		double throughPut = 1000 * actualEndTimeArray.length / (maxEndTime - minEndTime);
		System.out.println(String.format("throughPut: %.0f requests/sec", throughPut));
		String message = "Actual median latency " + actualMedianRespTime + " > " + 
				(expectedMedianRespTime + delta) + " (Expected median latency + deviation)";
		assertTrue(message, actualMedianRespTime < (expectedMedianRespTime + delta));
	}
}