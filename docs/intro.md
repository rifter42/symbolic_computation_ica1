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

## Chat Examples
The developed program meets all the MVP requirements. Below are some examples of
possible conversations to prove that:
```
Bot> Hi! I'm here to provide information about various parks of Prague.
Bot> But first, tell me your name:
User> Anastasia
Bot> Hi Anastasia! If you already know which park you want to visit, just type its name and I'll give you a small overview.
Currently, I can tell you about the following parks: Bertramka, Vysehrad, Kinskeho zahrada, Petrin, Riegrovy sady, Obora hvezda, Kampa, Frantiskanska zahrada, Ladronka, Letenske sady, Stromovka, Klamovka
Bot> If you aren't sure which park to visit, ask about the activity you'd like to do!
Anastasia> What can you tell me about Kampa?
Bot> On the way from Charles Bridge over Na Kampe Square, at the end of it, a view opens up to a small park, which, however, provides a large amount of pleasure, whether for body or soul, on a relatively small area of less than three hectares.

I can tell you about wc, attractions, biking, skating, sports, playground, transportation, parking in Kampa.


Anastasia> Can I park my car there?
Bot> Unfortunately, there's no parking.
Anastasia> Can I park my car in Bertramka then?
Bot> Yes, Bertramka has  parking.


Anastasia> What attractions can I see there
Bot> Available attractions in Bertramka are: cultural monument, classical music concerts, social events, W. A. Mozart Museum.
How about visiting the cultural monument? Sounds interesting!
```


```
Bot> If you aren't sure which park to visit, ask about the activity you'd like to do!
Anastasia> I want to go skiing
Bot> I have information about skiing in Obora hvezda.
Anastasia> Is skiing possible in Obora hvezda?
Bot> Yes, you can ski in Obora hvezda!
Anastasia> quit
Bot> Goodbye!
```
