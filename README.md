# Amazon Warehouse Simulator

To improve the efficiency of packing orders at the Amazon Distribution Centers, they invented and utilise the Kiva robots. This is a simulation of these robots within a virtual warehouse of entities. In turn the actors each *tick* the complete the orders given to the simulation. 

## Background

This application was originally build as a coursework submission for my Java Program Construct module at [Aston University](https://www2.aston.ac.uk/study/courses/digital-and-technology-solutions-degree-apprenticeship). This was a team project build with [Jordan](https://github.com/JBamBam) and [Jess](https://github.com/morganjess). 

The task was to build a simulation in Java for the Amazon Kieva robots which aid human packers in shipping all our Amazon orders efficiently. The warehouse consists of a 2D grid of tiles of which either a packing station, storage shelf, or charging pod reside. You can imagine these on stilts allows robots to travel underneath to carry out their tasks. These four are known as entities.

The warehouse simulator is initiated with a collection of entities, a queue of orders, and a warehouse floor with the task to *tick* through each actor (a subset of entities) in sequence to complete all the orders.

With permission from my lecturer, I am now making our solution open-source to trial different strategies, add features, and practice design patterns taught within the course.

## Simulation Rules

1. The robot has a given quanity of power units (charge). On every move, the robot must **deduct 1** and additionally **another 1** if the robot is carrying. 
2. If the robot tries to move with 0 power units, the simulation fails.
3. If the robot collides into another robot, the simulation fails.
4. The packing station can only handle one order at a time. Once assigned an order, it must collect items from all required shelves and tick through the picks to pack (to simulate the human packing time)
5. Robots can peek at the next job in the queue but will only pick it up if it sufficent charge to complete the job.


## Usage

Currently there is no formal build and release process with an artifact to run. The cloned repository can be built using Maven and executed as followed:

```bash
java amazon.App <sim file location>
```

## Future Features

1. Additional Path Finding strategies.
2. Non-queue based Item Manager (location based for robots and jobs)
3. Update to the simulation implementation to lift and move a storage shelf to next to the packing station. 
4. Bring back to UI  
