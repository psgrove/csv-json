import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.util.*;

public class CsvConverter {

    private final File file;
    String fileName;
    String outFileName = "output.json";

    public static void main(String[] args) throws IOException {
       new CsvConverter(null, "Book1.csv");
    }

    private CsvConverter(File file ,  String filename) throws IOException {
        this.file = file;
        this.fileName = filename;
        Reader in =null;
        if(null!=filename){
            Class clazz = CsvConverter.class;
            InputStream inputStream = clazz.getResourceAsStream(filename);
            in = new InputStreamReader(inputStream);
        }
        else if(null!=file){
            in = new FileReader(file);
        }

        if (null!=in){
            convertToJson(in);
        }
        else{
            throw new RuntimeException("All poo");
        }
    }

    private void convertToJson(Reader in) throws IOException {
        CSVParser csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
        List<Map<String, String>> data = new ArrayList<>();
        csvParser.getRecords().forEach(record -> data.add(record.toMap()));
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        System.out.println(jsonResult);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));
        writer.write(jsonResult);
        writer.close();
    }
}
