package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;

import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiTaxonomyWithUnitBranchLengthsExtractor
	{
	BasicRootedPhylogeny<Integer> prepare(Set<Integer> allLabels) throws NoSuchNodeException;
	}
