package edu.uwb.nemolib;

import java.util.Map;

public class TargetGraphAnalyzer {

	SubgraphEnumerator subgraphEnumerator;
	SubgraphEnumerationResult subgraphEnumerationResult;

	public TargetGraphAnalyzer(SubgraphEnumerator subgraphEnumerator,
	                    SubgraphEnumerationResult subgraphEnumerationResult) {
		this.subgraphEnumerator = subgraphEnumerator;
		this.subgraphEnumerationResult = subgraphEnumerationResult;
	}

	public Map<String, Double> analyze(Graph graph, int subgraphSize) {
		subgraphEnumerator.enumerate(graph,
									 subgraphSize,
									 subgraphEnumerationResult);
		subgraphEnumerationResult.label();
		return subgraphEnumerationResult.getRelativeFrequencies();
	}
}