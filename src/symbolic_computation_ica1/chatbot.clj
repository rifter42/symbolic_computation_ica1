(ns symbolic-computation-ica1.chatbot
  (:require [symbolic-computation-ica1.matching :as matching]
            [symbolic-computation-ica1.format :as formatting]
            [clojure.data.json :as json]))

(def welcome-message
  "========================================================================
                  Welcome to Prague Parks chatbot!
The chatbot is designed to guide you on your jorney through Prague parks.

To exit the application, type quit.
========================================================================")

(defn start! []
  (println welcome-message)
  (println "Hi! I'm here to provide information about various parks of Prague.")
  (println (str "If you already know which park you want to visit, just type its name"
            " and I'll give you a small overview.\nCurrently, I can tell you about"
            " the following parks: " (clojure.string/join ", " (map name matching/park-names))))
  (println (str "If you aren't sure which park to visit, ask about the activity"
                 " you'd like to do!")))

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
        (println (formatting/translate-values-found
                 (name park) (name info) park-info))
      (and (not (nil? park)) (not (nil? info)))
        (println (format "%s %s"
                 (formatting/translate-values-not-found (name info))
                 (matching/get-parks-with-keyword info)))
      (not (nil? park))
        (println (format "History about %s incoming...\n\nI can tell you about %s in %s"
                 (name park) (matching/get-parks-activities park) (name park)))
      (not (nil? info))
        (println (format "I have information about %s in %s."
                 (name info) (matching/get-parks-with-keyword info))))))
