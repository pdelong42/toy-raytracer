(ns toy-raytracer.core
   (:require
      [clojure.string    :refer [join]]
      [clojure.tools.cli :refer [parse-opts]]  )
   (:import java.lang.Math)
   (:gen-class)  )

(defrecord Color [red green blue])

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
         [  disc (- (square b) (* 4.0 a c))  ]
         (if-not
            (neg? disc)
            (let
               [  discrt (Math/sqrt disc)  ]
               (min
                  (/ (+ (- b) discrt) (* 2.0 a))
                  (/ (- (- b) discrt) (* 2.0 a))  )  )  )  )  )  )

(defrecord Surface [color])

(defprotocol SurfaceProperties
   (to-surface [_])
   (color      [_])  )

(def surface-properties
   {  :to-surface (fn [surface]         surface)
      :color      (fn [surface] (:color surface))  }  )

(extend Surface SurfaceProperties surface-properties)

(defrecord Plane [surface normal magnitude])

(defprotocol PlaneProperties
   (to-plane [_])  )

(extend Plane
   SurfaceProperties
   {  :to-surface (fn [plane]          (:surface plane))
      :color      (fn [plane] (color (to-surface plane)))  }
   PlaneProperties
   {  :to-plane (fn [plane] plane)  }  )

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
      {  :to-surface (fn [sphere]          (:surface sphere))
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

(defmethod normal Plane
   [plane point]
   (unit-vector (toPoint (:normal plane))))

(defmethod normal Sphere
   [sphere point]
   (unit-vector (displacement (center sphere) point))  )

(defmulti intersect
   (fn [surface point ray] (type surface))  )

(defmethod intersect Plane
   [plane point ray]
   (let
      [  n (:normal plane)
         denom (inner ray n)  ]
      (if
         (not (zero? denom))
         (let
            [  d (/ (- (:magnitude plane) (inner point n)) denom)  ]
            (toPoint ; footnote 1
               (map #(+ % (* d %2))
                  (fromPoint point)
                  (fromPoint ray)  )  )  )  )  )  )

(defmethod intersect Sphere
   [sphere point ray]
   (let
      [  plumb (displacement point (center sphere))
         n (minroot
               (square ray)
               (* 2.0 (inner plumb ray))
               (- (square plumb) (square (radius sphere)))  )  ]
      (if
         (and n (not (neg? n)))
         (toPoint ; footnote 1
            (map #(+ % (* n %2))
               (fromPoint point)
               (fromPoint ray)  )  )  )  )  )

(defn lambert
   [surface intersection ray]
   (max 0 (inner ray (normal surface intersection)))  )

(defn first-hit
   [world point ray]
   (let
      [  shoot-object
            (fn
               [shots object]
               (let
                  [  shot (intersect object point ray)  ]
                  (assoc shots
                     (if shot (distance shot point))
                     [object shot]  )  )  )
         closest-hit #(% (-> % keys sort first))  ]
     (closest-hit (dissoc (reduce shoot-object {} world) nil))  )  )

; Yeah, I opted for overwriting a member of the map if it has the same key.  I
; suppose I could've added it to an array of values for that key, but I
; would've had to decide which one to select later anyway.  This way is
; probably a bit arbitrary, so I should either a) find some other way to
; further choose between the outcomes, or b) sort on some criterion to make the
; outcome deterministic.

(def eye (->Point 0.0 0.0 200.0))

(defn color-at
   [world x y]
   (let
      [  ray (unit-vector (displacement (->Point x y 0.0) eye))  ]
      (if-let
         [  [surface intersection] (first-hit world eye ray)  ]
         (apply ->Color
            (map
              #(int (* 255 % (lambert surface intersection ray)))
               (vals (color surface))  ))
         (->Color 0 0 0)  )  )  )

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
      (printf "P3 %d %d 255\n" sidelen sidelen)
      (println
         (join \space
            (for [y sideseq x sideseq]
               (join \space (vals (color-at world x y)))  )  )  )  )  )

(defn defsurface
   [color]
   (->Surface (apply ->Color color)))

(defn defplane
   [a b c d color]
   (apply ->Plane
      (defsurface color)
      (->Point a b c) d  )  )

(defn defsphere
   [radius center color]
   (->Sphere
      (defsurface color)
      radius
      (apply ->Point center)  )  )

(def world
   (concat
      [  (defsphere 200.0 [  0.0 -300.0 -1200.0] [0.8 0.0 0.0])
         (defsphere 200.0 [-80.0 -150.0 -1200.0] [0.0 0.7 0.0])
         (defsphere 200.0 [ 70.0 -100.0 -1200.0] [0.0 0.0 0.9])  ]
      (for
         [  xx (range -2 3) zz (range 2 8)  ]
         (defsphere
            40.0
            [(* xx 200.0) 300.0 (* zz -400.0)]
            [0.75 0.75 0.75]  )  )  )  )

(def cli-options
   [  [  "-m"
         "--multiplier INT"
         "resolution multiplier"
         :parse-fn #(Integer/parseInt %)
         :validate [integer? "not an integer"]
         :default 1           ]
      [  "-h" "--help" "help"  ]  ]  )

(defn usage
   [exit-code options-summary & [error-msg]]
   (if error-msg (println error-msg "\n"))
   (println
      (join \newline
         [  "Usage:"
            ""
            "   java -jar toy-raytracer-X.Y.Z-standalone.jar -options..."
            ""
            "Options: (with defaults indicated)"
            options-summary  ]  )  )
   (System/exit exit-code)  )

(defn main-loop
   [  {  {  :keys [help multiplier]  } :options
         :keys [arguments errors summary]  }  ]
   (if help   (usage 0 summary errors))
   (if errors (usage 1 summary errors))
   (tracer world :res multiplier))

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (main-loop (parse-opts args cli-options))  )

; Footnote 1:
;
; This needs to be abstracted out into its own function(s).
