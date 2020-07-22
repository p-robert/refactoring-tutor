package theatrical.main;

import java.io.IOException;

import theatrical.service.DataLoader;
import theatrical.service.InvoiceService;

public class Main {

    private static void generateInvoice() throws IOException {
        DataLoader.readInvoices().stream().map(InvoiceService::statement).forEach(System.out::println);
    }

    public static void main(String[] args) throws IOException {
        generateInvoice();
    }
}
