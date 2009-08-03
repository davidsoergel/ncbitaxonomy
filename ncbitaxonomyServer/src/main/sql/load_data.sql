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

