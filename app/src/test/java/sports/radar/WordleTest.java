package sports.radar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.junit.jupiter.api.Test;

import sports.radar.game.Wordle;

public class WordleTest {
  private static final String GREEN = "\033[42m";
  private static final String YELLOW = "\033[43m";
  private static final String RESET = "\033[0m";
  private static final int WORD_LENGTH = 5;
  Wordle wordle = new Wordle();

  @Test
  public void extractWordsOK() {
    String multilineString = """
        clone
        saved
        torch
        quick
        black
        inbox""";
    BufferedReader reader = new BufferedReader(new StringReader(multilineString));
    List<String> result = wordle.extractWords(reader);
    assertTrue(result.size() == 6);
    assertTrue(result.containsAll(List.of("clone", "saved", "torch", "quick", "black", "inbox")));
    System.out.println(result);
  }

  @Test
  public void extractWordsEdgeCases() {
    String multilineString = """
        clon
        savede
        TORCH
        quick
        black
        inbox""";
    BufferedReader reader = new BufferedReader(new StringReader(multilineString));
    List<String> result = wordle.extractWords(reader);
    assertTrue(result.size() == 4);
    assertTrue(result.containsAll(List.of("torch", "quick", "black", "inbox")));
  }

  @Test
  public void testExactMatch() {
    wordle.setSecretWord("house");
    String guess = "house";
    String expected = GREEN + " h " + RESET + GREEN + " o " + RESET + GREEN + " u " + RESET + GREEN + " s " + RESET
        + GREEN + " e " + RESET;
    assertEquals(expected, wordle.generateOutput(guess));
  }

  @Test
  public void testPartialMatch() {
    wordle.setSecretWord("house");
    String guess = "hoyve";
    String expected = GREEN + " h " + RESET + GREEN + " o " + RESET + " y " + " v " + GREEN + " e " + RESET;
    assertEquals(expected, wordle.generateOutput(guess));
  }

  @Test
  public void testWrongOrder() {
    wordle.setSecretWord("house");
    String guess = "esouh";
    String expected = YELLOW + " e " + RESET + YELLOW + " s " + RESET + YELLOW + " o " + RESET + YELLOW + " u " + RESET
        + YELLOW + " h " + RESET;
    assertEquals(expected, wordle.generateOutput(guess));
  }

/**
 * Tests that the generateOutput method correctly identifies letters in the guess
 * that match letters in the secret word, including cases where there are multiple
 * occurrences of a letter in the guess but only one correct occurrence in the secret word.
 * Specifically, this test checks that the correct letters are marked with a green 
 * background, while the extra occurrences remain uncolored.
 */
  @Test
  public void testPartialMatchWithMultipleLettersInGuessIncludingCorrectOne() {
    wordle.setSecretWord("house");
    String guess = "hosss";
    String expected = GREEN + " h " + RESET + GREEN + " o " + RESET + " s " + GREEN + " s " + RESET + " s ";
    assertEquals(expected, wordle.generateOutput(guess));
  }

  /**
   * Tests that the generateOutput method correctly identifies letters in the guess
   * that match letters in the secret word, including cases where there are multiple
   * occurrences of a letter in the guess but only one correct occurrence in the secret word.
   * Specifically, this test checks that the correct letters are marked with a green 
   * background, while the extra occurrences remain uncolored.
   */
  @Test
  public void testPartialMatchWithMultipleLettersInGuess() {
    wordle.setSecretWord("house");
    String guess = "hosis";
    String expected = GREEN + " h " + RESET + GREEN + " o " + RESET + " s " + " i " + " s ";
    assertEquals(expected, wordle.generateOutput(guess));
  }

  @Test
  public void testNoMatch() {
    wordle.setSecretWord("house");
    String guess = "black";
    String expected = " b " + " l " + " a " + " c " + " k ";
    assertEquals(expected, wordle.generateOutput(guess));
  }

  @Test
  public void testIncorrectInput() {
    wordle.setSecretWord("house");
    String guess = "blacke";
    String expected = "Please enter a " + WORD_LENGTH + "-letter word.";

    assertEquals(expected, wordle.evaluateInput(guess));
  }

  @Test
  public void testNullInput() {
    wordle.setSecretWord("house");
    String guess = null;
    String expected = "Please enter a " + WORD_LENGTH + "-letter word.";

    assertEquals(expected, wordle.evaluateInput(guess));
  }

}
