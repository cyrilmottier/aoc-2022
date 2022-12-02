(defn string-to-rps [s]
  (condp = s
    "A" :rock
    "B" :paper
    "C" :scissor
    "X" :rock
    "Y" :paper
    "Z" :scissor))

(def beats {:rock :scissor, :paper :rock, :scissor :paper})
 
(defn score [other-player me]
  (let [rps-score {:rock 1, :paper 2, :scissor 3}] 
    (+ (rps-score me)
      (cond 
        (= other-player me) 3
        (= (beats other-player) me) 0
        (= other-player (beats me)) 6))))
 

(defn parse-line [line]
  (map string-to-rps (clojure.string/split line #" ")))

(defn get-score [line]
  (apply score (parse-line line)))

(defn get-my-move [other-player result]
  (let [is-beatten-by {:rock :paper, :paper :scissor, :scissor :rock}] 
    (condp = result
      :lose (beats other-player)
      :draw other-player
      :win (is-beatten-by other-player))))

(defn string-to-result [s]
  (condp = s 
    "X" :lose
    "Y" :draw
    "Z" :win))

(defn get-score-2 [line]
  (let [[other-player result] (clojure.string/split line #" ")]
    (score (string-to-rps other-player) 
           (get-my-move 
             (string-to-rps other-player) 
             (string-to-result result)))))

(map get-score-2 (clojure.string/split (slurp "input.txt") #"\n"))

(as-> (slurp "input.txt") $
    (clojure.string/split $ #"\n")
    (vector (reduce + (map get-score $)) 
      (reduce + (map get-score-2 $))))
