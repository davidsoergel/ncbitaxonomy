#!/usr/bin/perl -w

opendir(DIR, ".");
@subdirs = readdir(DIR);
closedir(DIR);

foreach $subdir (@subdirs) {
	opendir(DIR, "./$subdir");
	@files = readdir(DIR);
	closedir(DIR);
	#print "$subdir @files\n";

	if(@files < 3)
		{
		print "No file in directory: $subdir\n";
		next;
		}

	$fastaName = "./$subdir/$files[2]";
	#print "$fastaName\n";
	open(FASTA, $fastaName) or next;
	$fastaHeader = <FASTA>; 
	if(!defined $fastaHeader)
		{ next; }
	#print "$fastaHeader\n";
	close(FASTA);

	$fastaHeader =~ /.*\|\s*(.*?)\s*(chromosome|plasmid|,)/;
	$realName = $1;
	$realName =~ s/ /_/g;
	if($subdir ne $realName)
		{
		if(! rename $subdir, $realName)
			{
			$newName = $realName;
			$newName =~ s/\//_/g;
			if(rename $subdir, $newName)
				{
				$realName =~ s/_/ /g;
				$newName =~ s/_/ /g;
				print ("insert into names (tax_id, name_txt, name_class) select tax_id, '$newName', 'synonym' from names where name_txt = '$realName';\n");
				}
			else
				{
				#print "FAILED: $subdir -> $realName\n";
				$subdir =~ s/_/ /g;
				$realName =~ s/_/ /g;
				print ("insert into names (tax_id, name_txt, name_class) select tax_id, '$subdir', 'synonym' from names where name_txt = '$realName';\n");			
				}
			}
		}

	}


