(ns prague_parks_chatbot.matching
  (:require [clojure.data.json :as json]
            [clojure.string :as str]))

(def parks "Main park data loaded into a map"
  (json/read-str (slurp "resources/park-data.json") :key-fn keyword))

(def synonyms "The list of synonyms for every availiable activity"
  (json/read-str (slurp "resources/synonym-list.json") :key-fn keyword))

(def selected-park "Stores the name of the park user entered in a previous message"
  (ref nil))

(def park-names "A set of park names"
  (into #{} (keys parks)))

(defn match-keyword
  "Returns the element as a keyword if it's present in the set of keys"
  [keys el]
  (when (contains? keys (keyword el))
    (keyword el)))

(defn match-keyword-with-syn [keys el]
  "Returns the key if it's present in the vector of synonyms for this key"
  (some
    (fn [[k v]]
      (if (some #(.contains el %) v)
        k)) keys))

(defn match-keywords
  "Takes user input and matches every word against a list of given keys.
  If syn? is true, matches every word against a map of synonym. If false,
  matches against a list of keywords.
  Returns the first matched result"
  [keys input syn?]
  (let [input-vector (str/split input #" ")]
    (first
      (filter identity
        (if syn?
          (map #(match-keyword-with-syn keys %) input-vector)
          (map #(match-keyword keys %) input-vector))))))

(defn get-parks-list
  "Returns park name if park has a given key"
  [key park]
  (when (contains? (get parks park) key)
    (name park)))

(defn get-parks-with-keyword
  "Returns a list of parks that have a given activity"
  [activity]
  (filter identity
    (map #(get-parks-list activity %) park-names)))

(defn get-parks-activities
  "Returns a list of all activities in a given park"
  [park]
  (str/join ", "
    (map name (keys (get parks park)))))

(defn match
  "Main matching function. Takes user input and applies matching logic:
  matches park name, activities, and activities in specific park if selected.
  Returns a vector of the matching results"
  [input]
  (let* [matched-park (match-keywords park-names input false)
         matched-info (match-keywords synonyms input true)
         matched-park-info (get (get parks matched-park) matched-info)]
      (if (not (nil? matched-park))
          (dosync (ref-set selected-park matched-park)))
      (if (and (nil? matched-park) (not (nil? matched-info)))
        (if (contains? (get parks @selected-park) matched-info)
          [@selected-park, matched-info, (get (get parks @selected-park) matched-info)]
          [@selected-park, matched-info, matched-park-info])
        [matched-park, matched-info, matched-park-info])))
