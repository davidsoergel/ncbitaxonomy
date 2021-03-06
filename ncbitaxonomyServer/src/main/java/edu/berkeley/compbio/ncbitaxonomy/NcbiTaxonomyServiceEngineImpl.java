/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.CacheManager;
import com.davidsoergel.dsutils.DSStringUtils;
import com.davidsoergel.trees.NoSuchNodeException;
import com.davidsoergel.trees.PhylogenyNode;
import com.davidsoergel.trees.RootedPhylogeny;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNameDao;
import edu.berkeley.compbio.ncbitaxonomy.dao.NcbiTaxonomyNodeDao;
import edu.berkeley.compbio.ncbitaxonomy.jpa.NcbiTaxonomyNode;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides access to a MySQL database containing the NCBI taxonomy, translating database records into Java objects
 * using Hibernate, JPA, and Spring.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
@Service
public class NcbiTaxonomyServiceEngineImpl implements NcbiTaxonomyServiceEngine
	{


	// ------------------------------ FIELDS ------------------------------

	private static final Logger logger = Logger.getLogger(NcbiTaxonomyServiceEngineImpl.class);

	@Autowired
	private NcbiTaxonomyNameDao ncbiTaxonomyNameDao;

	@Autowired
	private NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao;

	private Map<String, Integer> taxIdByNameRelaxed = new HashMap<String, Integer>();
	private Map<String, Integer> taxIdByName = new HashMap<String, Integer>();

	private Map<Integer, HashSet<String>> synonyms = new HashMap<Integer, HashSet<String>>();
	private Map<Integer, String> scientificNames = new HashMap<Integer, String>();


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
			 logger.error("Error", e);
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

	public NcbiTaxonomyServiceEngineImpl()
		{
		readStateIfAvailable();
		}

	private void readStateIfAvailable()
		{
		taxIdByNameRelaxed =
				CacheManager.getAccumulatingMap(this, "taxIdByNameRelaxed"); //, new HashMap<String, Integer>());
		taxIdByName = CacheManager.getAccumulatingMap(this, "taxIdByName"); //, new HashMap<String, Integer>());
		synonyms = CacheManager.getAccumulatingMap(this, "synonyms"); //, new HashMap<Integer, Set<String>>());
		scientificNames = CacheManager.getAccumulatingMap(this, "scientificNames");
/*
	 if (stringNearestKnownAncestorCache == null)
		 {
		 stringNearestKnownAncestorCache = new HashMap<String, Integer>();
		 }
	 if (integerNearestKnownAncestorCache == null)
		 {
		 integerNearestKnownAncestorCache = new HashMap<Integer, Integer>();
		 }
	 if (integerNearestAncestorWithBranchLengthCache == null)
		 {
		 integerNearestAncestorWithBranchLengthCache = new HashMap<Integer, Integer>();
		 }*/
		}
	/*
	private void readStateIfAvailable()
		{
		try
			{
			FileInputStream fin = new FileInputStream(EnvironmentUtils.getCacheRoot() + cacheFilename);
			ObjectInputStream ois = new ObjectInputStream(fin);
			taxIdByNameRelaxed = (Map<String, Integer>) ois.readObject();
			taxIdByName = (Map<String, Integer>) ois.readObject();
			//nearestKnownAncestorCache = (Map<Integer, Integer>) ois.readObject();
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
		}*/

	// --------------------- GETTER / SETTER METHODS ---------------------

/*	public void setNcbiTaxonomyNameDao(NcbiTaxonomyNameDao ncbiTaxonomyNameDao)
		{
		this.ncbiTaxonomyNameDao = ncbiTaxonomyNameDao;
		}

	public void setNcbiTaxonomyNodeDao(NcbiTaxonomyNodeDao ncbiTaxonomyNodeDao)
		{
		this.ncbiTaxonomyNodeDao = ncbiTaxonomyNodeDao;
		}
*/
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
	public Integer findTaxidByName(@NotNull String speciesNameA) throws NoSuchNodeException
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


	public Set<String> getCachedNamesForId(Integer id)
		{
		//PERF, need a BiMultiMap or something
		Set<String> result = new HashSet<String>();
		for (Map.Entry<String, Integer> entry : taxIdByName.entrySet())
			{
			if (entry.getValue().equals(id))
				{
				result.add(entry.getKey());
				}
			}
		for (Map.Entry<String, Integer> entry : taxIdByNameRelaxed.entrySet())
			{
			if (entry.getValue().equals(id))
				{
				result.add(entry.getKey());
				}
			}
		return result;
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

	public Collection<String> synonymsOfIdNoCache(int taxid) throws NoSuchNodeException
		{
		return ncbiTaxonomyNameDao.findSynonyms(taxid);
		}

	public Collection<String> synonymsOf(String s) throws NoSuchNodeException
		{
		int taxid = findTaxidByName(s);
		HashSet<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		return result;
		}

	public Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException
		{

		logger.info("NcbiTaxonomyServer synonymsOfRelaxed " + s);
		int taxid = findTaxidByNameRelaxed(s);
		HashSet<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		logger.info("NcbiTaxonomyServer synonymsOfRelaxed " + s + " DONE");
		return result;
		//return ncbiTaxonomyNameDao.findSynonymsRelaxed(s);
		}

	public Collection<String> synonymsOfParent(String s) throws NoSuchNodeException
		{
		int taxid = findParentTaxidByName(s);
		HashSet<String> result = synonyms.get(taxid);
		if (result == null)
			{
			result = new HashSet<String>(ncbiTaxonomyNameDao.findSynonyms(taxid));
			synonyms.put(taxid, result);
			}
		return result;
		}

/*
	String cacheFilename = "/ncbitaxonomy.cache";

	public void saveState()
		{
		try
			{
			File cacheFile = new File(EnvironmentUtils.getCacheRoot() + cacheFilename);
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
		}*/

	//@Transactional(propagation = Propagation.REQUIRED)

	@NotNull
	//@Transactional
	public NcbiTaxonomyNode findNode(Integer taxid) throws NoSuchNodeException
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


	// @Transactional
	public void toNewick(final Writer out, final String prefix, final String tab, final int minClusterSize,
	                     final double minLabelProb) throws IOException
		{
		toNewick(1, out, prefix, tab, minClusterSize, minLabelProb);
		out.write(";");
		}

	// @Transactional
	private void toNewick(int id, final Writer out, final String prefix, final String tab, final int minClusterSize,
	                      final double minLabelProb) throws IOException
		{

		List<Integer> childIds = ncbiTaxonomyNodeDao.findChildIds(id);


		if (prefix != null)
			{
			out.write(prefix);
			}

		if (!childIds.isEmpty())
			{

			String childPrefix = prefix == null ? null : prefix + tab;
			out.write("(");
			Iterator<Integer> i = childIds.iterator();
			while (i.hasNext())
				{
				toNewick(i.next(), out, childPrefix, tab, minClusterSize, minLabelProb);
				//sb.append(i.next());
				if (i.hasNext())
					{
					out.write(",");
					}
				}
			if (prefix != null)
				{
				out.write(prefix);
				}
			out.write(")");
			if (prefix != null)
				{
				out.write(prefix);
				}
			}
		//String n = value.toString();
		//n = n.replaceAll(" ", "_");
		out.write("" + id);
		}

	public void writeSynonyms(final Writer out)
		{
		//DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> i = getTree().depthFirstIterator();
		Iterator<Integer> i = ncbiTaxonomyNodeDao.findAllIds().iterator(); //
		while (i.hasNext())
			{
			//PhylogenyNode<Integer> n = i.next();
			//Integer id = n.getPayload();
			Integer id = i.next();
			try
				{
				out.append(id.toString()).append("\t");

				String scientificName = findScientificName(id);
				out.append(scientificName).append("\t");

				Collection<String> synonyms = synonymsOfIdNoCache(id);
				synonyms.remove(scientificName);
				out.append(DSStringUtils.join(synonyms, "\t")).append("\n");
				}
			catch (NoSuchNodeException e)
				{
				logger.error("Error", e);
				}
			catch (IOException e)
				{
				logger.error("Error", e);
				}
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
					rootPhylogeny.getNode(n.getPayload());

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
					if (n.getPayload() == 1)
						{					// arrived at root, too bad
						throw new NoSuchNodeException("Taxon " + leafId + " has no ancestors in provided tree.");
						}				//ncbiDb.getEntityManager().refresh(n);
					}
				}
			result = n.getPayload();
			nearestKnownAncestorCache.put(leafId, result);
			}		//return n.getId();
		return result;
		}


	/**
	 * Search up the NCBI taxonomy until a node is encountered that is of the requested rank
	 *
	 * @param leafId
	 * @return
	 * @throws edu.berkeley.compbio.phyloutils.PhyloUtilsException
	 *
	 */	//@Transactional(propagation = Propagation.REQUIRED)
	public Integer findNearestAncestorAtRank(@NotNull final String rankName, Integer leafId) throws NoSuchNodeException
		{
		NcbiTaxonomyNode n;

		n = findNode(leafId);

		/*if (n == null)
			  {
			  throw new NoSuchNodeException("Leaf phylogeny does not contain node " + leafId + ".");
			  }*/

		//while (rootPhylogeny.getNode(n.getValue()) == null)
		while (true)
			{
			if (rankName.equals(n.getRank()))
				{
				return n.getPayload();
				}

			n = n.getParent();
			if (n.getPayload() == 1)
				{					// arrived at root, too bad
				throw new NoSuchNodeException("Taxon " + leafId + " has no ancestors of rank " + rankName);
				}				//ncbiDb.getEntityManager().refresh(n);
			}
		}


	public Set<Integer> findTaxIdsWithRank(String rank)
		{
		/*
		Set<Integer> result = new HashSet<Integer>();
		List<NcbiTaxonomyNode> nodes = ncbiTaxonomyNodeDao.findByRank(rankName);
		for (NcbiTaxonomyNode node : nodes)
			{
			result.add(node.getId());
			}
		return result;
		*/

		return new HashSet<Integer>(ncbiTaxonomyNodeDao.findIdsByRank(rank));
		}

	public Set<String> findNamesWithRank(String rank)
		{
		Set<String> result = new HashSet<String>();
		List<NcbiTaxonomyNode> nodes = ncbiTaxonomyNodeDao.findByRank(rank);
		for (NcbiTaxonomyNode node : nodes)
			{
			result.add(node.getScientificName());
			}
		return result;
		}

	@NotNull
	public String findScientificName(final Integer taxid) throws NoSuchNodeException
		{
		String result = scientificNames.get(taxid);
		if (result == null)
			{
			result = findNode(taxid).getScientificName();
			scientificNames.put(taxid, result);
			}
		return result;
		}
	}


