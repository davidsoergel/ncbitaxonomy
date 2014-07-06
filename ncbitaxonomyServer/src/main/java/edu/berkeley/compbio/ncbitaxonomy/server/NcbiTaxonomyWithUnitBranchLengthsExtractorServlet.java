/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy.server;

import com.caucho.hessian.server.HessianServlet;
import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.dsutils.DSStringUtils;
import com.davidsoergel.trees.AbstractRootedPhylogeny;
import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyWithUnitBranchLengthsPhylogeny;
import edu.berkeley.compbio.ncbitaxonomy.service.NcbiTaxonomyWithUnitBranchLengthsExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service
public class NcbiTaxonomyWithUnitBranchLengthsExtractorServlet extends HessianServlet
		implements NcbiTaxonomyWithUnitBranchLengthsExtractor //NcbiTaxonomyService
	{
	@Autowired
	@Qualifier("ncbiTaxonomyWithUnitBranchLengthsPhylogeny")
	NcbiTaxonomyWithUnitBranchLengthsPhylogeny ncbiTaxonomyWithUnitBranchLengthsPhylogeny;


	public BasicRootedPhylogeny<Integer> prepare(Set<Integer> allLabels) throws NoSuchNodeException
		{
		String idString = toString() + ":" + DSStringUtils.joinSorted(allLabels, ":");

		BasicRootedPhylogeny<Integer> extractedTree = (BasicRootedPhylogeny<Integer>) CacheManager.get(this, idString);
		if (extractedTree == null)
			{
			extractedTree = ncbiTaxonomyWithUnitBranchLengthsPhylogeny.extractTreeWithLeafIDs(allLabels, false, true,
			                                                                                  AbstractRootedPhylogeny.MutualExclusionResolutionMode.BOTH);
			extractedTree.setAllBranchLengthsTo(1.0);
			CacheManager.put(this, idString, extractedTree);
			}
		return extractedTree;
		}
	}
