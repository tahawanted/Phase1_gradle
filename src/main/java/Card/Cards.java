package Card;

import org.json.simple.JSONObject;

import java.io.Serializable;

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
interface giveLife{}
interface inspire{}
interface divineShield{}
interface windFury{}
interface poisonous{}
interface echo{}
interface twinEcho{}
interface stealth{}


public class Cards{
    public static class card implements Serializable {
        private String name;
        private int mana;
        private String heroClass;
        private String rarity;
        private String type;
        private String description;
        private int price;
        /*
         ADD ANY NEW VARIABLE TO THE JSON OBJECT AS WELL
         */
        card(String name, int mana, String heroClass, String rarity, String type, String description){
            this.name = name;
            setMana(mana);
            setRarity(rarity);
            setType(type);
            setHeroClass(heroClass);
            this.description = description;
            setPrice();
        }
        private void setMana(int mana){
            assert mana >= 0 && mana <= 10;
            this.mana = mana;
        }
        private void setRarity(String rarity){
            assert rarity.equals("Common") || rarity.equals("Rare") || rarity.equals("Epic")
                    || rarity.equals("Legendary");
            this.rarity = rarity;
        }
        private void setType(String type){
            assert type.equals("Minion") || type.equals("Spell") || type.equals("Weapon") || type.equals("Quest");
            this.type = type;
        }
        private void setHeroClass(String heroClass){
            assert heroClass.equals("Mage") || heroClass.equals("Rogue") || heroClass.equals("Warlock")
                    || heroClass.equals("Neutral");
            this.heroClass = heroClass;
        }
        private void setPrice(){
            switch (rarity){
                case "Common":
                    price = 10;
                    break;
                case "Rare":
                    price = 20;
                    break;
                case "Epic":
                    price = 30;
                    break;
                case "Legendary":
                    price = 40;
                    break;
            }
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

        public int getPrice() {
            return price;
        }

        public JSONObject getCardJsonObject(){
            JSONObject cardObject = new JSONObject();
            cardObject.put("name", name);
            cardObject.put("mana", mana);
            cardObject.put("heroClass", heroClass);
            cardObject.put("rarity", rarity);
            cardObject.put("type", type);
            cardObject.put("description", description);
            return cardObject;
        }
    }

    public static class minion extends card {
        private int health;
        private int maxHealth; //used for health restoration
        private int attackPower;
        /*
         ADD ANY NEW VARIABLE TO THE JSON OBJECT AS WELL
         */
        minion(String name, int mana, int health, int attackPower, String heroClass, String rarity, String description){
            super(name, mana, heroClass, rarity, "Minion", description);
            setHealth(health);
            setAttackPower(attackPower);
            maxHealth = health;
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

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            cardObject.put("health", health);
            cardObject.put("attackPower", attackPower);
            return cardObject;
        }
    }
    public static class spell extends card{
        spell(String name, int mana, String heroClass, String rarity, String description){
            super(name, mana, heroClass, rarity, "Spell", description);
        }

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            return cardObject;
        }
    }
    public static class quest extends card{
        quest(String name, int mana, String heroClass, String rarity, String description) {
            super(name, mana, heroClass, rarity, "Quest", description);
        }

        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            return cardObject;
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
        @Override
        public JSONObject getCardJsonObject() {
            JSONObject cardObject = super.getCardJsonObject();
            cardObject.put("durability", durability);
            cardObject.put("attackPower", attackPower);
            return cardObject;
        }
    }


}
