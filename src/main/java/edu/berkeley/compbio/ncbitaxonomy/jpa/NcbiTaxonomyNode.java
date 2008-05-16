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

import com.davidsoergel.dsutils.tree.HierarchyNode;
import com.davidsoergel.springjpautils.SpringJpaObject;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: soergel Date: Nov 6, 2006 Time: 2:30:36 PM To change this template use File |
 * Settings | File Templates.
 */
@Entity
@Table(name = "nodes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
/*@NamedQueries({@NamedQuery(
		name = "NcbiTaxonomyNode.findByTaxId",
		query = "select n from NcbiTaxonomyNode n WHERE id = :taxid")})
*/
// or NONSTRICT_READ_WRITE?
//@NamedQuery(name="NcbiTaxonomyNode.findByName",query="select n from NcbiTaxonomyNode n WHERE Name = :name"),
public class NcbiTaxonomyNode extends SpringJpaObject implements PhylogenyNode<Integer>
	{
	// ------------------------------ FIELDS ------------------------------

	private static Logger logger = Logger.getLogger(NcbiTaxonomyName.class);

	//private int taxId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_tax_id")
	private NcbiTaxonomyNode parent;

	@OneToMany(mappedBy = "taxon", fetch = FetchType.LAZY)
	private Set<NcbiTaxonomyName> names;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	//, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//, fetch = FetchType.EAGER)
	//, CascadeType.REFRESH})
	//@LazyCollection(LazyCollectionOption.FALSE)
	//@Fetch(value = FetchMode.SUBSELECT)
	private Set<NcbiTaxonomyNode> children = new HashSet<NcbiTaxonomyNode>();

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

	public Integer getValue()
		{
		return getId();
		}

	public NcbiTaxonomyNode getParent()
		{
		return parent;
		}

	public boolean hasValue()
		{
		return true;
		}

	public HierarchyNode<? extends Integer> newChild()
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public void setValue(Integer contents)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public void setParent(HierarchyNode<? extends Integer> parent)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public void setParent(NcbiTaxonomyNode parent)
		{
		this.parent = parent;
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

		if (names != null ? !names.equals(that.names) : that.names != null)
			{
			return false;
			}
		if (parent != null ? !parent.equals(that.parent) : that.parent != null)
			{
			return false;
			}

		return true;
		}

	public int hashCode()
		{
		int result;
		result = ((parent != null && parent.getId() != this.getId()) ? parent.hashCode() : 0);
		result = 31 * result + (names != null ? names.hashCode() : 0);

		return result;
		}

	// -------------------------- OTHER METHODS --------------------------

	public Set<NcbiTaxonomyNode> getChildren()
		{
		return children;
		}

	public PhylogenyNode<Integer> getChild(Integer id)
		{
		// We could map the children collection as a Map; but that's some hassle, and since there are generally just 2 children anyway, this is simpler

		for (NcbiTaxonomyNode child : children)
			{
			if (child.getId() == id)
				{
				return child;
				}
			}
		return null;
		}

	public int getMitochondrialGeneticCodeId()
		{
		return mitachondrialGeneticCodeId;
		}

	public void setChildSets(Set<NcbiTaxonomyNode> children)
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
		return null;
		}


	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();
		NcbiTaxonomyNode trav = this;

		while (trav != null)
			{
			result.add(0, trav);
			trav = trav.getParent();
			}

		return result;
		}

	public Double getLength()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public Double getLargestLengthSpan()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public boolean isLeaf()
		{
		return getChildren().isEmpty();
		}

	public double getWeight()
		{

		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	public void setWeight(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	public void incrementWeightBy(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch weights.");
		}

	public void propagateWeightFromBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public double distanceToRoot()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public RootedPhylogeny<Integer> clone()
		{
		throw new NotImplementedException();
		}
	}
