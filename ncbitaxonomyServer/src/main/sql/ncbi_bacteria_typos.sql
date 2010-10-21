# Directory names from ftp://ftp.ncbi.nih.gov/genomes/Bacteria that are typographical variants of NCBI taxonomy names
# David Soergel <soergel@compbio.berkeley.edu>
# created November 17, 2006
# updated June 5, 2008
# updated March 14, 2009
# updated October 21, 2010
# See also (in phyloutils) ciccarelliToNcbiMap.sql -> deprecated

# Now we just rename the directories according to the FASTA headers using fixGenomeDirectoryNames.pl.
# These synonyms remain because the directories can't be renamed, either because of slashes or name collisions.

# the names that need help are these:

-- mysql> select name_txt, tax_id from tmp LEFT OUTER JOIN names USING (name_txt) where tax_id is null;
-- +--------------------------------------------------------------------+--------+
-- | name_txt                                                           | tax_id |
-- +--------------------------------------------------------------------+--------+
-- | Brachyspira pilosicoli 95_1000                                     |   NULL |
-- | Burkholderia multivorans ATCC 17616 Tohoku                         |   NULL |
-- | Chlamydia trachomatis 434_Bu                                       |   NULL |
-- | Chlamydia trachomatis A_HAR-13                                     |   NULL |
-- | Chlamydia trachomatis B_Jali20_OT                                  |   NULL |
-- | Chlamydia trachomatis B_TZ1A828_OT                                 |   NULL |
-- | Chlamydia trachomatis D_UW-3_CX                                    |   NULL |
-- | Chlamydia trachomatis L2b_UCH-1_proctitis                          |   NULL |
-- | Chlamydophila abortus S26_3                                        |   NULL |
-- | Chlamydophila felis Fe_C-56                                        |   NULL |
-- | Corynebacterium glutamicum ATCC 13032 Kitasato                     |   NULL |
-- | Cupriavidus taiwanensis str. LMG19424                              |   NULL |
-- | Ehrlichia ruminantium Welgevonden UPSA                             |   NULL |
-- | Erwinia pyrifoliae Ep1_96                                          |   NULL |
-- | Erwinia tasmaniensis Et1_99                                        |   NULL |
-- | Escherichia coli O127:H6 str. E2348_69                             |   NULL |
-- | Flavobacterium psychrophilum JIP02_86                              |   NULL |
-- | Gluconacetobacter diazotrophicus PAl 5 JGI                         |   NULL |
-- | Ignicoccus hospitalis KIN4_I                                       |   NULL |
-- | Lawsonia intracellularis PHE_MN1-00                                |   NULL |
-- | Legionella pneumophila 2300_99 Alcoy                               |   NULL |
-- | Listeria seeligeri serovar 1_2b str. SLCC3954                      |   NULL |
-- | Mycobacterium bovis AF2122_97                                      |   NULL |
-- | Mycoplasma conjunctivae HRC_581                                    |   NULL |
-- | NC_001318 Borrelia burgdorferi B31                                 |   NULL |
-- | Natranaerobius thermophilus JW_NM-WN-LF                            |   NULL |
-- | Salmonella enterica subsp. arizonae serovar 62:z4                  |   NULL |
-- | Salmonella enterica subsp. enterica serovar Gallinarum str. 287_91 |   NULL |
-- | Streptococcus agalactiae 2603V_R                                   |   NULL |
-- | Streptococcus pneumoniae TCH8431_19A                               |   NULL |
-- | Streptococcus suis P1_7                                            |   NULL |
-- | Tropheryma whipplei TW08_27                                        |   NULL |
-- | Yersinia pseudotuberculosis PB1_+                                  |   NULL |
-- +--------------------------------------------------------------------+--------+
-- 33 rows in set (0.04 sec)

# Most of those can be addressed using the synonyms.sql produced by fixGenomeDirectoryNames.pl.

# this one is a special case: there are genomes with two names that map to the same taxid, but both names are already in the database:, so DON'T insert it:
# insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus licheniformis DSM 13', 'synonym' from names where name_txt = 'Bacillus licheniformis ATCC 14580';

# The rest are OK:

insert into names (tax_id, name_txt, name_class) select tax_id, 'Brachyspira pilosicoli 95_1000', 'synonym' from names where name_txt = 'Brachyspira pilosicoli 95/1000';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Burkholderia multivorans ATCC 17616 Tohoku', 'synonym' from names where name_txt = 'Burkholderia multivorans ATCC 17616';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis 434_Bu', 'synonym' from names where name_txt = 'Chlamydia trachomatis 434/Bu';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis A_HAR-13', 'synonym' from names where name_txt = 'Chlamydia trachomatis A/HAR-13';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis B_Jali20_OT', 'synonym' from names where name_txt = 'Chlamydia trachomatis B/Jali20/OT';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis B_TZ1A828_OT', 'synonym' from names where name_txt = 'Chlamydia trachomatis B/TZ1A828/OT';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis D_UW-3_CX', 'synonym' from names where name_txt = 'Chlamydia trachomatis D/UW-3/CX';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis L2b_UCH-1_proctitis', 'synonym' from names where name_txt = 'Chlamydia trachomatis L2b/UCH-1/proctitis';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila abortus S26_3', 'synonym' from names where name_txt = 'Chlamydophila abortus S26/3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila felis Fe_C-56', 'synonym' from names where name_txt = 'Chlamydophila felis Fe/C-56';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Corynebacterium glutamicum ATCC 13032 Kitasato', 'synonym' from names where name_txt = 'Corynebacterium glutamicum ATCC 13032';

# Cupriavidus taiwanensis str. LMG19424

insert into names (tax_id, name_txt, name_class) select tax_id, 'Ehrlichia ruminantium Welgevonden UPSA', 'synonym' from names where name_txt = 'Ehrlichia ruminantium str. Welgevonden';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Erwinia pyrifoliae Ep1_96', 'synonym' from names where name_txt = 'Erwinia pyrifoliae Ep1/96';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Erwinia tasmaniensis Et1_99', 'synonym' from names where name_txt = 'Erwinia tasmaniensis Et1/99';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli O127:H6 str. E2348_69', 'synonym' from names where name_txt = 'Escherichia coli O127:H6 str. E2348/69';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Flavobacterium psychrophilum JIP02_86', 'synonym' from names where name_txt = 'Flavobacterium psychrophilum JIP02/86';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Gluconacetobacter diazotrophicus PAl 5 JGI', 'synonym' from names where name_txt = 'Gluconacetobacter diazotrophicus PAl 5';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ignicoccus hospitalis KIN4_I', 'synonym' from names where name_txt = 'Ignicoccus hospitalis KIN4/I';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lawsonia intracellularis PHE_MN1-00', 'synonym' from names where name_txt = 'Lawsonia intracellularis PHE/MN1-00';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Legionella pneumophila 2300_99 Alcoy', 'synonym' from names where name_txt = 'Legionella pneumophila 2300/99 Alcoy';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Listeria seeligeri serovar 1_2b str. SLCC3954', 'synonym' from names where name_txt = 'Listeria seeligeri serovar 1/2b str. SLCC3954';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycobacterium bovis AF2122_97', 'synonym' from names where name_txt = 'Mycobacterium bovis AF2122/97';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycoplasma conjunctivae HRC_581', 'synonym' from names where name_txt = 'Mycoplasma conjunctivae HRC/581';

# NC_001318 Borrelia burgdorferi B31

insert into names (tax_id, name_txt, name_class) select tax_id, 'Natranaerobius thermophilus JW_NM-WN-LF', 'synonym' from names where name_txt = 'Natranaerobius thermophilus JW/NM-WN-LF';

# Salmonella enterica subsp. arizonae serovar 62:z4

insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella enterica subsp. enterica serovar Gallinarum str. 287_91', 'synonym' from names where name_txt = 'Salmonella enterica subsp. enterica serovar Gallinarum str. 287/91';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus agalactiae 2603V_R', 'synonym' from names where name_txt = 'Streptococcus agalactiae 2603V/R';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus pneumoniae TCH8431_19A', 'synonym' from names where name_txt = 'Streptococcus pneumoniae TCH8431/19A';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus suis P1_7', 'synonym' from names where name_txt = 'Streptococcus suis P1/7';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Tropheryma whipplei TW08_27', 'synonym' from names where name_txt = 'Tropheryma whipplei TW08/27';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Yersinia pseudotuberculosis PB1_+', 'synonym' from names where name_txt = 'Yersinia pseudotuberculosis PB1/+';


# and the three species names from FASTA headers that are not in the taxonomy:

insert into names (tax_id, name_txt, name_class) select tax_id, 'Cupriavidus taiwanensis str. LMG19424', 'synonym' from names where name_txt = 'Cupriavidus taiwanensis';
insert into names (tax_id, name_txt, name_class) select tax_id, 'NC_001318 Borrelia burgdorferi B31', 'synonym' from names where name_txt = 'Borrelia burgdorferi B31';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella enterica subsp. arizonae serovar 62:z4', 'synonym' from names where name_txt = 'Salmonella enterica subsp. arizonae serovar 62:z4,z23:-';


-- # finally one from the Mavro2007 set
--
-- insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus suis 89_1591', 'synonym' from names where name_txt = 'Streptococcus suis 89/1591';
--
-- # Some special-case bugs in that procedure:
-- mv 'Aeromonas salmonicida salmonicida A449' 'Aeromonas salmonicida subsp. salmonicida A449'
-- mv 'Methanocaldococcus jannaschii DSM 2661 extrachromosomal' 'Methanocaldococcus jannaschii DSM 2661'
-- mv 'Pseudomonas syringae pv. phaseolicola 1448A large' 'Pseudomonas syringae pv. phaseolicola 1448A'
-- mv 'Gluconacetobacter_diazotrophicus_PAl_5' 'Gluconacetobacter diazotrophicus PAL5'
-- mv 'Acinetobacter baumannii' 'Acinetobacter baumannii SDF'
-- mv 'Salmonella enterica subsp. arizonae serovar 62:z4' 'Salmonella enterica subsp. arizonae serovar 62:z4,z23:--'
--
