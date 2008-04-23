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

/* $Id: PhyloUtilsService.java 112 2008-04-21 23:34:53Z soergel $ */

package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.HierarchyNode;
import com.davidsoergel.dsutils.PropertiesUtils;
import edu.berkeley.compbio.phyloutils.AbstractRootedPhylogeny;
import edu.berkeley.compbio.phyloutils.PhylogenyNode;
import edu.berkeley.compbio.phyloutils.RootedPhylogeny;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

	public PhylogenyNode getNode(Integer taxid)
		{
		return ncbiTaxonomyServiceImpl.getNode(taxid);
		}

	public Collection<PhylogenyNode<Integer>> getNodes()
		{
		return null;
		}

	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
		{
		return ncbiTaxonomyServiceImpl.extractTreeWithLeaves(ids);
		}

	public Set<? extends PhylogenyNode<Integer>> getChildren()
		{
		return null;
		}

	public Integer getValue()
		{
		return 1;// the root taxid of the ncbi taxonomy.
		}

	public PhylogenyNode getParent()
		{
		return null;
		}

	public HierarchyNode<? extends Integer> newChild()
		{
		return null;
		}

	public void setValue(Integer taxid)
		{
		throw new NotImplementedException("The NCBI taxonomy is not editable");
		}

	public void setParent(HierarchyNode<? extends Integer> parent)
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


	/**
	 * Returns an iterator over a set of elements of type T.
	 *
	 * @return an Iterator.
	 */
	public Iterator<PhylogenyNode<Integer>> iterator()
		{
		throw new NotImplementedException("Iterating over the entire NCBI taxonomy is probably a bad idea");
		}
	}
