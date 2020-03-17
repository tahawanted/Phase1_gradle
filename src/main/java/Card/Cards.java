package Card;

interface playable{}
interface vulnerable{}
interface battlecry{}
interface charge{}
interface rush{}
interface attack{}
interface deathRattle{}
interface taunt{}
interface freeze{}
interface discover{}
interface reborn{}
interface lifeSteal{}
interface restore{}
interface inspire{}
interface divineShield{}
interface windFury{}
interface poisonous{}
interface echo{}
interface twinEcho{}
interface stealth{}


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
        boolean battlecry, charge, deathRattle, taunt, freeze, discover, stealth;
    }
    public static class spell extends card{}
    public static class quest extends card{}
    public static class weapon extends card{
        int durability;
    }
    public static class heroPower extends spell{}

}
