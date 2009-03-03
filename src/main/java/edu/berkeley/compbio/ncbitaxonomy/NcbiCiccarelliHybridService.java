/*
 * Copyright (c) 2001-2008 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * dev@davidsoergel.com
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of any contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.berkeley.compbio.ncbitaxonomy;

import edu.berkeley.compbio.phyloutils.CiccarelliTaxonomyService;
import edu.berkeley.compbio.phyloutils.HybridRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.IntegerNodeNamer;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonomyService;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


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
public class NcbiCiccarelliHybridService
		implements TaxonomyService<Integer> //TaxonMergingPhylogeny<Integer> //, RootedPhylogeny<Integer>
	{
	private static final Logger logger = Logger.getLogger(NcbiCiccarelliHybridService.class);

	private static NcbiTaxonomyService ncbiTaxonomyService;// = NcbiTaxonomyService.getInstance();
	private static CiccarelliTaxonomyService ciccarelli;// = CiccarelliUtils.getInstance();

	private static HybridRootedPhylogeny<Integer> hybridTree;

	/*	static
	   {
	   hybridTree = new HybridRootedPhylogeny<Integer>(
			   ncbiTaxonomyService.convertToIntegerIDTree(ciccarelli.getTree()), ncbiTaxonomyService);
	   }*/

	private Map<Integer, String> ciccarelliNames = new HashMap<Integer, String>();


	private static NcbiCiccarelliHybridService _instance;//= new NcbiCiccarelliHybridService();


	// -------------------------- STATIC METHODS --------------------------

	public static NcbiCiccarelliHybridService getInstance()
		{
		if (_instance == null)
			{
			//	if (!loadCachedInstance)
			//		{
			makeInstance();
			//		storeCachedInstance();
			//		}
			}

		return _instance;
		}

	//@Transactional
	public static void makeInstance()
		{
		ncbiTaxonomyService = NcbiTaxonomyService.getInstance();
		ciccarelli = CiccarelliTaxonomyService.getInstance();
		RootedPhylogeny<Integer> ciccarelliIntegerTree = ncbiTaxonomyService
				.convertToIDTree(ciccarelli.getTree(), new IntegerNodeNamer(10000000), ncbiTaxonomyService);

		// the root must be node 1, regardless of what children have unknown IDs
		ciccarelliIntegerTree.setValue(new Integer(1));

		hybridTree = new HybridRootedPhylogeny<Integer>(ciccarelliIntegerTree, ncbiTaxonomyService);
		_instance = new NcbiCiccarelliHybridService();
		_instance.saveState();
		}

	// ------------------

	// we have to maintain the mapping from species names used in the Ciccarelli tree to NCBI ids

	public NcbiCiccarelliHybridService()
		{
		try
			{
			for (PhylogenyNode<String> n : ciccarelli.getTree().getLeaves())
				{
				try
					{
					String name = n.getValue();
					int id = ncbiTaxonomyService.findTaxidByName(name);
					}
				catch (NcbiTaxonomyException e)
					{
					logger.error("Leaf with unknown taxid: " + n.getValue());
					}
				}

			for (PhylogenyNode<String> n : ciccarelli.getTree().getUniqueIdToNodeMap())
				{
				try
					{
					String name = n.getValue();

					// names like "Vibrio subclade" would be relaxed to "Vibrio", which would be wrong; ignore these
					if (!name.contains("subclade"))
						{
						int id = ncbiTaxonomyService.findTaxidByName(name);
						ciccarelliNames.put(id, name);
						}
					}
				catch (NcbiTaxonomyException e)
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


	private Map<String, Integer> stringNearestKnownAncestorCache = new HashMap<String, Integer>();
	private Map<Integer, Integer> integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();

	public Integer nearestKnownAncestor(String name) throws PhyloUtilsException
		{
		Integer result = stringNearestKnownAncestorCache.get(name);
		if (result == null)
			{
			result = hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByNameRelaxed(name));
			stringNearestKnownAncestorCache.put(name, result);
			}
		return result;
		}

	public Integer nearestKnownAncestor(Integer id) throws PhyloUtilsException
		{
		Integer result = integerNearestKnownAncestorCache.get(id);
		if (result == null)
			{
			result = hybridTree.nearestKnownAncestor(id);
			integerNearestKnownAncestorCache.put(id, result);
			}
		return result;
		}

	public Integer nearestAncestorWithBranchLength(Integer id) throws PhyloUtilsException
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
		try
			{
			FileInputStream fin = new FileInputStream("/tmp/edu.berkeley.compbio.ncbitaxonomy.ciccarellihybrid.cache");
			ObjectInputStream ois = new ObjectInputStream(fin);
			stringNearestKnownAncestorCache = (Map<String, Integer>) ois.readObject();
			integerNearestKnownAncestorCache = (Map<Integer, Integer>) ois.readObject();
			integerNearestAncestorWithBranchLengthCache = (Map<Integer, Integer>) ois.readObject();
			//nearestKnownAncestorCache = (Map<Integer, Integer>) ois.readObject();
			ois.close();
			}
		catch (IOException e)
			{// no problem
			logger.warn("Failed to read NCBI/Ciccarelli hybrid taxonomy cache; will query database from scratch");
			stringNearestKnownAncestorCache = new HashMap<String, Integer>();
			integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
			integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();
			}
		catch (ClassNotFoundException e)
			{// no problem
			logger.warn("Failed to read NCBI/Ciccarelli hybrid taxonomy cache; will query database from scratch");
			stringNearestKnownAncestorCache = new HashMap<String, Integer>();
			integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
			integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();
			}
		}

/*	public RootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids) throws PhyloUtilsException
		{
		//return ciccarelli.extractTreeWithLeafIDs(CollectionUtils.mapAll(ciccarelliNames, ids));
		return hybridTree.extractTreeWithLeafIDs(ids, false);  // ** why was this true before??
		}*/

	public RootedPhylogeny<Integer> extractTreeWithLeafIDs(Collection<Integer> ids) throws PhyloUtilsException
		{
		return hybridTree.extractTreeWithLeafIDs(ids);
		}

	public RootedPhylogeny<Integer> extractTreeWithLeafIDs(Collection<Integer> ids, boolean ignoreAbsentNodes)
			throws PhyloUtilsException
		{
		return hybridTree.extractTreeWithLeafIDs(ids, ignoreAbsentNodes);
		}

	public void saveState()
		{
		hybridTree.saveState();
		try
			{
			FileOutputStream fout =
					new FileOutputStream("/tmp/edu.berkeley.compbio.ncbitaxonomy.ciccarellihybrid.cache");
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
		}


	public Double minDistanceBetween(Integer id1, Integer id2) throws PhyloUtilsException
		{
		id1 = nearestKnownAncestor(id1); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		id2 = nearestKnownAncestor(id2); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}


	public Double minDistanceBetween(String name1, String name2) throws PhyloUtilsException
		{
		int id1 = nearestKnownAncestor(
				name1); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		int id2 = nearestKnownAncestor(
				name2); //hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}

	public Integer findTaxidByName(String name) throws NcbiTaxonomyException
		{
		return ncbiTaxonomyService.findTaxidByName(name);
		}


/*	public TaxonMergingPhylogeny<Integer> getTree()
		{
		return hybridTree;
		}*/


	public double exactDistanceBetween(Integer taxidA, Integer taxidB)
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		if (ciccarelliName1 == null)
			{
			throw new NoSuchElementException("No such element: " + taxidA);
			}
		String ciccarelliName2 = ciccarelliNames.get(taxidB);
		if (ciccarelliName2 == null)
			{
			throw new NoSuchElementException("No such element: " + taxidB);
			}

		return ciccarelli.exactDistanceBetween(ciccarelliName1, ciccarelliName2);
		}

	public boolean isDescendant(@NotNull Integer ancestor, @NotNull Integer descendant) throws PhyloUtilsException
		{
		// PERF cache this
		return hybridTree.isDescendant(ancestor, descendant);
		}

	public double greatestDepth(Integer taxidA)
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		if (ciccarelliName1 == null)
			{
			throw new NoSuchElementException("No such element: " + taxidA);
			}

		return ciccarelli.greatestDepth(ciccarelliName1);
		}
	}
