# COMP3506 Assignment 2

## Introduction

Second assignment for Data Structure and Algorithm. A search engine implemented by Java. All data structures were implemented from scretch including:

- ```Entry``` (for HashMap)
- ```Node``` (for LinkedList)
- ```LinkedList```
- ```HashMap```
- ```Set```

Content in .txt files will be divided into single word and stored with its position as key-value pairs in ```bucketTable``` that is an array of Entries. 

```java
private NewMap<String, MySet<Triple<Integer, Integer, String>>> wordMap;
```

## Other Solution

Obviously, only using ```HashMap``` is not an ideal solution. Another solution is implementing a ```Trie``` . And each leaf node connect with a list of positions of the corresponding word, i.e. inverted index.