package nemolib;

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
	 * @param currentSubgraph a Subgraph to add to this
	 * SubgraphEnumerationResult
	 */
	void add(Subgraph currentSubgraph);

	/**
	 * Merges g6 labels into SubgraphProfile labels. Highly recommended to use 
	 * the Labeler class to accomplish this.
	 * @param labelGPath the absolute path to the labelg binar
	 */
	void label(String labelGPath);

	/**
	 * Calculates and returns a map of relative frequencies or concentrations
	 * of labels in this subgraph.
	 * @return The map of relative frequencies
	 */
	Map<String, Double> getRelativeFrequencies();
}
