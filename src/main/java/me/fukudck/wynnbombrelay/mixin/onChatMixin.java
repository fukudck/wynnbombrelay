package me.fukudck.wynnbombrelay.mixin;


import com.wynntils.core.text.StyledText;
import com.wynntils.models.worlds.type.BombInfo;
import com.wynntils.utils.mc.StyledTextUtils;
import me.fukudck.wynnbombrelay.client.BombParse;
import me.fukudck.wynnbombrelay.client.BombSender;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.Text;




@Mixin(ChatHud.class)
public class onChatMixin {

    @Inject(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onAddMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        try {
            StyledText st = StyledText.fromComponent(message);
            StyledText unwrapped = StyledTextUtils.unwrap(st.stripAlignment());

            BombInfo bomb = BombParse.parse(unwrapped);

            if (bomb != null) {
                BombSender.send(String.valueOf(bomb.bomb()), bomb.user(),bomb.server(), bomb.getLength());
            }



        } catch (Exception e) {
            System.out.println("Error in onAddMessage: " + e);
        }
    }
}

