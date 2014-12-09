package Parsers.WordListParser;

import WordModel.Categories;
import WordModel.EnglishWord;
import WordModel.FlashCard;
import WordModel.GermanWord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by ajit on 09.12.14.
 */
public class Slangs {
    public static final  String DOMAIN_NAME_FOR_SLANGS="http://german.about.com/library/blvoc_avoid";


    public ArrayList<Categories> getSlangs()  {


        ArrayList<Categories> categoriesArrayList = new  ArrayList<Categories>();

        ArrayList<String> allURLsByLetter=getAllURLByLetter(DOMAIN_NAME_FOR_SLANGS);


        for(String letterURL:allURLsByLetter){

            try {
                Document doc = Jsoup.connect(letterURL).timeout(10*1000).get();
                Categories categorie=new Categories();
                categorie.setCategoryID(MemriseParser.categoryID++);
                categorie.setCategoryNameInEnglish("Slangs");
                categorie.setCategoryNameInGerman("TODO");


                categorie.setFlashCards(extractWords(doc));
                categoriesArrayList.add(categorie);

            }catch (Exception e) {

                e.printStackTrace();
            }


        }
        return categoriesArrayList;

    }



    public  ArrayList<String> getAllURLByLetter(String domain){

        ArrayList<String> listOfAllURL=new ArrayList<String>();


        for (int i=65;i<=90;i++) {

            String letterURL = domain+(char)i+".htm" ;


            listOfAllURL.add(letterURL);
        }

        return listOfAllURL;
    }


    public   ArrayList<FlashCard> extractWords(Document doc){
        ArrayList<FlashCard> flashCardForCategory=new ArrayList<FlashCard>();


        Elements wordsTable=doc.getElementsByAttribute("data-thing-id");

        for (Element e:wordsTable){
            FlashCard card=new FlashCard();

            card.setFlashCardID(MemriseParser.cardID++);
            String germanBlock=e.select("div.col_a").text();
            GermanWord germanWord=new GermanWord();
            MemriseParser.wordID++;
            germanWord.setGermanWordID( MemriseParser.wordID);
            if (germanBlock.length()>3){

                String article=germanBlock.substring(0,3);
                boolean ifArticleExists="der".equalsIgnoreCase(article)||"die".equalsIgnoreCase(article)||"das".equalsIgnoreCase(article);
                if(ifArticleExists){
                    germanWord.setGender(article);
                    germanWord.setWord(germanBlock.substring(4));
                }
                else {
                    germanWord.setGender("");
                    germanWord.setWord(germanBlock);
                }

            }else{
                germanWord.setGender("");
                germanWord.setWord(germanBlock);

            }

            card.setGermanFaceOfFlashCard(germanWord);

            String englishBlock=e.select("div.col_b").text();
            EnglishWord englishWord=new EnglishWord();
            englishWord.setEnglishWordID( MemriseParser.wordID);
            englishWord.setWord(englishBlock);
            card.setEnglishFaceOfFlashCard(englishWord);
            flashCardForCategory.add(card);

        }

        return  flashCardForCategory;
    }

}
