/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.TaxonomyService;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiTaxonomyService extends TaxonomyService<Integer>, TaxonomySynonymService
	{
	String getScientificName(Integer from) throws NoSuchNodeException;

	Set<Integer> getTaxIdsWithRank(String rank);
	}
