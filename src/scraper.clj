;;defining namespace
(ns scraper
  (:require [net.cgrand.enlive-html :as html]))

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
  "function takes file name and a class as clojure string returns text of"
  ([park-name]
   (map html/text
        (html/select
          (html/html-resource (java.io.File. park-name))
          [:div.js-tabbed-content
           :p])))

  ([park-name html-class]
   (map html/text
        (html/select
          (html/html-resource (java.io.File. park-name))
          [:div.js-tabbed-content
           (keyword (str "p." html-class))]
          )))
  )
