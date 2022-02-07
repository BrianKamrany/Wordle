package games.wordle;

import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class ANSIConsole {
	private Scanner scanner;
	
	public void openConsole() {
		this.scanner = new Scanner(System.in);
	}

	public String nextLine() {
		return scanner.nextLine();
	}
	
	public void print(String output) {
		System.out.print(output);
	}

	public void printLines(int count) {
		for (int i = 0; i < count; i++) {
	        System.out.println();
		}
	}

	public void printLine(String output) {
		System.out.println(output);
		System.out.println();
	}

	public String toString(WordleCharacter character) {
		String output = character.toString();
		
		switch (character.getMatchState()) {
			case EXACT_MATCH:
				return green(output);
			case CONTAINED_IN:
				return yellow(output);
			case NOT_IN:
				return output;
			default:
				return output;
		}
	}
	
	private String green(String input) {
        return "\033[38;2;83;141;78m" + input + reset();
	}
	
	private String yellow(String input) {
        return "\033[38;2;181;159;59m" + input + reset();
	}
	
	private String reset() {
        return "\033[0m";
	}
}
