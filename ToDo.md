 - [ ] Fix some of the initial tests to use a real color value, rather
   than the placeholder :red value I was using.  Also, rename the test
   apprpriately.

 - [X] Finish laying-out the original scene from the book.

 - [X] Find out why x and y axes are transposed, and fix.

 - [X] Test in higher resolutions.

 - [ ] I currently mix floats and ints in my tests, rather
   arbitrarily.  I need to normalize this.

 - [ ] I still find first-hit to be rather ugly.  I'd like to abstract
   out bits of it, and make it easier to read.

 - [ ] Next I should implement a "plane" primitive.

 - [ ] Implement CSG (constructive solid geometry).  This probably
   entails converting the scene description into a hierarchy, rather
   than it continuing to be linear.  That means the logic needs to be
   updated to handle a hierarchy.

 - [ ] Implement a "cube" object (or *any* polyhedron, for that
   matter) using CSG of planes.

 - [ ] Add support for color (not just greyscale).

 - [ ] Parallelize this code.

 - [ ] Implement light sources that don't coincide with the
   eye/camera.
