package com.thegroup;


//import com.wiseyak.unicodetokenizer.UnicodeCharacterTokenizer;

import com.wiseyak.unicodetokenizer.UnicodeCharacterTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TheGroup working with UnicodeTokenizer to prepare test dataset.
 * @author  TheGroup
 * @date    2024-08-05
 */
public class TestDataCollection {
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
        }catch(IOException e){
            e.printStackTrace();
        }


        // Preparing HashTable Length:[]
        HashMap<Integer, List<String>> seqLength2Seq = new HashMap<>();
        for(String currentSequence: wordlist){
            UnicodeCharacterTokenizer tokenizer = new UnicodeCharacterTokenizer(currentSequence);
            String tokenizedCurrentSequence = tokenizer.GetGroupedCharacters().toString();
            int currentSequenceLength = tokenizer.GetGroupedCharacters().size();
            seqLength2Seq
                    .computeIfAbsent(currentSequenceLength, k-> new ArrayList<>())
                    .add(currentSequence);
        }



        // Writing to CSV
        String outputFile = "src/main/java/com/thegroup/output.csv";
        // Write HashMap to CSV
        try (FileWriter writer = new FileWriter(outputFile)) {
            // Write header
            writer.append("ID\n");

            // Write data
            for (Map.Entry<Integer, List<String>> entry : seqLength2Seq.entrySet()) {
                Integer key = entry.getKey();
                List<String> values = entry.getValue();
                writer.append(key.toString());
                for (String value : values) {
                    writer.append(",");
                    writer.append(value);
                }
                writer.append("\n");
            }

            System.out.println("CSV file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}