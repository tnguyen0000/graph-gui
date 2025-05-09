# Graph-GUI

Simple GUI app used to create and visualise weighted, directed graphs.\
GUI was made in Java through the [Swing library](https://docs.oracle.com/javase/8/docs/api/index.html?javax/swing/package-summary.html) and graph visualisation was implemented through the external [JUNG library](https://jung.sourceforge.net/).

## How to run / set up (Using WSL2):
Ensure you have Java installed and git clone the project.
1. From the project root directory, run the './gradlew build' command in console. This will create a 'app.jar' file in '/graph-gui/app/build/libs/'
2. Run the 'java -jar app/build/libs/app.jar' in console. This should start up the app.

#### Currently, there are only three algorithms implemented:
1. Kahn (Topological sort),
2. Dijkstra (Shortest path from given vertex),
3. Ford-Fulkerson (Max flow given source and sink vertices).
