(ns cloudhackers.app
  (:require
   [net.cgrand.moustache :as moustache]
   [cloudhackers.other]
   ))

(def apphandler
     (moustache/app
      ["hi"] {:get "Hello world" }
      ["stuff"] {:get cloudhackers.other/stuff}
      [&] {:get "default"}))
