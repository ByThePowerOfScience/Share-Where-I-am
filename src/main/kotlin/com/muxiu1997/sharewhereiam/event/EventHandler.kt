package com.muxiu1997.sharewhereiam.event

import com.muxiu1997.sharewhereiam.client.KeyShare
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointManager
import com.muxiu1997.sharewhereiam.integration.journeymap.WaypointMarker
import com.muxiu1997.sharewhereiam.network.MessageMarkWaypoint
import com.muxiu1997.sharewhereiam.network.MessageShareWaypoint
import com.muxiu1997.sharewhereiam.network.network
import com.muxiu1997.sharewhereiam.util.WaypointUtil.waypointOfLocation
import com.muxiu1997.sharewhereiam.util.WaypointUtil.waypointOfRayTrace
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import org.lwjgl.input.Keyboard

object EventHandler {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  fun handleEntityJoinWorld(event: EntityJoinWorldEvent) {
    if (event.world.isRemote) {
      if (event.entity is EntityPlayer) {
        WaypointManager.clearActiveTempBeacon()
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  fun handleKeyInput(event: InputEvent.KeyInputEvent) {
    if (KeyShare.isPressed.not()) return

    val playerWaypoint =
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
          waypointOfLocation()
        } else {
          waypointOfRayTrace()
        }

    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
      network.sendToServer(MessageShareWaypoint(playerWaypoint))
    } else {
      network.sendToServer(MessageMarkWaypoint(playerWaypoint))
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  fun handleRenderWorldLast(event: RenderWorldLastEvent) {
    WaypointMarker.render(event)
  }
}
