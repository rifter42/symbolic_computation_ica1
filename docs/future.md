# Future Improvements

Given the general design idea of this implementation, it would make sense to focus
on improving the "conversation" opportunities of the chatbot, for example:

* Suggest a different park if the activity is not available in the current park.

* As previously mentioned, adding multiple keywords opportunity that will make possible
to answer questions like `Can I bike or eat in this park?`.

* Add random variations into bot responses ("Goodbye", "See you later!" and etc.)

Moreover, some improvements to code should be made. `formatting/park-to-keyword`
function is not generic enough, and `formatting/translate-values-found` is quite big
and confusing. Exception handling is a very important part of every software and,
unfortunately, is still missing from the implementation. These issues will be one
of the main goals for the team in the second part of this project.

Talking about the operations part of the project, the team aims to add at least a few automated tests to the project. With that, a github workflow with syntax check and
test runs on pull requests would be a good final touch.
