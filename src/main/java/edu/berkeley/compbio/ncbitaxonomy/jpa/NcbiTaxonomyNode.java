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

import com.davidsoergel.dsutils.tree.DepthFirstTreeIterator;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.springjpautils.SpringJpaObject;
import edu.berkeley.compbio.phyloutils.NodeNamer;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import java.util.ArrayList;
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
@NamedQueries({
		@NamedQuery(
				name = "NcbiTaxonomyNode.findByRank",
				query = "select n from NcbiTaxonomyNode n WHERE rank = :rank"),
		@NamedQuery(
				name = "NcbiTaxonomyNode.findIdsByRank",
				query = "select n.id from NcbiTaxonomyNode n WHERE rank = :rank")
})

// or NONSTRICT_READ_WRITE?
//@NamedQuery(name="NcbiTaxonomyNode.findByName",query="select n from NcbiTaxonomyNode n WHERE Name = :name"),
public class NcbiTaxonomyNode extends SpringJpaObject implements PhylogenyNode<Integer>
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyName.class);

	//private int taxId;
	@ManyToOne(fetch = FetchType.LAZY)
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
	public Integer getValue()
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
	public NcbiTaxonomyNode newChild()
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * {@inheritDoc}
	 */
	public void setValue(Integer contents)
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

	/**
	 * {@inheritDoc}
	 */
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
		/*
				if (names != null ? !names.equals(that.names) : that.names != null)
					{
					return false;
					}
				if (parent != null ? !parent.equals(that.parent) : that.parent != null)
					{
					return false;
					}

				return true;*/
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	//@Transactional
	public int hashCode()
		{
		int result;

		result = getId();
		/*
		result = ((parent != null && !parent.getId().equals(this.getId())) ? parent.hashCode() : 0);
		result = 31 * result + (names != null ? names.hashCode() : 0);
*/

		return result;
		}


	// -------------------------- OTHER METHODS --------------------------

	/**
	 * {@inheritDoc}
	 */
	public List<NcbiTaxonomyNode> getChildren()
		{
		return children;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getChild(Integer id) throws NoSuchNodeException
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

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();
		NcbiTaxonomyNode trav = this;

		while (trav != null)
			{
			result.add(0, trav);

			// because the nodes.parent_id columnis notnull, the root node is its own parent.
			// avoid infinite loop:

			NcbiTaxonomyNode parent = trav.getParent();
			if (parent == null || trav.getId().equals(parent.getId()))
				{
				trav = null;
				}
			else
				{
				trav = parent;
				}
			}

		return result;
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
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
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


	public void toNewick(StringBuffer sb, String prefix, String tab, int minClusterSize, double minLabelProb)
		{
		throw new NotImplementedException("Printing the entire NCBI taxonomy is probably a bad idea");
		}

	public RootedPhylogeny<Integer> asRootedPhylogeny()
		{
		throw new NotImplementedException();
		}
	}
