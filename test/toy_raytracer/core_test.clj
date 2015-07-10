(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )
   (  :import
      [toy_raytracer.core Surface]  )  )

(deftest type-of-surface
   (is
      (= Surface
         (type
            (toy_raytracer.core.Surface. :red))))  )

(deftest type-of-sphere
   (is
      (= toy_raytracer.core.Sphere
         (type
            (toy_raytracer.core.Sphere.
               (toy_raytracer.core.Surface. :red)
               5
               (toy_raytracer.core.Point. 1 2 3)  )  )  )  )  )

;toy-raytracer.core=> (normal (Sphere. (Surface. :red) 5 (Point. 1 2 3)) (Point. 4 5 6))
;(-0.5773502691896257 -0.5773502691896257 -0.5773502691896257)
