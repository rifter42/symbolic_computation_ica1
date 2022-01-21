# ICA2

The second part of the project implements a dog identifying feature. Based on user's
answers and the collected data about different dog features, the program helps
to identify dog breeds found in Prague parks. The dog domain was chosen as it seemed
the most often requested: people often ask what breed a dog is, while birds or plants
are mostly only interesting to people with specific hobbies.

## Implementation
The app iterates over a set of possible attributes from `dog-data.json` and request user to provide
values for those attributes (answers).
```
(doseq [[k v] dog/possible-values]
  (print-bot (str "What is the " (name k) " of the dog? (" v ")"))
```

When the answers are collected, to identify the dog the program iterates over
all possible values in `dog-data.json` and finds set difference between user answers and available dogs:
```
(set (vals @dog-map)) (set (vals (get dog-data el)))))
```

When the difference is empty, the dog is considered matched.


## Other improvements
* A github workflow was created performing syntax checks and running automated tests

* Syntax errors were fixed

* Several automated tests were created
