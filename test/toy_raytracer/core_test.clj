(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )
   (  :import
      [toy_raytracer.core Sphere Surface]  )  )

(deftest type-of-surface (is (= Surface (type (->Surface :red)))))

(deftest type-of-sphere
   (is
      (= Sphere
         (type
            (->Sphere
               (->Surface :red)
               5
               (->Point 1 2 3)  )  )  )  )  )

;toy-raytracer.core=> (normal (Sphere. (Surface. :red) 5 (Point. 1 2 3)) (Point. 4 5 6))
;(-0.5773502691896257 -0.5773502691896257 -0.5773502691896257)
