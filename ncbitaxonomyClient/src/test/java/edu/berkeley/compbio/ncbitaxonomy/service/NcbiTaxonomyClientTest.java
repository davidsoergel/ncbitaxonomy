package edu.berkeley.compbio.ncbitaxonomy.service;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyClientTest
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyClientTest.class);

	@BeforeTest
	public void setUp()
		{
		}

/*	@Test
	public void testRothia() throws NoSuchNodeException
		{
		String name = "Rothia mucilaginosa DY-18";
		int id = NcbiTaxonomyClient.getInstance().findTaxidByName(name);
		logger.info(name + " -> " + id);
		}*/
	}
