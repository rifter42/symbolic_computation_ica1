(ns symbolic-computation-ica1.scraper
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))

;;binding parks .html paths to variables for code clarity
(def letenske-sady "./resources/parks_html/letenske_sady.html")
(def bertramka "./resources/parks_html/bertramka.html")
(def frantiskanska-zahrada "./resources/parks_html/frantiskanska_zahrada.html")
(def kampa "./resources/parks_html/kampa.html")
(def kinskeho-zahrada "./resources/parks_html/kinskeho_zahrada.html")
(def klamovka "./resources/parks_html/klamovka.html")
(def ladronka "./resources/parks_html/ladronka.html")
(def obora-hvezda "./resources/parks_html/obora_hvezda.html")
(def petrin "./resources/parks_html/petrin.html")
(def riegrovy-sady "./resources/parks_html/riegrovy_sady.html")
(def stromovka "./resources/parks_html/stromovka.html")
(def vojanovy-sady "./resources/parks_html/vojanovy_sady.html")
(def vysehrad "./resources/parks_html/vysehrad.html")

;;Binding a list of strings of the parks names to be used
;; in map-generator function
(def parks-list
  '("letenske-sady"
     "stromovka"
     "klamovka"
     "bertramka"
     "vysehrad"
     "kinskeho-zahrada"
     "petrin"
     "riegrovy-sady"
     "obora-hvezda"
     "vojanovy-sady"
     "kampa"
     "frantiskanska-zahrada"
     "ladronka"))

;;Classes of the values to extract specific information
;;from the parks web pages:
;;i_restaurace, i_wc, i_misto, i_kolo, i_brusle, i_sport
;;i_hriste, i_mhd, i_gps, i_parking, i_cesty, i_provoz
;;i_doba

(defn text-extract
  "function takes an html file/park name and a class (as clojure string)
  and a wild-card element. it returns text content of matching tags/attributes
  if bool false, wild-card should be a html class from the list above
  if bool is true, wild-card should be either key or value as trings"
  ([park-name]
   (map html/text
        (html/select
          (html/html-resource (java.io.File. park-name))
          [:div.js-tabbed-content
           :p]
          )))

  ([park-name bool wild-card]
   (if (false? bool)
     (map html/text
          (html/select
            (html/html-resource (java.io.File. park-name))
            [:div.js-tabbed-content
             (keyword (str "p." wild-card))]
            ))

     (if (= wild-card "key")
       (map html/text
            (html/select
              (html/html-resource (java.io.File. park-name))
              [:div.js-tabbed-content
               :p
               :strong]
              ))

       (if (= wild-card "value")
         (map html/text
              (html/select
                (html/html-resource (java.io.File. park-name))
                [:div.js-tabbed-content
                 :p
                 [:font (html/nth-child 2)]]
                ))))
     )))

(defn text-extract-keys [park-name]
  "Takes a park-name that's bound to html file of the park
  returns keys of the elements to be extracted"
  (text-extract park-name true "key"))

(defn text-extract-values [park-name]
  "Takes a park-name that's bound to html file of the park
  and returns the value of values for the keys extracted previously"
  (text-extract park-name true "value"))


(defn sanitizer [str]
  "Takes a string and formats the output to comply with JSON specifications"
  (clojure.string/replace
    (clojure.string/replace
      (clojure.string/replace
        str
        #"\s+\S*$" "")
      #" " "_")
    #":" "")
  )

(defn key-sanitizer [park-name]
  "Takes an html file and returns
  sanitized keywords after extracting them from the text"
  (map keyword
       (map
         sanitizer (text-extract-keys park-name))))

(defn map-generator
  "Takes park name as a string only or a map in addition to the string
   and, it constructs a map of the park name, and it's description
   or ot merges the park name and its description with the map provided"
  ([park-name]
   (assoc {} (keyword park-name)
             (assoc {} :description
                       (first
                         (map html/text
                              (html/select
                                (html/html-resource (java.io.File. (eval (read-string park-name))))
                                [:p.perex]))))))

  ([park-name map]
   (merge (map-generator park-name) map)))

(defn json-generator [parks-list]
  "Transforms a map of parks descriptions to a JSON formatted file"
  (let [parks-map (apply merge
                         (map map-generator
                              parks-list))]
    (spit "./resources/parks_json/park-description.json"
          (json/write-str parks-map)
          :append false)))

;;dogs breeds

; caching content

(def dog-breeds-url "https://www.akc.org/dog-breeds/")

(def cache-dog-breeds-url
  "Get website content from www.akc.org
  returns a hashlist of html content as hash-maps"

  (html/html-resource (java.net.URL. dog-breeds-url)))

;; list of breeds
(html/select cache-dog-breeds-url [:div.custom-select :select :option])

;; The breeds

(def dog-breeds
  (drop 1
        (distinct (map html/text
                       (html/select cache-dog-breeds-url
                                    [:div.custom-select
                                     :select#breed-search
                                     :option])))))

;; The URLs of each breed

(def dog-breeds-urls
  (mapcat #(html/attr-values % :value)
          (html/select cache-dog-breeds-url
                       [:div.custom-select
                        :select#breed-search
                        :option])))

;;TODO: extracting data from each breed using the URLs

;; template to start with

(def dog-data-url-tmp "https://www.akc.org/dog-breeds/affenpinscher/")

(def cache-dog-data-url
  "Get website content from www.akc.org
  returns a hashlist of html content as hash-maps"

  (html/html-resource (java.net.URL. dog-data-url-tmp)))

