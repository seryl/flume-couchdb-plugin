flume-couchdb-plugin
====================

The flume-couchdb-plugin allows you to use CouchDB as a Flume Sink.

Compiling
---------

run `ant`

Setting up the Sink
----------------------------------------------

couchdbSink( "logging.mydomain.com", 5984, "logs", "\\[({host}\\w+)\\] ({pool}\\w+): \\[.*?\\] \\[\\w+\\] \\[client ({client}.*?)\\] ({error}.*?): ({message}.*)", "message, host" )

This sets up a basic couchdb sink to drop logs off to the "logs" databse in couchdb, using the regex to setup hashes for the log message.

Turns your flat logs into hashed, usable logs!

I'll have some more documentation here later this week, I know this was a little brief for now.
