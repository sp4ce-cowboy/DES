# Discrete Event Simulator
An archive of my discrete event simulator project for [CS2030 Programming Methodology II](https://nusmods.com/courses/CS2030/programming-methodology-ii) taken in NUS in AY22/23 Semester 2 

### A Brief Description
A Discrete Event Simulator that simulates the sequential generation and execution of discrete events in the context of a shop that contains servers and self-checkout counters that serve customers that arrive at specified times and have unspecified service times. A customer that arrives will be served by either a server or a self-checkout counter if available. If the customer cannot be served immediately, they wait in a queue of specified length. If they are unable to wait, they leave the shop.

A customer's arrival triggers a sequence of events that simulate a real-world scenario. A customer can arrive, then be served, wait, or leave. Some time later the customer would be done serving by the server or a self-checkout counter. A human server would then take a break for an unspecified duration of time, but a self-checkout counter does not need to rest.

All events generate another event which, if not the same event, is added to an immutable implementation of a PriorityQueue, PQ. Events are polled and the relevant information is contained within a String that is printed line by line by the Main class. The average waiting time per customer, number of customers served, and the number of customers who leave without being served is output as the final line.

### Some Notes

This project was completed as part of the course requirement for [CS2030](https://nusmods.com/courses/CS2030/programming-methodology-ii). A proper introduction to the OOP paradigm. As per the course specifications, the process of iterating until this final deliverable would have taken about 10KLoC, including the final deliverable which is around 2KLoC. 

Every single line of Java code here was written in Vim and Vim only.

In promoting effect-free programming and immutability, an immutable delegation of the `ArrayList` class called `ImList` is used throughout the codebase. Only the main method outputs any information explicitly to the console, all other classes and methods return non-void values. Setter methods are banned and getter methods are discouraged. 

### Usage

This repo is an attempt at ~showcasing~ archiving some of my old work. It wouldn't exactly make sense to "use" it.

Nonetheless, for the determined:

1. Download all files inside the [99_FinalProject](99_FinalProject) folder.

2. Compile the files with:

```sh
javac *.java
```

3. Copy over the `test.in` files from the [tests](99_FinalProject/tests) folder.

4. Run the [main](99_FinalProject/Main.java) class with text redirection:

```sh
java Main < test1.in
```

5. Ponder about what the confusing output could mean.

### Docs

(Almost) All classes and methods have [documentation](https://sp4ce-cowboy.github.io/DES/package-summary.html), generated with javadocs, hosted with GitHub pages.

All code was written in and tested only with JDK17.
