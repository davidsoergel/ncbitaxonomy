package edu.berkeley.compbio.ncbitaxonomy.server;

import com.caucho.hessian.server.HessianServlet;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.dsutils.tree.TreeException;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyWithUnitBranchLengthsPhylogeny;
import edu.berkeley.compbio.ncbitaxonomy.service.NcbiTaxonomyService;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicPhylogenyNode;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service
public class NcbiTaxonomyWithUnitBranchLengthsServlet extends HessianServlet implements NcbiTaxonomyService
	{
	@Autowired
	@Qualifier("ncbiTaxonomyWithUnitBranchLengthsPhylogeny")
	NcbiTaxonomyWithUnitBranchLengthsPhylogeny ncbiTaxonomyWithUnitBranchLengthsPhylogeny;

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny
				.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.findMatchingIdsRelaxed(name);
		}


	public Set<Integer> getTaxIdsWithRank(final String rankName)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getTaxIdsWithRank(rankName);
		}

	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getScientificName(taxid);
		}

	public Integer findTaxidByName(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.findTaxidByNameRelaxed(name);
		}

	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getAncestorPath();
		}


	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getAncestorPathIds(id);
		}


	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getCachedNamesForId(id);
		}


	public double getDepthFromRoot(final Integer b) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getDepthFromRoot(b);
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getFriendlyLabelMap();
		}


	public double getGreatestDepthBelow(final Integer a)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getGreatestDepthBelow(a);
		}


	public Set<Integer> getLeafIds()
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getLeafIds();
		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny
				.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public String getRelaxedName(final String name)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getRelaxedName(name);
		}


	public boolean isKnown(final Integer leafId)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.isKnown(leafId);
		}


	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.isLeaf(leafId);
		}


	public double maxDistance()
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.maxDistance();
		}


	public double minDistanceBetween(final Integer taxIdA, final Integer taxIdB)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.minDistanceBetween(taxIdA, taxIdB);
		}


	public Integer nearestAncestorWithBranchLength(final Integer leafId)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.nearestAncestorWithBranchLength(leafId);
		}


	public void setSynonymService(final TaxonomySynonymService taxonomySynonymService)
		{
		ncbiTaxonomyWithUnitBranchLengthsPhylogeny.setSynonymService(taxonomySynonymService);
		}


	@Override
	public String toString()
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.toString();
		}


	@NotNull
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny
				.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}


	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.getAncestorPath(id);
		}


	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny
				.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}


	public boolean isDescendant(final Integer ancestor, final Integer descendant)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.isDescendant(ancestor, descendant);
		}


	public Set<Integer> selectAncestors(final Set<Integer> labels, final Integer id)
		{
		return ncbiTaxonomyWithUnitBranchLengthsPhylogeny.selectAncestors(labels, id);
		}
	}
