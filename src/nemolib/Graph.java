// TODO retain mapping of nodeID to nodeName from input file
// TODO make a GraphParser class to take the complexity out of this class

package nemolib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Implementation of graph data structure to manage network graphs.
 */
public class Graph implements Serializable {
	private List<AdjacencyList> adjacencyLists;

	/**
	 * Construct a graph object from a file. The file must contain two nodes 
	 * on each line representing an edge.
	 *
	 * @filename the name of the file from which to construct the graph.
	 */
    public Graph(String filename) throws IOException
    {
        adjacencyLists = new ArrayList<>();
        parse(filename);
    }

	/**
	 * Construct a Graph object.
	 */
    public Graph()
    {
        adjacencyLists = new ArrayList<>();
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
	 * @param vertexA One of the vertices between which to add an edge.
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

    // get the number of nodes in the graph
    public int size()
    {
        return adjacencyLists.size();
    }

    // get the adjacency list for a given node
    public AdjacencyList getAdjacencyList(Integer index) {
        return adjacencyLists.get(index);
    }

    // parses a data file into an adjacency list representing the graph
    private void parse(String filename) throws IOException {
        Map<String, Integer> nameToIndex = new HashMap<String, Integer>();

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
            int fromIndex = getOrCreateIndex(edge[0], nameToIndex);
            int toIndex   = getOrCreateIndex(edge[1], nameToIndex);

            // don't add self edges
            if (fromIndex != toIndex) {
                getAdjacencyList(fromIndex).add(toIndex);
                getAdjacencyList(toIndex).add(fromIndex);
            }
        }
    }

    // get index of a node given the node's name
    // create an entry if it does not exist
    private Integer getOrCreateIndex(String nodeName,
                                     Map<String, Integer> nameToIndex) {
        if (!nameToIndex.containsKey(nodeName)) {
            nameToIndex.put(nodeName, adjacencyLists.size());
            adjacencyLists.add(new AdjacencyList());
        }
        return nameToIndex.get(nodeName);
    }

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
		    sb.append(edge).append("%n");
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
			return (this.nodeA == ((Edge)o).nodeA && this.nodeB == ((Edge)o).nodeB) || (this.nodeA == ((Edge)o).nodeB && this.nodeB == ((Edge)o).nodeA);
		}

		@Override
	    public String toString() {
			return "[" + nodeA + ", " + nodeB + "]";
		}

	}
}
