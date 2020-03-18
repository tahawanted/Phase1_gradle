package Hero;

public class Hero {
    private String name;
    private int heroLevel;
    private int cardsOwnedByHero;
    private String description;
    private int maxHealth;
    private int currentHealth;
    private int shield;
    public Hero(String name, String description) {
        this.name = name;
        this.description = description;
        heroLevel = 1;
        cardsOwnedByHero = 10;
    }

    public String getName() {
        return name;
    }

    public int getHeroLevel() {
        return heroLevel;
    }

    public int getCardsOwnedByHero() {
        return cardsOwnedByHero;
    }

    public String getDescription() {
        return description;
    }

}
