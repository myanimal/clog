(ns clog.controller
  (:use clojure.string
        clog.templates
        ring.util.response))
 
(defn rps [a]
  "Play Rock, Paper, Scissors"
  (str "THE CONTENT: " a))
 
(defn chooseOponent []
  "Chose oponent for game"
  (def options ["rock" "paper" "scissors"])
  (nth options (rand-int (count options))))

(defn playGame [weapon oponent]
  "Play Rock, Paper, Scissors"
  (def weapon (lower-case weapon))
  (def oponent (lower-case oponent))
  (cond (= weapon oponent) "It's a Draw!"
    (and (= weapon "rock") (= oponent "scissors")) "You WIN! Rock beats scissors!"
    (and (= weapon "rock") (= oponent "paper")) "You Lose! Paper beats rock!"
    (and (= weapon "paper") (= oponent "rock")) "You WIN! Paper beats rock!"
    (and (= weapon "paper") (= oponent "scissors")) "You Lose! Scissors beat paper!"
    (and (= weapon "scissors") (= oponent "paper")) "You WIN! Scissors beat paper!"
    (and (= weapon "scissors") (= oponent "rock")) "You Lose! Paper beats rock!"
    :else "Impossible.."))


(defn index
  "Index page handler"
  [req]
  (->> (str "a") home-page response)) ;; Equivalent to  (response (home-page))

(defn play
  "Game page handler"
  [req weapon]
  (def oponent (chooseOponent))
  (->> (game-page weapon oponent (playGame weapon oponent)) response)) ;; Equivalent to  (response (home-page (select posts))
  ; (->> (weapon oponent (playGame weapon oponent)) game-page response)) ;; Equivalent to  (response (home-page (select posts))
