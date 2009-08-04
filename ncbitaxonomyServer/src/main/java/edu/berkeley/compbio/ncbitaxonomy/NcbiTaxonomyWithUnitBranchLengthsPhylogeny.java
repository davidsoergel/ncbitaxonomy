package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.dsutils.DSStringUtils;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RequiresPreparationTaxonomyService;
import edu.berkeley.compbio.phyloutils.TaxonomyService;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyWithUnitBranchLengthsPhylogeny extends NcbiTaxonomyPhylogeny
		implements RequiresPreparationTaxonomyService<Integer> // TaxonomyService<Integer>,
	{
	//private NcbiTaxonomyService taxonomyService = NcbiTaxonomyService.getInstance();

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyWithUnitBranchLengthsPhylogeny.class);
	private BasicRootedPhylogeny<Integer> extractedTree;

	private static NcbiTaxonomyWithUnitBranchLengthsPhylogeny instance;


	public static NcbiTaxonomyWithUnitBranchLengthsPhylogeny getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiTaxonomyWithUnitBranchLengthsPhylogeny();
			}
		return instance;
		}

	public static NcbiTaxonomyWithUnitBranchLengthsPhylogeny getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiTaxonomyWithUnitBranchLengthsPhylogeny instance)
		{
		NcbiTaxonomyWithUnitBranchLengthsPhylogeny.instance = instance;
		}

	private Double maxDistance = 1000.;

	public double maxDistance()
		{
		/*if (maxDistance == null)
			{
			maxDistance = 2.0 * getRoot().getGreatestBranchLengthDepthBelow();
			}*/
		return maxDistance;
		}

	// ** this whole class is kind of a hack


	public void prepare(Set<Integer> allLabels) throws NoSuchNodeException
		{
		String idString = toString() + ":" + DSStringUtils.joinSorted(allLabels, ":");

		extractedTree = (BasicRootedPhylogeny<Integer>) CacheManager.get(this, idString);
		if (extractedTree == null)
			{
			extractedTree = extractTreeWithLeafIDs(allLabels, false, true,
			                                       AbstractRootedPhylogeny.MutualExclusionResolutionMode.BOTH);
			extractedTree.setAllBranchLengthsTo(1.0);
			CacheManager.put(this, idString, extractedTree);
			}
		}


	@Override
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(final Set<Integer> ids, final boolean ignoreAbsentNodes,
	                                                            final boolean includeInternalBranches,
	                                                            AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException
		{
		// we must include the internal branches for the sake of consistent branch lengths
		BasicRootedPhylogeny<Integer> result = super.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, true, mode);
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


	// PERF would be far better to extract the whole tree in advance
	@Override
	public double distanceBetween(final PhylogenyNode<Integer> a, final PhylogenyNode<Integer> b)
		{


		try
			{
			/*RootedPhylogeny<Integer> result = super.extractTreeWithLeaves(DSCollectionUtils.setOf(a, b), true,
			                                                              MutualExclusionResolutionMode.BOTH);
			result.setAllBranchLengthsTo(1.0);

			return result.distanceBetween(a.getValue(), b.getValue());*/
			return extractedTree.distanceBetween(a.getPayload(), b.getPayload());
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}
		}

	@Override
	public double distanceBetween(final Integer taxIdA, final Integer taxIdB)
		{
		try
			{
			/*
			RootedPhylogeny<Integer> result =

					super.extractTreeWithLeafIDs(DSCollectionUtils.setOf(taxIdA, taxIdB), false, true,
					                             MutualExclusionResolutionMode.BOTH);
			result.setAllBranchLengthsTo(1.0);

			return result.distanceBetween(taxIdA, taxIdB); */
			return extractedTree.distanceBetween(taxIdA, taxIdB);
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Error", e);
			throw new Error(e);
			}
		}

	@Override
	public Double minDistanceBetween(final PhylogenyNode<Integer> node1, final PhylogenyNode<Integer> node2)
			throws PhyloUtilsException
		{
		return distanceBetween(node1, node2);
		}

	@Override
	public double minDistanceBetween(final Integer taxIdA, final Integer taxIdB)
		{
		return distanceBetween(taxIdA, taxIdB);
		}
	}
