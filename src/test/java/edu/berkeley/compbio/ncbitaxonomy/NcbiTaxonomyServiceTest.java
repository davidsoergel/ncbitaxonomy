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

import com.davidsoergel.dsutils.EnvironmentUtils;
import com.davidsoergel.dsutils.tree.NoSuchNodeException;
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

public class NcbiTaxonomyServiceTest
		//extends ContractTestAware<NcbiTaxonomyService>
		//implements TestInstanceFactory<NcbiTaxonomyService>
	{

	@Test
	public void findTaxonByNameWorks() throws NcbiTaxonomyException, NoSuchNodeException
		{
		//assert ncbiTaxonomyNameDao.findByName("Myxococcus xanthus").getTaxon().getTaxId() == 34;
		assert NcbiTaxonomyService.getInstance().findTaxidByName("Myxococcus xanthus") == 34;
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
