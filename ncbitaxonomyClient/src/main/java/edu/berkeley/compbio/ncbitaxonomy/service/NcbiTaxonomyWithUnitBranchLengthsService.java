/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy.service;

import edu.berkeley.compbio.phyloutils.RequiresPreparationTaxonomyService;
import edu.berkeley.compbio.phyloutils.TaxonomyService;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiTaxonomyWithUnitBranchLengthsService
		extends TaxonomyService<Integer>, RequiresPreparationTaxonomyService<Integer>
	{
	}
