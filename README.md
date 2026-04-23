# StudySpace Finder System

# Divison of Labor
Harmony Barber

RoomManager.java — HashMap-based room storage (add, search, remove, display)
RoomManagementApp.java — Standalone room management CLI
Room.java (initial version) — Room entity model

Chase

CampusGraph.java — Weighted undirected graph using adjacency list
Edge.java — Graph edge model (destination + weight)
RoomCandidate.java — Helper class for priority queue routing

Najhier

CliApp.java — Main unified CLI tying all modules together
StudySpaceService.java — Room and building management service
RoutingService.java — Dijkstra's shortest path algorithm
Building.java, Reservation.java, StudentRequest.java — Model classes
Integration of all components into the final package system

Copernic

ReservationTree.java — AVL BST for reservation scheduling
ReservationService.java — Reservation make/cancel/conflict detection
Interval overlap conflict detection logic
Next-available-time calculation

# Project Structure

studyspace/
├── app/
│   └── CliApp.java              — Main CLI entry point
├── model/
│   ├── Building.java            — Building entity
│   ├── Reservation.java         — Reservation entity
│   ├── Room.java                — Room entity (holds TreeMap of reservations)
│   └── StudentRequest.java      — Encapsulates a student's search criteria
├── services/
│   ├── ReservationService.java  — Conflict detection, make/cancel reservation
│   ├── RoutingService.java      — Dijkstra's shortest path on campus graph
│   └── StudySpaceService.java   — Room/building management + nearest-room search
└── structures/
    ├── CampusGraph.java         — Weighted undirected graph (adjacency list)
    ├── Edge.java                — Graph edge (destination + weight)
    ├── ReservationTree.java     — AVL BST for cross-room reservation storage
    └── RoomCandidate.java       — (room, distance) pair used in priority queue


# Build Instructions

Step 1 — Navigate into the project folder:
bashcd StudySpaceFinder

Step 2 — Compile:
bashjavac studyspace/model/*.java studyspace/structures/*.java studyspace/services/*.java studyspace/app/*.java

Step 3 — Run:
bashjava studyspace.app.CliApp
That's it. Sample data loads automatically when it starts up.
