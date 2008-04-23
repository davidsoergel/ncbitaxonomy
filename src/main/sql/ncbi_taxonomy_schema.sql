# Dump of table names
# ------------------------------------------------------------

DROP TABLE IF EXISTS `names`;

CREATE TABLE `names` (
  `id` bigint(20) NOT NULL auto_increment,
  `modifieddate` datetime default NULL,
  `tax_id` mediumint(11) unsigned NOT NULL default '0',
  `name_txt` varchar(255) NOT NULL default '',
  `unique_name` varchar(255) default NULL,
  `name_class` varchar(32) NOT NULL default '',
  PRIMARY KEY (`id`),
  KEY `tax_id` (`tax_id`),
  KEY `name_class` (`name_class`),
  KEY `name_txt` (`name_txt`)
) TYPE=MyISAM;



# Dump of table nodes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `nodes`;

CREATE TABLE `nodes` (
  `id` mediumint(11) unsigned NOT NULL default '0',
   `modifieddate` datetime default NULL,
  `parent_tax_id` mediumint(8) unsigned NOT NULL default '0',
  `rank` varchar(32) default NULL,
  `embl_code` varchar(16) default NULL,
  `division_id` smallint(6) NOT NULL default '0',
  `inherited_div_flag` tinyint(4) NOT NULL default '0',
  `genetic_code_id` smallint(6) NOT NULL default '0',
  `inherited_GC_flag` tinyint(4) NOT NULL default '0',
  `mitochondrial_genetic_code_id` smallint(4) NOT NULL default '0',
  `inherited_MGC_flag` tinyint(4) NOT NULL default '0',
  `GenBank_hidden_flag` smallint(4) NOT NULL default '0',
  `hidden_subtree_root_flag` tinyint(4) NOT NULL default '0',
  `comments` varchar(255) default NULL,
  PRIMARY KEY (`id`),
  KEY `parent_tax_id` (`parent_tax_id`)
) TYPE=MyISAM;


