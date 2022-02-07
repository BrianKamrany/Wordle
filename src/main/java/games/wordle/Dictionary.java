package games.wordle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class Dictionary {
	private boolean loaded;
	private List<String> words;
	private List<String> valid;

	public void load() throws IOException {
		if (loaded)
			return;
		
		this.words = readLines("words.txt");
		this.valid = readLines("valid.txt");
		this.valid.addAll(words);
		this.loaded = true;
	}

	public String getRandomWord() {
		Random random = new Random();
	    return words.get(random.nextInt(words.size()));
	}

	public boolean isValidGuess(String input) {
		return valid.contains(input);
	}

	private List<String> readLines(String resourcePath) throws IOException {
		try (InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
			return IOUtils.readLines(resourceStream, StandardCharsets.UTF_8);
		}
	}
}
