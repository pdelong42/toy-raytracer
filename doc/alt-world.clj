(def world
   (for
      [  xx (range 5) yy (range 5) zz (range 5)  ]
      (defsphere
         40.0
         [  (+  -400.0 (* xx 200.0))
            (+  -400.0 (* yy 200.0))
            (+ -3200.0 (* zz 400.0))  ]
         [  (/ xx 4.0)
            (/ yy 4.0)
            (/ zz 4.0)  ]  )  )  )
