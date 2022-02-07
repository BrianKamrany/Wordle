package games.wordle;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DictionaryTest {
	@Inject private Dictionary dictionary;
	
	@Before
	public void initializeMethod() throws Exception {
		dictionary.load();
	}

	@Test
	public void isValidGuess() throws Exception {
		assertTrue(dictionary.isValidGuess("aahed"));
		assertTrue(dictionary.isValidGuess("dream"));
		assertFalse(dictionary.isValidGuess("hubra"));
	}
}
