/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy.jpadao;

import com.davidsoergel.springjpautils.GenericDaoImpl;
import com.davidsoergel.trees.NoSuchNodeException;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyRuntimeException;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNameDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyName;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

@Repository
public class NcbiTaxonomyNameDaoImpl extends GenericDaoImpl<NcbiTaxonomyName> implements NcbiTaxonomyNameDao
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyNameDaoImpl.class);

	// ------------------------------ FIELDS ------------------------------

	@PersistenceContext
	private EntityManager entityManager;

	private Map<String, NcbiTaxonomyName> names = new HashMap<String, NcbiTaxonomyName>();


	// --------------------- GETTER / SETTER METHODS ---------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityManager getEntityManager()
		{
		return entityManager;
		}

/*	@PersistenceContext
	public void setEntityManager(EntityManager entityManager)
		{
		this.entityManager = entityManager;
		}
*/
	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface GenericDao ---------------------

	//@Transactional(propagation = Propagation.SUPPORTS)

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public NcbiTaxonomyName findById(Integer id)
		{
		NcbiTaxonomyName result = entityManager.find(NcbiTaxonomyName.class, id);
		if (result == null)
			{
			throw new NoResultException("Could not find taxon: " + id);
			//throw new NoSuchElementException();
			}
		return result;
		}

	// --------------------- Interface NcbiTaxonomyNameDao ---------------------

	//@Transactional(propagation = Propagation.REQUIRED,
	//               noRollbackFor = {javax.persistence.NoResultException.class,
	//		               javax.persistence.EntityNotFoundException.class})

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public NcbiTaxonomyName findByName(String name) throws NoSuchNodeException
		{
		NcbiTaxonomyName result = names.get(name);
		if (result != null)
			{
			return result;
			}
		//HibernateDB.getDb().beginTaxn();
		// Status notstarted = Status.findByName("Waiting");

		/*Query q = PhyloUtils.getNcbiDb().createNamedQuery("NcbiTaxonomyName.findByName");
		q.setMaxResults(1);
		q.setParameter("name", name);

		result = (NcbiTaxonomyName) q.getSingleResult();*/

		try
			{
			result = (NcbiTaxonomyName) (entityManager.createNamedQuery("NcbiTaxonomyName.findByName")
					                             .setParameter("name", name).getSingleResult());
			assert result != null;
			names.put(name, result);
			return result;
			}
		catch (NonUniqueResultException e)
			{
			logger.error("Name not unique in database; trying scientific: " + name);
			try
				{
				return findByScientificName(name);
				}
			catch (NoSuchNodeException f)
				{
				logger.error("Scientific Name not found in database; trying unique: " + name);
				return findByUniqueName(name);
				}
			}
		catch (NoResultException e)
			{
			logger.error("Name not found in database; trying unique: " + name);
			return findByUniqueName(name);
			}
		}

	@NotNull
	public Collection<String> findSynonyms(Integer taxid)
		{
		List<String> result = (List<String>) (entityManager.createNamedQuery("NcbiTaxonomyName.findSynonyms")
				                                      .setParameter("taxid", taxid).getResultList());
		return result;
		}
	/*public Collection<String> findSynonyms(String name)
		{
		List<String> result = (List<String>) (entityManager.createNamedQuery("NcbiTaxonomyName.findSynonyms")
				.setParameter("name", name).getResultList());
		return result;
		}*/

	/*
	 public Collection<String> findSynonymsOfParent(String name)
		 {
		 List<String> result = (List<String>) (entityManager.createNamedQuery("NcbiTaxonomyName.findSynonymsOfParent")
				 .setParameter("name", name).getResultList());
		 return result;
		 }
 */

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public NcbiTaxonomyName findByUniqueName(String name) throws NoSuchNodeException
		{
		NcbiTaxonomyName result = names.get(name);
		if (result != null)
			{
			return result;
			}
		//HibernateDB.getDb().beginTaxn();
		// Status notstarted = Status.findByName("Waiting");

		/*Query q = PhyloUtils.getNcbiDb().createNamedQuery("NcbiTaxonomyName.findByName");
		q.setMaxResults(1);
		q.setParameter("name", name);

		result = (NcbiTaxonomyName) q.getSingleResult();*/

		try
			{
			result = (NcbiTaxonomyName) (entityManager.createNamedQuery("NcbiTaxonomyName.findByUniqueName")
					                             .setParameter("name", name).getSingleResult());
			}
		catch (NonUniqueResultException e)
			{
			logger.error("Impossible: unique name is not unique in database: " + name);
			throw new NcbiTaxonomyRuntimeException("Impossible: unique name is not unique in database: " + name);
			}
		catch (NoResultException e)
			{
			//logger.error("Name not unique in database: " + name);
			throw new NoSuchNodeException("Unique Name not found: " + name);
			}
		assert result != null;
		/*	if (result == null)
		   {
		   throw new NcbiTaxonomyException("Could not find taxon: " + name);
		   }*/
		names.put(name, result);
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public NcbiTaxonomyName findByScientificName(String name) throws NoSuchNodeException
		{
		NcbiTaxonomyName result = names.get(name);
		if (result != null)
			{
			return result;
			}

		try
			{
			result = (NcbiTaxonomyName) (entityManager.createNamedQuery("NcbiTaxonomyName.findByScientificName")
					                             .setParameter("name", name).getSingleResult());
			}
		catch (NonUniqueResultException e)
			{
			try
				{
				result = (NcbiTaxonomyName) (entityManager.createNamedQuery(
						"NcbiTaxonomyName.findByScientificNameWithUniqueTag").setParameter("name", name)
						                             .setParameter("tag", "%acteria%").getSingleResult());

				// there are not yet any database entries where searching for "archaea" would help
				// leave off the first letter to be agnostic about capitalization
				}
			catch (NonUniqueResultException f)
				{
				logger.warn("Scientific Name is not unique in database: " + name);
				throw new NcbiTaxonomyRuntimeException(
						"Impossible: scientific name is not unique in database: " + name);
				}
			catch (NoResultException f)
				{
				throw new NoSuchNodeException("Scientific Name not found: " + name);
				}
			}
		catch (NoResultException e)
			{
			throw new NoSuchNodeException("Scientific Name not found: " + name);
			}
		assert result != null;

		names.put(name, result);
		return result;
		}

	/**
	 * Relaxation is the successive truncation of space-delimited suffixes, which may be overly specific strain IDs and
	 * such.  This is done only on the query; the reference names are not munged.  However, there are reference names
	 * available at each taxonomic level already. {@inheritDoc}
	 */
	//@Transactional(propagation = Propagation.MANDATORY)
	@NotNull
	public NcbiTaxonomyName findByNameRelaxed(String name) throws NoSuchNodeException
		{
		NcbiTaxonomyName result = null;
		String origName = name;
		String oldname = null;
		try
			{
			while (!name.equals(oldname))
				{
				try
					{
					result = findByName(name);
					break;
					}
				catch (NoSuchNodeException e)
					{
					oldname = name;
					name = name.substring(0, name.lastIndexOf(" "));
					}
				}
			}
		catch (IndexOutOfBoundsException e)
			{
			throw new NoSuchNodeException("Could not find taxon: " + name);
			}
		if (!name.equals(origName))
			{
			logger.warn("Relaxed name " + origName + " to " + name);
			}
		return result;
		}

/*
	public Collection<String> findSynonymsRelaxed(String name) throws NcbiTaxonomyException
		{
		try
			{
			String relaxedName = findByNameRelaxed(name).getName();
			List<String> result = (List<String>) (entityManager.createNamedQuery("NcbiTaxonomyName.findSynonyms")
					.setParameter("name", relaxedName).getResultList());
			return result;
			}
		catch (NcbiTaxonomyException e)
			{
			// nothing found
			return new HashSet<String>(0);
			}
		}*/
	}

