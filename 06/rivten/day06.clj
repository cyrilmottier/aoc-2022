(def input (clojure.string/trim (slurp "input.txt")))

(defn four-different? [l]
  (->> l
      (take 4)
      (map second)
      (set)
      (count)
      (= 4)))

(defn fourteen-different? [l]
  (->> l
      (take 14)
      (map second)
      (set)
      (count)
      (= 14)))

(loop [elems (map vector (range) input)]
  (if (four-different? elems)
    (println (->> (first elems) (first) (+ 4)))
    (recur (rest elems))))

(loop [elems (map vector (range) input)]
  (if (fourteen-different? elems)
    (println (->> (first elems) (first) (+ 14)))
    (recur (rest elems))))
      
  
