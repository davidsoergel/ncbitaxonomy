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


package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNameDao;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides access to a MySQL database containing the NCBI taxonomy, translating database records into Java objects
 * using Hibernate, JPA, and Spring.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyServiceImpl
	{	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyServiceImpl.class);
	private NcbiTaxonomyNameDao ncbiTaxonomyNameDao;
	private NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao;

	private Map<String, Integer> taxIdByNameRelaxed = new HashMap<String, Integer>();
	private Map<String, Integer> taxIdByName = new HashMap<String, Integer>();

	private Map<Integer, Set<String>> synonyms = new HashMap<Integer, Set<String>>();

	// the nearest known ancestor only makes sense for a given rootPhylogeny, but that is passed in anew for each nearestKnownAncestor call.
	// we could make a Map<RootedPhylogeny, Map<Integer, Integer>>, but that seems like overkill when in practice the rootPhylogeny is
	// always the same one anyway
	private Map<Integer, Integer> nearestKnownAncestorCache = new HashMap<Integer, Integer>();

	private final Integer HASNOTAXID = -1;


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
			 logger.error(e);
			 }
		 }
 */


	/*
	  ncbiDb = new HibernateDB("ncbiTaxonomy");
	  if (ncbiDb == null)
		  {
		  throw new PhyloUtilsException("Couldn't connect to NCBI Taxonomy database");
		  }
		  */

	public NcbiTaxonomyServiceImpl()
		{
		readStateIfAvailable();
		}

	private void readStateIfAvailable()
		{
		try
			{
			FileInputStream fin = new FileInputStream(cacheFilename);
			ObjectInputStream ois = new ObjectInputStream(fin);
			taxIdByNameRelaxed = (Map<String, Integer>) ois.readObject();
			taxIdByName = (Map<String, Integer>) ois
					.readObject();			//nearestKnownAncestorCache = (Map<Integer, Integer>) ois.readObject();
			synonyms = (Map<Integer, Set<String>>) ois.readObject();
			ois.close();
			}
		catch (IOException e)
			{// no problem
			logger.warn("Failed to read NcbiTaxonomy cache; will query database from scratch");
			taxIdByNameRelaxed = new HashMap<String, Integer>();
			taxIdByName = new HashMap<String, Integer>();
			synonyms = new HashMap<Integer, Set<String>>();
			}
		catch (ClassNotFoundException e)
			{// no problem
			logger.warn("Failed to read NcbiTaxonomy cache; will query database from scratch");
			taxIdByNameRelaxed = new HashMap<String, Integer>();
			taxIdByName = new HashMap<String, Integer>();
			synonyms = new HashMap<Integer, Set<String>>();
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

	/*	@Transactional(propagation = Propagation.SUPPORTS)
	 public Integer commonAncestorID(RootedPhylogeny tree, Set<Integer> mergeIds) throws PhyloUtilsException
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

		 return tree.commonAncestor(knownMergeIds);
		 }
 */	/*
   public static HibernateDB getNcbiDb()
	   {
	   return ncbiDb;
	   }*/

	/*	@Transactional(propagation = Propagation.SUPPORTS)
	 public int commonAncestorID(RootedPhylogeny tree, Integer taxIdA, Integer taxIdB) throws NcbiTaxonomyException
		 {
		 if (taxIdA == null)
			 {
			 return taxIdB;
			 }
		 if (taxIdB == null)
			 {
			 return taxIdA;
			 }

		 taxIdA = nearestKnownAncestor(tree, taxIdA);
		 taxIdB = nearestKnownAncestor(tree, taxIdB);


		 if (taxIdA == taxIdB)
			 {
			 return taxIdA;
			 }

		 return tree.commonAncestor(taxIdA, taxIdB);
		 }
 */

	/*	@Transactional(propagation = Propagation.SUPPORTS)
   public double distanceBetween(String speciesNameA, String speciesNameB) throws NcbiTaxonomyException
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

	   return distanceBetween(taxIdA, taxIdB);
	   }
	   */

	//@Transactional(propagation = Propagation.REQUIRED)

	public Integer findTaxidByNameRelaxed(String speciesNameA) throws NoSuchNodeException
		{
		//sometimes the taxid is already in the string
		try
			{
			return Integer.parseInt(speciesNameA);
			}
		catch (NumberFormatException e)
			{
			// no problem, look for it by name then
			}

		Integer taxIdA = taxIdByNameRelaxed.get(speciesNameA);
		if (taxIdA == null)
			{
			taxIdA = ncbiTaxonomyNameDao.findByNameRelaxed(speciesNameA).getTaxon().getId();
			taxIdByNameRelaxed.put(speciesNameA, taxIdA);
			}
		return taxIdA;
		}

	//@Transactional(propagation = Propagation.REQUIRED)
	@Nullable
	public Integer findTaxidByName(String speciesNameA) throws NoSuchNodeException
		{
		//sometimes the taxid is already in the string
		try
			{
			return Integer.parseInt(speciesNameA);
			}
		catch (NumberFormatException e)
			{
			// no problem, look for it by name then
			}

		Integer taxIdA = taxIdByName.get(speciesNameA);

		if (taxIdA == null)
			{
			try
				{
				taxIdA = ncbiTaxonomyNameDao.findByName(speciesNameA).getTaxon().getId();
				}			//if (taxIdA == null)
			catch (NoSuchNodeException e)
				{
				taxIdA = HASNOTAXID;
				}
			taxIdByName.put(speciesNameA, taxIdA);
			}

		if (taxIdA.equals(HASNOTAXID))
			{
			throw new NoSuchNodeException("No taxId found for name: " + speciesNameA);
			}

		return taxIdA;
		}

	//@Transactional(propagation = Propagation.REQUIRED)
	@Nullable
	public Integer findParentTaxidByName(String speciesNameA) throws NoSuchNodeException
		{
		//sometimes the taxid is already in the string
		try
			{
			return Integer.parseInt(speciesNameA);
			}
		catch (NumberFormatException e)
			{
			// no problem, look for it by name then
			}

		Integer taxIdA = taxIdByName.get(speciesNameA);

		if (taxIdA == null)
			{
			try
				{
				taxIdA = ncbiTaxonomyNameDao.findByName(speciesNameA).getTaxon().getParent().getId();
				}			//if (taxIdA == null)
			catch (NoSuchNodeException e)
				{
				taxIdA = HASNOTAXID;
				}
			taxIdByName.put(speciesNameA, taxIdA);
			}

		if (taxIdA.equals(HASNOTAXID))
			{
			throw new NoSuchNodeException("No taxId found for name: " + speciesNameA);
			}

		return taxIdA;
		}


	public Collection<String> synonymsOf(String s) throws NoSuchNodeException
		{
		int taxid = findTaxidByName(s);
		Set<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		return result;
		}

	public Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException
		{
		int taxid = findTaxidByNameRelaxed(s);
		Set<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		return result;
		//return ncbiTaxonomyNameDao.findSynonymsRelaxed(s);
		}

	public Collection<String> synonymsOfParent(String s) throws NoSuchNodeException
		{
		int taxid = findParentTaxidByName(s);
		Set<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		return result;
		}


	String cacheFilename = "/tmp/edu.berkeley.compbio/ncbitaxonomy.cache";

	public void saveState()
		{
		try
			{
			File cacheFile = new File(cacheFilename);
			cacheFile.getParentFile().mkdirs();
			FileOutputStream fout = new FileOutputStream(cacheFile);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(taxIdByNameRelaxed);
			oos.writeObject(taxIdByName);			//	oos.writeObject(nearestKnownAncestorCache);
			oos.writeObject(synonyms);
			oos.close();
			}
		catch (Exception e)
			{
			logger.error("Error", e);
			}
		}

	//@Transactional(propagation = Propagation.REQUIRED)
	@NotNull
	public PhylogenyNode findNode(Integer taxid) throws NoSuchNodeException
		{
		try
			{
			return ncbiTaxonomyNodeDao.findById(taxid);
			}
		catch (NoResultException e)
			{
			throw new NoSuchNodeException(e);
			}
		}

	/**
	 * Search up the NCBI taxonomy until a node is encountered that is a leaf in the Ciccarelli taxonomy
	 *
	 * @param leafId
	 * @return
	 * @throws edu.berkeley.compbio.phyloutils.PhyloUtilsException
	 *
	 */	//@Transactional(propagation = Propagation.REQUIRED)
	public Integer findNearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId)
			throws NoSuchNodeException
		{
		// ** sanity check that the rootPhylogeny is always the same when using the cache
		Integer result = nearestKnownAncestorCache.get(leafId);
		if (result == null)
			{
			PhylogenyNode<Integer> n;

			n = findNode(leafId);

			/*if (n == null)
				{
				throw new NoSuchNodeException("Leaf phylogeny does not contain node " + leafId + ".");
				}*/

			//while (rootPhylogeny.getNode(n.getValue()) == null)
			while (true)
				{
				try
					{
					rootPhylogeny.getNode(n.getValue());

					// if we got here, we found a node
					break;
					}
				catch (NoSuchNodeException e)
					{
					if (logger.isDebugEnabled())
						{
						logger.debug("Node " + n + " not in root tree, trying parent: " + n.getParent());
						}
					n = n.getParent();
					if (n.getValue() == 1)
						{					// arrived at root, too bad
						throw new NoSuchNodeException("Taxon " + leafId + " not found in tree.");
						}				//ncbiDb.getEntityManager().refresh(n);
					}
				}
			result = n.getValue();
			nearestKnownAncestorCache.put(leafId, result);
			}		//return n.getId();
		return result;
		}
	}
