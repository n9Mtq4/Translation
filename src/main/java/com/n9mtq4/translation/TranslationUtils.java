package com.n9mtq4.translation;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by will on 4/16/16 at 7:29 PM.
 * 
 * OMG THIS IS SOME OF THE WORST CODE I HAVE EVER WRITTEN
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
public class TranslationUtils {
	
	protected static Translation makeTranslations(BufferedReader bufferedReader) throws IOException, SyntaxError {
		
//		first line should look like: [lang: en]
//		skip every line before it
		String line;
		do {
			line = bufferedReader.readLine();
			if (line == null) throw new SyntaxError("Reached the end of the file looking for language declaration", "null line");
			line = line.trim();
		} while(!(line.startsWith("[lang: ") && line.endsWith("]")));
		
//		line should now be like: [lang: en]
		String langCode = line.split(":")[1];
		langCode = langCode.trim();
		langCode = langCode.substring(0, langCode.length() - 1);
		
//		define the translation
		Translation translation = new Translation(langCode);
		
//		start working on the string declarations
		while ((line =  bufferedReader.readLine()) != null) {
			
//			filter out comments
			if (line.trim().startsWith("#")) continue;
			
//			we have a blank line
			if (line.trim().equals("")) continue;
			
//			make sure there is an equals sign
			if (!line.contains("=")) throw new SyntaxError("Unexpected symbol", line);
			
//			split - {'name ', ' "String Text"'}
			String[] declarationTokens = line.split("=");
			
//			there should only be two tokens
			if (declarationTokens.length != 2) throw new SyntaxError("Invalid string declaration", line);
			
//			get the name
			String declarationName = declarationTokens[0].trim();
			
			String rawDeclaration = declarationTokens[1];
			
//			the declaration should start with at least one quote
			if (!rawDeclaration.trim().startsWith("\"")) throw new SyntaxError("Declaration doesn't start with a \"", line);
			
//			three quotes - multiline string
			if (rawDeclaration.trim().startsWith("\"\"\"")) {
				
//				remove the triple """ on the start
				String noTripleQuote = rawDeclaration.substring(4);
				
//				if the triple quote ends in one line
				if (noTripleQuote.trim().endsWith("\"\"\"")) {
					String declarationString = trimTrailing(noTripleQuote).substring(0, noTripleQuote.length() - 3);
					translation.put(declarationName, declarationString);
					continue;
				}
				
				String declarationString = "";
				
//				ignore the first line if there is nothing on it
				if (!noTripleQuote.trim().equals("")) declarationString += noTripleQuote;
				
//				continue through the lines until we see the closing """
				while ((line = bufferedReader.readLine()) != null) {
					
//					we have reached the end
					if (line.trim().endsWith("\"\"\"")) {
//						there could still be a line like 'text"""' that we have to take into account
						if (!line.trim().equals("\"\"\"")) {
							if (!line.trim().endsWith("\"\"\"")) throw new SyntaxError("The closing three quotes are not at the end of the line", line);
							declarationString += trimTrailing(line).substring(0, line.trim().length() - 3) + "\n";
						}
					}else {
//						still add stuff
						declarationString += line + "\n";
					}
					
				}
				
				translation.put(declarationName, declarationString.substring(0, declarationString.length() - 2)); // trim off the last \n
				continue;
				
			}else {
//				one quote - single line string
				
				String trimmed = rawDeclaration.trim();
				
				if (!trimmed.endsWith("\"")) throw new SyntaxError("No closing \"", line);
				
				String declarationString = trimmed.substring(1, trimmed.length() - 1);
				translation.put(declarationName, declarationString);
				continue;
				
			}
			
		}
		
		return translation;
		
	}
	
	/**
	 * http://stackoverflow.com/a/16974310/5196460
	 * */
	private static String trimTrailing(String str) {
		return str.replaceFirst("\\s+$", "");
	}
	
}
