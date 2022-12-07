(defn get-directory-size
  [directory-sizes current-directory-size input]
  (cond 
    (empty? input) 
    [(conj directory-sizes current-directory-size) current-directory-size input]

    (re-find #"^\$ cd \.\." (first input)) 
    [(conj directory-sizes current-directory-size) 
     current-directory-size 
     (rest input)]

    (re-find #"^\$ cd" (first input)) 
    (let [[directory-sizes in-directory-size input] 
          (get-directory-size directory-sizes 0 (rest input))]
      (get-directory-size 
        directory-sizes 
        (+ in-directory-size current-directory-size) 
        input))

    (re-find #"^\d+ " (first input))
    (let [file-size (Integer/parseInt (first (clojure.string/split (first input) #" ")))]
      (get-directory-size directory-sizes (+ file-size current-directory-size) (rest input)))

    :else ((do (tap> input) (assert false)))))

(def cleaned-input 
  (as-> (slurp "input.txt") $
    (clojure.string/split $ #"\n")
    (remove (partial re-find #"^(dir|\$ ls)") $)))

; part 1
(->> cleaned-input
  (get-directory-size [] 0)
  (first)
  (filter (partial > 100000))
  (reduce +))

; part 2
(let [sizes (first (get-directory-size [] 0 cleaned-input)) 
      space-to-free (- 30000000 (- 70000000 (last sizes)))]
  (->> sizes 
       (filter (partial < space-to-free)) 
       (apply min)))
