package games.wordle;

import lombok.Data;

@Data
public class WordleCharacter {
	private char character;
	private MatchState matchState;

	public char getChar() {
		return character;
	}

	public void setChar(char character) {
		this.character = character;
	}
	
	@Override
	public String toString() {
		return String.valueOf(character).toUpperCase();
	}
}
