# StudySpace Finder System

Universities rely on scheduling systems to manage shared spaces. In this project, you will build StudySpace Finder, a system that assigns students to the nearest available study room that meets requested constraints.

Functional Requirements

Room Management
    •	Add rooms (ID, capacity, features)
    •	Search rooms
    •	Remove rooms

Reservation Scheduling
    •	Make reservation
    •	Cancel reservation
    •	Prevent conflicts

Nearest Room Routing
    •	Given student location + requirements
    •	Find nearest valid room
    •	Display route



Room records	Hash Map (roomID → Room)

Building lookup	Hash Map (buildingID → Building)

Reservation storage per room	Balanced BST / TreeMap or sorted list by time

Conflict detection	Interval overlap check (on sorted reservations)

“Next available room” selection	Priority Queue / Heap (earliest available start time)

Campus/building map	Weighted Graph (adjacency list)

Routing	Dijkstra’s Shortest Path Algorithm

