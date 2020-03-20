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
        heroPowers(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMana() {
            return mana;
        }

        public void setMana(int mana) {
            this.mana = mana;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
