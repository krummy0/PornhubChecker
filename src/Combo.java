import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Combo {
	private List<String> emails = new ArrayList<String>();
	private List<String> passes = new ArrayList<String>();
	private int currentIndex = 0;
	private static char delimiter;
	
    // Constructor that reads from a File object
    public Combo(File file) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
	    catch (FileNotFoundException e) {
	        System.err.println("Error: File not found - " + file.getAbsolutePath());
	        throw e;
	    }
        catch (IOException e) {
	        System.err.println("Error: Unable to read the file - " + e.getMessage());
	        throw e;
	    }
        
        parse(content.toString().split("\n"), delimiter);
    }
    
    public static void setDelimiter(char delimiterChar) {
    	delimiter = delimiterChar;
    }

    // Method to process the data array and initialize properties
    private void parse(String[] data, char delimiter) {
        for (String str : data) {
        	try {
        		String split[] = str.split("\\Q" + delimiter + "\\E", 1);
        		if (split[0].contains("@")) {
        			emails.add(split[0]);
        			passes.add(split[1]);
        		}
        		else if (split[1].contains("@")) {
        			emails.add(split[1]);
        			passes.add(split[0]);
        		}
        		else
        			throw new Exception("No Email Found");
        	}
        	catch (Exception e) {
        		System.err.println("Failed to read line of combos");
        	}
        }
    }
    
    //gets
    
    //String[Email[], Pass[]]
    public String[][] getAllCombos() {
    	return new String[][] 
    			{ emails.toArray(new String[0]), passes.toArray(new String[0])} ;
    }
    
    //String[Email, Pass]
    public String[] getNext() {
        if (currentIndex < emails.size()) {
            String[] result = { emails.get(currentIndex), passes.get(currentIndex) };
            currentIndex++;
            return result;
        }
        return null; // Return null if all values have been iterated
    }

    // Method to reset the iterator
    public void resetIterator() {
        currentIndex = 0; // Reset the index to the beginning
    }
}
