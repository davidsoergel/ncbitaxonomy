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


import com.davidsoergel.springjpautils.SpringJpaObject;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Represents a row of the "names" table in the NCBI taxonomy database.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

@Entity
@Table(name = "names")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({@NamedQuery(
		name = "NcbiTaxonomyName.findByName",
		query = "select n from NcbiTaxonomyName n WHERE n.name = :name")})

// or NONSTRICT_READ_WRITE?
public class NcbiTaxonomyName extends SpringJpaObject
	{
	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyName.class);


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tax_id")
	private NcbiTaxonomyNode taxon;

	@Column(name = "name_txt")
	private String name;

	@Column(name = "unique_name")
	private String uniqueName;

	@Column(name = "name_class")
	private String nameClass;


	// --------------------- GETTER / SETTER METHODS ---------------------

	public String getName()
		{
		return name;
		}

	public void setName(String name)
		{
		this.name = name;
		}

	public String getNameClass()
		{
		return nameClass;
		}

	public void setNameClass(String nameClass)
		{
		this.nameClass = nameClass;
		}

	public NcbiTaxonomyNode getTaxon()
		{
		return taxon;
		}

	public void setTaxon(NcbiTaxonomyNode taxon)
		{
		this.taxon = taxon;
		}

	public String getUniqueName()
		{
		return uniqueName;
		}

	public void setUniqueName(String uniqueName)
		{
		this.uniqueName = uniqueName;
		}

	// ------------------------ CANONICAL METHODS ------------------------

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
		if (!(o instanceof NcbiTaxonomyName))
			{
			return false;
			}

		NcbiTaxonomyName that = (NcbiTaxonomyName) o;

		if (name != null ? !name.equals(that.name) : that.name != null)
			{
			return false;
			}
		if (nameClass != null ? !nameClass.equals(that.nameClass) : that.nameClass != null)
			{
			return false;
			}
		if (taxon != null ? !taxon.equals(that.taxon) : that.taxon != null)
			{
			return false;
			}
		if (uniqueName != null ? !uniqueName.equals(that.uniqueName) : that.uniqueName != null)
			{
			return false;
			}

		return true;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
		{
		int result;

		//** need to avoid circular dependency, since the node hashCode depends on its names

		//result = (taxon != null ? taxon.hashCode() : 0);
		//result = 31 * result + (name != null ? name.hashCode() : 0);
		result = (name != null ? name.hashCode() : 0);
		result = 31 * result + (uniqueName != null ? uniqueName.hashCode() : 0);
		result = 31 * result + (nameClass != null ? nameClass.hashCode() : 0);
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
		{
		return getName();
		}
	}
