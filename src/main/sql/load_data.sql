LOAD DATA INFILE '/home/soergel/taxdump/names.dmp'
INTO TABLE names
FIELDS TERMINATED BY '\t|\t'
LINES TERMINATED BY '\t|\n'
(tax_id, name_txt, unique_name, name_class);

LOAD DATA INFILE '/home/soergel/taxdump/nodes.dmp'
INTO TABLE nodes
FIELDS TERMINATED BY '\t|\t'
LINES TERMINATED BY '\t|\n'
(id, parent_tax_id,rank,embl_code,division_id,inherited_div_flag,genetic_code_id,inherited_GC_flag,
mitochondrial_genetic_code_id,inherited_MGC_flag,GenBank_hidden_flag,hidden_subtree_root_flag,comments);


# work around typographical variation
insert into names (tax_id, name_txt, name_class) select tax_id, 'Acinetobacter sp ADP1', 'synonym' from names where name_txt = 'Acinetobacter sp. ADP1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Aeromonas hydrophila ATCC 7966', 'synonym' from names where name_txt = 'Aeromonas hydrophila subsp. hydrophila ATCC 7966';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Agrobacterium tumefaciens C58 Cereon', 'synonym' from names where name_txt = 'Agrobacterium tumefaciens str. C58';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Agrobacterium tumefaciens C58 UWash', 'synonym' from names where name_txt = 'Agrobacterium tumefaciens str. C58';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Anaplasma marginale St Maries', 'synonym' from names where name_txt = 'Anaplasma marginale str. St. Maries';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Arthrobacter FB24', 'synonym' from names where name_txt = 'Arthrobacter sp. FB24';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Aster yellows witches-broom phytoplasma AYWB', 'synonym' from names where name_txt = 'Aster yellows witches\'-broom phytoplasma AYWB';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Azoarcus sp EbN1', 'synonym' from names where name_txt = 'Azoarcus sp. EbN1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus anthracis Ames 0581', 'synonym' from names where name_txt = 'Bacillus anthracis str. Ames 0581';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus anthracis str Sterne', 'synonym' from names where name_txt = 'Bacillus anthracis str. Sterne';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus cereus ATCC14579', 'synonym' from names where name_txt = 'Bacillus cereus ATCC 14579';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacillus thuringiensis konkukian', 'synonym' from names where name_txt = 'Bacillus thuringiensis serovar konkukian';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bartonella henselae Houston-1', 'synonym' from names where name_txt = 'Bartonella henselae strain Houston-1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bartonella quintana Toulouse', 'synonym' from names where name_txt = 'Bartonella quintana str. Toulouse';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Baumannia cicadellinicola Homalodisca coagulata', 'synonym' from names where name_txt = 'Baumannia cicadellinicola str. Hc (Homalodisca coagulata)';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Brucella abortus 9-941', 'synonym' from names where name_txt = 'Brucella abortus biovar 1 str. 9-941';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Buchnera aphidicola Cc Cinara cedri', 'synonym' from names where name_txt = 'Buchnera aphidicola (Cinara cedri)';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Buchnera aphidicola Sg', 'synonym' from names where name_txt = 'Buchnera aphidicola str. Sg (Schizaphis graminum)';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Buchnera sp', 'synonym' from names where name_txt = 'Buchnera sp.';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Burkholderia 383', 'synonym' from names where name_txt = 'Burkholderia sp. 383';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydia trachomatis A HAR-13', 'synonym' from names where name_txt = 'Chlamydia trachomatis A/HAR-13';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila abortus S26 3', 'synonym' from names where name_txt = 'Chlamydophila abortus S26/3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila felis Fe C-56', 'synonym' from names where name_txt = 'Chlamydophila felis Fe/C-56';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Chlamydophila pneumoniae TW 183', 'synonym' from names where name_txt = 'Chlamydophila pneumoniae TW-183';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Corynebacterium glutamicum ATCC 13032 Bielefeld', 'synonym' from names where name_txt = 'Corynebacterium glutamicum ATCC 13032';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Corynebacterium glutamicum ATCC 13032 Kitasato', 'synonym' from names where name_txt = 'Corynebacterium glutamicum ATCC 13032';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Dehalococcoides CBDB1', 'synonym' from names where name_txt = 'Dehalococcoides sp. CBDB1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ehrlichia ruminantium Gardel', 'synonym' from names where name_txt = 'Ehrlichia ruminantium str. Gardel';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Ehrlichia ruminantium Welgevonden', 'synonym' from names where name_txt = 'Ehrlichia ruminantium str. Welgevonden';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Erwinia carotovora atroseptica SCRI1043', 'synonym' from names where name_txt = 'Erwinia carotovora subsp. atroseptica SCRI1043';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli O157H7 EDL933', 'synonym' from names where name_txt = 'Escherichia coli O157:H7 EDL933';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Escherichia coli O157H7', 'synonym' from names where name_txt = 'Escherichia coli O157:H7';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Francisella tularensis FSC 198', 'synonym' from names where name_txt = 'Francisella tularensis subsp. tularensis FSC 198';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Francisella tularensis holarctica OSU18', 'synonym' from names where name_txt = 'Francisella tularensis subsp. holarctica OSU18';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Francisella tularensis holarctica', 'synonym' from names where name_txt = 'Francisella tularensis subsp. holarctica';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Francisella tularensis tularensis', 'synonym' from names where name_txt = 'Francisella tularensis subsp. tularensis';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Frankia CcI3', 'synonym' from names where name_txt = 'Frankia sp. CcI3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Haemophilus influenzae 86 028NP', 'synonym' from names where name_txt = 'Haemophilus influenzae 86-028NP';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Jannaschia CCS1', 'synonym' from names where name_txt = 'Jannaschia sp. CCS1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lactobacillus delbrueckii bulgaricus ATCC BAA-365', 'synonym' from names where name_txt = 'Lactobacillus delbrueckii subsp. bulgaricus ATCC BAA-365';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lactobacillus sakei 23K', 'synonym' from names where name_txt = 'Lactobacillus sakei subsp. sakei 23K';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lactobacillus salivarius UCC118', 'synonym' from names where name_txt = 'Lactobacillus salivarius subsp. salivarius UCC118';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lactococcus lactis cremoris SK11', 'synonym' from names where name_txt = 'Lactococcus lactis subsp. cremoris SK11';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Lawsonia intracellularis PHE MN1-00', 'synonym' from names where name_txt = 'Lawsonia intracellularis PHE/MN1-00';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Legionella pneumophila Philadelphia 1', 'synonym' from names where name_txt = 'Legionella pneumophila subsp. pneumophila str. Philadelphia 1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Leifsonia xyli xyli CTCB0', 'synonym' from names where name_txt = 'Leifsonia xyli subsp. xyli str. CTCB07';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Leuconostoc mesenteroides ATCC 8293', 'synonym' from names where name_txt = 'Leuconostoc mesenteroides subsp. mesenteroides ATCC 8293';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Magnetococcus MC-1', 'synonym' from names where name_txt = 'Magnetococcus sp. MC-1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mesorhizobium BNC1', 'synonym' from names where name_txt = 'Mesorhizobium sp. BNC1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycobacterium MCS', 'synonym' from names where name_txt = 'Mycobacterium sp. MCS';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Mycoplasma capricolum ATCC 27343', 'synonym' from names where name_txt = 'Mycoplasma capricolum subsp. capricolum ATCC 27343';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Nocardia farcinica IFM10152', 'synonym' from names where name_txt = 'Nocardia farcinica IFM 10152';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Nostoc sp', 'synonym' from names where name_txt = 'Nostoc sp.';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Parachlamydia sp UWE25', 'synonym' from names where name_txt = 'Candidatus Protochlamydia amoebophila UWE25';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Pirellula sp', 'synonym' from names where name_txt = 'Pirellula sp.';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Polaromonas JS666', 'synonym' from names where name_txt = 'Polaromonas sp. JS666';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Prochlorococcus marinus CCMP1375', 'synonym' from names where name_txt = 'Prochlorococcus marinus subsp. marinus str. CCMP1375';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Prochlorococcus marinus MIT 9312', 'synonym' from names where name_txt = 'Prochlorococcus marinus str. MIT 9312';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Prochlorococcus marinus NATL2A', 'synonym' from names where name_txt = 'Prochlorococcus marinus str. NATL2A';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Pseudomonas syringae phaseolicola 1448A', 'synonym' from names where name_txt = 'Pseudomonas syringae pv. phaseolicola 1448A';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Pseudomonas syringae pv B728a', 'synonym' from names where name_txt = 'Pseudomonas syringae pv. syringae B728a';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Pseudomonas syringae tomato DC3000', 'synonym' from names where name_txt = 'Pseudomonas syringae pv. tomato str. DC3000';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Rhizobium leguminosarum bv viciae 3841', 'synonym' from names where name_txt = 'Rhizobium leguminosarum bv. viciae 3841';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Rhodobacter sphaeroides 2 4 1', 'synonym' from names where name_txt = 'Rhodobacter sphaeroides 2.4.1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Rhodococcus RHA1', 'synonym' from names where name_txt = 'Rhodococcus sp. RHA1';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Rickettsia typhi wilmington', 'synonym' from names where name_txt = 'Rickettsia typhi str. Wilmington';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella enterica Choleraesuis', 'synonym' from names where name_txt = 'Salmonella enterica subsp. enterica serovar Choleraesuis';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella enterica Paratypi ATCC 9150', 'synonym' from names where name_txt = 'Salmonella enterica subsp. enterica serovar Paratyphi A str. ATCC 9150';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Salmonella typhi Ty2', 'synonym' from names where name_txt = 'Salmonella enterica subsp. enterica serovar Typhi Ty2';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Shewanella ANA-3', 'synonym' from names where name_txt = 'Shewanella sp. ANA-3';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Shewanella MR-4', 'synonym' from names where name_txt = 'Shewanella sp. MR-4';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Shewanella MR-7', 'synonym' from names where name_txt = 'Shewanella sp. MR-7';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Shigella flexneri 2a 2457T', 'synonym' from names where name_txt = 'Shigella flexneri 2a str. 2457T';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Shigella flexneri 5 8401', 'synonym' from names where name_txt = 'Shigella flexneri 5 str. 8401';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Silicibacter TM1040', 'synonym' from names where name_txt = 'Silicibacter sp. TM1040';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Sodalis glossinidius morsitans', 'synonym' from names where name_txt = 'Sodalis glossinidius str. \'morsitans\'';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus aureus MRSA252', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus MRSA252';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus aureus MSSA476', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus MSSA476';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus Mu50', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus Mu50';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus MW2', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus MW2';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus N315', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus N315';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Staphylococcus aureus USA300', 'synonym' from names where name_txt = 'Staphylococcus aureus subsp. aureus USA300';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Streptococcus agalactiae 2603', 'synonym' from names where name_txt = 'Streptococcus agalactiae 2603V/R';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Synechococcus CC9311', 'synonym' from names where name_txt = 'Synechococcus sp. CC9311';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Synechococcus CC9605', 'synonym' from names where name_txt = 'Synechococcus sp. CC9605';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Synechococcus CC9902', 'synonym' from names where name_txt = 'Synechococcus sp. CC9902';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Synechococcus sp WH8102', 'synonym' from names where name_txt = 'Synechococcus sp. WH 8102';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Syntrophomonas wolfei Goettingen', 'synonym' from names where name_txt = 'Syntrophomonas wolfei subsp. wolfei str. Goettingen';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Tropheryma whipplei TW08 27', 'synonym' from names where name_txt = 'Tropheryma whipplei TW08/27';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Xanthomonas campestris 8004', 'synonym' from names where name_txt = 'Xanthomonas campestris pv. campestris str. 8004';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Xanthomonas campestris vesicatoria 85-10', 'synonym' from names where name_txt = 'Xanthomonas campestris pv. vesicatoria str. 85-10';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Xanthomonas oryzae KACC10331', 'synonym' from names where name_txt = 'Xanthomonas oryzae pv. oryzae KACC10331';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Xanthomonas oryzae MAFF 311018', 'synonym' from names where name_txt = 'Xanthomonas oryzae pv. oryzae MAFF 311018';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Zymomonas mobilis ZM4', 'synonym' from names where name_txt = 'Zymomonas mobilis subsp. mobilis ZM4';



# definite typo, confirmed against FASTA header
insert into names (tax_id, name_txt, name_class) select tax_id, 'Bacteroides fragilis NCTC 9434', 'misspelling' from names where name_txt = 'Bacteroides fragilis NCTC 9343';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Wolbachia endosymbiont of Brugia malayi TRS', 'misspelling' from names where name_txt = 'Wolbachia endosymbiont of Brugia malayi';
insert into names (tax_id, name_txt, name_class) select tax_id, 'Yersinia pestis biovar Mediaevails', 'misspelling' from names where name_txt = 'Yersinia pestis biovar Microtus str. 91001';
