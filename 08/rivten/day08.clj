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

