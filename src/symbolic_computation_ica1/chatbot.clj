(ns symbolic-computation-ica1.chatbot)

(def welcome-message
  "Welcome to Prague Parks chatbot!
The chatbot is designed to guide you
on your jorney through Prague parks.

To exit the application, type quit.")

(defn exit? [input]
  (if (.contains '("quit", "bye", "exit") input)
    true
    false))

(defn exit! []
  (println "Goodbye!"))

(defn answer! [input]
  (println (str "Your input: " input)))
