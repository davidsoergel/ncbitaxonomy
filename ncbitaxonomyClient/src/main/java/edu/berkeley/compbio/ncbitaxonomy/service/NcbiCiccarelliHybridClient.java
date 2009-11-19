package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.BasicPhylogenyNode;
import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.TreeException;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiCiccarelliHybridClient implements NcbiCiccarelliHybridService
	{
	private static final Logger logger = Logger.getLogger(NcbiCiccarelliHybridClient.class);

	private NcbiCiccarelliHybridService ncbiCiccarelliHybrid;

	private static NcbiCiccarelliHybridClient instance;


	public static NcbiCiccarelliHybridClient getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiCiccarelliHybridClient();
			}
		return instance;
		}

	public static NcbiCiccarelliHybridClient getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiCiccarelliHybridClient instance)
		{
		NcbiCiccarelliHybridClient.instance = instance;
		}

	public NcbiCiccarelliHybridClient()
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

		ncbiCiccarelliHybrid = (NcbiCiccarelliHybridService) ctx.getBean("ncbiCiccarelliHybridService");
		}

	public Integer nearestKnownAncestor(final Integer taxidA) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.nearestKnownAncestor(taxidA);
		}

	public double exactDistanceBetween(final Integer taxidA, final Integer taxidB) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.exactDistanceBetween(taxidA, taxidB);
		}

	public boolean isDescendant(final Integer ancestor, final Integer descendant) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.isDescendant(ancestor, descendant);
		}

	public double minDistanceBetween(final Integer name1, final Integer name2) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.minDistanceBetween(name1, name2);
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
		//ncbiCiccarelliHybrid.setSynonymService(taxonomySynonymService);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
			throws NoSuchNodeException, TreeException
		{
		return ncbiCiccarelliHybrid.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiCiccarelliHybrid.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.isLeaf(leafId);
		}

	public double getDepthFromRoot(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getDepthFromRoot(taxid);
		}

	public double getGreatestDepthBelow(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getGreatestDepthBelow(taxid);
		}

	public double getLargestLengthSpan(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getLargestLengthSpan(taxid);
		}

	public double maxDistance()
		{
		return ncbiCiccarelliHybrid.maxDistance();
		}

	public String getRelaxedName(final String name)
		{
		return ncbiCiccarelliHybrid.getRelaxedName(name);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.findMatchingIdsRelaxed(name);
		}

	public Set<Integer> selectAncestors(final Collection<Integer> labels, final Integer id)
		{
		return ncbiCiccarelliHybrid.selectAncestors(labels, id);
		}

	public Set<Integer> getLeafIds()
		{
		return ncbiCiccarelliHybrid.getLeafIds();
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiCiccarelliHybrid.getFriendlyLabelMap();
		}

	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}

	public boolean isKnown(final Integer value)
		{
		return ncbiCiccarelliHybrid.isKnown(value);
		}

	public Integer nearestAncestorWithBranchLength(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.nearestAncestorWithBranchLength(id);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getAncestorPathIds(id);
		}

	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.getAncestorPathAsBasic(id);
		}

	/*	@NotNull
	 public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		 {
		 return ncbiCiccarelliHybrid.getAncestorPath(id);
		 }
 */
	public Integer findTaxidByName(@NotNull final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybrid.findTaxidByNameRelaxed(name);
		}

	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiCiccarelliHybrid.getCachedNamesForId(id);
		}
	}
