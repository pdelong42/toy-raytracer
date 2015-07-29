(ns toy_raytracer.core-test
   (  :require
      [clojure.test       :refer :all]
      [toy-raytracer.core :refer :all]  )
   (  :import
      [toy_raytracer.core Sphere Surface]  )  )

(def dummy-point (->Point 1.0 2.0 3.0))

(def dummy-ray (->Point 4.0 5.0 6.0))

(def displacement-result (->Point -3.0 -3.0 -3.0))

(def red-surface (->Surface (apply ->Color [1.0 0.0 0.0])))

(def red-sphere (->Sphere red-surface 5.0 dummy-point))

(defn unity? [x] (zero? (dec x)))

(defn unityRad? [x] (unity? (square x)))

(deftest type-of-surface (is (= Surface (type red-surface))))

(deftest type-of-sphere (is (= Sphere (type red-sphere))))

(deftest displacement-test
   (is (= displacement-result (displacement (center red-sphere) dummy-ray)))  )

(deftest unit-vector-test (is (unityRad? (unit-vector displacement-result))))

(deftest normal-test (is (unityRad? (normal red-sphere dummy-ray))))

(deftest magnitude-test (is (= 7.0 (magnitude (->Point 2.0 3.0 6.0)))))

(deftest distance-test (is (= 27.0 (square (distance dummy-point dummy-ray)))))

; this started to return an actual meaningful result;
; so, that's cool, but I needed a more representative test anyway;
;(deftest intersect-test
;   (is (nil? (intersect red-sphere dummy-point dummy-ray)))  )

(def pixel (->Point 0.0 0.0 0.0))

(def sphere-back   (defsphere 200.0 [    0.0     0.0 -1200.0] [1.0 1.0 1.0]))
(def sphere-front  (defsphere 200.0 [    0.0     0.0  1200.0] [1.0 1.0 1.0]))
(def sphere-bottom (defsphere 200.0 [    0.0 -1200.0     0.0] [1.0 1.0 1.0]))
(def sphere-top    (defsphere 200.0 [    0.0  1200.0     0.0] [1.0 1.0 1.0]))
(def sphere-left   (defsphere 200.0 [-1200.0     0.0     0.0] [1.0 1.0 1.0]))
(def sphere-right  (defsphere 200.0 [ 1200.0     0.0     0.0] [1.0 1.0 1.0]))

(def dummy-world
   [  sphere-front
      sphere-back
      sphere-top
      sphere-bottom
      sphere-left
      sphere-right  ]  )

(def test-eye1 (->Point 0.0 0.0 -200.0))

(def test-eye2 (->Point 0.0 0.0  200.0))

(def test-eye3 (->Point -200.0 -200.0 -200.0))

(def test-ray1 (unit-vector (displacement pixel test-eye1)))

(def test-ray2 (unit-vector (displacement pixel test-eye2)))

(def test-ray3 (unit-vector (displacement pixel test-eye3)))

(def intersect-answer1 (->Point 0.0 0.0  1000.0))

(def intersect-answer2 (->Point 0.0 0.0 -1000.0))

(deftest intersect-test1
   "Camera aft of screen plane, looking at a sphere fore of sceen plane"
   (is (= intersect-answer1 (intersect sphere-front test-eye1 test-ray1)))  )

(deftest intersect-test2
   "In this case, the sphere shouldn't even be visible from the camera"
   (is (nil? (intersect sphere-back test-eye1 test-ray1)))  )

(deftest intersect-test3
   "In this case, the sphere shouldn't even be visible from the camera"
   (is (nil? (intersect sphere-front test-eye2 test-ray2)))  )

(deftest intersect-test4
   "Camera fore of screen plane, looking at a sphere aft of sceen plane"
   (is (= intersect-answer2 (intersect sphere-back test-eye2 test-ray2)))  )

(deftest intersect-test5
   "Camera fore of screen plane, looking at a sphere aft of sceen plane"
   (is (nil? (intersect sphere-front test-eye3 test-ray3)))  )

(def first-hit-answer1 [sphere-front intersect-answer1])

(def first-hit-answer2 [sphere-back intersect-answer2])

(deftest first-hit-test1
   (is (= first-hit-answer1 (first-hit dummy-world test-eye1 test-ray1))))

(deftest first-hit-test2
   (is (= first-hit-answer2 (first-hit dummy-world test-eye2 test-ray2))))

(deftest first-hit-test3
   "Camera fore of screen plane, looking at a sphere aft of sceen plane"
   (is (nil? (first-hit dummy-world test-eye3 test-ray3)))  )
