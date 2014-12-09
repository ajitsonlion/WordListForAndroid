import Parsers.WordListParser.IELanguagesSiteParser;
import Parsers.WordListParser.MemriseParser;
import WordModel.Categories;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by ajit on 05.12.14.
 */
public  class MainClass {



    public static void main( final String[] args ){
        ArrayList<Categories> categories=new ArrayList<Categories>();



        MemriseParser memriseParser=new MemriseParser();
        categories.addAll(memriseParser.getAllGSCEWords());
        categories.addAll(memriseParser.getAllTOPWords());

        Gson gson = new Gson();
        FileWriter fileWriter = null;
        try {
            String content = gson.toJson(categories);
            File newTextFile = new File("./dictionary.json");
            fileWriter = new FileWriter(newTextFile);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
         } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
             }
        }

        System.out.println("Finished");
    }
}
