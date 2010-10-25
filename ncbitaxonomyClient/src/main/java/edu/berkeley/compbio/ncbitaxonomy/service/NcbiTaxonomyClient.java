package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.BasicPhylogenyNode;
import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.TreeException;
import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Presents the Hessian proxy as a class requiring no configuration, for use as a Jandy plugin.  Just delegates
 * everything through.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyClient implements NcbiTaxonomyService
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyClient.class);

	private NcbiTaxonomyService ncbiTaxonomy;

	private static NcbiTaxonomyClient instance;

	/*
	 private Map<Integer, HashSet<String>> synonyms = new HashMap<Integer, HashSet<String>>();


	 public Collection<String> synonymsOf(String s) throws NoSuchNodeException
		 {
		 int taxid = findTaxidByName(s);
		 HashSet<String> result = synonyms.get(taxid);
		 if (result == null)
			 {
			 result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			 synonyms.put(taxid, result);
			 }
		 return result;
		 }

	 public Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException
		 {
		 int taxid = findTaxidByNameRelaxed(s);
		 HashSet<String> result = synonyms.get(taxid);
		 if (result == null)
			 {
			 result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			 synonyms.put(taxid, result);
			 }
		 return result;
		 //return ncbiTaxonomyNameDao.findSynonymsRelaxed(s);
		 }

	 public Collection<String> synonymsOfParent(String s) throws NoSuchNodeException
		 {
		 int taxid = findParentTaxidByName(s);
		 HashSet<String> result = synonyms.get(taxid);
		 if (result == null)
			 {
			 result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			 synonyms.put(taxid, result);
			 }
		 return result;
		 }
 */

	public Collection<String> synonymsOf(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.synonymsOf(name);
		}

	public Collection<String> synonymsOfParent(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.synonymsOfParent(name);
		}

	public Collection<String> synonymsOfRelaxed(final String name) throws NoSuchNodeException
		{
		logger.info("NcbiTaxonomyClient synonymsOfRelaxed " + name);
		Collection<String> result = ncbiTaxonomy.synonymsOfRelaxed(name);
		logger.info("NcbiTaxonomyClient synonymsOfRelaxed " + name + " DONE");
		return result;
		}

	public static NcbiTaxonomyClient getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiTaxonomyClient();
			}
		return instance;
		}

	public static NcbiTaxonomyClient getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiTaxonomyClient instance)
		{
		NcbiTaxonomyClient.instance = instance;
		}

	public NcbiTaxonomyClient()
		{
		ApplicationContext ctx = null;
		try
			{
			ctx = NcbiTaxonomyServicesContextFactory.makeNcbiTaxonomyServicesContext();
			}
		catch (IOException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}

		ncbiTaxonomy = (NcbiTaxonomyService) ctx.getBean("ncbiTaxonomyService");

		readStateIfAvailable();
		}

	protected void readStateIfAvailable()
		{
		taxIdByNameRelaxed =
				CacheManager.getAccumulatingMap(this, "taxIdByNameRelaxed"); //, new HashMap<String, Integer>());
		taxIdByName = CacheManager.getAccumulatingMap(this, "taxIdByName"); //, new HashMap<String, Integer>());
		taxIdsWithRank = CacheManager.getAccumulatingMap(this, "taxIdsWithRank"); //, new HashMap<String, Integer>());
		//	synonyms = CacheManager.getAccumulatingMap(this, "synonyms"); //, new HashMap<Integer, Set<String>>());
		scientificNames = CacheManager.getAccumulatingMap(this, "scientificNames");

		ancestorPathIdsCache = CacheManager.getAccumulatingMapAssumeSerializable(this, "ancestorPathCache");

		ancestorPathNodesCache = CacheManager.getAccumulatingMapAssumeSerializable(this, "ancestorPathNodesCache");
		}


	private Map<Integer, String> scientificNames = new HashMap<Integer, String>();

	/*public String getScientificName(final Integer from) throws NoSuchNodeException
		 {
		 return ncbiTaxonomy.getScientificName(from);
		 }
 */

	@NotNull
	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		String result = scientificNames.get(taxid);
		if (result == null)
			{
			result = ncbiTaxonomy.getScientificName(taxid); //findNode(taxid).getScientificName();
			scientificNames.put(taxid, result);
			}
		return result;
		}

	private Map<String, HashSet<Integer>> taxIdsWithRank = new HashMap<String, HashSet<Integer>>();

	public Set<Integer> getTaxIdsWithRank(final String rank)
		{
		HashSet<Integer> result = taxIdsWithRank.get(rank);
		if (result == null)
			{
			result = new HashSet<Integer>(ncbiTaxonomy.getTaxIdsWithRank(rank));
			taxIdsWithRank.put(rank, result);
			}
		return result;
		}

	public boolean isDescendant(final Integer ancestor, final Integer descendant) throws NoSuchNodeException
		{
		return ncbiTaxonomy.isDescendant(ancestor, descendant);
		}

	public double minDistanceBetween(final Integer name1, final Integer name2) throws NoSuchNodeException
		{
		return ncbiTaxonomy.minDistanceBetween(name1, name2);
		}

	public void setSynonymService(final TaxonomySynonymService taxonomySynonymService)
		{
		if (taxonomySynonymService instanceof NcbiTaxonomyClient)
			{
			//ignore
			}
		else
			{
			logger.warn("Can't use a non-NCBI-taxonomy synonym service");
			}
		//	ncbiTaxonomy.setSynonymService(taxonomySynonymService);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomy.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomy.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomy.isLeaf(leafId);
		}

	public double getDepthFromRoot(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getDepthFromRoot(taxid);
		}

	public double getGreatestDepthBelow(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getGreatestDepthBelow(taxid);
		}

	public double getLargestLengthSpan(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getLargestLengthSpan(taxid);
		}

	public double maxDistance()
		{
		return ncbiTaxonomy.maxDistance();
		}

	public String getRelaxedName(final String name)
		{
		return ncbiTaxonomy.getRelaxedName(name);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiTaxonomy.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.findMatchingIdsRelaxed(name);
		}

	public Set<Integer> selectAncestors(final Collection<Integer> labels, final Integer id)
		{
		return ncbiTaxonomy.selectAncestors(labels, id);
		}

	public Set<Integer> getLeafIds()
		{
		return ncbiTaxonomy.getLeafIds();
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

	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}

	public boolean isKnown(final Integer value)
		{
		return ncbiTaxonomy.isKnown(value);
		}


	public Integer nearestAncestorWithBranchLength(final Integer id) throws NoSuchNodeException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//return ncbiTaxonomy.nearestAncestorWithBranchLength(id);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiTaxonomy.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiTaxonomy.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}


	private Map<Integer, List<Integer>> ancestorPathIdsCache;

/*
	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getAncestorPathIds(id);
		}*/

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		List<Integer> result = ancestorPathIdsCache.get(id);

		if (result == null || result.isEmpty())
			{
			result = ncbiTaxonomy.getAncestorPathIds(id);
			assert !result.isEmpty();
			ancestorPathIdsCache.put(id, result);
			}
		return result;
		}

	private Map<Integer, List<BasicPhylogenyNode<Integer>>> ancestorPathNodesCache;

	// PERF not sure caching all the paths like this makes sense, vs. just caching the tree as a whole?

	@NotNull
	//@Override
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		List<BasicPhylogenyNode<Integer>> result = ancestorPathNodesCache.get(id);
		//** Note these BasicPhylogenyNode entries have null parents!  In our particular use case we never access them anyway, but it's ugly.

		if (result == null || result.isEmpty())
			{

			result = ncbiTaxonomy.getAncestorPathAsBasic(id);

			assert !result.isEmpty();
			ancestorPathNodesCache.put(id, result);
			}
		return result;
		//return getNode(id).getAncestorPathIds();
		}

/*
	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getAncestorPathAsBasic(id);
		}


	private Map<Integer, List<BasicPhylogenyNode<Integer>>> ancestorPathNodesCache;

	*/

/*
	@NotNull
	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		List<PhylogenyNode<Integer>> result = ancestorPathNodesCache.get(id);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomy.findTaxidByNameRelaxed(speciesNameA);
			taxIdByNameRelaxed.put(speciesNameA, taxIdA);
			}
		return taxIdA;
		return ncbiTaxonomy.getAncestorPath(id);
		}
*/


	private Map<String, Integer> taxIdByName = new HashMap<String, Integer>();

	public Integer findTaxidByName(@NotNull final String speciesNameA) throws NoSuchNodeException
		{
		;
		Integer taxIdA = taxIdByName.get(speciesNameA);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomy.findTaxidByName(speciesNameA);
			taxIdByName.put(speciesNameA, taxIdA);
			}
		return taxIdA;
		}


	// PERF not sure caching all the paths like this makes sense, vs. just caching the tree as a whole?
	private Map<String, Integer> taxIdByNameRelaxed = new HashMap<String, Integer>();

	public Integer findTaxidByNameRelaxed(final String speciesNameA) throws NoSuchNodeException
		{
		//sometimes the taxid is already in the string
		try
			{
			return Integer.parseInt(speciesNameA);
			}
		catch (NumberFormatException e)
			{
			// no problem, look for it by name then
			}

		Integer taxIdA = taxIdByNameRelaxed.get(speciesNameA);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomy.findTaxidByNameRelaxed(speciesNameA);
			taxIdByNameRelaxed.put(speciesNameA, taxIdA);
			}
		return taxIdA;
		}

	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiTaxonomy.getCachedNamesForId(id);
		}
	}
