package edu.uwb.nemolib;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * RandomGraphAnalyzer is a facade class that generates and enumerates using
 * RAND-ESU a set of random network graphs based on the degree sequence vector
 * of a specified graph. The output of the analyze() method can be used by a
 * RelativeFrequencyAnalyzer object to determine whether a target graph contains any
 * network motifs.
 */
public final class RandomGraphAnalyzer {

	private SubgraphEnumerator enumerator;
	private int randomGraphCount;

	public RandomGraphAnalyzer(SubgraphEnumerator enumerator,
	                           int randomGraphCount) {
		this.enumerator = enumerator;
		this.randomGraphCount = randomGraphCount;

	}

	/**
	 * Generate and enumerate a set of random graphs.
	 * @param targetGraph the network graph from which to derive a degree
	 *                    sequence vector for generating random graphs
	 * @param subgraphSize the getSize of subgraph to enumerate
	 * @return mapping of labels to relative frequencies as found in the
	 * random graph pool
	 */
	public Map<String, List<Double>> analyze (Graph targetGraph, int subgraphSize) {

		// create the return map and fill it with the labels we found in the
		// target graph, as those are the only labels about which we care
		Map<String, List<Double>> labelToRelativeFrequencies = new HashMap<>();

		for(int i = 0; i < randomGraphCount; i++) {
			Graph randomGraph = RandomGraphGenerator.generate(targetGraph);

			// enumerate random graphs
			SubgraphCount subgraphCount = new SubgraphCount();
			enumerator.enumerate(randomGraph, subgraphSize, subgraphCount);
			subgraphCount.label();

			Map<String, Double> curLabelRelFreqMap =
					subgraphCount.getRelativeFrequencies();

			// populate labelToRelativeFrequencies with result
			for (Map.Entry<String, Double> curLabelRelFreqPair :
					curLabelRelFreqMap.entrySet()) {
				String curLabel = curLabelRelFreqPair.getKey();
				Double curFreq = curLabelRelFreqPair.getValue();

				if (!labelToRelativeFrequencies.containsKey(curLabel)) {
					labelToRelativeFrequencies.put(curLabel, new LinkedList<>());
				}
				labelToRelativeFrequencies.get(curLabel).add(curFreq);
			}
		}

		// fill in with zeros any List that is less than subgraph count to
		// ensure non-detection is accounted for.
		for (List<Double> freqs :
				labelToRelativeFrequencies.values()) {
			while (freqs.size() < randomGraphCount) {
				freqs.add(0.0);
			}
		}
		return labelToRelativeFrequencies;
	}
}