# Directory names from ftp://ftp.ncbi.nih.gov/genomes/Bacteria that are typographical variants of NCBI taxonomy names
# David Soergel <soergel@compbio.berkeley.edu>
# created November 17, 2006
# updated June 5, 2008
# updated March 14, 2009
# See also (in phyloutils) ciccarelliToNcbiMap.sql

# BEFORE RUNNING THIS SCRIPT
# fix a bug in the download (non-.fna files):

# find . ! -name '*.fna' ! -type d -exec rm {} \;

# Now we just rename the directories according to the FASTA headers.  These synonyms remain because the directories can't be renamed, either because of slashes or name collisions.
# This section is just the output of fixGenomeDirectoryNames.pl:

insert into names (tax_id, name_txt, name_class) select tax_id, 'Burkholderia multivorans ATCC 17616 Tohoku', 'synonym' from names where name_txt = 'Burkholderia multivorans ATCC 17616';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis D_UW-3_CX', 'synonym' from names where name_txt = 'Chlamydia trachomatis D/UW-3/CX';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis 434_Bu', 'synonym' from names where name_txt = 'Chlamydia trachomatis 434/Bu';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis A_HAR-13', 'synonym' from names where name_txt = 'Chlamydia trachomatis A/HAR-13';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis L2b_UCH-1_proctitis', 'synonym' from names where name_txt = 'Chlamydia trachomatis L2b/UCH-1/proctitis';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila abortus S26_3', 'synonym' from names where name_txt = 'Chlamydophila abortus S26/3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila felis Fe_C-56', 'synonym' from names where name_txt = 'Chlamydophila felis Fe/C-56';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Corynebacterium glutamicum ATCC 13032 Kitasato', 'synonym' from names where name_txt = 'Corynebacterium glutamicum ATCC 13032';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ehrlichia ruminantium Welgevonden UPSA', 'synonym' from names where name_txt = 'Ehrlichia ruminantium str. Welgevonden';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Erwinia tasmaniensis Et1_99', 'synonym' from names where name_txt = 'Erwinia tasmaniensis Et1/99';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli O127:H6 str. E2348_69', 'synonym' from names where name_txt = 'Escherichia coli O127:H6 str. E2348/69';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Flavobacterium psychrophilum JIP02_86', 'synonym' from names where name_txt = 'Flavobacterium psychrophilum JIP02/86';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Gluconacetobacter_diazotrophicus_PAl_5', 'synonym' from names where name_txt = 'Gluconacetobacter diazotrophicus PAl 5';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ignicoccus hospitalis KIN4_I', 'synonym' from names where name_txt = 'Ignicoccus hospitalis KIN4/I';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lawsonia intracellularis PHE_MN1-00', 'synonym' from names where name_txt = 'Lawsonia intracellularis PHE/MN1-00';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycobacterium bovis AF2122_97', 'synonym' from names where name_txt = 'Mycobacterium bovis AF2122/97';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Natranaerobius thermophilus JW_NM-WN-LF', 'synonym' from names where name_txt = 'Natranaerobius thermophilus JW/NM-WN-LF';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella enterica subsp. enterica serovar Gallinarum str. 287_91', 'synonym' from names where name_txt = 'Salmonella enterica subsp. enterica serovar Gallinarum str. 287/91';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus agalactiae 2603V_R', 'synonym' from names where name_txt = 'Streptococcus agalactiae 2603V/R';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Tropheryma whipplei TW08_27', 'synonym' from names where name_txt = 'Tropheryma whipplei TW08/27';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Yersinia pseudotuberculosis PB1_+', 'synonym' from names where name_txt = 'Yersinia pseudotuberculosis PB1/+';

# this one is a special case: there are genomes with two names that map to the same taxid, but both names are already in the database:, so DON'T insert it:
# insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus licheniformis DSM 13', 'synonym' from names where name_txt = 'Bacillus licheniformis ATCC 14580';

# and two species names from FASTA headers that are not in the taxonomy:

insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli W3110 DNA', 'synonym' from names where name_txt = 'Escherichia coli W3110';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ralstonia eutropha JMP134 mega', 'synonym' from names where name_txt = 'Ralstonia eutropha JMP134';

# finally one from the Mavro2007 set

insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus suis 89_1591', 'synonym' from names where name_txt = 'Streptococcus suis 89/1591';

# Some special-case bugs in that procedure:
mv 'Aeromonas salmonicida salmonicida A449' 'Aeromonas salmonicida subsp. salmonicida A449'
mv 'Methanocaldococcus jannaschii DSM 2661 extrachromosomal' 'Methanocaldococcus jannaschii DSM 2661'
mv 'Pseudomonas syringae pv. phaseolicola 1448A large' 'Pseudomonas syringae pv. phaseolicola 1448A'
mv 'Gluconacetobacter_diazotrophicus_PAl_5' 'Gluconacetobacter diazotrophicus PAL5'
mv 'Acinetobacter baumannii' 'Acinetobacter baumannii SDF'
mv 'Salmonella enterica subsp. arizonae serovar 62:z4' 'Salmonella enterica subsp. arizonae serovar 62:z4,z23:--'
