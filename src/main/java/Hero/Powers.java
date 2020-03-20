package Hero;

import java.io.Serializable;

public class Powers {
    public static class heroPowers implements Serializable {
        String name;
        int mana;
        String description;
        public heroPowers(String name, int mana, String description) {
            this.name = name;
            this.mana = mana;
            this.description = description;
        }
    }

}
