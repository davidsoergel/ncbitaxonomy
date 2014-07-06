/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy.dao;

import com.davidsoergel.springjpautils.GenericDao;
import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyName;

import java.util.Collection;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public interface NcbiTaxonomyNameDao extends GenericDao<NcbiTaxonomyName, Integer>
	{
	// -------------------------- OTHER METHODS --------------------------

	Collection<String> findSynonyms(Integer taxid);

//	Collection<String> findSynonymsOfParent(String s);

//	Collection<String> findSynonymsRelaxed(String s) throws NcbiTaxonomyException;


	NcbiTaxonomyName findByName(String name) throws NoSuchNodeException;

	NcbiTaxonomyName findByNameRelaxed(String name) throws NoSuchNodeException;
	}
