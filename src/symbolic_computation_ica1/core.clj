(ns symbolic-computation-ica1.core
  (:require
    [symbolic-computation-ica1.chatbot :as chatbot])
  (:gen-class))

(defn -main
  "Main function"
  []
  (println chatbot/welcome-message)
  (loop []
    (let [input (read-line)]
      (if (chatbot/exit? input)
        (chatbot/exit!)
        (do
          (chatbot/answer! input)
          (recur))))))
