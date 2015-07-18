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

(deftest magnitude-test (is (= 7.0 (magnitude (->Point 2 3 6)))))

(deftest distance-test (is (= 27.0 (square (distance dummy-point dummy-ray)))))

; this started to return an actual meaningful result;
; so, that's cool, but I needed a more representative test anyway;
;(deftest intersect-test
;   (is (nil? (intersect red-sphere dummy-point dummy-ray)))  )

(def pixel (->Point 0 0 0))

(def basic-sphere (defsphere 1 200 [0 0 1200]))

(def intersect-answer (->Point 0.0 0.0 1000.0))

(deftest intersect-test
   (is (= intersect-answer (intersect basic-sphere pixel eye)))  )

(def dummy-world
   [  (defsphere 1.0 200 [    0     0 -1200])
      (defsphere 1.0 200 [    0     0  1200])
      (defsphere 1.0 200 [    0 -1200     0])
      (defsphere 1.0 200 [    0  1200     0])
      (defsphere 1.0 200 [-1200     0     0])
      (defsphere 1.0 200 [ 1200     0     0])  ]  )

(def first-hit-answer (defsphere 1.0 200 [0 0 1200]))

(deftest first-hit-test
   (is (= first-hit-answer (first-hit dummy-world pixel eye))))
