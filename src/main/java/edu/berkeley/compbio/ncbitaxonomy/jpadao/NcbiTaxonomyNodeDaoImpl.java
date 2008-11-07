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
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyNode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

@Repository
public class NcbiTaxonomyNodeDaoImpl extends GenericDaoImpl<NcbiTaxonomyNode> implements NcbiTaxonomyNodeDao
	{
	// ------------------------------ FIELDS ------------------------------

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
	public NcbiTaxonomyNode findById(Integer id)
		{
		NcbiTaxonomyNode node = entityManager.find(NcbiTaxonomyNode.class, id);
		// eagerly load the path
		node.getAncestorPath();
		return node;
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
	}
