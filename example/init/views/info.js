{
   "_id": "_design/info",
   "language": "javascript",
   "views": {
       "errors": {
           "map": "function(doc) {\n  if(doc.error) {\n    emit(doc.error, null);\n}\n}",
          "reduce": "function(keys, values) {\n  return null;\n}"
       },
       "pools": {
           "map": "function(doc) {\n  if (doc.pool) {\n    emit(doc.pool, null);\n}\n}",
           "reduce": "function(keys, values) {\n  return null;\n}"
        }
   }
}