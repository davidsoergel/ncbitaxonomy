/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.trees.BasicRootedPhylogeny;
import com.davidsoergel.trees.NoSuchNodeException;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiTaxonomyWithUnitBranchLengthsExtractor
	{
	BasicRootedPhylogeny<Integer> prepare(Set<Integer> allLabels) throws NoSuchNodeException;
	}
