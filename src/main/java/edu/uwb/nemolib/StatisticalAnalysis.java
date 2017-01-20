package edu.uwb.nemolib;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Subsystem to store relative frequencies for each subgraph pattern / label and
 * calculate the p-values and z-scores for those labels.
 */
public final class StatisticalAnalysis {

	// labels mapped to a list containing relativeFrequencies
	private Map<String, Double> targetGraphRelFreqs;
	private Map<String, Double> zScores;
	private Map<String, Double> pValues;

	// do not allow default constructor
	private StatisticalAnalysis() {
		throw new AssertionError();
	}

	/**
	 * Constructor for a Statistical Analysis object.
	 * @param randGraphRelFreqs Labels paired with lists of relative
	 *                          frequencies of subgraph patterns found in
	 *                          a random graph pool
	 * @param targetGraphRelFreqs Labels paired with relative frequencies as
	 *                            found in the target network.
	 */
	public StatisticalAnalysis( Map<String, List<Double>> randGraphRelFreqs,
								Map<String, Double> targetGraphRelFreqs) {
		this.targetGraphRelFreqs = targetGraphRelFreqs;
		this.zScores = new HashMap<>();
		calculateZScores(randGraphRelFreqs, targetGraphRelFreqs);
		this.pValues = new HashMap<>();
		calculatePValues(randGraphRelFreqs, targetGraphRelFreqs);
	}

	/**
	 * Get the z-scores for this StatisticalAnalysis object.
	 * @return a map containing labels and corresponding z-scores
	 */
	public Map<String, Double> getZScores() {
		return zScores;
	}

	/**
	 * Get the z-score for a specified label for the data sets contained in
	 * this StatisticalAnalysis object
	 * @param label the label for which to get the z-score
	 * @return the z-score for the given label
	 */
	private double getZScore(String label) {
		return zScores.getOrDefault(label, 0.0);
	}

	/**
	 * Get the p-values for this StatisticalAnalysis object.
	 * @return a map containing labels and corresponding p-values
	 */
	public Map<String, Double> getPValues() {
		return pValues;
	}

	/**
	 * Get the p-value for a specified label for the data sets contained in
	 * this StatisticalAnalysis object
	 * @param label the label for which to get the z-score
	 * @return the z-score for the given label
	 */
	private double getPValue(String label) {
		return pValues.getOrDefault(label, 0.0);
	}

	// calculates z-scores for this Statistical Analysis object
	private void calculateZScores ( Map<String, List<Double>> randGraphRelFreqs,
									Map<String, Double> targetGraphRelFreqs) {
		for (Map.Entry<String, List<Double>> labelFreqPair :
				randGraphRelFreqs.entrySet()) {
			String label = labelFreqPair.getKey();
			List<Double> freqs = labelFreqPair.getValue();
			double randMean = calcMean(freqs);
			double randStdDev = calcStdDev(randMean, freqs);
			double targetGraphFreq =
					targetGraphRelFreqs.getOrDefault(label, 0.0);
			double zScore = 0.0;
			if (randStdDev != 0) {
				zScore = (targetGraphFreq - randMean) / randStdDev;
			}
			zScores.put(label, zScore);
		}
	}

	// calculates the standard deviation for a random
	private double calcStdDev(double randMean, List<Double> values) {
		double variance = 0.0;
		for (double value : values) {
			double distance = value - randMean;
			double squaredDistance = Math.pow(distance, 2);
			variance += squaredDistance;
		}
		return Math.sqrt(variance / (values.size() - 1));
	}

	// calculates the p-values for this object
	private void calculatePValues(Map<String, List<Double>> randGraphRelFreqs,
	                              Map<String, Double> targetGraphRelFreqs) {
		for(String label : randGraphRelFreqs.keySet())
		{
			double pValue = calcPValue(label, randGraphRelFreqs,
					targetGraphRelFreqs);
			pValues.put(label, pValue);
		}
	}

	// calculates a p-value for an specified label
	private double calcPValue(String label,
	                          Map<String, List<Double>> randomGraphRelFreqs,
	                          Map<String, Double> targetGraphRelFreqs) {
		// if a label appears in the target graph that didn't show up in any
		// random graphs, clearly it's a network motif. This scenario shouldn't
		// happen for a reasonable number of random graphs
		if (!randomGraphRelFreqs.containsKey(label)) {
			return 0;
		}

		// This shouldn't happen with the current design, but in case the design
		// changes to include functionality to display all labels found in the
		// random graphs instead of just those in the target graph, this will
		// ensure those labels are not identified as network motifs
		if (!targetGraphRelFreqs.containsKey(label)) {
			return 1;
		}

		int prePValue = 0;
		List<Double> randFreqs = randomGraphRelFreqs.get(label);
		double targetFreq = targetGraphRelFreqs.get(label);
		for (double randFreq : randFreqs) {
			// if randFreq >= targetFreq
			if (randFreq >= targetFreq) {
				prePValue++;
			}
		}
		double randFreqCount = randFreqs.size();
		return (double) prePValue / randFreqCount;
	}

	//
	private double calcMean(List<Double> values) {
		double total = 0.0;
		for (double randFreq : values ) {
			total += randFreq;
		}
		return total / values.size();
	}

	@Override
	public String toString() {
		NumberFormat nf = new DecimalFormat("0.000");
		StringBuilder sb = new StringBuilder();
		sb.append("Label\tRelFreq\tZ-Score\tP-Value");
		sb.append(String.format("%n"));
		for (String label : zScores.keySet()) {
			sb.append(label).append('\t');
			if (targetGraphRelFreqs.containsKey(label)) {
				double targetGraphRelFreqPerc =
						targetGraphRelFreqs.get(label) * 100.0;
				sb.append(nf.format(targetGraphRelFreqPerc));
			} else {
				sb.append(nf.format(0.0));
			}
			sb.append("%\t");
			double zScore = getZScore(label);
			sb.append(nf.format(zScore)).append("\t");
			double pValue = getPValue(label);
			sb.append(nf.format(pValue));
			sb.append(String.format("%n"));
		}
		return sb.toString();
	}
}