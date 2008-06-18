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

import com.davidsoergel.dsutils.collections.CollectionUtils;
import edu.berkeley.compbio.phyloutils.CiccarelliUtils;
import edu.berkeley.compbio.phyloutils.HybridRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonMergingPhylogeny;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Provides a view of a tree that extends the Ciccarelli tree from its leaves with subtrees taken from the NCBI
 * taxonomy. The NCBI taxonomy may contain several strains of a species that is represented in the Ciccarelli tree as a
 * single node.  This tree places these strains below the appropriate node in the Ciccarelli tree.  At higher levels of
 * the tree, only the Ciccarelli topology is considered; the NCBI topology is ignored.  The Ciccarelli tree provides
 * branch lengths, but the NCBI taxonomy does not.  This hybrid tree provides branch lengths according to the Ciccarelli
 * tree. It navigates the NCBI topology where necessary, but assigns zero length to those branches.  Thus, it is
 * possible to compute a distance between two strains that do not appear explicitly in the Ciccarelli tree, but this
 * distance is a lower bound.  Of course, the distance from a species node to a strain node below it is usually
 * effectively zero anyway, so this lower bound can be expected to be reasonably tight.
 *
 * @Author David Soergel
 * @Version 1.0
 */
public class NcbiCiccarelliHybridService
	{
	private static final Logger logger = Logger.getLogger(NcbiCiccarelliHybridService.class);

	private static final NcbiTaxonomyService ncbiTaxonomyService = NcbiTaxonomyService.getInstance();
	private static final CiccarelliUtils ciccarelli = CiccarelliUtils.getInstance();

	private static HybridRootedPhylogeny<Integer> hybridTree = new HybridRootedPhylogeny<Integer>(
			ncbiTaxonomyService.convertToIntegerIDTree(ciccarelli.getTree()), ncbiTaxonomyService);

	private Map<Integer, String> ciccarelliNames = new HashMap<Integer, String>();


	private static NcbiCiccarelliHybridService _instance = new NcbiCiccarelliHybridService();


	// -------------------------- STATIC METHODS --------------------------

	public static NcbiCiccarelliHybridService getInstance()
		{
		return _instance;
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
					logger.warn("Leaf with unknown taxid: " + n.getValue());
					}
				}

			for (PhylogenyNode<String> n : ciccarelli.getTree().getNodes())
				{
				try
					{
					String name = n.getValue();
					int id = ncbiTaxonomyService.findTaxidByName(name);
					ciccarelliNames.put(id, name);
					}
				catch (NcbiTaxonomyException e)
					{
					// too bad, ignore that one; probably an internal node
					}
				}
			}
		catch (Throwable e)
			{
			e.printStackTrace();
			logger.error(e);
			}
		}

	public static Integer nearestKnownAncestor(String name) throws PhyloUtilsException
		{
		return hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name));
		}

	public static Integer nearestKnownAncestor(Integer id) throws PhyloUtilsException
		{
		return hybridTree.nearestKnownAncestor(id);
		}

	public RootedPhylogeny<String> extractTreeWithLeafIDs(Set<Integer> ids) throws PhyloUtilsException
		{
		return ciccarelli.extractTreeWithLeafIDs(CollectionUtils.mapAll(ciccarelliNames, ids));
		}

	public Double minDistanceBetween(String name1, String name2) throws PhyloUtilsException
		{
		int id1 = hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		int id2 = hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));

		return exactDistanceBetween(id1, id2);
		}

	public static Integer findTaxidByName(String name) throws NcbiTaxonomyException
		{
		return ncbiTaxonomyService.findTaxidByName(name);
		}


	public static TaxonMergingPhylogeny<Integer> getTree()
		{
		return hybridTree;
		}

	public double exactDistanceBetween(Integer taxidA, Integer taxidB)
		{
		// this is tricky because we don't know which name variant the Ciccarelli tree uses
		// (if loaded from a file in terms of names rather than IDs).

		// we can't let the Ciccarelli tree deal with the String<->id mapping, because it doesn't have access to NcbiTaxonomyService.

		String ciccarelliName1 = ciccarelliNames.get(taxidA);
		String ciccarelliName2 = ciccarelliNames.get(taxidB);

		return ciccarelli.exactDistanceBetween(ciccarelliName1, ciccarelliName2);
		}
	}
