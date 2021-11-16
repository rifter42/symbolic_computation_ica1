;;defining namespace
(ns scraper
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))

;;binding parks .html paths to variabes for clarity
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

;;parks of files references
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

;;Classes of the Values to extract:
;;i_restaurace, i_wc, i_misto, i_kolo, i_brusle, i_sport
;;i_hriste, i_mhd, i_gps, i_parking, i_cesty, i_provoz
;;i_doba

;; extract text from relevant html tags
(defn text-extract
  "function takes file/park name and a class (as clojure string)
  returns text content of matching tags/attributes"
  ([park-name]
   (map html/text
        (html/select
          (html/html-resource (java.io.File. park-name))
          [:div.js-tabbed-content
           :p]
          )))

  ;if bool false, joker should be a html class from the lst above
  ;if bool is true, joker should be either key or value
  ([park-name bool wild-card]
   (if (false? bool)
    (map html/text
        (html/select
          (html/html-resource (java.io.File. park-name))
          [:div.js-tabbed-content
           (keyword (str "p." wild-card))]
          ))

    ;getting the keys for later JSON processing
    (if (= wild-card "key")
      (map html/text
           (html/select
             (html/html-resource (java.io.File. park-name))
             [:div.js-tabbed-content
              :p
              :strong]
             ))

      ;;getting the values for later JSON processing
      ;;the n-th child 2 selects the font tag
      ;;that comes as a second desendent of p tag
      (if (= wild-card "value")
        (map html/text
             (html/select
               (html/html-resource (java.io.File. park-name))
               [:div.js-tabbed-content
                :p
                [:font (html/nth-child 2)]]
               ))))
    )))

;;example usage: (extractor-helper parks-list extracting-function)

;;exctarcting keys and values of from paks pages
(defn text-extract-keys [park-name]
  (text-extract park-name true "key"))

(defn text-extract-values [park-name]
  (text-extract park-name true "value"))

;;sanitizing output
(defn sanitizer [str]
  (clojure.string/replace
    (clojure.string/replace
      (clojure.string/replace
        str
        #"\s+\S*$" "")
      #" " "_")
    #":" "")
  )

;;sanitizing keywords

(defn key-sanitizer [park-name]
  (map keyword
       (map
         sanitizer (text-extract-keys park-name))))

;; takes string
(defn map-generator [park-name]
  (assoc {} (keyword park-name)
            (assoc {} :description
                      (first
                        (map html/text
                           (html/select
                             (html/html-resource (java.io.File. (eval (read-string park-name))))
                             [:p.perex]))))))

;;testing json generation
(defn json-generator [park-list]
  (spit "./resources/parks_json/park-description.json" (json/write-str
                                                         (map-generator (first park-list))))
  (loop [park-lst (rest park-list)]
    (let [park-map (map-generator
                     (json/write-str (first park-lst)))]
      ;(spit "./resources/parks_json/park-description.json" park-map :append true)
      ;(println "./resources/parks_json/park-description.json" park-map)
      )
    (and (not (nil? (rest park-lst))) (recur (rest park-lst)))
    ))

;generate JSON file
(spit "./resources/parks_json/park-description.json" map :append true)


