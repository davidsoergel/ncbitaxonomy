/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.PhylogenyNode;
import com.davidsoergel.trees.RootedPhylogeny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public interface NcbiTaxonomyServiceEngine
	{
	Integer findTaxidByNameRelaxed(String speciesNameA)
			throws NoSuchNodeException;//@Transactional(propagation = Propagation.REQUIRED)

	@NotNull
	Integer findTaxidByName(String speciesNameA) throws NoSuchNodeException;

	@NotNull
	String findScientificName(Integer taxid) throws NoSuchNodeException;

	Set<String> getCachedNamesForId(Integer id);//@Transactional(propagation = Propagation.REQUIRED)

	@Nullable
	Integer findParentTaxidByName(String speciesNameA) throws NoSuchNodeException;

	Collection<String> synonymsOfIdNoCache(int taxid) throws NoSuchNodeException;

	Collection<String> synonymsOf(String s) throws NoSuchNodeException;

	Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException;

	Collection<String> synonymsOfParent(String s) throws NoSuchNodeException;

	@NotNull
	PhylogenyNode findNode(Integer taxid)
			throws NoSuchNodeException;	//@Transactional(propagation = Propagation.REQUIRED)

	Integer findNearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId) throws NoSuchNodeException;

	Integer findNearestAncestorAtRank(String rank, Integer leafId) throws NoSuchNodeException;

	Set<Integer> findTaxIdsWithRank(String rank);

	Set<String> findNamesWithRank(String rank);

	void toNewick(Writer out, String prefix, String tab, int minClusterSize, double minLabelProb) throws IOException;

	void writeSynonyms(Writer out);
	}
