package edu.uwb.nemolib;

import java.util.Map;

/**
 * Analyzes a target graph to determine relative frequency of subgraph patterns.
 */
public class TargetGraphAnalyzer {

	SubgraphEnumerator subgraphEnumerator;
	SubgraphEnumerationResult subgraphEnumerationResult;

	/**
	 * Default constructor for TargetGraphAnalyzer objects.
	 * @param subgraphEnumerator the algorithm to be used to enumerate
	 *                           subgraphs for a target graph.
	 * @param subgraphEnumerationResult the data structure to use when storing
	 */
	public TargetGraphAnalyzer(SubgraphEnumerator subgraphEnumerator,
	                    SubgraphEnumerationResult subgraphEnumerationResult) {
		this.subgraphEnumerator = subgraphEnumerator;
		this.subgraphEnumerationResult = subgraphEnumerationResult;
	}

	/**
	 * Analyze the target graph to enumerate all subgraphSize subgraphs and
	 * deposit the results into ths TargetGraphAnalyzer's subgraphEnumerator.
	 * @param graph the graph to analyze
	 * @param subgraphSize the size of the subgraphs to enumerate
	 * @return a mapping of subgraph labels to their respective relative
	 * frequencies in the target graph.
	 */
	public Map<String, Double> analyze(Graph graph, int subgraphSize) {
		subgraphEnumerator.enumerate(graph,
									 subgraphSize,
									 subgraphEnumerationResult);
		subgraphEnumerationResult.label();
		return subgraphEnumerationResult.getRelativeFrequencies();
	}
}