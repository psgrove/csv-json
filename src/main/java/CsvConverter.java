import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.time.Instant;
import java.util.*;

public class CsvConverter {

    private final File file;
    private String fileName;
    private String outFileName = "output-"+ Instant.now().getEpochSecond()+".json";
    private Reader in =null;

    public static void main(String[] args) throws IOException {
        CsvConverter converter = new CsvConverter(null, "Book1.csv");
        converter.convertToJson();
    }

    private CsvConverter(File file ,  String filename) throws IOException {
        this.file = file;
        this.fileName = filename;
    }

    private void convertToJson() throws IOException {
        if(null!=fileName){
            Class clazz = CsvConverter.class;
            InputStream inputStream = new BOMInputStream( clazz.getResourceAsStream(fileName));
            in = new InputStreamReader(inputStream);
        }
        else if(null!=file){
            in = new FileReader(file);
        }
        if(null==in){
            throw new RuntimeException("All poo");
        }
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
