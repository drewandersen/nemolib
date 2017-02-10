package edu.uwb.nemolib;

import java.util.Map;

/**
 * Created by user on 1/20/17.
 */
public class NemoProfileBuilder {

	// prevent default constructor from being called
	private NemoProfileBuilder() {throw new AssertionError();}

	public static SubgraphProfile build(SubgraphProfile sp,
	                  RelativeFrequencyAnalyzer sa,
	                  double pThresh) {
		SubgraphProfile result = new SubgraphProfile();
		Map<String, Double> pValues = sa.getPValues();
		for (Map.Entry<String, Double> labelPValue : pValues.entrySet()) {
			if (labelPValue.getValue() <= pThresh) {
				System.out.println(labelPValue.getKey());
				result.addFrequencies(labelPValue.getKey(),
						sp.getFrequencies(labelPValue.getKey()));
			}
		}
		return result;
	}
}
