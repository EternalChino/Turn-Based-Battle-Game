import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

class GameStates 
{
  public static void SaveGame(String fileName, 
                              String element, 
                              int health, 
                              int maxHealth, 
                              int primaryAttackDamage, 
                              int secondaryAttackDamage, 
                              float scalingFactor, 
                              int scaledPrimaryAttack, 
                              int scaledSecondaryAttack, 
                              int level, 
                              int exp, 
                              int expRequired,
                              int rounds)
  {
      try (FileWriter saveState = new FileWriter(fileName)) {
          saveState.write(element + "\n");
          saveState.write(health + "\n");
          saveState.write(maxHealth + "\n");
          saveState.write(primaryAttackDamage + "\n");
          saveState.write(secondaryAttackDamage + "\n");
          saveState.write(scalingFactor + "\n");
          saveState.write(scaledPrimaryAttack + "\n");
          saveState.write(scaledSecondaryAttack + "\n");
          saveState.write(level + "\n");
          saveState.write(exp + "\n");
          saveState.write(expRequired + "\n");
          saveState.write(rounds + "\n");
          System.out.println("Game state saved successfully!");
      } catch (IOException error) {
          System.out.println("Error saving game state: " + error.getMessage());
      }
  }
  public static int LoadGame(String filename, Player player, Enemy enemy)
  {
    int rounds = 1;
    BufferedReader loadState = null;
    try 
    {
      loadState = new BufferedReader(new FileReader(filename));
      player.element = loadState.readLine();
      player.health = Integer.parseInt(loadState.readLine());
      player.maxHealth = Integer.parseInt(loadState.readLine());
      player.primaryAttackDamage = Integer.parseInt(loadState.readLine());
      player.secondaryAttackDamage = Integer.parseInt(loadState.readLine());
      enemy.scalingFactor = Float.parseFloat(loadState.readLine());
      enemy.scaledPrimaryAttack = Integer.parseInt(loadState.readLine());
      enemy.scaledSecondaryAttack = Integer.parseInt(loadState.readLine());
      player.level = Integer.parseInt(loadState.readLine());
      player.exp = Integer.parseInt(loadState.readLine());
      player.expRequired = Integer.parseInt(loadState.readLine());
      rounds = Integer.parseInt(loadState.readLine());
      System.out.println("Rounds loaded: " + rounds);
    } catch (IOException error) {
        System.out.println("Error loading game state: " + error.getMessage());
    }
    return rounds;
  }
}