ncbitaxonomy
============

_A Java library for easy access to an NCBI taxonomy database_

 * 	Provides a Hibernate-based object-relational interface to the NCBI taxonomy database, and convenience classes for navigating it.
 * 	In particular, makes the NCBI taxonomy available as objects conforming to [phyloutils](http://github.com/davidsoergel/phyloutils/) APIs.

Documentation
-------------

 * [API docs](http://davidsoergel.github.io/ncbitaxonomy/)

Download
--------

[Maven](http://maven.apache.org/) is by far the easiest way to make use of ncbitaxonomy.  Just add these to your pom.xml:
```xml
<repositories>
	<repository>
		<id>dev.davidsoergel.com releases</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/releases</url>
		<snapshots>
			<enabled>false</enabled>
		</snapshots>
	</repository>
	<repository>
		<id>dev.davidsoergel.com snapshots</id>
		<url>http://dev.davidsoergel.com/nexus/content/repositories/snapshots</url>
		<releases>
			<enabled>false</enabled>
		</releases>
	</repository>
</repositories>

<dependencies>
	<dependency>
		<groupId>edu.berkeley.compbio</groupId>
		<artifactId>ncbitaxonomy</artifactId>
		<version>0.9</version>
	</dependency>
</dependencies>
```

If you really want just the jar, you can get the [latest release](http://dev.davidsoergel.com/nexus/content/repositories/releases/edu.berkeley.compbio/ncbitaxonomy/) from the Maven repo; or get the [latest stable build](http://dev.davidsoergel.com/jenkins/job/ncbitaxonomy/lastStableBuild/edu.berkeley.compbio$ncbitaxonomy/) from the build server.

