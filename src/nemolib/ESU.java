package nemolib;

import java.util.ArrayList;
import java.util.List;

/**
 * ESU is a static class used for executing the Enumerate Subgraphs algorithm
 * on a network graph.
 */
public class ESU
{
	// prevent default constructor from being called
	private ESU() {
		throw new AssertionError();
	}

	/**
	 * Enumerates Subgraphs using the ESU algorithm. Requires user to specify
	 * return type(s) and provide the accompanying data structures.
	 *
	 * @param graph the graph on which to execute ESU
	 * @param subgraphs the SubgraphEnumerationResult into which to enumerated
	 *                  Subgraphs will be stored.
	 * @param subgraphSize the size of the target Subgraphs
	 */
	public static void enumerate(Graph graph,
	                      SubgraphEnumerationResult subgraphs,
	                      int subgraphSize) {
		for (int i = 0; i < graph.size(); i++) {
			enumerate(graph, subgraphs, subgraphSize, i);
		}
	}

	/**
	 * Enumerates Subgraphs for one branch of the ESU tree starting at the
	 * given node. Allows for more control over the order the order of 
	 * execution, but does not perform a full enumeration.
	 *
	 * @param graph the graph on which to execute ESU
	 * @param subgraphs the data structure to which results are written
	 * @param subgraphSize the target subgraph size to enumerate
	 * @param vertex the graph vertex at which to execute
	 */
	public static void enumerate(Graph graph, SubgraphEnumerationResult subgraphs,
	                      int subgraphSize, int vertex) {
		List<Double> probs = new ArrayList<>();
		for (int i = 0; i < subgraphSize; ++i)
		{
			probs.add(1.0);
		}
		RandESU.enumerate(graph, subgraphs, subgraphSize, probs, vertex);
	}
}
