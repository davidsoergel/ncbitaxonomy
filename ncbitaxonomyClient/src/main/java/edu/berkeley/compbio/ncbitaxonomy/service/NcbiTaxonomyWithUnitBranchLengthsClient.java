package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.dsutils.tree.TreeException;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicPhylogenyNode;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyWithUnitBranchLengthsClient implements NcbiTaxonomyWithUnitBranchLengthsService
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyClient.class);

	private NcbiTaxonomyWithUnitBranchLengthsService ncbiTaxonomyWithUnitBranchLengths;

	private static NcbiTaxonomyWithUnitBranchLengthsClient instance;


	public static NcbiTaxonomyWithUnitBranchLengthsClient getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiTaxonomyWithUnitBranchLengthsClient();
			}
		return instance;
		}

	public static NcbiTaxonomyWithUnitBranchLengthsClient getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiTaxonomyWithUnitBranchLengthsClient instance)
		{
		NcbiTaxonomyWithUnitBranchLengthsClient.instance = instance;
		}


	public NcbiTaxonomyWithUnitBranchLengthsClient()
		{

		ApplicationContext ctx = null;
		try
			{
			ctx = NcbiTaxonomyServicesContextFactory.makeNcbiTaxonomyServicesContext();
			}
		catch (IOException e)
			{
			logger.error(e);
			throw new Error(e);
			}

		ncbiTaxonomyWithUnitBranchLengths =
				(NcbiTaxonomyWithUnitBranchLengthsService) ctx.getBean("ncbiTaxonomyWithUnitBranchLengthsService");
		}

	public boolean isDescendant(final Integer ancestor, final Integer descendant) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.isDescendant(ancestor, descendant);
		}

	public double minDistanceBetween(final Integer name1, final Integer name2) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.minDistanceBetween(name1, name2);
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
		//	ncbiTaxonomyWithUnitBranchLengths.setSynonymService(taxonomySynonymService);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.isLeaf(leafId);
		}

	public double getDepthFromRoot(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getDepthFromRoot(taxid);
		}

	public double getGreatestDepthBelow(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getGreatestDepthBelow(taxid);
		}

	public double maxDistance()
		{
		return ncbiTaxonomyWithUnitBranchLengths.maxDistance();
		}

	public String getRelaxedName(final String name)
		{
		return ncbiTaxonomyWithUnitBranchLengths.getRelaxedName(name);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.findMatchingIdsRelaxed(name);
		}

	public Set<Integer> selectAncestors(final Set<Integer> labels, final Integer id)
		{
		return ncbiTaxonomyWithUnitBranchLengths.selectAncestors(labels, id);
		}

	public Set<Integer> getLeafIds()
		{
		return ncbiTaxonomyWithUnitBranchLengths.getLeafIds();
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiTaxonomyWithUnitBranchLengths.getFriendlyLabelMap();
		}

	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths
				.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}

	public boolean isKnown(final Integer value)
		{
		return ncbiTaxonomyWithUnitBranchLengths.isKnown(value);
		}

	public Integer nearestAncestorWithBranchLength(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.nearestAncestorWithBranchLength(id);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths
				.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths
				.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getAncestorPathIds(id);
		}

	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.getAncestorPath(id);
		}

	public Integer findTaxidByName(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengths.findTaxidByNameRelaxed(name);
		}

	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiTaxonomyWithUnitBranchLengths.getCachedNamesForId(id);
		}
	}
