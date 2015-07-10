(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )
   (  :import
      [toy_raytracer.core Sphere Surface]  )  )

(def dummy-point (->Point 1 2 3))

(def dummy-ray (->Point 4 5 6))

(def displacement-result (->Point -3 -3 -3))

(def red-surface (->Surface :red))

(def red-sphere (->Sphere red-surface 5 dummy-point))

(defn unity? [x] (zero? (dec x)))

(defn unityRad? [x] (unity? (square x)))

(deftest type-of-surface (is (= Surface (type red-surface))))

(deftest type-of-sphere (is (= Sphere (type red-sphere))))

(deftest displacement-test
   (is (= displacement-result (displacement (center red-sphere) dummy-ray)))  )

(deftest unit-vector-test (is (unityRad? (unit-vector displacement-result))))

(deftest normal-test (is (unityRad? (normal red-sphere dummy-ray))))
