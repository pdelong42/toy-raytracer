(ns toy-raytracer.core
   (:import java.lang.Math)
   (:gen-class)  )

(defn square [x] (* x x))

(defn mag [v] (Math/sqrt (reduce + (map square v))))

(defn unit-vector [v] (map #(/ % (mag v)) v))

(defrecord Point [x y z])

(defn distance
   [p1 p2]
   (mag
      [  (- (:x p1) (:x p2))
         (- (:y p1) (:y p2))
         (- (:z p1) (:z p2))  ]  )  )

(defn minroot
   [a b c]
   (if
      (zero? a)
      (/ (- c) b)
      (let
         [  disc (- (square b) (* 4 a c))  ]
         (if-not
            (> disc 0)
            (let
               [  discrt (Math/sqrt disc)  ]
               (min
                  (/ (+ (- b) discrt) (* 2 a))
                  (/ (- (- b) discrt) (* 2 a))  )  )  )  )  )  )

(defrecord Surface [color])

(def ^:dynamic *world* nil)

(def eye (Point. 0 0 200))

(defn tracer

   "The original function printed to the file whose name is passed in as the
    first arg.  However, I can't remember the file I/O functions in
    Clojure/Java at the moment, so I'm just going to send the output to stdout
    for now.  Also, color-at isn't yet defined, so we're just printing
    coordinates as a sanity check until that changes."

   [pathname & {:keys [res] :or {res 1}}]
   (printf "P2 %.0f %.0f 255\n" (* res 100) (* res 100))
   (let
      [  delta (/ res)
         side (range -50 50 delta)  ]
      (doseq [x side y side]
         ;(print (color-at x y))  )  )  )
         (println [x y])  )  )  )

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (println "Hello, World!"))
