# Wordle
## What is this ? 
- You have five guesses
- All words are 5 length
- The guesser gets the feedback about any letters in their guess that are in the right
position with a green highlight, while letters that are in the word but not in the
correct position will get a yellow outline.
- The yellow highlight will not show up if you have more of a letter than in the correct
answer.


## How to run 
If you're using windows, use gradlew.bat !
 
You can generate the jar from sources at the root with: 
```
./gradlew jar
```
It will be generated in : /app/build/libs

You can run the jar like so : 
```
java -jar app.jar
```

You can run the unit tests with :
```
./gradlew test
```
