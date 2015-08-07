 - [ ] I still find first-hit to be rather ugly.  I'd like to abstract
   out bits of it, and make it easier to read.

 - [ ] Next I should implement a "plane" primitive.

 - [ ] Implement CSG (constructive solid geometry).  This probably
   entails converting the scene description into a hierarchy, rather
   than it continuing to be linear.  That means the logic needs to be
   updated to handle a hierarchy.

 - [ ] Implement a "cube" object (or *any* polyhedron, for that
   matter) using CSG of planes.

 - [ ] Parallelize this code.

 - [ ] Implement light sources that don't coincide with the
   eye/camera (so we can have cool stuff like shadows).

 - [ ] Optimize the numeric code to include hinting and to avoid as
   much boxing as possible.

 - [ ] Implement transformations (e.g., rotations, translations,
   scaling) which can be applied to groups of objects in the scene.
   Again, this requires the scene description to be a hierarchical
   graph.

 - [ ] add provisions for velocity coordinates, and relativistic
   effects (this would fall under the pie-in-the-sky category of
   feature additions, but it would be nice to finally do this)

 - [ ] add support for texture maps (they can be a property of the
   base "surface" type, like color is)

 - [ ] put the functions into a separate namespace than the scene
   description

 - [ ] support output resolution / dimensions as direct
   width-by-height args, rather than this business with a multiplier
   and a square or fixed shape

 - [ ] implement a primitive for polygon/wireframe mesh (?)

 - [X] Finish laying-out the original scene from the book.

 - [X] Find out why x and y axes are transposed, and fix.

 - [X] Test in higher resolutions.

 - [X] Add support for color (not just greyscale) and output in the
   appropriate format.

 - [X] I currently mix floats and ints in my tests, rather
   arbitrarily.  I need to normalize this.

 - [X] Fix some of the initial tests to use a real color value, rather
   than the placeholder :red value I was using.  Also, rename the test
   appropriately.

 - [X] add a command-line interface for passing options (particularly
   for passing resolution arguments)
