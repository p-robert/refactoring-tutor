package theatrical.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import theatrical.model.Invoice;
import theatrical.model.Play;

@UtilityClass
public class DataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Invoice> readInvoices() throws IOException {
        return  objectMapper.readValue(new File("files/invoices.json"), new TypeReference<List<Invoice>>(){});
    }

    public static List<Play> readPlays() throws IOException {
        return objectMapper.readValue(new File("files/plays.json"), new TypeReference<List<Play>>(){});
    }
}
