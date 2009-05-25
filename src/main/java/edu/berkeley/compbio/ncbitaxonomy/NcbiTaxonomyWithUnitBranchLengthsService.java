package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonomyService;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyWithUnitBranchLengthsService extends NcbiTaxonomyService implements TaxonomyService<Integer>
	{
	// BAD hack

	@Override
	public RootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                       final boolean includeInternalBranches,
	                                                       MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		RootedPhylogeny<Integer> result =
				super.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
		result.setAllBranchLengthsTo(1.0);
		return result;
		}

	@Override
	public PhylogenyNode<Integer> nearestAncestorWithBranchLength() throws NoSuchNodeException
		{
		return this;
		}

	@Override
	public PhylogenyNode<Integer> nearestAncestorWithBranchLength(final PhylogenyNode<Integer> id)
		{
		return id;
		}

	@Override
	public Integer nearestAncestorWithBranchLength(final Integer leafId)
		{
		return leafId;
		}
	}
