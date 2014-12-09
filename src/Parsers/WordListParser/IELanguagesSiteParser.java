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
 * Created by ajit on 05.12.14.
 */
public class IELanguagesSiteParser {

    public static final  String DOMAIN_NAME ="http://ielanguages.com/";
    public static final String BASE_URL=DOMAIN_NAME+"gerlang.html";

    public static short cardId=0;
    public static short categoriesId=1;

    public ArrayList<Categories> getData(){

        ArrayList<Categories> categorieses=new ArrayList<Categories>();


    try {

        Document doc = Jsoup.connect(BASE_URL).get();
        Elements categoriesTable=doc.select("ol li");

        for (Element link : categoriesTable) {
            String linkofpage =link.getElementsByTag("a").attr("href");
            String linkHref = DOMAIN_NAME+linkofpage;
            String linkText = link.text();
            Categories  subject=new Categories();
            subject.setCategoryID(categoriesId++);
            subject.setCategoryNameInEnglish(linkText);
            subject.setCategoryNameInGerman("AAAA");
            Document docforCategory = Jsoup.connect(linkHref).get();
            Element wordsTable=docforCategory.getElementsByTag("table").get(1);
            Elements wordsTableBody=wordsTable.getElementsByTag("tr");
            wordsTableBody.remove(0);
            ArrayList<FlashCard>flashCardsForCatefory=new ArrayList<FlashCard>();

            for (Element e:wordsTableBody){
                cardId++;
                EnglishWord englishWord=new EnglishWord();
                GermanWord germanWord=new GermanWord();
                FlashCard flashCard=new FlashCard();
                try {
                    if (e.getElementsByTag("td").size()>2){
                        englishWord.setWord(e.getElementsByTag("td").first().text());
                        englishWord.setEnglishWordID(cardId);
                        germanWord.setWord( e.getElementsByTag("td").get(1).text());
                        germanWord.setGermanWordID(cardId);
                        flashCard.setFlashCardID(cardId);
                        flashCard.setGermanFaceOfFlashCard(germanWord);
                        flashCard.setEnglishFaceOfFlashCard(englishWord);
                        flashCardsForCatefory.add(flashCard);
                    }


                }catch (Exception ex){
                    ex.printStackTrace();

                }

            }
             subject.setFlashCards(flashCardsForCatefory);
            categorieses.add(subject);

        }



    }catch (Exception e) {

        e.printStackTrace();
    }


        return categorieses;

}


}
