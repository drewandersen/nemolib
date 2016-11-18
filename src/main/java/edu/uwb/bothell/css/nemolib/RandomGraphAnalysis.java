package edu.uwb.bothell.css.nemolib;

import java.util.*;

/**
 * Generates random graphs and analyzes their content to produce
 * meaningful data to be used by the StatisticalAnalysis class
 */
public final class RandomGraphAnalysis {

	// prevent default constructor from being instantiated
	private RandomGraphAnalysis() {
		throw new AssertionError();
	}

	/**
	 *
	 */
	public static Map<String, List<Double>> analyze (Set<String> labels,
			Graph targetGraph, int randomGraphCount, int subgraphSize,
			List<Double> probs) {

		// create the return map and fill it with the labels we found in the
		// target graph, as those are the only labels about which we care
		// TODO consider changing this, as it creates the precondition of 
		// executing the target graph analysis first
		Map<String, List<Double>> labelRelFreqsMap = new HashMap<>();
		for(String label : labels) {
			// LinkedList because we don't need random access
			labelRelFreqsMap.put(label, new LinkedList<>());
		}

		for(int i = 0; i < randomGraphCount; i++) {
			// display status for every 100th graph
			if (i % 100 == 99) {
				System.out.println("Analyzing random graph " + (i + 1) + "...");
			}
			// generate random graphs
			Graph randomGraph = RandomGraphGenerator.generate(targetGraph);

			// enumerate random graphs
			SubgraphCount subgraphCount = new SubgraphCount();
			RandESU.enumerate(randomGraph, subgraphCount, subgraphSize, probs);
			subgraphCount.label();

			Map<String, Double> curLabelRelFreqMap =
					subgraphCount.getRelativeFrequencies();

			// populate labelRelFreqsMap with result
			for (Map.Entry<String, List<Double>> labelRelFreqs :
					labelRelFreqsMap.entrySet()) {

				String label = labelRelFreqs.getKey();
				if (curLabelRelFreqMap.containsKey(label)) {
					labelRelFreqs.getValue().add(curLabelRelFreqMap.get(label));
				} else {
					labelRelFreqs.getValue().add(0.0);
				}
			}
		}
		return labelRelFreqsMap;
	}
}
