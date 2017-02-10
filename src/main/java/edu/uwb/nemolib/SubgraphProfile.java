package edu.uwb.nemolib;

import java.io.Serializable;
import java.util.*;

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
	private Map<String, Map<Integer, Integer>> labelToVertexToFrequency;

	/**
	 * Construct a SubgraphProfile object
	 */
	public SubgraphProfile()
	{
		labelToVertexToFrequency = new HashMap<>();
	}

	// uses interface JavaDoc comment
	@Override
	public void addSubgraph(Subgraph currentSubgraph)
	{
		int[] vertices = currentSubgraph.getNodes();
		String label = currentSubgraph.getByteString();
		Map<Integer, Integer> nodeToFrequency =
				labelToVertexToFrequency.get(label);
		if(nodeToFrequency == null) {
			nodeToFrequency = new HashMap<>();
		}
		for (int i = 0; i < vertices.length; i++) {
			int currentNode = currentSubgraph.get(i);
			int currentFreq = 0;
			if (nodeToFrequency.containsKey(currentNode)) {
				currentFreq = nodeToFrequency.get(currentNode);
			}
			currentFreq++;
			nodeToFrequency.put(currentNode, currentFreq);
		}
		labelToVertexToFrequency.put(label, nodeToFrequency);
	}

	public boolean addFrequencies(String label,
	                           Map<Integer, Integer> frequencies) {
		if (labelToVertexToFrequency.containsKey(label)) {
			return false;
		}
		labelToVertexToFrequency.put(label, frequencies);
		return true;
	}

	public Map<Integer, Integer> getFrequencies(String label) {
		return labelToVertexToFrequency.getOrDefault(label, null);
	}

	// uses interface JavaDoc comment
	@Override
	public void label()
	{
		// get the canonical labels, which should be ordered.
		Labeler labeler = new Labeler();
		Map<String, String> g6LabelToCanonicalLabel =
				labeler.getCanonicalLabels(labelToVertexToFrequency.keySet());

		// make a replacement structure for the original map
		Map<String, Map<Integer, Integer>> canLabelToVertexToFrequency =
				new HashMap<>();

		// merge labelFreqMap into canLabelFreqMap
		for (Map.Entry<String, Map<Integer, Integer>> g6VertexToFrequency:
				labelToVertexToFrequency.entrySet() )
		{
			// must exist
			String canLabel =
					g6LabelToCanonicalLabel.get(g6VertexToFrequency.getKey());

			Map<Integer, Integer> vertexToFrequency =
					canLabelToVertexToFrequency.get(canLabel);

			if (vertexToFrequency == null){
				vertexToFrequency = new HashMap<>();
			}

			for (Map.Entry<Integer, Integer> g6VertexFreq :
					g6VertexToFrequency.getValue().entrySet())
			{
				int vertex = g6VertexFreq.getKey();
				int total = g6VertexFreq.getValue();
				if (vertexToFrequency.containsKey(vertex)) {
					total += vertexToFrequency.get(vertex);
				}
				vertexToFrequency.put(vertex, total);
			}
			canLabelToVertexToFrequency.put(canLabel, vertexToFrequency);
		}
		labelToVertexToFrequency = canLabelToVertexToFrequency;
	}

	/**
	 * Merge this SubgraphProfile with another SubgraphProfile
	 * @param other the other SubgraphProfile to merge with this 
	 * SubgraphProfile
	 */
	public void merge(SubgraphProfile other) {
		for (String otherLabel : other.labelToVertexToFrequency.keySet()) {
			if (!this.labelToVertexToFrequency.containsKey(otherLabel)) {
				this.labelToVertexToFrequency.put(otherLabel,
						other.labelToVertexToFrequency.get(otherLabel));
			} else {
				Map<Integer, Integer> otherLabelVertexFreqMap =
						other.labelToVertexToFrequency.get(otherLabel);
				Map<Integer, Integer> thisLabelVertexFreqMap =
						this.labelToVertexToFrequency.get(otherLabel);
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
		Set<String> labels = labelToVertexToFrequency.keySet();
		for (String label : labels) {
			int total = 0;
			Map<Integer, Integer> vertexFreqMap =
					labelToVertexToFrequency.get(label);
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

		for (Map.Entry<String, Map<Integer, Integer>> labelFreqs :
				labelToVertexToFrequency.entrySet()) {
			result.append(labelFreqs.getKey());
			result.append(newline);
			for (Map.Entry<Integer, Integer> nodeFreqs :
					labelFreqs.getValue().entrySet()) {
				result.append("[").append(nodeFreqs.getKey()).append(",")
						.append(nodeFreqs.getValue()).append("]");
			}
			result.append(newline);
		}
		return result.toString();
	}

	// Returns the total number of subgraphs in this SubgraphProfile
	private int getTotalSubgraphCount() {
		int total = 0;
		for (Map.Entry<String, Map<Integer, Integer>> labelVertexFreqMap :
				labelToVertexToFrequency.entrySet()) {
			for (Map.Entry<Integer, Integer> vertexFreqMap :
					labelVertexFreqMap.getValue().entrySet()) {
				total += vertexFreqMap.getValue();
			}
		}
		return total;
	}
}
