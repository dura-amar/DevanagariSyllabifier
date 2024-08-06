package com.thegroup;


//import com.wiseyak.unicodetokenizer.UnicodeCharacterTokenizer;

import com.wiseyak.unicodetokenizer.UnicodeCharacterTokenizer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TheGroup working with UnicodeTokenizer to prepare test dataset.
 * @author  TheGroup
 * @date    2024-08-05
 */
public class TestDataCollection {

    private static final Logger logger = Logger.getLogger(TestDataCollection.class.getName());

    public static void main(String[] args) {
//        String resultExpected = "[रा{WC_DV}, ष्ट्रि{WC_HLN_WC_HLN_WC_DV}, य{WC}, ता{WC_DV}, को{WC_DV}]";
//        String testData = "राष्ट्रियताको";
//        UnicodeCharacterTokenizer tokenizer;
//        tokenizer = new UnicodeCharacterTokenizer(testData);
//        String resultOutcome1 = tokenizer.GetGroupedCharacters().toString();
//        System.out.println("\nInput: " + testData);
//        System.out.println("\t" + resultOutcome1);


        // Reading from the file
        String csvFile = "src/main/java/com/thegroup/originalWordlist.csv";
        ArrayList<String> wordlist = new ArrayList<>();
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while((line = br.readLine())!=null){
                // Filter out the lines that contains space in between: e.g. अन्तर्राष्ट्रिय वित्तीय आयोग
                // Including only the words that contains:
                //                          ि   : for reordering issue
                //                          ्   : for composite formation issues
                //                          /   : some dictionary entry contained '/' e.g.अठपरिया/अठपरी/अठपरे
                if (!line.contains(" ") && !line.contains("/")&& (line.contains("्") | line.contains("ि"))) {
                    wordlist.add(line);
                }
            }
            logger.info("File read successfully: " + csvFile);
            logger.info("Total words read: " + wordlist.size());
        }catch(IOException e){
            logger.log(Level.SEVERE, "Error reading file: " + csvFile, e);
        }


        // Preparing HashTable Length:[]
        HashMap<Integer, List<String>> seqLength2Seq = new HashMap<>();
        for(String currentSequence: wordlist){
            UnicodeCharacterTokenizer tokenizer = new UnicodeCharacterTokenizer(currentSequence);
//            String tokenizedCurrentSequence = tokenizer.GetGroupedCharacters().toString();
            int currentSequenceLength = tokenizer.GetGroupedCharacters().size();
            seqLength2Seq
                    .computeIfAbsent(currentSequenceLength, k-> new ArrayList<>())
                    .add(currentSequence);
        }
        logger.info("HashTable prepared with sequences grouped by length.");
        // Log the size of lists in the HashMap
        for (Map.Entry<Integer, List<String>> entry : seqLength2Seq.entrySet()) {
            logger.info("Length " + entry.getKey() + ": " + entry.getValue().size() + " items");
        }


        // Writing to CSV
        String outputFile = "src/main/java/com/thegroup/output.csv";
        // Extract headers from keys
        List<Integer> headers = new ArrayList<>(seqLength2Seq.keySet());
        Collections.sort(headers); // Sort headers if needed

        // Determine the number of rows for the CSV (the length of the longest list in values)
        int maxRowCount = seqLength2Seq.values().stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        try (FileWriter writer = new FileWriter(outputFile);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(Arrays.toString(headers.toArray(new Integer[0]))))) {

            // Write data rows
            for (int i = 0; i < maxRowCount; i++) {
                List<String> row = new ArrayList<>();
                for (Integer header : headers) {
                    List<String> values = seqLength2Seq.get(header);
                    if (i < values.size()) {
                        row.add(values.get(i));
                    } else {
                        row.add(""); // Add empty string if the current list is shorter
                    }
                }
                csvPrinter.printRecord(row);
            }

            logger.info("CSV file created successfully: " + outputFile);

        } catch (IOException e) {
           logger.severe("Problem with writing to csv." + e);
        }
    }
}