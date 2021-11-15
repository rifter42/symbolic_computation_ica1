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

;;parks as a list of strings list
;;The order of this one shall remain the same
(def parks-str-list (map str (keys (ns-publics 'scraper))))

;;parks of files references
(def parks-list
  (list
    parks-list
    letenske-sady
    stromovka
    klamovka
    bertramka
    text-extract
    vysehrad
    kinskeho-zahrada
    petrin
    text-extract-keys
    riegrovy-sady
    obora-hvezda
    vojanovy-sady
    text-extract-values
    parks-list
    kampa
    frantiskanska-zahrada
    key-sanitizer
    map-generator
    ladronka
    sanitizer))

;;example of mapping html source to allow selection selection
(html/html-resource (java.io.File. letenske_sady))

;;select the relevant resources on each page
(html/select (html/html-resource (java.io.File. letenske_sady)) [:div.js-tabbed-content
                                                                 :p])
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


(defn extractor-helper [lst func]
  (map func lst))

;;example usage: (extractor-helper parks-list extracting-function)

;;TODO: scraped data to JSON
(json/write (text-extract letenske_sady))

;;something that works
;; create json from string, then we read it and pprint it
(json/pprint (json/read-str (json/write-str (text-extract letenske_sady))))

;;example of running/using write
(json/write-str (text-extract bertramka "i_wc") java.util.Collection)

(defn text-extract-keys [park-name]
  (text-extract park-name true "key"))

(defn text-extract-values [park-name]
  (text-extract park-name true "value"))

(json/write letenske_sady java.util.Map {:key-fn text-extract-keys
                                         :value-fn text-extract-values})
;;todo: map from keys and values
(zipmap (keys (text-extract-keys letenske_sady)) (vals (text-extract-keys letenske_sady)))

;;todo: Lazyseq of keys
(map keyword (text-extract-keys letenske_sady))

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

(zipmap (key-sanitizer letenske_sady)
        (vals "test"))

;Todo:getting description of a park and initiating the map

(json/pprint (json/read-str (json/write-str (assoc {} :description
                                                      (first (map html/text
                                                                  (html/select
                                                                    (html/html-resource (java.io.File. letenske_sady))
                                                                    [:p.perex])))))))

;; takes string
(defn map-generator [park-name]
  (assoc {} (keyword park-name)
            (assoc {} :description
                      (map html/text
                           (html/select
                             (html/html-resource (java.io.File. (eval (read-string park-name))))
                             [:p.perex])))))

(defn json-generator [park-list ]
  (loop [park-name park-list]
    (let [map (map-generator park-name)]
      (println "./resources/parks_json/park-description.json" "Line to be written")
      (spit "./resources/parks_json/park-description.json" map :append true)))
  )



