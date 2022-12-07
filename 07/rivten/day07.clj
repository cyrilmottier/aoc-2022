(defn get-directory-size
  [sum-of-big-directory-size current-directory-size input]
  (cond 
    (empty? input) [sum-of-big-directory-size current-directory-size input]
    (re-find #"^\$ cd \.\." (first input)) 
    [(if 
       (< current-directory-size 100000) 
       (+ current-directory-size sum-of-big-directory-size) 
       sum-of-big-directory-size) 
     current-directory-size 
     (rest input)]

    (re-find #"^\$ cd" (first input)) 
    (let [[sum-of-big-directory-size in-directory-size input] 
          (get-directory-size sum-of-big-directory-size 0 (rest input))]
      (get-directory-size 
        sum-of-big-directory-size 
        (+ in-directory-size current-directory-size) 
        input))

    (re-find #"^\$ ls" (first input))
    (get-directory-size sum-of-big-directory-size current-directory-size (rest input))

    (re-find #"^dir" (first input)) 
    (get-directory-size sum-of-big-directory-size current-directory-size (rest input))

    (re-find #"^\d+ " (first input))
    (let [file-size (Integer/parseInt (first (clojure.string/split (first input) #" ")))]
      (get-directory-size sum-of-big-directory-size (+ file-size current-directory-size) (rest input)))

    :else ((do (tap> input) (assert false)))))
      
(get-directory-size 0 0 (clojure.string/split (slurp "input.txt") #"\n"))
