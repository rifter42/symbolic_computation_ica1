(defproject symbolic_computation_ica1 "0.0.2"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [enlive "1.1.6"]
                 [codox-theme-rdash "0.1.2"]
                 [org.clojure/data.json "2.4.0"]]
  :plugins [[lein-codox "0.10.8"]]
  :codox {:output-path "docs"
          :doc-files ["docs/intro.md", "docs/implementation.md", "docs/future.md"]
          :metadata {:doc/format :markdown}
          :themes [:rdash]}
  :repl-options {:init-ns symbolic-computation-ica1.core}
  :main symbolic-computation-ica1.core)
