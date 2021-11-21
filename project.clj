(defproject prague_parks_chatbot "0.1.0-SNAPSHOT"
  :description "ELIZA-style chatbot that provides information about prague parks"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [enlive "1.1.6"]
                 [org.clojure/data.json "2.4.0"]]
  :repl-options {:init-ns prague_parks_chatbot.core}
  :main prague_parks_chatbot.core)
