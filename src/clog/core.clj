(ns clog.core
  (:use ring.adapter.jetty
        ring.middleware.resource
        ring.middleware.reload
        ring.middleware.stacktrace
        ring.util.response
        net.cgrand.moustache
        clog.controller
        ))

;; Routes definition
(def routes
  (app 
    (wrap-reload)
    (wrap-stacktrace)
    [""] {:get index}
    ["play" weapon] {:get (fn [req] (play req weapon))}
    ))
 
;;; function for starting/stopping jetty
(defonce server
  (run-jetty #'routes {:port 8888 :join? false}))
