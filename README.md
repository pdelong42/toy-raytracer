toy-raytracer
=============

A learning exercise.

This is adapted from the raytracer code in Chapter 9 of "ANSI Common
Lisp", by Paul Graham.

I've been adding my own improvements to the code, where I think I can
make it clearer, less redundant, and take advantage of Clojure's
abstractions.  Some of this has made for clearer, more concise code,
particuarly in the places where I can use Clojure's built-in data
structures and destructuring; other places it's been a more rocky
transition, such as adapting to Clojure's object model (my limited
understanding of it, at-least).

To run:

``
   $ lein run | pnmtopng > foo.png
``

(This assumes you've installed the netpbm utilities.)
