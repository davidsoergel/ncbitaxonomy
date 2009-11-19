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

import com.davidsoergel.dsutils.EnvironmentUtils;
import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import com.davidsoergel.dsutils.math.MathUtils;
import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.IntegerNodeNamer;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.RootedPhylogeny;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import edu.berkeley.compbio.phyloutils.CiccarelliTaxonomyService;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhyloUtilsRuntimeException;
import edu.berkeley.compbio.phyloutils.PhylogenyTypeConverter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiCiccarelliHybridServiceImplTest
	{
	@Test
	public void nearestKnownAncestorStringWorks() throws PhyloUtilsException, NoSuchNodeException
		{
		assert NcbiCiccarelliHybridServiceImpl.getInstance()
				.nearestKnownAncestor("Vibrio cholerae O1 biovar eltor str. N16961") == 666;//243277)
		}


	@Test
	public void nearestKnownAncestorIntegerWorks() throws PhyloUtilsException, NoSuchNodeException
		{
		assert NcbiCiccarelliHybridServiceImpl.getInstance().nearestKnownAncestor(243277) == 666;
		}

	@Test
	public void exactDistanceBetweenWorks() throws PhyloUtilsException, NoSuchNodeException
		{
		NcbiCiccarelliHybridServiceImpl s = NcbiCiccarelliHybridServiceImpl.getInstance();
		assert MathUtils.equalWithinFPError(s.exactDistanceBetween(5664, 5741), 1.11275);


		double d = s.exactDistanceBetween(s.findTaxidByName("Escherichia coli O6"),
		                                  s.findTaxidByName("Escherichia coli K12"));//(217992, 562);
		assert d == 0.00022;

		d = s.exactDistanceBetween(s.findTaxidByName("Escherichia coli O6"), s.findTaxidByName(
				"Prochlorococcus marinus CCMP1378"));//sp. MED4");//(217992, 59919);
		//logger.warn(d);
		assert MathUtils.equalWithinFPError(d, 1.47741);
		}


	@Test(expectedExceptions = NoSuchNodeException.class)
	public void exactDistanceBetweenThrowsNoSuchNodeExceptionOnUnknownTaxid()
			throws PhyloUtilsException, NoSuchNodeException
		{
		assert NcbiCiccarelliHybridServiceImpl.getInstance().exactDistanceBetween(243277, 666) == 0;
		}

	@Test
	public void minDistanceBetweenWorks() throws PhyloUtilsException, NoSuchNodeException
		{
		assert NcbiCiccarelliHybridServiceImpl.getInstance()
				.minDistanceBetween("Vibrio cholerae O1 biovar eltor str. N16961", "Vibrio cholerae") == 0;
		}

	@Test
	public void extractTreeWithLeafIDsWorksForCiccarelliNodes() throws PhyloUtilsException, NoSuchNodeException
		{
		Set<Integer> leafIds = DSCollectionUtils.setOf(5794, 7227, 9031, 317);

		RootedPhylogeny<Integer> result = NcbiCiccarelliHybridServiceImpl.getInstance()
				.extractTreeWithLeafIDs(leafIds, false, false,
				                        AbstractRootedPhylogeny.MutualExclusionResolutionMode.EXCEPTION);

		assert DSCollectionUtils.isEqualCollection(result.getLeafValues(), leafIds);
		assert result.getUniqueIdToNodeMap().size() == 7;
		assert MathUtils.equalWithinFPError(result.distanceBetween(5794, 317),
		                                    NcbiCiccarelliHybridServiceImpl.getInstance().exactDistanceBetween(5794,
		                                                                                                       317));
		}

	@Test(expectedExceptions = PhyloUtilsRuntimeException.class)
	public void extractTreeWithLeafIDsThrowsExceptionForCiccarelliNodesWhenArgumentsAreNotAllLeaves()
			throws PhyloUtilsException, NoSuchNodeException
		{
		Set<Integer> leafIds = DSCollectionUtils.setOf(5794, 7147, 7227, 9031, 317);

		RootedPhylogeny<Integer> result = NcbiCiccarelliHybridServiceImpl.getInstance()
				.extractTreeWithLeafIDs(leafIds, false, false,
				                        AbstractRootedPhylogeny.MutualExclusionResolutionMode.EXCEPTION);

		leafIds.remove(7147);// this one is internal
		assert DSCollectionUtils.isEqualCollection(result.getLeafValues(), leafIds);
		assert result.getUniqueIdToNodeMap().size() == 7;
		assert MathUtils.equalWithinFPError(result.distanceBetween(5794, 317),
		                                    NcbiCiccarelliHybridServiceImpl.getInstance().exactDistanceBetween(5794,
		                                                                                                       317));
		}

	@Test
	//(expectedExceptions = NoSuchNodeException.class)
	public void extractTreeWithLeafIDsForNonCiccarelliNodesWorks() throws PhyloUtilsException, NoSuchNodeException
		{
		Set<Integer> leafIds = DSCollectionUtils.setOf(422676, 244440, 9031, 199202);

		RootedPhylogeny<Integer> result = NcbiCiccarelliHybridServiceImpl.getInstance()
				.extractTreeWithLeafIDs(leafIds, false, false,
				                        AbstractRootedPhylogeny.MutualExclusionResolutionMode.EXCEPTION);

		assert DSCollectionUtils.isEqualCollection(result.getLeafValues(), leafIds);
		assert result.getUniqueIdToNodeMap().size() == 7;

		assert MathUtils.equalWithinFPError(result.distanceBetween(422676, 199202),
		                                    NcbiCiccarelliHybridServiceImpl.getInstance().exactDistanceBetween(5794,
		                                                                                                       317));
		}

/*	@Test
	public void findNodeByTaxIDInRootPhylogenyWorks()
		{
		PhylogenyNode<Integer> n =
				((HybridRootedPhylogeny<Integer>) NcbiCiccarelliHybridService.getInstance().getTree())
						.getRootPhylogeny().getNode(666);
		assert n.getParent().getValue().equals(662);
		}*/

	@Test
	public void findTaxIDByNameWorks() throws NcbiTaxonomyException, NoSuchNodeException
		{
		assert NcbiCiccarelliHybridServiceImpl.getInstance().findTaxidByName("Vibrio cholerae") == 666;

		assert NcbiCiccarelliHybridServiceImpl.getInstance()
				.findTaxidByName("Vibrio cholerae O1 biovar eltor str. N16961") == 243277;

		assert NcbiCiccarelliHybridServiceImpl.getInstance().findTaxidByName("Herpes simplex virus (type 1 / strain F)")
		       == 10304;
		}

	@Test(expectedExceptions = NoSuchNodeException.class)
	public void findTaxIDByUnknownNameThrowsException() throws NcbiTaxonomyException, NoSuchNodeException
		{
		NcbiCiccarelliHybridServiceImpl.getInstance().findTaxidByName("This species does not exist");
		}

	@Test
	public void ciccarelliTreeIsConvertedToTaxIdTree()
		{
		CiccarelliTaxonomyService ciccarelli = CiccarelliTaxonomyService.getInstance();


		final Multimap<String, Integer> nameToIdMap = HashMultimap.create();
		final Multimap<String, Integer> extraNameToIdMap = HashMultimap.create();
		RootedPhylogeny<Integer> ciccarelliIntegerTree = //NcbiCiccarelliHybridService.getInstance().
				PhylogenyTypeConverter.convertToIDTree(ciccarelli.getTree(), new IntegerNodeNamer(10000000, false),
				                                       NcbiTaxonomyPhylogeny.getInstance(), nameToIdMap,
				                                       extraNameToIdMap);
		System.err.println(ciccarelliIntegerTree);
		assert ciccarelliIntegerTree.getLeaves().size() > 100;
		}

	@Test
	public void emptyLinkedListsAreEqual()
		{
		List<Integer> l = new LinkedList<Integer>();
		List<Integer> m = new LinkedList<Integer>();

		assert l.equals(l);
		assert l.equals(m);
		assert m.equals(m);

		l.add(1);
		m.add(2);

		l.remove(new Integer(1));
		m.remove(new Integer(2));

		assert l.equals(l);
		assert l.equals(m);
		assert m.equals(m);
		}

	@BeforeClass
	public void setUp() throws Exception
		{
		EnvironmentUtils.setCacheRoot("/tmp/testCache");
		}
	}
