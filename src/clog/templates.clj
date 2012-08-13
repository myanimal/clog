(ns clog.templates
  (:use [net.cgrand.enlive-html]))
 
(deftemplate home-page "home.html" [a]
  [:title] (content "rock.paper.scissors"))

(deftemplate game-page "game.html" [weapon oponent result]
  [:title] (content "rock.paper.scissors")
  [:span.weapon] (content weapon)
  [:span.oponent] (content oponent)
  [:span.score] (content (result "score"))
  [:span.result] (content (result "message")))