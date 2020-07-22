package theatrical.model;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;

public class Main {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static String statement(Invoice invoice) {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("Statement for " + invoice.getCustomer()).append(" \n");
        final NumberFormat format = NumberFormat.getCurrencyInstance();

        for (val performance: invoice.getPerformances()) {
            int thisAmount = amountFor(performance, playFor(performance));

            volumeCredits += Math.max(performance.getAudience() - 30, 0);
             if (PlayType.COMEDY == playFor(performance).getType())
                 volumeCredits += Math.floor(performance.getAudience() / 5);
            result.append("    ").append(playFor(performance).getName())
                    .append(": ").append(format.format(thisAmount/100))
                    .append(" (").append(performance.getAudience()).append(" seats").append(")")
                    .append("\n");
            totalAmount += thisAmount;
        }

        result.append("Amount owed is ").append(format.format(totalAmount/100)).append("\n");
        result.append("You earned ").append(volumeCredits).append(" credits");


        return result.toString();
    }

    @SneakyThrows
    private static Play playFor(Performance aPerformance) {
        return readPlays().stream().filter(p -> Objects.equals(aPerformance.getPlayID(), p.getId())).findFirst().orElseThrow(RuntimeException::new);
    }

    private static int amountFor(Performance aPerformance, Play play) {
        int result = 0;
        switch (play.getType()) {
            case TRAGEDY:
                result = 40000;
                if (aPerformance.getAudience() > 30) {
                    result += 1000 * (aPerformance.getAudience() - 30);
                }
                break;
            case COMEDY:
                result = 30000;
                if (aPerformance.getAudience() > 20) {
                    result += 10000 + 500 * (aPerformance.getAudience() - 20);
                }
                result += 300 * aPerformance.getAudience();
                break;
            default:
                throw new Error("unknown type " + play.getType());
        }
        return result;
    }

    private static List<Invoice> readInvoices() throws IOException {
        return  objectMapper.readValue(new File("files/invoices.json"), new TypeReference<List<Invoice>>(){});
    }

    private static List<Play> readPlays() throws IOException {
        return objectMapper.readValue(new File("files/plays.json"), new TypeReference<List<Play>>(){});
    }

    public static void main(String[] args) throws IOException {
        val invoices = readInvoices();
        val plays = readPlays();

        invoices.stream().map(Main::statement).forEach(System.out::println);
    }
}
