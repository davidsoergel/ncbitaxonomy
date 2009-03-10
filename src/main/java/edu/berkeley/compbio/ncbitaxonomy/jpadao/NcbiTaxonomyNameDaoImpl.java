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


package edu.berkeley.compbio.ncbitaxonomy.jpadao;

import com.davidsoergel.springjpautils.GenericDaoImpl;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyException;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNameDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyName;
import org.apache.log4j.Logger;
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

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager)
		{
		this.entityManager = entityManager;
		}

	// ------------------------ INTERFACE METHODS ------------------------


	// --------------------- Interface GenericDao ---------------------

	//@Transactional(propagation = Propagation.SUPPORTS)

	/**
	 * {@inheritDoc}
	 */
	public NcbiTaxonomyName findById(Integer id)
		{
		return entityManager.find(NcbiTaxonomyName.class, id);
		}

	// --------------------- Interface NcbiTaxonomyNameDao ---------------------

	//@Transactional(propagation = Propagation.REQUIRED,
	//               noRollbackFor = {javax.persistence.NoResultException.class,
	//		               javax.persistence.EntityNotFoundException.class})

	/**
	 * {@inheritDoc}
	 */
	public NcbiTaxonomyName findByName(String name) throws NcbiTaxonomyException
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

			if (result == null)
				{
				throw new NcbiTaxonomyException("Could not find taxon: " + name);
				}
			names.put(name, result);
			return result;
			}
		catch (NonUniqueResultException e)
			{
			logger.error("Name not unique in database; trying unique: " + name);
			return findByUniqueName(name);
			//	throw new NcbiTaxonomyException("Name not unique in database: " + name);
			}
		catch (NoResultException e)
			{
			return findByUniqueName(name);
			//logger.error("Name not unique in database: " + name);
			//	throw new NcbiTaxonomyException("Name not found: " + name);
			}
		}

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
	public NcbiTaxonomyName findByUniqueName(String name) throws NcbiTaxonomyException
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
			throw new NcbiTaxonomyException("Impossible: unique name is not unique in database: " + name);
			}
		catch (NoResultException e)
			{
			//logger.error("Name not unique in database: " + name);
			throw new NcbiTaxonomyException("Unique Name not found: " + name);
			}

		if (result == null)
			{
			throw new NcbiTaxonomyException("Could not find taxon: " + name);
			}
		names.put(name, result);
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	//@Transactional(propagation = Propagation.MANDATORY)
	public NcbiTaxonomyName findByNameRelaxed(String name) throws NcbiTaxonomyException
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
				catch (NoResultException e)
					{
					oldname = name;
					name = name.substring(0, name.lastIndexOf(" "));
					}
				}
			}
		catch (IndexOutOfBoundsException e)
			{
			throw new NcbiTaxonomyException("Could not find taxon: " + name);
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

