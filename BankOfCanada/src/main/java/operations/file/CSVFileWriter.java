package operations.file;

import com.opencsv.CSVWriter;
import utils.Series;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVFileWriter {

    /**
     * creates a new CSV file
     * @param contents - The data to add to the csv file
     * @param filePath - the path of the file
     */
    public static void createNewCSV(List<List<Series>> contents, String filePath) {

        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        boolean fileExists = file.exists();


        try {
            // create FileWriter object with file as parameter
            FileWriter outputFile = new FileWriter(file, true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputFile);

            // Writes header to csv file
            if (!fileExists) {
                outputFile.write("Date,Series Name,Label,Description,Value\n");
            }

            for (List<Series> contentList :contents) {
                for (Series content : contentList) {
                    outputFile.append(content.getDate() + "," +
                            content.getSeriesName() + "," +
                            content.getLabel() + "," +
                            content.getDescription() + "," +
                            content.getValue() + "\n"
                    );
                }
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
