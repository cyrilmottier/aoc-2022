(def forest
  (as-> (slurp "input.txt") $
    (clojure.string/split $ #"\n")
    (apply map vector $)
    (mapv (partial mapv #(Integer. (str %))) $)))

(defn transpose [forest]
  (apply mapv vector forest))

(defn get-left-visibility [forest transposed-forest row col]
  (subvec (forest row) 0 col))

(defn get-right-visibility [forest transposed-forest row col]
  (subvec (forest row) (+ 1 col)))

(defn get-top-visibility [forest transposed-forest row col]
  (subvec (transposed-forest col) 0 row))

(defn get-bottom-visibility [forest transposed-forest row col]
  (subvec (transposed-forest col) (+ 1 row)))

(defn is-visible?
  ([forest transposed-forest row col]
   (some #(is-visible? forest transposed-forest row col %) [get-left-visibility get-right-visibility get-top-visibility get-bottom-visibility]))
  ([forest transposed-forest row col fn-visibility]
   (every? #(< % ((forest row) col)) (fn-visibility forest transposed-forest row col))))

(->> (let [row-count (count forest)
           col-count (count (forest 0))
           transposed-forest (transpose forest)]
       (for [row (range row-count)
             col (range col-count)]
         (is-visible? forest transposed-forest row col)))
     (filter identity)
     (count))

; part 2
(defn get-generic-score [tree-height visibility]
  (let [[taken remaining] (split-with #(< % tree-height) visibility)]
    (if (not-empty remaining)
      (+ 1 (count taken))
      (count taken))))

(defn left-score [forest transposed-forest row col]
  (let [tree-height ((forest row) col)]
    (get-generic-score tree-height (reverse (get-left-visibility forest transposed-forest row col)))))

(defn right-score [forest transposed-forest row col]
  (let [tree-height ((forest row) col)]
    (get-generic-score tree-height (get-right-visibility forest transposed-forest row col))))

(defn top-score [forest transposed-forest row col]
  (let [tree-height ((forest row) col)]
    (get-generic-score tree-height (reverse (get-top-visibility forest transposed-forest row col)))))

(defn bottom-score [forest transposed-forest row col]
  (let [tree-height ((forest row) col)]
    (get-generic-score tree-height (get-bottom-visibility forest transposed-forest row col))))

(defn scenic-score [forest transposed-forest row col]
  (apply * (map #(% forest transposed-forest row col) [top-score bottom-score left-score right-score])))

(->> (let [row-count (count forest)
           col-count (count (forest 0))
           transposed-forest (transpose forest)]
       (for [row (range row-count)
             col (range col-count)]
         (scenic-score forest transposed-forest row col)))
     (reduce max))
