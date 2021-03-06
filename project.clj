(defproject symbolic_computation_ica1 "0.1.0"
  :description "An ELIZA_like chatbot about Prague parks in Clojure"
  :url "https://github.com/rifter42/symbolic_computation_ica1"
  :license {:name "Eclipse Public License - v 2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [enlive "1.1.6"]
                 [codox-theme-rdash "0.1.2"]
                 [org.clojure/data.json "2.4.0"]]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:output-path "docs"
          :doc-files ["docs/intro.md", "docs/implementation.md", "docs/future.md", "docs/ica2.md"]
          :metadata {:doc/format :markdown}
          :themes [:rdash]}
  :repl-options {:init-ns symbolic-computation-ica1.core}
  :main symbolic-computation-ica1.core)
