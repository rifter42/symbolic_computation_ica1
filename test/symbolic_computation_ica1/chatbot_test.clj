(ns symbolic-computation-ica1.chatbot-test
  (:require [clojure.test :refer :all]
            [symbolic-computation-ica1.chatbot :refer :all]))

(deftest print-bot-test
  (testing "Bot output testing"
    (is (= "Bot> Med\r\n"
           (binding [*out* (java.io.StringWriter.)]
             (print-bot "Med")
             (str *out*))))

    (is (= "Bot> Anastasia\r\n"
           (binding [*out* (java.io.StringWriter.)]
             (print-bot "Anastasia")
             (str *out*))))))