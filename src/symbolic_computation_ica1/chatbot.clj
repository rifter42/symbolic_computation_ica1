(ns symbolic-computation-ica1.chatbot
  (:require [symbolic-computation-ica1.matching :as matching]
            [symbolic-computation-ica1.formatting :as formatting]
            [symbolic-computation-ica1.dog_identifying :as dog]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.set]))


(def welcome-message "A message to display at the start of the application"
  "========================================================================
                  Welcome to Prague Parks Chatbot!
The chatbot is designed to guide you on your journey through Prague parks.

To exit the application, type quit.
========================================================================")

(def hist-data "Historical data for parks"
  (json/read-str (slurp "resources/parks_json/park-description.json") :key-fn keyword))

(def user-name "Username to display at the beginning of user input"
  (ref "User"))

(defn set-name!
  "Sets user-name variable"
  [name]
  (dosync (ref-set user-name name)))

(defn print-bot
  "Prints bot messages with a prompt at the beginning"
  [output]
  (println "Bot>" output)
  (flush))

(defn get-input
  "Get user input with a user-name at the beginning"
  []
  (print (str @user-name "> "))
  (flush)
  (string/trim-newline (read-line)))

(defn start!
  "Runs once at the start of the application. Prints information and
  asks for user-name"
  []
  (println welcome-message)
  (print-bot "Hi! I'm here to provide information about various parks of Prague.")
  (print-bot "But first, tell me your name:")
  (set-name! (get-input))
  (print-bot (str "Hi " @user-name "! If you already know which park you want to visit, just type its name"
            " and I'll give you a small overview.\nCurrently, I can tell you about"
            " the following parks: " (formatting/format-park-names matching/park-names)))
  (print-bot (str "If you aren't sure which park to visit, ask about the activity"
                 " you'd like to do!")))

(defn exit?
  "Checks if user input contains one of the exit words"
  [input]
  (.contains '("quit" "bye" "exit") input))

(defn exit!
  "Prints goodbye message before exiting"
  []
  (print-bot "Goodbye!"))

(defn answer-park
  "Contains answer logic based on the kind of information
  matched from user input for the park part of the application."
  [input]
  (let [[park info park-info] (matching/match (formatting/sanitizer input))]
    (cond
      (and (not (nil? park)) (not (nil? info)) (not (nil? park-info)))
        (print-bot (formatting/translate-values-found
                    park (name info) park-info))

      (and (not (nil? park)) (not (nil? info)))
        (print-bot (format "%s %s"
                    (formatting/translate-values-not-found (name info))
                    (formatting/format-park-names (matching/get-parks-with-keyword info))))

      (not (nil? park))
        (print-bot (format "%s\n\nI can tell you about %s in %s."
                    (get (get hist-data park) :description)
                    (matching/get-parks-activities park)
                    (formatting/keyword-to-park park)))

      (not (nil? info))
        (print-bot (format "I have information about %s in %s."
                    (name info)
                    (formatting/format-park-names (matching/get-parks-with-keyword info))))

      :else
        (print-bot "Sorry, I'm not sure what you mean"))))


(defn answer-dog
  "Contains answer logic based on the kind of information
  matched from user input for the dog part of the application."
  []
  (doseq [[k v] dog/possible-values]
    (print-bot (str "What is the " (name k) " of the dog? (" v ")"))
    (dosync (ref-set dog/dog-map (assoc @dog/dog-map k (get-input)))))
    (let [dog (some #(dog/match-dog %) (keys dog/dog-data))]
      (if dog
        (print-bot (str "This dog could be " (name dog)))
        (print-bot (str "Sorry, can't find a dog with this description.\n"
                         "If you want to try again, type 'identify dog'")))
      (dosync (ref-set dog/dog-map {}))))

(defn answer!
  "Main bot fucntion, displays bot answer to the user."
  [input]
  (let [sanitized-input (formatting/sanitizer input)]
    (if (.contains '("identify dog") sanitized-input)
      (answer-dog)
      (answer-park sanitized-input))))
