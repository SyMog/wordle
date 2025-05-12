package sports.radar;

import java.util.Scanner;

import sports.radar.game.Wordle;

public class App {

    public static void main(String[] args) {
        // Create a new game of Wordle
        Wordle game = new Wordle();

        // Initialize the game by selecting a random word for the secret word
        game.init();

        // Print the introduction, which includes instructions and a welcome message
        System.out.println(game.displayIntroduction());

        Scanner scanner = new Scanner(System.in);

        while (!game.isWon()) {
            // Check if the user has used up all of their guesses
            if (game.getRemainingTry() == 0) {
                System.out.println("The word was: " + game.getSecretWord());
                scanner.close();
                System.exit(1);
            }

            System.out.print("\nEnter your guess: ");

            String guess = scanner.nextLine().trim();

            if (guess.length() != game.getWORD_LENGTH()) {
                System.out.println("Please enter a " + game.getWORD_LENGTH() + "-letter word.");
                continue;
            }

            String output = game.evaluateInput(guess);
            System.out.println(output);
        }
    }
}
