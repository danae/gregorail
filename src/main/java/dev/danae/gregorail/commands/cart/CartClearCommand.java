package dev.danae.gregorail.commands.cart;

import dev.danae.gregorail.RailPlugin;
import dev.danae.gregorail.commands.CommandMessages;
import dev.danae.gregorail.commands.CommandExecutionType;
import dev.danae.gregorail.commands.CommandUtils;
import dev.danae.gregorail.util.commands.CommandContext;
import dev.danae.gregorail.util.commands.CommandException;
import dev.danae.gregorail.util.commands.CommandUsageException;
import dev.danae.gregorail.util.minecart.InvalidQueryException;
import dev.danae.gregorail.util.minecart.MinecartUtils;
import dev.danae.gregorail.util.minecart.QueryParser;
import dev.danae.gregorail.util.parser.ParserException;
import dev.danae.gregorail.webhooks.WebhookType;
import dev.danae.gregorail.webhooks.WebhookUtils;
import java.util.List;


public class CartClearCommand extends CartCommand
{
  // The execution type of the command
  private final CommandExecutionType executionType;
  
  
  // Constructor
  public CartClearCommand(CommandExecutionType executionType)
  {
    super("gregorail.cart.clear");
    
    this.executionType = executionType;
  }
    
  
  // Handle the command
  @Override
  public void handle(CommandContext context) throws CommandException, CommandUsageException
  {
    try
    {      
      // Assert that the command sender has a location
      context.assertSenderHasLocation();
      
      // Parse the properties
      var entityDistance = context.getPropertyAsUnsignedInt("distance", RailPlugin.getEntitySearchRadius());
    
      // Parse the arguments
      if (!context.hasAtLeastArgumentsCount(this.executionType == CommandExecutionType.CONDITIONAL ? 1 : 0))
        throw new CommandUsageException();
      
      var argumentIndex = 0;
      
      var query = this.executionType == CommandExecutionType.CONDITIONAL ? QueryParser.parseQuery(context.getArgument(argumentIndex++)) : null;
      
      var cart = this.findMinecart(context, argumentIndex++, entityDistance);
      if (cart == null)
        throw new CommandException("No cart found");
      
      // Check if the minecart matches the query
      if (query == null || MinecartUtils.matchCode(cart, query))
      {
        // Remove the code from the cart
        MinecartUtils.setCode(cart, null);

        // Execute the appropriate webhooks
        RailPlugin.getInstance().executeWebhook(WebhookType.CART_CODE_CLEARED, WebhookUtils.createCartCodeClearedPayload(cart));
      
        // Send a message about the updated cart
        CommandMessages.sendCartCodeClearedMessage(context, cart);
      }
      else
      {
        // Send a message about the unchanged cart
        CommandMessages.sendCartCodeUnchangedMessage(context, cart);
      }
    }
    catch (ParserException ex)
    {
      throw new CommandException(ex.getMessage(), ex);
    }
  }
  
  // Handle tab completion of the command
  @Override
  public List<String> handleTabCompletion(CommandContext context)
  {
    if (context.getLastArgument().startsWith("#"))
      return CommandUtils.handlePropertyTabCompletion(context.getLastArgument(), "distance=");
    
    switch (this.executionType)
    {
      case ALWAYS:
        if (context.hasAtLeastArgumentsCount(1))
          return CommandUtils.handleLocationTabCompletion(context, 0);
        else
          return null;
        
      case CONDITIONAL:
        if (context.hasAtLeastArgumentsCount(2))
          return CommandUtils.handleLocationTabCompletion(context, 1);
        else if (context.hasArgumentsCount(1))
          return CommandUtils.handleCodesTabCompletion(context.getArgument(0));
        else
          return null;
        
      default:
        return null;
    }
  }
}
