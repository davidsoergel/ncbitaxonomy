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

/* $Id: PhyloUtilsServiceImpl.java 112 2008-04-21 23:34:53Z soergel $ */

package edu.berkeley.compbio.ncbitaxonomy;

import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNameDao;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyNode;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: soergel Date: Nov 6, 2006 Time: 2:21:41 PM To change this template use File |
 * Settings | File Templates.
 */
public class PhyloUtilsServiceImpl
	{
	// ------------------------------ FIELDS ------------------------------

	protected static Logger logger = Logger.getLogger(PhyloUtilsServiceImpl.class);
	private NcbiTaxonomyNameDao ncbiTaxonomyNameDao;
	private NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao;

	private Map<String, Integer> taxIdByNameRelaxed = new HashMap<String, Integer>();
	private Map<String, Integer> taxIdByName = new HashMap<String, Integer>();
	private Map<Integer, Integer> nearestKnownAncestorCache = new HashMap<Integer, Integer>();

	private RootedPhylogeny<Integer> ciccarelliTree;
	private String ciccarelliFilename = "tree_Feb15_unrooted.txt";


	// --------------------------- CONSTRUCTORS ---------------------------

	//private static HibernateDB ncbiDb;
	/*
		{
		try
			{
			logger.info("Initializing NCBI taxonomy database connection...");
			init();
			}
		catch (PhyloUtilsException e)
			{
			e.printStackTrace();
			logger.error(e);
			}
		}
*/

	public PhyloUtilsServiceImpl()// throws PhyloUtilsException
		{
		try
			{
			readStateIfAvailable();

			URL res = ClassLoader.getSystemResource(ciccarelliFilename);
			InputStream is = res.openStream();
			/*if (is == null)
				{
				is = new FileInputStream(filename);
				}*/
			ciccarelliTree = new NewickParser<Integer>().read(is, new IntegerNodeNamer(100000000));
			}
		catch (IOException e)
			{
			e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
			logger.error(e);
			}
		catch (PhyloUtilsException e)
			{
			e.printStackTrace();//To change body of catch statement use File | Settings | File Templates.
			logger.error(e);
			}

		/*
		ncbiDb = new HibernateDB("ncbiTaxonomy");
		if (ncbiDb == null)
			{
			throw new PhyloUtilsException("Couldn't connect to NCBI Taxonomy database");
			}
			*/
		}

	private void readStateIfAvailable()
		{
		try
			{
			FileInputStream fin = new FileInputStream("/tmp/edu.berkeley.compbio.phyloutils.cache");
			ObjectInputStream ois = new ObjectInputStream(fin);
			taxIdByNameRelaxed = (Map<String, Integer>) ois.readObject();
			taxIdByName = (Map<String, Integer>) ois.readObject();
			nearestKnownAncestorCache = (Map<Integer, Integer>) ois.readObject();
			ois.close();
			}
		catch (Exception e)
			{// no problem
			//e.printStackTrace();
			}
		}

	// --------------------- GETTER / SETTER METHODS ---------------------

	public void setNcbiTaxonomyNameDao(NcbiTaxonomyNameDao ncbiTaxonomyNameDao)
		{
		this.ncbiTaxonomyNameDao = ncbiTaxonomyNameDao;
		}

	public void setNcbiTaxonomyNodeDao(NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao)
		{
		this.ncbiTaxonomyNodeDao = ncbiTaxonomyNodeDao;
		}

	// -------------------------- OTHER METHODS --------------------------

	@Transactional(propagation = Propagation.SUPPORTS)
	public Integer commonAncestorID(Set<Integer> mergeIds) throws PhyloUtilsException
		{
		mergeIds.remove(null);
		Set<Integer> knownMergeIds = new HashSet<Integer>();

		for (Integer id : mergeIds)
			{
			knownMergeIds.add(nearestKnownAncestor(id));
			}

		if (knownMergeIds.size() == 1)
			{
			return knownMergeIds.iterator().next();
			}

		return ciccarelliTree.commonAncestor(knownMergeIds);
		}

	/*
   public static HibernateDB getNcbiDb()
	   {
	   return ncbiDb;
	   }*/

	@Transactional(propagation = Propagation.SUPPORTS)
	public int commonAncestorID(Integer taxIdA, Integer taxIdB) throws PhyloUtilsException
		{
		if (taxIdA == null)
			{
			return taxIdB;
			}
		if (taxIdB == null)
			{
			return taxIdA;
			}

		taxIdA = nearestKnownAncestor(taxIdA);
		taxIdB = nearestKnownAncestor(taxIdB);


		if (taxIdA == taxIdB)
			{
			return taxIdA;
			}

		return ciccarelliTree.commonAncestor(taxIdA, taxIdB);
		}

	@Transactional(propagation = Propagation.SUPPORTS)
	public double exactDistanceBetween(String speciesNameA, String speciesNameB) throws PhyloUtilsException
		{
		Integer taxIdA = taxIdByName.get(speciesNameA);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomyNameDao.findByName(speciesNameA).getTaxon().getId();
			taxIdByName.put(speciesNameA, taxIdA);
			}
		Integer taxIdB = taxIdByName.get(speciesNameB);
		if (taxIdB == null)
			{
			taxIdB = ncbiTaxonomyNameDao.findByName(speciesNameB).getTaxon().getId();
			taxIdByName.put(speciesNameB, taxIdB);
			}
		//logger.error(speciesNameA + " -> " + taxIdA);
		//logger.error(speciesNameB + " -> " + taxIdB);

		return exactDistanceBetween(taxIdA, taxIdB);
		}

	/**
	 * For each species, walk up the NCBI tree until a node that is part of the Ciccarelli tree is found; then return the
	 * Ciccarelli distance.
	 *
	 * @param speciesNameA
	 * @param speciesNameB
	 * @return
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public double minDistanceBetween(String speciesNameA, String speciesNameB) throws PhyloUtilsException
		{
		if (speciesNameA.equals(speciesNameB))
			{
			return 0;// account for TreeUtils.computeDistance bug
			}
		Integer taxIdA = findTaxidByName(speciesNameA);
		Integer taxIdB = findTaxidByName(speciesNameB);

		return minDistanceBetween(taxIdA, taxIdB);
		}

	@Transactional(propagation = Propagation.REQUIRED)
	public Integer findTaxidByName(String speciesNameA) throws PhyloUtilsException
		{
		Integer taxIdA = taxIdByNameRelaxed.get(speciesNameA);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomyNameDao.findByNameRelaxed(speciesNameA).getTaxon().getId();
			taxIdByNameRelaxed.put(speciesNameA, taxIdA);
			}
		return taxIdA;
		}

	/**
	 * For each species, walk up the NCBI tree until a node that is part of the Ciccarelli tree is found; then return the
	 * Ciccarelli distance.
	 */
	@Transactional(propagation = Propagation.SUPPORTS)
	public double minDistanceBetween(int taxIdA, int taxIdB) throws PhyloUtilsException
		{
		if (taxIdA == taxIdB)
			{
			return 0;// account for TreeUtils.computeDistance bug
			}
		taxIdA = nearestKnownAncestor(taxIdA);
		taxIdB = nearestKnownAncestor(taxIdB);
		return exactDistanceBetween(taxIdA, taxIdB);
		}

	/**
	 * Search up the NCBI taxonomy until a node is encountered that is a leaf in the Ciccarelli taxonomy
	 *
	 * @param taxId
	 * @return
	 * @throws PhyloUtilsException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int nearestKnownAncestor(int taxId) throws PhyloUtilsException
		{
		Integer result = nearestKnownAncestorCache.get(taxId);
		if (result == null)
			{
			NcbiTaxonomyNode n;
			try
				{
				n = ncbiTaxonomyNodeDao.findByTaxId(taxId);
				}
			catch (NoResultException e)
				{
				throw new PhyloUtilsException("Taxon " + taxId + " does not exist in the NCBI taxonomy.");
				}
			while (ciccarelliTree.getNode(n.getId()) == null)
				{
				n = n.getParent();
				if (n.getId() == 1)
					{
					// arrived at root, too bad
					throw new PhyloUtilsException("Taxon " + taxId + " not found in tree.");
					}
				//ncbiDb.getEntityManager().refresh(n);
				}
			result = n.getId();
			nearestKnownAncestorCache.put(taxId, result);
			}
		//return n.getId();
		return result;
		}

	public double exactDistanceBetween(int taxIdA, int taxIdB) throws PhyloUtilsException
		{
		return ciccarelliTree.distanceBetween(taxIdA, taxIdB);
		}

	@Transactional(propagation = Propagation.REQUIRED)
	public int nearestKnownAncestor(String speciesName) throws PhyloUtilsException
		{
		return nearestKnownAncestor(findTaxidByName(speciesName));
		}

	public void saveState()
		{
		try
			{
			FileOutputStream fout = new FileOutputStream("/tmp/edu.berkeley.compbio.phyloutils.cache");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(taxIdByNameRelaxed);
			oos.writeObject(taxIdByName);
			oos.writeObject(nearestKnownAncestorCache);
			oos.close();
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}

	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
		{
		return ciccarelliTree.extractTreeWithLeaves(ids);
		}
	}
