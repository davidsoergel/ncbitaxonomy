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


/**
 * @Author David Soergel
 * @Version 1.0
 */
public class HybridRootedPhylogenyTest
	{
	private static final NcbiTaxonomyService ncbiTaxonomyService = NcbiTaxonomyService.getInstance();
	private static final CiccarelliUtils ciccarelli = CiccarelliUtils.getInstance();

	private HybridRootedPhylogeny<Integer> hybridTree;

	/*@Test
	public void nearestKnownAncestorWorks() throws PhyloUtilsException
		{
		hybridTree = new HybridRootedPhylogeny(ciccarelli.getTree(), ncbiTaxonomyService);
		assert hybridTree.nearestKnownAncestor("Vibrio cholerae O1 biovar eltor str. N16961") == 666; //243277)
		}*/
	}