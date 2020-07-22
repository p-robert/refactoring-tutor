package theatrical.model;

import java.util.List;

import lombok.Value;

@Value
public class Invoice {
    String customer;
    List<Performance> performances;
}
