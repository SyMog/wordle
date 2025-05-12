package sports.radar.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class Wordle {
  private final int WORD_LENGTH = 5;
  private static final int MAX_TRY = 5;
  private static final String RESET = "\u001B[0m";
  private static final String YELLOW = "\u001B[43m";
  private static final String GREEN = "\u001B[42m";

  private boolean isWon = false;
  private int remainingTry = MAX_TRY;
  private String secretWord;

  /**
   * Displays the introduction message for the Wordle game.
   * 
   * @return A formatted string containing the introduction message.
   */

  public String displayIntroduction() {
    return """
        ***********************************************
        Try to guess the 5-letter word.
        You have %d attempts.
        After each guess, you'll get feedback:
        - %s X %s : Exact position.
        - %s X %s : Letter in the word but in the wrong position.
        - No color : it's not in the word
        Good luck
        ***********************************************
        """.formatted(MAX_TRY, GREEN, RESET, YELLOW, RESET);
  }

  public void init() {
    Random random = new Random();
    secretWord = loadWordList().get(random.nextInt(loadWordList().size()));
  }

  /**
   * Read a file with a list of words, one word per line, from the classpath.
   * The file is expected to be named "wordlist.txt".
   * The words are converted to lower case and filtered to only include words with
   * exactly WORD_LENGTH characters.
   * If the file does not exist, an error message is printed and the program exits
   * with status 1.
   * 
   * @return a list of words.
   */
  public List<String> loadWordList() {
    List<String> wordList = new ArrayList<>();
    try (InputStream inputStream = Wordle.class.getClassLoader().getResourceAsStream("wordlist.txt")) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      wordList = extractWords(reader);
    } catch (IOException e) {
      System.err.println("Error creating while reading word list from file: " + e.getMessage());
      System.exit(1);
    }
    return wordList;
  }

  /**
   * Read a BufferedReader line by line, and return a list of words.
   * A word is a line that has exactly WORD_LENGTH characters.
   * The words are also converted to lower case.
   * This method on its own to facilitate testing.
   * 
   * @param reader the file to read.
   * @return a list of words.
   */
  public List<String> extractWords(BufferedReader reader) {
    List<String> wordList = reader.lines()
        .map(String::trim)
        .filter(line -> line.length() == WORD_LENGTH)
        .map(String::toLowerCase)
        .collect(Collectors.toList());
    return wordList;
  }

  /**
   * Evaluates the given guess against the secret word and updates the game state
   * accordingly.
   * 
   * @param guess a 5-letter word.
   * @return a message that either reports the success of the guess, or details
   *         the number of
   *         tries left.
   */
  public String evaluateInput(String guess) {
    if (guess == null || guess.length() != WORD_LENGTH) {
      return "Please enter a " + WORD_LENGTH + "-letter word.";
    }
    guess = guess.toLowerCase();
    remainingTry--;

    if (guess.equals(secretWord)) {
      isWon = true;
      return "Congratulations ! You guessed the word correctly !";
    }

    String output = generateOutput(guess);
    return output + "\n You have " + remainingTry + " tries left.";
  }

  /**
   * Given a guess, this function generates a colored string which represents the
   * similarity between the guess and the secret word.
   * 
   * The string is colored in the following way:
   * - A green background ({@code YELLOW}) for a letter which is in the correct
   * position in the secret word.
   * - A yellow background ({@code YELLOW}) for a letter which is in the secret
   * word
   * but in the wrong position.
   * - No background for a letter which is not in the secret word.
   * 
   * @param guess a 5-letter word.
   * @return a colored string which represents the similarity between the guess
   *         and
   *         the secret word.
   */
  public String generateOutput(String guess) {
    boolean[] exactMatches = new boolean[guess.length()];
    Map<Character, Integer> secretLetterOccurences = new HashMap<>();
    Map<Character, Integer> guessLetterOccurences = new HashMap<>();

    for (char c : secretWord.toCharArray()) {
      secretLetterOccurences.merge(c, 1, Integer::sum);
    }

    for (char c : guess.toCharArray()) {
      guessLetterOccurences.merge(c, 1, Integer::sum);
    }

    for (int i = 0; i < guess.length(); i++) {
      if (guess.charAt(i) == secretWord.charAt(i)) {
        exactMatches[i] = true;
        secretLetterOccurences.compute(guess.charAt(i), (k, v) -> v - 1);
      }
    }

    StringBuilder output = new StringBuilder();
    for (int i = 0; i < guess.length(); i++) {
      char c = guess.charAt(i);
      if (exactMatches[i]) {
        output.append(GREEN).append(" ").append(c).append(" ").append(RESET);
      } else if (secretLetterOccurences.getOrDefault(c, 0) > 0) {
        if (guessLetterOccurences.get(c) == secretLetterOccurences.get(c)) {
          output.append(YELLOW).append(" ").append(c).append(" ").append(RESET);
          secretLetterOccurences.compute(c, (k, v) -> v - 1);
        } else {
          output.append(" ").append(c).append(" ");
        }
      } else {
        output.append(" ").append(c).append(" ");
      }
    }

    return output.toString();
  }
}
