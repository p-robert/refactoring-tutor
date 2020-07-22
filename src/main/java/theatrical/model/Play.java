package theatrical.model;

import lombok.Value;

@Value
public class Play {
    String id;
    String name;

    PlayType type;
}
