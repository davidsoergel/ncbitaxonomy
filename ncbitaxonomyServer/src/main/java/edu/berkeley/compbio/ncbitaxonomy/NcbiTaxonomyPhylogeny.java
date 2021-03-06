/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.stats.ContinuousDistribution1D;
import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.BasicPhylogenyNode;
import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.DepthFirstTreeIterator;
import com.davidsoergel.trees.HierarchyNode;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.NodeNamer;
import com.davidsoergel.trees.PhylogenyNode;
import com.davidsoergel.trees.RootedPhylogeny;
import com.davidsoergel.trees.TreeException;
import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multiset;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhyloUtilsRuntimeException;
import edu.berkeley.compbio.phyloutils.TaxonomyService;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Presents the NCBI taxonomy as an RootedPhylogeny with Integer IDs corresponding to the NCBI taxids.  This is a live
 * view onto a MySQL database imported from ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdump.tar.gz.  The database connection
 * is configured by placing a file at ~/.ncbitaxonomy/ncbitaxonomy.properties, containing something like this:
 * <p/>
 * <pre>
 * default = orchid_test
 *
 * localhost_test.driver = com.mysql.jdbc.Driver
 * localhost_test.url = jdbc:mysql://localhost/ncbi_tx_test
 * localhost_test.username = ncbi_tx_test
 * localhost_test.password = xxxxxx
 *
 * orchid_test.driver = com.mysql.jdbc.Driver
 * orchid_test.url = jdbc:mysql://orchid.berkeley.edu/ncbi_tx_test
 * orchid_test.username = ncbi_tx_test
 * orchid_test.password = xxxxxx
 *
 * </pre>
 * <p/>
 * Many methods required by the RootedPhylogeny interface are not appropriate for this purpose and hence throw
 * NotImplementedException (perhaps this is an indication that the interface should be refactored, e.g. separated into a
 * mutable vs. immutable varieties, etc.; oh well).
 * <p/>
 * Those methods that are functional are largely delegated to NcbiTaxonomyServiceImpl, which is instantiated here and
 * configured by Spring.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service(value = "ncbiTaxonomyPhylogeny")
public class NcbiTaxonomyPhylogeny extends AbstractRootedPhylogeny<Integer>
		implements TaxonomyService<Integer>, TaxonomySynonymService //extends Singleton<PhyloUtilsService> //,
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyPhylogeny.class);
	// ------------------------------ FIELDS ------------------------------

	private static NcbiTaxonomyPhylogeny instance;// = new NcbiTaxonomyService();

	@Autowired
	private NcbiTaxonomyServiceEngine ncbiTaxonomyServiceEngine;

	// -------------------------- STATIC METHODS --------------------------


	private Map<Integer, List<Integer>> ancestorPathIdsCache;

	@NotNull
	@Override
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		List<Integer> result = ancestorPathIdsCache.get(id);

		if (result == null || result.isEmpty())
			{
			if (result != null && result.isEmpty())
				{
				logger.warn("Cache contained empty ancestorPathIds for " + id);
				}

			PhylogenyNode<Integer> node = getNode(id);

			if (id == 1)
				{
				result = new ArrayList<Integer>(1);
				}
			else
				{
				PhylogenyNode<Integer> parent = node.getParent();

				if (parent == null || parent.equals(node))
					{
					throw new NcbiTaxonomyRuntimeException("Node " + id + " has invalid parent: " + parent);
					}

				result = new ArrayList<Integer>(getAncestorPathIds(parent.getPayload()));
				}

			result.add(id);

			result = Collections.unmodifiableList(result);

			assert !result.isEmpty();
			ancestorPathIdsCache.put(id, result);
			}
		return result;
		//return getNode(id).getAncestorPathIds();
		}

	private Map<Integer, BasicPhylogenyNode<Integer>> convertedNodes =
			new MapMaker().makeComputingMap(new Function<Integer, BasicPhylogenyNode<Integer>>()
			{
			public BasicPhylogenyNode<Integer> apply(final Integer id)
				{
				try
					{
					List<Integer> idPath = getAncestorPathIds(id);

					BasicPhylogenyNode<Integer> parent = null;
					if (idPath.size() > 1)
						{
						parent = convertedNodes.get(idPath.get(idPath.size() - 2));
						}

					BasicPhylogenyNode<Integer> convertedNode = new BasicPhylogenyNode<Integer>(parent, id);
					convertedNode.setWeight(1.0);

					return convertedNode;
					}
				catch (NoSuchNodeException e)
					{
					throw new PhyloUtilsRuntimeException(e);
					}
				}
			});

	@NotNull
	@Override
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return Collections.unmodifiableList(convertedNodes.get(id).getAncestorPath());
		}
/*
	private Map<Integer, List<BasicPhylogenyNode<Integer>>> ancestorPathNodesCache;

	// PERF not sure caching all the paths like this makes sense, vs. just caching the tree as a whole?
	// ** a problem is that, this way, the same node in multiple paths is represented by distinct objects
	// because serializing it and then deserializing it twice makes copies
	// and the copies don't know that they're the same because BasicPhylogenyNode doesn't have an equals() based on the ID, because it doesn't assume unique IDs.

	@NotNull
	@Override
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		List<BasicPhylogenyNode<Integer>> result = ancestorPathNodesCache.get(id);

		if (result == null || result.isEmpty())
			{
			if (result != null && result.isEmpty())
				{
				logger.warn("Cache contained empty ancestorPathAsBasic for " + id);
				}

			PhylogenyNode<Integer> node = getNode(id);

			if (id == 1)
				{
				result = new ArrayList<BasicPhylogenyNode<Integer>>(1);
				BasicPhylogenyNode<Integer> convertedNode = new BasicPhylogenyNode<Integer>(null, node);
				result.add(convertedNode);
				}
			else
				{
				PhylogenyNode<Integer> parent = node.getParent();

				if (parent == null || parent.equals(node))
					{
					throw new NcbiTaxonomyRuntimeException("Node " + id + " has invalid parent: " + parent);
					}

				result = new ArrayList<BasicPhylogenyNode<Integer>>(getAncestorPathAsBasic(parent.getPayload()));
				BasicPhylogenyNode<Integer> convertedNode =
						new BasicPhylogenyNode<Integer>(result.get(result.size() - 1), node);
				result.add(convertedNode);
				}

			result = Collections.unmodifiableList(result);

			assert !result.isEmpty();
			ancestorPathNodesCache.put(id, result);
			}
		return result;
		//return getNode(id).getAncestorPathIds();
		}
*/

	public static NcbiTaxonomyPhylogeny getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiTaxonomyPhylogeny();
			}
		return instance;
		}

	public static NcbiTaxonomyPhylogeny getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiTaxonomyPhylogeny instance)
		{
		NcbiTaxonomyPhylogeny.instance = instance;
		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold)
		{
		throw new NotImplementedException();
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold,
	                                                      Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		throw new NotImplementedException();
		}
	// --------------------------- CONSTRUCTORS ---------------------------

	public boolean isLeaf(Integer leafId) throws NoSuchNodeException
		{
		return getNode(leafId).isLeaf();
		}

	public synchronized boolean isKnown(Integer leafId) //throws NoSuchNodeException
	{
	try
		{
		getNode(leafId);
		return true;
		}
	catch (NoSuchNodeException e)
		{
		return false;
		}
	}


	public NcbiTaxonomyPhylogeny()
		{
		/*	try
			  {


			  ncbiTaxonomyServiceEngine = ((NcbiTaxonomyServiceEngine) ctx.getBean("ncbiTaxonomyServiceEngineImpl"));

			  // we've got what we need, so we can ditch the context already
			  // no, that breaks transactioning
			  //ctx.close();
			  }
		  catch (Exception e)
			  {
			  logger.error("Error", e);
			  throw new RuntimeException("Could not load database properties for NCBI taxonomy", e);
			  }
  */
		//ancestorPathIdsCache = CacheManager.getAccumulatingMap(this, "ancestorPathCache");
		ancestorPathIdsCache = CacheManager.getAccumulatingMapAssumeSerializable(this, "ancestorPathCache");

		//ancestorPathNodesCache = CacheManager.getAccumulatingMap(this, "ancestorPathNodesCache");
		//ancestorPathNodesCache = CacheManager.getAccumulatingMapAssumeSerializable(this, "ancestorPathNodesCache");
		}

	/**
	 * Cache database search results for the current session in a local file in /tmp/edu.berkeley.compbio/ncbitaxonomy.cache,
	 * since it'll be much faster to load that up again in the future than to rerun all the database queries.  The
	 * assumption is that the database isn't changing anyway; if it does change, deleting the cache file will allow the new
	 * data to take effect.
	 */
/*	public void saveState()
		{
		ncbiTaxonomyServiceImpl.saveState();
		}
*/

	/**
	 * Find the taxid corresponding to the given taxon name.
	 *
	 * @param name the name of the taxon, matching any name in the NCBI names table (including scientific names,
	 *             misspellings, etc.)
	 * @return the taxid for the given name, if found
	 * @throws NcbiTaxonomyException when the name is not found, or if the name maps to multiple taxids
	 */
	public Integer findTaxidByName(@NotNull String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.findTaxidByName(name);
		}

	/**
	 * Find the taxid corresponding to the given taxon name, removing space-delimited words from the end of the name one at
	 * a time until a match is found.  The idea is to find the species node when a name is given that includes more
	 * detailed information, such as a strain identifier.  Similarly, will match the genus if the species name does not
	 * exist.
	 *
	 * @param name the name of the taxon, with a prefix matching any name in the NCBI names table (including scientific
	 *             names, misspellings, etc.)
	 * @return the taxid for the given name, if found
	 * @throws NoSuchNodeException when the name is not found, or if the name maps to multiple taxids
	 */
	public Integer findTaxidByNameRelaxed(String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.findTaxidByNameRelaxed(name);
		}

	public Set<String> getCachedNamesForId(Integer id)
		{
		return ncbiTaxonomyServiceEngine.getCachedNamesForId(id);
		}

	public void addToMap(Map<Integer, PhylogenyNode<Integer>> uniqueIdToNodeMap, NodeNamer<Integer> namer)
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	public Collection<String> synonymsOf(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.synonymsOf(s);
		}

	public Collection<String> synonymsOfParent(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.synonymsOfParent(s);
		}

	public Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.synonymsOfRelaxed(s);
		}

	public PhylogenyNode<Integer> findRoot()
		{
		return getRoot();
		}

	/**
	 * {@inheritDoc}
	 */
	//@Override
	public PhylogenyNode<Integer> getRoot()
		{
		try
			{
			return getNode(1);
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Ncbi taxonomy has no root!", e);
			throw new PhyloUtilsRuntimeException(e, "Ncbi taxonomy has no root!");
			}
		}

	/*	public Integer commonAncestor(Set<Integer> knownMergeIds)
			 {
			 return ncbiTaxonomyServiceImpl.commonAncestorID(knownMergeIds);
			 }

		 public Integer commonAncestor(Integer taxIdA, Integer taxIdB)
			 {
			 return ncbiTaxonomyServiceImpl.commonAncestorID(taxIdA, taxIdB);
			 }
	 */

	/**
	 * Not implemented
	 */
	@Override
	public double distanceBetween(Integer taxIdA, Integer taxIdB)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//		return ncbiTaxonomyServiceImpl.distanceBetween(taxIdA, taxIdB);
		}

	@Override
	public double distanceBetween(PhylogenyNode<Integer> a, PhylogenyNode<Integer> b)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public PhylogenyNode<Integer> nearestAncestorWithBranchLength(PhylogenyNode<Integer> id)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public Double minDistanceBetween(PhylogenyNode<Integer> node1, PhylogenyNode<Integer> node2)
			throws PhyloUtilsException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double minDistanceBetween(Integer taxIdA, Integer taxIdB)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//		return ncbiTaxonomyServiceImpl.distanceBetween(taxIdA, taxIdB);
		}

	public void setSynonymService(TaxonomySynonymService taxonomySynonymService)
		{
		logger.warn("NCBI taxonomy doesn't currently use other synonym services for any purpose; ignoring");
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getNode(Integer taxid) throws NoSuchNodeException//throws NcbiTaxonomyException
		{
		try
			{
			return ncbiTaxonomyServiceEngine.findNode(taxid);
			}
		catch (NoResultException e)
			{
			throw new NoSuchNodeException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");
			//return null;
			//throw new NcbiTaxonomyException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");

			//throw new NoSuchElementException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");
			}
		}


	/**
	 * {@inheritDoc}
	 */
	/*	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
				 {
				 return ncbiTaxonomyServiceImpl.extractTreeWithLeaves(ids);
				 }
		 */
	@NotNull
	public List<? extends PhylogenyNode<Integer>> getChildren()
		{
		return getRoot().getChildren();
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getChildWithPayload(Integer id) throws NoSuchNodeException
		{
		return getRoot().getChildWithPayload(id);
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public Integer getPayload()
		{
		return 1;// the root taxid of the ncbi taxonomy.
		}

	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode getParent()
		{
		return null;
		}

	/**
	 * Not implemented
	 */
	/*	public PhylogenyNode<Integer> newChild()
		 {
		 throw new NotImplementedException("The NCBI taxonomy is not editable");
		 }
 */

	/**
	 * Not implemented
	 */
	public PhylogenyNode<Integer> newChild(Integer payload)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * Not implemented
	 */
	public void setPayload(Integer taxid)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}


	/**
	 * {@inheritDoc}
	 */
	public void setParent(PhylogenyNode<Integer> parent)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasValue()
		{
		return true;
		}

	/**
	 * {@inheritDoc}
	 */
	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		// this is the root node
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();

		result.add(0, getRoot());

		return Collections.unmodifiableList(result);
		}

	public List<? extends HierarchyNode<Integer, PhylogenyNode<Integer>>> getAncestorPath(final boolean includeSelf)
		{
		throw new NotImplementedException();
		}

	/**
	 * {@inheritDoc}
	 */
	public List<Integer> getAncestorPathPayloads()
		{
		// this is the root node
		List<Integer> result = new LinkedList<Integer>();

		result.add(0, getRoot().getPayload());

		return Collections.unmodifiableList(result);
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getLength()
		{
		return 0.;
		}

	/**
	 * Not implemented
	 */
	public void setLength(Double d)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getLargestLengthSpan()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getGreatestBranchLengthDepthBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getLeastBranchLengthDepthBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getGreatestDepthBelow(Integer a)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getLargestLengthSpan(Integer a)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public double maxDistance()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public void printDepthsBelow()
		{
		}

	/**
	 * Not implemented
	 */
	public void registerChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}

	/**
	 * Not implemented
	 */
	public void unregisterChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}


	/**
	 * Not implemented
	 */
	public Iterator<PhylogenyNode<Integer>> iterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> depthFirstIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> phylogenyIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		//return getTree().depthFirstIterator();
		}

	public void toNewick(Writer out, String prefix, String tab, int minClusterSize, double minLabelProb)
			throws IOException
		{
		//throw new NotImplementedException("Printing the entire NCBI taxonomy is probably a bad idea");
		ncbiTaxonomyServiceEngine.toNewick(out, prefix, tab, minClusterSize, minLabelProb);
		}

	public void writeSynonyms(Writer out)
		{

		ncbiTaxonomyServiceEngine.writeSynonyms(out);
		}


	/**
	 * Not implemented
	 */
	public Collection<PhylogenyNode<Integer>> getLeaves()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public Set<Integer> getLeafValues()
		{
		throw new PhyloUtilsRuntimeException(
				"Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public void addNode(PhylogenyNode<Integer> n) throws PhyloUtilsException
		{
		throw new PhyloUtilsRuntimeException(
				"Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}


	/**
	 * Not implemented
	 */
	public Set<Integer> getNodeValues()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public Map<Integer, PhylogenyNode<Integer>> getUniqueIdToNodeMap()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer nearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.findNearestKnownAncestor(rootPhylogeny, leafId);
		}

	public Integer nearestAncestorAtRank(final String rankName, Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.findNearestAncestorAtRank(rankName, leafId);
		}

	public Set<Integer> getTaxIdsWithRank(final String rankName)
		{
		return ncbiTaxonomyServiceEngine.findTaxIdsWithRank(rankName);
		}

	/**
	 * Not implemented
	 */
	public Integer nearestAncestorWithBranchLength(Integer leafId)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf()
		{
		return getRoot().isLeaf();
		}

	/**
	 * Not implemented
	 */
	public Double getWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public double getWeight(Integer id)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public Double getCurrentWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public void setWeight(@NotNull Double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public void incrementWeightBy(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/*
	 public void propagateWeightFromBelow()
		 {
		 throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		 }
 */

	/**
	 * Not implemented
	 */
	public double distanceToRoot()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	@Override
	public void setLeafWeightsRandom(ContinuousDistribution1D dist)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	@Override
	public RootedPhylogeny<Integer> clone()
		{
		throw new NotImplementedException();
		}

	/**
	 * Not implemented
	 */
	@Override
	public void setLeafWeights(Multiset<Integer> ids)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public Map<Integer, Double> distributeInternalWeightsToLeaves(Map<Integer, Double> taxIdToWeightMap)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode<Integer> getSelfNode()
		{
		return getRoot();
		}


	/**
	 * Not implemented
	 */
	public void appendSubtree(StringBuffer sb, String indent)
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a String is probably a bad idea");
		}

	public RootedPhylogeny<Integer> getTree()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Tree is probably a bad idea");
		}

	public PhylogenyNode<Integer> nearestAncestorWithBranchLength() throws NoSuchNodeException
		{
		throw new NoSuchNodeException("Root doesn't have a branch length.");
		}

	public double getDepthFromRoot(Integer b) throws NoSuchNodeException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//return stringTaxonomyService.minDistanceBetween(intToNodeMap.get(a), intToNodeMap.get(b));
		//	return exactDistanceBetween(name1, name2);
		}

	@Override
	public String toString()
		{
		String shortname = getClass().getName();
		shortname = shortname.substring(shortname.lastIndexOf(".") + 1);
		return shortname;
		}

/*	public RootedPhylogeny<Integer> findSubtreeByName(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> findSubtreeByNameRelaxed(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}
*/

	public String getRelaxedName(String name)
		{
		throw new NotImplementedException();
		}
/*
	public Integer findTaxIdOfShallowestLeaf(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> findTreeForName(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public int getNumNodesForName(String name)
		{
		throw new NotImplementedException();
		}*/

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(Set<Integer> matchingIds, String name)
			throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public Set<Integer> findMatchingIds(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public Set<Integer> findMatchingIdsRelaxed(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	@Override
	@Transactional
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids, boolean ignoreAbsentNodes,
	                                                            boolean includeInternalBranches,
	                                                            MutualExclusionResolutionMode mode)
			throws NoSuchNodeException //, NodeNamer<T> namer

	{
	return super.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
	}

	public Set<Integer> getLeafIds()
		{
		throw new NotImplementedException();
		}

	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceEngine.findScientificName(taxid);
		}

	public Collection<String> getAllNamesForIds(final Set<Integer> ids)
		{
		throw new NotImplementedException();
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return new MapMaker().makeComputingMap(new Function<Integer, String>()
		{
		public String apply(@Nullable final Integer id)
			{
			try
				{
				return getScientificName(id);
				}
			catch (NoSuchNodeException e)
				{
				return id.toString();
				}
			}
		});
		}

	public void collectLeavesBelowAtApproximateDistance(final double minDesiredTreeDistance,
	                                                    final double maxDesiredTreeDistance,
	                                                    final Collection<PhylogenyNode<Integer>> result)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}


	@NotNull
	public Collection<? extends PhylogenyNode<Integer>> getDescendantLeaves()
		{
		Set<PhylogenyNode<Integer>> result = new HashSet<PhylogenyNode<Integer>>();
		for (PhylogenyNode<Integer> n : this)
			{
			if (n.isLeaf())
				{
				result.add(n);
				}
			}
		return result;
		}
	}
