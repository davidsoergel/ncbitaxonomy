/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyDbContextFactory extends GenericApplicationContext
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyDbContextFactory.class);


	public static ApplicationContext makeNcbiTaxonomyDbContext() //String dbName)
			throws IOException

		{
		/*
		File propsFile = PropertiesUtils
				.findPropertiesFile("NCBI_TAXONOMY_PROPERTIES", ".ncbitaxonomy", "ncbi_taxonomy.properties");
		EnvironmentUtils.init(propsFile);

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
		//String dbName = (String) p.get("default");

		Map<String, Properties> databases = PropertiesUtils.splitPeriodDelimitedProperties(p);

*/
		GenericApplicationContext ctx = new GenericApplicationContext();
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
//			xmlReader.loadBeanDefinitions(new ClassPathResource("springjpautils.xml"));
		xmlReader.loadBeanDefinitions(new ClassPathResource("ncbitaxonomy.xml"));

		//PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		//cfg.setProperties(databases.get(dbName));
		//ctx.addBeanFactoryPostProcessor(cfg);

		ctx.refresh();

		// add a shutdown hook for the above context...
		ctx.registerShutdownHook();

		return ctx;
		}
	}
