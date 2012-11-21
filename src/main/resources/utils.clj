(ns net.danielkvasnicka.elasticsearch.scriptingengine.utils
  (:require [clojure.test :refer :all]))

(defn resolve-fn-from-script [scrpt]
  (let [nsName (last (read-string scrpt))]
    (ns-resolve nsName (first (first (ns-publics nsName))))))