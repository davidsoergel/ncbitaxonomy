/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy.jpadao;

import com.davidsoergel.springjpautils.GenericDaoImpl;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyNode;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

@Repository
public class NcbiTaxonomyNodeDaoImpl extends GenericDaoImpl<NcbiTaxonomyNode> implements NcbiTaxonomyNodeDao
	{
	// ------------------------------ FIELDS ------------------------------

	@PersistenceContext
	private EntityManager entityManager;


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
	//@Transactional
	public NcbiTaxonomyNode findById(Integer id)
		{
		NcbiTaxonomyNode node = entityManager.find(NcbiTaxonomyNode.class, id);
		if (node == null)
			{
			throw new NoResultException("Could not find taxon: " + id);
			}
		// eagerly load the path
		//node.getAncestorPath();
		return node;
		}


	/**
	 * Need this to do recursive calls without running out of memory, by starting a new transaction for each query.  Slow,
	 * but works.
	 *
	 * @return
	 */
/*	public List<Integer> findChildIds(Integer id)
		{
		NcbiTaxonomyNode node = entityManager.find(NcbiTaxonomyNode.class, id);
		if (node == null)
			{
			throw new NoResultException("Could not find taxon: " + id);
			}

		List<Integer> result = new ArrayList<Integer>();
		for (NcbiTaxonomyNode child : node.getChildren())
			{
			result.add(child.getPayload());
			}
		return result;
		}
*/
	public List<Integer> findChildIds(Integer id)
		{
		List<Integer> result =
				(List<Integer>) (entityManager.createNamedQuery("NcbiTaxonomyNode.findChildIds").setParameter("id", id)
						                 .getResultList());

		// stupid hack because the root is its own child
		result.remove(new Integer(1));
		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	/*public List<PhylogenyNode<Integer>> getAncestorPath(NcbiTaxonomyNode n)
		{
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();
		NcbiTaxonomyNode trav = n;

		while (trav != null)
			{
			result.add(0, trav);
			trav = trav.getParent();
			}

		return result;
		}*/

	// --------------------- Interface NcbiTaxonomyNodeDao ---------------------

	// same as findById

	/*	@Transactional(propagation = Propagation.SUPPORTS, noRollbackFor = javax.persistence.NoResultException.class)
   public NcbiTaxonomyNode findByTaxId(Integer taxid)
	   {
	   return (NcbiTaxonomyNode) (entityManager.createNamedQuery("NcbiTaxonomyNode.findByTaxId")
			   .setParameter("taxid", taxid).getSingleResult());
	   }*/
//	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = javax.persistence.NoResultException.class)
	public List<NcbiTaxonomyNode> findByRank(String rankName)
		{
		return (List<NcbiTaxonomyNode>) (entityManager.createNamedQuery("NcbiTaxonomyNode.findByRank")
				                                 .setParameter("rank", rankName).getResultList());
		}

	//	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = javax.persistence.NoResultException.class)
	public List<Integer> findIdsByRank(String rankName)
		{
		return (List<Integer>) (entityManager.createNamedQuery("NcbiTaxonomyNode.findIdsByRank")
				                        .setParameter("rank", rankName).getResultList());
		}

	public List<Integer> findAllIds()
		{
		return (List<Integer>) (entityManager.createNamedQuery("NcbiTaxonomyNode.findAllIds").getResultList());
		}
	}
