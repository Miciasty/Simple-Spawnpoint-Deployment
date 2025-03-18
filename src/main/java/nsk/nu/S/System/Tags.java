package nsk.nu.S.System;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class Tags {

    public static TagResolver getStandard() {
        return TagResolver.resolver(
                Placeholder.parsed("primary",   "<#00FFFF>"),
                Placeholder.parsed("secondary", "<#6D8CFD>"),
                Placeholder.parsed("ascent",    "<#6DFD96>"),
                Placeholder.parsed("ascent2",   "<#FDDE6D>"),

                Placeholder.parsed("-primary",      "</#00FFFF>"),
                Placeholder.parsed("-secondary",    "</#6D8CFD>"),
                Placeholder.parsed("-ascent",       "</#6DFD96>"),
                Placeholder.parsed("-ascent2",      "</#FDDE6D>")
        );
    }

    public static TagResolver getGradient() {
        return TagResolver.resolver(
                Placeholder.parsed("gprimary",   "#00FFFF"),
                Placeholder.parsed("gsecondary", "#6D8CFD"),
                Placeholder.parsed("gascent",    "#6DFD96"),
                Placeholder.parsed("gascent2",   "#FDDE6D")
        );
    }

}
