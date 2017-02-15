package edu.uwb.nemolib;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of a Subgraph Count structure, which stores the number of
 * subgraphs detected in a network separated by subgraph type. A SubgraphCount
 * object exists in one of two states: labeled and unlabeled. Certain 
 * operations can only be performed based on the Subgraph's labeled state.
 */
public class SubgraphCount implements SubgraphEnumerationResult
{

	// String key is label, integer is frequency of subgraph instances
	// in the Graph from which this object was created
	private Map<String, Integer> labelFreqMap;

	/**
	 * Construct an empty SubgraphCount.
	 */
	public SubgraphCount()
	{
		labelFreqMap = new HashMap<>();
	}

	// uses interface's JavaDoc comment
	@Override
	public void addSubgraph(Subgraph currentSubgraph)
	{
		String label = currentSubgraph.getByteString();
		int total = 0;
		if (labelFreqMap.containsKey(label)) {
			total = labelFreqMap.get(label);
		}
		total++;
		labelFreqMap.put(label, total);
	}

	// uses interface's JavaDoc comment
	@Override
	public void label()
	{
		// get the canonical labels, which should be ordered.
		Labeler labeler = new Labeler();
		Map<String, String> g6CanLabelMap = 
				labeler.getCanonicalLabels(labelFreqMap.keySet());

		Map<String, Integer> canLabelFreqMap = new HashMap<>();
		
		// merge labelFreqMap into canLabelFreqMap
		for (Map.Entry<String, Integer> g6LabelFreq: labelFreqMap.entrySet() )
		{
			String g6Label = g6LabelFreq.getKey();
			int freq = g6LabelFreq.getValue();
			String canLabel = g6CanLabelMap.get(g6Label);

			if (canLabelFreqMap.containsKey(canLabel)) {
				freq += canLabelFreqMap.get(canLabel);
			}

			canLabelFreqMap.put(canLabel, freq);
		}
		
		labelFreqMap = canLabelFreqMap;
		
	}

	@Override
	public Map<String, Double> getRelativeFrequencies() {
		double total = 0;
		for (Integer freq : labelFreqMap.values()) {
			total += (double) freq;
		}
		Map<String, Double> labelRelFreqMap = new HashMap<>();
		for (Map.Entry<String, Integer> labelFreq : labelFreqMap.entrySet()) {
			String label = labelFreq.getKey();
			int freq = labelFreq.getValue();
			double relFreq = (double) freq / total;
			labelRelFreqMap.put(label, relFreq);
		}
		return labelRelFreqMap;
	}

	// STUB
	@Override public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Label").append('\t').append("Frequency");
		sb.append(String.format("%n"));
		for(Map.Entry<String, Integer> labelFreq: labelFreqMap.entrySet()) {
			sb.append(labelFreq.getKey()).append('\t');
			sb.append(labelFreq.getValue());
			sb.append(String.format("%n"));
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		SubgraphCount other = (SubgraphCount) obj;
		return other.labelFreqMap.equals(this.labelFreqMap);
	}

}
