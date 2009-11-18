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
