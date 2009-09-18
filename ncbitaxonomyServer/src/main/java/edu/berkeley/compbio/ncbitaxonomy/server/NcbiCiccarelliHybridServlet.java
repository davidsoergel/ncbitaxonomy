package edu.berkeley.compbio.ncbitaxonomy.server;

import com.caucho.hessian.server.HessianServlet;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.dsutils.tree.TreeException;
import edu.berkeley.compbio.ncbitaxonomy.service.NcbiCiccarelliHybridService;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicPhylogenyNode;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NcbiCiccarelliHybridServlet extends HessianServlet implements NcbiCiccarelliHybridService
	{
	@Autowired
	//@Qualifier("ncbiCiccarelliHybridServiceImpl")
			NcbiCiccarelliHybridService ncbiCiccarelliHybridServiceImpl;

	public double exactDistanceBetween(final Integer taxidA, final Integer taxidB) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.exactDistanceBetween(taxidA, taxidB);
		}

	public Integer nearestKnownAncestor(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.nearestKnownAncestor(id);
		}

	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            final AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl
				.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		}

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(final Set<Integer> matchingIds, final String name)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.findCompactSubtreeWithIds(matchingIds, name);
		}

	public Set<Integer> findMatchingIds(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.findMatchingIds(name);
		}

	public Set<Integer> findMatchingIdsRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.findMatchingIdsRelaxed(name);
		}


	public Integer findTaxidByName(@NotNull final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(final String name) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.findTaxidByNameRelaxed(name);
		}


	@NotNull
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getAncestorPathAsBasic(id);
		}

	@NotNull
	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getAncestorPathIds(id);
		}


	public Set<String> getCachedNamesForId(final Integer id)
		{
		return ncbiCiccarelliHybridServiceImpl.getCachedNamesForId(id);
		}


	public double getDepthFromRoot(final Integer b) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getDepthFromRoot(b);
		}

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiCiccarelliHybridServiceImpl.getFriendlyLabelMap();
		}


	public double getGreatestDepthBelow(final Integer a) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getGreatestDepthBelow(a);
		}


	public Set<Integer> getLeafIds()
		{
		return ncbiCiccarelliHybridServiceImpl.getLeafIds();
		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold)
			throws TreeException, NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getRandomSubtree(numTaxa, mergeThreshold);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(final int numTaxa, final Double mergeThreshold,
	                                                      final Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		return ncbiCiccarelliHybridServiceImpl.getRandomSubtree(numTaxa, mergeThreshold, exceptDescendantsOf);
		}

	public String getRelaxedName(final String name)
		{
		return ncbiCiccarelliHybridServiceImpl.getRelaxedName(name);
		}


	public boolean isKnown(final Integer leafId)
		{
		return ncbiCiccarelliHybridServiceImpl.isKnown(leafId);
		}


	public boolean isLeaf(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.isLeaf(leafId);
		}


	public double maxDistance()
		{
		return ncbiCiccarelliHybridServiceImpl.maxDistance();
		}


	public double minDistanceBetween(final Integer taxIdA, final Integer taxIdB) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.minDistanceBetween(taxIdA, taxIdB);
		}


	public Integer nearestAncestorWithBranchLength(final Integer leafId) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.nearestAncestorWithBranchLength(leafId);
		}


	public void setSynonymService(final TaxonomySynonymService taxonomySynonymService)
		{
		ncbiCiccarelliHybridServiceImpl.setSynonymService(taxonomySynonymService);
		}


	@Override
	public String toString()
		{
		return ncbiCiccarelliHybridServiceImpl.toString();
		}


	@NotNull
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches)
			throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
		}


/*	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.getAncestorPath(id);
		}
*/

	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl
				.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}


	public boolean isDescendant(final Integer ancestor, final Integer descendant) throws NoSuchNodeException
		{
		return ncbiCiccarelliHybridServiceImpl.isDescendant(ancestor, descendant);
		}


	public Set<Integer> selectAncestors(final Collection<Integer> labels, final Integer id)
		{
		return ncbiCiccarelliHybridServiceImpl.selectAncestors(labels, id);
		}
	}
