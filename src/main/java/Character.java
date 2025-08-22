class Character {
    String element;
    int health;
    int maxHealth;
    int primaryAttackDamage;
    int secondaryAttackDamage;
    int primaryAttackCooldown;
    int healCooldown;
    int healStrength;
    boolean isDefending;

    public Character(String element) {
        setElement(element); // Initialize stats based on the element
        this.element = element;
        this.primaryAttackCooldown = 0;
        this.healCooldown = 0;
        this.isDefending = false;
    }
        // Assign stats based on the element
        public void setElement(String element) {
            this.element = element;
            switch (element){
            case "Water":
                this.primaryAttackDamage = 15;
                this.secondaryAttackDamage = 10;
                this.health = 105;
                this.maxHealth = 105;
                this.healStrength = 20;
                break;
            case "Fire":
                this.primaryAttackDamage = 30;
                this.secondaryAttackDamage = 10;
                this.health = 75;
                this.maxHealth = 75;
                this.healStrength = 5;
                break;
            case "Ice":
                this.primaryAttackDamage = 15;
                this.secondaryAttackDamage = 5;
                this.health = 140;
                this.maxHealth = 140;
                this.healStrength = 20;
                break;
            case "Grass":
                this.primaryAttackDamage = 15;
                this.secondaryAttackDamage = 10;
                this.maxHealth = 100;
                this.health = 100;
                this.healStrength = 15;
                break;
                case "Moon":
                this.primaryAttackDamage = 20;
                this.secondaryAttackDamage = 5;
                this.health = 105;
                this.maxHealth = 105;
                this.healStrength = 15;
                break;
                case "Light":
                this.primaryAttackDamage = 35;
                this.secondaryAttackDamage = 15;
                this.health = 70;
                this.maxHealth = 70;
                this.healStrength = 5;
        }
    }

    @Override
    public String toString() {
        return element + " Element (Health: " + health + ")";
    }
    public void updateStats() {
    }
}


class Enemy extends Character
{
    float scalingFactor;
    int scaledPrimaryAttack;
    int scaledSecondaryAttack;
    
    public Enemy(String element) {
        super(element);
        int rounds = 1;
        this.scalingFactor = 1.0f;
        scaleStats(1);
    }
    
    public void updateStats(int rounds, String element) {
        super.setElement(element);
        scalingFactor += 0.1f; // Increase scaling factor per update
        scalingFactor = (float) Math.round(scalingFactor * 10) / 10;
        scaleStats(rounds);
        System.out.println("Enemy stats scaled with factor " + scalingFactor + "!");
    }

    private void scaleStats(int rounds) {
        this.maxHealth = (int) Math.round(100 * scalingFactor);
        scaledPrimaryAttack += 2*rounds / 5;
        scaledPrimaryAttack += rounds / 5;
        health = maxHealth; // Reset health to max after scaling
        primaryAttackDamage += scaledPrimaryAttack;
        secondaryAttackDamage += scaledSecondaryAttack;
    }
    
}



class Player extends Character
{
    int level;
    int exp;
    int expRequired;
    public Player(String element) {
        super(element);
        this.level = 1;
        this.expRequired = 100 * level;
        this.exp = 0;
        this.maxHealth = this.maxHealth + 5*(level-1);
    }
    @Override
    public void updateStats() {
        while (exp >= expRequired) {
            exp -= expRequired;
            level++;
            expRequired = 100 * level;
            this.maxHealth += 5 * (level-1);
            this.health = this.maxHealth;
            primaryAttackDamage += 2;
            secondaryAttackDamage += 1;

            System.out.println("Player leveled up to level " + level + "!");
        }
        System.out.println("Player EXP: " + exp + "/" + expRequired);
    }

    public void gainExp(int amount) {
        exp += amount;
        System.out.println("Player gained " + amount + " EXP!");
        updateStats();
    }
}