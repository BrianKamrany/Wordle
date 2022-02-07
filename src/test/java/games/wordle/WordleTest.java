package games.wordle;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WordleTest {
	@Inject private Wordle wordle;
	@Inject private Dictionary dictionary;
	
	@Before
	public void initializeMethod() throws Exception {
		dictionary.load();
	}

	@Test
	public void makeGuess_AllExact() throws Exception {
		wordle.setWord("abcde");
		List<WordleCharacter> results = wordle.makeGuess("abcde");
		assertCount(5, MatchState.EXACT_MATCH, results);
	}

	@Test
	public void makeGuess_AllExact_Repeats() throws Exception {
		wordle.setWord("aabaa");
		List<WordleCharacter> results = wordle.makeGuess("aabaa");
		assertCount(5, MatchState.EXACT_MATCH, results);
	}

	@Test
	public void makeGuess_Exact() throws Exception {
		wordle.setWord("abcde");
		List<WordleCharacter> results = wordle.makeGuess("axcxe");
		assertCount(3, MatchState.EXACT_MATCH, results);
	}

	@Test
	public void makeGuess_Exact_Repeats() throws Exception {
		wordle.setWord("aabbc");
		List<WordleCharacter> results = wordle.makeGuess("axbbc");
		assertCount(4, MatchState.EXACT_MATCH, results);
	}

	@Test
	public void makeGuess_NoneExact() throws Exception {
		wordle.setWord("abcde");
		List<WordleCharacter> results = wordle.makeGuess("fghij");
		assertCount(0, MatchState.EXACT_MATCH, results);
	}

	@Test
	public void makeGuess_ContainedIn() throws Exception {
		wordle.setWord("abcde");
		List<WordleCharacter> results = wordle.makeGuess("xaxcx");
		assertCount(2, MatchState.CONTAINED_IN, results);
	}

	@Test
	public void makeGuess_ContainedIn_RepeatsInWord() throws Exception {
		wordle.setWord("ooxxx");
		List<WordleCharacter> results = wordle.makeGuess("xooxx");
		assertState(MatchState.CONTAINED_IN, 3, results);
	}

	@Test
	public void makeGuess_ContainedIn_TripleRepeatsInWord() throws Exception {
		wordle.setWord("oxxoo");
		List<WordleCharacter> results = wordle.makeGuess("oooxx");
		assertState(MatchState.CONTAINED_IN, 2, results);
		assertState(MatchState.CONTAINED_IN, 3, results);
	}

	@Test
	public void makeGuess_NotIn() throws Exception {
		wordle.setWord("abcde");
		List<WordleCharacter> results = wordle.makeGuess("xxzxx");
		assertState(MatchState.NOT_IN, 3, results);
	}

	@Test
	public void makeGuess_NotIn_RepeatsInGuess() throws Exception {
		wordle.setWord("oxxxx");
		List<WordleCharacter> results = wordle.makeGuess("oxoxx");
		assertState(MatchState.NOT_IN, 3, results);
	}

	@Test
	public void makeGuess_NotIn_TripleRepeatsInGuess() throws Exception {
		wordle.setWord("ooxxx");
		List<WordleCharacter> results = wordle.makeGuess("oxxoo");
		assertState(MatchState.NOT_IN, 5, results);
	}
	
	private static void assertCount(long count, MatchState matchState, List<WordleCharacter> results) {
		assertEquals(count, results.stream().filter(character -> character.getMatchState() == matchState).count());
	}
	
	private static void assertState(MatchState matchState, int characterOneIndex, List<WordleCharacter> results) {
		assertTrue(matchState == results.get(characterOneIndex - 1).getMatchState());
		
	}
}
