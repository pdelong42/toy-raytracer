#!/usr/bin/perl

use strict;
use warnings;

foreach( readline STDIN ) {
   next unless m/^\s*\|\s*([^|]*toy_raytracer[^|]*)\s*\|[^|]*?\s*(\d+)\s*\%/;
   printf "%02d %s\n", $2, $1;
}

=pod

This is a single-purpose script written to summarize the output
exported by YourKit.  It is looking through the method list, only
printing results for toy_raytracer (ignoring all the Clojure-specific
calls), and printing only the percent of time spent in each method
invocation.

I'm hoping to use the data from this to demonstrate measurable
performance gains from using type-hinting, or an equivalent library.

Example:

   ./foo.pl < Live-method-list.txt > before.txt
   [...fix the code...]
   ./foo.pl < Live-method-list.txt > after.txt

=cut
