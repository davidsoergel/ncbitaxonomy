package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.TaxonomyService;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiCiccarelliHybridService extends TaxonomyService<Integer>
	{
	Integer nearestKnownAncestor(Integer taxidA) throws NoSuchNodeException;

	double exactDistanceBetween(Integer taxidA, Integer taxidB) throws NoSuchNodeException;
	}
