(ns cloudhackers.servlet
  (:gen-class
   :extends javax.servlet.http.HttpServlet
   :exposes-methods {init superInit})
  (:require
   [ring.util.servlet :as servlet]
   ))

(defn reload-libs
  "Reload libs that are already loaded and that match one any of the
given prefixes."
  [& reloadables]
  (println "reload-libs" reloadables)
  (doseq [lib (loaded-libs)]
    (let [libname (name lib)
          no-matches (not-any? #(.startsWith libname %) reloadables)]
      (when (not no-matches)
        (println "Reload" lib)
        (require lib :reload)
        )
      )))

(defn wrap-reload-prefix
  [app & reloadables]
  (fn [req]
    (apply reload-libs reloadables)
    (app req)))

(defn resolve-handler
  "Creates a handler function that uses ns-resolve to lookup another
  handler to call"
  [namespace name]
  (require namespace) ; Require once so that subsequent ns-resolve
                      ; will work
  (fn [req]
    ((ns-resolve namespace name) req)
    )
  )

(def handler
     (-> (resolve-handler 'cloudhackers.app 'apphandler)
         ;; TODO: Avoid reload when deployed to production
         (wrap-reload-prefix  "cloudhackers.")
         )
     )

(servlet/defservice handler)

;;(defn -init
;;  "Override init so that we can dynamically require cloudhackers.app
;;exactly once to avoid AOT compiling cloudhackers.app"
;;  ([this config] (.superInit this config))
;;  ([this] (require 'cloudhackers.app)))
