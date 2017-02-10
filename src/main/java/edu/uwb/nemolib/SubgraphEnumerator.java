package edu.uwb.nemolib;

public interface SubgraphEnumerator {
	public void enumerate(Graph targetGraph, int subgraphSize,
	                      SubgraphEnumerationResult subgraphs);
}