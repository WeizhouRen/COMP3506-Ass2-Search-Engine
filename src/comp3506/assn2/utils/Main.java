package comp3506.assn2.utils;

import java.util.List;
import java.io.FileNotFoundException;

import comp3506.assn2.application.AutoTester;

public class Main {

	public static void main(String args[]) {
		System.out.println("test");
		MySet<Integer> set = new MySet<>();
		MySet<Integer> set1 = new MySet<>();
		// System.out.println(set.contains(1));
//		set.add(1);
//		set.add(2);
//		set.add(3);
//		set.add(4);
//		
//		set1.add(1);
//		set1.add(5);
//		set1.add(4);
//		set1.add(8);
//		set.addAll(set1);
//		MySet<Integer> subtraction = set;
//		System.out.println("size is "+subtraction.size());
//		for (Integer i : subtraction) {
//			System.out.println(i);
//		}
//		System.out.println(set.size());
//		NewMap<String,Integer> test = new NewMap<>(1);
//		test.put("a", 1);
//		test.put("a", 0);
//		test.put("b", 3);
//		test.put("c", 3);
//		test.put("d", 3);
//		test.put("e", 3);
//		//System.out.println(test.get("a"));
//		System.out.println(test.isEmpty());
//		System.out.println(test.size());
//		System.out.println(test.get("a"));
//		System.out.println(test.get("e"));
		try {
			AutoTester a = new AutoTester("./files/test.txt", "./files/shakespeare-index.txt",
					"./files/stop-words.txt");

//			 List<Pair<Integer, Integer>> pos =a.prefixOccurrence("Ha");
//			for (Pair<Integer, Integer> p : pos) {
//				System.out.println(p.getLeftValue());
//				System.out.println(p.getRightValue());
//			}
//			System.out.println( "size is "+pos.size());
			String[] titles = { "CYMBELINE", "THE TRAGEDY OF HAMLET", "THE LIFE OF KING HENRY THE FIFTH",
					"THE FIRST PART OF KING HENRY THE SIXTH", "THE SECOND PART OF KING HENRY THE SIXTH",
					"KING RICHARD THE SECOND", "VENUS AND ADONIS" };
			String[] requiredWords = { "obscure" };
			String[] orWords = {};

//			String[] titles = { "CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH",
//					"THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", "VENUS AND ADONIS" };
//			String[] requiredWords = { "obscure", "rusty" };

			//List<Triple<Integer, Integer, String>> test = a.compoundAndOrSearch(titles, requiredWords, orWords);
			
			List<Pair<Integer,Integer>> test = a.phraseOccurrence("aaa bbb");
			
			
			System.out.println("----------"+test.size());

//			for (Triple<Integer, Integer, String> triple : test) {
//				System.out.println("row: - " + triple.getLeftValue()+" column: - " + triple.getCentreValue()+" word - " + triple.getRightValue()); 
//			}
//			 System.out.println("obscure: "+ a.wordCount("obscure"));
			//
			// System.out.println("size = " + test.size());
			// for (Triple<Integer, Integer, String> s : test) {
//				System.out.println("row: " + s.getLeftValue() + " column: " + s.getCentreValue());
//				System.out.println(s.getRightValue());
//			}
//			String[] titles = { "CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH",
//					"THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", "VENUS AND ADONIS" };
//			String[] requiredWords = { "obscure", "rusty" };
//			//System.out.println("tis " + a.wordCount("a"));
//			List<Triple<Integer, Integer, String>> list = a.simpleAndSearch(titles, requiredWords);
			// for ()
			// a.getColumn("t");
			// System.out.println("rusty " + a.wordCount("rusty"));
			// System.out.println("beaver " + a.wordCount("beaver"));

		} catch (FileNotFoundException | IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
