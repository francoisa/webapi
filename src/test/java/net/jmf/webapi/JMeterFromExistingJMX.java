package net.jmf.webapi;

import java.io.File;
import java.util.List;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

/*
 * Your jmeter testplan must have a ResultCollector with a TableVisualizer gui 
 * and save the data to a file.  JMeterFromExistingJMX will parse the test plan
 * for the result file and use the latencies to calculate an average.
 */
public class JMeterFromExistingJMX {
	private String jmeterHome;
	private String propFile;
	
	public JMeterFromExistingJMX(String jmeterHome, String propFile) {
		this.jmeterHome = jmeterHome;
		this.propFile = propFile;
	}
	
    public void run(String testPlan, List<SampleData> times) throws Exception {
        JMeterUtils.setJMeterHome(jmeterHome);
        // Initialize Properties, logging, locale, etc.
        JMeterUtils.loadJMeterProperties(propFile);

        JMeterUtils.initLocale();
        // Initialize JMeter SaveService
		SaveService.loadProperties();

        // Load existing .jmx Test Plan
        File in = new File(testPlan);;
        HashTree testPlanTree = null;
        testPlanTree = SaveService.loadTree(in);
        if (testPlanTree != null) {
            Summariser summer = null;
            String summariserName = JMeterUtils.getPropDefault("summariser.name", "summary");
            if (summariserName.length() > 0) {
                summer = new Summariser(summariserName);
            }
            MemoryResultSet memory =  new MemoryResultSet(summer, times);
            testPlanTree.add(testPlanTree.getArray()[0], memory);
            // Run JMeter Test
            // JMeter Engine
            StandardJMeterEngine jmeter = new StandardJMeterEngine();
            jmeter.configure(testPlanTree);
            jmeter.run();
        }
    }
}