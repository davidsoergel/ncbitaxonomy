#!/usr/bin/perl -w

use strict;

opendir(DIR, ".");
my @subdirs = readdir(DIR);
closedir(DIR);

foreach my $subdir (@subdirs)
    {
	if($subdir eq "." || $subdir eq "..")
	    {
		next;
	    }

	opendir(DIR, "./$subdir");
	my @files = readdir(DIR);
	closedir(DIR);
	#print "$subdir @files\n";

	if(@files < 3)
		{
		print "No file in directory: $subdir\n";
		next;
		}

    my $subdirSpaces = $subdir;
    $subdirSpaces =~ s/_/ /g;

    # sometimes the fasta headers in a directory contain multiple species names, due to named plasmids or whatever.
    # we just pick the one that appears most often, or first, or that matches the directory name

    my %realNameCounts = ();
    foreach my $file (@files)
        {
    	my $fastaName = "./$subdir/$file";
    	#print "$fastaName\n";
    	open(FASTA, $fastaName) or next;
    	my $fastaHeader = <FASTA>;
    	if(!defined $fastaHeader)
    		{ next; }
    	#print "$fastaHeader\n";
    	close(FASTA);

	    # >gi|52783855|ref|NC_006322.1| Bacillus licheniformis ATCC 14580, complete genome

    	$fastaHeader =~ /.*\|\s*(.*?)\s*(chromosome|plasmid|,)/;
	    my $realName = $1;

	    $realNameCounts{$realName}++;
        }

    my $bestCount = 0;
    my $bestName;
    foreach my $realName (keys(%realNameCounts))
        {
        if($realNameCounts{$realName} > $bestCount)
            {
            $bestCount = $realNameCounts{$realName};
            $bestName = $realName;
            }
        if($realName eq $subdirSpaces)
            {
            $bestName = $realName;
            last;
            }
        }

    if($bestName ne $subdirSpaces && keys(%realNameCounts) > 1)
        {
        print STDERR "==========\n";
        print STDERR "$subdir     $subdirSpaces\n";
        foreach my $realName (keys(%realNameCounts))
            {
            print STDERR "    $realNameCounts{$realName}   $realName\n";
            }
        print STDERR ">>>> $bestName\n";
    }

	#print "$bestName\n";
	# $realName =~ s/ /_/g;  # don't do this; the names may already have underscores in them which are hereby obscured


	if($subdir ne $bestName)
		{
		#print "$bestName\n";
		if(! rename $subdir, $bestName)
			{
			if(rename $subdir, $subdirSpaces)
			    {
			    $subdir = $subdirSpaces;
			    }
		    else
			    {
			    print STDERR "Warning: could not rename $subdir to $subdirSpaces\n";
                }


			my $newName = $bestName;
			# $newName =~ s/\//\\\//g;
			$newName =~ s/\//_/g;  # okay, slashes turn into underscores
			if(rename $subdir, $newName)
				{
				print STDERR "renamed $subdir to $newName\n";
				# $bestName =~ s/_/ /g;
				# $newName =~ s/_/ /g;
				print ("insert into names (tax_id, name_txt, name_class) select tax_id, '$newName', 'synonym' from names where name_txt = '$bestName';\n");
				}
			else
				{
			    print STDERR "Warning: could not rename $subdir to $newName\n";
				#print "FAILED: $subdir -> $realName\n";
				# $subdir =~ s/_/ /g;
				# $realName =~ s/_/ /g;
				print ("insert into names (tax_id, name_txt, name_class) select tax_id, '$subdir', 'synonym' from names where name_txt = '$bestName';\n");
				}
			}
		}

	}


