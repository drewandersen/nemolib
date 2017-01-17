package edu.uwb.nemolib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by user on 1/17/17.
 */
public class GraphParser {
	// parses a data file into an adjacency list representing the graph
	public static Graph parse(String filename) throws IOException {
		Map<String, Integer> nameToIndex = new HashMap<String, Integer>();
		Graph output = new Graph();
		// we read in all the data at once only so we can easily randomize it
		// with Collections.shuffle()
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		List<String> lines = new ArrayList<>();
		String currentLine = reader.readLine();
		while (currentLine != null) {
			lines.add(currentLine);
			currentLine = reader.readLine();
		}
		reader.close();

		// avoid clustering (data collection bias) by randomly parsing the
		// input lines of data
		Collections.shuffle(lines);

		String delimiters = "\\s+"; // one or more whitespace characters
		for (String line:lines) {
			String[] edge = line.split(delimiters);
			int fromIndex = output.getOrCreateIndex(edge[0], nameToIndex);
			int toIndex   = output.getOrCreateIndex(edge[1], nameToIndex);

			// don't add self edges
			if (fromIndex != toIndex) {
				output.getAdjacencyList(fromIndex).add(toIndex);
				output.getAdjacencyList(toIndex).add(fromIndex);
			}
		}
		return output;
	}

}
