package edu.uwb.nemolib;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class SubgraphCollection
{
	Set<Set<Integer>> subgraphs;

	// prevent default constructor from being called
	private SubgraphCollection()
	{
		throw new AssertionError();
	}

	SubgraphCollection(int subgraphSize)
	{
		subgraphs = new HashSet<Set<Integer>>();
	}

	void add(Set<Integer> subgraph)
	{
		subgraphs.add(subgraph);
	}

}
