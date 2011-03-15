{
   "_id": "_design/list",
   "language": "javascript",
   "views": {
       "by_timestamp": {
           "map": "function(doc) {\n  emit(doc.timestamp, doc);\n}"
       }
   }
}