# Introduction

The goal of this project was to create a textual chatbot bases on the ELIZA system in Clojure programming language. The chatbot should be able to provide information about parks of Prague.

The following MVP requirements were outlined:

* The application must be started by an appropriate command

* The chatbot must follow a dialogue structure inspired by the ELIZA system

* The program must have at least 10 response options that use the keywords provided by the
user

* The application must be able to provide basic information about Prague parks upon request. For the first task, provide only
textual information for one park

In addition, the requirements included a README file for the project, a Github page formatted as a technical report, and use of Github collaboration tools.

## Chosen technologies
As writing the chatbot in Clojure was a part of the requirements, the project was written in this language. [Leiningen](https://leiningen.org/) was used to automate necessary development tasks (create project, fetch dependencies, run a fully-configured REPL and etc.)

Github issues, milestones and branching system was an obvious choice for all project management related tasks, and is described in detail in the pdf report.

In addition, to make this webpage, the team used Github Pages per requirements. The html files are generated with Clojure plugin [Codox](https://github.com/weavejester/codox) and a custom theme [Rdash](https://github.com/xsc/codox-theme-rdash) was used for better website experience.
