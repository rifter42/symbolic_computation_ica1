(ns symbolic-computation-ica1.chatbot
  (:require [symbolic-computation-ica1.matching :as matching]
            [symbolic-computation-ica1.format :as format]
            [clojure.data.json :as json]))

(def welcome-message
  "Welcome to Prague Parks chatbot!
The chatbot is designed to guide you
on your jorney through Prague parks.

To exit the application, type quit.")

(defn start! []
  (matching/init)
  (println welcome-message))

(defn exit? [input]
  (if (.contains '("quit", "bye", "exit") input)
    true
    false))

(defn exit! []
  (println "Goodbye!"))

(defn print-fl [& messages]
  (apply print messages)
  (flush))
  
(defn get-input []
  (print-fl "> ")
  (clojure.string/trim-newline (read-line)))

(defn answer! [input]
  (let [[park info park-info] (matching/match input)]
    (cond
      (and (not (nil? park)) (not (nil? info)) (not (nil? park-info)))
        (println (str (format/translate-values-found info park-info) " in " (name park)))
      (and (not (nil? park)) (not (nil? info)))
        (println (str (format/translate-values-not-found info) (matching/get-parks-with-keyword info)))
      (not (nil? park))
        (println (str "History about " park " incoming..."))
      (not (nil? info))
        (println (str "You can do " (name info) " in "(matching/get-parks-with-keyword info))))))
