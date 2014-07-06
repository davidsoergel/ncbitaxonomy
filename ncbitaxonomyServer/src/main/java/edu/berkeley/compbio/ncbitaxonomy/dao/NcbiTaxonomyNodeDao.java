/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy.dao;

import com.davidsoergel.springjpautils.GenericDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyNode;

import java.util.Collection;
import java.util.List;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface NcbiTaxonomyNodeDao extends GenericDao<NcbiTaxonomyNode, Integer>
	{
	// -------------------------- OTHER METHODS --------------------------

	//NcbiTaxonomyNode findByTaxId(Integer taxid);


	List<Integer> findChildIds(Integer id);

	List<NcbiTaxonomyNode> findByRank(String rankName);

	Collection<Integer> findIdsByRank(String rankName);

	Collection<Integer> findAllIds();
	}
