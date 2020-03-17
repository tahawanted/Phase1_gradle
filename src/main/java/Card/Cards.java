package Card;

interface playable{}
interface vulnerable{}
interface battlecry{}
interface charge{}
interface attack{}
interface quest{}
interface deathRattle{}
interface taunt{}
interface freeze{}
interface discover{}
interface stealth{}
interface dead{}

public class Cards{
    public static class card implements playable{
        String name;
        int mana;
        String description;
        String heroClass;
        String rarity;
        String type;
    }
    public static class minion extends card implements vulnerable, battlecry, charge, attack,
            deathRattle, taunt, freeze, discover, stealth{
        String health;
        String attack;
    }
    public static class spell extends card{}
    public static class quest extends card{}
    public static class weapon extends card{}
    public static class heroPower extends spell{}

}
