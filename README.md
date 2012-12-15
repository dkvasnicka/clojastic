Clojastic
=========

Clojure lang plugin for Elasticsearch. 
Build and unzip the final Maven distribution under `${elasticsearch}/plugins`

The plugin will load your scripts and will take the *last* function in every script as an "entry point" to that script.
Namespace declaration is *mandatory* and the function has to take one argument - a map of values 
(`_doc`, `_fields`, `_source` and whatever you send in your query as script params). 
This map solution may (should?) be subject to change in the future...
