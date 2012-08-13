(ns clog.core
  (:use ring.adapter.jetty
        ring.middleware.resource
        ring.middleware.reload
        ring.middleware.stacktrace
        ring.util.response
        net.cgrand.moustache
        clog.controller
        )
  (:require [taoensso.carmine :as r]))

;;; Define Redis connection
(def pool (r/make-conn-pool :max-active 8))
(def spec-server1 (r/make-conn-spec :host     "127.0.0.1"
                                    :port     6379
                                    :db       3
                                    :timeout  4000))
(defmacro redis
  "Acts like (partial with-conn pool spec-server1)."
  [& body] `(r/with-conn pool spec-server1 ~@body))
 
;; A simple handler to show send some response to the client.
; (defn index
;   [req]
;   (response
;     (str "Welcome, to Clog - A Blog Engine written in Clojure<br/>\n"
;       (redis
;         (r/ping)
;         (r/set "foo" "barstard")
;         (r/get "foo")))))
 
;; Routes definition
(def routes
  (app 
    (wrap-reload)
    (wrap-stacktrace)
    [""] {:get index}
    ["play" weapon] {:get (fn [req] (play req weapon))}
    ; ["play" weapon] (delegate play)
    ))
 
;;; function for starting/stopping jetty
(defonce server
  (run-jetty #'routes {:port 8888 :join? false}))
