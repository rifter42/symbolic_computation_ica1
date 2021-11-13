(ns symbolic-computation-ica1.format)


(defn translate-values-found [key matched-info]
  (cond
    (contains? #{:attractions, :transportation} key)
      (str "Available " (name key) " are: " matched-info)
    (and (contains? #{:wc, :playground, :parking, :restaurant} key) matched-info)
      (str "Yes, there is a " (name key))
    (and (contains? #{:wc, :playground, :parking, :restaurant} key) (not matched-info))
      (str "No, there's no " (name key))
    (and (contains? #{:biking, :skating, :sports, :dogs} key) matched-info)
      (str "Yes, you can " (name key))
    (and (contains? #{:biking, :skating, :sports, :dogs} key) (not matched-info))
      (str "No, yoy can't " (name key))))

(defn translate-values-not-found [key]
  (str
    "Unfortunately, there's no information on " (name key)
    " in this park. Ask me about " (name key) " in " ))
