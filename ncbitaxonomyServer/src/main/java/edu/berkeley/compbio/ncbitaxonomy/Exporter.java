/*
 * Copyright (c) 2008-2013  David Soergel  <dev@davidsoergel.com>
 * Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package edu.berkeley.compbio.ncbitaxonomy;

import org.springframework.context.ApplicationContext;

import java.io.FileWriter;
import java.util.Set;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class Exporter
	{
	static String[] ranks = {"phylum", "class", "order", "family", "genus", "species"};

	public static void main(String[] argv)
		{
		try
			{
			ApplicationContext ctx = NcbiTaxonomyDbContextFactory.makeNcbiTaxonomyDbContext();
			NcbiTaxonomyPhylogeny ncbi = (NcbiTaxonomyPhylogeny) ctx.getBean("ncbiTaxonomyPhylogeny");

			FileWriter treeWriter = new FileWriter("tree.newick");
			ncbi.toNewick(treeWriter, "", "", 0, 0);
			treeWriter.close();

			FileWriter synonymWriter = new FileWriter("synonyms");
			ncbi.writeSynonyms(synonymWriter);
			synonymWriter.close();

			for (String rank : ranks)
				{
				FileWriter rankWriter = new FileWriter(rank);
				Set<Integer> ids = ncbi.getTaxIdsWithRank(rank);
				for (Integer id : ids)
					{
					rankWriter.append(id.toString()).append("\n");
					}
				rankWriter.close();
				}
			}
		catch (Throwable e)
			{
			e.printStackTrace();
			}
		}
	}
