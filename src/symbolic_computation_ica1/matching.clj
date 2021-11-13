(ns symbolic-computation-ica1.matching
  (:require [clojure.data.json :as json]
            [clojure.set :as set]))

(def selected-park (ref "a"))

(defn load-park-info []
  (json/read-str (slurp "files/park-data.json") :key-fn keyword))

(defn init []
  (def parks (load-park-info))
  (def park-names (into #{} (keys parks)))
  (def possible-info (into #{} (keys (reduce merge (vals parks)))))
  (def selected-park (ref nil)))

(defn split-input [str]
  (clojure.string/split str #" "))

(defn match-keyword [keys el]
  (if (contains? keys (keyword el))
      (keyword el)))

(defn get-parks-list [key park]
  (if (contains? (get parks park) key)
    park))

(defn match-keywords [keys query]
  (let [query-vector (split-input query)]
    (first (filter identity (map #(match-keyword keys %) query-vector)))))

(defn get-parks-with-keyword [keyword]
  (clojure.string/join ", "
    (filter identity
      (map #(get-parks-list keyword %) park-names))))

(defn match [query]
  (let* [matched-park (match-keywords park-names query)
         matched-info (match-keywords possible-info query)
         matched-park-info (get (get parks matched-park) matched-info)]
      (if (not (nil? matched-park))
          (dosync (ref-set selected-park matched-park)))
      (if (and (nil? matched-park) (not (nil? matched-info)))
        (if (contains? (get parks @selected-park) matched-info)
          [@selected-park, matched-info, (get (get parks @selected-park) matched-info)]
          [@selected-park, matched-info, matched-park-info])
        [matched-park, matched-info, matched-park-info])))
