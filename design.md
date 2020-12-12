The program has three main classes:
1. Explorer -> the main class doing most of the work
2. Paper -> simple POJO for storing each paper's data
3. Main -> to get input from the user and work with explorer

Explorer class has one public method called nextTire which should get called from the main class in order to get the next tire of papers.
The first call to this method also reads the entire file in 5000-line chunks and starts processing them in parallel.
To keep the current working tire I have used a java ConcurrentHashMap for its thread-safe operations and to make finding a specific item faster(O(1)).
There is also another map called papersMap which simply maps each id to its associated paper
