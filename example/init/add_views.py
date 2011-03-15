#!/bin/env python2.6
from couchdbhelper import CouchHelper

ch = CouchHelper("http://mydomain.com:5984")
try:
    ch.couch.create('apache_errors')
except:
    pass
ch.select('apache_errors')
ch.sync_view('./views')

