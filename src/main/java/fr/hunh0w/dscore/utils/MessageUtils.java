package fr.hunh0w.dscore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

import java.util.Arrays;

public class MessageUtils {

    public static Component getGradient(String str, boolean bold, String... hexColors){
        MiniMessage gradient = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.reset())
                        .resolver(StandardTags.gradient())
                        .build()
                ).build();

        String hexColorsStr = StringUtils.joinArray(Arrays.asList(hexColors), ":");
        String gradientText = "<gradient:"+hexColorsStr+">"+str+"</gradient>";
        if(bold) gradientText = "<bold>"+gradientText+"<reset>";
        return gradient.deserialize(gradientText);
    }

}
