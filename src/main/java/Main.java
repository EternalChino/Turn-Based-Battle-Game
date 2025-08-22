import java.util.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            // Game introduction
            System.out.println("Welcome to the Enhanced Elemental Battle Game!");
            System.out.println("Choose your element and face an enemy with unique strengths and weaknesses!");
            System.out.println("Elements: Water, Fire, Light, Moon, Ice, Grass");
            System.out.println("Each element has unique stats and abilities. Let the battle begin...\n");

            System.out.println("Have you played before? If so, and you would like to load an already existing game, enter 'yes'. Otherwise, enter 'no'.");

            String playerChoice = scanner.nextLine();

            Player player = null;
            Enemy enemy = null;
            int rounds = 1;

            if (playerChoice.equalsIgnoreCase("yes"))
            {
                String saveFileName = "testFile.txt";
                player = new Player(getRandomElement());
                enemy = new Enemy(getRandomElement());
                rounds = GameStates.LoadGame(saveFileName, player, enemy);
                System.out.println("Game loaded successfully!");
                System.out.println("Starting from round: " + rounds);
            }
            else if(playerChoice.equalsIgnoreCase("no"))
            {
            // Character selection
            String playerElement = chooseCharacter(scanner);

            // Create player and enemy characters
            player = new Player(playerElement);
            enemy = new Enemy(getRandomElement());
            
            // Print player's and enemy's information
            System.out.println("\nYour character: " + player);
            System.out.println("Enemy character: " + enemy + "\n");
            }
            
            
            // Battle loop
            boolean defeat = fightRounds(scanner, player, enemy, rounds);

            // Prompt to play again or quit
            if(defeat == true)
            {
            System.out.println("\nWould you like to play again? (yes/no)");
            String answer = scanner.nextLine().toLowerCase();
            if (answer.equals("no")) {
                playAgain = false;
                System.out.println("Thanks for playing! Goodbye!");
            }
            }
        
        }
    }

    private static String chooseCharacter(Scanner scanner) {
        System.out.println("Choose your character by typing the name of the element:");
        System.out.println("1. Water");
        System.out.println("2. Fire");
        System.out.println("3. Light");
        System.out.println("4. Moon");
        System.out.println("5. Ice");
        System.out.println("6. Grass");

        String choice = scanner.nextLine().toLowerCase();

        switch (choice) {
            case "water":
            case "fire":
            case "light":
            case "moon":
            case "ice":
            case "grass":
                return choice.substring(0, 1).toUpperCase() + choice.substring(1); // Capitalize
            default:
                System.out.println("Invalid choice. Please choose a valid element.");
                return chooseCharacter(scanner);
        }
    }

    private static String getRandomElement() {
        String[] elements = {"Water", "Fire", "Light", "Moon", "Ice", "Grass"};
        Random rand = new Random();
        return elements[rand.nextInt(elements.length)];
    }


    private static boolean fightRounds(Scanner scanner, Player player, Enemy enemy, int rounds) {
        while (true) {
            if (!battle(scanner, player, enemy, rounds)) {
                return true;
            } else {
                System.out.print("\033[2J");
                System.out.flush();
                rounds++;
                player.gainExp(enemy.maxHealth);
                player.primaryAttackCooldown = 0;
                player.healCooldown = 0;
                System.out.println("Do you wish to continue? (yes/no)");
                String answer = scanner.nextLine().toLowerCase();
                if(answer.equals("no"))
                {
                    System.out.println("Do you wish to save the game? (yes/no)");
                    answer = scanner.nextLine().toLowerCase();
                    if(answer.equals("yes"))
                    {
                    GameStates.SaveGame("testFile.txt", player.element, player.health, player.maxHealth, player.primaryAttackDamage, player.secondaryAttackDamage, enemy.scalingFactor, enemy.scaledPrimaryAttack, enemy.scaledSecondaryAttack, player.level, player.exp, player.expRequired, rounds);
                        return false;
                    }
                }
                enemy.element = getRandomElement();
                enemy.updateStats(rounds, enemy.element);
                System.out.println("\nThe enemy has changed its element!");
                System.out.println("New enemy character: " + enemy + "\n");
            }
        }
    }

    private static boolean battle(Scanner scanner, Player player, Enemy enemy, int rounds) {
        while (player.health > 0 && enemy.health > 0) {
            System.out.println("Round:" + rounds);
            System.out.println("\n=========== CURRENT STATS ===========");
            System.out.printf("Player: %-10s | Health: %3d/%-3d\n", player.element, player.health, player.maxHealth);
            System.out.printf("Enemy:  %-10s | Health: %3d/%-3d\n", enemy.element, enemy.health, enemy.maxHealth);
            System.out.println("=====================================");

            // Player's turn
            playerTurn(scanner, player, enemy);

            // Check if enemy is defeated
            if (enemy.health <= 0) {
                System.out.println("\nYou defeated the enemy!");
                return true;
            }

            // Enemy's turn
            enemyTurn(player, enemy);

            // Check if player is defeated
            if (player.health <= 0) {
                System.out.println("\nYou were defeated by the enemy!");
                return false;
            }

            // Reduce cooldowns
            if (player.primaryAttackCooldown > 0) player.primaryAttackCooldown--;
            if (player.healCooldown > 0) player.healCooldown--;
        }
        if(player.health <= 0)
            return false;
        else
            return true;
    }

    private static void playerTurn(Scanner scanner, Player player, Enemy enemy) {
        System.out.println("\nIt's your turn!");
        System.out.println("Choose your action:");
        System.out.println("1. Primary Attack (" + player.primaryAttackDamage + " damage) [Cooldown: " + (player.primaryAttackCooldown > 0 ? player.primaryAttackCooldown + " turn(s)" : "Ready") + "]");
        System.out.println("2. Secondary Attack (" + player.secondaryAttackDamage + " damage)");
        System.out.println("3. Defend (Reduce incoming damage by 50%)");
        System.out.println("4. Heal (Restore " + player.healStrength + " health) [Cooldown: " + (player.healCooldown > 0 ? player.healCooldown + " turn(s)" : "Ready") + "]");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                if (player.primaryAttackCooldown == 0) {
                    System.out.println("\nYou used your Primary Attack!\n" + enemy.health + " -> " + Math.max((enemy.health - player.primaryAttackDamage),0));
                    enemy.health -= player.primaryAttackDamage;
                    player.primaryAttackCooldown = 2; // 2-turn cooldown
                } else {
                    System.out.println("Primary Attack is on cooldown!");
                }
                break;
            case "2":
                System.out.println("\nYou used your Secondary Attack!\n" + enemy.health + " -> " + Math.max((enemy.health - player.secondaryAttackDamage),0));
                enemy.health -= player.secondaryAttackDamage;
                break;
            case "3":
                System.out.println("\nYou defend yourself, reducing damage next turn!");
                player.isDefending = true;
                break;
            case "4":
                if (player.healCooldown == 0) {
                    int actualHeal = Math.min(player.healStrength, player.maxHealth - player.health);
                    player.health = Math.min(player.maxHealth, player.health + actualHeal);
                    System.out.println("You heal yourself for " + actualHeal + " health!\n" + (player.health - actualHeal) + " -> " + player.health);
                    player.healCooldown = 3; // 3-turn cooldown
                } else {
                    System.out.println("Heal is on cooldown!");
                }
                break;
            default:
                System.out.println("Invalid choice! You missed your turn.");
        }
    }

    private static void enemyTurn(Character player, Character enemy) {
        System.out.println("\nIt's the enemy's turn!");
        Random rand = new Random();
        int attackChoice = rand.nextInt(4); // 0 = primary, 1 = secondary

        if (attackChoice == 0) {
            System.out.println("\nThe enemy uses their Primary Attack!");
            int damage = player.isDefending ? enemy.primaryAttackDamage / 2 : enemy.primaryAttackDamage;
            player.health -= damage;
            System.out.println("You received " + damage + " damage!\n" + (player.health + damage) + " -> " + Math.max(player.health,0));
        } else if (attackChoice == 1){
            System.out.println("\nThe enemy uses their Secondary Attack!");
            int damage = player.isDefending ? enemy.secondaryAttackDamage / 2 : enemy.secondaryAttackDamage;
            player.health -= damage;
            System.out.println("You received " + damage + " damage!\n" + (player.health + damage) + " -> " + Math.max(player.health,0));
        } else if (attackChoice == 2){
            System.out.println("\nThe enemy uses Defend!");
            player.isDefending = true; 
        }  else {
            System.out.println("\nThe enemy uses heal!");
            int actualHeal = Math.min(enemy.healStrength, enemy.maxHealth - enemy.health);
            enemy.health = Math.min(enemy.maxHealth, enemy.health + actualHeal);
            System.out.println("The enemy heals for " + actualHeal + " health!\n" + (enemy.health - actualHeal) + " -> " + enemy.health);
        }
        player.isDefending = false; 
    }
}


