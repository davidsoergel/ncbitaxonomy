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

import com.davidsoergel.dsutils.PropertiesUtils;
import com.davidsoergel.dsutils.tree.DepthFirstTreeIterator;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
import com.davidsoergel.dsutils.tree.TreeException;
import com.davidsoergel.stats.ContinuousDistribution1D;
import com.google.common.collect.Multiset;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhyloUtilsRuntimeException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import edu.berkeley.compbio.phyloutils.TaxonomyService;
import edu.berkeley.compbio.phyloutils.TaxonomySynonymService;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Presents the NCBI taxonomy as an RootedPhylogeny with Integer IDs corresponding to the NCBI taxids.  This is a live
 * view onto a MySQL database imported from ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdump.tar.gz.  The database connection
 * is configured by placing a file at ~/.ncbitaxonomy/ncbitaxonomy.properties, containing something like this:
 * <p/>
 * <pre>
 * default = orchid_test
 * <p/>
 * localhost_test.driver = com.mysql.jdbc.Driver
 * localhost_test.url = jdbc:mysql://localhost/ncbi_tx_test
 * localhost_test.username = ncbi_tx_test
 * localhost_test.password = xxxxxx
 * <p/>
 * orchid_test.driver = com.mysql.jdbc.Driver
 * orchid_test.url = jdbc:mysql://orchid.berkeley.edu/ncbi_tx_test
 * orchid_test.username = ncbi_tx_test
 * orchid_test.password = xxxxxx
 * <p/>
 * </pre>
 * <p/>
 * Many methods required by the RootedPhylogeny interface are not appropriate for this purpose and hence throw
 * NotImplementedException (perhaps this is an indication that the interface should be refactored, e.g. separated into a
 * mutable vs. immutable varieties, etc.; oh well).
 * <p/>
 * Those methods that are functional are largely delegated to NcbiTaxonomyServiceImpl, which is instantiated here and
 * configured by Spring.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyService extends AbstractRootedPhylogeny<Integer>
		implements TaxonomyService<Integer>, TaxonomySynonymService //extends Singleton<PhyloUtilsService>
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyService.class);
	// ------------------------------ FIELDS ------------------------------

	private static NcbiTaxonomyService instance;// = new NcbiTaxonomyService();

	private NcbiTaxonomyServiceImpl ncbiTaxonomyServiceImpl;

	// -------------------------- STATIC METHODS --------------------------

	public static NcbiTaxonomyService getInstance()
		{
		if (instance == null)
			{
			instance = new NcbiTaxonomyService();
			}
		return instance;
		}

	public static NcbiTaxonomyService getInjectedInstance()
		{
		return instance;
		}

	public static void setInjectedInstance(NcbiTaxonomyService instance)
		{
		NcbiTaxonomyService.instance = instance;
		}


	public RootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold)
		{
		throw new NotImplementedException();
		}

	public RootedPhylogeny<Integer> getRandomSubtree(int numTaxa, Double mergeThreshold, Integer exceptDescendantsOf)
			throws NoSuchNodeException, TreeException
		{
		throw new NotImplementedException();
		}
	// --------------------------- CONSTRUCTORS ---------------------------

	public boolean isLeaf(Integer leafId) throws NoSuchNodeException
		{
		return getNode(leafId).isLeaf();
		}

	public NcbiTaxonomyService()
		{
		try
			{
			File propsFile = PropertiesUtils
					.findPropertiesFile("NCBI_TAXONOMY_PROPERTIES", ".ncbitaxonomy", "ncbi_taxonomy.properties");
			logger.debug("Using properties file: " + propsFile);
			Properties p = new Properties();
			FileInputStream is = null;
			try
				{
				is = new FileInputStream(propsFile);
				p.load(is);
				}
			finally
				{
				is.close();
				}
			String dbName = (String) p.get("default");

			Map<String, Properties> databases = PropertiesUtils.splitPeriodDelimitedProperties(p);


			GenericApplicationContext ctx = new GenericApplicationContext();
			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
			xmlReader.loadBeanDefinitions(new ClassPathResource("ncbitaxonomy.xml"));

			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
			cfg.setProperties(databases.get(dbName));
			ctx.addBeanFactoryPostProcessor(cfg);

			ctx.refresh();

			// add a shutdown hook for the above context...
			ctx.registerShutdownHook();

			ncbiTaxonomyServiceImpl = ((NcbiTaxonomyServiceImpl) ctx.getBean("ncbiTaxonomyServiceImpl"));

			// we've got what we need, so we can ditch the context already
			// no, that breaks transactioning
			//ctx.close();
			}
		catch (Exception e)
			{
			logger.error("Error", e);
			throw new RuntimeException("Could not load database properties for NCBI taxonomy", e);
			}
		}

	/**
	 * Cache database search results for the current session in a local file in /tmp/edu.berkeley.compbio/ncbitaxonomy.cache,
	 * since it'll be much faster to load that up again in the future than to rerun all the database queries.  The
	 * assumption is that the database isn't changing anyway; if it does change, deleting the cache file will allow the new
	 * data to take effect.
	 */
/*	public void saveState()
		{
		ncbiTaxonomyServiceImpl.saveState();
		}
*/

	/**
	 * Find the taxid corresponding to the given taxon name.
	 *
	 * @param name the name of the taxon, matching any name in the NCBI names table (including scientific names,
	 *             misspellings, etc.)
	 * @return the taxid for the given name, if found
	 * @throws NcbiTaxonomyException when the name is not found, or if the name maps to multiple taxids
	 */
	public Integer findTaxidByName(String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.findTaxidByName(name);
		}

	/**
	 * Find the taxid corresponding to the given taxon name, removing space-delimited words from the end of the name one at
	 * a time until a match is found.  The idea is to find the species node when a name is given that includes more
	 * detailed information, such as a strain identifier.  Similarly, will match the genus if the species name does not
	 * exist.
	 *
	 * @param name the name of the taxon, with a prefix matching any name in the NCBI names table (including scientific
	 *             names, misspellings, etc.)
	 * @return the taxid for the given name, if found
	 * @throws NoSuchNodeException when the name is not found, or if the name maps to multiple taxids
	 */
	public Integer findTaxidByNameRelaxed(String name) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.findTaxidByNameRelaxed(name);
		}


	public Collection<String> synonymsOf(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.synonymsOf(s);
		}

	public Collection<String> synonymsOfParent(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.synonymsOfParent(s);
		}

	public Collection<String> synonymsOfRelaxed(String s) throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.synonymsOfRelaxed(s);
		}

	public PhylogenyNode<Integer> findRoot()
		{
		return getRoot();
		}

	/**
	 * {@inheritDoc}
	 */
	//@Override
	public PhylogenyNode<Integer> getRoot()
		{
		try
			{
			return getNode(1);
			}
		catch (NoSuchNodeException e)
			{
			logger.error("Ncbi taxonomy has no root!", e);
			throw new PhyloUtilsRuntimeException(e, "Ncbi taxonomy has no root!");
			}
		}

	/*	public Integer commonAncestor(Set<Integer> knownMergeIds)
			 {
			 return ncbiTaxonomyServiceImpl.commonAncestorID(knownMergeIds);
			 }

		 public Integer commonAncestor(Integer taxIdA, Integer taxIdB)
			 {
			 return ncbiTaxonomyServiceImpl.commonAncestorID(taxIdA, taxIdB);
			 }
	 */

	/**
	 * Not implemented
	 */
	@Override
	public double distanceBetween(Integer taxIdA, Integer taxIdB)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//		return ncbiTaxonomyServiceImpl.distanceBetween(taxIdA, taxIdB);
		}

	@Override
	public double distanceBetween(PhylogenyNode<Integer> a, PhylogenyNode<Integer> b)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public PhylogenyNode<Integer> nearestAncestorWithBranchLength(PhylogenyNode<Integer> id)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public Double minDistanceBetween(PhylogenyNode<Integer> node1, PhylogenyNode<Integer> node2)
			throws PhyloUtilsException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double minDistanceBetween(Integer taxIdA, Integer taxIdB)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//		return ncbiTaxonomyServiceImpl.distanceBetween(taxIdA, taxIdB);
		}

	public void setSynonymService(TaxonomySynonymService taxonomySynonymService)
		{
		logger.warn("NCBI taxonomy doesn't currently use other synonym services for any purpose; ignoring");
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getNode(Integer taxid) throws NoSuchNodeException//throws NcbiTaxonomyException
		{
		try
			{
			return ncbiTaxonomyServiceImpl.findNode(taxid);
			}
		catch (NoResultException e)
			{
			throw new NoSuchNodeException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");
			//return null;
			//throw new NcbiTaxonomyException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");

			//throw new NoSuchElementException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");
			}
		}


	/**
	 * {@inheritDoc}
	 */
	/*	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
				 {
				 return ncbiTaxonomyServiceImpl.extractTreeWithLeaves(ids);
				 }
		 */
	@NotNull
	public List<? extends PhylogenyNode<Integer>> getChildren()
		{
		return getRoot().getChildren();
		}

	/**
	 * {@inheritDoc}
	 */
	@NotNull
	public PhylogenyNode<Integer> getChild(Integer id) throws NoSuchNodeException
		{
		return getRoot().getChild(id);
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer getValue()
		{
		return 1;// the root taxid of the ncbi taxonomy.
		}

	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode getParent()
		{
		return null;
		}

	/**
	 * Not implemented
	 */
	public PhylogenyNode<Integer> newChild()
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * Not implemented
	 */
	public void setValue(Integer taxid)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}


	/**
	 * {@inheritDoc}
	 */
	public void setParent(PhylogenyNode<Integer> parent)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasValue()
		{
		return true;
		}

	/**
	 * {@inheritDoc}
	 */
	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		// this is the root node
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();

		result.add(0, getRoot());

		return result;
		}

	/**
	 * {@inheritDoc}
	 */
	public Double getLength()
		{
		return 0.;
		}

	/**
	 * Not implemented
	 */
	public void setLength(Double d)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getLargestLengthSpan()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getGreatestDepthBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public double getGreatestDepthBelow(Integer a)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public double maxDistance()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	public void registerChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}

	/**
	 * Not implemented
	 */
	public void unregisterChild(PhylogenyNode<Integer> a)
		{
		throw new NotImplementedException();
		}


	/**
	 * Not implemented
	 */
	public Iterator<PhylogenyNode<Integer>> iterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> depthFirstIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public DepthFirstTreeIterator<Integer, PhylogenyNode<Integer>> phylogenyIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	public void toNewick(StringBuffer sb, String prefix, String tab, int minClusterSize, double minLabelProb)
		{
		throw new NotImplementedException("Printing the entire NCBI taxonomy is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public Collection<PhylogenyNode<Integer>> getLeaves()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public Collection<Integer> getLeafValues()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	/**
	 * Not implemented
	 */
	public Collection<Integer> getNodeValues()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public Map<Integer, PhylogenyNode<Integer>> getUniqueIdToNodeMap()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	/**
	 * {@inheritDoc}
	 */
	public Integer nearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId)
			throws NoSuchNodeException
		{
		return ncbiTaxonomyServiceImpl.findNearestKnownAncestor(rootPhylogeny, leafId);
		}

	/**
	 * Not implemented
	 */
	public Integer nearestAncestorWithBranchLength(Integer leafId)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLeaf()
		{
		return getRoot().isLeaf();
		}

	/**
	 * Not implemented
	 */
	public Double getWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public Double getCurrentWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public void setWeight(Double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	public void incrementWeightBy(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/*
	 public void propagateWeightFromBelow()
		 {
		 throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		 }
 */

	/**
	 * Not implemented
	 */
	public double distanceToRoot()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	/**
	 * Not implemented
	 */
	@Override
	public void randomizeLeafWeights(ContinuousDistribution1D dist)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	/**
	 * Not implemented
	 */
	@Override
	public RootedPhylogeny<Integer> clone()
		{
		throw new NotImplementedException();
		}

	/**
	 * Not implemented
	 */
	@Override
	public void setLeafWeights(Multiset<Integer> ids)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}


	/**
	 * {@inheritDoc}
	 */
	public PhylogenyNode<Integer> getSelfNode()
		{
		return getRoot();
		}


	/**
	 * Not implemented
	 */
	public void appendSubtree(StringBuffer sb, String indent)
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a String is probably a bad idea");
		}

	public RootedPhylogeny<Integer> getTree()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Tree is probably a bad idea");
		}

	public PhylogenyNode<Integer> nearestAncestorWithBranchLength() throws NoSuchNodeException
		{
		throw new NoSuchNodeException("Root doesn't have a branch length.");
		}

	public double getDepthFromRoot(Integer b) throws NoSuchNodeException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//return stringTaxonomyService.minDistanceBetween(intToNodeMap.get(a), intToNodeMap.get(b));
		//	return exactDistanceBetween(name1, name2);
		}

	@Override
	public String toString()
		{
		String shortname = getClass().getName();
		shortname = shortname.substring(shortname.lastIndexOf(".") + 1);
		return shortname;
		}
	}
