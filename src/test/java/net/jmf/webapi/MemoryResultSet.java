package net.jmf.webapi;

import java.util.List;

import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;

public class MemoryResultSet extends ResultCollector {
	private static final long serialVersionUID = 1820566879373727240L;
	private List<SampleData> times;
	
    public MemoryResultSet(Summariser summer, List<SampleData> times) {
        super(summer);
        this.times = times;
    }

    @Override
    public void sampleOccurred(SampleEvent e) {
        super.sampleOccurred(e);
        SampleResult r = e.getResult();
        if (r.isSuccessful()) {
            times.add(new SampleData(r.getEndTime(), r.getTime()));
        }
    }
}