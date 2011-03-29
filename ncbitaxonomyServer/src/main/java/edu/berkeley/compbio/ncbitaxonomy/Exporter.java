package edu.berkeley.compbio.ncbitaxonomy;

import org.springframework.context.ApplicationContext;

import java.io.FileWriter;

/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id$
 */
public class Exporter
	{
	public static void main(String[] argv)
		{
		try
			{
			ApplicationContext ctx = NcbiTaxonomyDbContextFactory.makeNcbiTaxonomyDbContext();
			NcbiTaxonomyPhylogeny ncbi = (NcbiTaxonomyPhylogeny) ctx.getBean("ncbiTaxonomyPhylogeny");

			FileWriter treeWriter = new FileWriter("ncbi.newick");
			ncbi.toNewick(treeWriter, "", "", 0, 0);
			treeWriter.close();

			FileWriter synonymWriter = new FileWriter("ncbi.newick");
			ncbi.writeSynonyms(synonymWriter);
			synonymWriter.close();
			}
		catch (Throwable e)
			{
			e.printStackTrace();
			}
		}
	}
