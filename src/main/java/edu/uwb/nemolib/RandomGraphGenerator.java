package edu.uwb.nemolib;

import java.util.*;

/**
  * Generates random graphs from an input graph based on the degree sequence of 
  * the original graph.
  */
final class RandomGraphGenerator {

	/**
	 * Private constructor to prevent instantiation
	 */
	private RandomGraphGenerator() { throw new AssertionError(); }

	/**
	  * Generates random Graphs from an input Graph based on the degree
	  * sequence of the original Graph.
	  * @param inputGraph the Graph from which to derive the random Graphs
	  * @return a random Graph of the same getSize and order as the original
	  */
	static Graph generate(Graph inputGraph)
	{
		List<Integer> degreeSequenceVector = getDegreeSequenceVector(inputGraph);

		Graph randomGraph = new Graph();
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		List<Integer> vertexList = new ArrayList<>();
		for (int vertex = 0; vertex < inputGraph.getSize(); ++vertex) {
			randomGraph.addVertex();
			for (int degree = 0; degree < degreeSequenceVector.get(vertex);
					++degree) {
				vertexList.add(vertex);
			}
		}
		Collections.shuffle(vertexList);
		
		// create edges
		int u, v;
		for (int c = 0; vertexList.size() > 1; ++c) {
			Random generator = new Random();
			u = generator.nextInt(vertexList.size());
			// make sure v does not equal u
			while((v = generator.nextInt(vertexList.size())) == u) {}
			// swap not factored because Java
			if ( u > v) {
				int temp = u;
				u = v;
				v = temp;
			}
			int edgeVertexV = vertexList.get(v);
			int edgeVertexU = vertexList.get(u);
			vertexList.remove(v);
			vertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
		return randomGraph;
	}

	static Graph generate(Graph inputGraph, List<Integer> probs)
	{
		List<Integer> degreeSequenceVector = getDegreeSequenceVector(inputGraph);

		Graph randomGraph = new Graph();
		// generate randomized list of vertices
		// the vertexList is a set where each node is represented by a number
		// of elements equal to that vertex's degree
		List<Integer> vertexList = new ArrayList<>();
		for (int vertex = 0; vertex < inputGraph.getSize(); ++vertex) {
			randomGraph.addVertex();
			for (int degree = 0; degree < degreeSequenceVector.get(vertex);
			     ++degree) {
				vertexList.add(vertex);
			}
		}
		Collections.shuffle(vertexList);

		// create edges
		int u, v;
		Iterator<Integer> probItr = probs.iterator();
		for (int c = 0; vertexList.size() > 1; c++) {
			u = probItr.next();
			System.out.println("c = " + c);
			System.out.println("u = " + u);

			// make sure v does not equal u
			while((v = probItr.next()) == u) {}
			System.out.println("v = " + v);
			// swap not factored because Java
			if ( u > v) {
				int temp = u;
				u = v;
				v = temp;
			}
			int edgeVertexV = vertexList.get(v);
			int edgeVertexU = vertexList.get(u);
			vertexList.remove(v);
			vertexList.remove(u);
			randomGraph.addEdge(edgeVertexU, edgeVertexV);
		}
		return randomGraph;
	}

	/**
	  * Generates a degree sequence vector for a given Graph
	  * @param inputGraph the Graph from which to derive the degree sequence
	  * vector
	  * @return a List representing the degree sequence vector
	  */
	private static List<Integer> getDegreeSequenceVector(Graph inputGraph)
	{
		List<Integer> degreeSequenceVector = new ArrayList<>();
		for (int currentVertex = 0; currentVertex < inputGraph.getSize();
				++currentVertex) {
			int degree = inputGraph.getAdjacencyList(currentVertex).size();
			degreeSequenceVector.add(degree);
		}
		return degreeSequenceVector;
	}


}
