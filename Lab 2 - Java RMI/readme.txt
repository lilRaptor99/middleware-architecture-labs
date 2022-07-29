Q: How can you ensure the thread safety of the increment count function, which updates a shared variable?
A: By adding the synchronized keyword to the newly added method (clientCount method), java will run the function synchronized across all threads. This ensures the thread safety of the client count variable.

Q: What does the count increment feature say about the Server object’s instantiation method? 
A: It seems to be per client object instantiation.

Q: Briefly explain how can you make this a Per client or Per call instantiation? 
A: We can have multiple server objects registered in the registry and the clients can call multiple server object instances.
