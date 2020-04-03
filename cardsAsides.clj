(ns class452.core)

;;make a deck as a list of (52) cards. Each card is a [[suite card-face] num-value]
;(defn cards []
;  (into {} ;;add to empty hash map
;        (for [sui suites
;              fa faces
;              va vals]
;          ;(print va))
;          [ [sui fa] va ]) ;;key-value pairs in vectors (so can use shuffle) --> key = card suite and face-value, value=numeric rank
;        ;; make each card --> a suite and face combination
;        )
;  )

;;;; ASIDE: (assoc)
(assoc {} :key1 "value" :key2 "another value")
;;=> {:key2 "another value", :key1 "value"}
(assoc {:key1 "old value1" :key2 "value2"};; Here we see an overwrite by a second entry with the same key
  :key1 "value1" :key3 "value3")
;;=> {:key3 "value3", :key2 "value2", :key1 "value1"}
(assoc nil :key1 4) ;; We see a nil being treated as an empty map
;;=> {:key1 4}
; 'assoc' can be used on a vector (but not a list), in this way:
;; (assoc vec index replacement)
(assoc [1 2 3] 0 10)     ;;=> [10 2 3]
(assoc [1 2 3] 2 '(4 6)) ;;=> [1 2 (4 6)]
;; Next, 0 < index <= n, so the following will work
(assoc [1 2 3] 3 10)     ;;=> [1 2 3 10]
;; However, when index > n, an error is thrown
(assoc [1 2 3] 4 10)
;; java.lang.IndexOutOfBoundsException (NO_SOURCE_FILE:0)

;; order is value and suite
(def suites [:club :diamond :heart :spade])
(def ranks [1 2 3 4 5 6 7 8 9 10 11 12 13])
;(def vals (into [] (flatten (replicate 4 ranks))))
;(into [] (sort (flatten (replicate 4 ranks))))


(get [1 2 3] 1)
;;=> 2

(get [1 2 3] 0) ;=> 1

(get [1 2 3] 5)
;;=> nil

(get [1 2 3] 5 100)
;;=> 100

(get {:a 1 :b 2} :b)
;;=> 2

(get {:a 1 :b 2} :z "missing")
;;=> "missing"



;;;; https://www.tutorialspoint.com/clojure/clojure_strings_join.htm
;; (ns clojure.examples.hello (:gen-class))
(defn hello-world []
  (println (clojure.string/join ", " [1 2 3])))
(hello-world)


;;;; https://clojuredocs.org/clojure.core/str
;; Using str in maps
(def user-map {:fname "Jane" :sname "Doe"})
;;getting the value of the first key
(:fname user-map)
;=> "Jane"
(str (:fname user-map) " " (:sname user-map))
;=> "Jane Doe"