# here we create a table listing all genome names for full bacterial genomes from NCBI as of 2008-06-05
# the directory names use underscores instead of spaces; we fix that
# then we can check which of these names do not exist in the NCBI taxonomy names table

use ncbi_taxonomy;
drop table if exists tmp;
create table tmp (name_txt varchar(255));


Acaryochloris marina MBIC11017
Acholeplasma laidlawii PG 8A
Acidiphilium cryptum JF-5
Acidobacteria bacterium Ellin345
Acidothermus cellulolyticus 11B
Acidovorax avenae citrulli AAC00-1
Acidovorax JS42
Acinetobacter baumannii ATCC 17978
Acinetobacter baumannii AYE
Acinetobacter baumannii SDF
Acinetobacter sp ADP1
Actinobacillus pleuropneumoniae L20
Actinobacillus pleuropneumoniae serovar 3 JL03
Actinobacillus succinogenes 130Z
Aeromonas hydrophila ATCC 7966
Aeromonas salmonicida A449
Aeropyrum pernix
Agrobacterium tumefaciens C58 Cereon
Alcanivorax borkumensis SK2
Alkalilimnicola ehrlichei MLHE-1
Alkaliphilus metalliredigens QYMF
Alkaliphilus oremlandii OhILAs
Anabaena variabilis ATCC 29413
Anaeromyxobacter dehalogenans 2CP-C
Anaeromyxobacter Fw109-5
Anaplasma marginale St Maries
Anaplasma phagocytophilum HZ
Aquifex aeolicus
Archaeoglobus fulgidus
Arcobacter butzleri RM4018
Arthrobacter aurescens TC1
Arthrobacter FB24
Aster yellows witches-broom phytoplasma AYWB
Azoarcus BH72
Azoarcus sp EbN1
Azorhizobium caulinodans ORS 571
Bacillus amyloliquefaciens FZB42
Bacillus anthracis Ames
Bacillus anthracis Ames 0581
Bacillus anthracis str Sterne
Bacillus cereus ATCC 10987
Bacillus cereus ATCC14579
Bacillus cereus cytotoxis NVH 391-98
Bacillus cereus ZK
Bacillus clausii KSM-K16
Bacillus halodurans
Bacillus licheniformis ATCC 14580
Bacillus licheniformis DSM 13
Bacillus pumilus SAFR-032
Bacillus subtilis
Bacillus thuringiensis Al Hakam
Bacillus thuringiensis konkukian
Bacillus weihenstephanensis KBAB4
Bacteroides fragilis NCTC 9434
Bacteroides fragilis YCH46
Bacteroides thetaiotaomicron VPI-5482
Bacteroides vulgatus ATCC 8482
Bartonella bacilliformis KC583
Bartonella henselae Houston-1
Bartonella quintana Toulouse
Bartonella tribocorum CIP 105476
Baumannia cicadellinicola Homalodisca coagulata
Bdellovibrio bacteriovorus
Beijerinckia indica ATCC 9039
Bifidobacterium adolescentis ATCC 15703
Bifidobacterium longum
Bordetella avium 197N
Bordetella bronchiseptica
Bordetella parapertussis
Bordetella pertussis
Bordetella petrii
Borrelia afzelii PKo
Borrelia burgdorferi
Borrelia garinii PBi
Bradyrhizobium BTAi1
Bradyrhizobium japonicum
Bradyrhizobium ORS278
Brucella abortus 9-941
Brucella abortus S19
Brucella canis ATCC 23365
Brucella melitensis
Brucella melitensis biovar Abortus
Brucella ovis
Brucella suis 1330
Brucella suis ATCC 23445
Buchnera aphidicola
Buchnera aphidicola Cc Cinara cedri
Buchnera aphidicola Sg
Buchnera sp
Burkholderia 383
Burkholderia ambifaria MC40 6
Burkholderia cenocepacia AU 1054
Burkholderia cenocepacia HI2424
Burkholderia cenocepacia MC0 3
Burkholderia cepacia AMMD
Burkholderia mallei ATCC 23344
Burkholderia mallei NCTC 10229
Burkholderia mallei NCTC 10247
Burkholderia mallei SAVP1
Burkholderia multivorans ATCC 17616
Burkholderia phymatum STM815
Burkholderia pseudomallei 1106a
Burkholderia pseudomallei 1710b
Burkholderia pseudomallei 668
Burkholderia pseudomallei K96243
Burkholderia thailandensis E264
Burkholderia vietnamiensis G4
Burkholderia xenovorans LB400
Caldicellulosiruptor saccharolyticus DSM 8903
Caldivirga maquilingensis IC-167
Campylobacter concisus 13826
Campylobacter curvus 525 92
Campylobacter fetus 82-40
Campylobacter hominis ATCC BAA-381
Campylobacter jejuni
Campylobacter jejuni 81116
Campylobacter jejuni 81-176
Campylobacter jejuni doylei 269 97
Campylobacter jejuni RM1221
Candidatus Blochmannia floridanus
Candidatus Blochmannia pennsylvanicus BPEN
Candidatus Carsonella ruddii PV
Candidatus Desulfococcus oleovorans Hxd3
Candidatus Desulforudis audaxviator MP104C
Candidatus Korarchaeum cryptofilum OPF8
Candidatus Methanoregula boonei 6A8
Candidatus Pelagibacter ubique HTCC1062
Candidatus Ruthia magnifica Cm Calyptogena magnifica 
Candidatus Sulcia muelleri GWSS
Candidatus Vesicomyosocius okutanii HA
Carboxydothermus hydrogenoformans Z-2901
Caulobacter crescentus
Caulobacter K31
Chlamydia muridarum
Chlamydia trachomatis
Chlamydia trachomatis 434 Bu
Chlamydia trachomatis A HAR-13
Chlamydia trachomatis L2b UCH 1 proctitis
Chlamydophila abortus S26 3
Chlamydophila caviae
Chlamydophila felis Fe C-56
Chlamydophila pneumoniae AR39
Chlamydophila pneumoniae CWL029
Chlamydophila pneumoniae J138
Chlamydophila pneumoniae TW 183
Chlorobium chlorochromatii CaD3
Chlorobium phaeobacteroides DSM 266
Chlorobium tepidum TLS
Chloroflexus aurantiacus J 10 fl
Chromobacterium violaceum
Chromohalobacter salexigens DSM 3043
Citrobacter koseri ATCC BAA-895
Clavibacter michiganensis NCPPB 382
Clavibacter michiganensis sepedonicus
Clostridium acetobutylicum
Clostridium beijerinckii NCIMB 8052
Clostridium botulinum A
Clostridium botulinum A3 Loch Maree
Clostridium botulinum A ATCC 19397
Clostridium botulinum A Hall
Clostridium botulinum B1 Okra
Clostridium botulinum B Eklund 17B
Clostridium botulinum F Langeland
Clostridium difficile 630
Clostridium kluyveri DSM 555
Clostridium novyi NT
Clostridium perfringens
Clostridium perfringens ATCC 13124
Clostridium perfringens SM101
Clostridium phytofermentans ISDg
Clostridium tetani E88
Clostridium thermocellum ATCC 27405
Colwellia psychrerythraea 34H
Corynebacterium diphtheriae
Corynebacterium efficiens YS-314
Corynebacterium glutamicum ATCC 13032 Bielefeld
Corynebacterium glutamicum ATCC 13032 Kitasato
Corynebacterium glutamicum R
Corynebacterium jeikeium K411
Corynebacterium urealyticum DSM 7109
Coxiella burnetii
Coxiella burnetii Dugway 7E9-12
Coxiella burnetii RSA 331
Cupriavidus taiwanensis
Cyanobacteria bacterium Yellowstone A-Prime
Cyanobacteria bacterium Yellowstone B-Prime
Cyanothece ATCC 51142
Cytophaga hutchinsonii ATCC 33406
Dechloromonas aromatica RCB
Dehalococcoides BAV1
Dehalococcoides CBDB1
Dehalococcoides ethenogenes 195
Deinococcus geothermalis DSM 11300
Deinococcus radiodurans
Delftia acidovorans SPH-1
Desulfitobacterium hafniense Y51
Desulfotalea psychrophila LSv54
Desulfotomaculum reducens MI-1
Desulfovibrio desulfuricans G20
Desulfovibrio vulgaris DP4
Desulfovibrio vulgaris Hildenborough
Dichelobacter nodosus VCS1703A
Dinoroseobacter shibae DFL 12
Ehrlichia canis Jake
Ehrlichia chaffeensis Arkansas
Ehrlichia ruminantium Gardel
Ehrlichia ruminantium str. Welgevonden
Ehrlichia ruminantium Welgevonden
Enterobacter 638
Enterobacter sakazakii ATCC BAA-894
Enterococcus faecalis V583
Erwinia carotovora atroseptica SCRI1043
Erythrobacter litoralis HTCC2594
Escherichia coli 536
Escherichia coli APEC O1
Escherichia coli C ATCC 8739
Escherichia coli CFT073
Escherichia coli E24377A
Escherichia coli HS
Escherichia coli K 12 substr  DH10B
Escherichia coli K12 substr  MG1655
Escherichia coli O157H7
Escherichia coli O157H7 EDL933
Escherichia coli SECEC SMS 3 5
Escherichia coli UTI89
Escherichia coli W3110
Exiguobacterium sibiricum 255 15
Fervidobacterium nodosum Rt17-B1
Finegoldia magna ATCC 29328
Flavobacterium johnsoniae UW101
Flavobacterium psychrophilum JIP02 86
Francisella philomiragia ATCC 25017
Francisella tularensis FSC 198
Francisella tularensis holarctica
Francisella tularensis holarctica FTA
Francisella tularensis holarctica OSU18
Francisella tularensis mediasiatica FSC147
Francisella tularensis novicida U112
Francisella tularensis tularensis
Francisella tularensis WY96-3418
Frankia alni ACN14a
Frankia CcI3
Frankia EAN1pec
Fusobacterium nucleatum
Geobacillus kaustophilus HTA426
Geobacillus thermodenitrificans NG80-2
Geobacter metallireducens GS-15
Geobacter sulfurreducens
Geobacter uraniumreducens Rf4
Gloeobacter violaceus
Gluconacetobacter diazotrophicus PAl 5
Gluconobacter oxydans 621H
Gramella forsetii KT0803
Granulobacter bethesdensis CGDNIH1
Haemophilus ducreyi 35000HP
Haemophilus influenzae
Haemophilus influenzae 86 028NP
Haemophilus influenzae PittEE
Haemophilus influenzae PittGG
Haemophilus somnus 129PT
Haemophilus somnus 2336
Hahella chejuensis KCTC 2396
Haloarcula marismortui ATCC 43049
Halobacterium salinarum R1
Halobacterium sp
Haloquadratum walsbyi
Halorhodospira halophila SL1
Helicobacter acinonychis Sheeba
Helicobacter hepaticus
Helicobacter pylori 26695
Helicobacter pylori HPAG1
Helicobacter pylori J99
Heliobacterium modesticaldum Ice1
Herminiimonas arsenicoxydans
Herpetosiphon aurantiacus ATCC 23779
Hyperthermus butylicus
Hyphomonas neptunium ATCC 15444
Idiomarina loihiensis L2TR
Ignicoccus hospitalis KIN4 I
Jannaschia CCS1
Janthinobacterium Marseille
Kineococcus radiotolerans SRS30216
Klebsiella pneumoniae MGH 78578
Lactobacillus acidophilus NCFM
Lactobacillus brevis ATCC 367
Lactobacillus casei ATCC 334
Lactobacillus delbrueckii bulgaricus
Lactobacillus delbrueckii bulgaricus ATCC BAA-365
Lactobacillus fermentum IFO 3956
Lactobacillus gasseri ATCC 33323
Lactobacillus helveticus DPC 4571
Lactobacillus johnsonii NCC 533
Lactobacillus plantarum
Lactobacillus reuteri F275
Lactobacillus sakei 23K
Lactobacillus salivarius UCC118
Lactococcus lactis
Lactococcus lactis cremoris MG1363
Lactococcus lactis cremoris SK11
Lawsonia intracellularis PHE MN1-00
Legionella pneumophila Corby
Legionella pneumophila Lens
Legionella pneumophila Paris
Legionella pneumophila Philadelphia 1
Leifsonia xyli xyli CTCB0
Leptospira biflexa serovar Patoc  Patoc 1  Paris 
Leptospira borgpetersenii serovar Hardjo-bovis JB197
Leptospira borgpetersenii serovar Hardjo-bovis L550
Leptospira interrogans serovar Copenhageni
Leptospira interrogans serovar Lai
Leptothrix cholodnii SP 6
Leuconostoc citreum KM20
Leuconostoc mesenteroides ATCC 8293
Listeria innocua
Listeria monocytogenes
Listeria monocytogenes 4b F2365
Listeria welshimeri serovar 6b SLCC5334
Lysinibacillus sphaericus C3 41
Magnetococcus MC-1
Magnetospirillum magneticum AMB-1
Mannheimia succiniciproducens MBEL55E
Maricaulis maris MCS10
Marinobacter aquaeolei VT8
Marinomonas MWYL1
Mesoplasma florum L1
Mesorhizobium BNC1
Mesorhizobium loti
Metallosphaera sedula DSM 5348
Methanobacterium thermoautotrophicum
Methanobrevibacter smithii ATCC 35061
Methanococcoides burtonii DSM 6242
Methanococcus aeolicus Nankai-3
Methanococcus jannaschii
Methanococcus maripaludis C5
Methanococcus maripaludis C6
Methanococcus maripaludis C7
Methanococcus maripaludis S2
Methanococcus vannielii SB
Methanocorpusculum labreanum Z
Methanoculleus marisnigri JR1
Methanopyrus kandleri
Methanosaeta thermophila PT
Methanosarcina acetivorans
Methanosarcina barkeri fusaro
Methanosarcina mazei
Methanosphaera stadtmanae
Methanospirillum hungatei JF-1
Methylibium petroleiphilum PM1
Methylobacillus flagellatus KT
Methylobacterium 4 46
Methylobacterium extorquens PA1
Methylobacterium radiotolerans JCM 2831
Methylococcus capsulatus Bath
Microcystis aeruginosa NIES 843
Moorella thermoacetica ATCC 39073
Mycobacterium abscessus ATCC 19977T
Mycobacterium avium 104
Mycobacterium avium paratuberculosis
Mycobacterium bovis
Mycobacterium bovis BCG Pasteur 1173P2
Mycobacterium gilvum PYR-GCK
Mycobacterium JLS
Mycobacterium KMS
Mycobacterium leprae
Mycobacterium marinum M
Mycobacterium MCS
Mycobacterium smegmatis MC2 155
Mycobacterium tuberculosis CDC1551
Mycobacterium tuberculosis F11
Mycobacterium tuberculosis H37Ra
Mycobacterium tuberculosis H37Rv
Mycobacterium ulcerans Agy99
Mycobacterium vanbaalenii PYR-1
Mycoplasma agalactiae PG2
Mycoplasma capricolum ATCC 27343
Mycoplasma gallisepticum
Mycoplasma genitalium
Mycoplasma hyopneumoniae 232
Mycoplasma hyopneumoniae 7448
Mycoplasma hyopneumoniae J
Mycoplasma mobile 163K
Mycoplasma mycoides
Mycoplasma penetrans
Mycoplasma pneumoniae
Mycoplasma pulmonis
Mycoplasma synoviae 53
Myxococcus xanthus DK 1622
Nanoarchaeum equitans
Natronomonas pharaonis
Neisseria gonorrhoeae FA 1090
Neisseria meningitidis 053442
Neisseria meningitidis FAM18
Neisseria meningitidis MC58
Neisseria meningitidis Z2491
Neorickettsia sennetsu Miyayama
Nitratiruptor SB155-2
Nitrobacter hamburgensis X14
Nitrobacter winogradskyi Nb-255
Nitrosococcus oceani ATCC 19707
Nitrosomonas europaea
Nitrosomonas eutropha C71
Nitrosopumilus maritimus SCM1
Nitrosospira multiformis ATCC 25196
Nocardia farcinica IFM10152
Nocardioides JS614
Nostoc sp
Novosphingobium aromaticivorans DSM 12444
Oceanobacillus iheyensis
Ochrobactrum anthropi ATCC 49188
Oenococcus oeni PSU-1
Onion yellows phytoplasma
Opitutus terrae PB90 1
Orientia tsutsugamushi Boryong
Orientia tsutsugamushi Ikeda
Parabacteroides distasonis ATCC 8503
Parachlamydia sp UWE25
Paracoccus denitrificans PD1222
Parvibaculum lavamentivorans DS-1
Pasteurella multocida
Pediococcus pentosaceus ATCC 25745
Pelobacter carbinolicus
Pelobacter propionicus DSM 2379
Pelodictyon luteolum DSM 273
Pelotomaculum thermopropionicum SI
Petrotoga mobilis SJ95
Photobacterium profundum SS9
Photorhabdus luminescens
Picrophilus torridus DSM 9790
Pirellula sp
Polaromonas JS666
Polaromonas naphthalenivorans CJ2
Polynucleobacter necessarius STIR1
Polynucleobacter QLW-P1DMWA-1
Porphyromonas gingivalis W83
Prochlorococcus marinus AS9601
Prochlorococcus marinus CCMP1375
Prochlorococcus marinus MED4
Prochlorococcus marinus MIT 9211
Prochlorococcus marinus MIT 9215
Prochlorococcus marinus MIT 9301
Prochlorococcus marinus MIT 9303
Prochlorococcus marinus MIT 9312
Prochlorococcus marinus MIT9313
Prochlorococcus marinus MIT 9515
Prochlorococcus marinus NATL1A
Prochlorococcus marinus NATL2A
Propionibacterium acnes KPA171202
Prosthecochloris vibrioformis DSM 265
Pseudoalteromonas atlantica T6c
Pseudoalteromonas haloplanktis TAC125
Pseudomonas aeruginosa
Pseudomonas aeruginosa PA7
Pseudomonas aeruginosa UCBPP-PA14
Pseudomonas entomophila L48
Pseudomonas fluorescens Pf-5
Pseudomonas fluorescens PfO-1
Pseudomonas mendocina ymp
Pseudomonas putida F1
Pseudomonas putida GB 1
Pseudomonas putida KT2440
Pseudomonas putida W619
Pseudomonas stutzeri A1501
Pseudomonas syringae phaseolicola 1448A
Pseudomonas syringae pv B728a
Pseudomonas syringae tomato DC3000
Psychrobacter arcticum 273-4
Psychrobacter cryohalolentis K5
Psychrobacter PRwf-1
Psychromonas ingrahamii 37
Pyrobaculum aerophilum
Pyrobaculum arsenaticum DSM 13514
Pyrobaculum calidifontis JCM 11548
Pyrobaculum islandicum DSM 4184
Pyrococcus abyssi
Pyrococcus furiosus
Pyrococcus horikoshii
Ralstonia eutropha H16
Ralstonia eutropha JMP134
Ralstonia metallidurans CH34
Ralstonia solanacearum
Renibacterium salmoninarum ATCC 33209
Rhizobium etli CFN 42
Rhizobium leguminosarum bv viciae 3841
Rhodobacter sphaeroides 2 4 1
Rhodobacter sphaeroides ATCC 17025
Rhodobacter sphaeroides ATCC 17029
Rhodococcus RHA1
Rhodoferax ferrireducens T118
Rhodopseudomonas palustris BisA53
Rhodopseudomonas palustris BisB18
Rhodopseudomonas palustris BisB5
Rhodopseudomonas palustris CGA009
Rhodopseudomonas palustris HaA2
Rhodospirillum rubrum ATCC 11170
Rickettsia akari Hartford
Rickettsia bellii OSU 85-389
Rickettsia bellii RML369-C
Rickettsia canadensis McKiel
Rickettsia conorii
Rickettsia felis URRWXCal2
Rickettsia massiliae MTU5
Rickettsia prowazekii
Rickettsia rickettsii Iowa
Rickettsia rickettsii Sheila Smith
Rickettsia typhi wilmington
Roseiflexus castenholzii DSM 13941
Roseiflexus RS-1
Roseobacter denitrificans OCh 114
Rubrobacter xylanophilus DSM 9941
Saccharophagus degradans 2-40
Saccharopolyspora erythraea NRRL 2338
Salinibacter ruber DSM 13855
Salinispora arenicola CNS-205
Salinispora tropica CNB-440
Salmonella enterica arizonae serovar 62 z4 z23  
Salmonella enterica Choleraesuis
Salmonella enterica Paratypi ATCC 9150
Salmonella enterica serovar Paratyphi B SPB7
Salmonella typhi
Salmonella typhimurium LT2
Salmonella typhi Ty2
Serratia proteamaculans 568
Shewanella amazonensis SB2B
Shewanella ANA-3
Shewanella baltica OS155
Shewanella baltica OS185
Shewanella baltica OS195
Shewanella denitrificans OS217
Shewanella frigidimarina NCIMB 400
Shewanella halifaxensis HAW EB4
Shewanella loihica PV-4
Shewanella MR-4
Shewanella MR-7
Shewanella oneidensis
Shewanella pealeana ATCC 700345
Shewanella putrefaciens CN-32
Shewanella sediminis HAW-EB3
Shewanella W3-18-1
Shewanella woodyi ATCC 51908
Shigella boydii CDC 3083 94
Shigella boydii Sb227
Shigella dysenteriae
Shigella flexneri 2a
Shigella flexneri 2a 2457T
Shigella flexneri 5 8401
Shigella sonnei Ss046
Silicibacter pomeroyi DSS-3
Silicibacter TM1040
Sinorhizobium medicae WSM419
Sinorhizobium meliloti
Sodalis glossinidius morsitans
Solibacter usitatus Ellin6076
Sorangium cellulosum  So ce 56 
Sphingomonas wittichii RW1
Sphingopyxis alaskensis RB2256
Staphylococcus aureus aureus MRSA252
Staphylococcus aureus aureus MSSA476
Staphylococcus aureus COL
Staphylococcus aureus JH1
Staphylococcus aureus JH9
Staphylococcus aureus Mu3
Staphylococcus aureus Mu50
Staphylococcus aureus MW2
Staphylococcus aureus N315
Staphylococcus aureus NCTC 8325
Staphylococcus aureus Newman
Staphylococcus aureus RF122
Staphylococcus aureus USA300
Staphylococcus aureus USA300 TCH1516
Staphylococcus epidermidis ATCC 12228
Staphylococcus epidermidis RP62A
Staphylococcus haemolyticus
Staphylococcus saprophyticus
Staphylothermus marinus F1
Streptococcus agalactiae 2603
Streptococcus agalactiae A909
Streptococcus agalactiae NEM316
Streptococcus gordonii Challis substr CH1
Streptococcus mutans
Streptococcus pneumoniae CGSP14
Streptococcus pneumoniae D39
Streptococcus pneumoniae Hungary19A 6
Streptococcus pneumoniae R6
Streptococcus pneumoniae TIGR4
Streptococcus pyogenes M1 GAS
Streptococcus pyogenes Manfredo
Streptococcus pyogenes MGAS10270
Streptococcus pyogenes MGAS10394
Streptococcus pyogenes MGAS10750
Streptococcus pyogenes MGAS2096
Streptococcus pyogenes MGAS315
Streptococcus pyogenes MGAS5005
Streptococcus pyogenes MGAS6180
Streptococcus pyogenes MGAS8232
Streptococcus pyogenes MGAS9429
Streptococcus pyogenes SSI-1
Streptococcus sanguinis SK36
Streptococcus suis 05ZYH33
Streptococcus suis 98HAH33
Streptococcus thermophilus CNRZ1066
Streptococcus thermophilus LMD-9
Streptococcus thermophilus LMG 18311
Streptomyces avermitilis
Streptomyces coelicolor
Streptomyces griseus NBRC 13350
Sulfolobus acidocaldarius DSM 639
Sulfolobus solfataricus
Sulfolobus tokodaii
Sulfurovum NBC37-1
Symbiobacterium thermophilum IAM14863
Synechococcus CC9311
Synechococcus CC9605
Synechococcus CC9902
Synechococcus elongatus PCC 6301
Synechococcus elongatus PCC 7942
Synechococcus PCC 7002
Synechococcus RCC307
Synechococcus sp WH8102
Synechococcus WH 7803
Synechocystis PCC6803
Syntrophobacter fumaroxidans MPOB
Syntrophomonas wolfei Goettingen
Syntrophus aciditrophicus SB
Thermoanaerobacter pseudethanolicus ATCC 33223
Thermoanaerobacter tengcongensis
Thermoanaerobacter X514
Thermobifida fusca YX
Thermococcus kodakaraensis KOD1
Thermofilum pendens Hrk 5
Thermoplasma acidophilum
Thermoplasma volcanium
Thermoproteus neutrophilus V24Sta
Thermosipho melanesiensis BI429
Thermosynechococcus elongatus
Thermotoga lettingae TMO
Thermotoga maritima
Thermotoga petrophila RKU-1
Thermotoga RQ2
Thermus thermophilus HB27
Thermus thermophilus HB8
Thiobacillus denitrificans ATCC 25259
Thiomicrospira crunogena XCL-2
Thiomicrospira denitrificans ATCC 33889
Treponema denticola ATCC 35405
Treponema pallidum
Trichodesmium erythraeum IMS101
Tropheryma whipplei TW08 27
Tropheryma whipplei Twist
uncultured methanogenic archaeon RC-I
Ureaplasma parvum serovar 3 ATCC 27815
Ureaplasma urealyticum
Verminephrobacter eiseniae EF01-2
Vibrio cholerae
Vibrio cholerae O395
Vibrio fischeri ES114
Vibrio harveyi ATCC BAA-1116
Vibrio parahaemolyticus
Vibrio vulnificus CMCP6
Vibrio vulnificus YJ016
Wigglesworthia brevipalpis
Wolbachia endosymbiont of Brugia malayi TRS
Wolbachia endosymbiont of Drosophila melanogaster
Wolinella succinogenes
Xanthobacter autotrophicus Py2
Xanthomonas campestris
Xanthomonas campestris 8004
Xanthomonas campestris vesicatoria 85-10
Xanthomonas citri
Xanthomonas oryzae KACC10331
Xanthomonas oryzae MAFF 311018
Xylella fastidiosa
Xylella fastidiosa M12
Xylella fastidiosa M23
Xylella fastidiosa Temecula1
Yersinia enterocolitica 8081
Yersinia pestis Angola
Yersinia pestis Antiqua
Yersinia pestis biovar Mediaevails
Yersinia pestis CO92
Yersinia pestis KIM
Yersinia pestis Nepal516
Yersinia pestis Pestoides F
Yersinia pseudotuberculosis IP 31758
Yersinia pseudotuberculosis IP32953
Yersinia pseudotuberculosis YPIII
Zymomonas mobilis ZM4












insert into tmp values ('Acidobacteria bacterium Ellin345');
insert into tmp values ('Acidothermus cellulolyticus 11B');
insert into tmp values ('Acinetobacter sp ADP1');
insert into tmp values ('Aeromonas hydrophila ATCC 7966');
insert into tmp values ('Aeropyrum pernix');
insert into tmp values ('Agrobacterium tumefaciens C58 Cereon');
insert into tmp values ('Agrobacterium tumefaciens C58 UWash');
insert into tmp values ('Alcanivorax borkumensis SK2');
insert into tmp values ('Alkalilimnicola ehrlichei MLHE-1');
insert into tmp values ('Anabaena variabilis ATCC 29413');
insert into tmp values ('Anaeromyxobacter dehalogenans 2CP-C');
insert into tmp values ('Anaplasma marginale St Maries');
insert into tmp values ('Anaplasma phagocytophilum HZ');
insert into tmp values ('Aquifex aeolicus');
insert into tmp values ('Archaeoglobus fulgidus');
insert into tmp values ('Arthrobacter FB24');
insert into tmp values ('Aster yellows witches-broom phytoplasma AYWB');
insert into tmp values ('Azoarcus sp EbN1');
insert into tmp values ('Bacillus anthracis Ames');
insert into tmp values ('Bacillus anthracis Ames 0581');
insert into tmp values ('Bacillus anthracis str Sterne');
insert into tmp values ('Bacillus cereus ATCC 10987');
insert into tmp values ('Bacillus cereus ATCC14579');
insert into tmp values ('Bacillus cereus ZK');
insert into tmp values ('Bacillus clausii KSM-K16');
insert into tmp values ('Bacillus halodurans');
insert into tmp values ('Bacillus licheniformis ATCC 14580');
insert into tmp values ('Bacillus licheniformis DSM 13');
insert into tmp values ('Bacillus subtilis');
insert into tmp values ('Bacillus thuringiensis konkukian');
insert into tmp values ('Bacteroides fragilis NCTC 9434');
insert into tmp values ('Bacteroides fragilis YCH46');
insert into tmp values ('Bacteroides thetaiotaomicron VPI-5482');
insert into tmp values ('Bartonella henselae Houston-1');
insert into tmp values ('Bartonella quintana Toulouse');
insert into tmp values ('Baumannia cicadellinicola Homalodisca coagulata');
insert into tmp values ('Bdellovibrio bacteriovorus');
insert into tmp values ('Bifidobacterium longum');
insert into tmp values ('Bordetella bronchiseptica');
insert into tmp values ('Bordetella parapertussis');
insert into tmp values ('Bordetella pertussis');
insert into tmp values ('Borrelia afzelii PKo');
insert into tmp values ('Borrelia burgdorferi');
insert into tmp values ('Borrelia garinii PBi');
insert into tmp values ('Bradyrhizobium japonicum');
insert into tmp values ('Brucella abortus 9-941');
insert into tmp values ('Brucella melitensis');
insert into tmp values ('Brucella melitensis biovar Abortus');
insert into tmp values ('Brucella suis 1330');
insert into tmp values ('Buchnera aphidicola');
insert into tmp values ('Buchnera aphidicola Cc Cinara cedri');
insert into tmp values ('Buchnera aphidicola Sg');
insert into tmp values ('Buchnera sp');
insert into tmp values ('Burkholderia 383');
insert into tmp values ('Burkholderia cenocepacia AU 1054');
insert into tmp values ('Burkholderia cenocepacia HI2424');
insert into tmp values ('Burkholderia cepacia AMMD');
insert into tmp values ('Burkholderia mallei ATCC 23344');
insert into tmp values ('Burkholderia pseudomallei 1710b');
insert into tmp values ('Burkholderia pseudomallei K96243');
insert into tmp values ('Burkholderia thailandensis E264');
insert into tmp values ('Burkholderia xenovorans LB400');
insert into tmp values ('Campylobacter jejuni');
insert into tmp values ('Campylobacter jejuni RM1221');
insert into tmp values ('Candidatus Blochmannia floridanus');
insert into tmp values ('Candidatus Blochmannia pennsylvanicus BPEN');
insert into tmp values ('Candidatus Carsonella ruddii');
insert into tmp values ('Candidatus Pelagibacter ubique HTCC1062');
insert into tmp values ('Carboxydothermus hydrogenoformans Z-2901');
insert into tmp values ('Caulobacter crescentus');
insert into tmp values ('Chlamydia muridarum');
insert into tmp values ('Chlamydia trachomatis');
insert into tmp values ('Chlamydia trachomatis A HAR-13');
insert into tmp values ('Chlamydophila abortus S26 3');
insert into tmp values ('Chlamydophila caviae');
insert into tmp values ('Chlamydophila felis Fe C-56');
insert into tmp values ('Chlamydophila pneumoniae AR39');
insert into tmp values ('Chlamydophila pneumoniae CWL029');
insert into tmp values ('Chlamydophila pneumoniae J138');
insert into tmp values ('Chlamydophila pneumoniae TW 183');
insert into tmp values ('Chlorobium chlorochromatii CaD3');
insert into tmp values ('Chlorobium tepidum TLS');
insert into tmp values ('Chromobacterium violaceum');
insert into tmp values ('Chromohalobacter salexigens DSM 3043');
insert into tmp values ('Clostridium acetobutylicum');
insert into tmp values ('Clostridium perfringens');
insert into tmp values ('Clostridium perfringens ATCC 13124');
insert into tmp values ('Clostridium perfringens SM101');
insert into tmp values ('Clostridium tetani E88');
insert into tmp values ('CLUSTERS');
insert into tmp values ('Colwellia psychrerythraea 34H');
insert into tmp values ('Corynebacterium diphtheriae');
insert into tmp values ('Corynebacterium efficiens YS-314');
insert into tmp values ('Corynebacterium glutamicum ATCC 13032 Bielefeld');
insert into tmp values ('Corynebacterium glutamicum ATCC 13032 Kitasato');
insert into tmp values ('Corynebacterium jeikeium K411');
insert into tmp values ('Coxiella burnetii');
insert into tmp values ('Cyanobacteria bacterium Yellowstone A-Prime');
insert into tmp values ('Cyanobacteria bacterium Yellowstone B-Prime');
insert into tmp values ('Cytophaga hutchinsonii ATCC 33406');
insert into tmp values ('Dechloromonas aromatica RCB');
insert into tmp values ('Dehalococcoides CBDB1');
insert into tmp values ('Dehalococcoides ethenogenes 195');
insert into tmp values ('Deinococcus geothermalis DSM 11300');
insert into tmp values ('Deinococcus radiodurans');
insert into tmp values ('Desulfitobacterium hafniense Y51');
insert into tmp values ('Desulfotalea psychrophila LSv54');
insert into tmp values ('Desulfovibrio desulfuricans G20');
insert into tmp values ('Desulfovibrio vulgaris Hildenborough');
insert into tmp values ('Ehrlichia canis Jake');
insert into tmp values ('Ehrlichia chaffeensis Arkansas');
insert into tmp values ('Ehrlichia ruminantium Gardel');
insert into tmp values ('Ehrlichia ruminantium str. Welgevonden');
insert into tmp values ('Ehrlichia ruminantium Welgevonden');
insert into tmp values ('Enterococcus faecalis V583');
insert into tmp values ('Erwinia carotovora atroseptica SCRI1043');
insert into tmp values ('Erythrobacter litoralis HTCC2594');
insert into tmp values ('Escherichia coli 536');
insert into tmp values ('Escherichia coli APEC O1');
insert into tmp values ('Escherichia coli CFT073');
insert into tmp values ('Escherichia coli K12');
insert into tmp values ('Escherichia coli O157H7');
insert into tmp values ('Escherichia coli O157H7 EDL933');
insert into tmp values ('Escherichia coli UTI89');
insert into tmp values ('Escherichia coli W3110');
insert into tmp values ('Francisella tularensis FSC 198');
insert into tmp values ('Francisella tularensis holarctica');
insert into tmp values ('Francisella tularensis holarctica OSU18');
insert into tmp values ('Francisella tularensis tularensis');
insert into tmp values ('Frankia alni ACN14a');
insert into tmp values ('Frankia CcI3');
insert into tmp values ('Fusobacterium nucleatum');
insert into tmp values ('Geobacillus kaustophilus HTA426');
insert into tmp values ('Geobacter metallireducens GS-15');
insert into tmp values ('Geobacter sulfurreducens');
insert into tmp values ('Gloeobacter violaceus');
insert into tmp values ('Gluconobacter oxydans 621H');
insert into tmp values ('Granulobacter bethesdensis CGDNIH1');
insert into tmp values ('Haemophilus ducreyi 35000HP');
insert into tmp values ('Haemophilus influenzae');
insert into tmp values ('Haemophilus influenzae 86 028NP');
insert into tmp values ('Haemophilus somnus 129PT');
insert into tmp values ('Hahella chejuensis KCTC 2396');
insert into tmp values ('Haloarcula marismortui ATCC 43049');
insert into tmp values ('Halobacterium sp');
insert into tmp values ('Haloquadratum walsbyi');
insert into tmp values ('Helicobacter acinonychis Sheeba');
insert into tmp values ('Helicobacter hepaticus');
insert into tmp values ('Helicobacter pylori 26695');
insert into tmp values ('Helicobacter pylori HPAG1');
insert into tmp values ('Helicobacter pylori J99');
insert into tmp values ('Hyphomonas neptunium ATCC 15444');
insert into tmp values ('Idiomarina loihiensis L2TR');
insert into tmp values ('Jannaschia CCS1');
insert into tmp values ('Lactobacillus acidophilus NCFM');
insert into tmp values ('Lactobacillus brevis ATCC 367');
insert into tmp values ('Lactobacillus casei ATCC 334');
insert into tmp values ('Lactobacillus delbrueckii bulgaricus');
insert into tmp values ('Lactobacillus delbrueckii bulgaricus ATCC BAA-365');
insert into tmp values ('Lactobacillus gasseri ATCC 33323');
insert into tmp values ('Lactobacillus johnsonii NCC 533');
insert into tmp values ('Lactobacillus plantarum');
insert into tmp values ('Lactobacillus sakei 23K');
insert into tmp values ('Lactobacillus salivarius UCC118');
insert into tmp values ('Lactococcus lactis');
insert into tmp values ('Lactococcus lactis cremoris SK11');
insert into tmp values ('Lawsonia intracellularis PHE MN1-00');
insert into tmp values ('Legionella pneumophila Lens');
insert into tmp values ('Legionella pneumophila Paris');
insert into tmp values ('Legionella pneumophila Philadelphia 1');
insert into tmp values ('Leifsonia xyli xyli CTCB0');
insert into tmp values ('Leptospira borgpetersenii serovar Hardjo-bovis JB197');
insert into tmp values ('Leptospira borgpetersenii serovar Hardjo-bovis L550');
insert into tmp values ('Leptospira interrogans serovar Copenhageni');
insert into tmp values ('Leptospira interrogans serovar Lai');
insert into tmp values ('Leuconostoc mesenteroides ATCC 8293');
insert into tmp values ('Listeria innocua');
insert into tmp values ('Listeria monocytogenes');
insert into tmp values ('Listeria monocytogenes 4b F2365');
insert into tmp values ('Listeria welshimeri serovar 6b SLCC5334');
insert into tmp values ('Magnetococcus MC-1');
insert into tmp values ('Magnetospirillum magneticum AMB-1');
insert into tmp values ('Mannheimia succiniciproducens MBEL55E');
insert into tmp values ('Maricaulis maris MCS10');
insert into tmp values ('Mesoplasma florum L1');
insert into tmp values ('Mesorhizobium BNC1');
insert into tmp values ('Mesorhizobium loti');
insert into tmp values ('Methanobacterium thermoautotrophicum');
insert into tmp values ('Methanococcoides burtonii DSM 6242');
insert into tmp values ('Methanococcus jannaschii');
insert into tmp values ('Methanococcus maripaludis S2');
insert into tmp values ('Methanopyrus kandleri');
insert into tmp values ('Methanosaeta thermophila PT');
insert into tmp values ('Methanosarcina acetivorans');
insert into tmp values ('Methanosarcina barkeri fusaro');
insert into tmp values ('Methanosarcina mazei');
insert into tmp values ('Methanosphaera stadtmanae');
insert into tmp values ('Methanospirillum hungatei JF-1');
insert into tmp values ('Methylobacillus flagellatus KT');
insert into tmp values ('Methylococcus capsulatus Bath');
insert into tmp values ('Moorella thermoacetica ATCC 39073');
insert into tmp values ('Mycobacterium avium paratuberculosis');
insert into tmp values ('Mycobacterium bovis');
insert into tmp values ('Mycobacterium leprae');
insert into tmp values ('Mycobacterium MCS');
insert into tmp values ('Mycobacterium tuberculosis CDC1551');
insert into tmp values ('Mycobacterium tuberculosis H37Rv');
insert into tmp values ('Mycoplasma capricolum ATCC 27343');
insert into tmp values ('Mycoplasma gallisepticum');
insert into tmp values ('Mycoplasma genitalium');
insert into tmp values ('Mycoplasma hyopneumoniae 232');
insert into tmp values ('Mycoplasma hyopneumoniae 7448');
insert into tmp values ('Mycoplasma hyopneumoniae J');
insert into tmp values ('Mycoplasma mobile 163K');
insert into tmp values ('Mycoplasma mycoides');
insert into tmp values ('Mycoplasma penetrans');
insert into tmp values ('Mycoplasma pneumoniae');
insert into tmp values ('Mycoplasma pulmonis');
insert into tmp values ('Mycoplasma synoviae 53');
insert into tmp values ('Myxococcus xanthus DK 1622');
insert into tmp values ('Nanoarchaeum equitans');
insert into tmp values ('Natronomonas pharaonis');
insert into tmp values ('Neisseria gonorrhoeae FA 1090');
insert into tmp values ('Neisseria meningitidis MC58');
insert into tmp values ('Neisseria meningitidis Z2491');
insert into tmp values ('Neorickettsia sennetsu Miyayama');
insert into tmp values ('Nitrobacter hamburgensis X14');
insert into tmp values ('Nitrobacter winogradskyi Nb-255');
insert into tmp values ('Nitrosococcus oceani ATCC 19707');
insert into tmp values ('Nitrosomonas europaea');
insert into tmp values ('Nitrosomonas eutropha C71');
insert into tmp values ('Nitrosospira multiformis ATCC 25196');
insert into tmp values ('Nocardia farcinica IFM10152');
insert into tmp values ('Nostoc sp');
insert into tmp values ('Novosphingobium aromaticivorans DSM 12444');
insert into tmp values ('Oceanobacillus iheyensis');
insert into tmp values ('Oenococcus oeni PSU-1');
insert into tmp values ('Onion yellows phytoplasma');
insert into tmp values ('Parachlamydia sp UWE25');
insert into tmp values ('Pasteurella multocida');
insert into tmp values ('Pediococcus pentosaceus ATCC 25745');
insert into tmp values ('Pelobacter carbinolicus');
insert into tmp values ('Pelodictyon luteolum DSM 273');
insert into tmp values ('Photobacterium profundum SS9');
insert into tmp values ('Photorhabdus luminescens');
insert into tmp values ('Picrophilus torridus DSM 9790');
insert into tmp values ('Pirellula sp');
insert into tmp values ('Polaromonas JS666');
insert into tmp values ('Porphyromonas gingivalis W83');
insert into tmp values ('Prochlorococcus marinus CCMP1375');
insert into tmp values ('Prochlorococcus marinus MED4');
insert into tmp values ('Prochlorococcus marinus MIT 9312');
insert into tmp values ('Prochlorococcus marinus MIT9313');
insert into tmp values ('Prochlorococcus marinus NATL2A');
insert into tmp values ('Propionibacterium acnes KPA171202');
insert into tmp values ('Pseudoalteromonas atlantica T6c');
insert into tmp values ('Pseudoalteromonas haloplanktis TAC125');
insert into tmp values ('Pseudomonas aeruginosa');
insert into tmp values ('Pseudomonas aeruginosa UCBPP-PA14');
insert into tmp values ('Pseudomonas entomophila L48');
insert into tmp values ('Pseudomonas fluorescens Pf-5');
insert into tmp values ('Pseudomonas fluorescens PfO-1');
insert into tmp values ('Pseudomonas putida KT2440');
insert into tmp values ('Pseudomonas syringae phaseolicola 1448A');
insert into tmp values ('Pseudomonas syringae pv B728a');
insert into tmp values ('Pseudomonas syringae tomato DC3000');
insert into tmp values ('Psychrobacter arcticum 273-4');
insert into tmp values ('Psychrobacter cryohalolentis K5');
insert into tmp values ('Pyrobaculum aerophilum');
insert into tmp values ('Pyrococcus abyssi');
insert into tmp values ('Pyrococcus furiosus');
insert into tmp values ('Pyrococcus horikoshii');
insert into tmp values ('Ralstonia eutropha H16');
insert into tmp values ('Ralstonia eutropha JMP134');
insert into tmp values ('Ralstonia metallidurans CH34');
insert into tmp values ('Ralstonia solanacearum');
insert into tmp values ('Rhizobium etli CFN 42');
insert into tmp values ('Rhizobium leguminosarum bv viciae 3841');
insert into tmp values ('Rhodobacter sphaeroides 2 4 1');
insert into tmp values ('Rhodococcus RHA1');
insert into tmp values ('Rhodoferax ferrireducens T118');
insert into tmp values ('Rhodopseudomonas palustris BisA53');
insert into tmp values ('Rhodopseudomonas palustris BisB18');
insert into tmp values ('Rhodopseudomonas palustris BisB5');
insert into tmp values ('Rhodopseudomonas palustris CGA009');
insert into tmp values ('Rhodopseudomonas palustris HaA2');
insert into tmp values ('Rhodospirillum rubrum ATCC 11170');
insert into tmp values ('Rickettsia bellii RML369-C');
insert into tmp values ('Rickettsia conorii');
insert into tmp values ('Rickettsia felis URRWXCal2');
insert into tmp values ('Rickettsia prowazekii');
insert into tmp values ('Rickettsia typhi wilmington');
insert into tmp values ('Roseobacter denitrificans OCh 114');
insert into tmp values ('Rubrobacter xylanophilus DSM 9941');
insert into tmp values ('Saccharophagus degradans 2-40');
insert into tmp values ('Salinibacter ruber DSM 13855');
insert into tmp values ('Salmonella enterica Choleraesuis');
insert into tmp values ('Salmonella enterica Paratypi ATCC 9150');
insert into tmp values ('Salmonella typhi');
insert into tmp values ('Salmonella typhimurium LT2');
insert into tmp values ('Salmonella typhi Ty2');
insert into tmp values ('Shewanella ANA-3');
insert into tmp values ('Shewanella denitrificans OS217');
insert into tmp values ('Shewanella frigidimarina NCIMB 400');
insert into tmp values ('Shewanella MR-4');
insert into tmp values ('Shewanella MR-7');
insert into tmp values ('Shewanella oneidensis');
insert into tmp values ('Shigella boydii Sb227');
insert into tmp values ('Shigella dysenteriae');
insert into tmp values ('Shigella flexneri 2a');
insert into tmp values ('Shigella flexneri 2a 2457T');
insert into tmp values ('Shigella flexneri 5 8401');
insert into tmp values ('Shigella sonnei Ss046');
insert into tmp values ('Silicibacter pomeroyi DSS-3');
insert into tmp values ('Silicibacter TM1040');
insert into tmp values ('Sinorhizobium meliloti');
insert into tmp values ('Sodalis glossinidius morsitans');
insert into tmp values ('Solibacter usitatus Ellin6076');
insert into tmp values ('Sphingopyxis alaskensis RB2256');
insert into tmp values ('Staphylococcus aureus aureus MRSA252');
insert into tmp values ('Staphylococcus aureus aureus MSSA476');
insert into tmp values ('Staphylococcus aureus COL');
insert into tmp values ('Staphylococcus aureus Mu50');
insert into tmp values ('Staphylococcus aureus MW2');
insert into tmp values ('Staphylococcus aureus N315');
insert into tmp values ('Staphylococcus aureus NCTC 8325');
insert into tmp values ('Staphylococcus aureus RF122');
insert into tmp values ('Staphylococcus aureus USA300');
insert into tmp values ('Staphylococcus epidermidis ATCC 12228');
insert into tmp values ('Staphylococcus epidermidis RP62A');
insert into tmp values ('Staphylococcus haemolyticus');
insert into tmp values ('Staphylococcus saprophyticus');
insert into tmp values ('Streptococcus agalactiae 2603');
insert into tmp values ('Streptococcus agalactiae A909');
insert into tmp values ('Streptococcus agalactiae NEM316');
insert into tmp values ('Streptococcus mutans');
insert into tmp values ('Streptococcus pneumoniae D39');
insert into tmp values ('Streptococcus pneumoniae R6');
insert into tmp values ('Streptococcus pneumoniae TIGR4');
insert into tmp values ('Streptococcus pyogenes M1 GAS');
insert into tmp values ('Streptococcus pyogenes MGAS10270');
insert into tmp values ('Streptococcus pyogenes MGAS10394');
insert into tmp values ('Streptococcus pyogenes MGAS10750');
insert into tmp values ('Streptococcus pyogenes MGAS2096');
insert into tmp values ('Streptococcus pyogenes MGAS315');
insert into tmp values ('Streptococcus pyogenes MGAS5005');
insert into tmp values ('Streptococcus pyogenes MGAS6180');
insert into tmp values ('Streptococcus pyogenes MGAS8232');
insert into tmp values ('Streptococcus pyogenes MGAS9429');
insert into tmp values ('Streptococcus pyogenes SSI-1');
insert into tmp values ('Streptococcus thermophilus CNRZ1066');
insert into tmp values ('Streptococcus thermophilus LMD-9');
insert into tmp values ('Streptococcus thermophilus LMG 18311');
insert into tmp values ('Streptomyces avermitilis');
insert into tmp values ('Streptomyces coelicolor');
insert into tmp values ('Sulfolobus acidocaldarius DSM 639');
insert into tmp values ('Sulfolobus solfataricus');
insert into tmp values ('Sulfolobus tokodaii');
insert into tmp values ('Symbiobacterium thermophilum IAM14863');
insert into tmp values ('Synechococcus CC9311');
insert into tmp values ('Synechococcus CC9605');
insert into tmp values ('Synechococcus CC9902');
insert into tmp values ('Synechococcus elongatus PCC 6301');
insert into tmp values ('Synechococcus elongatus PCC 7942');
insert into tmp values ('Synechococcus sp WH8102');
insert into tmp values ('Synechocystis PCC6803');
insert into tmp values ('Syntrophobacter fumaroxidans MPOB');
insert into tmp values ('Syntrophomonas wolfei Goettingen');
insert into tmp values ('Syntrophus aciditrophicus SB');
insert into tmp values ('Thermoanaerobacter tengcongensis');
insert into tmp values ('Thermobifida fusca YX');
insert into tmp values ('Thermococcus kodakaraensis KOD1');
insert into tmp values ('Thermoplasma acidophilum');
insert into tmp values ('Thermoplasma volcanium');
insert into tmp values ('Thermosynechococcus elongatus');
insert into tmp values ('Thermotoga maritima');
insert into tmp values ('Thermus thermophilus HB27');
insert into tmp values ('Thermus thermophilus HB8');
insert into tmp values ('Thiobacillus denitrificans ATCC 25259');
insert into tmp values ('Thiomicrospira crunogena XCL-2');
insert into tmp values ('Thiomicrospira denitrificans ATCC 33889');
insert into tmp values ('Treponema denticola ATCC 35405');
insert into tmp values ('Treponema pallidum');
insert into tmp values ('Trichodesmium erythraeum IMS101');
insert into tmp values ('Tropheryma whipplei TW08 27');
insert into tmp values ('Tropheryma whipplei Twist');
insert into tmp values ('Ureaplasma urealyticum');
insert into tmp values ('Vibrio cholerae');
insert into tmp values ('Vibrio fischeri ES114');
insert into tmp values ('Vibrio parahaemolyticus');
insert into tmp values ('Vibrio vulnificus CMCP6');
insert into tmp values ('Vibrio vulnificus YJ016');
insert into tmp values ('Wigglesworthia brevipalpis');
insert into tmp values ('Wolbachia endosymbiont of Brugia malayi TRS');
insert into tmp values ('Wolbachia endosymbiont of Drosophila melanogaster');
insert into tmp values ('Wolinella succinogenes');
insert into tmp values ('Xanthomonas campestris');
insert into tmp values ('Xanthomonas campestris 8004');
insert into tmp values ('Xanthomonas campestris vesicatoria 85-10');
insert into tmp values ('Xanthomonas citri');
insert into tmp values ('Xanthomonas oryzae KACC10331');
insert into tmp values ('Xanthomonas oryzae MAFF 311018');
insert into tmp values ('Xylella fastidiosa');
insert into tmp values ('Xylella fastidiosa Temecula1');
insert into tmp values ('Yersinia pestis Antiqua');
insert into tmp values ('Yersinia pestis biovar Mediaevails');
insert into tmp values ('Yersinia pestis CO92');
insert into tmp values ('Yersinia pestis KIM');
insert into tmp values ('Yersinia pestis Nepal516');
insert into tmp values ('Yersinia pseudotuberculosis IP32953');
insert into tmp values ('Zymomonas mobilis ZM4');
select name_txt, tax_id from tmp LEFT OUTER JOIN names USING (name_txt) where tax_id is null;
