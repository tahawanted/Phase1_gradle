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
    public static class card{
        private String name;
        private int mana;
        private String heroClass;
        private String rarity;
        private String type;
        private String description;
        card(String name, int mana, String heroClass, String rarity, String type, String description){
            this.name = name;
            setMana(mana);
            setRarity(rarity);
            setType(type);
            setHeroClass(heroClass);
            this.description = description;
        }
        private void setMana(int mana){
            assert mana >= 0 && mana <= 10;
            this.mana = mana;
        }
        private void setRarity(String rarity){
            assert rarity.equals("Common") || rarity.equals("rare") || rarity.equals("epic")
                    || rarity.equals("Legendary");
            this.rarity = rarity;
        }
        private void setType(String type){
            assert type.equals("Minion") || type.equals("Spell") || type.equals("Weapon") || type.equals("Quest");
            this.type = type;
        }
        private void setHeroClass(String heroClass){
            assert heroClass.equals("Mage") || heroClass.equals("Rogue") || heroClass.equals("Warlock")
                    || heroClass.equals("netrual");
            this.heroClass = heroClass;
        }

        public String getName() {
            return name;
        }

        public int getMana() {
            return mana;
        }

        public String getHeroClass() {
            return heroClass;
        }

        public String getRarity() {
            return rarity;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class minion extends card {
        private int health;
        private int attackPower;
        //private boolean battlecry, charge, deathRattle, taunt, freeze, discover, stealth;
        minion(String name, int mana, int health, int attackPower, String heroClass, String rarity, String description){
            super(name, mana, heroClass, rarity, "Minion", description);
            setHealth(health);
            setAttackPower(attackPower);
        }
        private void setHealth(int health){
            assert health > 0 && health <= 10;
            this.health = health;
        }
        private void setAttackPower(int attackPower){
            assert attackPower >= 0 && attackPower <= 10;
            this.attackPower = attackPower;
        }

        public int getHealth() {
            return health;
        }

        public int getAttackPower() {
            return attackPower;
        }
    }
    public static class spell extends card{
        spell(String name, int mana, String heroClass, String rarity, String description){
            super(name, mana, heroClass, rarity, "Spell", description);
        }
    }
    public static class quest extends card{
        quest(String name, int mana, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Quest", description);
        }

    }
    public static class weapon extends card{
        private int durability, attackPower;
        weapon(String name, int mana, int durability, int attackPower, String heroClass, String rarity, String description){
            super(name, mana, heroClass, rarity, "Weapon", description);
            setAttackPower(attackPower);
            setDurability(durability);
        }
        void setDurability(int durability){
            assert durability >= 0;
            this.durability = durability;
        }
        private void setAttackPower(int attackPower){
            assert attackPower > 0;
            this.attackPower = attackPower;
        }
    }


}
