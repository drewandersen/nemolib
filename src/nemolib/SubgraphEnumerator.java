package nemolib;

/**
 * SubgraphEnumerators detect all instances of subgraphs of a particular size
 * and add them to a SubgraphEnumerationResult object.
 */
public interface SubgraphEnumerator
{
	public void enumerate(Graph targetGraph,
	                      SubgraphEnumerationResult subgraphs,
	                      int subgraphSize);
}
