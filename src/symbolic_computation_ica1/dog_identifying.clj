(ns symbolic-computation-ica1.dog_identifying
  (:require [clojure.data.json :as json]
            [clojure.set]))

(def dog-map "Map to store user answers"
  (ref {}))

(def dog-data "Data with possible dogs and their parameters"
  (json/read-str (slurp "resources/dog-data.json") :key-fn keyword))

(def possible-keys "Possible parameters for questions"
  (distinct (keys (reduce merge (vals dog-data)))))

(def possible-values "Possible values of these parameters"
  (into {}
  (for [key possible-keys]
    (let [vals (distinct (map #(get % key) (vals dog-data)))]
      [key (clojure.string/join " " vals)]))))

(defn match-dog
  "Matches user response against a set of values for a dog. If the set difference
   is zero, returns dog's name"
  [el]
  (when
    (empty?
      (clojure.set/difference
        (set (vals @dog-map)) (set (vals (get dog-data el)))))
    el))
