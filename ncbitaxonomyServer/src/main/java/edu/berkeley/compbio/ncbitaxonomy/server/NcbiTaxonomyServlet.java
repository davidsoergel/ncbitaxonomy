package edu.berkeley.compbio.ncbitaxonomy.server;

import com.caucho.hessian.server.HessianServlet;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.dsutils.tree.TreeException;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyPhylogeny;
import edu.berkeley.compbio.ncbitaxonomy.service.NcbiTaxonomyService;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicPhylogenyNode;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service
public class NcbiTaxonomyServlet extends HessianServlet implements NcbiTaxonomyService
	{
	@Autowired
	NcbiTaxonomyPhylogeny thePhylogeny;

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return thePhylogeny.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return thePhylogeny.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return thePhylogeny.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return thePhylogeny.findMatchingIdsRelaxed(name);
		}

	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		return thePhylogeny.getScientificName(taxid);
		}

	public Set<Integer> getTaxIdsWithRank(final String rankName)
		{
		return thePhylogeny.getTaxIdsWithRank(rankName);
		}

	public Integer findTaxidByName(final String name) throws NoSuchNodeException
		{
		return thePhylogeny.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return thePhylogeny.findTaxidByNameRelaxed(name);
		}

	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		return thePhylogeny.getAncestorPath();
		}


	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return thePhylogeny.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return thePhylogeny.getAncestorPathIds(id);
		}


	public Set<String> getCachedNamesForId(final Integer id)
		{
		return thePhylogeny.getCachedNamesForId(id);
		}


	public double getDepthFromRoot(final Integer b) throws NoSuchNodeException
		{
		return thePhylogeny.getDepthFromRoot(b);
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return thePhylogeny.getFriendlyLabelMap();
		}


	public double getGreatestDepthBelow(final Integer a)
		{
		return thePhylogeny.getGreatestDepthBelow(a);
		}


	public Set<Integer> getLeafIds()
		{
		return thePhylogeny.getLeafIds();
		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
		{
		return thePhylogeny.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return thePhylogeny.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public String getRelaxedName(final String name)
		{
		return thePhylogeny.getRelaxedName(name);
		}


	public boolean isKnown(final Integer leafId)
		{
		return thePhylogeny.isKnown(leafId);
		}


	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return thePhylogeny.isLeaf(leafId);
		}


	public double maxDistance()
		{
		return thePhylogeny.maxDistance();
		}


	public double minDistanceBetween(final Integer taxIdA, final Integer taxIdB)
		{
		return thePhylogeny.minDistanceBetween(taxIdA, taxIdB);
		}


	public Integer nearestAncestorWithBranchLength(final Integer leafId)
		{
		return thePhylogeny.nearestAncestorWithBranchLength(leafId);
		}


	public void setSynonymService(final TaxonomySynonymService taxonomySynonymService)
		{
		thePhylogeny.setSynonymService(taxonomySynonymService);
		}


	@Override
	public String toString()
		{
		return thePhylogeny.toString();
		}


	@NotNull
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return thePhylogeny.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}


	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return thePhylogeny.getAncestorPath(id);
		}


	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return thePhylogeny.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}


	public boolean isDescendant(final Integer ancestor, final Integer descendant)
		{
		return thePhylogeny.isDescendant(ancestor, descendant);
		}


	public Set<Integer> selectAncestors(final Set<Integer> labels, final Integer id)
		{
		return thePhylogeny.selectAncestors(labels, id);
		}
	}
