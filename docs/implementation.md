# Implementation

The program parses user input as a string, first sanitising the input to remove
unnecessary characters such as punctuation marks:

```
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
  available keywords for that park:
```
(not (nil? park))
  (print-bot (format "%s\n\nI can tell you about %s in %s."
               (get (get hist-data park) :description)
               (matching/get-parks-activities park)
               (formatting/keyword-to-park park)))
```

* If only a keyword is matched, bot responds with a list of parks that contain
  information for that keyword:
```
(not (nil? info))
  (print-bot (format "I have information about %s in %s."
               (name info)
               (formatting/format-park-names (matching/get-parks-with-keyword info))))
```

* If both the keyword and the park is matched, bot tries to get information
  from the `parks` map.

* If selected park contains the identified keyword, bot responds, providing user with an answer to their question:
```
(and (not (nil? park)) (not (nil? info)) (not (nil? park-info)))
  (print-bot (formatting/translate-values-found
              park (name info) park-info))
```

* If selected park doesn't contain the keyword, a bot performs a search of the parks
 that do, giving user an option to query other parks:
 ```
 (and (not (nil? park)) (not (nil? info)))
   (print-bot (format "%s %s"
               (formatting/translate-values-not-found (name info))
               (formatting/format-park-names (matching/get-parks-with-keyword info)))
 ```

* There's an additional `else` case with a response to all unmatched results:
```
:else
  (print-bot "Sorry, I'm not sure what you mean"))
```


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


In addition, it's worth noting that the use of `some` and `map` in `matching` functions
is intentional. `matching/match-keyword-with-syn` uses `some` because, for every keyword
it attempts to match, it should only return one keyword, even if (accidentally) it ended up
with two matches. However, for the `matching/match-keywords` function `map` provides an
opportunity to locate several keywords in one user input. Right now, only the first
match is used, but the logic makes it possible to match several keywords in the future.
