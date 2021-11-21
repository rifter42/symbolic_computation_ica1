# Implementation

The program parses user input as a string, first sanitising the input to remove
unnecessary characters such as punctuation marks:

```
(defn sanitizer
  "Sanitises user input before passing it for matching"
  [input]
  (-> input
    (str/lower-case)
    (str/replace #"[;,.!?\\]" "")
    (park-to-keyword)))
```

The input then gets split into a vector with one word as an element and gets matched
against chosen keywords. From `matching/match`:

```
matched-park (match-keywords park-names input false)
matched-info (match-keywords synonyms input true)
matched-park-info (get (get parks matched-park) matched-info)
```

Based on the information that was matched (or wasn't), a bot response in generated:

* If only park is matched, bot prints general information about the park and the
  available keywords for that park.

* If only a keyword is matched, bot responds with a list of parks that contain
  information for that keyword.

* If both the keyword and the park is matched, bot tries to get information
  from the `parks` map.

* If selected park contains the identified keyword, bot responds, providing user with an answer to their question.

* If selected park doesn't contain the keyword, a bot performs a search of the parks
 that do, giving user an option to query other parks.

## Notable design decisions

The goal of this implementation was to create a feeling of a real conversation
instead of simply a "query program". Therefore, the program contains several key
features:

* The users don't need to select a specific park or a specific activity. Instead,
they can write questions like `Where can I bike?` or `I want to know more about Vysehrad` and receive an appropriate response:
```
Anastasia> Where can I bike?
Bot> I have information about biking in Bertramka, Vysehrad, Kinskeho zahrada, Petrin, Riegrovy sady, Obora hvezda, Kampa, Frantiskanska zahrada, Ladronka, Letenske sady, Stromovka, Klamovka.
Anastasia> I want to know more about Vysehrad
Bot> Next to the entrance to the Podolská hospital, a narrow steep path winds, which perhaps does not even have a name. It waves between old houses, and whoever reaches the end of it finds himself in front of a tunnel leading through the hill and the fortifications.

I can tell you about wc, attractions, biking, skating, sports, playground, transportation, parking in Vysehrad.
```

* Bot remembers the park user talked about in the previous message and users don't have
to specify the park they're talking about every time - it's assumed. Following the previous
example:
```
...
I can tell you about wc, attractions, biking, skating, sports, playground, transportation, parking in Vysehrad.
Anastasia> Can I bike there?
Bot> Yes, you can bike in Vysehrad!
```

* Bot can recommend user a place to visit:
```
Anastasia> What kind of sights can I see in this park?
Bot> Available attractions in Vysehrad are: sightseeing, museum, cemetery of famous personalities Slavín, national cultural monument.
How about visiting the cemetery of famous personalities Slavín? Sounds interesting!
```
