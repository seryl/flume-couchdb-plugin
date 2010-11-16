package couchdb;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import jregex.*;

import org.ektorp.*;
import org.ektorp.impl.*;
import org.ektorp.http.*;
import org.apache.log4j.Logger;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory.SinkBuilder;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.reporter.ReportEvent;
import com.cloudera.util.Pair;

public class CouchDBSink extends EventSink.Base {
  static Logger logger = Logger.getLogger(CouchDBSink.class);

  private String host;
  private int port;
  private String database;
  private HttpClient httpClient;
  private CouchDbInstance dbInstance;
  private CouchDbConnector db;

  private Pattern regexPattern;
  private Matcher matcher;

  private SimpleDateFormat dateformatter;

  public CouchDBSink(String hostAddress, int hostPort, String hostDatabase,
          String inputRegex) {
      host = hostAddress;
      port = hostPort;
      database = hostDatabase;

      regexPattern = new Pattern(inputRegex, "s");
      matcher = regexPattern.matcher();
      dateformatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
  }
  
  @Override
  public void open() throws IOException {
      httpClient = new StdHttpClient.Builder()
          .host(host)
          .port(port)
          .build();

      dbInstance = new StdCouchDbInstance(httpClient);
      db = new StdCouchDbConnector(database, dbInstance);
      db.createDatabaseIfNotExists();
  }

  /**
   * @param e Event which has been sent by Flume Source
   * @throws IOException
   */
  @Override
  public void append(Event e) throws IOException {
    Map<String, Object> referenceData = new HashMap<String, Object>();
    Date date = new Date(e.getTimestamp());
    String timestamp = dateformatter.format(date);
    String message = new String(e.getBody());

    matcher.setTarget(message);
    matcher.find();

    referenceData.put("timestamp", timestamp);
    for (String group : matcher.groupNames()) {
        referenceData.put(group, matcher.group(group));
    }

    db.create(referenceData);
  }

  /**
   * Close the connection to CouchDB
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    if (db != null) {
      logger.debug("CouchDB sink is shutting down...");
      db = null;
    }
  }

  /**
   * Construct a new parameterized sink
   * @return CouchDBSink
   */
  public static SinkBuilder builder() {
    return new SinkBuilder() {
      
      @Override
      public EventSink build(Context context, String... argv) {
        if (argv.length != 5) {
          throw new IllegalArgumentException(
              "usage: couchdbSink(\"hostname\", port, \"database\", \"regexString\") ");
        }

        String hostname = argv[0];
        int hostPort = Integer.parseInt(argv[1]);
        String hostDatabase = argv[2];
        String regexString = argv[3];

        return new CouchDBSink(hostname, hostPort, hostDatabase,
                regexString);
      }
    };
  }

  /**
   * This is a special function used by the SourceFactory to pull in this class
   * as a plugin sink.
   */
  public static List<Pair<String, SinkBuilder>> getSinkBuilders() {
    List<Pair<String, SinkBuilder>> builders =
      new ArrayList<Pair<String, SinkBuilder>>();
    builders.add(new Pair<String, SinkBuilder>("couchdbSink", builder()));
    return builders;
  }

}