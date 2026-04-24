# StudySpace Finder System

# Description

StudySpace Finder is a command-line application that helps students find and reserve study rooms on campus.

Users can manage buildings and rooms, make and cancel reservations with automatic conflict detection, and find the nearest available room matching their capacity, features, and time slot using Dijkstra's shortest path algorithm.


# Build and Run Instructions

cd StudySpaceFinder

javac studyspace/model/*.java studyspace/structures/*.java studyspace/services/*.java studyspace/app/*.java

java studyspace.app.CliApp

### Note

*/data contains sample csv data*
*time must be inputted as hh:mm example: 09:30 AM