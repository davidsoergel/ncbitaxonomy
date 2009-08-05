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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Presents the Hessian proxy as a class requiring no configuration, for useas a Jandy plugin.  Just delegates
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
		return ncbiTaxonomy.synonymsOfRelaxed(name);
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
			logger.error(e);
			throw new Error(e);
			}

		ncbiTaxonomy = (NcbiTaxonomyService) ctx.getBean("ncbiTaxonomyService");
		}

	public String getScientificName(final Integer from) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getScientificName(from);
		}

	public Set<Integer> getTaxIdsWithRank(final String rank)
		{
		return ncbiTaxonomy.getTaxIdsWithRank(rank);
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
		ncbiTaxonomy.setSynonymService(taxonomySynonymService);
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

	public Set<Integer> selectAncestors(final Set<Integer> labels, final Integer id)
		{
		return ncbiTaxonomy.selectAncestors(labels, id);
		}

	public Set<Integer> getLeafIds()
		{
		return ncbiTaxonomy.getLeafIds();
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiTaxonomy.getFriendlyLabelMap();
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
		return ncbiTaxonomy.nearestAncestorWithBranchLength(id);
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

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getAncestorPathIds(id);
		}

	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomy.getAncestorPath(id);
		}

	public Integer findTaxidByName(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomy.findTaxidByNameRelaxed(name);
		}

	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiTaxonomy.getCachedNamesForId(id);
		}
	}
