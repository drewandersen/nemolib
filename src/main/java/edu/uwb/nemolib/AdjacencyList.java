package edu.uwb.nemolib;
import java.io.Serializable;

/**
 * AdjacencyList represents all adjacent vertices for a particular vertex in a
 * network graph.
 */
class AdjacencyList implements Serializable{

    private CompactHashSet nodes;

    AdjacencyList() {
        this.nodes = new CompactHashSet();
    }

    private AdjacencyList(AdjacencyList adjacencyList) {
        this.nodes = adjacencyList.nodes.copy();
    }

    void add(int node) {
        nodes.add(node);
    }

    CompactHashSet.Iter iterator() {
        return nodes.iterator();
    }

    boolean contains(int node) {
        return nodes.contains(node);
    }

    int size() {
        return nodes.size();
    }

    boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public String toString() {
        return nodes.toString();
    }

    boolean remove(int node) {
        return nodes.remove(node);
    }

    AdjacencyList copy() {
        return new AdjacencyList(this);
    }
}
