package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	@Nullable
	Integer findTaxidByName(String speciesNameA) throws NoSuchNodeException;

	Set<String> getCachedNamesForId(Integer id);//@Transactional(propagation = Propagation.REQUIRED)

	@Nullable
	Integer findParentTaxidByName(String speciesNameA) throws NoSuchNodeException;

	Collection<String> synonymsOf(String s) throws NoSuchNodeException;

	Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException;

	Collection<String> synonymsOfParent(String s) throws NoSuchNodeException;

	@NotNull
	PhylogenyNode findNode(Integer taxid)
			throws NoSuchNodeException;	//@Transactional(propagation = Propagation.REQUIRED)

	Integer findNearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId) throws NoSuchNodeException;

	Integer findNearestAncestorAtRank(String rankName, Integer leafId) throws NoSuchNodeException;
	}
