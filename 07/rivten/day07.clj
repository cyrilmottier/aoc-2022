(defn get-directory-size
  [directory-sizes current-directory-size input]
  (cond 
    (empty? input) [(conj directory-sizes current-directory-size) current-directory-size input]
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

    (re-find #"^\$ ls" (first input))
    (get-directory-size directory-sizes current-directory-size (rest input))

    (re-find #"^dir" (first input)) 
    (get-directory-size directory-sizes current-directory-size (rest input))

    (re-find #"^\d+ " (first input))
    (let [file-size (Integer/parseInt (first (clojure.string/split (first input) #" ")))]
      (get-directory-size directory-sizes (+ file-size current-directory-size) (rest input)))

    :else ((do (tap> input) (assert false)))))
      
(reduce + (filter #(< % 100000) (first (get-directory-size [] 0 (clojure.string/split (slurp "input.txt") #"\n")))))

(let [sizes (first (get-directory-size [] 0 (clojure.string/split (slurp "input.txt") #"\n")))]
  (let [space-to-free (- 30000000 (- 70000000 (apply max sizes)))]
    (apply min (filter #(< space-to-free %) sizes))))
