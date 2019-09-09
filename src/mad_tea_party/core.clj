(ns mad-tea-party.core
  (:require [clojure.core.async :as async]))

(def google-tea (async/chan 10))
(def yahoo-tea (async/chan 10))

(defn random-add []
  (reduce + (conj [] (repeat (rand-int 100000) 1))))

(defn request-google []
  (async/go 
    (random-add)
    (async/>! google-tea
              "tea compliments of google")))

(defn request-yahoo []
  (async/go
    (random-add)
    (async/>! yahoo-tea
              "tea compliments of yahoo")))

(defn request-tea []
  (request-google)
  (request-yahoo)
  (async/go (let [[v] (async/alts!
                       [google-tea
                        yahoo-tea])]
              (println v))))

