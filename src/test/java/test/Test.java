package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws Exception {

		Pattern pattern = Pattern.compile("[^0-9]");

		BufferedReader in = new BufferedReader(new FileReader("src/test/resources/test.txt"));

		String line = null;

		while ((line = in.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			System.out.println(line);
			if (matcher.find()) {
				System.out.println("aaa");
			} else {
				System.out.println("bbbb");
			}

		}

	}

}
