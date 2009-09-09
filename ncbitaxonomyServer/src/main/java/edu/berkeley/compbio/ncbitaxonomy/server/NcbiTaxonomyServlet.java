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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
	@Qualifier("ncbiTaxonomyPhylogeny")
	NcbiTaxonomyPhylogeny ncbiTaxonomyPhylogeny;

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public Collection<String> synonymsOf(final String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.synonymsOf(s);
		}

	public Collection<String> synonymsOfParent(final String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.synonymsOfParent(s);
		}

	public Collection<String> synonymsOfRelaxed(final String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.synonymsOfRelaxed(s);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findMatchingIdsRelaxed(name);
		}

	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getScientificName(taxid);
		}

	public Set<Integer> getTaxIdsWithRank(final String rankName)
		{
		return ncbiTaxonomyPhylogeny.getTaxIdsWithRank(rankName);
		}

	public Integer findTaxidByName(@NotNull final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findTaxidByNameRelaxed(name);
		}

	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		return ncbiTaxonomyPhylogeny.getAncestorPath();
		}


	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getAncestorPathIds(id);
		}


	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiTaxonomyPhylogeny.getCachedNamesForId(id);
		}


	public double getDepthFromRoot(final Integer b) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getDepthFromRoot(b);
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiTaxonomyPhylogeny.getFriendlyLabelMap();
		}


	public double getGreatestDepthBelow(final Integer a)
		{
		return ncbiTaxonomyPhylogeny.getGreatestDepthBelow(a);
		}


	public Set<Integer> getLeafIds()
		{
		return ncbiTaxonomyPhylogeny.getLeafIds();
		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
		{
		return ncbiTaxonomyPhylogeny.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiTaxonomyPhylogeny.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public String getRelaxedName(final String name)
		{
		return ncbiTaxonomyPhylogeny.getRelaxedName(name);
		}


	public boolean isKnown(final Integer leafId)
		{
		return ncbiTaxonomyPhylogeny.isKnown(leafId);
		}


	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.isLeaf(leafId);
		}


	public double maxDistance()
		{
		return ncbiTaxonomyPhylogeny.maxDistance();
		}


	public double minDistanceBetween(final Integer taxIdA, final Integer taxIdB)
		{
		return ncbiTaxonomyPhylogeny.minDistanceBetween(taxIdA, taxIdB);
		}


	public Integer nearestAncestorWithBranchLength(final Integer leafId)
		{
		return ncbiTaxonomyPhylogeny.nearestAncestorWithBranchLength(leafId);
		}


	public void setSynonymService(final TaxonomySynonymService taxonomySynonymService)
		{
		ncbiTaxonomyPhylogeny.setSynonymService(taxonomySynonymService);
		}


	@Override
	public String toString()
		{
		return ncbiTaxonomyPhylogeny.toString();
		}


	@NotNull
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}


/*	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getAncestorPath(id);
		}
*/

	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}


	public boolean isDescendant(final Integer ancestor, final Integer descendant)
		{
		return ncbiTaxonomyPhylogeny.isDescendant(ancestor, descendant);
		}


	public Set<Integer> selectAncestors(final Set<Integer> labels, final Integer id)
		{
		return ncbiTaxonomyPhylogeny.selectAncestors(labels, id);
		}
	}
