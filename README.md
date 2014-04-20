jeopardy-javafx
===============

Implementation of the famous Jeopardy game show in JavaFX (inspired by Hacker Jeopardy)

Requirements
------------

* JDK 1.8 (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

Run
---
`./gradlew run '-Pargs=/path/to/your/round.yml'`

Development
-----------

To import the project into NetBeans, you should install the Gradle Plugin (http://plugins.netbeans.org/plugin/44510/gradle-support) and then open the project via File -> Open Project... . To be able to run the project from within NetBeans, create a custom Profile in the Project Properties, and change the built-in Task "Run" to include the argument `-Pargs=/path/to/your/round.yml`.

Jeopardy Theme Music
--------------------

As the Jeopardy theme music is probably protected (as is the trade mark, most likely :worried:), we do not include the Jeopardy theme music in this project. We do however support playing it back if it happens to lie around as `src/main/resources/com/nerdinand/jeopardy/media/jeopardy.mp3` in your source tree... :wink:
