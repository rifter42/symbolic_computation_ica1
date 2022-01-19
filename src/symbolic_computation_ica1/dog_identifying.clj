(ns symbolic-computation-ica1.dog_identifying
  (:require [clojure.data.json :as json]
            [clojure.set]))

(def dog-map ""
  (ref {}))

(def dog-data ""
  (json/read-str (slurp "resources/dog-data.json") :key-fn keyword))

(def possible-keys (distinct (keys (reduce merge (vals dog-data)))))

(def possible-values
  (into {}
  (for [key possible-keys]
    (let [vals (distinct (map #(get % key) (vals dog-data)))]
      [key (clojure.string/join " " vals)]))))


(defn match-dog
  [el]
  (when
    (empty?
      (clojure.set/difference
        (set (vals @dog-map)) (set (vals (get dog-data el)))))
    el))
