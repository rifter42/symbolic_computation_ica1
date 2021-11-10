;;defining namespace
(ns scraper
  (:require [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]))

;;binding parks .html paths to variabes for clarity
(def letenske_sady "./resources/parks_html/letenske_sady.html")
(def bertramka "./resources/parks_html/bertramka.html")
(def frantiskanska_zahrada "./resources/parks_html/frantiskanska_zahrada.html")
(def kampa "./resources/parks_html/kampa.html")
(def kinskeho_zahrada "./resources/parks_html/kinskeho_zahrada.html")
(def klamovka "./resources/parks_html/klamovka.html")
(def ladronka "./resources/parks_html/ladronka.html")
(def obora_hvezda "./resources/parks_html/obora_hvezda.html")
(def petrin "./resources/parks_html/petrin.html")
(def riegrovy_sady "./resources/parks_html/riegrovy_sady.html")
(def stromovka "./resources/parks_html/stromovka.html")
(def vojanovy_sady "./resources/parks_html/vojanovy_sady.html")
(def vysehrad "./resources/parks_html/vysehrad.html")

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

;;TODO: function to iterate through the list of parks
(def parks-list (keys (ns-publics 'scraper)))

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