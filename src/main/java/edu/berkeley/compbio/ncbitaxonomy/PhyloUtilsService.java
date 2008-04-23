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

import com.davidsoergel.dsutils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: soergel Date: May 7, 2007 Time: 2:03:39 PM To change this template use File |
 * Settings | File Templates.
 */
public class PhyloUtilsService//extends Singleton<PhyloUtilsService>
	{
	private static final Logger logger = Logger.getLogger(PhyloUtilsService.class);
	// ------------------------------ FIELDS ------------------------------

	private static final PhyloUtilsService instance = new PhyloUtilsService();

	private PhyloUtilsServiceImpl phyloUtilsServiceImpl;


	// -------------------------- STATIC METHODS --------------------------

	public static PhyloUtilsService getInstance()
		{
		return instance;
		}

	// --------------------------- CONSTRUCTORS ---------------------------

	public PhyloUtilsService()
		{
		try
			{
			File propsFile = PropertiesUtils
					.findPropertiesFile("NCBI_TAXONOMY_PROPERTIES", ".phyloutils", "ncbi_taxonomy.properties");
			logger.debug("Using properties file: " + propsFile);
			Properties p = new Properties();
			p.load(new FileInputStream(propsFile));
			String dbName = (String) p.get("default");

			Map<String, Properties> databases = PropertiesUtils.splitPeriodDelimitedProperties(p);


			GenericApplicationContext ctx = new GenericApplicationContext();
			XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
			xmlReader.loadBeanDefinitions(new ClassPathResource("phyloutils.xml"));

			PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
			cfg.setProperties(databases.get(dbName));
			ctx.addBeanFactoryPostProcessor(cfg);

			ctx.refresh();

			// add a shutdown hook for the above context...
			ctx.registerShutdownHook();

			phyloUtilsServiceImpl = ((PhyloUtilsServiceImpl) ctx.getBean("phyloUtilsServiceImpl"));

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

	// -------------------------- OTHER METHODS --------------------------

	public Integer commonAncestorID(Set<Integer> mergeIds) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.commonAncestorID(mergeIds);
		}

	public Integer commonAncestorID(Integer taxIdA, Integer taxIdB) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.commonAncestorID(taxIdA, taxIdB);
		}

	//private Map<String, Double> exactDistanceBetweenStringsCache = new HashMap<String, Double>();

	public double exactDistanceBetween(String speciesNameA, String speciesNameB) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.exactDistanceBetween(speciesNameA, speciesNameB);
		/*	Double result = exactDistanceBetweenStringsCache.get(speciesNameA+"|"+speciesNameB);
				if(result == null)
					{
					result = phyloUtilsServiceImpl.exactDistanceBetween(speciesNameA, speciesNameB);
					exactDistanceBetweenStringsCache.put(speciesNameA+speciesNameB, result);
					}
				return result;*/
		}

	//	private Map<String, Double> exactDistanceBetweenIntsCache = new HashMap<String, Double>();
	public double exactDistanceBetween(int taxIdA, int taxIdB) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.exactDistanceBetween(taxIdA, taxIdB);
		}

	public double minDistanceBetween(String speciesNameA, String speciesNameB) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.minDistanceBetween(speciesNameA, speciesNameB);
		}

	public double minDistanceBetween(int taxIdA, int taxIdB) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.minDistanceBetween(taxIdA, taxIdB);
		}

	public int nearestKnownAncestor(int taxId) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.nearestKnownAncestor(taxId);
		}

	public int nearestKnownAncestor(String speciesName) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.nearestKnownAncestor(speciesName);
		}

	public void saveState()
		{
		phyloUtilsServiceImpl.saveState();
		}

	public int findTaxidByName(String name) throws NcbiTaxonomyException
		{
		return phyloUtilsServiceImpl.findTaxidByName(name);
		}

	public RootedPhylogeny<Integer> extractTreeWithLeaves(Set<Integer> ids)
		{
		return phyloUtilsServiceImpl.extractTreeWithLeaves(ids);
		}
	}
