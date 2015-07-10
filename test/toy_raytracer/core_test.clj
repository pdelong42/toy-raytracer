(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )
   (  :import
      [toy_raytracer.core Sphere Surface]  )  )

(def red-surface (->Surface :red))

(def dummy-point (->Point 1 2 3))

(deftest type-of-surface (is (= Surface (type red-surface))))

(deftest type-of-sphere
   (is (= Sphere (type (->Sphere red-surface 5 dummy-point))))  )

;toy-raytracer.core=> (normal (Sphere. (Surface. :red) 5 (Point. 1 2 3)) (Point. 4 5 6))
;(-0.5773502691896257 -0.5773502691896257 -0.5773502691896257)
