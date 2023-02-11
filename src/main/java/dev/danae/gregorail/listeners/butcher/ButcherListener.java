package dev.danae.gregorail.listeners.butcher;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.util.location.Cuboid;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;


public final class ButcherListener implements Listener
{
  // Reference to the plugin
  private final RailPlugin plugin;
  
  // The options for the butcher listener
  private final ButcherOptions options;
  
  
  // Constructor
  public ButcherListener(RailPlugin plugin, ButcherOptions options)
  {    
    this.plugin = plugin;
    this.options = options;
  }
  
  
  // Event listener for when a vehicle moves
  @EventHandler
  public void onVehicleMoveEvent(VehicleMoveEvent e)
  {
    // Check if the listener is enabled
    if (!this.options.isEnabled())
      return;
    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if a player is riding the minecart
    var player = MinecartUtils.getRidingPlayer(cart);
    if (player == null)
      return;
    
    // Check if the minecart moved to a new block (to avoid spamming the event)
    if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ())
      return;
    
    // Get all living entities around the minecart and kill them if they are eligible
    for (var entity : Cuboid.of(cart.getLocation(), this.options.getRadius()).findEntities(LivingEntity.class, l -> this.isEligibleToBeKilled(l, player)))
      this.kill(entity, player);
  }
  
  // Event listener for when a vehicle collides with an entity
  @EventHandler
  public void onVehicleEntityCollision(VehicleEntityCollisionEvent e)
  {
    // Check if the listener is enabled
    if (!this.options.isEnabled())
      return;
    
    // Check if the vehicle is a minecart
    if (!(e.getVehicle() instanceof RideableMinecart cart))
      return;
    
    // Check if a player is riding the minecart
    var player = MinecartUtils.getRidingPlayer(cart);
    if (player == null)
      return;
    
    // Check if the collided entity is a living entity and not a player
    if (!(e.getEntity() instanceof LivingEntity entity))
      return;
    
    // Kill the living entity if it is eligible
    if (this.isEligibleToBeKilled(entity, player))
      this.kill(entity, player);
    
    // Cancel the collision
    e.setCollisionCancelled(true);
  }
  
  
  // Check if the specified living entity is eligible to be killed
  private boolean isEligibleToBeKilled(LivingEntity entity, Player source)
  {
    if (entity instanceof Player)
      return false;
    if (this.options.getIgnoreEntitiesOfType().contains(entity.getType()))
      return false;
    if (this.options.isIgnoreNamedEntities() && entity.getCustomName() != null)
      return false;
    return true;
  }
  
  // Kill the specified living entity
  private void kill(LivingEntity entity, Player source)
  {
    if (this.options.isLightningBoltEffect())
      entity.getWorld().strikeLightningEffect(entity.getLocation());
    
    entity.damage(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), source);
  }
}
