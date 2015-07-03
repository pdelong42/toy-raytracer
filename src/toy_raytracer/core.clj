(ns toy-raytracer.core
   (:import java.lang.Math)
   (:gen-class)  )

(defn inner [u v] (reduce + (map * u v)))

(defn magnitude [v] (Math/sqrt (inner v v)))

(defn unit-vector [v] (map #(/ % (magnitude v)) v))

(defn minroot
   [a b c]
   (if
      (zero? a)
      (/ (- c) b)
      (let
         [  disc (- (* b b) (* 4 a c))  ]
         (if-not
            (> disc 0)
            (let
               [  discrt (Math/sqrt disc)  ]
               (min
                  (/ (+ (- b) discrt) (* 2 a))
                  (/ (- (- b) discrt) (* 2 a))  )  )  )  )  )  )

(defrecord Point [x y z])

(defprotocol PointProperties
   (x [_])
   (y [_])
   (z [_])  )

(extend Point PointProperties
   {  :x (fn [point] (:x point))
      :y (fn [point] (:y point))
      :z (fn [point] (:z point))  }  )

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
   [  (- (x p1) (x p2))
      (- (y p1) (y p2))
      (- (z p1) (z p2))  ]  )

(defn distance
   [p1 p2]
   (magnitude (displacement p1 p2))  )

(defmulti normal
   (fn [surface point] (type surface))  )

(defmethod normal Sphere
   [sphere point]
   (unit-vector (displacement (center sphere) point))  )

(defmulti intersect
   (fn [surface point xr yr zr] (type surface))  )

(def eye (Point. 0 0 200))

(defn tracer

   "The original function printed to the file whose name is passed in as the
    first arg.  However, I can't remember the file I/O functions in
    Clojure/Java at the moment, so I'm just going to send the output to stdout
    for now.  Also, color-at isn't yet defined, so we're just printing
    coordinates as a sanity check until that changes."

   [pathname & {:keys [res] :or {res 1}}]
   (let
      [  delta (/ res)
         sideseq (range -50 (+ 50 delta) delta)
         sidelen (count sideseq)  ]
      (printf "P2 %d %d 255\n" sidelen sidelen)
      (for [x sideseq y sideseq]
         ;(color-at x y)  )  )  )
         [x y]  )  )  )

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (dorun (map println (tracer nil)))  )
