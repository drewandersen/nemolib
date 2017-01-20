package edu.uwb.nemolib;

import java.util.Map;

/**
 * Objects implementing SubgraphEnumerationResult are used by
 * SubgraphEnumerators to store the results of enumeration. For instance, a
 * SubgraphCount enumeration object tracks the frequency of each g6 label type
 *
 */
public interface SubgraphEnumerationResult
{
	/**
	 * Adds a Subgraph to this SubgraphEnumerationResult
	 * @param currentSubgraph a Subgraph to addSubgraph to this
	 * SubgraphEnumerationResult
	 */
	void addSubgraph(Subgraph currentSubgraph);

	/**
	 * Merges g6 labels into SubgraphProfile labels. Recommended to use the
	 * Labeler class to accomplish this.
	 */
	void label();

	/**
	 * Calculates and returns a map of relative frequencies or concentrations
	 * of labels in this subgraph.
	 * @return The map of relative frequencies
	 */
	Map<String, Double> getRelativeFrequencies();
}