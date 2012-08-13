(ns clog.controller
  (:use clog.templates
        ring.util.response)
  (:require [clojure.string :as string]
            [taoensso.carmine :as r]))
 
;;; Define Redis connection
(def pool (r/make-conn-pool :max-active 8))
(def spec-server1 (r/make-conn-spec :host     "127.0.0.1"
                                    :port     6379
                                    :db       3
                                    :timeout  4000))
(defmacro redis
  "Acts like (partial with-conn pool spec-server1)."
  [& body] `(r/with-conn pool spec-server1 ~@body))

;;; TODO: get user id from session
(def userId "m24032")


(defn chooseOponent []
  "Chose oponent for game"
  (def options ["rock" "paper" "scissors"])
  (nth options (rand-int (count options))))

(defn playGame [weapon oponent]
  "Play Rock, Paper, Scissors"
  (def weapon (string/lower-case weapon))
  (def oponent (string/lower-case oponent))
  (def output (cond (= weapon oponent) { "result" 0, "message" "It's a Draw!" }
    (and (= weapon "rock")      (= oponent "scissors"))   { "result"  1, "message" "You WIN! Rock beats scissors!" }
    (and (= weapon "rock")      (= oponent "paper"))      { "result" -1, "message" "You Lose! Paper beats rock!" } 
    (and (= weapon "paper")     (= oponent "rock"))       { "result"  1, "message" "You WIN! Paper beats rock!" }
    (and (= weapon "paper")     (= oponent "scissors"))   { "result" -1, "message" "You Lose! Scissors beat paper!" }
    (and (= weapon "scissors")  (= oponent "paper"))      { "result"  1, "message" "You WIN! Scissors beat paper!" }
    (and (= weapon "scissors")  (= oponent "rock"))       { "result" -1, "message" "You Lose! Paper beats rock!" }
    :else { "result" 0, "message" "Impossible.." }))
  (assoc output "score" (str (redis
    (r/zincrby "leaderboard" (str (output "result")) userId)))))

(defn index
  "Index page handler"
  [req]
  (->> home-page response)) ;; Equivalent to  (response (home-page))

(defn play
  "Game page handler"
  [req weapon]
  (def oponent (chooseOponent))
  (->> (game-page weapon oponent (playGame weapon oponent)) response)) ;; Equivalent to  (response (home-page (select posts))
