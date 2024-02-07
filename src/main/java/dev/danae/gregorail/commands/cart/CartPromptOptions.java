package dev.danae.gregorail.commands.cart;

import org.bukkit.Material;

public class CartPromptOptions
{
  // The title to use in the cart prompt command
  private String title = "Select a code";
  
  // The material to use for items in the cart prompt command
  private Material itemMaterial = Material.MINECART;
  
  // The distance in blocks to search for a player to display the prompt to
  private int playerSearchRadius = 5;
  
  
  // Return the title to use in the cart prompt command
  public String getTitle()
  {
    return this.title;
  }
  
  // Set the title to use in the cart prompt command
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  // Return the material to use for items in the cart prompt command
  public Material getItemMaterial()
  {
    return this.itemMaterial;
  }
  
  // Set the material to use for items in the cart prompt command
  public void setItemMaterial(Material itemMaterial)
  {
    this.itemMaterial = itemMaterial;
  }
  
  // Get the distance in blocks to search for a player to display the prompt to
  public int getPlayerSearchRadius()
  {
    return this.playerSearchRadius;
  }
  
  // Set the distance in blocks to search for a player to display the prompt to
  public void setPlayerSearchRadius(int playerSearchRadius)
  {
    this.playerSearchRadius = playerSearchRadius;
  }
}
