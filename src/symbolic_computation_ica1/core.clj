(ns symbolic-computation-ica1.core
  (:require [symbolic-computation-ica1.chatbot :as chatbot])
  (:gen-class))

(defn -main
  "Main function"
  []
  (chatbot/start!)
  (loop []
    (let [input (chatbot/get-input)]
      (if (chatbot/exit? input)
        (chatbot/exit!)
        (do
          (chatbot/answer! input)
          (recur))))))
