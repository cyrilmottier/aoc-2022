(defn string-to-rps [s]
  (condp = s
    "A" :rock
    "B" :paper
    "C" :scissor
    "X" :rock
    "Y" :paper
    "Z" :scissor))
    
(defn score [other_player me]
  (let [rps-score {:rock 1, :paper 2, :scissor 3} 
        beats {:rock :scissor, :paper :rock, :scissor :paper}]
    (+ (rps-score me)
      (cond 
        (= other_player me) 3
        (= (beats other_player) me) 0
        (= other_player (beats me)) 6))))
 


(defn parse-line [line]
  (map string-to-rps (clojure.string/split line #" ")))

(defn get-score [line]
  (apply score (parse-line line)))

(as-> (slurp "input.txt") $
    (clojure.string/split $ #"\n")
    (reduce + (map get-score $)))
