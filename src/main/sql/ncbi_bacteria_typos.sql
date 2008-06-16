# Directory names from ftp://ftp.ncbi.nih.gov/genomes/Bacteria that are typographical variants of NCBI taxonomy names
# David Soergel <soergel@compbio.berkeley.edu>
# created November 17, 2006
# updated June 5, 2008

# Now we just rename the directories according to the FASTA headers.  These synonyms remain because the directories can't be renamed, either because of slashes or name collisions. 

insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus licheniformis DSM 13', 'synonym' from names where name_txt = 'Bacillus licheniformis ATCC 14580';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis A HAR-13', 'synonym' from names where name_txt = 'Chlamydia trachomatis A/HAR-13';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila abortus S26 3', 'synonym' from names where name_txt = 'Chlamydophila abortus S26/3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Corynebacterium glutamicum ATCC 13032 Kitasato', 'synonym' from names where name_txt = 'Corynebacterium glutamicum ATCC 13032';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ehrlichia ruminantium Welgevonden', 'synonym' from names where name_txt = 'Ehrlichia ruminantium str. Welgevonden';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Tropheryma whipplei TW08 27', 'synonym' from names where name_txt = 'Tropheryma whipplei TW08/27';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila felis Fe C-56', 'synonym' from names where name_txt = 'Chlamydophila felis Fe/C-56';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lawsonia intracellularis PHE MN1-00', 'synonym' from names where name_txt = 'Lawsonia intracellularis PHE/MN1-00';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ignicoccus hospitalis KIN4 I', 'synonym' from names where name_txt = 'Ignicoccus hospitalis KIN4/I';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Flavobacterium psychrophilum JIP02 86', 'synonym' from names where name_txt = 'Flavobacterium psychrophilum JIP02/86';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis 434 Bu', 'synonym' from names where name_txt = 'Chlamydia trachomatis 434/Bu';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis D UW-3 CX', 'synonym' from names where name_txt = 'Chlamydia trachomatis D/UW-3/CX';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycobacterium bovis AF2122 97', 'synonym' from names where name_txt = 'Mycobacterium bovis AF2122/97';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus agalactiae 2603V R', 'synonym' from names where name_txt = 'Streptococcus agalactiae 2603V/R';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis L2b UCH-1 proctitis', 'synonym' from names where name_txt = 'Chlamydia trachomatis L2b/UCH-1/proctitis';

# that leaves one directory name that wasn't renamed properly:
# Salmonella enterica subsp. arizonae serovar 62:z4    should be  
# Salmonella enterica subsp. arizonae serovar 62:z4,z23:--

# and three species names from FASTA headers that are not in the taxonomy:

insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli W3110 DNA', 'synonym' from names where name_txt = 'Escherichia coli W3110';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ralstonia eutropha JMP134 mega', 'synonym' from names where name_txt = 'Ralstonia eutropha JMP134';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus subsp. aureus USA300 TCH1516', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus USA300';
