package edu.uwb.nemolib;

import java.io.Serializable;
import java.util.*;

/**
 * Implementation of graph data structure to manage network graphs.
 */
public class Graph implements Serializable {
	private List<AdjacencyList> adjacencyLists;
	private int maxNodeId;

	/**
	 * Construct a graph object.
	 */
    public Graph() {
	    adjacencyLists = new ArrayList<>();
	    maxNodeId = 0;
    }

	/**
	 * Add a vertex to this Graph.
	 * @return the ID number assigned to the new vertex
	 */
	public int addVertex()
    {
    	adjacencyLists.add(new AdjacencyList());
	    return adjacencyLists.size() - 1;
    }

	/**
	 * Add an edge between two existing vertices on this graph.
	 * @param vertexA One of the vertices between which to addSubgraph an edge.
	 * @param vertexB The other vertex.
	 * @return true if both vertexA and vertexB exist in this Graph; false
	 * otherwise.
	 */
    public boolean addEdge(int vertexA, int vertexB)
    {
    	if (vertexA > adjacencyLists.size() - 1 ||
		    vertexB > adjacencyLists.size() - 1)
	    {
	    	return false;
	    }
	    else
	    {
		    adjacencyLists.get(vertexA).add(vertexB);
		    adjacencyLists.get(vertexB).add(vertexA);
		    return true;
	    }
    }

	/**
	 * Get the getSize of this Graph
	 * @return the getSize of this graph
	 */
	public int getSize()
    {
        return adjacencyLists.size();
    }

    // get the adjacency list for a given node
    public AdjacencyList getAdjacencyList(Integer index) {
        return adjacencyLists.get(index);
    }

    // get index of a node given the node's name
    // create an entry if it does not exist
    Integer getOrCreateIndex(String nodeName,
                                     Map<String, Integer> nameToIndex) {
        if (!nameToIndex.containsKey(nodeName)) {
            nameToIndex.put(nodeName, adjacencyLists.size());
            adjacencyLists.add(new AdjacencyList());
        }
        return nameToIndex.get(nodeName);
    }

	/**
	 * Return a string representation of this Graph object.
	 * @return
	 */
	@Override
    public String toString() {
	    Set<Edge> edges = new HashSet<>();
	    for(int i = 0; i < adjacencyLists.size(); i++) {
		    AdjacencyList curAdjList = adjacencyLists.get(i);
		    CompactHashSet.Iter adjListItr = curAdjList.iterator();
		    while(adjListItr.hasNext()) {
			    edges.add(new Edge(i, adjListItr.next()));
		    }
	    }
	    StringBuilder sb = new StringBuilder();
	    for(Edge edge : edges) {
		    sb.append(edge).append('\n');
	    }
	    return sb.toString();
    }

	// Represents edges for a Graph
    private class Edge {
		int nodeA;
		int nodeB;

		Edge() {
			throw new AssertionError();
		}

		Edge(int nodeA, int nodeB) {
			this.nodeA = nodeA;
			this.nodeB = nodeB;
		}

		@Override
		public boolean equals(Object o) {
			if (o.getClass() != this.getClass()) {
				return false;
			}
			return (this.nodeA == ((Edge)o).nodeA &&
					this.nodeB == ((Edge)o).nodeB) ||
					(this.nodeA == ((Edge)o).nodeB &&
							this.nodeB == ((Edge)o).nodeA);
		}

		@Override
	    public String toString() {
			return "[" + nodeA + ", " + nodeB + "]";
		}
	}
}
