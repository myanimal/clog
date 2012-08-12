(ns clog.core
  (:use ring.adapter.jetty
        ring.middleware.resource
        ring.util.response
        ring.middleware.reload
        ring.middleware.stacktrace
        net.cgrand.moustache
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
 
;;; A simple handler to show send some response to the client.
(defn index
  [req]
  (response
    (str "Welcome, to Clog - A Blog Engine written in Clojure<br/>\n"
      (redis
        (r/ping)
        (r/set "foo" "barstar")
        (r/get "foo")))))
 
;; Routes definition
(def routes
  (app (wrap-reload) (wrap-stacktrace)
    [""] index))
 
;;; function for starting/stopping jetty
(defonce server
  (run-jetty #'routes {:port 8888 :join? false}))
