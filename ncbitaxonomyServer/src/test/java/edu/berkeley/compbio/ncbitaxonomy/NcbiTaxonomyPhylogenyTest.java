/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */


package edu.berkeley.compbio.ncbitaxonomy;

import com.davidsoergel.dsutils.EnvironmentUtils;
import com.davidsoergel.trees.NoSuchNodeException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Although the NcbiTaxonomyService technically extends AbstractRootedPhylogeny, in reality its behavior is extremely
 * limited, and any methods are not implemented.  As a result, we can't use the Contract Test methodology to run
 * AbstractRootedPhylogenyAbstractTest, since it'll just cause a ton of failures.  So, we'll just test a few things
 * explicitly.
 *
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */

public class NcbiTaxonomyPhylogenyTest
		//extends ContractTestAware<NcbiTaxonomyService>
		//implements TestInstanceFactory<NcbiTaxonomyService>
	{

	@Test
	public void findTaxonByNameWorks() throws NcbiTaxonomyException, NoSuchNodeException
		{
		//assert ncbiTaxonomyNameDao.findByName("Myxococcus xanthus").getTaxon().getTaxId() == 34;
		assert NcbiTaxonomyPhylogeny.getInstance().findTaxidByName("Myxococcus xanthus") == 34;
		}


	/*	public NcbiTaxonomyService createInstance() throws Exception
	   {
	   return NcbiTaxonomyService.getInstance();
	   }

   public void addContractTestsToQueue(Queue theContractTests)
	   {
	   theContractTests.add(new AbstractRootedPhylogenyAbstractTest<NcbiTaxonomyService>(this));
	   }

   @Factory
   public Object[] instantiateAllContractTests()
	   {
	   return super.instantiateAllContractTestsWithName(NcbiTaxonomyService.class.getCanonicalName());
	   }*/

	@BeforeClass
	public void setUp() throws Exception
		{
		EnvironmentUtils.setCacheRoot("/tmp/testCache");
		}
	}
