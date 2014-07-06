/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy;

import edu.berkeley.compbio.phyloutils.PhyloUtilsRuntimeException;
import org.apache.log4j.Logger;


/**
 * This exception is thrown when something goes wrong when accessing the NCBI taxonomy database.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class NcbiTaxonomyRuntimeException extends PhyloUtilsRuntimeException
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyRuntimeException.class);


	// --------------------------- CONSTRUCTORS ---------------------------

	public NcbiTaxonomyRuntimeException(String s)
		{
		super(s);
		}

	public NcbiTaxonomyRuntimeException(Exception e)
		{
		super(e);
		}

	public NcbiTaxonomyRuntimeException(Exception e, String s)
		{
		super(e, s);
		}
	}
