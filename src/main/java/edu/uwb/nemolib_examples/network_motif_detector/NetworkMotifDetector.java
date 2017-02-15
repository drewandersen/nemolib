package edu.uwb.nemolib_examples.network_motif_detector;

import edu.uwb.nemolib.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NetworkMotifDetector {

	public static void main (String[] args) {

		if (args.length < 3) {
			System.err.println("usage: NetworkMotifDetector path_to_data " +
					"motif_size, random_graph_count");
			System.exit(1);
		}

		String filename = args[0];
		System.out.println("filename = " + args[0]);
		int motifSize = Integer.parseInt(args[1]);
		int randGraphCount = Integer.parseInt(args[2]);

		if (motifSize < 3) {
			System.err.println("Motif getSize must be 3 or larger");
			System.exit(-1);
		}

		// Hard-code probs for now. This vector will take about ~10% sample
		List<Double> probs = new LinkedList<>();
		for (int i = 0; i < motifSize - 2; i++)
		{
			probs.add(1.0);
		}
		probs.add(1.0);
		probs.add(0.1);

		// parse input graph
		System.out.println("Parsing target graph...");
		Graph targetGraph = null;
		try {
			targetGraph = GraphParser.parse(filename);
		} catch (IOException e) {
			System.err.println("Could not process " + filename);
			System.err.println(e);
			System.exit(-1);
		}

		SubgraphEnumerationResult subgraphCount = new SubgraphCount();
		SubgraphEnumerator targetGraphESU = new ESU();
		TargetGraphAnalyzer targetGraphAnalyzer =
				new TargetGraphAnalyzer(targetGraphESU, subgraphCount);
		Map<String, Double> targetLabelToRelativeFrequency =
				targetGraphAnalyzer.analyze(targetGraph, motifSize);

		SubgraphEnumerator randESU = new RandESU(probs);
		RandomGraphAnalyzer randomGraphAnalyzer =
				new RandomGraphAnalyzer(randESU, randGraphCount);
		Map<String, List<Double>> randomLabelToRelativeFrequencies =
				randomGraphAnalyzer.analyze(targetGraph, motifSize);

		RelativeFrequencyAnalyzer relativeFrequencyAnalyzer =
				new RelativeFrequencyAnalyzer(randomLabelToRelativeFrequencies,
						                      targetLabelToRelativeFrequency);
		System.out.println(relativeFrequencyAnalyzer);

		System.out.println("Compete");
	}
}

