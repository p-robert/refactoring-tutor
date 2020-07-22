package theatrical.service;

import java.text.NumberFormat;
import java.util.Objects;

import lombok.SneakyThrows;
import lombok.val;
import theatrical.model.Invoice;
import theatrical.model.Performance;
import theatrical.model.Play;
import theatrical.model.PlayType;

public class InvoiceService {

    public static String statement(Invoice invoice) {
        int totalAmount = 0;
        int volumeCredits = 0;
        StringBuilder result = new StringBuilder("Statement for " + invoice.getCustomer()).append(" \n");
        final NumberFormat format = NumberFormat.getCurrencyInstance();

        for (val performance: invoice.getPerformances()) {
            volumeCredits += Math.max(performance.getAudience() - 30, 0);
            if (PlayType.COMEDY == playFor(performance).getType())
                volumeCredits += Math.floor(performance.getAudience() / 5);
            result.append("    ").append(playFor(performance).getName())
                    .append(": ").append(format.format(amountFor(performance)/100))
                    .append(" (").append(performance.getAudience()).append(" seats").append(")")
                    .append("\n");
            totalAmount += amountFor(performance);
        }

        result.append("Amount owed is ").append(format.format(totalAmount/100)).append("\n");
        result.append("You earned ").append(volumeCredits).append(" credits");


        return result.toString();
    }


    @SneakyThrows
    private static Play playFor(Performance aPerformance) {
        return DataLoader.readPlays().stream().filter(p -> Objects.equals(aPerformance.getPlayID(), p.getId())).findFirst().orElseThrow(RuntimeException::new);
    }

    private static int amountFor(Performance aPerformance) {
        int result;
        switch (playFor(aPerformance).getType()) {
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
                throw new Error("unknown type " + playFor(aPerformance).getType());
        }
        return result;
    }
}
