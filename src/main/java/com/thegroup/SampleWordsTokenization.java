package com.thegroup;

import com.wiseyak.unicodetokenizer.UnicodeCharacterTokenizer;

import java.util.ArrayList;
import java.util.List;

public class SampleWordsTokenization {
    public static void main(String[] args) {

//        String uni ="स्त्री";
//        String bi ="चिट्ठी";
//        String tri ="गतम्";
//        String quad ="कम्प्युटर";
//        String penta ="जलविद्युत्";
//        String hexa ="स्वदेशीकरण";
//        String hepta ="पदपरिवर्तन";
//        String octa ="आवश्यकतापूर्ति";
//        String nine ="अन्तरराष्ट्रियकरण";
//        String ten ="उपमहानगरपालिका";

        List<String> words = List.of("स्त्री", "चिट्ठी", "गतम्", "कम्प्युटर", "जलविद्युत्", "स्वदेशीकरण", "पदपरिवर्तन", "आवश्यकतापूर्ति", "अन्तरराष्ट्रियकरण", "उपमहानगरपालिका");

        UnicodeCharacterTokenizer tokenizer;

//        for(String word: words){
//            tokenizer = new UnicodeCharacterTokenizer(word);
//            String result = tokenizer.GetGroupedCharacters().toString();
//            System.out.println("\n"+ word + " : " + result);
//        }
        String word ="सिञ्जा";
        tokenizer = new UnicodeCharacterTokenizer(word);
        String result = tokenizer.GetGroupedCharacters().toString();
        System.out.println("\n"+ word + " : " + result);


    }
}
