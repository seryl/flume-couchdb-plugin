logflume-couchdb-plugin
====================

The flume-couchdb-plugin allows you to use CouchDB as a Flume Sink.

Anyone who was considering using this project as a logging utility, I'd highly suggest looking at [Logstash](https://github.com/logstash/logstash). Apparently someone else had the same idea, but I think that project is a little more promising.

Compiling
---------

    run `ant`

Note: this build includes [jregex_custom](https://github.com/seryl/jregex_custom) which allows named group regular expressions.

Basic idea
----------

![Logging Example](https://github.com/seryl/flume-couchdb-plugin/raw/master/example/logging.png)

The initial idea was to allow multiple inputs to be pushed through the filter, and finally into the couchdb store. Each input, can go into it's own separate filter, allowing you to change the filtering and metadata that you setup with the sink.

Setting up the Sink
-------------------

    couchdbSink(
        "logging.mydomain.com", 5984, "logs",
        "\\[({host}\\w+)\\] ({pool}\\w+): \\[.*?\\] \\[\\w+\\] \\[client ({client}.*?)\\] ({error}.*?): ({message}.*)")

This sets up a basic couchdb sink to drop logs off to the "logs" database in couchdb, using the regex to setup hashes for the log message. And now you have turned your flat logs into hashed, usable logs!

Example
-------

In the example folder that's added; I have some example init scripts that I'd used to create a web app to display the log data. Sadly I wasn't able to provide that source, however the basic idea's in the scripts provided. Basically you can grab the immediately available data from CouchDB, or you can search the data with elasticsearch.

I once again will highly recommend taking a look at [Logstash](https://github.com/logstash/logstash); had I seen that prior to creating this I definitely would have gone that route.

One of the main problems with this setup; is that couchdb is an append only database. Logtash is using MongoDB and because of that -- they're able to set limits on db size and do in-place incremental counters. It's just a better overall solution.
