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
import com.davidsoergel.dsutils.tree.HierarchyNode;
import com.davidsoergel.stats.ContinuousDistribution1D;
import com.google.common.collect.Multiset;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.BasicPhylogenyNode;
import edu.berkeley.compbio.phyloutils.BasicRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.IntegerNodeNamer;
import edu.berkeley.compbio.phyloutils.LengthWeightHierarchyNode;
import edu.berkeley.compbio.phyloutils.PhyloUtilsException;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
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
 * Created by IntelliJ IDEA. User: soergel Date: May 7, 2007 Time: 2:03:39 PM To change this template use File |
 * Settings | File Templates.
 */
public class NcbiTaxonomyService extends AbstractRootedPhylogeny<Integer>//extends Singleton<PhyloUtilsService>
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyService.class);
	// ------------------------------ FIELDS ------------------------------

	private static final NcbiTaxonomyService instance = new NcbiTaxonomyService();

	private NcbiTaxonomyServiceImpl ncbiTaxonomyServiceImpl;


	// -------------------------- STATIC METHODS --------------------------

	public static NcbiTaxonomyService getInstance()
		{
		return instance;
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	public NcbiTaxonomyService()
		{
		try
			{
			File propsFile = PropertiesUtils
					.findPropertiesFile("NCBI_TAXONOMY_PROPERTIES", ".ncbitaxonomy", "ncbi_taxonomy.properties");
			logger.debug("Using properties file: " + propsFile);
			Properties p = new Properties();
			p.load(new FileInputStream(propsFile));
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
			e.printStackTrace();
			throw new RuntimeException("Could not load database properties for NCBI taxonomy", e);
			}
		}


	public void saveState()
		{
		ncbiTaxonomyServiceImpl.saveState();
		}

	public int findTaxidByName(String name) throws NcbiTaxonomyException
		{
		return ncbiTaxonomyServiceImpl.findTaxidByName(name);
		}

	public PhylogenyNode<Integer> getRoot()
		{
		return getNode(1);
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
	public double distanceBetween(Integer taxIdA, Integer taxIdB)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		//		return ncbiTaxonomyServiceImpl.distanceBetween(taxIdA, taxIdB);
		}

	public PhylogenyNode getNode(Integer taxid)//throws NcbiTaxonomyException
		{
		try
			{
			return ncbiTaxonomyServiceImpl.getNode(taxid);
			}
		catch (NoResultException e)
			{
			return null;
			//throw new NcbiTaxonomyException("Taxon " + taxid + " does not exist in the NCBI taxonomy.");
			}
		}


	/*	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
			 {
			 return ncbiTaxonomyServiceImpl.extractTreeWithLeaves(ids);
			 }
	 */
	public Collection<? extends PhylogenyNode<Integer>> getChildren()
		{
		return getNode(1).getChildren();
		}

	public PhylogenyNode<Integer> getChild(Integer id)
		{
		return getNode(1).getChild(id);
		}

	public Integer getValue()
		{
		return 1;// the root taxid of the ncbi taxonomy.
		}

	public PhylogenyNode getParent()
		{
		return null;
		}

	public HierarchyNode<? extends Integer, LengthWeightHierarchyNode<Integer>> newChild()
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public void setValue(Integer taxid)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}


	public void setParent(HierarchyNode<? extends Integer, LengthWeightHierarchyNode<Integer>> parent)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public boolean hasValue()
		{
		return true;
		}

	public List<PhylogenyNode<Integer>> getAncestorPath()
		{
		// this is the root node
		List<PhylogenyNode<Integer>> result = new LinkedList<PhylogenyNode<Integer>>();

		result.add(0, getNode(1));

		return result;
		}

	public Double getLength()
		{
		return 0.;
		}

	public void setLength(Double d)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public Double getLargestLengthSpan()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public void addChild(LengthWeightHierarchyNode<Integer> a)
		{
		throw new NotImplementedException();
		}


	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<LengthWeightHierarchyNode<Integer>> iterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	public DepthFirstTreeIterator<Integer, LengthWeightHierarchyNode<Integer>> depthFirstIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	public DepthFirstTreeIterator<Integer, LengthWeightHierarchyNode<Integer>> phylogenyIterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}

	public Collection<PhylogenyNode<Integer>> getNodes()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public Collection<PhylogenyNode<Integer>> getLeaves()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public Collection<Integer> getLeafValues()
		{
		throw new NotImplementedException("Loading the entire NCBI taxonomy into a Collection is probably a bad idea");
		}

	public Integer nearestKnownAncestor(RootedPhylogeny<Integer> rootPhylogeny, Integer leafId)
			throws PhyloUtilsException
		{
		return ncbiTaxonomyServiceImpl.nearestKnownAncestor(rootPhylogeny, leafId);
		}

	public Integer nearestAncestorWithBranchLength(Integer leafId) throws PhyloUtilsException
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public boolean isLeaf()
		{
		return getRoot().isLeaf();
		}

	public Double getWeight()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}


	public void setWeight(Double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public void incrementWeightBy(double v)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public void propagateWeightFromBelow()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public double distanceToRoot()
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide branch lengths.");
		}

	public void randomizeLeafWeights(ContinuousDistribution1D dist)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}

	public RootedPhylogeny<Integer> clone()
		{
		throw new NotImplementedException();
		}

	public void setLeafWeights(Multiset<Integer> ids)
		{
		throw new NotImplementedException("The NCBI Taxonomy does not provide weights.");
		}


	public RootedPhylogeny<Integer> convertToIntegerIDTree(RootedPhylogeny<String> stringTree)
		//	throws NcbiTaxonomyException
		{
		if (stringTree.getBasePhylogeny() != null)
			{
			logger.warn("Converting an extracted subtree from String IDs to Integer IDs; base phylogeny gets lost");
			}

		if (stringTree.getParent() != null)
			{
			logger.warn(
					"Rooted phylogeny shouldn't have a parent; dropping it in conversion from String IDs to Integer IDs");
			}

		// this duplicates convertToIntegerIDNode just so we operate on a BasicRootedPhylogeny instead of a PhylogenyNode

		BasicRootedPhylogeny<Integer> result = new BasicRootedPhylogeny<Integer>();
		copyValuesToNode(stringTree, result);
		IntegerNodeNamer namer = new IntegerNodeNamer(10000000);
		try
			{
			result.updateNodes(namer);
			}
		catch (PhyloUtilsException e)
			{
			// impossible
			logger.debug(e);
			e.printStackTrace();
			throw new Error(e);
			}
		return result;
		}

	private PhylogenyNode<Integer> convertToIntegerIDNode(
			PhylogenyNode<String> stringNode)//throws NcbiTaxonomyException
		{
		PhylogenyNode<Integer> result = new BasicPhylogenyNode<Integer>();
		copyValuesToNode(stringNode, result);
		return result;
		}

	private void copyValuesToNode(PhylogenyNode<String> stringNode, PhylogenyNode<Integer> result)
		{
		result.setLength(stringNode.getLength());
		result.setWeight(stringNode.getWeight());

		Integer id = null;
		try
			{
			id = findTaxidByName(stringNode.getValue());
			}
		catch (NcbiTaxonomyException e)
			{
			//id = namer.generate(); //nameInternal(unknownCount)
			}
		result.setValue(id);

		for (PhylogenyNode<String> node : stringNode.getChildren())
			{
			result.addChild(convertToIntegerIDNode(node));
			}
		}
	}
