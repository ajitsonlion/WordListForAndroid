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
public class MemriseParser {

    public static final  String DOMAIN_NAME_FOR_GSCE ="http://www.memrise.com/course/89242/edexcel-gcse-german-vocabulary-list/";
    public static final  String DOMAIN_NAME_FOR_TOP_WORDS="http://www.memrise.com/course/58866/5000-words-sorted-by-frequency-strict-typing/";
    public static final  int GSCE_PAGES_NUMBER =38;
    public static final  int TOP_WORDS_PAGES_NUMBER =102;
    public static short  categoryID=1;
    public static short  cardID=1;
    public static short  wordID=1;
    public    ArrayList<Categories>  getAllGSCEWords()  {


        ArrayList<Categories> categoriesArrayList = new  ArrayList<Categories>();

        ArrayList<String> allURLsByLetter=getAllURLByLetter(DOMAIN_NAME_FOR_GSCE,GSCE_PAGES_NUMBER);


        for(String letterURL:allURLsByLetter){

            try {
                Document doc = Jsoup.connect(letterURL).timeout(10*1000).get();

                Categories categorie=new Categories();

                    categorie.setCategoryID(categoryID++);

                String categoryName=doc.select("h3.progress-box-title").first().text();
                categorie.setCategoryNameInEnglish(categoryName);
                categorie.setCategoryNameInGerman("TODO");


                categorie.setFlashCards(extractWords(doc));
                categoriesArrayList.add(categorie);

            }catch (Exception e) {

                e.printStackTrace();
            }


        }
        return categoriesArrayList;

    }


    public  ArrayList<Categories>  getAllTOPWords()  {


        ArrayList<Categories> categoriesArrayList = new  ArrayList<Categories>();

        ArrayList<String> allURLsByLetter=getAllURLByLetter(DOMAIN_NAME_FOR_TOP_WORDS,TOP_WORDS_PAGES_NUMBER);


        for(String letterURL:allURLsByLetter){

            try {
                Document doc = Jsoup.connect(letterURL).timeout(10*1000).get();
                Categories categorie=new Categories();
                categorie.setCategoryID(categoryID++);
                 categorie.setCategoryNameInEnglish("Top 5000");
                categorie.setCategoryNameInGerman("TODO");


                categorie.setFlashCards(extractWords(doc));
                categoriesArrayList.add(categorie);

            }catch (Exception e) {

                e.printStackTrace();
            }


        }
        return categoriesArrayList;

    }

    public  ArrayList<String> getAllURLByLetter(String domain,int pages){

        ArrayList<String> listOfAllURL=new ArrayList<String>();


        for (int i=1;i<=pages;i++) {

            String letterURL = domain+i+"/" ;


            listOfAllURL.add(letterURL);
        }

        return listOfAllURL;
    }


    public   ArrayList<FlashCard> extractWords(Document doc){
        ArrayList<FlashCard> flashCardForCategory=new ArrayList<FlashCard>();


        Elements wordsTable=doc.getElementsByAttribute("data-thing-id");

        for (Element e:wordsTable){
            FlashCard card=new FlashCard();

            card.setFlashCardID(cardID++);
            String germanBlock=e.select("div.col_a").text();
            GermanWord germanWord=new GermanWord();
            wordID++;
            germanWord.setGermanWordID(wordID);
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
            englishWord.setEnglishWordID(wordID);
            englishWord.setWord(englishBlock);
            card.setEnglishFaceOfFlashCard(englishWord);
            flashCardForCategory.add(card);

        }

        return  flashCardForCategory;
    }
}
