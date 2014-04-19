jeopardy-javafx
===============

Implementation of the famous Jeopardy game show in JavaFX (inspired by Hacker Jeopardy)

Requirements
------------

* JDK 1.8 (http://www.oracle.com/technetwork/java/javase/downloads/index.html)

Preparation
-----------

* `cp nbproject/project.properties.sample nbproject/project.properties`
* Change `javafx.param.0.name=/Users/ferdi/projects/jeopardy/pony-jeopardy/round1.yml` in `nbproject/project.properties` to the path of your round YAML file.

Run
---
`ant run`

Jeopardy Theme Music
--------------------

As the Jeopardy theme music is probably protected (as is the trade mark, most likely :worried:), we do not include the Jeopardy theme music in this project. We do however support playing it back if it happens to lie around as `src/com/nerdinand/jeopardy/media/jeopardy.mp3` in your source tree... :wink:
