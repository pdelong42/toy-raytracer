(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )  )

(deftest type-of-surface
   (is
      (= toy_raytracer.core.Surface
         (type
            (toy_raytracer.core.Surface. :red))))  )

(deftest type-of-sphere
   (is (= toy_raytracer.core.Sphere (type (toy_raytracer.core.Sphere. (toy_raytracer.core.Surface. :red) 5 (toy_raytracer.core.Point. 1 2 3)))))  )
