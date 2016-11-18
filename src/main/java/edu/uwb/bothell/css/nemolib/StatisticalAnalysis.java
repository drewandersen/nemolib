package edu.uwb.bothell.css.nemolib;

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
	private Map<String, List<Double>> randomGraphRelFreqs;
	int randGraphCount;

	// do not allow default constructor
	private StatisticalAnalysis() {
		throw new AssertionError();
	}

	public StatisticalAnalysis(Map<String, Double> targetGraphRelFreqsMap,
	                    Map<String, List<Double>> randomGraphRelFreqsMap,
	                    int randGraphCount) {
		this.targetGraphRelFreqs = targetGraphRelFreqsMap;
		this.randomGraphRelFreqs = randomGraphRelFreqsMap;
		this.randGraphCount = randGraphCount;
	}

	public Map<String, Double> getZScores() {
		Map<String, Double> zScores = new HashMap<>();
		for (String label : randomGraphRelFreqs.keySet()) {
			double randMean = calcRandMean(label);
			double randStdDev = calcRandStdDev(label, randMean);
			double targetGraphFreq = 0.0;
			if (targetGraphRelFreqs.containsKey(label)) {
				targetGraphFreq = targetGraphRelFreqs.get(label);
			}
			double zScore = 0.0;
			if (randStdDev != 0) {
				zScore = (targetGraphFreq - randMean) / randStdDev;
			}
			zScores.put(label, zScore);
		}
		return zScores;
	}

	public double getZScore(String label) {
		double randMean = calcRandMean(label);
		double randStdDev = calcRandStdDev(label, randMean);
		return getZScore(label, randMean, randStdDev);
	}

	public double getZScore(String label, double mean, double stdDev) {

		double targetGraphFreq = 0.0;
		if (targetGraphRelFreqs.containsKey(label)) {
			targetGraphFreq = targetGraphRelFreqs.get(label);
		}
		double zScore = 0.0;
		if (stdDev != 0) {
			zScore = (targetGraphFreq - mean) / stdDev;
		}
		return zScore;
	}

	private double calcRandStdDev(String label, double randMean) {
		double variance = 0.0;
		for (double randFreq : randomGraphRelFreqs.get(label)) {
			double distance = randFreq - randMean;
			double squaredDistance = Math.pow(distance, 2);
			variance += squaredDistance;
		}
		return Math.sqrt(variance / (randGraphCount - 1));
	}


	public Map<String, Double> getPValues()
	{
		Map<String, Double> pValues = new HashMap<>();
		for(String label : randomGraphRelFreqs.keySet())
		{
			double pValue = getPValue(label);
			pValues.put(label, pValue);
		}
		return pValues;
	}

	public double getPValue(String label) {

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
		double pValue = (double) prePValue / randFreqCount;
		return pValue;
	}

	private double calcRandMean(String label) {
		double total = 0.0;
		List<Double> relFreqs = randomGraphRelFreqs.get(label);
		if (relFreqs.isEmpty()) { return 0; }
		for (double randFreq : relFreqs ) {
			total += randFreq;
		}
		return total / (double) relFreqs.size();
	}

	@Override
	public String toString() {
		NumberFormat nf = new DecimalFormat("0.000");
		StringBuilder sb = new StringBuilder();
		sb.append("Label\tRelFreq\t\tMean\t\tStDev\t\tZ-Score\t\tP-Value");
		sb.append(String.format("%n"));
		for (String label : randomGraphRelFreqs.keySet()) {
			sb.append(label).append('\t');
			if (targetGraphRelFreqs.containsKey(label)) {
				double targetGraphRelFreqPerc =
						targetGraphRelFreqs.get(label) * 100.0;
				sb.append(nf.format(targetGraphRelFreqPerc));
			} else {
				sb.append(nf.format(0.0));
			}
			sb.append("%\t\t");
			double mean = calcRandMean(label);
			sb.append(nf.format(mean * 100)).append("%\t\t");
			double stDev = calcRandStdDev(label, mean);
			sb.append(nf.format(stDev)).append("\t\t");
			double zScore = getZScore(label, mean, stDev);
			sb.append(nf.format(zScore)).append("\t\t");
			double pValue = getPValue(label);
			sb.append(nf.format(pValue));
			sb.append(String.format("%n"));
		}
		return sb.toString();
	}
}
