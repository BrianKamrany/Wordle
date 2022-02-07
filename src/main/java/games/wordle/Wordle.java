package games.wordle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class Wordle {
	@Inject private Dictionary dictionary;
	@Inject private ANSIConsole console;
	
	private String word;
	
	public void startGame() throws IOException {
		dictionary.load();
		chooseWord();
		printPrompt();
		console.openConsole();
		
		for (int i = 0; i < 6; i++) {
			String input = getUserInput();
		    List<WordleCharacter> results = makeGuess(input);
			printGuess(results);
			if (isWordGuessed(input)) {
				console.print("Impressive");
				return;
			}
		}

		console.print("The word was " + word.toUpperCase() + ".");
	}

	private void chooseWord() {
		if (StringUtils.isBlank(word)) {
		    this.word = dictionary.getRandomWord();
		}
	}
	
	private void printPrompt() {
		for (int i = 0; i < 5; i++) {
			console.print("_" + " ");
		}

		console.printLines(2);
	}

	private String getUserInput() {
		String validInput = null;
		
		while (StringUtils.isEmpty(validInput)) {
			String input = console.nextLine();
			
			if (StringUtils.isBlank(input)) {
				console.printLine("Guess is nothing.");
				continue;
			}
			
			input = input.replaceAll("\\s", "").toLowerCase();
			if (input.length() != 5) {
				console.printLine("Guess is not 5 letters.");
				continue;
			}
			
			if (!dictionary.isValidGuess(input)) {
				console.printLine("Guess is not a word.");
				continue;
			}
			
			validInput = input;
		}
		
		return validInput;
	}

	protected List<WordleCharacter> makeGuess(String guess) {
		List<WordleCharacter> results = new ArrayList<>();
		for (int i = 0; i < guess.length(); i++) {
			WordleCharacter character = new WordleCharacter();
			character.setChar(guess.charAt(i));
			character.setMatchState(MatchState.NOT_IN);
			results.add(character);
		}
		
		Map<Character, Integer> frequencies = toFrequencies(word);
		for (int i = 0; i < results.size(); i++) {
			WordleCharacter character = results.get(i);
			boolean exactMatch = word.charAt(i) == character.getChar();
			if (exactMatch) {
				character.setMatchState(MatchState.EXACT_MATCH);
				frequencies.put(character.getChar(), frequencies.get(character.getChar()) - 1);
			}
		}
		
		for (int i = 0; i < results.size(); i++) {
			WordleCharacter character = results.get(i);
			if (character.getMatchState() == MatchState.EXACT_MATCH) {
				continue;
			}
			
			if (frequencies.containsKey(character.getChar()) && frequencies.get(character.getChar()) > 0) {
				character.setMatchState(MatchState.CONTAINED_IN);
				frequencies.put(character.getChar(), frequencies.get(character.getChar()) - 1);
			}
		}
		
		return results;
	}

	private void printGuess(List<WordleCharacter> wordleCharacters) {
		for (WordleCharacter character : wordleCharacters) {
			console.print(console.toString(character) + " ");
		}
		console.printLines(2);
	}

	private boolean isWordGuessed(String input) {
		return input.equals(word);
	}

	private Map<Character, Integer> toFrequencies(String input) {
		Map<Character, Integer> frequencies = new HashMap<>();
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			frequencies.put(character, frequencies.getOrDefault(character, 0) + 1);
		}
		return frequencies;
	}
}
