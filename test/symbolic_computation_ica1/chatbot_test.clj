(ns symbolic-computation-ica1.chatbot-test
  (:require [clojure.test :refer :all]
            [symbolic-computation-ica1.chatbot :refer :all]))

(deftest print-bot-test
  (testing "Bot output testing"
    (is (= "Bot> Med\n"
           (binding [*out* (java.io.StringWriter.)]
             (print-bot "Med")
             (str *out*))))

    (is (= "Bot> Anastasia\n"
           (binding [*out* (java.io.StringWriter.)]
             (print-bot "Anastasia")
             (str *out*))))))

(deftest exit?-test
  (testing "Exit predicate testing"
    (is (exit? "exit"))
    (is (exit? "bye"))
    (is (exit? "quit"))))