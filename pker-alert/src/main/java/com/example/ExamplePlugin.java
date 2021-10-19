package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import java.util.regex.Matcher;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;

import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "Pker alert"
)
public class ExamplePlugin extends Plugin
{
	private static final Pattern WILDERNESS_LEVEL_PATTERN = Pattern.compile("^Level: (\\d+)$");

	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private Notifier notifier;

	@Override
	protected void startUp() throws Exception
	{
		sendChatMessage("started.");
		//log.info("Example started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		//log.info("Example stopped!");
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned event)
	{
		// in wilderness
		if (client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL) == null)
			return;
		else
			notifier.notify("Pker!");
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}

	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(message)
						.build());
	}

	private boolean combatAttackRange(Player player, final int wildernessLevel)
	{
		//if (player.getCombatLevel() >= client.getLocalPlayer().getCombatLevel() - wildernessLevel
		//&&  player.getCombatLevel() <= client.getLocalPlayer().getCombatLevel() + wildernessLevel)
		if(player.getCombatLevel() >= client.getLocalPlayer().getCombatLevel() - 100)
			return true;
		else
			return false;
	}
}
