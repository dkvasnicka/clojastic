(ns net.danielkvasnicka.elasticsearch.scriptingengine.utils
  (:require [clojure.test :refer :all]))

(defn resolve-fn-from-script [scrpt]
  (let [nsName (nth (read-string scrpt) 1)]
    (ns-resolve nsName (first (first (ns-publics nsName))))))