package edu.uwb.nemolib;
import org.junit.*;
import static org.junit.Assert.*;

import java.util.*;

/**
 * Created by user on 1/19/17.
 */
public class StatisticalAnalysisTest {
	Map<String, List<Double>> labelRandRelFreqs;
	Map<String, Double> labelTargetRelFreqs;

	@Before
	public void setUp() {
		labelRandRelFreqs = constructLabelRandFreqMap();
		labelTargetRelFreqs = constructTargetRelFreqs();
	}

	@Test
	public void testContructor() {
		StatisticalAnalysis sa = new StatisticalAnalysis(labelRandRelFreqs,
				labelTargetRelFreqs);

		String result = sa.toString();
		String[] resultArray = result.split("[\t\n]");
		System.out.println(Arrays.toString(resultArray));
		assertEquals(resultArray[5], "55.000%");
		assertEquals(resultArray[6], "0.612");
		assertEquals(resultArray[7], "0.250");
		assertEquals(resultArray[9], "40.000%");
		assertEquals(resultArray[10], "6.124");
		assertEquals(resultArray[11], "0.000");
	}

	private Map<String, List<Double>> constructLabelRandFreqMap() {
		Map<String, List<Double>> labelRandFreqs = new HashMap<>();
		Double[] values1 = {0.4, 0.5, 0.6, 0.5};
		List<Double> freqs1 = Arrays.asList(values1);
		labelRandFreqs.put("l1", freqs1);
		Double[] values2 = {0.1, 0.15, 0.2, 0.15};
		List<Double> freqs2 = Arrays.asList(values2);
		labelRandFreqs.put("l2", freqs2);

		return labelRandFreqs;
	}

	private Map<String, Double> constructTargetRelFreqs() {
		Map<String, Double> result = new HashMap<>();
		result.put("l1", 0.55);
		result.put("l2", 0.4);
		return result;
	}
}