(ns cloudhackers.servlet
  (:gen-class
   :extends javax.servlet.http.HttpServlet
   :exposes-methods {init superInit})
  (:require
   [ring.util.servlet :as servlet]
   [ring.middleware.reload :as reload]
   )
)

(defn -service
  [servlet request response]
  
  (require 'cloudhackers.app :reload)
  (let [handler (ns-resolve 'cloudhackers.app 'my-app)]
    ((servlet/make-service-method handler)
     servlet request response)))
