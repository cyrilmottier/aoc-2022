(def input (clojure.string/trim (slurp "input.txt")))

(defn four-different? [l]
  (->> l
      (take 4)
      (map second)
      (set)
      (count)
      (= 4)))

(loop [elems (map vector (range) input)]
  (if (four-different? elems)
    (println (first elems))
    (recur (rest elems))))
      
    
  
