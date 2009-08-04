package edu.berkeley.compbio.ncbitaxonomy.service;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class NcbiTaxonomyClient
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyClient.class);

	static NcbiTaxonomyService ncbiTaxonomy;


	public static NcbiTaxonomyService getNcbiTaxonomyService()
		{
		if (ncbiTaxonomy == null)
			{
			ApplicationContext ctx = null;
			try
				{
				ctx = NcbiTaxonomyServicesContextFactory.makeNcbiTaxonomyServicesContext();
				}
			catch (IOException e)
				{
				logger.error(e);
				throw new Error(e);
				}

			ncbiTaxonomy = (NcbiTaxonomyService) ctx.getBean("ncbiTaxonomyService");
			}
		return ncbiTaxonomy;
		}

	static NcbiTaxonomyWithUnitBranchLengthsService ncbiTaxonomyWithUnitBranchLengths;

	public static NcbiTaxonomyWithUnitBranchLengthsService getNcbiTaxonomyWithUnitBranchLengthsService()
		{
		if (ncbiTaxonomyWithUnitBranchLengths == null)
			{
			ApplicationContext ctx = null;
			try
				{
				ctx = NcbiTaxonomyServicesContextFactory.makeNcbiTaxonomyServicesContext();
				}
			catch (IOException e)
				{
				logger.error(e);
				throw new Error(e);
				}

			ncbiTaxonomyWithUnitBranchLengths =
					(NcbiTaxonomyWithUnitBranchLengthsService) ctx.getBean("ncbiTaxonomyWithUnitBranchLengthsService");
			}
		return ncbiTaxonomyWithUnitBranchLengths;
		}

	static NcbiCiccarelliHybridService ncbiCiccarelliHybrid;


	public static NcbiCiccarelliHybridService getNcbiCiccarelliHybridService()
		{
		if (ncbiCiccarelliHybrid == null)
			{
			ApplicationContext ctx = null;
			try
				{
				ctx = NcbiTaxonomyServicesContextFactory.makeNcbiTaxonomyServicesContext();
				}
			catch (IOException e)
				{
				logger.error(e);
				throw new Error(e);
				}

			ncbiCiccarelliHybrid = (NcbiCiccarelliHybridService) ctx.getBean("ncbiCiccarelliHybridService");
			}
		return ncbiCiccarelliHybrid;
		}
	}
