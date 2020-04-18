Logback Loki Appender
===============================

Send log events directly from Logback to Loki. Logs are delivered asynchronously (i.e. not on the main thread) so will not block execution of the program. Note that the queue backlog can be bounded and messages *can* be lost if Loki is down and either the backlog queue is full or the producer program is trying to exit. For long-lived programs, this should not be a problem, as messages should be delivered eventually.

Usage
=====
Include slf4j and logback as usual (depending on this library will *not* automatically pull them in).

In your `pom.xml` (or equivalent), add:

     <dependency>
        <groupId>com.lotfy</groupId>
        <artifactId>loki-logback-appender</artifactId>
        <version>1.0</version>
     </dependency>
     
     <repositories>
         <repository>
             <id>loki.read</id>
             <url>https://mymavenrepo.com/repo/jAdYcoPJW46SYQr459hM/</url>
         </repository>
     </repositories>


In your `logback.xml`:

        <appender name="Loki" class="com.lotfy.logback.loki.LokiAppender">
              <lokiUrl>http://loki:3100</lokiUrl>
              <label>my-application</label> <!-- this will result in {app="my-application"} label in Loki-->
              <enabled>true</enabled>
        </appender>


Configuration Reference
=======================

 * `lokiUrl` (required): The URL to your Loki push API endpoint
 * `label` (required): name of your application to add this label in Loki as `app={labelValue}`
 * `enabled` (required): if you have multiple environments you may need to disable appender for some of them.
 
