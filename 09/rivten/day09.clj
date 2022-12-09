(def moves
  (as-> (slurp "input.txt") $
    (clojure.string/split $ #"\n")
    (map #(clojure.string/split % #" ") $)
    (map #(vector (first %) (Integer. (second %))) $)))

(defn do-head-move [dir [hx hy]]
  (condp = dir
    "R" [(inc hx) hy]
    "L" [(dec hx) hy]
    "U" [hx (inc hy)]
    "D" [hx (dec hy)]
    (assert false)))

(defn sign [x]
  (cond
    (= 0 x) 0
    (< 0 x) 1
    (> 0 x) -1))

(defn offset [d]
  (* (sign d) (min 1 (Math/abs d))))

(defn do-tail-move [[hx hy] [tx ty]]
  (let [[dx dy]
        (map - [hx hy] [tx ty])]
    (if (= 1 (max (Math/abs dx) (Math/abs dy)))
      [tx ty]
      [(+ tx (offset dx))
       (+ ty (offset dy))])))

(defn do-move
  ([dir h t]
   (let [h (do-head-move dir h)
         t (do-tail-move h t)]
     [h t]))
  ([dir n h t]
   (if (= 0 n)
     [[h t]]
     (let [[h2 t2] (do-move dir h t)]
       (conj (do-move dir (dec n) h2 t2) [h t])))))

(defn moves-reduce [[h t tposes] move]
  (let [moves (do-move (first move) (second move) h t)
        [h t] (first moves)
        tposes (into #{} (concat tposes (map second moves)))]
    [h t tposes]))

(count ((reduce moves-reduce [[0 0] [0 0] #{}] moves) 2))
