/*
 * Copyright (c) 2001-2008 David Soergel
 * 418 Richmond St., El Cerrito, CA  94530
 * dev@davidsoergel.com
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the names of any contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package edu.berkeley.compbio.ncbitaxonomy.jpa;

import com.davidsoergel.dsutils.collections.DSCollectionUtils;
import com.davidsoergel.springjpautils.SpringJpaObject;
import com.davidsoergel.trees.DepthFirstTreeIterator;
import com.davidsoergel.trees.HierarchyNode;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.NodeNamer;
import com.davidsoergel.trees.PhylogenyNode;
import com.davidsoergel.trees.RootedPhylogeny;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Represents a row of the "nodes" table in the NCBI taxonomy database.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

@Entity
@Table(name = "nodes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({@NamedQuery(
		name = "NcbiTaxonomyNode.findByRank",
		query = "select n from NcbiTaxonomyNode n WHERE rank = :rank"), @NamedQuery(
		name = "NcbiTaxonomyNode.findIdsByRank",
		query = "select n.id from NcbiTaxonomyNode n WHERE rank = :rank")})

// or NONSTRICT_READ_WRITE?
//@NamedQuery(name="NcbiTaxonomyNode.findByName",query="select n from NcbiTaxonomyNode n WHERE Name = :name"),
public class NcbiTaxonomyNode extends SpringJpaObject implements PhylogenyNode<Integer>
	{
	// ------------------------------ FIELDS ------------------------------
	@Autowired
	@Transient
	private NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao;

	public void setName(final String name)
		{
		throw new NotImplementedException();
		}

	public String getName()
		{
		throw new NotImplementedException();
		}

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyName.class);

	//private int taxId;
	@ManyToOne(fetch = FetchType.EAGER)
	//LAZY
	@JoinColumn(name = "parent_tax_id")
	private NcbiTaxonomyNode parent;

	// may as well be eager since we need these for the hashcode
	@OneToMany(mappedBy = "taxon", fetch = FetchType.EAGER)
	private Set<NcbiTaxonomyName> names;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	//, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//, fetch = FetchType.EAGER)
	//, CascadeType.REFRESH})
	//@LazyCollection(LazyCollectionOption.FALSE)
	//@Fetch(value = FetchMode.SUBSELECT)
	@OrderBy("id")
	private List<NcbiTaxonomyNode> children = new ArrayList<NcbiTaxonomyNode>();

	@Column(name = "rank")
	private String rank;

	@Column(name = "embl_code")
	private String emblCode;

	@Column(name = "division_id")
	private int divisionId;

	@Column(name = "inherited_div_flag")
	private boolean inheritedDivFlag;

	@Column(name = "genetic_code_id")
	private int geneticCodeId;

	@Column(name = "inherited_GC_flag")
	private boolean inheritedGCFlag;

	@Column(name = "mitochondrial_genetic_code_id")
	private int mitachondrialGeneticCodeId;

	@Column(name = "inherited_MGC_flag")
	private boolean inheritedMGCFlag;

	@Column(name = "GenBank_hidden_flag")
	private boolean genBankHiddenFlag;

	@Column(name = "hidden_subtree_root_flag")
	private boolean hiddenSubtreeRootFlag;

	@Column(name = "comments")
	private String comments;
	private static final String SCIENTIFIC_NAME = "scientific name";


	// --------------------- GETTER / SETTER METHODS ---------------------

	public String getComments()
		{
		return comments;
		}

	public void setComments(String comments)
		{
		this.comments = comments;
		}

	public int getDivisionId()
		{
		return divisionId;
		}

	public void setDivisionId(int divisionId)
		{
		this.divisionId = divisionId;
		}

	public String getEmblCode()
		{
		return emblCode;
		}

	public void setEmblCode(String emblCode)
		{
		this.emblCode = emblCode;
		}

	public int getGeneticCodeId()
		{
		return geneticCodeId;
		}

	public void setGeneticCodeId(int geneticCodeId)
		{
		this.geneticCodeId = geneticCodeId;
		}

	public Set<NcbiTaxonomyName> getNames()
		{
		return names;
		}

	public void setNames(Set<NcbiTaxonomyName> names)
		{
		this.names = names;
		}

	public long getTaxId()
		{
		return getId();
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPayload()
		{
		return getId();
		}

	/**
	 * {@inheritDoc}
	 */
	public NcbiTaxonomyNode getParent()
		{
		return parent;
		}

	public PhylogenyNode<Integer> findRoot()
		{
		if (parent == null)
			{
			return this;
			}
		else
			{
			return parent.findRoot();
			}
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasValue()
		{
		return true;
		}

	/**
	 * {@inheritDoc}
	 */
	/*	public NcbiTaxonomyNode newChild()
		 {
		 throw new NotImplementedException("The NCBI taxonomy is not editable");
		 }
 */

	/**
	 * Not implemented
	 */
	public PhylogenyNode<Integer> newChild(Integer payload)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * {@inheritDoc}
	 */
	public void setPayload(Integer contents)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}


	/**
	 * {@inheritDoc}
	 */
	//** sketchy; should be setParent(NcbiTaxonomyNode parent)
	public void setParent(PhylogenyNode<Integer> parent)
		{
		if (this.parent != null)
			{
			this.parent.unregisterChild(this);
			}
		this.parent = (NcbiTaxonomyNode) parent;
		if (this.parent != null)
			{
			this.parent.registerChild(this);
			}
		}

	public String getRank()
		{
		return rank;
		}

	public void setRank(String rank)
		{
		this.rank = rank;
		}

	public boolean isGenBankHiddenFlag()
		{
		return genBankHiddenFlag;
		}

	public void setGenBankHiddenFlag(boolean genBankHiddenFlag)
		{
		this.genBankHiddenFlag = genBankHiddenFlag;
		}

	public boolean isHiddenSubtreeRootFlag()
		{
		return hiddenSubtreeRootFlag;
		}

	public void setHiddenSubtreeRootFlag(boolean hiddenSubtreeRootFlag)
		{
		this.hiddenSubtreeRootFlag = hiddenSubtreeRootFlag;
		}

	public boolean isInheritedDivFlag()
		{
		return inheritedDivFlag;
		}

	public void setInheritedDivFlag(boolean inheritedDivFlag)
		{
		this.inheritedDivFlag = inheritedDivFlag;
		}

	public boolean isInheritedGCFlag()
		{
		return inheritedGCFlag;
		}

	public void setInheritedGCFlag(boolean inheritedGCFlag)
		{
		this.inheritedGCFlag = inheritedGCFlag;
		}

	public boolean isInheritedMGCFlag()
		{
		return inheritedMGCFlag;
		}

	public void setInheritedMGCFlag(boolean inheritedMGCFlag)
		{
		this.inheritedMGCFlag = inheritedMGCFlag;
		}

	// ------------------------ CANONICAL METHODS ------------------------

	//** Using the names collection here probably doesn't work
	// well, not with LAZY loading, but now that it's EAGER it should be OK


	// ** gah why can't equals and hashCode depend only on getId?
	// If there are ever transient nodes that don't have an ID yet, that'sa problem; but the NCBI taxonomy is read-only so that should never occur

	/*
	@Override
	public boolean equals(Object o)
		{
		if (this == o)
			{
			return true;
			}
		if (!(o instanceof NcbiTaxonomyNode))
			{
			return false;
			}

		NcbiTaxonomyNode that = (NcbiTaxonomyNode) o;

		return this.getId().equals(that.getId());

//				if (names != null ? !names.equals(that.names) : that.names != null)
//					{
//					return false;
//					}
//				if (parent != null ? !parent.equals(that.parent) : that.parent != null)
//					{
//					return false;
//					}
//
//				return true;
		}

	@Override
	//@Transactional
	public int hashCode()
		{
		int result;

		result = getId();

	//	result = ((parent != null && !parent.getId().equals(this.getId())) ? parent.hashCode() : 0);
	//	result = 31 * result + (names != null ? names.hashCode() : 0);


		return result;
		}
*/

	// -------------------------- OTHER METHODS --------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<NcbiTaxonomyNode> getChildren()
		{
		return children;
		}

	/**
	 * Need this to do recursive calls without running out of memory, by starting a new transaction for each query.  Slow,
	 * but works.
	 *
	 * @return
	 */
	public List<Integer> getChildIds()
		{
		List<Integer> result = new ArrayList<Integer>();
		for (NcbiTaxonomyNode child : children)
			{
			result.add(child.getPayload());
			}
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getChildWithPayload(Integer id) throws NoSuchNodeException
		{
		// We could map the children collection as a Map; but that's some hassle, and since there are generally just 2 children anyway, this is simpler

		for (NcbiTaxonomyNode child : children)
			{
			if (child.getId().equals(id))
				{
				return child;
				}
			}
		throw new NoSuchNodeException("Could not find child: " + id);
		}

	public int getMitochondrialGeneticCodeId()
		{
		return mitachondrialGeneticCodeId;
		}

	public void setChildSets(List<NcbiTaxonomyNode> children)
		{
		this.children = children;
		}

	public void setMitochondrialGeneticCodeId(int mitachondrialGeneticCodeId)
		{
		this.mitachondrialGeneticCodeId = mitachondrialGeneticCodeId;
		}

	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<PhylogenyNode<Integer>> iterator()
		{
		throw new NotImplementedException("Iterating the entire NCBI taxonomy is probably a bad idea");
		}


	public void addToMap(Map<Integer, PhylogenyNode<Integer>> uniqueIdToNodeMap, NodeNamer<Integer> namer)
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * {@inheritDoc}
	 */
	public DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> depthFirstIterator()
		{
		throw new NotImplementedException("Iterating the entire NCBI taxonomy is probably a bad idea");
		}


	@Transient
	private List<PhylogenyNode<Integer>> ancestorPath;

	/**
	 * {@inheritDoc}
	 */
	//@Transactional(propagation = Propagation.MANDATORY)
	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		if (ancestorPath == null)
			{
			fetchAncestorPath();
			}
		return ancestorPath;
		}

	public List<? extends HierarchyNode<Integer, PhylogenyNode<Integer>>> getAncestorPath(final boolean includeSelf)
		{
		throw new NotImplementedException();
		}


	@Transient
	private List<Integer> ancestorPathIds;

	/**
	 * {@inheritDoc}
	 */
	//@Transactional(propagation = Propagation.MANDATORY)
	public List<Integer> getAncestorPathPayloads()
		{
		if (ancestorPathIds == null)
			{
			fetchAncestorPathIds();
			}
		return ancestorPathIds;
		}

	//@Transactional(propagation = Propagation.MANDATORY)

	public void fetchAncestorPath()
		{

		// because the nodes.parent_id column is notnull, the root node is its own parent.
		// avoid infinite loop:

		//NcbiTaxonomyNode parent = getParent();
		if (parent == null || getId().equals(parent.getId()))
			{
			ancestorPath = new LinkedList<PhylogenyNode<Integer>>();  //we're at the root
			}
		else
			{
			ancestorPath = new LinkedList<PhylogenyNode<Integer>>(getParent().getAncestorPath());
			}
		ancestorPath.add(this);

		ancestorPath = Collections.unmodifiableList(ancestorPath);
		}

	//@Transactional(propagation = Propagation.MANDATORY)

	public void fetchAncestorPathIds()
		{
		ancestorPathIds = new LinkedList<Integer>();
		for (PhylogenyNode<Integer> node : getAncestorPath())
			{
			ancestorPathIds.add(node.getPayload());
			}
		ancestorPathIds = Collections.unmodifiableList(ancestorPathIds);
		}

	/**
	 * {@inheritDoc}
	 */
	@Nullable
	public Double getLength()
		{
		return null;
		//throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	public void setLength(Double d)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	//@Nullable
	public double getLargestLengthSpan()
		{
		//return null;
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	//@Nullable
	public double getGreatestBranchLengthDepthBelow()
		{
		//return null;
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	//@Nullable
	public double getLeastBranchLengthDepthBelow()
		{
		//return null;
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public void registerChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}

	/**
	 * Not implemented
	 */
	public void unregisterChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}


	/**
	 * {@inheritDoc}
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public boolean isLeaf()
		{
		return getChildren().isEmpty();
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getWeight()
		{
		//throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		return null;
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getCurrentWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	/**
	 * {@inheritDoc}
	 */
	public void setWeight(Double d)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	public void setWeight(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	/**
	 * {@inheritDoc}
	 */
	public void incrementWeightBy(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	/*public void propagateWeightFromBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}*/

	/**
	 * {@inheritDoc}
	 */
	public double distanceToRoot()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public PhylogenyNode<Integer> nearestAncestorWithBranchLength()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RootedPhylogeny<Integer> clone()
		{
		throw new NotImplementedException();
		}

	/**
	 * {@inheritDoc}
	 */
	public NcbiTaxonomyNode getSelfNode()
		{
		return this;
		}

	/**
	 * Not implemented
	 */
	public void appendSubtree(StringBuffer sb, String indent)
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a String is probably a bad idea");
		}

	public int countDescendantsIncludingThis()
		{
		//return 0;
		throw new NotImplementedException("Counting subtrees in  the entire NCBI taxonomy is probably a bad idea");
		}


	public String toString()
		{
		return getTaxId() + "/" + getScientificName();
		}

	public String getScientificName()
		{
		for (NcbiTaxonomyName name : names)
			{
			if (name.getNameClass().equals(SCIENTIFIC_NAME))
				{
				return name.getName();
				}
			}
		return "No Scientific Name Available";
		}


	// cut & paste, too bad
	public void toNewick(Writer out, String prefix, String tab, int minClusterSize, double minLabelProb)
			throws IOException
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> asRootedPhylogeny()
		{
		throw new NotImplementedException();
		}

	/**
	 * Note this samples from the distribution of leaves weighted by the tree structure, i.e. uniformly _per level_, not
	 * uniformly from the set of leaves.  Basically, leaves with fewer siblings and cousins are more likely to be chosen.
	 *
	 * @return
	 */
	public PhylogenyNode<Integer> getRandomLeafBelow()
		{
		// iterate, don't recurse, in case the tree is deep
		PhylogenyNode<Integer> trav = this;
		List<? extends PhylogenyNode<Integer>> travChildren = trav.getChildren();

		while (!travChildren.isEmpty())
			{
			trav = DSCollectionUtils.chooseRandom(travChildren);
			travChildren = trav.getChildren();
			}

		return trav;
		}

	public void collectLeavesBelowAtApproximateDistance(final double minDesiredTreeDistance,
	                                                    final double maxDesiredTreeDistance,
	                                                    final Collection<PhylogenyNode<Integer>> result)
		{

		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}
	}
