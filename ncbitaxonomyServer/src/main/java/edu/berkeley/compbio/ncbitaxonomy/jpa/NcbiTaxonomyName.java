/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
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
		name = "NcbiTaxonomyName.findSynonyms",
		query = "select n.name from NcbiTaxonomyName n WHERE n.taxon.id = :taxid"), @NamedQuery(
		name = "NcbiTaxonomyName.findByName",
		query = "select n from NcbiTaxonomyName n WHERE n.name = :name"), @NamedQuery(
		name = "NcbiTaxonomyName.findByUniqueName",
		query = "select n from NcbiTaxonomyName n WHERE n.uniqueName = :name"), @NamedQuery(
		name = "NcbiTaxonomyName.findByScientificName",
		query = "select n from NcbiTaxonomyName n WHERE n.name = :name AND n.nameClass = 'scientific name'"),
               @NamedQuery(
		               name = "NcbiTaxonomyName.findByScientificNameWithUniqueTag",
		               query = "select n from NcbiTaxonomyName n WHERE n.name = :name AND n.nameClass = 'scientific name' AND n.uniqueName LIKE :tag")})

/*@NamedQuery(
		name = "NcbiTaxonomyName.findSynonyms",
		query = "select m.name from NcbiTaxonomyName n, NcbiTaxonomyName m WHERE n.taxon = m.taxon AND n.name = :name"),
@NamedQuery(
		name = "NcbiTaxonomyName.findSynonymsOfParent",
		query = "select m.name from NcbiTaxonomyName n, NcbiTaxonomyName m WHERE n.taxon.parent = m.taxon AND n.name = :name"),*/


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

/*	@Override
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
*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
		{
		return getName();
		}
	}
