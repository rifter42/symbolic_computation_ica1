(ns symbolic-computation-ica1.core
  (:require [symbolic-computation-ica1.chatbot :as chatbot])
  (:gen-class))

(defn -main
  "Main function. Initialises chatbot and loops, consuming user input, until
  a specific exit word is entered, On each iteration of the loop, passes the input
  to chatbot to print an answer."
  []
  (chatbot/start!)
  (loop []
    (let [input (chatbot/get-input)]
      (if (chatbot/exit? input)
        (chatbot/exit!)
        (do
          (chatbot/answer! input)
          (recur))))))
