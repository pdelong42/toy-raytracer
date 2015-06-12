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
      (- (:x p1) (:x p2))
      (- (:y p1) (:y p2))
      (- (:z p1) (:z p2))  )  )

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

(defn -main
   [& args]
   ;; work around dangerous default behaviour in Clojure
   (alter-var-root #'*read-eval* (constantly false))
   (println "Hello, World!"))
