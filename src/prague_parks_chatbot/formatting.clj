(ns prague_parks_chatbot.formatting
  (:require [clojure.string :as str]))

(defn recommed-attraction
  "Selects a random attraction from the data and suggests user to visit it"
  [attr-list]
  (let [random-attraction (rand-nth (str/split attr-list #", "))]
    (str "How about visiting the " random-attraction "? Sounds interesting!")))

(defn replace-ing
  "Removes ing endings from existing keys based on language rules"
  [key]
  (if (= :skiing (keyword key))
    (str/replace key #"ing" "")
    (str/replace key #"ing" "e")))

(defn keyword-to-park
  "Replaces keyword park names with regular names"
  [keyword]
  (-> keyword
    (name)
    (str/replace #"-" " ")
    (str/capitalize)))

(defn park-to-keyword
  "Replaces two word park names with keyword names"
  [input]
  (-> input
    (str/replace #"kinskeho zahrada" "kinskeho-zahrada")
    (str/replace #"letenske sady" "letenske-sady")
    (str/replace #"riegrovy sady" "riegrovy-sady")
    (str/replace #"obora hvezda" "obora-hvezda")
    (str/replace #"vojanovy sady" "vojanovy-sady")
    (str/replace #"frantiskanska zahrada" "frantiskanska-zahrada")))

(defn format-park-names
  "Turns a list of park names into a formatted string"
  [park-list]
  (str/join ", " (map keyword-to-park park-list)))

(defn sanitizer
  "Sanitises user input before passing it for matching"
  [input]
  (-> input
    (str/lower-case)
    (str/replace #"[;,.!?\\]" "")
    (park-to-keyword)))

(defn translate-values-found
  "Formats a response based on the results of the matching when the results for
  a park were found"
  [park key matched-info]
  (cond
    (= :attractions (keyword key))
      (format "Available %s in %s are: %s.\n%s"
          key (keyword-to-park park) matched-info (recommed-attraction matched-info))

    (= :transportation (keyword key))
      (format "You can get to %s by: %s." (keyword-to-park park) matched-info)

    (and (contains? #{:wc, :playground, :parking, :restaurant} (keyword key))
         matched-info)
      (format "Yes, %s has %s %s."
        (keyword-to-park park) (if (= :parking (keyword key)) "" "a") key)

    (and (contains? #{:wc, :playground, :parking, :restaurant} (keyword key))
         (not matched-info))
      (format "Unfortunately, there's no %s." key)

    (and (contains? #{:biking, :skating, :skiing} (keyword key))
         matched-info)
      (format "Yes, you can %s in %s!" (replace-ing key) (keyword-to-park park))

    (and (contains? #{:biking, :skating, :skiing} (keyword key))
         (not matched-info))
      (format "Unfortunately, you can't %s in %s."
        (replace-ing (keyword key)) (keyword-to-park park))

    (and (= :sports (keyword key)) matched-info)
      (format "Yes, you can do %s in %s." key (keyword-to-park park))

    (and (= :sports (keyword key)) (not matched-info))
      (format "Sorry, you can't do %s in %s." key (keyword-to-park park))

    (and (= :dogs (keyword key)) matched-info)
      (format "Yes, %s are allowed in %s." key (keyword-to-park park))

    (and (= :dogs (keyword key)) (not matched-info))
      (format "Sorry, %s aren't allowed in %s." key (keyword-to-park park))))

(defn translate-values-not-found
  "Formats a response based on the results of the matching when the results for
  a park were NOT found"
  [key]
  (format "Unfortunately, there's no information on %s in this park.\nAsk me about %s in"
  key key))
