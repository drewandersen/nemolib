package nemolib;

import java.util.*;

/**
 * RandomGraphAnalysis is a facade class that generates and enumerates using
 * RAND-ESU a set of random network graphs based on the degree sequence vector
 * of a specified graph. The output of the analyze() method can be used by a
 * StatisticalAnalysis object to determine whether a target graph contains any
 * network motifs.
 */
public final class RandomGraphAnalysis {

	// prevent default constructor from being instantiated
	private RandomGraphAnalysis() {
		throw new AssertionError();
	}

	/**
	 * Generate and enumerate a set of random graphs.
	 * @param targetGraph the network graph from which to derive a degree
	 *                    sequence vector for generating random graphs
	 * @param randomGraphCount the number of random graphs to use for analysis
	 * @param subgraphSize the size of subgraph to enumerate
	 * @param probs the probability vector to be used by the RAND-ESU algorithm
	 * @return mapping of labels to relative frequencies as found in the 
	 * random graph pool
	 */
	public static Map<String, List<Double>> analyze (Graph targetGraph,
	                                                 int randomGraphCount,
	                                                 int subgraphSize,
	                                                 List<Double> probs) {

		// create the return map and fill it with the labels we found in the
		// target graph, as those are the only labels about which we care
		// TODO consider changing this, as it creates the precondition of 
		// executing the target graph analysis first
		Map<String, List<Double>> labelRelFreqsMap = new HashMap<>();

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
			for (Map.Entry<String, Double> curLabelRelFreqPair :
					curLabelRelFreqMap.entrySet()) {
				String curLabel = curLabelRelFreqPair.getKey();
				Double curFreq = curLabelRelFreqPair.getValue();

				if (!labelRelFreqsMap.containsKey(curLabel)) {
					labelRelFreqsMap.put(curLabel, new LinkedList<Double>());
				}

				labelRelFreqsMap.get(curLabel).add(curFreq);

			}
		}

		// fill in with zeros any List that is less than subgraph count to
		// ensure non-detection is accounted for.
		for (List<Double> freqs :
				labelRelFreqsMap.values()) {
			while (freqs.size() < randomGraphCount) {
				freqs.add(0.0);
			}
		}

		return labelRelFreqsMap;
	}
}
