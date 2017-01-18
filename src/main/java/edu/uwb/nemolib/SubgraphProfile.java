package edu.uwb.nemolib;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The SubgraphProfile is an enumeration of each label, grouped by network
 * vertex id number.<p>
 * A SubgraphProfile is always in one of two states: unlabeled or labeled, 
 * indicating whether the 
 * state and moves to the labeled state once the label() method has been 
 * called. A SubgraphProfile cannot move from a labeled state back to an 
 * unlabeled state.
 */
public class SubgraphProfile implements SubgraphEnumerationResult, Serializable
{
	// The primary structure of a subgraph profile. Essentially a table to map 
	// labels(String) and nodes(Integer) to the frequency of subgraphs 
	// of type label that include the node.
	private Map<String, Map<Integer, Integer>> labelVertexFreqMapMap;

	/**
	 * Construct a SubgraphProfile object
	 */
	public SubgraphProfile()
	{
		labelVertexFreqMapMap = new HashMap<>();
	}

	// uses interface JavaDoc comment
	@Override
	public void add(Subgraph currentSubgraph)
	{
		int[] vertices = currentSubgraph.getNodes();
		String label = currentSubgraph.getByteString();
		Map<Integer, Integer> nodeFreqMap =
				labelVertexFreqMapMap.get(label);
		if(nodeFreqMap == null) {
			nodeFreqMap = new HashMap<>();
		}
		for (int i = 0; i < vertices.length; i++) {
			int currentNode = currentSubgraph.get(i);
			int currentFreq = 0;
			if (nodeFreqMap.containsKey(currentNode)) {
				currentFreq = nodeFreqMap.get(currentNode);
			}
			currentFreq++;
			nodeFreqMap.put(currentNode, currentFreq);
		}
		labelVertexFreqMapMap.put(label, nodeFreqMap);
	}

	/*
	public Set<String> getLabels() {
		return labelVertexFreqMapMap.keySet();
	}
	*/

	// uses interface JavaDoc comment
	@Override
	public void label()
	{
		// get the canonical labels, which should be ordered.
		Labeler labeler = new Labeler();
		Map<String, String> g6CanLabelMap =
				labeler.getCanonicalLabels(labelVertexFreqMapMap.keySet());

		// make a replacement structure for the original map
		Map<String, Map<Integer, Integer>> canLabelVertexFreqMapMap =
				new HashMap<>();

		// merge labelFreqMap into canLabelFreqMap
		for (Map.Entry<String, Map<Integer, Integer>> labelVertexFreqMap:
				labelVertexFreqMapMap.entrySet() )
		{
			// must exist
			String canLabel = g6CanLabelMap.get(labelVertexFreqMap.getKey());

			Map<Integer, Integer> canVertexFreqMap =
					canLabelVertexFreqMapMap.get(canLabel);

			if (canVertexFreqMap == null){
				canVertexFreqMap = new HashMap<>();
			}

			for (Map.Entry<Integer, Integer> g6VertexFreq :
					labelVertexFreqMap.getValue().entrySet())
			{
				int vertex = g6VertexFreq.getKey();
				int total = g6VertexFreq.getValue();
				if (canVertexFreqMap.containsKey(vertex)) {
					total += canVertexFreqMap.get(vertex);
				}
				canVertexFreqMap.put(vertex, total);
			}
			canLabelVertexFreqMapMap.put(canLabel, canVertexFreqMap);
		}
		labelVertexFreqMapMap = canLabelVertexFreqMapMap;
	}

	/**
	 * Merge this SubgraphProfile with another SubgraphProfile
	 * @param other the other SubgraphProfile to merge with this 
	 * SubgraphProfile
	 */
	public void merge(SubgraphProfile other) {
		for (String otherLabel : other.labelVertexFreqMapMap.keySet()) {
			if (!this.labelVertexFreqMapMap.containsKey(otherLabel)) {
				this.labelVertexFreqMapMap.put(otherLabel,
						other.labelVertexFreqMapMap.get(otherLabel));
			} else {
				Map<Integer, Integer> otherLabelVertexFreqMap =
						other.labelVertexFreqMapMap.get(otherLabel);
				Map<Integer, Integer> thisLabelVertexFreqMap =
						this.labelVertexFreqMapMap.get(otherLabel);
				for (int vertex : otherLabelVertexFreqMap.keySet()) {
					if (!thisLabelVertexFreqMap.containsKey(vertex)) {
						thisLabelVertexFreqMap.put(vertex,
								otherLabelVertexFreqMap.get(vertex));
					} else {
						int total = thisLabelVertexFreqMap.get(vertex);
						total += otherLabelVertexFreqMap.get(vertex);
						thisLabelVertexFreqMap.put(vertex, total);
					}
				}
			}
		}
	}

	@Override
	public Map<String, Double> getRelativeFrequencies() {
		Map<String, Double> result = new HashMap<>();
		double totalSubgraphCount = (double) getTotalSubgraphCount();
		Set<String> labels = labelVertexFreqMapMap.keySet();
		for (String label : labels) {
			int total = 0;
			Map<Integer, Integer> vertexFreqMap =
					labelVertexFreqMapMap.get(label);
			for(Map.Entry<Integer, Integer> vertexFreq : vertexFreqMap.entrySet()) {
				total += vertexFreq.getValue();
			}
			double relFreq = (double) total / totalSubgraphCount;
			result.put(label, relFreq);
		}
		return result;
	}

	/**
	 * Returns a string representation of this SubgraphProfile, with rows
	 * representing vertex id numbers and columns representing subgraph labels.
	 * @return a string representation of this SubgraphProfile object.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newline = System.getProperty("line.separator");
		result.append("Node\tLabel").append(newline);
		result.append('\t');
		for (String label : labelVertexFreqMapMap.keySet()) {
			result.append(label).append('\t');
		}
		return result.toString();
	}

	private int getTotalSubgraphCount() {
		int total = 0;
		for (Map.Entry<String, Map<Integer, Integer>> labelVertexFreqMap :
				labelVertexFreqMapMap.entrySet()) {
			for (Map.Entry<Integer, Integer> vertexFreqMap :
					labelVertexFreqMap.getValue().entrySet()) {
				total += vertexFreqMap.getValue();
			}
		}
		return total;
	}
}
