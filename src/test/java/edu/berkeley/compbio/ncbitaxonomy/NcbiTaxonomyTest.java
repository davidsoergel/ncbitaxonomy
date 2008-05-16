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

import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA. User: soergel Date: Nov 6, 2006 Time: 4:29:37 PM To change this template use File |
 * Settings | File Templates.
 */
public class NcbiTaxonomyTest//extends AbstractJpaTests
	{
	// ------------------------------ FIELDS ------------------------------

	//	private NcbiTaxonomyNameDao ncbiTaxonomyNameDao;
	//private NcbiTaxonomyService ncbiTaxonomyService;

	/*
	 public PhyloUtilsTest()
		 {
		 super();
		 setAutowireMode(AUTOWIRE_BY_NAME);
		 }
 */
	// --------------------- GETTER / SETTER METHODS ---------------------

	/*	public void setNcbiTaxonomyNameDao(NcbiTaxonomyNameDao ncbiTaxonomyNameDao)
		 {
		 this.ncbiTaxonomyNameDao = ncbiTaxonomyNameDao;
		 }

	 public void setPhyloUtilsService(PhyloUtilsService phyloUtilsService)
		 {
		 this.phyloUtilsService = phyloUtilsService;
		 }
 */
	// -------------------------- OTHER METHODS --------------------------


	@Test
	public void findTaxonByNameWorks() throws NcbiTaxonomyException
		{
		//assert ncbiTaxonomyNameDao.findByName("Myxococcus xanthus").getTaxon().getTaxId() == 34;
		assert new NcbiTaxonomyService().findTaxidByName("Myxococcus xanthus") == 34;
		}

	/*
   @Override
   protected String[] getConfigLocations()
	   {
	   return new String[]{
			   "classpath:ncbitaxonomy-test.xml",
			   "classpath:ncbitaxonomy.xml"
	   };
	   }*/

	/*
	 @BeforeClass
	 public void launchSetup() throws Exception
		 {
		 setUp();
		 }

	 @AfterClass
	 public void launchTearDown() throws Exception
		 {
		 tearDown();
		 }
 */

	/*	@BeforeClass
	 public void setup()
		 {
		 ncbiTaxonomyService = NcbiTaxonomyService.getInstance();//new PhyloUtilsService();
		 }
 */
	}
