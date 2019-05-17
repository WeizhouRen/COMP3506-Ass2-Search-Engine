package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.IOException;

import comp3506.assn2.utils.*;

/**
 * My data structure which combined by NewMap<K, V> and MySet<E>
 * 
 * Memory Usage: 0.16GB I use Activity Monitor in mac to check the memory
 * usage, memory used is 3.29GB before launching the program, but it turn to
 * 3.45GB during the program is running.
 * 
 * @author Weizhou Ren
 *
 */
public class MyStructure {

	private BufferedReader documentBr;
	private BufferedReader indexBr;
	private BufferedReader stopWordsBr;

	// Main structure to store every word as key, and its positions as value
	private NewMap<String, MySet<Triple<Integer, Integer, String>>> wordMap;
	// Store the words in every line
	private NewMap<Integer, String[]> rowMap;
	// store the row number and its corresponding title
	private NewMap<Integer, String> indexMap;
	// store all stop words
	private MySet<String> stopSet;
	// store the position and its corresponding title
	private NewMap<Triple<Integer, Integer, String>, String> simpleAndSection;
	private NewMap<Triple<Integer, Integer, String>, String> simpleOrSection;

	public MyStructure(BufferedReader documentBr, BufferedReader indexBr, BufferedReader stopWordsBr) {
		// store file and setup my application
		wordMap = new NewMap<>(1000301);
		indexMap = new NewMap<>(51);
		stopSet = new MySet<>();
		rowMap = new NewMap<>(150001);

		this.documentBr = documentBr;
		this.indexBr = indexBr;
		this.stopWordsBr = stopWordsBr;

		simpleAndSection = new NewMap<>(3);
		simpleOrSection = new NewMap<>(3);
	}

	public void storeDocument() {

		String line;
		int row = 0;
		String indexName = null;
		try {
			while ((line = documentBr.readLine()) != null) {
				row++; // row count from 1
				String everyLine = line.trim();
				line = line.toLowerCase(); // convert to lower case

				if (everyLine.length() > 0) { // is not space line, put into the map
					int column = 0;
					if (getIndex().containsKey(row)) { // this this a start row of an index
						indexName = getIndex().get(row).toLowerCase(); // update index name
					}

					// find first letter in line
					boolean hasFoundFirst = false;
					Triple<Integer, Integer, String> firstPos = new Triple<>(row, column, indexName);
					while (hasFoundFirst == false && column < line.length()) {
						if ((line.charAt(column) >= 'a' && line.charAt(column) <= 'z')) {
							firstPos.setCentreValue(column + 1);
							hasFoundFirst = true;
						} else {
							column++;
						}
					}

					if (hasFoundFirst == false) {
						continue;
					}
					String[] wordsInLine = line.split(" ");

					int wordsCount = 0; // number of words

					for (int i = 0; i < wordsInLine.length; i++) {
						if (wordsInLine[i].length() > 0) {
							wordsCount++;
						}
					}

					@SuppressWarnings("unchecked")
					Triple<Integer, Integer, String>[] posArray = new Triple[wordsCount]; // store all positions in a
																							// line
					posArray[0] = firstPos;

					int firstLetterIndex = firstPos.getCentreValue() - 1; // iterate from the first letter
					int count = 1; // index in posArray

					for (int i = firstLetterIndex; i < line.length() - 1 && line.length() != 0; i++) {
						if ( /*
								 * line.charAt(i) == ' ' && ((line.charAt(i + 1) >= 'a' && line.charAt(i + 1) <=
								 * 'z') || (line.charAt(i + 1) >= 48 && line.charAt(i + 1) <= 57) ||
								 * line.charAt(i + 1) == 39)
								 */
						(line.charAt(i) == ' ') && ((line.charAt(i + 1) >= 'a' && line.charAt(i + 1) <= 'z')
								|| line.charAt(i + 1) == 39)) {

							Triple<Integer, Integer, String> position;
							position = new Triple<>(row, i + 2, indexName);
							posArray[count] = position;
							count++;
						}
					}

					int countPos = 0;
					String[] validWord = new String[wordsCount];
					for (int i = 0; i < wordsInLine.length; i++) {
						if (wordsInLine[i].length() != 0) {
							@SuppressWarnings("unused")
							boolean flag = false;
							for (int j = 0; j < wordsInLine[i].length(); j++) {
								if (wordsInLine[i].charAt(j) >= 'a' && wordsInLine[i].charAt(j) <= 'z') {
									flag = true;
									break;
								}
							}
							validWord[countPos] = wordsInLine[i];
							countPos++;
						}
					}
					// Setup rowMap
					rowMap.put(row, validWord);

					// setup wordsMap
					for (int i = 0; i < wordsCount; i++) {

						validWord[i] = ignoreStartOrEnd(validWord[i]);

						if (wordMap.containsKey(validWord[i])) {
							wordMap.get(validWord[i]).add(posArray[i]);
						} else {
							MySet<Triple<Integer, Integer, String>> posSet = new MySet<>();
							posSet.add(posArray[i]);
							wordMap.put(validWord[i], posSet);
						}

					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private NewMap<Integer, String> getIndex() {
		String IndexLine; // chapter + line number
		@SuppressWarnings("unused")
		int row = 0;
		try {
			while ((IndexLine = indexBr.readLine()) != null) {
				row++;
				String[] index = IndexLine.split(",");
				// index[0] is CHAPTER name String as key
				// index[1] is line number Integer as value
				indexMap.put(Integer.parseInt(index[1]), index[0]);
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		return indexMap;
	}

	/**
	 * Store all stop word in to a set O(n): n is the number of stop words
	 */
	public void storeStopWords() {
		String stopWord;
		try {
			while ((stopWord = stopWordsBr.readLine()) != null) {
				stopSet.add(stopWord);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Slice the word which has other character at begin or end
	 * 
	 * @param word the word need to be sliced
	 * @return valid words
	 */
	private String ignoreStartOrEnd(String word) {
		if (word.length() != 1) {
			if (!(word.charAt(0) >= 'a' && word.charAt(0) <= 'z')
					&& !(word.charAt(0) >= '0' && word.charAt(0) <= '9')) {
				word = word.substring(1);
			}
			if (!(word.charAt(word.length() - 1) >= 'a' && word.charAt(word.length() - 1) <= 'z')
					&& !(word.charAt(word.length() - 1) >= '0' && word.charAt(word.length() - 1) <= '9')) {
				word = word.substring(0, word.length() - 1);
			}
		}
		return word;
	}

	/**
	 * Determines the number of times the word appears in the document.
	 * 
	 * O(1) as expected, because it is searched by the hashCode of word O(n) is
	 * worst case because of the capacity may be too small
	 * 
	 * @param word The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 * @throws IllegalArgumentException if word is null or an empty String.
	 */
	public int wordCount(String word) throws IllegalArgumentException {
		if (!stopSet.contains(word)) {
			return wordMap.get(word.toLowerCase()).size();
		} else {
			return 0;
		}
	}

	/**
	 * Finds all occurrences of the phrase in the document. A phrase may be a single
	 * word or a sequence of words.
	 * 
	 * Idea: Save all position sets of every words in phrase into an array, compare
	 * current word with previous word, if they are continues, store in result set,
	 * otherwise eliminate the corresponding position sets.
	 * 
	 * O(n m p + (n-1)) n is number of words of phrase; m is size of totalPos[i]; p
	 * is size of totalPos[i-1].
	 * 
	 * @param phrase The phrase to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number
	 *         of each occurrence of the phrase. Returns an empty list if the phrase
	 *         is not found in the document.
	 * @throws IllegalArgumentException if phrase is null or an empty String.
	 */
	public MySet<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
		if (phrase == null || phrase.length() == 0) {
			throw new IllegalArgumentException();
		}
		MySet<Pair<Integer, Integer>> result = new MySet<>();
		phrase = phrase.toLowerCase();
		phrase = phrase.trim();
		String[] phraseWords = phrase.split(" ");
		@SuppressWarnings("unchecked")
		// Storing every position of all phrase words
		MySet<Triple<Integer, Integer, String>>[] totalPos = new MySet[phraseWords.length];

		for (int i = 0; i < phraseWords.length; i++) {
			totalPos[i] = new MySet<>(); // a set to store all positions of phraseWords[i]
			if (wordMap.containsKey(phraseWords[i])) {
				for (Triple<Integer, Integer, String> triple : wordMap.get(phraseWords[i])) {
					totalPos[i].add(triple);
				}
			}
			if (i != 0) { // is not the first words in phrase
				for (Triple<Integer, Integer, String> t : totalPos[i]) {
					boolean flag = false;
					// compare to next word
					for (Triple<Integer, Integer, String> t1 : totalPos[i - 1]) {
						if (t.getLeftValue() == t1.getLeftValue() // in same row and continuous
								&& t.getCentreValue() == t1.getCentreValue() + (phraseWords[i - 1].length() + 1)) {
							flag = true;
							break;
						}
					}
					if (!flag) { // if isn't continuous word
						totalPos[i].remove(t);
					}
				}
			}
		}
		for (Triple<Integer, Integer, String> pos : totalPos[phraseWords.length - 1]) {
			int column = pos.getCentreValue() - phrase.length() + phraseWords[phraseWords.length - 1].length();
			Pair<Integer, Integer> toAdd = new Pair<>(pos.getLeftValue(), column);
			result.add(toAdd);
		}
		return result;
	}

	/**
	 * Finds all occurrences of the prefix in the document. A prefix is the start of
	 * a word. It can also be the complete word. For example, "obscure" would be a
	 * prefix for "obscure", "obscured", "obscures" and "obscurely".
	 * 
	 * O(p+nm) n is the number of distinct words; m is the number of positions of
	 * every word; p is number of stop words.
	 * 
	 * @param prefix The prefix of a word that is to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number
	 *         of each occurrence of the prefix. Returns an empty list if the prefix
	 *         is not found in the document.
	 * @throws IllegalArgumentException if prefix is null or an empty String.
	 */
	public MySet<Pair<Integer, Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
		if (prefix == null || prefix.length() == 0) {
			throw new IllegalArgumentException();
		}
		prefix = prefix.toLowerCase();
		MySet<Pair<Integer, Integer>> position = new MySet<>();
		if (stopSet.contains(prefix)) {
			return position;
		}
		for (String word : wordMap.keySet()) {

			if (word.contains(prefix.toLowerCase()) && word.charAt(0) == prefix.charAt(0)) {
				for (Triple<Integer, Integer, String> triple : wordMap.get(word)) {
					position.add(new Pair<>(triple.getLeftValue(), triple.getCentreValue()));
				}
			}
		}

		return position;
	}

	/**
	 * Searches the document for lines that contain all the words in the 'words'
	 * parameter. Implements simple "and" logic when searching for the words. The
	 * words do not need to be contiguous on the line.
	 * 
	 * O(n + m + p (q + r (n + s)) n is running time to get the value of key; m is
	 * size of position size of words[0]; p is words' size - 1; q is time to iterate
	 * keySet to check if words[i] is a stop word; r is number of all rows which has
	 * words[0]; s is number of words in each of r rows.
	 * 
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which all the words appear in the document.
	 *         Returns an empty list if the words do not appear in any line in the
	 *         document.
	 * @throws IllegalArgumentException if words is null or an empty array or any of
	 *                                  the Strings in the array are null or empty.
	 */
	public MySet<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {

		MySet<Integer> lines = new MySet<>();
		MySet<Integer> result = new MySet<>();

		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}

		if (words[0].length() == 0 || words[0] == null) {
			throw new IllegalArgumentException();
		}

		for (Triple<Integer, Integer, String> triple : wordMap.get(words[0].toLowerCase())) {
			lines.add(triple.getLeftValue()); // all lines which contain the first words
		}
		if (words.length == 1) {
			return lines;
		}
		for (int i = 1; i < words.length; i++) {
			if (stopSet.contains(words[i])) {
				return result;
			}
			if (words[i].length() == 0 || words[i] == null) {
				throw new IllegalArgumentException();
			}
			for (Integer line : lines) {
				boolean containsWord = false;
				for (String word : rowMap.get(line)) {
					if (words[i].equals(word)) {
						containsWord = true;
					}
				}
				// line contains all word
				if (containsWord == true) {
					result.add(line);
				}
			}

		}
		return result;
	}

	/**
	 * Searches the document for lines that contain any of the words in the 'words'
	 * parameter. Implements simple "or" logic when searching for the words. The
	 * words do not need to be contiguous on the line.
	 * 
	 * O(p(q+n+m(r))) p is size of words; q is times to iterate stop set to check if
	 * words[i] is stop word; n is time to get words[i] from wordMap; m is number of
	 * positions of words[i]; r is check whether the row number has existed in
	 * result set.
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which any of the words appear in the
	 *         document. Returns an empty list if none of the words appear in any
	 *         line in the document.
	 * @throws IllegalArgumentException if words is null or an empty array or any of
	 *                                  the Strings in the array are null or empty.
	 */
	public MySet<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {

		MySet<Integer> result = new MySet<>();
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < words.length; i++) {
			if (!stopSet.contains(words[i])) {
				if (words[i] == null || words[i].length() == 0) {
					throw new IllegalArgumentException();
				}
				for (Triple<Integer, Integer, String> triple : wordMap.get(words[i].toLowerCase())) {
					if (!result.contains(triple.getLeftValue())) {
						result.add(triple.getLeftValue());
					}
				}
			}
		}
		return result;
	}

	/**
	 * Searches the document for lines that contain all the words in the
	 * 'wordsRequired' parameter and none of the words in the 'wordsExcluded'
	 * parameter. Implements simple "not" logic when searching for the words. The
	 * words do not need to be contiguous on the line.
	 * 
	 * O(2*p(q+n+m(r))+s) Called wordsOnLine() two times and getSubtraction should
	 * iterate to determine the result.
	 * 
	 * @param wordsRequired Array of words to find on a single line in the document.
	 * @param wordsExcluded Array of words that must not be on the same line as
	 *                      'wordsRequired'.
	 * @return List of line numbers on which all the wordsRequired appear and none
	 *         of the wordsExcluded appear in the document. Returns an empty list if
	 *         no lines meet the search criteria.
	 * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded
	 *                                  are null or an empty array or any of the
	 *                                  Strings in either of the arrays are null or
	 *                                  empty.
	 */
	public MySet<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded)
			throws IllegalArgumentException {

		MySet<Integer> requiredSet = new MySet<>();
		MySet<Integer> excludedSet = new MySet<>();

		requiredSet = wordsOnLine(wordsRequired);
		excludedSet = wordsOnLine(wordsExcluded);

		return excludedSet.getSubtraction(requiredSet);
	}

	/**
	 * Searches the document for sections that contain all the words in the 'words'
	 * parameter. Implements simple "and" logic when searching for the words. The
	 * words do not need to be on the same lines.
	 * 
	 * O(n+m+p(q+s)) n is time to get position set of first word, m is size of
	 * position set of first words, p is Idea: find out the positions of fist word,
	 * get intersection set with words in next index, the range of result should be
	 * smaller and smaller.
	 * 
	 * @param titles Array of titles of the sections to search within, the entire
	 *               document is searched if titles is null or an empty array.
	 * @param words  Array of words to find within a defined section in the
	 *               document.
	 * @return List of triples, where each triple indicates the line and column
	 *         number and word found, for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated
	 *         sections of the document, or all the indicated sections are not part
	 *         of the document.
	 * @throws IllegalArgumentException if words is null or an empty array or any of
	 *                                  the Strings in either of the arrays are null
	 *                                  or empty.
	 */
	public MySet<Triple<Integer, Integer, String>> simpleAndSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}
		MySet<Triple<Integer, Integer, String>> result = new MySet<>();
		// get every sections that contains the word
		MySet<Triple<Integer, Integer, String>> positions = wordMap.get(words[0].toLowerCase());
		MySet<String> sections = new MySet<>();
		for (Triple<Integer, Integer, String> triple : positions) {
			sections.add(triple.getRightValue()); // words[0]'s sections
		}
		for (int i = 1; i < words.length; i++) {
			if (!stopSet.contains(words[i])) {
				MySet<Triple<Integer, Integer, String>> pos = wordMap.get(words[i].toLowerCase());
				MySet<String> section = new MySet<>();
				for (Triple<Integer, Integer, String> triple : pos) {
					section.add(triple.getRightValue()); // words[0]'s sections
				}
				sections = sections.getIntersection(section);
			}
		}
		for (int i = 0; i < titles.length; i++) {
			titles[i] = titles[i].toLowerCase();
		}
		MySet<String> titleSet = new MySet<>();
		titleSet = titleSet.toSet(titles);

		titleSet = titleSet.getIntersection(sections);

		for (String title : titleSet) {
			for (String word : words) {
				if (!stopSet.contains(word)) {
					MySet<Triple<Integer, Integer, String>> pos = wordMap.get(word.toLowerCase());
					for (Triple<Integer, Integer, String> triple : pos) {
						if (triple.getRightValue().equals(title)) {

							result.add(new Triple<>(triple.getLeftValue(), triple.getCentreValue(), word));

							simpleAndSection.put(triple, word);

						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Searches the document for sections that contain any of the words in the
	 * 'words' parameter. Implements simple "or" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * O( n (m + p + q)); n is size of words, m is time to iterate stop set to check
	 * if words[i] is stop word, p is time to get word from wordMap and q is size of
	 * position set of word.
	 * 
	 * @param titles Array of titles of the sections to search within, the entire
	 *               document is searched if titles is null or an empty array.
	 * @param words  Array of words to find within a defined section in the
	 *               document.
	 * @return List of triples, where each triple indicates the line and column
	 *         number and word found, for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated
	 *         sections of the document, or all the indicated sections are not part
	 *         of the document.
	 * @throws IllegalArgumentException if words is null or an empty array or any of
	 *                                  the Strings in either of the arrays are null
	 *                                  or empty.
	 */
	public MySet<Triple<Integer, Integer, String>> simpleOrSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		if (words == null || words.length == 0) {
			throw new IllegalArgumentException();
		}
		MySet<Triple<Integer, Integer, String>> result = new MySet<>();
		for (String word : words) {
			if (!stopSet.contains(word)) {
				word = word.toLowerCase();
				MySet<Triple<Integer, Integer, String>> positions = wordMap.get(word);
				for (Triple<Integer, Integer, String> triple : positions) {

					if (titles != null) {
						for (String title : titles) {
							if (title.length() == 0 || title == null) {
								throw new IllegalArgumentException();
							}
							title = title.toLowerCase();
							// if one of the title equal add to list "||"
							if (title.equals(triple.getRightValue())) {
								Triple<Integer, Integer, String> toAdd = new Triple<>(triple.getLeftValue(),
										triple.getCentreValue(), word);
								result.add(toAdd);

								simpleOrSection.put(new Triple<>(triple.getLeftValue(), triple.getCentreValue(),
										triple.getRightValue()), word);
							}
						}
					} else { // search in the whole document
						Triple<Integer, Integer, String> toAdd = new Triple<>(triple.getLeftValue(),
								triple.getCentreValue(), word);
						result.add(toAdd);
					}

				}
			}
		}
		return result;
	}

	/**
	 * Searches the document for sections that contain all the words in the
	 * 'wordsRequired' parameter and none of the words in the 'wordsExcluded'
	 * parameter. Implements simple "not" logic when searching for the words. The
	 * words do not need to be on the same lines.
	 * 
	 * O(m + n + t + 2*simpleAndSearch + p * q);
	 * 
	 * m is wordsRequired.length, n is wordsExcluded.length, t is titles.length, p
	 * and q are size of two position sets.
	 * 
	 * idea: merge two arrays into one array and do and search to get the position
	 * which contains both wordsRequired and wordsExcluded, and let the set which
	 * only contains all of the wordsRequired minus the merged set.
	 * 
	 * @param titles        Array of titles of the sections to search within, the
	 *                      entire document is searched if titles is null or an
	 *                      empty array.
	 * @param wordsRequired Array of words to find within a defined section in the
	 *                      document.
	 * @param wordsExcluded Array of words that must not be in the same section as
	 *                      'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column
	 *         number and word found, for each occurrence of one of the required
	 *         words. Returns an empty list if the words are not found in the
	 *         indicated sections of the document, or all the indicated sections are
	 *         not part of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array
	 *                                  or any of the Strings in any of the arrays
	 *                                  are null or empty.
	 */
	public MySet<Triple<Integer, Integer, String>> simpleNotSearch(String[] titles, String[] wordsRequired,
			String[] wordsExcluded) throws IllegalArgumentException {
		MySet<Triple<Integer, Integer, String>> result = new MySet<>();

		// Find all sections which contain all required word
		MySet<Triple<Integer, Integer, String>> posRequired = simpleAndSearch(titles, wordsRequired);
		// Find all words which contains wordReqired and wordExcluded
		// Merge two arrays to wordsAnd
		String[] wordsAnd = new String[wordsRequired.length + wordsExcluded.length];
		int countPos = 0;
		for (int i = 0; i < wordsRequired.length; i++) {
			wordsAnd[i] = wordsRequired[i];
			countPos++;
		}
		for (int i = 0; i < wordsExcluded.length; i++) {
			wordsAnd[countPos] = wordsExcluded[i];
			countPos++;
		}
		MySet<String> andSet = new MySet<>();
		andSet = andSet.toSet(wordsAnd);
		// wordRequired - wordExcluded
		MySet<Triple<Integer, Integer, String>> posAnd = simpleAndSearch(titles, wordsAnd);
		for (Triple<Integer, Integer, String> posRe : posRequired) {
			boolean isVail = true;
			for (Triple<Integer, Integer, String> posAn : posAnd) {
				// check if posRequired contains posExcluded
				// cannot override equals method because we cannot modify Triple class
				if (posRe.getLeftValue() == posAn.getLeftValue() && posRe.getCentreValue() == posAn.getCentreValue()
						&& posRe.getRightValue() == posAn.getRightValue()) {
					isVail = false;
				}
			}
			if (isVail == true) {
				result.add(posRe);
			}
		}
		return result;
	}

	/**
	 * Searches the document for sections that contain all the words in the
	 * 'wordsRequired' parameter and at least one of the words in the 'orWords'
	 * parameter. Implements simple compound "and/or" logic when searching for the
	 * words. The words do not need to be on the same lines.
	 * 
	 * O(2*simpleAndSearch + p*q(n+n)). p is number of keySet of simpleAndSection, q
	 * is number of keySet of simpleOrSearch, n is time to get value of key.
	 * 
	 * @param titles        Array of titles of the sections to search within, the
	 *                      entire document is searched if titles is null or an
	 *                      empty array.
	 * @param wordsRequired Array of words to find within a defined section in the
	 *                      document.
	 * @param orWords       Array of words, of which at least one, must be in the
	 *                      same section as 'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column
	 *         number and word found, for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated
	 *         sections of the document, or all the indicated sections are not part
	 *         of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array
	 *                                  or any of the Strings in any of the arrays
	 *                                  are null or empty.
	 */
	@SuppressWarnings("unused")
	public MySet<Triple<Integer, Integer, String>> compoundAndOrSearch(String[] titles, String[] wordsRequired,
			String[] orWords) throws IllegalArgumentException {

		if (orWords == null || orWords.length == 0) {
			return simpleAndSearch(titles, wordsRequired);
		}
		if (wordsRequired == null || wordsRequired.length == 0) {
			throw new IllegalArgumentException();
		}
		MySet<Triple<Integer, Integer, String>> result = new MySet<>();
		// positions include all required word
		simpleAndSection = new NewMap<>(3);
		MySet<Triple<Integer, Integer, String>> andSet = simpleAndSearch(titles, wordsRequired);
		// positions include all required word but does not include any orWords
		MySet<Triple<Integer, Integer, String>> notSet = simpleOrSearch(titles, orWords);

		for (Triple<Integer, Integer, String> and : simpleAndSection.keySet()) {
			for (Triple<Integer, Integer, String> or : simpleOrSection.keySet()) {
				// if in same section add to result
				if (or.getRightValue() == and.getRightValue()) {
					String orWord = simpleOrSection.get(or);
					String andWord = simpleAndSection.get(and);
					result.add(new Triple<>(or.getLeftValue(), or.getCentreValue(), orWord));
					result.add(new Triple<>(and.getLeftValue(), and.getCentreValue(), andWord));
				}
			}
		}
		return result;
	}
}
