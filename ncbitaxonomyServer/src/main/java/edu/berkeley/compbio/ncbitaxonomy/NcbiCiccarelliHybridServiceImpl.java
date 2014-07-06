/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.BasicPhylogenyNode;
import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.IntegerGeneratingNodeNamer;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.PhylogenyNode;
import com.davidsoergel.trees.RootedPhylogeny;
import com.davidsoergel.trees.TreeException;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.berkeley.compbio.ncbitaxonomy.service.NcbiCiccarelliHybridService;
import edu.berkeley.compbio.phyloutils.CiccarelliTaxonomyService;
import edu.berkeley.compbio.phyloutils.HybridRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyTypeConverter;
import edu.berkeley.compbio.phyloutils.TaxonMerger;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Provides a view of a tree that extends the Ciccarelli tree from its leaves with subtrees taken from the NCBI
 * taxonomy.  The NCBI taxonomy may contain several strains of a species that is represented in the Ciccarelli tree as a
 * single node.  This tree places these strains below the appropriate node in the Ciccarelli tree.  At higher levels of
 * the tree, only the Ciccarelli topology is considered; the NCBI topology is ignored.
 * <p/>
 * The Ciccarelli tree provides branch lengths, but the NCBI taxonomy does not.  This hybrid tree provides branch
 * lengths according to the Ciccarelli tree.  It navigates the NCBI topology where necessary, but assigns zero length to
 * those branches.  Thus, it is possible to compute a distance between two strains that do not appear explicitly in the
 * Ciccarelli tree, but this distance is a lower bound.  Of course, the distance from a species node to a strain node
 * below it is usually effectively zero anyway, so this lower bound can be expected to be reasonably tight.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service
public class NcbiCiccarelliHybridServiceImpl
		implements NcbiCiccarelliHybridService //TaxonMergingPhylogeny<Integer> //, RootedPhylogeny<Integer>
	{
	private static final Logger logger = Logger.getLogger(NcbiCiccarelliHybridServiceImpl.class);

	@Autowired
	@Qualifier("ncbiTaxonomyPhylogeny")
	private NcbiTaxonomyPhylogeny ncbiTaxonomyPhylogeny;// = NcbiTaxonomyService.getInstance();

	private CiccarelliTaxonomyService ciccarelli = CiccarelliTaxonomyService.getInstance();

	private HybridRootedPhylogeny<Integer> hybridTree = null;

	/*	static
	   {
	   hybridTree = new HybridRootedPhylogeny<Integer>(
			   ncbiTaxonomyService.convertToIntegerIDTree(ciccarelli.getTree()), ncbiTaxonomyService);
	   }*/

	private Map<Integer, String> ciccarelliNames = new HashMap<Integer, String>();


	private static NcbiCiccarelliHybridServiceImpl _instance;
//= new NcbiCiccarelliHybridService();  // only used for testing

	public Map<Integer, String> getFriendlyLabelMap()
		{
		return ncbiTaxonomyPhylogeny.getFriendlyLabelMap();
		}

	public boolean isLeaf(Integer leafId) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.isLeaf(leafId);
		}

	public boolean isKnown(Integer leafId) //throws NoSuchNodeException
	{
	return ncbiTaxonomyPhylogeny.isKnown(leafId);
	}

	// -------------------------- STATIC METHODS --------------------------

	/*	public static NcbiCiccarelliHybridServlet getInjectedInstance()
		 {
		 return getInstance();
		 }

	 public static void setInjectedInstance(NcbiCiccarelliHybridServlet o)
		 {
		 throw new Error("Impossible");
		 }
 */

	public static NcbiCiccarelliHybridServiceImpl getInstance()
		{
		if (_instance == null)
			{
			_instance = new NcbiCiccarelliHybridServiceImpl();
			//	if (!loadCachedInstance)
			//		{
			//makeInstance();
			//		storeCachedInstance();
			//		}
			}

		return _instance;
		}


//	private static String cacheFilename = "/ncbitaxonomy.ciccarellihybrid.cache";


	//@Transactional

	public void init()
		{
		buildHybrid();
		}

	@PostConstruct
	public void buildHybrid()
		{
		if (hybridTree == null)
			{
			//ncbiTaxonomyPhylogeny = NcbiTaxonomyPhylogeny.getInstance();
			//ciccarelli = CiccarelliTaxonomyService.getInstance();
			String className = "edu.berkeley.compbio.ncbitaxonomy.NcbiCiccarelliHybridService";

			BasicRootedPhylogeny<Integer> ciccarelliIntegerTree = (BasicRootedPhylogeny<Integer>) CacheManager
					.get(className + File.separator + "ciccarelliIntegerTree");
			if (ciccarelliIntegerTree == null)
				{

				// REVIEW should NCBI taxids be pushed to leaves, or not?

				final Multimap<String, Integer> nameToIdMap = HashMultimap.create();
				final Multimap<String, Integer> extraNameToIdMap = HashMultimap.create();
				ciccarelliIntegerTree = PhylogenyTypeConverter
						.convertToIDTree(ciccarelli.getTree(), new IntegerGeneratingNodeNamer(10000000, false),
						                 ncbiTaxonomyPhylogeny, nameToIdMap, extraNameToIdMap);
				CacheManager.put(className + File.separator + "ciccarelliIntegerTree", ciccarelliIntegerTree);
				}

			// the root must be node 1, regardless of what children have unknown IDs
			ciccarelliIntegerTree.setPayload(new Integer(1));

			hybridTree = new HybridRootedPhylogeny<Integer>(ciccarelliIntegerTree, ncbiTaxonomyPhylogeny);
			//	_instance = new NcbiCiccarelliHybridServlet();
			//_instance.saveState();
			}


		try
			{
			for (PhylogenyNode<String> n : ciccarelli.getTree().getLeaves())
				{
				try
					{
					String name = n.getPayload();
					int id = ncbiTaxonomyPhylogeny.findTaxidByName(name);
					}
				catch (NoSuchNodeException e)
					{
					logger.error("Leaf with unknown taxid: " + n.getPayload());
					}
				}

			for (PhylogenyNode<String> n : ciccarelli.getTree().getUniqueIdToNodeMap().values())
				{
				try
					{
					String name = n.getPayload();

					// names like "Vibrio subclade" would be relaxed to "Vibrio", which would be wrong; ignore these
					if (name != null && !name.contains("subclade"))
						{
						int id = ncbiTaxonomyPhylogeny.findTaxidByName(name);
						ciccarelliNames.put(id, name);
						}
					}
				catch (NoSuchNodeException e)
					{
					// too bad, ignore that one; probably an internal node anyway
					}
				}
			}
		catch (Throwable e)
			{
			logger.error("Error", e);
			}
		readStateIfAvailable();
		}
/*
	public static void main(String[] argv)
		{
		ncbiTaxonomyService = NcbiTaxonomyService.getInstance();
		ciccarelli = CiccarelliTaxonomyService.getInstance();
		// add integer ids to unknown nodes

		try
			{
			RootedPhylogeny<Integer> theTree = PhylogenyTypeConverter
					.convertToIDTree(ciccarelli.getTree(), new IntegerNodeNamer(10000000), ncbiTaxonomyService,
					                 new HashMultimap<String, Integer>());

			StringBuffer sb = new StringBuffer();
			String prefix = argv[1];
			String tab = argv[2];
			prefix = prefix.replaceAll(Matcher.quoteReplacement("\\n"), "\n");
			prefix = prefix.replaceAll(Matcher.quoteReplacement("\\t"), "\t");
			prefix = prefix.replaceAll("\"", "");
			prefix = prefix.replaceAll("\'", "");
			tab = tab.replaceAll(Matcher.quoteReplacement("\\n"), "\n");
			tab = tab.replaceAll(Matcher.quoteReplacement("\\t"), "\t");
			tab = tab.replaceAll("\"", "");
			tab = tab.replaceAll("\'", "");

			theTree.toNewick(sb, prefix, tab, 0, 0);

			//	System.out.println(sb);

			//FileOutputStream foo = new FileOutputStream(argv[2]);
			FileWriter fw = new FileWriter(argv[3]);
			fw.write(sb.toString());
			fw.close();
			}
		catch (IOException e)
			{
			logger.error("Error", e);
			}
		}
*/
	// ------------------

	// we have to maintain the mapping from species names used in the Ciccarelli tree to NCBI ids

	public NcbiCiccarelliHybridServiceImpl()
		{

		}


	public BasicRootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold)
			throws NoSuchNodeException, TreeException
		{
		return getRandomSubtree(numTaxa, mergeThreshold, null);
		}


	public RootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Integer exceptDescendantsOf)
			throws TreeException, NoSuchNodeException
		{
		return getRandomSubtree(numTaxa, null, exceptDescendantsOf);
		}

	public BasicRootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold,
	                                                      Integer exceptDescendantsOf)
			throws TreeException, NoSuchNodeException
		{
		Set<Integer> mergedIds;
		if (mergeThreshold != null)
			{
			Map<Integer, Set<Integer>> mergeIdSets =
					TaxonMerger.merge(hybridTree.getLeafValues(), this, mergeThreshold);

			mergedIds = mergeIdSets.keySet();
			}
		else
			{
			mergedIds = hybridTree.getLeafValues();
			}

		if (exceptDescendantsOf != null)
			{
			for (Iterator<Integer> iter = mergedIds.iterator(); iter.hasNext();)
				{
				Integer id = iter.next();
				if (isDescendant(exceptDescendantsOf, id))
					{
					iter.remove();
					}
				}
			}

		DSCollectionUtils.retainRandom(mergedIds, numTaxa);
		return extractTreeWithLeafIDs(mergedIds, false, false);
		}


	private Map<String, Integer> stringNearestKnownAncestorCache = new HashMap<String, Integer>();
	private Map<Integer, Integer> integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();

	public Integer nearestKnownAncestor(String name) throws NoSuchNodeException
		{
		Integer result = stringNearestKnownAncestorCache.get(name);
		if (result == null)
			{
			result = hybridTree.nearestKnownAncestor(ncbiTaxonomyPhylogeny.findTaxidByNameRelaxed(name));
			stringNearestKnownAncestorCache.put(name, result);
			}
		return result;
		}

	public Integer nearestKnownAncestor(Integer id) throws NoSuchNodeException
		{
		Integer result = integerNearestKnownAncestorCache.get(id);
		if (result == null)
			{
			result = hybridTree.nearestKnownAncestor(id);
			integerNearestKnownAncestorCache.put(id, result);
			}
		return result;
		}

	public Integer nearestAncestorWithBranchLength(Integer id) throws NoSuchNodeException
		{
		Integer result = integerNearestAncestorWithBranchLengthCache.get(id);
		if (result == null)
			{
			result = hybridTree.nearestAncestorWithBranchLength(id);
			integerNearestAncestorWithBranchLengthCache.put(id, result);
			}
		return result;
		}

	private void readStateIfAvailable()
		{

		stringNearestKnownAncestorCache = CacheManager
				.getAccumulatingMap(this, "stringNearestKnownAncestorCache"); //, new HashMap<String, Integer>());
		integerNearestKnownAncestorCache = CacheManager
				.getAccumulatingMap(this, "integerNearestKnownAncestorCache"); //, new HashMap<Integer, Integer>());
		integerNearestAncestorWithBranchLengthCache = CacheManager.getAccumulatingMap(this,
		                                                                              "integerNearestAncestorWithBranchLengthCache"); //,new HashMap<Integer, Integer>());
/*
		if (stringNearestKnownAncestorCache == null)
			{
			stringNearestKnownAncestorCache = new HashMap<String, Integer>();
			}
		if (integerNearestKnownAncestorCache == null)
			{
			integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
			}
		if (integerNearestAncestorWithBranchLengthCache == null)
			{
			integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();
			}*/
		}

/*	public RootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids) throws PhyloUtilsException
		{
		//return ciccarelli.extractTreeWithLeafIDs(CollectionUtils.mapAll(ciccarelliNames, ids));
		return hybridTree.extractTreeWithLeafIDs(ids, false);
		}*/

	/*
	 public RootedPhylogeny<Integer> extractTreeWithLeafIDs(Collection<Integer> ids) throws NoSuchNodeException
		 {
		 return hybridTree.extractTreeWithLeafIDs(ids);
		 }
 */

	@Transactional
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids, boolean ignoreAbsentNodes,
	                                                            boolean includeInternalBranches,
	                                                            AbstractRootedPhylogeny.MutualExclusionResolutionMode mode)
			throws NoSuchNodeException //, NodeNamer<Integer> namer
	{
	return hybridTree.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches, mode);
	}

	@Transactional
	public BasicRootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids, boolean ignoreAbsentNodes,
	                                                            boolean includeInternalBranches)
			throws NoSuchNodeException //, NodeNamer<Integer> namer
	{
	return hybridTree.extractTreeWithLeafIDs(ids, ignoreAbsentNodes, includeInternalBranches);
	}
	/*
	public void flushCaches()
		{
		hybridTree.saveState();
		CacheManager.merge(this, "stringNearestKnownAncestorCache", stringNearestKnownAncestorCache);
		CacheManager.merge(this, "integerNearestKnownAncestorCache", integerNearestKnownAncestorCache);
		CacheManager.merge(this, "integerNearestAncestorWithBranchLengthCache",
		                   integerNearestAncestorWithBranchLengthCache);


	   try
		   {
		   File cacheFile = new File(EnvironmentUtils.getCacheRoot() + cacheFilename);
		   cacheFile.getParentFile().mkdirs();
		   FileOutputStream fout = new FileOutputStream(cacheFile);
		   ObjectOutputStream oos = new ObjectOutputStream(fout);
		   oos.writeObject(stringNearestKnownAncestorCache);
		   oos.writeObject(integerNearestKnownAncestorCache);
		   oos.writeObject(
				   integerNearestAncestorWithBranchLengthCache);			//	oos.writeObject(nearestKnownAncestorCache);
		   oos.close();
		   }
	   catch (Exception e)
		   {
		   logger.error("Error", e);
		   }
		}*/


	public double minDistanceBetween(Integer id1, Integer id2) throws NoSuchNodeException
		{
		id1 = nearestKnownAncestor(id1); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		id2 = nearestKnownAncestor(id2); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}

	public double getDepthFromRoot(Integer id2) throws NoSuchNodeException
		{
		Integer id1 = hybridTree.getRootPhylogeny().getRoot().getPayload();
		id2 = nearestKnownAncestor(id2);
		return exactDistanceBetween(id1, id2);
		//return stringTaxonomyService.minDistanceBetween(intToNodeMap.get(a), intToNodeMap.get(b));
		//	return exactDistanceBetween(name1, name2);
		}
/*	public Double minDistanceBetween(PhylogenyNode<Integer> node1, PhylogenyNode<Integer> node2)
			throws PhyloUtilsException
		{
		int id1 = nearestKnownAncestor(node1); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		int id2 = nearestKnownAncestor(node2); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}*/


	public Double minDistanceBetween(String name1, String name2) throws NoSuchNodeException
		{
		int id1 = nearestKnownAncestor(
				name1); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		int id2 = nearestKnownAncestor(
				name2); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}

	public Integer findTaxidByName(@NotNull String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findTaxidByName(name);
		}

	public Integer findTaxidByNameRelaxed(String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyPhylogeny.findTaxidByNameRelaxed(name);
		}

	public Set<String> getCachedNamesForId(Integer id)
		{
		return ncbiTaxonomyPhylogeny.getCachedNamesForId(id);
		}


/*	public TaxonMergingPhylogeny<Integer> getTree()
		{
		return hybridTree;
		}*/


	public double exactDistanceBetween(Integer taxidA, Integer taxidB) throws NoSuchNodeException
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		if (ciccarelliName1 == null)
			{
			throw new NoSuchNodeException("No such element: " + taxidA);
			}
		String ciccarelliName2 = ciccarelliNames.get(taxidB);
		if (ciccarelliName2 == null)
			{
			throw new NoSuchNodeException("No such element: " + taxidB);
			}

		return ciccarelli.exactDistanceBetween(ciccarelliName1, ciccarelliName2);
		}

	public boolean isDescendant(@NotNull Integer ancestor, @NotNull Integer descendant) throws NoSuchNodeException
		{
		// PERF cache this
		return hybridTree.isDescendant(ancestor, descendant);
		}

	public Set<Integer> selectAncestors(final Collection<Integer> labels, final Integer id)
		{
		return hybridTree.selectAncestors(labels, id);
		}

	public List<Integer> getAncestorPathIds(final Integer id) throws NoSuchNodeException
		{
		return hybridTree.getAncestorPathIds(id);
		}

	/*	public List<PhylogenyNode<Integer>> getAncestorPath(final Integer id) throws NoSuchNodeException
		 {
		 return hybridTree.getAncestorPath(id);
		 }
 */
	public List<BasicPhylogenyNode<Integer>> getAncestorPathAsBasic(final Integer id) throws NoSuchNodeException
		{
		return hybridTree.getAncestorPathAsBasic(id);
		}

	/*	public boolean isDescendant(PhylogenyNode<Integer> ancestor, PhylogenyNode<Integer> descendant)
			   throws PhyloUtilsException
		   {
		   hybridTree.isDescendant(ancestor, descendant);
		   }*/

	public double getGreatestDepthBelow(Integer taxidA) throws NoSuchNodeException
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		if (ciccarelliName1 == null)
			{
			throw new NoSuchNodeException("No such element: " + taxidA);
			}

		return ciccarelli.getGreatestDepthBelow(ciccarelliName1);
		}


	public double getLargestLengthSpan(Integer taxidA) throws NoSuchNodeException
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		if (ciccarelliName1 == null)
			{
			throw new NoSuchNodeException("No such element: " + taxidA);
			}

		return ciccarelli.getLargestLengthSpan(ciccarelliName1);
		}

	private Double maxDistance = null;

	public double maxDistance()
		{
		if (maxDistance == null)
			{
			maxDistance = ciccarelli.maxDistance(); //2.0 * getRoot().getGreatestDepthBelow();
			}
		return maxDistance;
		}

	public void printDepthsBelow()
		{
		}

	public PhylogenyNode<Integer> getRoot()
		{
		throw new NotImplementedException();  //hybridTree.getRoot();
		}


	public PhylogenyNode<Integer> nearestAncestorWithBranchLength(PhylogenyNode<Integer> id) throws PhyloUtilsException
		{
		return null;
		}

	public RootedPhylogeny<Integer> extractTreeWithLeaves(Collection<PhylogenyNode<Integer>> ids)
			throws PhyloUtilsException
		{
		return null;
		}


	public void setSynonymService(TaxonomySynonymService taxonomySynonymService)
		{
		logger.warn(
				"NCBI/Ciccarelli hybrid taxonomy doesn't currently use other synonym services for any purpose; ignoring");
		}

	@Override
	public String toString()
		{
		String shortname = getClass().getName();
		shortname = shortname.substring(shortname.lastIndexOf(".") + 1);
		return shortname;
		}
/*
	public RootedPhylogeny<Integer> findSubtreeByName(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> findSubtreeByNameRelaxed(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}
*/

	public BasicRootedPhylogeny<Integer> findCompactSubtreeWithIds(Set<Integer> matchingIds, String name)
			throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public String getRelaxedName(String name)
		{
		throw new NotImplementedException();
		}
/*
	public Integer findTaxIdOfShallowestLeaf(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> findTreeForName(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public int getNumNodesForName(String name)
		{
		throw new NotImplementedException();
		}*/


	public Set<Integer> findMatchingIds(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public Set<Integer> findMatchingIdsRelaxed(String name) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public Set<Integer> getLeafIds()
		{
		throw new NotImplementedException();
		}


	public Integer getLeafAtApproximateDistance(final Integer aId, final double minDesiredTreeDistance,
	                                            final double maxDesiredTreeDistance) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		//return hybridTree.getLeafAtApproximateDistance(aId, minDesiredTreeDistance, maxDesiredTreeDistance);
		}

	public String getScientificName(final Integer taxid) throws NoSuchNodeException
		{
		throw new NotImplementedException();
		}

	public Collection<String> getAllNamesForIds(final Set<Integer> ids)
		{
		throw new NotImplementedException();
		}
	}
