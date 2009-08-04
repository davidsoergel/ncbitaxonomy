package edu.berkeley.compbio.ncbitaxonomy.service;

import com.davidsoergel.dsutils.PropertiesUtils;
import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyServicesContextFactory
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyServicesContextFactory.class);

	public static ApplicationContext makeNcbiTaxonomyServicesContext() throws IOException
		{

		File propsFile = PropertiesUtils
				.findPropertiesFile("NCBI_TAXONOMY_SERVICE_PROPERTIES", ".ncbitaxonomy", "service.properties");

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
		xmlReader.loadBeanDefinitions(new ClassPathResource("ncbitaxonomyservices.xml"));

		PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
		Properties properties = databases.get(dbName);

		if (properties == null)
			{
			logger.error("Service definition not found: " + dbName);
			logger.error("Valid names: " + StringUtils.join(databases.keySet().iterator(), ", "));
			throw new NcbiTaxonomyRuntimeException("Database definition not found: " + dbName);
			}

		cfg.setProperties(properties);
		ctx.addBeanFactoryPostProcessor(cfg);

		ctx.refresh();

		// add a shutdown hook for the above context...
		ctx.registerShutdownHook();

		return ctx;
		}
	}
