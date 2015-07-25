toy-raytracer
=============

A learning exercise.

This is adapted from the raytracer code in Chapter 9 of "ANSI Common Lisp", by
Paul Graham.

For now, I'm just doing a rough porting job, from Common Lisp to Clojure, with
my only requirement being that it compiles.  I haven't completed enough of it
to validate whether the functionality does what it's supposed to.  I suppose I
should be writing unit tests as I go; but I won't lie, I've been lazy.

I've also been adding my own improvements to the code, where I think I can make
it clearer, less redundant, and take advantage of Clojure's abstractions.  Some
of this has made for clearer, more concise code, particuarly in the places
where I can use Clojure's built-in data structures and destructuring; other
places it's been a more rocky transition, such as adapting to Clojure's object
model (or lack thereof).

To run:

``
   $ lein run | pnmtopng > foo.png
``

(This assumes you've installed the netpbm utilities.)
