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

import edu.berkeley.compbio.phyloutils.CiccarelliUtils;
import edu.berkeley.compbio.phyloutils.HybridRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonMergingPhylogeny;

import java.util.Set;



/**
 * @Author David Soergel
 * @Version 1.0
 */
public class NcbiCiccarelliHybridService
	{

	private static final NcbiTaxonomyService ncbiTaxonomyService = NcbiTaxonomyService.getInstance();
	private static final CiccarelliUtils ciccarelli = CiccarelliUtils.getInstance();

	private static HybridRootedPhylogeny<Integer> hybridTree =
			new HybridRootedPhylogeny(ciccarelli.getTree(), ncbiTaxonomyService);


	public static Integer nearestKnownAncestor(String name) throws PhyloUtilsException
		{
		return hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name));
		}

	public static Integer nearestKnownAncestor(Integer id) throws PhyloUtilsException
		{
		return hybridTree.nearestKnownAncestor(id);
		}

	public static RootedPhylogeny<Integer> extractTreeWithLeafIDs(Set<Integer> ids) throws PhyloUtilsException
		{
		return ciccarelli.extractTreeWithLeafIDs(ids);
		}

	public static Double minDistanceBetween(String name1, String name2) throws PhyloUtilsException
		{
		int id1 = hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name1));
		int id2 = hybridTree.nearestKnownAncestor(ncbiTaxonomyService.findTaxidByName(name2));
		return ciccarelli.exactDistanceBetween(id1, id2);
		}

	public static Integer findTaxidByName(String name) throws NcbiTaxonomyException
		{
		return ncbiTaxonomyService.findTaxidByName(name);
		}


	 public static TaxonMergingPhylogeny<Integer> getTree()
		 {
		 return hybridTree;
		 }

	}
