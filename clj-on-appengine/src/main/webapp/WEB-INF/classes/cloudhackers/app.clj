(ns cloudhackers.app
  (:require
   [net.cgrand.moustache :as moustache]
   [cloudhackers.other] :reload
   ))

(def my-app
     (moustache/app
      ["hi"] {:get "Hello world" }
      ["stuff"] {:get ["stuff: " cloudhackers.other/stuff]}
      [&] {:get "default"}))
