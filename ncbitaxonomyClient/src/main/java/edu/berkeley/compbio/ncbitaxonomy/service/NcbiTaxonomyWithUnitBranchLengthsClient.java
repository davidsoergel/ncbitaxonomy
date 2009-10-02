package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.dsutils.DSStringUtils;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyWithUnitBranchLengthsClient extends NcbiTaxonomyClient
		implements NcbiTaxonomyWithUnitBranchLengthsService
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyClient.class);

	private NcbiTaxonomyWithUnitBranchLengthsExtractor ncbiTaxonomyWithUnitBranchLengthsExtractor;

	private BasicRootedPhylogeny<Integer> extractedTree;


	public void prepare(final Set<Integer> allLabels) throws NoSuchNodeException
		{
		String idString = DSStringUtils.joinSorted(allLabels, ":");

		extractedTree = (BasicRootedPhylogeny<Integer>) CacheManager.get(this, idString);
		//RootedPhylogeny<Integer> theTree = cache.get(sfiIDsBigString);
		if (extractedTree == null)
			{
			extractedTree = ncbiTaxonomyWithUnitBranchLengthsExtractor.prepare(allLabels);

			CacheManager.put(this, idString, extractedTree);

			//	cache.put(sfiIDsBigString, theTree);
			//	saveCache(cache);
			}
		}

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
			logger.error("Error", e);
			throw new Error(e);
			}

		ncbiTaxonomyWithUnitBranchLengthsExtractor =
				(NcbiTaxonomyWithUnitBranchLengthsExtractor) ctx.getBean("ncbiTaxonomyWithUnitBranchLengthsExtractor");
		}

	//** hack
	private Double maxDistance = 1000.;

	public double maxDistance()
		{
		/*if (maxDistance == null)
			{
			maxDistance = 2.0 * getRoot().getGreatestBranchLengthDepthBelow();
			}*/
		return maxDistance;
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
	public Integer nearestAncestorWithBranchLength(final Integer leafId)
		{
		return leafId;
		}


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
