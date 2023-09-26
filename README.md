# Discrete Event Simulator
An archive of my discrete event simulator project for CS2030 taken in AY22/23 Semester 2 

### A Brief Description
A Discrete Event Simulator that simulates the sequential generation and execution of discrete events in the context of a shop that contains servers and self-checkout counters that serve customers that arrive at specified times and have unspecified service times. A customer that arrives will be served by either a server or a self-checkout counter if available. If the customer cannot be served immediately, they wait in a queue of specified length. If they are unable to wait, they leave the shop.
A customer's arrival triggers a sequence of events that simulate a real-world scenario. A customer can arrive, then be served, wait, or leave. Some time later the customer would be done serving by the server or a self-checkout counter. A human server would then take a break for an unspecified duration of time, but a self-checkout counter does not need to rest.

All events generate another event which, if not the same event, is added to an immutable implementation of a PriorityQueue, PQ. Events are polled and the relevant information is contained within a String that is printed line by line by the Main class. The average waiting time per customer, number of customers served, and the number of customers who leave without being served is output as the final line.
