package dev.danae.gregorail.plugin.webhooks.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.danae.gregorail.model.Manager;
import dev.danae.gregorail.model.ManagerComponent;
import dev.danae.gregorail.model.minecart.Minecart;
import dev.danae.gregorail.model.minecart.MinecartCode;
import java.lang.reflect.Type;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class MinecartSerializer extends ManagerComponent implements JsonSerializer<Minecart>
{
  // Constructor
  public MinecartSerializer(Manager manager)
  {
    super(manager);
  }
  
  
  // Serialize a world to a JSON element
  @Override
  public JsonElement serialize(Minecart cart, Type type, JsonSerializationContext context)
  {
    if (cart == null)
      return JsonNull.INSTANCE;
    
    var obj = new JsonObject();
    obj.add("uuid", new JsonPrimitive(cart.getUUID().toString()));
    obj.add("location", context.serialize(cart.getLocation(), Location.class));
    obj.add("code", context.serialize(cart.getCode(), MinecartCode.class));
    obj.add("displayName", !cart.getCode().isEmpty() ? new JsonPrimitive(ChatColor.stripColor(this.getManager().getDisplayName(cart.getCode()))) : JsonNull.INSTANCE);
    obj.add("speedMultiplier", new JsonPrimitive(cart.getSpeedMultiplier()));   
    obj.add("passenger", context.serialize(cart.getPassenger(), Player.class));
    return obj;
  }
}
