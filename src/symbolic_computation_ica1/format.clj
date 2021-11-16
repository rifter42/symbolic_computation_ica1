(ns symbolic-computation-ica1.format
  (:require [clojure.string :as str]))


(defn recommed-attraction [attr-list]
  (let [random-attraction (rand-nth (str/split attr-list #", "))]
    (str "How about visiting the " random-attraction "? Sounds interesting!")))

(defn replace-ing [key]
  (if (= :skiing (keyword key))
    (str/replace key #"ing" "")
    (str/replace key #"ing" "e")))

(defn translate-values-found [park key matched-info]
  (cond
    (= :attractions (keyword key))
      (format "Available %s in %s are: %s.\n%s"
          key park matched-info (recommed-attraction matched-info))
    (= :transportation (keyword key))
      (format "You can get to %s by: %s." park matched-info)
    (and (contains? #{:wc, :playground, :parking, :restaurant} (keyword key))
         matched-info)
      (format "Yes, %s has %s %s." park (if (= :parking (keyword key)) "" "a") key)
    (and (contains? #{:wc, :playground, :parking, :restaurant} (keyword key))
         (not matched-info))
      (format "Unfortunately, there's no %s." key)
    (and (contains? #{:biking, :skating, :skiing} (keyword key))
         matched-info)
      (format "Yes, you can %s in %s!" (replace-ing key) park)
    (and (contains? #{:biking, :skating, :skiing} (keyword key))
         (not matched-info))
      (format "Unfortunately, you can't %s in %s." (replace-ing (keyword key)) park)
    (and (= :sports (keyword key)) matched-info)
      (format "Yes, you can do %s in %s." key park)
    (and (= :sports (keyword key)) (not matched-info))
      (format "Sorry, you can't do %s in %s." key park)
    (and (= :dogs (keyword key)) matched-info)
      (format "Yes, %s are allowed in %s." key park)
    (and (= :dogs (keyword key)) (not matched-info))
      (format "Sorry, %s aren't allowed in %s." key park)))

(defn translate-values-not-found [key]
  (format "Unfortunately, there's no information on %s in this park.\nAsk me about %s in"
  key key))
