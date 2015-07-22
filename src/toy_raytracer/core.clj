(ns toy-raytracer.core
   (:require [clojure.string :refer [join]])
   (:import java.lang.Math)
   (:gen-class)  )

(defrecord Point [x y z])

(defprotocol PointProperties
   (x [_])
   (y [_])
   (z [_])  )

(extend Point PointProperties
   {  :x (fn [point] (:x point))
      :y (fn [point] (:y point))
      :z (fn [point] (:z point))  }  )

(defn toPoint [p]
   (if
      (= Point (type p))
      p
      (apply ->Point p)  )  )

(defn fromPoint [p]
   (if
      (= Point (type p))
      (vals (seq p))
      p  )  )

(defn inner
   [u v]
   (reduce +
      (map *
         (fromPoint u)
         (fromPoint v)  )  )  )

(defn square [x]
   (if
      (number? x)
      (* x x)
      (inner x x)  )  )

(defn magnitude [v] (Math/sqrt (square v)))

(defn unit-vector [v] (toPoint (map #(/ % (magnitude v)) (fromPoint v))))

(defn minroot
   [a b c]
   (if
      (zero? a)
      (/ (- c) b)
      (let
         [  disc (- (square b) (* 4 a c))  ]
         (if-not
            (neg? disc)
            (let
               [  discrt (Math/sqrt disc)  ]
               (min
                  (/ (+ (- b) discrt) (* 2 a))
                  (/ (- (- b) discrt) (* 2 a))  )  )  )  )  )  )

(defrecord Surface [color])

(defprotocol SurfaceProperties
   (to-surface [_])
   (color      [_])  )

(def surface-properties
   {  :to-surface (fn [surface]         surface)
      :color      (fn [surface] (:color surface))  }  )

(extend Surface SurfaceProperties surface-properties)

(defrecord Sphere [surface radius center])

(defprotocol SphereProperties
   (to-sphere [_])
   (radius    [_])
   (center    [_])  )

(def sphere-properties
   {  :to-sphere  (fn [sphere]          sphere)
      :radius     (fn [sphere] (:radius sphere))
      :center     (fn [sphere] (:center sphere))  }  )

(extend Sphere
   SurfaceProperties
   (merge surface-properties
      {  :to-surface (fn [sphere] (:surface sphere))
         :color      (fn [sphere] (color (to-surface sphere)))  }  )
   SphereProperties
   sphere-properties  )

(defn displacement
   [p1 p2]
   (toPoint
      [  (- (x p1) (x p2))
         (- (y p1) (y p2))
         (- (z p1) (z p2))  ]  )  )

(defn distance
   [p1 p2]
   (magnitude (displacement p1 p2))  )

(defmulti normal
   (fn [surface point] (type surface))  )

(defmethod normal Sphere
   [sphere point]
   (unit-vector (displacement (center sphere) point))  )

(defmulti intersect
   (fn [surface point ray] (type surface))  )

(defmethod intersect Sphere
   [sphere point ray]
   (let
      [  plumb (displacement point (center sphere))
         n (minroot
              (square ray)
              (* 2 (inner plumb ray))
              (- (square plumb) (square (radius sphere)))  )  ]
      (if
         (and n (not (neg? n)))
         (toPoint
            (map #(+ % (* n %2))
               (fromPoint point)
               (fromPoint ray)  )  )  )  )  )

(defn lambert
   [surface intersection ray]
   (max 0 (inner ray (normal surface intersection)))  )

; ToDo: in the function below, I have to remember to map the intersection
; points to their (scalar) distances, for subsequent sorting...

(defn first-hit
   [world point ray]
   (let
      [  shoot-object
            (fn
               [shots object]
               (let
                  [  shot (intersect object point ray)  ]
                  (assoc shots (if shot (distance shot point)) object)  )  )
         hits (reduce shoot-object {} world)
         distances (keys (dissoc hits nil))
         closest (first (sort distances))  ]
      [closest (hits closest)]  )  )

; Oops, I forgot that first-hit is expected to return a tuple of the
; surface, and the point of intersection with that surface (named
; "shot" in the above).  I need to work on fixing that, and the test
; based on it.


; Yeah, I opted for overwriting a member of the map if it has the same key.  I
; suppose I could've added it to an array of values for that key, but I
; would've had to decide which one to select later anyway.  This way is
; probably a bit arbitrary, so I should either a) find some other way to
; further choose between the outcomes, or b) sort on some criterion to make the
; outcome deterministic.

(defn sendray
   [world point ray]
   (if-let
      [  [s intersection]
         (first-hit world point ray)  ]
      (* (lambert s intersection ray)
         (color s)  )
      0  )  )

(def eye (->Point 0 0 200))

(defn color-at
   [world x y]
   (* 255.0
      (sendray world eye
         (unit-vector
            (displacement (->Point x y 0) eye)  )  )  )  )

(defn tracer

   "The original function printed to the file whose name is passed in as the
    first arg.  However, I can't remember the file I/O functions in
    Clojure/Java at the moment, so I'm just going to send the output to stdout
    for now.  Also, color-at isn't yet defined, so we're just printing
    coordinates as a sanity check until that changes."

   [world & {:keys [res] :or {res 1}}]
   (let
      [  delta (/ res)
         sideseq (range -50 (+ 50 delta) delta)
         sidelen (count sideseq)  ]
      (printf "P2 %d %d 255\n" sidelen sidelen)
      (println
         (join \space
            (for [x sideseq y sideseq]
               (color-at world x y)  )  )  )  )  )

(defn defsphere
   [color radius center]
   (->Sphere (->Surface color) radius (apply ->Point center))  )

(def world
   [  (defsphere 0.8 200 [  0 -300 -1200])
      (defsphere 0.7 200 [-80 -150 -1200])
      (defsphere 0.9 200 [ 70 -100 -1200])  ]  )

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (tracer world)  )
