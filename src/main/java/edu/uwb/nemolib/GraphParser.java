package edu.uwb.nemolib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The GraphParser class parses a text file into a Graph object. Each row of
 * input text file represents an edge in the graph. Each row should consist of
 * two integers separated by a single space, with each integer representing a
 * vertex. Vertices are created automatically based on the edge information.
 * Self edges and unconnected vertices are not allowed.
 */
public class GraphParser {

	// prevent instantiation of default constructor
	private GraphParser() {throw new AssertionError();}

	/**
	 * Parses a text file into a Graph object.
	 * @param filename the file containing the edge data
	 * @return a Graph object with the correct mapping
	 * @throws IOException if input file cannot be found
	 */
	public static Graph parse(String filename) throws IOException {
		Map<String, Integer> nameToIndex = new HashMap<>();
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

			// don't addSubgraph self edges
			if (fromIndex != toIndex) {
				output.getAdjacencyList(fromIndex).add(toIndex);
				output.getAdjacencyList(toIndex).add(fromIndex);
			}
		}
		return output;
	}
}
