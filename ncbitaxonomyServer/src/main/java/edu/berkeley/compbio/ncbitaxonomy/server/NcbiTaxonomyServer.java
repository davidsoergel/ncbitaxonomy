/*
 * Copyright (c) 2008, Your Corporation. All Rights Reserved.
 */

package edu.berkeley.compbio.ncbitaxonomy.server;

import edu.berkeley.compbio.ncbitaxonomy.NcbiTaxonomyDbContextFactory;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;


/**
 * @author <a href="mailto:dev@davidsoergel.com">David Soergel</a>
 * @version $Id: JandyService.java 671 2009-07-27 09:20:46Z soergel $
 */

public class NcbiTaxonomyServer
	{
	private static final Logger logger = Logger.getLogger(NcbiTaxonomyServer.class);

	public static void main(String[] argv)
		{
		try
			{
			if (argv.length != 3)
				{
				logger.error("Usage: java NcbitaxonomyServer dbname service-username service-password");
				System.exit(1);
				}

			String dbName = argv[0].trim();
			String username = argv[1].trim();
			String password = argv[2].trim();

			System.out.println("\nNcbitaxonomy Server\n" + "http://dev.davidsoergel.com/ncbitaxonomy\n"
			                   + "(c) 2006-2008 David Soergel and Regents of the University of California");
			System.out.println("\nConnecting to database " + dbName + "...");


			ApplicationContext ctx = NcbiTaxonomyDbContextFactory.makeNcbiTaxonomyDbContext(dbName);

			Server server = new Server(9493);

			//	Connector connector = new SelectChannelConnector();
			//	connector.setPort(8080);
			//	server.setConnectors(new Connector[]{connector});

			Constraint constraint = new Constraint();
			constraint.setName(Constraint.__BASIC_AUTH);

			constraint.setRoles(new String[]{"user", "admin"});
			constraint.setAuthenticate(true);

			ConstraintMapping cm = new ConstraintMapping();
			cm.setConstraint(constraint);
			cm.setPathSpec("/*");

			SecurityHandler sh = new SecurityHandler();
			HashUserRealm userRealm = new HashUserRealm(
					"Jandy Service"); //, System.getProperty("jetty.home") + "/etc/realm.properties"));
			//** trivial auth options for now
			userRealm.put(username, password);
			userRealm.addUserToRole(username, "user");
			sh.setUserRealm(userRealm);
			sh.setConstraintMappings(new ConstraintMapping[]{cm});


			Context context = new Context(server, "/", Context.SESSIONS);

			ServletHolder servletHolder = new ServletHolder((NcbiTaxonomyServlet) ctx.getBean("ncbiTaxonomyServlet"));
			context.addServlet(servletHolder, "/ncbitaxonomy");

			ServletHolder servletHolder2 = new ServletHolder((NcbiTaxonomyWithUnitBranchLengthsServlet) ctx.getBean("ncbiTaxonomyWithUnitBranchLengthsServlet"));
			context.addServlet(servletHolder2, "/ncbitaxonomyWithUnitBranchLengths");

			ServletHolder servletHolder3 = new ServletHolder((NcbiCiccarelliHybridServlet) ctx.getBean("ncbiCiccarelliHybridServlet"));
			context.addServlet(servletHolder3, "/ncbiCiccarelliHybrid");

			context.addHandler(sh);

			/*
			WebAppContext webappcontext = new WebAppContext();
			webappcontext.setContextPath("/");
			webappcontext.addServlet(servletHolder, "/jandyCommands");
			webappcontext.addHandler(sh);
			*/

			//	HandlerCollection handlers = new HandlerCollection();
			//	handlers.setHandlers(new Handler[]{context, new DefaultHandler()});

			//	server.setHandler(handlers);
			server.start();
			server.join();
/*
			Server server = new Server();
			Connector connector = new SocketConnector();
			connector.setPort(8888);

			server.setConnectors(new Connector[]{connector});
			ServletHandler handler = new ServletHandler();
			server.setHandler(handler);
			handler.addServletWithMapping("com.caucho.hessian.server.HessianServlet", "/jandy");
			server.start();
			server.join();*/
			}
		catch (Throwable e)
			{
			logger.fatal("fatal", e);
			}
		}
	}
