package isonomicon.visual;

import com.badlogic.gdx.math.MathUtils;

import java.nio.charset.StandardCharsets;

/**
 * Created by Tommy Ettinger on 11/4/2017.
 */
public class Coloring {

    public static int lighten(final int rgba, float amount){
//        final int r = rgba >>> 24      ;
//        final int g = rgba >>> 16 & 255;
//        final int b = rgba >>> 8 & 255 ;
//        
//        amount *= 255.5f - Math.max(r, Math.max(g, b));
//        return  (int)(r + amount) << 24 |
//                (int)(g + amount) << 16 |
//                (int)(b + amount) << 8 |
//                (rgba & 255);
        return  (int) MathUtils.lerp(rgba >>> 24, 255.5f, amount) << 24 |
                (int) MathUtils.lerp(rgba >>> 16 & 255, 255.5f, amount) << 16 |
                (int) MathUtils.lerp(rgba >>> 8 & 255, 255.5f, amount) << 8 |
                (rgba & 255);
    }
    public static int darken(final int rgba, float amount){
        amount = 1f - amount;
        return (int)((rgba >>> 24) * amount) << 24 |
                (int)((rgba >>> 16 & 255) * amount) << 16 |
                (int)((rgba >>> 8 & 255) * amount) << 8 |
                (rgba & 255);
    }



    /**
     * DawnBringer's 256-color Aurora palette, modified slightly to fit one transparent color by removing one gray.
     * Aurora is available in <a href="http://pixeljoint.com/forum/forum_posts.asp?TID=26080&KW=">this set of tools</a>
     * for a pixel art editor, but it is usable for lots of high-color purposes.
     */
    public static final int[] AURORA = {
            0x00000000, 0x010101FF, 0x131313FF, 0x252525FF, 0x373737FF, 0x494949FF, 0x5B5B5BFF, 0x6E6E6EFF,
            0x808080FF, 0x929292FF, 0xA4A4A4FF, 0xB6B6B6FF, 0xC9C9C9FF, 0xDBDBDBFF, 0xEDEDEDFF, 0xFFFFFFFF,
            0x007F7FFF, 0x3FBFBFFF, 0x00FFFFFF, 0xBFFFFFFF, 0x8181FFFF, 0x0000FFFF, 0x3F3FBFFF, 0x00007FFF,
            0x0F0F50FF, 0x7F007FFF, 0xBF3FBFFF, 0xF500F5FF, 0xFD81FFFF, 0xFFC0CBFF, 0xFF8181FF, 0xFF0000FF,
            0xBF3F3FFF, 0x7F0000FF, 0x551414FF, 0x7F3F00FF, 0xBF7F3FFF, 0xFF7F00FF, 0xFFBF81FF, 0xFFFFBFFF,
            0xFFFF00FF, 0xBFBF3FFF, 0x7F7F00FF, 0x007F00FF, 0x3FBF3FFF, 0x00FF00FF, 0xAFFFAFFF, 0xBCAFC0FF,
            0xCBAA89FF, 0xA6A090FF, 0x7E9494FF, 0x6E8287FF, 0x7E6E60FF, 0xA0695FFF, 0xC07872FF, 0xD08A74FF,
            0xE19B7DFF, 0xEBAA8CFF, 0xF5B99BFF, 0xF6C8AFFF, 0xF5E1D2FF, 0x573B3BFF, 0x73413CFF, 0x8E5555FF,
            0xAB7373FF, 0xC78F8FFF, 0xE3ABABFF, 0xF8D2DAFF, 0xE3C7ABFF, 0xC49E73FF, 0x8F7357FF, 0x73573BFF,
            0x3B2D1FFF, 0x414123FF, 0x73733BFF, 0x8F8F57FF, 0xA2A255FF, 0xB5B572FF, 0xC7C78FFF, 0xDADAABFF,
            0xEDEDC7FF, 0xC7E3ABFF, 0xABC78FFF, 0x8EBE55FF, 0x738F57FF, 0x587D3EFF, 0x465032FF, 0x191E0FFF,
            0x235037FF, 0x3B573BFF, 0x506450FF, 0x3B7349FF, 0x578F57FF, 0x73AB73FF, 0x64C082FF, 0x8FC78FFF,
            0xA2D8A2FF, 0xE1F8FAFF, 0xB4EECAFF, 0xABE3C5FF, 0x87B48EFF, 0x507D5FFF, 0x0F6946FF, 0x1E2D23FF,
            0x234146FF, 0x3B7373FF, 0x64ABABFF, 0x8FC7C7FF, 0xABE3E3FF, 0xC7F1F1FF, 0xBED2F0FF, 0xABC7E3FF,
            0xA8B9DCFF, 0x8FABC7FF, 0x578FC7FF, 0x57738FFF, 0x3B5773FF, 0x0F192DFF, 0x1F1F3BFF, 0x3B3B57FF,
            0x494973FF, 0x57578FFF, 0x736EAAFF, 0x7676CAFF, 0x8F8FC7FF, 0xABABE3FF, 0xD0DAF8FF, 0xE3E3FFFF,
            0xAB8FC7FF, 0x8F57C7FF, 0x73578FFF, 0x573B73FF, 0x3C233CFF, 0x463246FF, 0x724072FF, 0x8F578FFF,
            0xAB57ABFF, 0xAB73ABFF, 0xEBACE1FF, 0xFFDCF5FF, 0xE3C7E3FF, 0xE1B9D2FF, 0xD7A0BEFF, 0xC78FB9FF,
            0xC87DA0FF, 0xC35A91FF, 0x4B2837FF, 0x321623FF, 0x280A1EFF, 0x401811FF, 0x621800FF, 0xA5140AFF,
            0xDA2010FF, 0xD5524AFF, 0xFF3C0AFF, 0xF55A32FF, 0xFF6262FF, 0xF6BD31FF, 0xFFA53CFF, 0xD79B0FFF,
            0xDA6E0AFF, 0xB45A00FF, 0xA04B05FF, 0x5F3214FF, 0x53500AFF, 0x626200FF, 0x8C805AFF, 0xAC9400FF,
            0xB1B10AFF, 0xE6D55AFF, 0xFFD510FF, 0xFFEA4AFF, 0xC8FF41FF, 0x9BF046FF, 0x96DC19FF, 0x73C805FF,
            0x6AA805FF, 0x3C6E14FF, 0x283405FF, 0x204608FF, 0x0C5C0CFF, 0x149605FF, 0x0AD70AFF, 0x14E60AFF,
            0x7DFF73FF, 0x4BF05AFF, 0x00C514FF, 0x05B450FF, 0x1C8C4EFF, 0x123832FF, 0x129880FF, 0x06C491FF,
            0x00DE6AFF, 0x2DEBA8FF, 0x3CFEA5FF, 0x6AFFCDFF, 0x91EBFFFF, 0x55E6FFFF, 0x7DD7F0FF, 0x08DED5FF,
            0x109CDEFF, 0x055A5CFF, 0x162C52FF, 0x0F377DFF, 0x004A9CFF, 0x326496FF, 0x0052F6FF, 0x186ABDFF,
            0x2378DCFF, 0x699DC3FF, 0x4AA4FFFF, 0x90B0FFFF, 0x5AC5FFFF, 0xBEB9FAFF, 0x00BFFFFF, 0x007FFFFF,
            0x4B7DC8FF, 0x786EF0FF, 0x4A5AFFFF, 0x6241F6FF, 0x3C3CF5FF, 0x101CDAFF, 0x0010BDFF, 0x231094FF,
            0x0C2148FF, 0x5010B0FF, 0x6010D0FF, 0x8732D2FF, 0x9C41FFFF, 0x7F00FFFF, 0xBD62FFFF, 0xB991FFFF,
            0xD7A5FFFF, 0xD7C3FAFF, 0xF8C6FCFF, 0xE673FFFF, 0xFF52FFFF, 0xDA20E0FF, 0xBD29FFFF, 0xBD10C5FF,
            0x8C14BEFF, 0x5A187BFF, 0x641464FF, 0x410062FF, 0x320A46FF, 0x551937FF, 0xA01982FF, 0xC80078FF,
            0xFF50BFFF, 0xFF6AC5FF, 0xFAA0B9FF, 0xFC3A8CFF, 0xE61E78FF, 0xBD1039FF, 0x98344DFF, 0x911437FF,
    };

    /**
     * This palette was given along with the Unseven palette
     * <a href="https://www.deviantart.com/foguinhos/art/Unseven-Full-541514728">in this set of swatches</a>, but it's
     * unclear if Unseven made it, or if this palette was published in some other medium. It's a nice palette, with 8
     * levels of lightness ramp for 30 ramps with different hues. It seems meant for pixel art that includes human
     * characters, and doesn't lack for skin tones like Unseven does. It has a generally good selection of light brown
     * colors, and has been adjusted to add some dark brown colors, as well as vividly saturated purple. Many ramps also
     * become more purple as they go into darker shades.
     * <p>
     * This is organized so the colors from index 24 to index 255 inclusive are sorted by hue, from red to orange to
     * yellow to green to blue to purple, while still being organized in blocks of 8 colors at a time from bright to
     * dark. Some almost-grayscale blocks are jumbled in the middle, but they do have a hue and it is always at the
     * point where they are in the sort. A block of colors that are practically true grayscale are at indices 16-23,
     * inclusive.
     */
    public static final int[] RINSED = {
            0x00000000, 0x4444447F, 0x1111117F, 0x88FFFF00, 0x2121217F, 0x00FF003F, 0x0000FF3F, 0x0808083F,
            0xFF574600, 0xFFB14600, 0xFFFD4600, 0x4BFF4600, 0x51BF6C00, 0x4697FF00, 0x9146FF00, 0xFF46AE00,
            0xF8F9FAFF, 0xC4C3C5FF, 0x9C9C9DFF, 0x757676FF, 0x616262FF, 0x4C484AFF, 0x252626FF, 0x090304FF,
            0xD89789FF, 0xC4877AFF, 0xB47B76FF, 0xA36C72FF, 0x905861FF, 0x76454CFF, 0x5F3234FF, 0x452327FF,
            0xF9DCB8FF, 0xCEB29AFF, 0xB29891FF, 0x8F797FFF, 0x75636FFF, 0x554B67FF, 0x3E3552FF, 0x272340FF,
            0xEAA18DFF, 0xCF9180FF, 0xB87C6BFF, 0xA06A60FF, 0x905C59FF, 0x73474BFF, 0x52383EFF, 0x35242AFF,
            0xBEAE97FF, 0xB0968AFF, 0x89756EFF, 0x6E5A54FF, 0x4F413CFF, 0x413534FF, 0x2F2525FF, 0x1C1415FF,
            0xEED8A1FF, 0xE7B38CFF, 0xCC967FFF, 0xB6776DFF, 0x995A55FF, 0x803D49FF, 0x662139FF, 0x500328FF,
            0xFDFE9CFF, 0xFDD7AAFF, 0xE9BBA4FF, 0xC9A09DFF, 0xB7889AFF, 0x957088FF, 0x755B7BFF, 0x514265FF,
            0xFDF067FF, 0xFDBF60FF, 0xEF995AFF, 0xCC7148FF, 0xB65549FF, 0xA34547FF, 0x7D303FFF, 0x61242FFF,
            0xDDBBA4FF, 0xC0A68FFF, 0x9F8871FF, 0x7F6B5CFF, 0x6B5755FF, 0x5D464CFF, 0x482F3DFF, 0x30232DFF,
            0xFEF5E1FF, 0xE9DFD3FF, 0xCFC5BAFF, 0xBAAFABFF, 0xAAA291FF, 0x9A877BFF, 0x816F69FF, 0x615D56FF,
            0xFEF1A8FF, 0xE4CE85FF, 0xC9AD77FF, 0xB19169FF, 0x957859FF, 0x7B604CFF, 0x60463BFF, 0x472F2AFF,
            0xFEFC74FF, 0xE8D861FF, 0xCDAD53FF, 0xB2893EFF, 0x91672FFF, 0x7D4F21FF, 0x693C12FF, 0x562810FF,
            0xFDFCB7FF, 0xFCFA3CFF, 0xFAD725FF, 0xF5B325FF, 0xD7853CFF, 0xB25345FF, 0x8A2B2BFF, 0x67160AFF,
            0xCBD350FF, 0xB3B24BFF, 0x9A9E3AFF, 0x808B30FF, 0x647717FF, 0x4B6309FF, 0x305413FF, 0x272A07FF,
            0x8DC655FF, 0x7BA838FF, 0x6C8A37FF, 0x5D733AFF, 0x4F633CFF, 0x3F5244FF, 0x323D4AFF, 0x232A45FF,
            0xADD54BFF, 0x80B040FF, 0x599135FF, 0x35761AFF, 0x2A621FFF, 0x1E5220FF, 0x063824FF, 0x012B1DFF,
            0xE8FFEFFF, 0xA9DDC0FF, 0x95C89CFF, 0x91B48EFF, 0x759983FF, 0x627F72FF, 0x4C655CFF, 0x36514AFF,
            0x91E49DFF, 0x69C085FF, 0x4F8F62FF, 0x4A7855FF, 0x396044FF, 0x385240FF, 0x31413DFF, 0x233631FF,
            0x09EFD0FF, 0x07CCA2FF, 0x03AA83FF, 0x038D75FF, 0x04726DFF, 0x01585AFF, 0x05454EFF, 0x083142FF,
            0x97D6F9FF, 0x3EB0CAFF, 0x3C919FFF, 0x0A737CFF, 0x226171FF, 0x0B505FFF, 0x0D3948FF, 0x052935FF,
            0x91FCFCFF, 0x68DBFEFF, 0x5CB1D5FF, 0x4C8CAAFF, 0x406883FF, 0x2B4965FF, 0x29324DFF, 0x1C1E34FF,
            0x80D1FBFF, 0x62B2E7FF, 0x4D96DBFF, 0x267DB9FF, 0x195F97FF, 0x114776FF, 0x0B355AFF, 0x031D41FF,
            0xCEEEFDFF, 0xCDD7FEFF, 0xA1AED7FF, 0x898CAEFF, 0x7C7196FF, 0x5E597CFF, 0x404163FF, 0x26294CFF,
            0x8391C1FF, 0x7181CAFF, 0x5E71BEFF, 0x555FA2FF, 0x424C84FF, 0x323B6DFF, 0x2B325CFF, 0x292349FF,
            0xE3D1FDFF, 0xBAABFAFF, 0x9F94E2FF, 0x9588D7FF, 0x7B71B3FF, 0x675E9CFF, 0x4F4D7CFF, 0x333158FF,
            0xA570FFFF, 0x9462FFFF, 0x814EFFFF, 0x6C39FCFF, 0x582DC1FF, 0x472195FF, 0x412160FF, 0x2E1F38FF,
            0xF7C1E7FF, 0xD791C6FF, 0xBB6FAAFF, 0xAF6190FF, 0x924B76FF, 0x623155FF, 0x47253FFF, 0x2F0E25FF,
            0xFDC7FBFF, 0xFC9FC5FF, 0xFB71A9FF, 0xE6497EFF, 0xC33C6BFF, 0x933255FF, 0x68243FFF, 0x3F122AFF,
            0xFDDDDCFF, 0xD1ABB1FF, 0xB48C9AFF, 0x9D7482FF, 0x8B5D6EFF, 0x705057FF, 0x583C4BFF, 0x421E29FF,
            0xFCD9FBFF, 0xFDB8C7FF, 0xFD97AAFF, 0xF46E7EFF, 0xC65365FF, 0x9E303CFF, 0x741B28FF, 0x50071AFF,
    };

//    /**
//     * Big OrderedMap of a name for each color in {@link #RINSED}, mapping String keys to index values, while also
//     * allowing lookup from an index to the corresponding String key using {@link OrderedMap#keyAt(int)}.
//     * Colors with numbers after the names have 0 mean the lightest color in a ramp and 7 mean the darkest.
//     * If you're reading the source, there's a comment above each ramp saying which index that ramp would have out of
//     * the full list of 30 ramps (each with 8 colors). The first ramp, which goes from white to black, takes up index 16
//     * to index 23. Before that, there's various special-use colors, such as invisible connectors for joining parts of
//     * some larger model, and a reserved black outline for when solid black surrounds the edges of a render.
//     */
//    public static final OrderedMap<String, Integer> RINSED_NAMES = OrderedMap.makeMap(
//            "Transparent", 0, "Shadow", 1, "Black Outline", 2, "Outlined Glass", 3,
//            "Gleaming Eyes", 4, "Green Placeholder", 5, "Blue Placeholder", 6, "Dark Placeholder", 7,
//            "Connector A", 8, "Connector B", 9, "Connector C", 10, "Connector D", 11,
//            "Connector E", 12, "Connector F", 13, "Connector G", 14, "Connector H", 15,
//            //0
//            "Gray 0", 16, "Gray 1", 17, "Gray 2", 18, "Gray 3", 19,
//            "Gray 4", 20, "Gray 5", 21, "Gray 6", 22, "Gray 7", 23,
//            //1
//            "Blush Skin 0", 24, "Blush Skin 1", 25, "Blush Skin 2", 26, "Blush Skin 3", 27,
//            "Blush Skin 4", 28, "Blush Skin 5", 29, "Blush Skin 6", 30, "Blush Skin 7", 31,
//            //2
//            "Dark Deepening Skin 0", 32, "Dark Deepening Skin 1", 33, "Dark Deepening Skin 2", 34, "Dark Deepening Skin 3", 35,
//            "Dark Deepening Skin 4", 36, "Dark Deepening Skin 5", 37, "Dark Deepening Skin 6", 38, "Dark Deepening Skin 7", 39,
//            //3
//            "Warm Skin 0", 40, "Warm Skin 1", 41, "Warm Skin 2", 42, "Warm Skin 3", 43,
//            "Warm Skin 4", 44, "Warm Skin 5", 45, "Warm Skin 6", 46, "Warm Skin 7", 47,
//            //4
//            "Dark Skin 0", 48, "Dark Skin 1", 49, "Dark Skin 2", 50, "Dark Skin 3", 51,
//            "Dark Skin 4", 52, "Dark Skin 5", 53, "Dark Skin 6", 54, "Dark Skin 7", 55,
//            //5
//            "Bold Skin 0", 56, "Bold Skin 1", 57, "Bold Skin 2", 58, "Bold Skin 3", 59,
//            "Bold Skin 4", 60, "Bold Skin 5", 61, "Bold Skin 6", 62, "Bold Skin 7", 63,
//            //6
//            "Light Deepening Skin 0", 64, "Light Deepening Skin 1", 65, "Light Deepening Skin 2", 66, "Light Deepening Skin 3", 67,
//            "Light Deepening Skin 4", 68, "Light Deepening Skin 5", 69, "Light Deepening Skin 6", 70, "Light Deepening Skin 7", 71,
//            //7
//            "Yellow Orange 0", 72, "Yellow Orange 1", 73, "Yellow Orange 2", 74, "Yellow Orange 3", 75,
//            "Yellow Orange 4", 76, "Yellow Orange 5", 77, "Yellow Orange 6", 78, "Yellow Orange 7", 79,
//            //8
//            "Wood 0", 80, "Wood 1", 81, "Wood 2", 82, "Wood 3", 83,
//            "Wood 4", 84, "Wood 5", 85, "Wood 6", 86, "Wood 7", 87,
//            //9
//            "Discolored Gray 0", 88, "Discolored Gray 1", 89, "Discolored Gray 2", 90, "Discolored Gray 3", 91,
//            "Discolored Gray 4", 92, "Discolored Gray 5", 93, "Discolored Gray 6", 94, "Discolored Gray 7", 95,
//            //10
//            "Bronze Skin 0", 96, "Bronze Skin 1", 97, "Bronze Skin 2", 98, "Bronze Skin 3", 99,
//            "Bronze Skin 4", 100, "Bronze Skin 5", 101, "Bronze Skin 6", 102, "Bronze Skin 7", 103,
//            //11
//            "Gold Fur 0", 104, "Gold Fur 1", 105, "Gold Fur 2", 106, "Gold Fur 3", 107,
//            "Gold Fur 4", 108, "Gold Fur 5", 109, "Gold Fur 6", 110, "Gold Fur 7", 111,
//            //12
//            "Fire 0", 112, "Fire 1", 113, "Fire 2", 114, "Fire 3", 115,
//            "Fire 4", 116, "Fire 5", 117, "Fire 6", 118, "Fire 7", 119,
//            //13
//            "Avocado 0", 120, "Avocado 1", 121, "Avocado 2", 122, "Avocado 3", 123,
//            "Avocado 4", 124, "Avocado 5", 125, "Avocado 6", 126, "Avocado 7", 127,
//            //14
//            "Dull Green 0", 128, "Dull Green 1", 129, "Dull Green 2", 130, "Dull Green 3", 131,
//            "Dull Green 4", 132, "Dull Green 5", 133, "Dull Green 6", 134, "Dull Green 7", 135,
//            //15
//            "Vivid Green 0", 136, "Vivid Green 1", 137, "Vivid Green 2", 138, "Vivid Green 3", 139,
//            "Vivid Green 4", 140, "Vivid Green 5", 141, "Vivid Green 6", 142, "Vivid Green 7", 143,
//            //16
//            "Gray Green 0", 144, "Gray Green 1", 145, "Gray Green 2", 146, "Gray Green 3", 147,
//            "Gray Green 4", 148, "Gray Green 5", 149, "Gray Green 6", 150, "Gray Green 7", 151,
//            //17
//            "Cold Forest 0", 152, "Cold Forest 1", 153, "Cold Forest 2", 154, "Cold Forest 3", 155,
//            "Cold Forest 4", 156, "Cold Forest 5", 157, "Cold Forest 6", 158, "Cold Forest 7", 159,
//            //18
//            "Turquoise 0", 160, "Turquoise 1", 161, "Turquoise 2", 162, "Turquoise 3", 163,
//            "Turquoise 4", 164, "Turquoise 5", 165, "Turquoise 6", 166, "Turquoise 7", 167,
//            //19
//            "Coastal Water 0", 168, "Coastal Water 1", 169, "Coastal Water 2", 170, "Coastal Water 3", 171,
//            "Coastal Water 4", 172, "Coastal Water 5", 173, "Coastal Water 6", 174, "Coastal Water 7", 175,
//            //20
//            "Ice 0", 176, "Ice 1", 177, "Ice 2", 178, "Ice 3", 179,
//            "Ice 4", 180, "Ice 5", 181, "Ice 6", 182, "Ice 7", 183,
//            //21
//            "Powder Blue 0", 184, "Powder Blue 1", 185, "Powder Blue 2", 186, "Powder Blue 3", 187,
//            "Powder Blue 4", 188, "Powder Blue 5", 189, "Powder Blue 6", 190, "Powder Blue 7", 191,
//            //22
//            "Dusty Gray 0", 192, "Dusty Gray 1", 193, "Dusty Gray 2", 194, "Dusty Gray 3", 195,
//            "Dusty Gray 4", 196, "Dusty Gray 5", 197, "Dusty Gray 6", 198, "Dusty Gray 7", 199,
//            //23
//            "Blue Steel 0", 200, "Blue Steel 1", 201, "Blue Steel 2", 202, "Blue Steel 3", 203,
//            "Blue Steel 4", 204, "Blue Steel 5", 205, "Blue Steel 6", 206, "Blue Steel 7", 207,
//            //24
//            "Lavender 0", 208, "Lavender 1", 209, "Lavender 2", 210, "Lavender 3", 211,
//            "Lavender 4", 212, "Lavender 5", 213, "Lavender 6", 214, "Lavender 7", 215,
//            //25
//            "Heliotrope 0", 216, "Heliotrope 1", 217, "Heliotrope 2", 218, "Heliotrope 3", 219,
//            "Heliotrope 4", 220, "Heliotrope 5", 221, "Heliotrope 6", 222, "Heliotrope 7", 223,
//            //26
//            "Purple 0", 224, "Purple 1", 225, "Purple 2", 226, "Purple 3", 227,
//            "Purple 4", 228, "Purple 5", 229, "Purple 6", 230, "Purple 7", 231,
//            //27
//            "Hot Pink 0", 232, "Hot Pink 1", 233, "Hot Pink 2", 234, "Hot Pink 3", 235,
//            "Hot Pink 4", 236, "Hot Pink 5", 237, "Hot Pink 6", 238, "Hot Pink 7", 239,
//            //28
//            "Withered Plum 0", 240, "Withered Plum 1", 241, "Withered Plum 2", 242, "Withered Plum 3", 243,
//            "Withered Plum 4", 244, "Withered Plum 5", 245, "Withered Plum 6", 246, "Withered Plum 7", 247,
//            //29
//            "Red 0", 248, "Red 1", 249, "Red 2", 250, "Red 3", 251,
//            "Red 4", 252, "Red 5", 253, "Red 6", 254, "Red 7", 255);
//    
//    public static final int[] PURE = {
//            0x00000000, 
//            0x000000FF, 0x202020FF, 0x404040FF, 0x606060FF, 0x808080FF, 0xA0A0A0FF, 0xC0C0C0FF, 0xE0E0E0FF, 0xFFFFFFFF, //Gray
//            0xBEAE97FF, 0xB0968AFF, 0x89756EFF, 0x6E5A54FF, 0x4F413CFF, 0x413534FF, 0x2F2525FF, 0x1C1415FF, //Dark Skin
//            0xFDFE9CFF, 0xFDD7AAFF, 0xE9BBA4FF, 0xC9A09DFF, 0xB7889AFF, 0x957088FF, 0x755B7BFF, 0x514265FF, //Light Deepening Skin
//            0xDDBBA4FF, 0xC0A68FFF, 0x9F8871FF, 0x7F6B5CFF, 0x6B5755FF, 0x5D464CFF, 0x482F3DFF, 0x30232DFF, //Wood
//            0xFDFCB7FF, 0xFCFA3CFF, 0xFAD725FF, 0xF5B325FF, 0xD7853CFF, 0xB25345FF, 0x8A2B2BFF, 0x67160AFF, //Fire
//            //0x8DC655FF, 0x7BA838FF, 0x6C8A37FF, 0x5D733AFF, 0x4F633CFF, 0x3F5244FF, 0x323D4AFF, 0x232A45FF, //Dull Green
//            0xADD54BFF, 0x80B040FF, 0x599135FF, 0x35761AFF, 0x2A621FFF, 0x1E5220FF, 0x063824FF, 0x012B1DFF, //Vivid Green
//            0x97D6F9FF, 0x3EB0CAFF, 0x3C919FFF, 0x0A737CFF, 0x226171FF, 0x0B505FFF, 0x0D3948FF, 0x052935FF, //Coastal Water
//            0x8391C1FF, 0x7181CAFF, 0x5E71BEFF, 0x555FA2FF, 0x424C84FF, 0x323B6DFF, 0x2B325CFF, 0x292349FF, //Blue Steel
//            0xE3D1FDFF, 0xBAABFAFF, 0x9F94E2FF, 0x9588D7FF, 0x7B71B3FF, 0x675E9CFF, 0x4F4D7CFF, 0x333158FF, //Lavender
//            //0xF7C1E7FF, 0xD791C6FF, 0xBB6FAAFF, 0xAF6190FF, 0x924B76FF, 0x623155FF, 0x47253FFF, 0x2F0E25FF, //Purple
//            0xFCD9FBFF, 0xFDB8C7FF, 0xFD97AAFF, 0xF46E7EFF, 0xC65365FF, 0x9E303CFF, 0x741B28FF, 0x50071AFF, //Red
//    };

    public static final int[] DB8 = {
        0x00000000, 0x000000FF, 0x55415FFF, 0x646964FF, 0xD77355FF, 0x508CD7FF, 0x64B964FF, 0xE6C86EFF, 0xDCF5FFFF,
    };

    /**
     * DawnBringer16 palette, plus transparent first. Has slight changes to match the palette used in DawnLike.
     */
    public static final int[] DB16 = {
            0x00000000,
            0x140C1CFF, 0x452434FF, 0x30346DFF, 0x4D494DFF, 0x864D30FF, 0x346524FF, 0xD34549FF, 0x757161FF,
            0x597DCFFF, 0xD37D2CFF, 0x8696A2FF, 0x6DAA2CFF, 0xD3AA9AFF, 0x6DC3CBFF, 0xDBD75DFF, 0xDFEFD7FF,
    };


    /**
     * DawnBringer32 palette, plus transparent first.
     */
    public static final int[] DB32 = {
            0x00000000,
            0x000000FF, 0x222034FF, 0x45283CFF, 0x663931FF, 0x8F563BFF, 0xDF7126FF, 0xD9A066FF, 0xEEC39AFF,
            0xFBF236FF, 0x99E550FF, 0x6ABE30FF, 0x37946EFF, 0x4B692FFF, 0x524B24FF, 0x323C39FF, 0x3F3F74FF,
            0x306082FF, 0x5B6EE1FF, 0x639BFFFF, 0x5FCDE4FF, 0xCBDBFCFF, 0xFFFFFFFF, 0x9BADB7FF, 0x847E87FF,
            0x696A6AFF, 0x595652FF, 0x76428AFF, 0xAC3232FF, 0xD95763FF, 0xD77BBAFF, 0x8F974AFF, 0x8A6F30FF,
    };

    public static final int[] GB = {
            //0x00000000, 0x000000FF, 0x5B5B5BFF, 0xA4A4A4FF, 0xFFFFFFFF,
            0x00000000, 0x252525FF, 0x6E6E6EFF, 0xB6B6B6FF, 0xFFFFFFFF,
    };

    public static final int[] GB_GREEN = {
            0x00000000, 0x081820FF, 0x346856FF, 0x88C070FF, 0xE0F8D0FF
    };
    public static final int[] GRAY16 = {
            0x00000000, 0x010101FF, 0x131313FF, 0x252525FF, 0x373737FF, 0x494949FF, 0x5B5B5BFF, 0x6E6E6EFF,
            0x808080FF, 0x929292FF, 0xA4A4A4FF, 0xB6B6B6FF, 0xC9C9C9FF, 0xDBDBDBFF, 0xEDEDEDFF, 0xFFFFFFFF,
    };
    public static final int[] GRAY8 = {
            0x00000000, 0x131313FF, 0x373737FF, 0x5B5B5BFF, 
            0x808080FF, 0xA4A4A4FF, 0xC9C9C9FF, 0xEDEDEDFF,
    };
    public static final int[] AZURESTAR33 = new int[]{
            0x00000000,
            0x15111BFF, 0x112D19FF, 0x372B26FF, 0x553549FF, 0x45644FFF, 0x6E6550FF, 0xC6B5A5FF, 0xC37C6BFF,
            0xDD997EFF, 0x9A765EFF, 0xEFCBB3FF, 0xE9B58CFF, 0xFFEDD4FF, 0xE1AD56FF, 0xF7DFAAFF, 0xBBD18AFF,
            0x557A41FF, 0x355525FF, 0x62966AFF, 0x86BB9AFF, 0x15452DFF, 0x396A76FF, 0x86A2B7FF, 0x92B3DBFF,
            0x6672BFFF, 0x3D4186FF, 0x9A76BFFF, 0x925EA2FF, 0xC7A2CFFF, 0xA24D72FF, 0xE3A6BBFF, 0xC38E92FF
    };

    // Azurestar33 Ramps
    // organized from darkest to lightest, with the color being adjusted in the second-to-last spot.
    public static final byte[][] AZURESTAR_RAMPS = new byte[][]{
            {0x00, 0x00, 0x00, 0x00,},
            {0x01, 0x01, 0x01, 0x03,},
            {0x01, 0x01, 0x02, 0x15,},
            {0x01, 0x01, 0x03, 0x04,},
            {0x01, 0x03, 0x04, 0x1E,},
            {0x02, 0x15, 0x05, 0x16,},
            {0x01, 0x03, 0x06, 0x0A,},
            {0x0A, 0x20, 0x07, 0x0B,},
            {0x04, 0x1E, 0x08, 0x09,},
            {0x1E, 0x08, 0x09, 0x0C,},
            {0x03, 0x06, 0x0A, 0x08,},
            {0x20, 0x07, 0x0B, 0x0D,},
            {0x08, 0x09, 0x0C, 0x0B,},
            {0x07, 0x0B, 0x0D, 0x0D,},
            {0x06, 0x0A, 0x0E, 0x0C,},
            {0x09, 0x0C, 0x0F, 0x0D,},
            {0x13, 0x14, 0x10, 0x0F,},
            {0x15, 0x12, 0x11, 0x13,},
            {0x02, 0x15, 0x12, 0x11,},
            {0x12, 0x11, 0x13, 0x14,},
            {0x11, 0x13, 0x14, 0x10,},
            {0x01, 0x02, 0x15, 0x05,},
            {0x02, 0x15, 0x16, 0x17,},
            {0x15, 0x16, 0x17, 0x18,},
            {0x15, 0x16, 0x18, 0x0D,},
            {0x01, 0x1A, 0x19, 0x18,},
            {0x01, 0x01, 0x1A, 0x19,},
            {0x04, 0x1E, 0x1B, 0x1D,},
            {0x03, 0x04, 0x1C, 0x1B,},
            {0x1E, 0x1B, 0x1D, 0x1F,},
            {0x03, 0x04, 0x1E, 0x20,},
            {0x0A, 0x20, 0x1F, 0x0B,},
            {0x06, 0x0A, 0x20, 0x1F,},
    };
    /**
     * <a href="https://i.imgur.com/QzvjODC.png">Looks like this</a> (that also shows the ramps).
     */
    public static final int[] SPLAY32 = new int[]{
            0x00000000, 0x383838FF, 0x565E5EFF, 0x808080FF, 0x997274FF, 0x997F72FF, 0x999472FF, 0x729972FF,
            0x727699FF, 0x997298FF, 0x889F9FFF, 0xBCBCBCFF, 0xB0D7D7FF, 0xF2F2F2FF, 0xE59C78FF, 0xBF724CFF,
            0x66493AFF, 0x66603AFF, 0xBFB14CFF, 0xE5D878FF, 0x3A663AFF, 0x4CBF4CFF, 0x78E578FF, 0x3A3F66FF,
            0x4C58BFFF, 0x7883E5FF, 0xBF4CBFFF, 0xE578E5FF, 0x663A65FF, 0x663A3CFF, 0xE5787CFF, 0xBF4C50FF
    };

    // organized from darkest to lightest, with the color being adjusted in the second-to-last spot.
    public static final byte[][] SPLAY_RAMPS = new byte[][]{
            {0x00, 0x00, 0x00, 0x00},
            {0x01, 0x01, 0x01, 0x02},
            {0x01, 0x01, 0x02, 0x03},
            {0x01, 0x02, 0x03, 0x0A},
            {0x01, 0x10, 0x04, 0x05},
            {0x01, 0x10, 0x05, 0x06},
            {0x10, 0x11, 0x06, 0x0B},
            {0x01, 0x14, 0x07, 0x0A},
            {0x01, 0x17, 0x08, 0x03},
            {0x01, 0x1C, 0x09, 0x19},
            {0x02, 0x03, 0x0A, 0x0B},
            {0x03, 0x0A, 0x0B, 0x0D},
            {0x03, 0x0A, 0x0C, 0x0D},
            {0x0A, 0x0B, 0x0D, 0x0D},
            {0x10, 0x0F, 0x0E, 0x0B},
            {0x01, 0x10, 0x0F, 0x0E},
            {0x01, 0x01, 0x10, 0x04},
            {0x01, 0x10, 0x11, 0x06},
            {0x11, 0x06, 0x12, 0x13},
            {0x06, 0x12, 0x13, 0x0D},
            {0x01, 0x01, 0x14, 0x07},
            {0x14, 0x07, 0x15, 0x16},
            {0x07, 0x15, 0x16, 0x0D},
            {0x01, 0x01, 0x17, 0x08},
            {0x01, 0x17, 0x18, 0x19},
            {0x17, 0x18, 0x19, 0x0B},
            {0x01, 0x1C, 0x1A, 0x1B},
            {0x1C, 0x1A, 0x1B, 0x0E},
            {0x01, 0x01, 0x1C, 0x09},
            {0x01, 0x01, 0x1D, 0x10},
            {0x1D, 0x1F, 0x1E, 0x0E},
            {0x01, 0x1D, 0x1F, 0x1E},
    };

    /**
     * A 64-color mix of <a href="https://lospec.com/palette-list/fleja-master-palette">Fleja's Master Palette</a> with
     * <a href="https://lospec.com/palette-list/resurrect-32">Resurrect 32 by Kerrie Lake</a>. Some very similar colors
     * have been removed from the overlap, and the range of green and purple coverage has been expanded. I'd say this is
     * a good option if we want to use less total colors relative to Rinsed or Aurora. The color count is low, so the
     * Colorizer objects that use this have the shade and wave bits set, enabling extra animation options.
     * <p>
     * This is sorted so the first element is transparent, then indices 1 to 9 are grayscale (or close to it), 10 to 13
     * are brownish-gray and so don't have an especially useful hue, and the rest are sorted by hue (red-green-blue).
     */
    public static final int[] FLESURRECT = {
            0x00000000, 0x1F1833FF, 0x2B2E42FF, 0x3E3546FF,
            0x414859FF, 0x68717AFF, 0x90A1A8FF, 0xB6CBCFFF,
            0xD3E5EDFF, 0xFFFFFFFF, 0x5C3A41FF, 0x826481FF,
            0x966C6CFF, 0x715A56FF, 0xAB947AFF, 0xF68181FF,
            0xF53333FF, 0x5A0A07FF, 0xAE4539FF, 0x8A503EFF,
            0xCD683DFF, 0xFBA458FF, 0xFB6B1DFF, 0xDDBBA4FF,
            0xFDD7AAFF, 0xFFA514FF, 0xC29162FF, 0xE8B710FF,
            0xFBE626FF, 0xC0B510FF, 0xFBFF86FF, 0xB4D645FF,
            0x729446FF, 0xC8E4BEFF, 0x45F520FF, 0x51C43FFF,
            0x0E4904FF, 0x55F084FF, 0x1EBC73FF, 0x30E1B9FF,
            0x7FE0C2FF, 0xB8FDFFFF, 0x039F78FF, 0x63C2C9FF,
            0x216981FF, 0x7FE8F2FF, 0x5369EFFF, 0x4D9BE6FF,
            0x28306FFF, 0x5C76BFFF, 0x4D44C0FF, 0x180FCFFF,
            0x53207DFF, 0x8657CCFF, 0xA884F3FF, 0x630867FF,
            0xA03EB2FF, 0x881AC4FF, 0xE4A8FAFF, 0xB53D86FF,
            0xF34FE9FF, 0x7A3045FF, 0xF04F78FF, 0xC93038FF,
    };

    /**
     * Another one of DawnBringer's palettes, winner of PixelJoint's 2017 22-color palette competition.
     * This has transparent at the start so it has 23 items.
     */
    public static final int[] DB_ISO22 = {
            0x00000000, 0x0C0816FF, 0x4C4138FF, 0x70503AFF,
            0xBC5F4EFF, 0xCE9148FF, 0xE4DA6CFF, 0x90C446FF,
            0x698E34FF, 0x4D613CFF, 0x26323CFF, 0x2C4B73FF,
            0x3C7373FF, 0x558DDEFF, 0x74BAEAFF, 0xF0FAFFFF,
            0xCFB690FF, 0xB67C74FF, 0x845A78FF, 0x555461FF,
            0x746658FF, 0x6B7B89FF, 0x939388FF
    };
    
    public static final int[] JAPANESE_WOODBLOCK = {
            0x00000000, 0x2B2821FF, 0x624C3CFF, 0xD9AC8BFF, 0xE3CFB4FF, 0x243D5CFF, 0x5D7275FF, 0x5C8B93FF,
            0xB1A58DFF, 0xB03A48FF, 0xD4804DFF, 0xE0C872FF, 0x3E6958FF,
    };

    /**
     * A surprisingly-really-good auto-generated 64-color palette. The generation was done in the CIE L*A*B* color space
     * but using a simpler, seemingly-better distance metric. Like several other palettes here, many colors were
     * produced in a sub-random way and the closest pair of colors (using that simpler distance metric) repeatedly
     * merged until a threshold was reached. Here, that threshold is 63 opaque colors; this also has one fully
     * transparent color. The sub-random way of getting colors generated L* almost uniformly, and A* and B* were
     * produced by a sine wave and a cosine wave at different frequencies, allowing a non-circular span to be reachable
     * by some inputs. If an imaginary color would have been produced, all of the inputs would change slightly and it
     * would regenerate that color.
     */
    public static final int[] TWIRL64 = {
            0x00000000, 0x13071dff, 0x2e1d3fff, 0x304024ff, 0x5f1255ff, 0x491aa3ff, 0x6d3e41ff, 0x535d26ff,
            0x93150cff, 0x4e6d80ff, 0x5e4dbbff, 0x895769ff, 0x733ee4ff, 0xae4726ff, 0x9a3390ff, 0x8d7e3cff,
            0x648c9dff, 0xe46c7aff, 0x8ba5a9ff, 0xfd3dedff, 0xcf9894ff, 0x99a1deff, 0x88c262ff, 0xbaa150ff,
            0xdf8fc3ff, 0xb2bea1ff, 0xe7bdf4ff, 0xf2b57dff, 0x7befb4ff, 0xcdc8c5ff, 0x6eea3cff, 0xc6ecfaff,
            0x9fede3ff, 0xf3e8b4ff, 0xbdef9cff, 0xfaf3f7ff, 0x213366ff, 0x4e3982ff, 0x337c28ff, 0xdf3527ff,
            0xb435c0ff, 0xaf597dff, 0x9064ceff, 0xc77e37ff, 0xcd66c6ff, 0x4db9dcff, 0x98c2eeff, 0x8e0f5cff,
            0x23bf27ff, 0x9474a7ff, 0xd368faff, 0x4cb082ff, 0xfa99e6ff, 0xa8887bff, 0x32dc97ff, 0xeee037ff,
            0x4c7fdeff, 0xc0c934ff, 0x2fa632ff, 0x9c9504ff, 0xf19318ff, 0xaafb1aff, 0xfafc56ff, 0x6fd4cbff,
    };

    /**
     * A surprisingly-really-good auto-generated 256-color palette. The generation was done in the CIE L*A*B* color
     * space but using a simpler, seemingly-better distance metric. Like several other palettes here, many colors were
     * produced in a sub-random way and the closest pair of colors (using that simpler distance metric) repeatedly
     * merged until a threshold was reached. Here, that threshold is 255 opaque colors; this also has one fully
     * transparent color. The sub-random way of getting colors generated L* almost uniformly, and A* and B* were
     * produced by a sine wave and a cosine wave at different frequencies, allowing a non-circular span to be reachable
     * by some inputs. If an imaginary color would have been produced, all of the inputs would change slightly and it
     * would regenerate that color.
     */

    public static final int[] TWIRL256 = {
            0x00000000, 0x0b040fff, 0x200c1cff, 0x151811ff, 0x1f2125ff, 0x3e101dff, 0x19156bff, 0x361f56ff,
            0x3c2526ff, 0x1a3029ff, 0x1c3915ff, 0x5f1255ff, 0x233f45ff, 0x21451eff, 0x692282ff, 0x2526bbff,
            0x484141ff, 0x733153ff, 0x3b4d4bff, 0x93150cff, 0x624866ff, 0x2f5766ff, 0x466125ff, 0x58625bff,
            0x5445c6ff, 0x8d4f5eff, 0x8021e6ff, 0xb1330cff, 0x8e4494ff, 0x5c697bff, 0xc0363eff, 0x56745eff,
            0xae5340ff, 0x827122ff, 0xc54a03ff, 0x707f86ff, 0x348b9aff, 0x947c52ff, 0x8d8180ff, 0xd76553ff,
            0x86917cff, 0xf4588cff, 0x8996a4ff, 0xe86c59ff, 0xfd3dedff, 0xaf979aff, 0x7da77bff, 0x4da1e4ff,
            0x8fac36ff, 0x95b0b3ff, 0xc9a43eff, 0xcd9bbeff, 0x8aba6bff, 0xc2af97ff, 0xa9bbc4ff, 0xd5a9edff,
            0x96cd70ff, 0xe0c479ff, 0xb9c6bcff, 0xeec543ff, 0x92dcb2ff, 0xd1ceaeff, 0x54ee38ff, 0xc8e0f7ff,
            0x62f8c2ff, 0x9ce9f3ff, 0xf9d7beff, 0xf9e399ff, 0xf5e0deff, 0xaafbb0ff, 0xe2efe9ff, 0xfaf3f7ff,
            0x261348ff, 0x55081bff, 0x551e1aff, 0x583220ff, 0x1a395dff, 0x543772ff, 0x6c4123ff, 0x722da1ff,
            0x5b5545ff, 0x7a2cceff, 0x2c7341ff, 0x845e73ff, 0x6f6d8aff, 0xdd3040ff, 0xbf27c0ff, 0x678042ff,
            0xdc4718ff, 0xb3617fff, 0x6c89afff, 0x4a9873ff, 0x9579d9ff, 0xce7551ff, 0xdb65caff, 0xde8035ff,
            0xb09c4dff, 0xa49f76ff, 0xce9080ff, 0x66c0d6ff, 0xd3a6a2ff, 0xa5bb8cff, 0x85c7edff, 0xc8b5c4ff,
            0xb3cc8bff, 0x99d8d8ff, 0xd4c3d2ff, 0x8ee358ff, 0xecd0a6ff, 0xd1dfa8ff, 0xceec9dff, 0xc4f2ddff,
            0xe4eec5ff, 0x8e0f5cff, 0x775140ff, 0xa32a75ff, 0x3d66a1ff, 0x487a9eff, 0x8d746dff, 0x6f9b76ff,
            0xbb8da3ff, 0x8aaa96ff, 0x989df5ff, 0x80b942ff, 0x1bc83bff, 0xb4abbeff, 0x76cb6fff, 0xabbdefff,
            0xfac5aeff, 0x99ef2fff, 0xc3f8fdff, 0x4b4216ff, 0x5f3244ff, 0x892f45ff, 0x893f13ff, 0x6b5f18ff,
            0x4656b9ff, 0x982fd2ff, 0x5b50f2ff, 0x745aacff, 0x736f61ff, 0x39840eff, 0x816ae0ff, 0xb5789eff,
            0xd652f3ff, 0xe66d9bff, 0x41ab78ff, 0x82a4b6ff, 0x74a3eaff, 0xee86bbff, 0xe5978bff, 0x9daae9ff,
            0xfda1c6ff, 0xa4ccb0ff, 0x83e719ff, 0x7de5acff, 0xe6d5eeff, 0xbcfcc9ff, 0x062815ff, 0x4000a3ff,
            0x3e2096ff, 0x76029bff, 0x315848ff, 0x55521fff, 0x6e49aeff, 0xe52a04ff, 0xc26f37ff, 0xd063adff,
            0xa8887bff, 0xbd69f7ff, 0x659d9dff, 0x9f8fcbff, 0x57b58cff, 0x3ed08fff, 0x6fdf59ff, 0xb9dfbaff,
            0xfbe037ff, 0xa23843ff, 0xa22b9aff, 0xa843bfff, 0x9657b9ff, 0x887f29ff, 0x6e8e4bff, 0x4487daff,
            0x4497beff, 0x33b1e2ff, 0xdcab6bff, 0xf59be2ff, 0xb9d160ff, 0x26e89eff, 0xbbdb9cff, 0xf4cdf8ff,
            0x162647ff, 0x1a5d22ff, 0x9a6316ff, 0x876a92ff, 0x7a70b2ff, 0x788362ff, 0xc156c4ff, 0xba52f4ff,
            0xd0688aff, 0x01a62eff, 0x9c9504ff, 0x5faa21ff, 0x829acdff, 0x95a6cdff, 0xf19318ff, 0xf7b397ff,
            0xfdbf63ff, 0xfcbbfaff, 0xaafa89ff, 0xf7f6d2ff, 0x18013fff, 0x462fc2ff, 0xaa507aff, 0x518775ff,
            0xb06e4eff, 0xb57f2dff, 0x58a048ff, 0xdc77feff, 0xe8a53cff, 0xfa93f7ff, 0xf5ab69ff, 0xa1cc30ff,
            0xddbef2ff, 0xe0df36ff, 0x9ee99aff, 0xaafb1aff, 0xfcfd39ff, 0xf5ffb7ff, 0x412b4bff, 0x143377ff,
            0x246d7bff, 0xb55609ff, 0x5376e2ff, 0x967cb9ff, 0xc578d9ff, 0x2bb613ff, 0xb19cb6ff, 0xd393d8ff,
            0xcdc12bff, 0x84fbebff, 0xf8fb73ff, 0x77322dff, 0x483b91ff, 0x883576ff, 0xef417cff, 0x8f74f1ff,
            0x948c50ff, 0xcc8830ff, 0xf17e77ff, 0x6fd4cbff, 0xb8d304ff, 0xc4d7cfff, 0xc9f46bff, 0x975439ff,
    };

    /**
     * https://i.imgur.com/WaMdOEF.png ; I like this one. It uses 8 octahedral blobs in HSV space,
     * distributed around 8 different hues, and centered on a column through grayscale.
     * The darkest color is 0x121111FF, and the lightest color is 0xFFFE92FF (which is very light yellow,
     * oddly; the lightest grayscale color is 0xECE6E0FF).
     */
    public static final int[] ZIGGURAT64 =
        new int[] {
            0x00000000, 0x121111FF, 0x363533FF, 0x38263BFF, 0x3C262BFF, 0x293C26FF, 0x25283EFF, 0x422C2AFF,
            0x2B4242FF, 0x492E2CFF, 0x4F4F2FFF, 0x5B5856FF, 0x5A445FFF, 0x60444BFF, 0x476044FF, 0x444764FF,
            0x6B4E4BFF, 0x4C6B6AFF, 0x75524FFF, 0x7F7C79FF, 0x7F7F56FF, 0xA39F9BFF, 0xC8C3BEFF, 0xECE6E0FF,
            0xA15A54FF, 0x935853FF, 0xDB655BFF, 0x7F4641FF, 0xFF9B92FF, 0xBE7D77FF, 0xC8675DFF, 0xF09C93FF,
            0x8B6241FF, 0xCFB282FF, 0xEFED63FF, 0xAFAE5BFF, 0xFFFE92FF, 0x7B9746FF, 0x819D68FF, 0x60B554FF,
            0x8FDA85FF, 0x51854BFF, 0x3B734DFF, 0x74AE93FF, 0x559391FF, 0x96F0EEFF, 0x61C8C6FF, 0x436A7FFF,
            0x6683A3FF, 0x4E59BCFF, 0x828BE2FF, 0x484E8AFF, 0x493877FF, 0x7F689BFF, 0xC886D7FF, 0xA256B3FF,
            0x794C83FF, 0x713C67FF, 0x9D6888FF, 0xB5546CFF, 0x854B59FF, 0xDA8599FF, 0x733B41FF, 0xAE7375FF
        };
    ////ZIGGURAT64 Ramps
    public static final byte[][] ZIGGURAT_RAMPS = {
        {0x00, 0x00, 0x00, 0x00},
        {0x01, 0x01, 0x01, 0x02},
        {0x01, 0x04, 0x02, 0x08},
        {0x01, 0x01, 0x03, 0x0C},
        {0x01, 0x01, 0x04, 0x07},
        {0x01, 0x01, 0x05, 0x0E},
        {0x01, 0x01, 0x06, 0x03},
        {0x01, 0x01, 0x07, 0x09},
        {0x01, 0x01, 0x08, 0x11},
        {0x01, 0x04, 0x09, 0x0D},
        {0x01, 0x05, 0x0A, 0x0E},
        {0x04, 0x02, 0x0B, 0x13},
        {0x01, 0x03, 0x0C, 0x3A},
        {0x04, 0x09, 0x0D, 0x10},
        {0x01, 0x05, 0x0E, 0x2A},
        {0x01, 0x06, 0x0F, 0x2F},
        {0x04, 0x09, 0x10, 0x12},
        {0x01, 0x08, 0x11, 0x2C},
        {0x09, 0x0D, 0x12, 0x19},
        {0x02, 0x0B, 0x13, 0x15},
        {0x05, 0x0A, 0x14, 0x26},
        {0x0B, 0x13, 0x15, 0x16},
        {0x13, 0x15, 0x16, 0x17},
        {0x15, 0x16, 0x17, 0x24},
        {0x09, 0x1B, 0x18, 0x1D},
        {0x09, 0x1B, 0x19, 0x3F},
        {0x1B, 0x18, 0x1A, 0x1C},
        {0x04, 0x09, 0x1B, 0x19},
        {0x18, 0x1E, 0x1C, 0x16},
        {0x1B, 0x18, 0x1D, 0x1F},
        {0x1B, 0x18, 0x1E, 0x1C},
        {0x18, 0x1D, 0x1F, 0x16},
        {0x09, 0x1B, 0x20, 0x1D},
        {0x0A, 0x14, 0x21, 0x16},
        {0x25, 0x23, 0x22, 0x24},
        {0x29, 0x25, 0x23, 0x21},
        {0x25, 0x23, 0x24, 0x17},
        {0x0E, 0x29, 0x25, 0x23},
        {0x0E, 0x29, 0x26, 0x2B},
        {0x0E, 0x29, 0x27, 0x28},
        {0x29, 0x27, 0x28, 0x24},
        {0x05, 0x0E, 0x29, 0x26},
        {0x01, 0x05, 0x2A, 0x29},
        {0x11, 0x2C, 0x2B, 0x2E},
        {0x08, 0x11, 0x2C, 0x2B},
        {0x2C, 0x2E, 0x2D, 0x24},
        {0x11, 0x2C, 0x2E, 0x2D},
        {0x06, 0x0F, 0x2F, 0x30},
        {0x0F, 0x2F, 0x30, 0x15},
        {0x03, 0x34, 0x31, 0x32},
        {0x38, 0x35, 0x32, 0x15},
        {0x03, 0x34, 0x33, 0x35},
        {0x01, 0x03, 0x34, 0x33},
        {0x34, 0x38, 0x35, 0x32},
        {0x38, 0x37, 0x36, 0x1F},
        {0x34, 0x38, 0x37, 0x36},
        {0x03, 0x34, 0x38, 0x35},
        {0x01, 0x03, 0x39, 0x38},
        {0x3E, 0x3C, 0x3A, 0x3F},
        {0x3E, 0x3C, 0x3B, 0x3D},
        {0x09, 0x3E, 0x3C, 0x19},
        {0x3C, 0x3B, 0x3D, 0x1F},
        {0x04, 0x09, 0x3E, 0x1B},
        {0x1B, 0x19, 0x3F, 0x1D},
    };
    /**
     * Manually-edited version of the 64-color Dawnplumnik palette, which was made by combining DawnBringer's Iso22,
     * bman4750's Super Plum 21, and Vinik's Vinik24. Has better gray, orange, yellow, and brown coverage, and reduced
     * cyan coverage (which was very heavy originally). Repeats the first 64 colors in four blocks, with the exception
     * of the first color (transparent, which changes to black in the repeated blocks).
     * <a href="https://i.imgur.com/es1zfEU.png">Image preview</a>.
     */
    public static final int[] MANOS64 = {
            0x00000000, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D11FF, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD29FF, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C621FF, 0x319421FF, 0x4AEF31FF, 0x39AD5AFF, 0x49FF8AFF, 0x319E7AFF, 0x296B5AFF, 0x49B39AFF,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426BFF, 0x394ABDFF,
            0x2910DEFF, 0x29189CFF, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DBFF, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DEFF, 0xE773D6FF, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6BFF, 0xD66B7BFF,

            0x000000FF, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D1100, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD2900, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C62100, 0x319421FF, 0x4AEF31FF, 0x39AD5A00, 0x49FF8A00, 0x319E7AFF, 0x296B5AFF, 0x49B39A00,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426B00, 0x394ABDFF,
            0x2910DEFF, 0x29189C00, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DB00, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DE00, 0xE773D600, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6B00, 0xD66B7BFF,

            0x000000FF, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D11FF, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD29FF, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C621FF, 0x319421FF, 0x4AEF31FF, 0x39AD5AFF, 0x49FF8AFF, 0x319E7AFF, 0x296B5AFF, 0x49B39AFF,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426BFF, 0x394ABDFF,
            0x2910DEFF, 0x29189CFF, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DBFF, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DEFF, 0xE773D6FF, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6BFF, 0xD66B7BFF,

            0x000000FF, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D11FF, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD29FF, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C621FF, 0x319421FF, 0x4AEF31FF, 0x39AD5AFF, 0x49FF8AFF, 0x319E7AFF, 0x296B5AFF, 0x49B39AFF,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426BFF, 0x394ABDFF,
            0x2910DEFF, 0x29189CFF, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DBFF, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DEFF, 0xE773D6FF, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6BFF, 0xD66B7BFF,

    };
    public static final byte[][] MANOS_RAMPS = {
            {0x00, 0x00, 0x00, 0x00},
            {0x01, 0x01, 0x01, 0x2E},
            {0x01, 0x01, 0x02, 0x03},
            {0x01, 0x02, 0x03, 0x26},
            {0x2E, 0x3E, 0x04, 0x06},
            {0x03, 0x26, 0x05, 0x06},
            {0x3E, 0x04, 0x06, 0x07},
            {0x04, 0x06, 0x07, 0x29},
            {0x2A, 0x29, 0x08, 0x0A},
            {0x06, 0x07, 0x09, 0x0A},
            {0x18, 0x15, 0x0A, 0x0A},
            {0x12, 0x0C, 0x0B, 0x0F},
            {0x01, 0x12, 0x0C, 0x0E},
            {0x0C, 0x3F, 0x0D, 0x15},
            {0x12, 0x0C, 0x0E, 0x0B},
            {0x0C, 0x0E, 0x0F, 0x11},
            {0x12, 0x0C, 0x10, 0x13},
            {0x0C, 0x10, 0x11, 0x14},
            {0x01, 0x01, 0x12, 0x0C},
            {0x0C, 0x10, 0x13, 0x15},
            {0x10, 0x11, 0x14, 0x15},
            {0x16, 0x18, 0x15, 0x0A},
            {0x1E, 0x19, 0x16, 0x18},
            {0x19, 0x16, 0x17, 0x08},
            {0x19, 0x16, 0x18, 0x15},
            {0x03, 0x1E, 0x19, 0x16},
            {0x1E, 0x21, 0x1A, 0x1D},
            {0x20, 0x1C, 0x1B, 0x0A},
            {0x21, 0x20, 0x1C, 0x1B},
            {0x21, 0x1A, 0x1D, 0x08},
            {0x02, 0x03, 0x1E, 0x19},
            {0x21, 0x20, 0x1F, 0x1B},
            {0x1E, 0x21, 0x20, 0x22},
            {0x03, 0x1E, 0x21, 0x23},
            {0x21, 0x20, 0x22, 0x1F},
            {0x1E, 0x21, 0x23, 0x1D},
            {0x21, 0x20, 0x24, 0x1B},
            {0x03, 0x26, 0x25, 0x27},
            {0x02, 0x03, 0x26, 0x25},
            {0x03, 0x26, 0x27, 0x2A},
            {0x26, 0x27, 0x28, 0x08},
            {0x25, 0x2A, 0x29, 0x08},
            {0x26, 0x25, 0x2A, 0x2B},
            {0x05, 0x2C, 0x2B, 0x29},
            {0x26, 0x05, 0x2C, 0x2B},
            {0x01, 0x2E, 0x2D, 0x2C},
            {0x01, 0x01, 0x2E, 0x2D},
            {0x32, 0x31, 0x2F, 0x2D},
            {0x32, 0x31, 0x30, 0x2F},
            {0x01, 0x32, 0x31, 0x2F},
            {0x01, 0x01, 0x32, 0x2E},
            {0x32, 0x31, 0x33, 0x35},
            {0x01, 0x38, 0x34, 0x36},
            {0x01, 0x38, 0x35, 0x34},
            {0x38, 0x34, 0x36, 0x09},
            {0x01, 0x38, 0x37, 0x3A},
            {0x01, 0x01, 0x38, 0x3B},
            {0x38, 0x37, 0x39, 0x3A},
            {0x38, 0x37, 0x3A, 0x36},
            {0x01, 0x38, 0x3B, 0x3C},
            {0x38, 0x3B, 0x3C, 0x3A},
            {0x12, 0x0C, 0x3D, 0x3F},
            {0x01, 0x2E, 0x3E, 0x04},
            {0x12, 0x0C, 0x3F, 0x0D},
    };

    public static final byte[] ENCODED_MANOS =
            "\001\001\001\001\001\001\001\001\00122222222111111100000000\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001222222221111111000000000\002\002\002\002\002\001\0012222222211111111000000000\002\002\002\002\002\002\002\002222222111111111000000000\002\002\002\002\002\002\002\002\00222.....1111111000000000\002\002\002\002\002\002\002\002\002........11111///0000000\002\002\002\002\002\003\003\003\003..........////////00000\003\003\003\003\003\003\003\003\003\003........./////////////\003\003\003\003\003\003\003\003\003\003\003......../////////////\036\036\036\036\036\036\036\003\003\003\003&&&...----///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-----------//////\036\036\036\036\036\036\036\036&&&&&&&&--------------//\036\036\036\036\036\036\036\036&&&&&&&&&---------------!!!!!!!!!!&&&&&&%---------------!!!!!!!!!!!%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!!!#####%%%%%%%,,,,,,,,,,,,!!!!###########%%%'''',,,,,,,,,,       #########''''''*****,,,,,          #######'''''*******+++            ####''''''*******+++               ''''''******+++++                '''''*****++++++\"\"\"\"\"\"\"\"\"\"\"\"\"    $$$(****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\"\"\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001\00122222222111111100000000\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001222222221111111000000000\002\002\002\002\002\001\0012222222211111111000000000\002\002\002\002\002\002\002\002222222111111111000000000\002\002\002\002\002\002\002\002\002.2.....1111111000000000\002\002\002\002\002\002\002\002\002........11111///0000000\003\002\002\002\003\003\003\003\003..........////////00000\003\003\003\003\003\003\003\003\003\003........./////////////\003\003\003\003\003\003\003\003\003\003\003......../////////////\036\036\036\036\036\036\036\003\003\003\003&&&..-----///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-----------//////\036\036\036\036\036\036\036\036&&&&&&&&--------------//\036\036\036\036\036\036\036\036&&&&&&&&&---------------!!!!!!!!!!&&&&&&%---------------!!!!!!!!!!!%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!!!#####%%%%%%%,,,,,,,,,,,,!!!!###########%%%'''',,,,,,,,,,       #########''''''*****,,,,,          #######'''''*******+++            ####''''''*******+++               ''''''******+++++                '''''*****++++++\"\"\"\"\"\"\"\"\"\"\"\"\"    $$$(****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\"\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001222222221111111000000000\002\002\002\002\002\002\0012222222211111111000000000\002\002\002\002\002\002\002\002222222111111111000000000\002\002\002\002\002\002\002\002\002.......1111111000000000\002\002\002\002\002\002\002\002.........11111///0000000\003\003\003\003\003\003\003\003\003........../////////0000\003\003\003\003\003\003\003\003\003\003........./////////////\003\003\003\003\003\003\003\003\003\003\003......../////////////\036\036\036\036\036\036\036\003\003\003&&&&..-----///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-----------//////\036\036\036\036\036\036\036\036&&&&&&&&---------------/\036\036\036\036\036\036\036&&&&&&&&&&---------------!!!!!!!!!!&&&&&%%---------------!!!!!!!!!!!%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!!######%%%%%%%,,,,,,,,,,,,!!!############%%%'''',,,,,,,,,,       #########''''''*****,,,,,          #######'''''*******+++            ####''''''******++++               ''''''******+++++                '''''****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"   $$$(****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001\00122222221111111100000000\001\001\001\001\001\001\001\001222222221111111100000000\002\002\001\001\001\001\001\001222222221111111000000000\002\002\002\002\002\002\0012222222211111111000000000\002\002\002\002\002\002\002\002222222111111111000000000\002\002\002\002\002\002\002\002\002.......1111111000000000\002\002\002\002\002\002\002\002.........1111////0000000\003\003\003\003\003\003\003\003\003........../////////0000\003\003\003\003\003\003\003\003\003\003........./////////////\003\003\003\003\003\003\003\003\003\003\003.......//////////////\036\036\036\036\036\036\036\003\003\003&&&&..-----///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-----------//////\036\036\036\036\036\036\036\036&&&&&&&&---------------/\036\036\036\036\036\036\036&&&&&&&&&&---------------!!!!!!!!!!&&&&&%%---------------!!!!!!!!!!%%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!!######%%%%%%%,,,,,,,,,,,,!!!############%%''''',,,,,,,,,,       #########''''''*****,,,,,          ######''''''*******+++            ####''''''******++++               ''''''*****++++++                '''''****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"   $$$(***++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001222222221111111100000000\001\001\001\001\001\001\001\001222222221111111100000000\001\001\001\001\001\001\001\001222222221111111100000000\002\002\002\001\001\001\0012222222211111111000000000\002\002\002\002\002\002\0022222222211111111000000000\002\002\002\002\002\002\002\002222222111111111000000000\002\002\002\002\002\002\002\002\002.......1111111000000000\002\002\002\002\002\002\002\002.........1111/////000000\003\003\003\003\003\003\003\003\003..........////////////0\003\003\003\003\003\003\003\003\003\003........//////////////\003\003\003\003\003\003\003\003\003\003\003.......//////////////\036\036\036\036\036\036\036\003\003\003&&&&&------///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-----------//////\036\036\036\036\036\036\036\036&&&&&&&&---------------/\036\036\036\036\036\036\036&&&&&&&&&----------------!!!!!!!!!!&&&&%%\005\005--------------!!!!!!!!!!%%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!!######%%%%%%%,,,,,,,,,,,,!!!############%%''''',,,,,,,,,,       #########''''''*****,,,,,          ######''''''*******+++            ####''''''******++++               ''''''*****++++++                '''''****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"   $$$((**++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001222222221111111100000000\001\001\001\001\001\001\001\001222222221111111100000000\002\001\001\001\001\001\0012222222211111111000000000\002\002\002\002\001\001\0012222222211111111000000000\002\002\002\002\002\002\0022222222111111111000000000\002\002\002\002\002\002\002\00222222..11111111000000000\002\002\002\002\002\002\002\002........1111111/00000000\002\002\002\002\002\002\002\002..........11//////000000\003\003\003\003\003\003\003\003\003.........//////////////\003\003\003\003\003\003\003\003\003\003........//////////////\003\003\003\003\003\003\003\003\003\003\003.......//////////////\036\036\036\036\036\036\036\003\003&&&&&&------///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&------------/////\036\036\036\036\036\036\036\036&&&&&&&&----------------\036\036\036!!!!\031&&&&&&&&----------------!!!!!!!!!!&&&%%%\005\005\005\005\005------,,,,4!!!!!!!!!!%%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!#######%%%%%%%,,,,,,,,,,,,!!#############%'''''',,,,,,,,,,       #########''''''*****,,,,,          ######''''''*******+++             ##'''''''******++++               ''''''*****++++++                '''''****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\" $$$$((((++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\001\001\001\001\001\001\001\001222222211111111000000000\001\001\001\001\001\001\0012222222211111111000000000\002\002\001\001\001\001\0012222222211111111000000000\002\002\002\022\022\001\0012222222111111111000000000\002\002\002\022\022\022\0022222222111111111000000000\002\002\002\022\022\022\002\002.22....11111111000000000\002\002\002\002\002\002\002\002.........11111///0000000\002\002\002\002\002\002\002\003..........1////////00000\003\003\003\003\003\003\003\003\003........./////////////3\003\003\003\003\003\003\003\003\003\003........//////////////\003\003\003\003\003\003\003\003\003\003\003.......//////////////\036\036\036\036\036\036\036\036&&&&&&&------///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&------------/////\036\036\036\036\036\036\036\036&&&&&&&&----------------!!!!!!\031\031\031&&&&&&&---------------4!!!!!!!!!!\031&%%%\005\005\005\005\005\005\005----,,,,44!!!!!!!!!!%%%%%%%%%\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!!%%%%%%%%%,,,,,,,,,,,,!!!!!!########%%%%%%,,,,,,,,,,,,###############%'''''',,,,,,,,,,       #########''''''*****,,,,+          ######''''''*******+++             ##'''''''******++++               ''''''*****++++++                ''''*****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\" $$$$(((((+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\001\001\001\001\0012222222211111111100000000\022\022\022\001\001\001\0012222222211111111100000000\022\022\022\022\022\001\0012222222211111111000000000\022\022\022\022\022\022\0222222222111111111000000000\022\022\022\022\022\022\022\022222222111111111000000000\022\022\022\022\022\022\022\022........1111111000000000\002\002\002\022\022\022\022\002.........1111////0000003\003\003\003\003\003\003\003\003..........//////////0333\003\003\003\003\003\003\003\003\003........./////////////3\003\003\003\003\003\003\003\003\003\003........//////////////\036\003\003\003\003\003\003\003\003\003\003......--/////////////\036\036\036\036\036\036\036&&&&&&&-------///////////\036\036\036\036\036\036\036\036&&&&&&&--------/////////\036\036\036\036\036\036\036\036&&&&&&&-------------////\036\036\036\036\036\031\031\031&&&&&&&&----------------!!!!!\031\031\031\031&&&&&&\005\005-------------44!!!!!!!!!\031\031\031%%\005\005\005\005\005\005\005\005---,,,,,44!!!!!!!!!!%%%%%%%%\005\005\005\005\005,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!##%%%%%%%%,,,,,,,,,,,,!!!!!#########%%%%%%,,,,,,,,,,,,  #############''''''',,,,,,,,,,        ########''''''******,,,+          ######''''''*******+++             ##'''''''******++++               ''''''*****++++++                ''''*****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$(((((+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$(((((((((++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\022\022\022\022\0012222222211111111100000000\022\022\022\022\022\022\0222222222211111111100000000\022\022\022\022\022\022\0222222222111111111100000000\022\022\022\022\022\022\0222222228811111111000000000\022\022\022\022\022\022\022\022222288811111111000000033\022\022\022\022\022\022\022\022.......1111111//00000333\022\022\022\022\022\022\022\022.........111//////033333\003\003\003\022\022\022\022\022..........//////////3333\003\003\003\003\003\003\003\003\003.........////////////33\003\003\003\003\003\003\003\003\003\003......../////////////3\036\003\003\003\003\003\003\003\003\003\003&....----////////////\036\036\036\036\036\036\036&&&&&&&--------//////////\036\036\036\036\036\036\036&&&&&&&&---------////////\036\036\036\036\036\036\036\036&&&&&&&&-------------//3\031\031\031\031\031\031\031\031\031&&&&&&&---------------4!!!\031\031\031\031\031\031\031&&&&\005\005\005\005-----------444!!!!!!!!!\031\031%%\005\005\005\005\005\005\005\005\005\005-,,,,,444!!!!!!!!!!%%%%%%%%\005\005\005\005\005,,,,,,,,4!!!!!!!!!!%%%%%%%%%\005\005,,,,,,,,,,,!!!!!!!!!!##%%%%%%%%,,,,,,,,,,,,!!!!!#########%%%%%%',,,,,,,,,,,   ############''''''',,,,,,,,,,        ########''''''******,,++          ######''''''*******+++              #'''''''*****+++++               ''''''****+++++++                ''''*****+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$(((((+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$((((((((((+\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\022\022\022\022\0222222288888111111100003333\022\022\022\022\022\022\0222222288888111111100003333\022\022\022\022\022\022\022\022222888888111111100033333\022\022\022\022\022\022\022\022888888888111111000033333\022\022\022\022\022\022\022\022888888888111111/03333333\022\022\022\022\022\022\022\022.888888881111///33333333\022\022\022\022\022\022\022\022.........1///////3333333\022\022\022\022\022\022\022\022\022.........////////333333\003\003\003\003\003\003\003\003\003.........///////////333\003\003\003\003\003\003\003\003\003\003........////////////33\003\003\003\003\003\003\003\003\003\003\003&..------//////////33\036\036\036\036\036\036&&&&&&&---------////////33\036\036\036\036\036\036\036&&&&&&&-----------/////33\036\036\036\036\036\036\031\031&&&&&&&---------------33\031\031\031\031\031\031\031\031\031&&&&&&&--------------44\031\031\031\031\031\031\031\031\031\031\031&\005\005\005\005\005\005\005\005--------4444!!!!!!!\031\031\031\031%%\005\005\005\005\005\005\005\005\005\005,,,,,4444!!!!!!!!!!%%%%%%%\005\005\005\005\005\005,,,,,,,44!!!!!!!!!!%%%%%%%%%\005\005,,,,,,,,,,,!!!!!!!!!###%%%%%%%%,,,,,,,,,,,,!!!!##########%%%%%'',,,,,,,,,,,    ###########''''''',,,,,,,,,,        ########''''''*******+++          #####'''''''*******+++              ''''''''*****+++++               ''''''****+++++++                ''''****++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((((+++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$((((((((((+\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\022\022\022\022\0228888888888881111133333333\022\022\022\022\022\022\0228888888888881111133333333\022\022\022\022\022\022\0228888888888881111333333333\022\022\022\022\022\022\022\022888888888881111333333333\022\022\022\022\022\022\022\022888888888881113333333333\022\022\022\022\022\022\022\022\02288888888888//3333333333\022\022\022\022\022\022\022\022\022..8888888/////333333333\022\022\022\022\022\022\022\022\022........///////33333333\003\003\003\003\003\003\003\003\003........//////////33333\003\003\003\003\003\003\003\003\003\003........//////////3333\003\003\003\003\003\003\003\003\003\003\003..-------////////3333\036\036\036\036\036\036&&&&&&&---------//////3333\036\036\036\036\036\036\036&&&&&&&-------------/3335\031\031\031\031\031\031\031\031&&&&&&&---------------55\031\031\031\031\031\031\031\031\031&&&&&&&-------------444\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005-----444444!!!!!\031\031\031\031\031\031%%\005\005\005\005\005\005\005\005\005\005,,,,44444!!!!!!!!!%%%%%%\005\005\005\005\005\005\005\005,,,,,,,44!!!!!!!!!!%%%%%%%%\005\005\005,,,,,,,,,,,!!!!!!!#####%%%%%%%%,,,,,,,,,,,,!!!###########%%%%''',,,,,,,,,,,     ##########''''''',,,,,,,,,,        ########''''''*******+++           ####'''''''******++++             \032\032''''''*****++++++               ''''''****+++++++                ''''****++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$((((((++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$((((((((((+\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\022\022\022\022\0228888888888888833333333333\022\022\022\022\022\022\0228888888888888833333333333\022\022\022\022\022\022\0228888888888888833333333333\022\022\022\022\022\022\022\022888888888888833333333333\022\022\022\022\022\022\022\022888888888888833333333333\022\022\022\022\022\022\022\022\f88888888888833333333333\022\022\022\022\022\022\f\f\f\f8888888888833333333333\022\022\022\022\022\f\f\f\f\f..888888/////333333333\003\003\003\003\003\003\003\f\f\f.......////////3333333\003\003\003\003\003\003\003\003\003\003.......//////////33333\003\003\003\003\003\003\003\003\003\003>>>>>>---////////33333\036\036\036\036\036&&&&&>>>>>-------/////33335\036\036\036\031\031\031\031\031&&&&&>-------------55555\031\031\031\031\031\031\031\031\031&&&&&&--------------555\031\031\031\031\031\031\031\031\031\031&&&&\005\005\005-----------4445\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005---4444444\031\031\031\031\031\031\031\031\031\031\031%\005\005\005\005\005\005\005\005\005\005\005,,,444444!!!!!!!!\031%%%%%\005\005\005\005\005\005\005\005\005,,,,,,444!!!!!!!!!!%%%%%%%\005\005\005\005,,,,,,,,,,4!!!!!!######%%%%%%%,,,,,,,,,,,,,\032\032\032###########%%''''',,,,,,,,,,,   \032\032\032#########'''''''*,,,,,,,,,       #########''''''*******+++           \032###'''''''******++++             \032\032''''''*****++++++               ''''''****+++++++     \034\034\034\034\034\034      '''****++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$((((((++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\"$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\022\022\022\022\f\f\f8888888888888833333333333\022\022\022\022\f\f\f8888888888888833333333333\022\022\022\022\f\f\f\f888888888888833333333333\022\022\022\022\f\f\f\f888888888888833333333333\022\022\022\022\f\f\f\f\f88888888888833333333333\022\022\022\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f\f8888888888833333333333\f\f\f\f\f\f\f\f\f\f\f888888888//3333333333\003\003\003\003\f\f\f\f\f\f\f...8888//////33333333\003\003\003\003\003\003\003\003\f\f\f>>>>>>/////////333333\003\003\003\003\003\003\003>>>>>>>>>>>-///////533555\031\031\031\031\031\031\031\031>>>>>>>>>----////5555555\031\031\031\031\031\031\031\031\031&>>>>>>>---------555555\031\031\031\031\031\031\031\031\031&&&&>>>------------5555\031\031\031\031\031\031\031\031\031\031\031&\005\005\005\005\005\005\005-------444445\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005444444444\031\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\00544444444!!!!!!!\031\031%%%%%\005\005\005\005\005\005\005\005\005,,,,,4444!!!!!!!!!#%%%%%\005\005\005\005\005\005,,,,,,,,,44!!!!!#######%%%%%%%,,,,,,,,,,,,,\032\032\032\032\032########%%'''''',,,,,,,,,,,\032\032\032\032\032\032\032\032#######''''''',,,,,,,,,,      \032\032\032######'''''''******++++         \032\032\032\032\032#'''''''*****+++++            \032\032\032''''''****+++++++               \032''''*****+++++++\034\034\034\034\034\034\034\034\034\034\034\034\034    ''(***)++++++++\"\"\"\"\"\"\"\"\034\034\034\034\"\"$$$$$$(((((((+++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((()\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\f\f\f\f\f\f\f\f888888888888833333333333\f\f\f\f\f\f\f\f888888888888833333333333\f\f\f\f\f\f\f\f888888888888833333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f\f8888888888833333333333\f\f\f\f\f\f\f\f\f\f8888888888873333333333\f\f\f\f\f\f\f\f\f\f\f888888888873333333333\f\f\f\f\f\f\f\f\f\f\f\f>88888////5553333333\003\003\003\003\003\f\f\f\f\f>>>>>>>>////5555555555\031\031\031\031\031\031>>>>>>>>>>>>>///5555555555\031\031\031\031\031\031\031\031>>>>>>>>>>>--/5555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>------55555555\031\031\031\031\031\031\031\031\031\031\031>>>>>>>--------555555\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005----4444455\031\031\031\031\031\031\031\031\031\031\031\004\005\005\005\005\005\005\005\005\005\005\005444444444\031\031\031\031\031\031\031\031\031\031\031\004\004\005\005\005\005\005\005\005\005\005\005444444444!!!!!\031\031\031\026%%%%\005\005\005\005\005\005\005\005\005\005\005,4444444!!!!!!####%%%%%\005\005\005\005\005\005,,,,,,,,444!!!!#######%%%%%%%',,,,,,,,,,,,4\032\032\032\032\032\032\032\032#####%''''''\006,,,,,,,,,,,\032\032\032\032\032\032\032\032\032#####'''''''',,,,,,,,,, \032\032\032\032\032\032\032\032\032\032####'''''''*****+++++       \032\032\032\032\032\032\032''''''''****++++++          \032\032\032\032\032''''''****+++++++              \032\032''''*****+++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034  $$$()))))+++++++\"\"\"\"\034\034\034\034\034\034\034\034\034\034$$$$$(((((((()+)++\"\"\"\"\"\"\"\"\"\034\034\034\034$$$$$$$(((((((((())\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f\f8888888888873333333333\f\f\f\f\f\f\f\f\f\f8888888888777333333333\f\f\f\f\f\f\f\f\f\f\f888888887777333333333\f\f\f\f\f\f\f\f\f\f\f888888877777333333333\f\f\f\f\f\f\f\f\f\f\f>>>>>>777555555555555\f\f\f\f\f\f\f\f>>>>>>>>>>>5555555555555\031\031\031\031\031\031>>>>>>>>>>>>>5555555555555\031\031\031\031\031\031\031\031>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>--5555555555\031\031\031\031\031\031\031\031\031\031>>>>>>>>>\005----55555555\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005444444455\031\031\031\031\031\031\031\031\031\031\031\004\004\005\005\005\005\005\005\005\005\0054444444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\005\005\005\005\005\005\005\005444444444!!\031\026\026\026\026\026\026\026\026\004\004\004\004\005\005\005\005\005\005\005\005\00544444444!!!!!##\026\026\026\026%%%\004\005\005\005\004\004\005,,,,,444444\032\032\032\032\032###\032\032\032%%%%%'\006\006\006\006,,,,,,,,,44\032\032\032\032\032\032\032\032\032\032\032#%%''''\006\006\006\006,,,,,,,,,,\032\032\032\032\032\032\032\032\032\032\032###'''''''\006\006,,,,,,,,+\032\032\032\032\032\032\032\032\032\032\032\032\032##'''''''****++++++   \032\032\032\032\032\032\032\032\032\032\032\032''''''****+++++++         \032\032\032\032\032\032''''''****+++++++              \032\032''''****++++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$$()))))))++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$$$$(((((()))))))\"\"\"\"\"\"\034\034\034\034\034\034\034$$$$$$$((((((((()))\"\"\"\"\"\"\"\"\"\"\034\034$$$$$$$$$(((((((((()\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\f\f\f\f\f\f\f\f\f88888888888877733333333\f\f\f\f\f\f\f\f\f88888888888877733333333\f\f\f\f\f\f\f\f\f88888888888877733333333\f\f\f\f\f\f\f\f\f\f8888888888877773333333\f\f\f\f\f\f\f\f\f\f8888888888777773333333\f\f\f\f\f\f\f\f\f\f\f888888887777773333333\f\f\f\f\f\f\f\f\f\f\f;8888;777777773333333\f\f\f\f\f\f\f\f\f\f\f;;;;;;777777755555555\f\f\f\f\f\f\f\f\f\f>>>>>>>777755555555555\f\f\f\f\f\f\f>>>>>>>>>>>>5555555555555\031\031\031\031\031>>>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031\031\020>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031\031>>>>>>>>>>\005\005-555555555\031\031\031\031\031\031\031\031\031\031\031\004\005\005\005\005\005\005\005\005\005\0054444444455\031\031\031\031\031\031\031\031\031\031\004\004\004\004\005\005\005\005\005\005\005\0054444444444\031\031\031\031\031\031\031\026\026\026\004\004\004\004\004\004\005\005\005\005\005\0054444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\005\005\005\005444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004,444444444\032\032\032\032\032\032\032\032\032\032\026\026%%\006\006\006\006\006\006\006\006,,,,,44444\032\032\032\032\032\032\032\032\032\032\032\032\032'''\006\006\006\006\006\006\006,,,,,,,,4\032\032\032\032\032\032\032\032\032\032\032\032\032#'''''\006\006\006\006\006,,,,,,,+\032\032\032\032\032\032\032\032\032\032\032\032\032\032'''''''\006\006\006\006+++++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032\032''''''***++++++++      \032\032\032\032\032\032\032\032\032''''''****+++++++ \034\034\034\034\034\034\034\034\034\034\034\034\032\032\032''''\007**\007\007+++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$$())))))))+++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$$$\035((((())))))))\034\034\034\034\034\034\034\034\034\034\034\034\034$$$$$$\035\035((((((())))\"\"\"\"\"\"\"\034\034\034\034\034$$$$$$$$\035\035(((((((())\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$$(((((((((\f\f\f\f\f\f\f\f\f88888888888777777333333\f\f\f\f\f\f\f\f\f\f8888888888777777333333\f\f\f\f\f\f\f\f\f\f8888888888777777333333\f\f\f\f\f\f\f\f\f\f8888888887777777333333\f\f\f\f\f\f\f\f\f\f;888888877777777333333\f\f\f\f\f\f\f\f\f\f;;;;;;;777777777555333\f\f\f\f\f\f\f\f\f\f;;;;;;;777777777555555\f\f\f\f\f\f\f\f\f\f\f;;;;;;;77777755555555\f\f\f\f\f\f\f\f\f>>>>>>>;;77775555555555\f\f\f\f\f\f>>>>>>>>>>>>77555555555555\031\031\031\020\020\020>>>>>>>>>>>>>>555555555555\031\031\031\020\020\020\020\020>>>>>>>>>>>>>55555555555\031\031\031\031\031\020\020\020\020>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\020\020>>>>>>>>>>>44455555555\031\031\031\031\031\031\031\031\031\031\004\004\004\005\004\004\004\004\005\005\00544444444445\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\005\005\00544444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\005\005\00544444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\005\0054444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\0064444444444\032\032\032\032\032\032\032\032\032\032\026\026\026\006\006\006\006\006\006\006\006\006\006\00644444444\032\032\032\032\032\032\032\032\032\032\032\032\026\006\006\006\006\006\006\006\006\006\006\006,,,,,444\032\032\032\032\032\032\032\032\032\032\032\032\032\032''\006\006\006\006\006\006\006\006\006,,,,,66\032\032\032\032\032\032\032\032\032\032\032\032\032\032\030''''\006\006\006\006\006\006\006++++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032\032''''''**\007\007\007\007\007++++ \032\032\032\032\032\032\032\032\032\032\032\032\032\032\032''''\007\007\007\007\007\007\007\007++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\032\032\030\030\030\030\007\007\007\007\007\007++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$\035()))))))))))++\034\034\034\034\034\034\034\034\034\034\034\034\034\034$$$\035\035\035((()))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034$$$$\035\035\035\035((((())))))\037\037\037\037\034\034\034\034\034\034\034\034$$$$$$\035\035\035\035((((((()))\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$\035\035\035(((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$\035(((((((((\f\f\f\f\f\f\f\f\f\f8888888877777777755553\f\f\f\f\f\f\f\f\f\f;888888877777777755553\f\f\f\f\f\f\f\f\f\f;888888877777777755553\f\f\f\f\f\f\f\f\f\f;;88;;;777777777755553\f\f\f\f\f\f\f\f\f\f;;;;;;;777777777555555\f\f\f\f\f\f\f\f\f\f;;;;;;;777777777555555\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777777555555\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777755555555\f\f\f\f\f\f\f>>>>>>>>;;;;7775555555555\016\016\016\016\016\016\016>>>>>>>>>>>;7775555555555\020\020\020\020\020\020\020>>>>>>>>>>>>>755555555555\020\020\020\020\020\020\020\020\020>>>>>>>>>>>>55555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>>5555555555\031\031\031\031\031\020\020\020\020\020\020>>>>>>>>>>44444555555\031\031\031\031\031\031\031\031\031\020\004\004\004\004\004\004\004\004\004\004444444444444\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\005\00544444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\006\0064444444444\032\032\032\032\032\032\032\032\032\026\026\026\026\006\006\006\006\006\006\006\006\006\006\00644444444\032\032\032\032\032\032\032\032\032\032\032\032\026\006\006\006\006\006\006\006\006\006\006\006\006,444466\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\006\006\006\006\006\006\006\006\006\006\006\007\0076666\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\006\006\006\006\006\006\006\006\007\007\007\007\00766\032\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\006\006\007\007\007\007\007\007\007\007\007++\032\032\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\030\030))))))))))))+\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035(())))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034$\035\035\035\035\035\035\035(())))))))\034\034\034\034\034\034\034\034\034\034\034\034\034$$$\035\035\035\035\035\035\035(((()))))\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$\035\035\035\035\035(((((((()\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$\035\035\035((((((((\f\f\f\f\f\f\f\f\f;;;;;;;;777777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;777777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;777777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;777777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;777777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777755555\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777555555\f\f\f\f\f\f\f\f>>;;;;;;;;;7777775555555\016\016\016\016\016\016\016\016>>>>;;;;;;;7777755555555\016\016\016\016\016\016\016\016>>>>>>>>>;;7777555555555\020\020\020\020\020\020\020\020>>>>>>>>>>>>775555555555\020\020\020\020\020\020\020\020\020>>>>>>>>>>>>75555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>44555555555\020\020\020\020\020\020\020\020\020\020\020\004>>>\004\004>>>444444444555\031\031\031\031\031\020\020\020\020\020\004\004\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\006\006\006\006\0064444444444\032\032\032\032\032\032\032\026\026\026\026\026\026\006\006\006\006\006\006\006\006\006\006\00644444444\032\032\032\032\032\032\032\032\032\032\032\026\026\006\006\006\006\006\006\006\006\006\006\006\006\006466666\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\006\006\006\006\006\006\006\006\006\006\00766666\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\006\006\006\006\006\006\006\006\007\007\007\007666\032\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\006\007\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\034\034\034\034\032\032\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007+\034\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007+\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\030\030\030\030))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035()))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034$\035\035\035\035\035\035\035\035\035(()))))))\037\037\037\037\037\037\037\037\037\033\033\033$$$$\035\035\035\035\035\035\035\035((((()))\037\037\037\037\037\037\037\037\037\033\033\033\033$$$$$$\035\035\035\035\035((((((((\f\f\f\f\f\f\f\f;;;;;;;;;777777777775555\f\f\f\f\f\f\f\f;;;;;;;;;777777777775555\f\f\f\f\f\f\f\f;;;;;;;;;777777777775555\f\f\f\f\f\f\f\f;;;;;;;;;777777777775555\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777775555\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777775555\f\f\f\f\f\f\f\f\f;;;;;;;;;;7777777755555\016\016\016\016\016\016\016\016\016\016;;;;;;;;;7777777555555\016\016\016\016\016\016\016\016\016\016>;;;;;;;;7777755555555\016\016\016\016\016\016\016\016\016>>>>>;;;;;;777755555555\020\020\020\020\020\020\020\020\016>>>>>>>>>>;777555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>75555555555\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>44445555555\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004\0044444444444445\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\020\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\026\004\004\006\006\006\006\006\006\0064444444444\032\032\032\032\026\026\026\026\026\026\026\026\026\006\006\006\006\006\006\006\006\006\006\00644444444\032\032\032\032\032\032\032\032\032\032\032\026\026\030\006\006\006\006\006\006\006\006\006\006\006\006666666\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\006\006\006\006\006\006\006\006\006\006666666\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\006\006\006\006\006\006\007\007\007\007\007666\032\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\030\030\030\030))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035())))))))\037\037\037\033\033\033\033\033\033\033\033\033\033$\035\035\035\035\035\035\035\035\035\035((()\b\b))\037\037\037\037\037\033\033\033\033\033\033\033\033\033$$$\035\035\035\035\035\035\035\035(((\b\b\b\b\f\f\f\f\f\f\f;;;;;;;;;;;77777777777755\f\f\f\f\f\f\f;;;;;;;;;;;77777777777755\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777755\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777555\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777555\f\f\f\f\016\016\016\016\016;;;;;;;;;;7777777777555\016\016\016\016\016\016\016\016\016;;;;;;;;;;7777777775555\016\016\016\016\016\016\016\016\016\016;;;;;;;;;7777777755555\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;;777755555555\016\016\016\016\016\016\016\016\016\016>>>;;;;;;;777755555555\020\020\020\020\020\020\020\020\016\016>>>>>>>>>;777555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>77555555555\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>444444555555\020\020\020\020\020\020\020\020\020\020\020\020\004\004\004????4444444444444\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004?4444444444444\026\026\026\026\026\026\026\026\026\020\020\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\00644444444444\026\026\026\026\026\026\026\026\026\026\026\026\026\006\006\006\006\006\006\006\006\006\006444444444\026\026\026\026\026\026\026\026\026\026\026\026\026\023\006\006\006\006\006\006\006\006\006\00644444666\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\006\006\006\006\006\006\006\006\006\0066666666\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\006\006\006\006\006\006\006\0066666666\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\006\006\006\006\007\007\007\007\007\007\007666\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\00766\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\030\030\035\035))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035))))))))\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b)\033\033\033\033\033\033\033\033\033\033\033\033\033\033$\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\f\f\f\f\f====;;;;;;;;;77777777777795\f\f\f\f\f\016===;;;;;;;;;77777777777795\f\f\f\f\016\016===;;;;;;;;;77777777777795\016\016\016\016\016\016\016===;;;;;;;;;7777777777795\016\016\016\016\016\016\016\016==;;;;;;;;;7777777777995\016\016\016\016\016\016\016\016\016==;;;;;;;;7777777777995\016\016\016\016\016\016\016\016\016\016;;;;;;;;;7777777779955\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;;777777799555\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;;777755555555\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;777555555555\020\020\020\020\020\020\020\016\016\016\016>>>>>>>>>777555555555\020\020\020\020\020\020\020\020\020\020\016>>>>>>>>>444555555555\020\020\020\020\020\020\020\020\020\020\020\020>>?????4444444444555\020\020\020\020\020\020\020\020\020\020\020\020???????4444444444444\020\020\020\020\020\020\020\020\020\020\020\020\004\004\004????4444444444444\026\026\026\026\026\026\026\026\026\026\020\004\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\006\006\00644444444444\026\026\026\026\026\026\026\026\026\026\026\026\026\023\006\006\006\006\006\006\006\006\006444444444\026\026\026\026\026\026\026\026\026\026\026\023\023\023\023\006\006\006\006\006\006\006\006\006\0064666666\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\006\006\006\006\006\006\006\006\0066666666\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\006\006\006\006\006\006\006\0066666666\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\006\006\007\007\007\007\007\007\0076666\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\00766\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\b)))))))\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\016\016\016\016\016\016=====;;;;;;;;7777777777999\016\016\016\016\016\016=====;;;;;;;;7777777777999\016\016\016\016\016\016=====;;;;;;;;7777777777999\016\016\016\016\016\016\016=====;;;;;;;7777777779999\016\016\016\016\016\016\016\016====;;;;;;;7777777779999\016\016\016\016\016\016\016\016\016===;;;;;;;7777777799999\016\016\016\016\016\016\016\016\016\016==;;;;;;;;777779999999\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;;777799999999\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;<777999999999\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;<779995555999\020\020\020\020\020\016\016\016\016\016\016\016\016>>>???=779955555559\020\020\020\020\020\020\020\020\020\016\016\016\016>??????444555555559\020\020\020\020\020\020\020\020\020\020\020\020????????4444444::::9\020\020\020\020\020\020\020\020\020\020\020\020????????4444444::::4\020\020\020\020\020\020\020\020\020\020\020\020????????444444444444\026\026\026\026\026\026\026\026\026\026\020\004\004\004\004\004????444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004?444444444444\026\026\026\026\026\026\026\026\026\026\026\026\026\004\006\006\006\006\006\006\00644444444444\026\026\026\026\026\026\026\026\026\026\026\026\023\023\006\006\006\006\006\006\006\006\006444444466\026\026\026\026\026\026\026\026\023\023\023\023\023\023\023\006\006\006\006\006\006\006\006\00666666666\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\006\006\006\006\006\006\006\00666666666\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\006\006\006\006\006\00666666666\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\00766666\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007666\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b)))\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\016\016\016\016\016\016=========;;;;7777777799999\016\016\016\016\016\016=========;;;;7777777799999\016\016\016\016\016\016\016========;;;;7777777999999\016\016\016\016\016\016\016========;;;;7777777999999\016\016\016\016\016\016\016\016=======;;;;;777779999999\016\016\016\016\016\016\016\016\016======;;;;;777799999999\016\016\016\016\016\016\016\016\016\016=======;<<777999999999\016\016\016\016\016\016\016\016\016\016\016======<<<<99999999999\016\016\016\016\016\016\016\016\016\016\016\016======<<<99999999999\016\016\016\016\016\016\016\016\016\016\016\016\016=====<<<99999999999\020\016\016\016\016\016\016\016\016\016\016\016\016??????<<99999999999\020\020\020\020\020\020\020\020\020\016\016\016?????????<9999999999\020\020\020\020\020\020\020\020\020\020\020??????????44::::::::9\020\020\020\020\020\020\020\020\020\020\020?????????444:::::::::\020\020\020\020\020\020\020\020\020\020\020??????????444::::::::\026\026\026\026\026\026\026\026\021\021\021\004\004????????44444444:44\026\026\026\026\026\026\026\026\021\021\021\021\004\004\004\004?????44444444444\026\026\026\026\026\026\026\026\026\026\026\026\023\023\023\006\006\006\006??44444444444\026\026\026\026\026\026\026\026\026\023\023\023\023\023\023\006\006\006\r\r\r\r4444466666\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\006\006\006\r\r\r\006666666666\032\032\032\032\023\023\023\023\030\030\030\030\023\023\023\030\030\006\006\006\006\006\006666666666\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\006\006\006\006\007666666666\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007666666\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\0076666\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\025)\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035\b))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b)\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\016\016\016\016\016\016==========;;;;777999999999\016\016\016\016\016\016==========;;;;777999999999\016\016\016\016\016\016\016=========;;;;777999999999\016\016\016\016\016\016\016==========;<<777999999999\016\016\016\016\016\016\016\016=========<<<779999999999\016\016\016\016\016\016\016\016\016========<<<<99999999999\016\016\016\016\016\016\016\016\016========<<<<99999999999\016\016\016\016\016\016\016\016\016\016=======<<<<99999999999\016\016\016\016\016\016\016\016\016\016\016\016=====<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016\016====<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016???????<<<9999999999\020\020\020\020\020\020\020\020\020\016\016??????????<9999999999\020\020\020\020\020\020\020\020\020\020???????????::::::::::9\020\020\020\020\020\020\020\020\020\020???????????:::::::::::\020\020\020\020\020\020\020\017\017\017???????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????44:::::::::\026\026\026\026\021\021\021\021\021\021\021\021\021\023????????444444::::\026\026\026\026\026\026\026\021\021\021\021\023\023\023\023\023\r\r\r\r\r?4444444:::\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\030\006\006\r\r\r\r666666666\032\032\032\032\032\023\030\030\030\030\030\030\030\030\030\030\030\030\030\006\r\r\r666666666\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\0076666666\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\00766666\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\00766\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\025\025\025\025\025\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035\025))))))))))\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\b\b)))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\016\016\016\016\016\013\013==========<<<<99999999999\016\016\016\016\016\013\013==========<<<<99999999999\016\016\016\016\016\013\013==========<<<<99999999999\016\016\016\016\016\013\013\013=========<<<<99999999999\016\016\016\016\016\013\013\013=========<<<<99999999999\016\016\016\016\016\016\013\013\013=======<<<<<99999999999\016\016\016\016\016\016\013\013\013=======<<<<<99999999999\016\016\016\016\016\016\016\016\016\016======<<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016===<<<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016\016???<<<<<<<999999999\017\017\017\017\017\017\017\016\016\016\013????????<<<<999999999\017\017\017\017\017\017\017\017\017????????????<::99999999\017\017\017\017\017\017\017\017\017\017???????????::::::::::9\017\017\017\017\017\017\017\017\017\017???????????:::::::::::\021\021\021\021\021\017\017\017\017\017???????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\023\023????????::::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\023\r\r\r\r\r\r\r44:::::::\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\030\030\030\030\030\030\030\030\030\030\r\r\r\r\r66666666\032\032\032\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\00766666666\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007666666\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\025\025\025\007\007\007\007\007\007\007\00766\034\034\034\034\034\034\034\035\030\030\030\030\030\030\025\025\025\025\025\025\025\025\025\007\007\007\007\007\007\007\t\t\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\025\025\025\025))))))\t\t\t\034\034\034\034\034\034\034\034\027\027\035\035\035\035\035\035\035\035\035\035\035\035\025)))))))))\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\013\013\013\013\013\013\013\013========<<<<<99999999999\013\013\013\013\013\013\013\013========<<<<<99999999999\013\013\013\013\013\013\013\013\013=======<<<<<99999999999\013\013\013\013\013\013\013\013\013=======<<<<<99999999999\013\013\013\013\013\013\013\013\013=======<<<<<99999999999\013\013\013\013\013\013\013\013\013======<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013====<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013===<<<<<<<<9999999999\016\016\016\016\013\013\013\013\013\013\013===<<<<<<<<<999999999\017\017\017\017\017\013\013\013\013\013\013\013???<<<<<<<<999999999\017\017\017\017\017\017\017\017\013\013\013????????<<<<999999999\017\017\017\017\017\017\017\017\017????????????:::::::9999\017\017\017\017\017\017\017\017\017\017???????????:::::::::::\017\017\017\017\017\017\017\017\017\017???????????:::::::::::\021\021\021\021\021\021\017\017\017\017\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\023\023????????::::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\r\r\r\r\r\r\r\r:::::::::\021\021\021\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r666666666\024\024\024\024\024\023\023\023\023\023\023\030\030\023\023\023\023\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\023\023\030\030\030\030\030\030\030\030\030\030\030\030\r\r\r\r66666666\024\024\024\024\024\024\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\0076666666\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\025\025\025\025\025\025\025\007\007\007\007\007\0076666\034\034\034\034\035\035\035\035\035\035\035\035\035\025\025\025\025\025\025\025\025\025\025\007\007\007\007\007\t\t\t\t\034\034\027\027\027\027\027\027\027\035\035\035\035\035\035\035\035\025\025\025\025\025\025\025))\t\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\035\035\035\035\035\035\035\035\035\035\025\025\025)))))\t\t\t\033\033\033\033\033\033\033\033\033\027\027\027\027\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\t\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\013\013\013\013\013\013\013\013\013======<<<<<<99999999999\013\013\013\013\013\013\013\013\013======<<<<<<99999999999\013\013\013\013\013\013\013\013\013======<<<<<<<9999999999\013\013\013\013\013\013\013\013\013=====<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013====<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013==<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013==<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<999999999\017\017\017\017\017\017\013\013\013\013\013\013???<<<<<<<<<99999999\017\017\017\017\017\017\017\017\013\013\013???????<<<<<<99999999\017\017\017\017\017\017\017\017\017\017??????????::::::::::99\017\017\017\017\017\017\017\017\017\017??????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021????\r\r\r\r\r::::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\023\r\r\r\r\r\r\r\r\r:::::::::\021\021\021\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r666666666\021\021\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\024\024\024\024\024\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\023\023\023\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\030\030\030\030\030\030\030\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\030\030\030\030\030\030\030\030\030\025\025\025\025\007\007\0076666666\024\024\024\024\024\024\024\024\030\030\030\030\025\025\025\025\025\025\025\025\025\025\025\007\007\007\007\007\t\t\t\t\027\027\027\027\027\027\027\035\035\035\035\035\025\025\025\025\025\025\025\025\025\025\025\025\007\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\035\035\035\035\035\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\035\025\025\025\025\025\025\025\b\b\t\t\t\t\t\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\035\035\035\035\b\b\b\b\b\b\b\b\b\t\t\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\027\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\027\027\027\027\b\b\b\b\b\b\b\b\b\n\n\013\013\013\013\013\013\013\013\013\013====<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013====<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013==<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\017\017\017\017\017\017\013\013\013\013\013\013??<<<<<<<<<<99999999\017\017\017\017\017\017\017\017\013\013\013??????<<<<<:::::::999\017\017\017\017\017\017\017\017\017\017??????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017?????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r:::::::::\021\021\021\021\021\021\021\021\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r6666666::\021\021\021\021\021\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\024\024\024\024\024\024\024\024\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\030\030\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\0076666666\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\007\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\035\025\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\b\t\t\t\t\t\t\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\b\b\b\b\b\b\b\b\b\t\t\t\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\035\035\035\b\b\b\b\b\b\b\b\b\b\t\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\035\b\b\b\b\b\b\b\b\b\n\n\033\033\033\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\b\b\b\b\b\b\n\n\n\n\n\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013==<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013==<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\017\017\017\017\017\017\013\013\013\013\013\013\013?<<<<<<<<<<:9999999\017\017\017\017\017\017\017\017\017\017\013?????<<<<<:::::::::::\017\017\017\017\017\017\017\017\017\017??????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017?????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r:::::::::\021\021\021\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r\r66::::::\024\024\024\024\024\024\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\023\023\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\025\025\025\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\0256666\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\b\b\b\b\b\b\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\b\b\b\b\b\n\n\t\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\b\b\b\n\n\n\n\033\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\b\b\b\n\n\n\n\n\n\n\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\017\017\017\017\017\017\017\017\013\013\013\013\013?<<<<<<<<:::9999999\017\017\017\017\017\017\017\017\017\017\017?????<<<<::::::::::::\017\017\017\017\017\017\017\017\017\017\017?????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017?????????::::::::::::\017\017\017\017\017\017\017\017\017\017\017??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r:::::::::\021\021\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r\r\r\r:::::::\024\024\024\024\024\024\024\024\024\024\024\024\024\023\r\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\02566666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\b\b\b\b\b\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\b\n\n\n\n\n\t\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\n\n\n\n\n\n\n\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\b\n\n\n\n\n\n\n\n\n\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<99999999\017\017\017\017\017\017\017\017\017\017\013\013\013?<<<<<<<:::::999:::\017\017\017\017\017\017\017\017\017\017\017?????<<<:::::::::::::\017\017\017\017\017\017\017\017\017\017\017????????:::::::::::::\017\017\017\017\017\017\017\017\017\017\017?????????::::::::::::\021\021\021\021\017\017\017\017\017\017\017??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021?????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r\r::::::::\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r::::::\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\t6666\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\b\b\b\n\n\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\n\n\n\n\n\n\n\n\n\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\n\n\n\n\n\n\n\n\n\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n".getBytes(StandardCharsets.ISO_8859_1)
            ;
    public static final byte[] ENCODED_MANOSSUS =
            "\001\001\001\001\001\001\001\001\0012222222211111¾¾¾0000000\001\001\001\001\001\001\001\001\0012222222111111¾¾¾0000000@@\001\001\001\001\001\001®­­­2222111111¾¾¾0000000@@@BBB®®®­­­­­2211111¾¾¾¾0000000@@@BBB®®®­­­­­­¸¸111¾¾¹¹00000000@@@@BB®®®­­­­­¸¸¸¸¸1¹¹¹¹¹¶¶0000º\002\002\002DDD­­­¸¸¸¸¸¸¸¹¹¹¹¹¹¶¶¶¶¼¼º\002\002\002\002DD....¸¸¸¸¸¸¹¹¹¶¶¶¶¶¶¼ºº¡¡¡¡¡¡.....¸¸¸¹¹¹¹¶¶¶¶¶ºººº\003\003\003\003\003\003¡¡¡¡¡¡.....±±±±±±/¶¶¶¼ºººº\003\003\003\003\003\003\003¡¡¡¡¡GG..±±±±±±±°°°/µµµµµ\036\036\036\036\036\036\036\003\003\003GGGGGG±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&GG----±°°°°°°·µµµµ\036\036\036\036\036\036\036\036&&&&&&&-------°°°····µµµ\036\036\036\036\036\036\036\036&&&&&&&¨¨¨¨¨--«««¬¬¬¬¬¬¬&&&&&¨¨¨¨¨¨««««¬¬¬¬¬¬¬¨¨¨¨¨¨««««¬¬¬¬¬¬¢¢¢¢¨¨««««©¬¬¬²²!!!!!!!!¢¢¢¢¢¥¥©©©©©©©©²!!!!!!!!%%%¢¢¢¢¥¥,©©©©©©©©####%%%%%%%¥¥,,,,©©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ##'''******§§§§           '''*******§§§                  +++++             ++++\"\"\"    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"(¤¤¤¤¤\"\"\"(((((¤\037\037\037$$$$$(((((\001\001\001\001\001\001\001\001\0012222222211111¾¾¾0000000\001\001\001\001\001\001\001\001\0012222222111111¾¾¾0000000@@\001\001\001\001\001\001®2­­2222111111¾¾¾0000000@@@BBB®®®­­­­22211111¾¾¾¾0000000@@@BBB®®®­­­­­­¸¸111¾¾¹¹¾0000000@@@@BB®®®­­­­­¸¸¸¸¸1¹¹¹¹¹¶¶0000º\002\002\002DDD­­­¸¸¸¸¸¸¸¹¹¹¹¹¹¶¶¶¼¼¼º\002\002\002DDD....¸¸¸¸¸¸¹¹¹¶¶¶¶¶¶¼ºº¡¡¡¡¡......¸¸¸±¹¹¹¶¶¶¶¶ºººº\003\003\003\003\003\003¡¡¡¡¡¡.....±±±±±±/¶¶¼¼ºººº\003\003\003\003\003\003\003¡¡¡¡¡GG..±±±±±±±°°°/µµµµµ\036\036\036\036\036\036\036\003\003\003GGGGGG±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&GG-----°°°°°°·µµµµ\036\036\036\036\036\036\036\036&&&&&&&-------°°°····µµµ\036\036\036\036\036\036\036\036&&&&&&&¨¨¨¨¨--«««¬¬¬¬¬¬¬&&&&&¨¨¨¨¨¨««««¬¬¬¬¬¬¬¨¨¨¨¨¨««««¬¬¬¬¬¬¢¢¢¢¨¨««««©¬¬¬²²!!!!!!!!¢¢¢¢¢¥¥©©©©©©©©²!!!!!!!!%%%¢¢¢¢¥¥,©©©©©©©©####%%%%%%%¥¥,,,,©©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ##'''******§§§§           '''*******§§§                  +++++             ++++\"\"\"    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"(¤¤¤¤¤\"\"\"(((((¤\037\037\037$$$$$(((((C\001\001\001\001\001\001\001\0012222222111111¾¾¾¾000000CC\001\001\001\001\001\001\0012222222111111¾¾¾¾000000@@\001\001\001\001\001\001®2222222111111¾¾¾0000000@@@BBB®®®­­­­22211111¾¾¾¾0000000@@@BBB®®®­­­­­­¸11111¾¾¹¾0000000@@@@BAA®®­­­­¸¸¸¸¸¸1¹¹¹¹¹¶¶000¼º\002\002\002\002DDD­­­¸¸¸¸¸¸¸¹¹¹¹¹¹¶¶¶¼¼¼º\002\002\002DDD....¸¸¸¸¸¸¹¹¹¶¶¶¶¶¼¼ºº¡¡¡¡¡......¸¸¸±¹¹¹¶¶¶¶¼¼ººº\003\003\003\003\003\003\003¡¡¡¡¡.....±±±±±±//¶¼¼¼ººº\003\003\003\003\003\003\003¡¡¡¡GGG..±±±±±±±°°°/µµµµµ\036\036\036\036\036\036\036\003\003\003GGGGGG±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&GG-----°°°°°°·µµµµ\036\036\036\036\036\036\036\036&&&&&&&-------°°°····µµµ\036\036\036\036\036\036\036\036J&&&&&&¨¨¨¨¨--«««¬¬¬¬¬¬¬&JJ&&¨¨¨¨¨¨««««¬¬¬¬¬¬¬¨¨¨¨¨¨««««¬¬¬¬¬¬¢¢¢¢¨«««««©¬¬¬²²!!!!!!!!¢¢¢¢¢¥¥©©©©©©©©²!!!!!!!!%%%¢¢¢¥¥¥,,©©©©©©©####%%%%%%%¥¥,,,,©©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ##'''******§§§§           '''******+§§§                  +++++            ++++\"\"    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"(¤¤¤¤¤\"\"\"(((((¤\037\037\037$$$$$(((((CCC\001\001\001\001\001\0012222222111111¾¾¾¾000000CCC\001\001\001\001\001\0012222222111111¾¾¾¾000000@CC\001\001\001\001\00122222222111111¾¾¾0000000@@BBA®®®®­22222211111¾¾¾¾0000000@@@AAA®®®­­­­­­¸11111¾¾¾¾0000000@@\002\002AAA®®­­­­¸¸¸¸¸11¹¹¹¹¹¶000¼¼º\002\002\002\002\002\002DDDD­¸¸¸¸¸¸¸¸¹¹¹¹¹¶¶¶¶¼¼¼º\002\002\002DDD.....¸¸¸¸¸¹¹¹¶¶¶¶¶¼¼ºº¡¡¡¡¡.......¸¸±±¹¹¶¶¶¶¼¼ººº\003\003\003\003\003\003\003¡¡¡¡......±±±±±////¼¼¼ººº\003\003\003\003\003\003\003\003¡¡¡GGG..±±±±±±±°°°/µµµµµ\036\036\036\036\036\036\036\003\003\003GGGGGG±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&GG-----°°°°°··µµµµ\036\036\036\036\036\036\036\036&&&&&&&--------°·····µµµ\036\036\036\036\036J&&&&&&¨¨¨¨¨--«««¬¬¬¬¬¬¬JJJJ¨¨¨¨¨¨¨««««¬¬¬¬¬¬¬¨¨¨¨¨«««««¬¬¬¬¬²¢¢¢¢¨«««««©©¬²²²!!!!!!!!¢¢¢¢¢¥¥©©©©©©©©²!!!!!!!!!!%%%%¢¢¢¥¥¥,,©©©©©©©####%%%%%%¥¥¥,,,,©©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ##'''******§§§§           '''******+§§§                  +++++            ++++    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"((¤¤¤¤\"\"\"\"(((((¤\037\037\037$$$$$(((((CCCC\001\001\001\00122222222111111¾¾¾¾000000CCCC\001\001\001\00122222222111111¾¾¾¾000000CCCC\001\001\001\00122222222111111¾¾¾0000000@AAAAA®®­2222221111111¾¾¾0000000@@AAAA®®®­­2­­¸¸¸1111¾¾¾¾000000º\002\002\002\002AAAD®­­­¸¸¸¸¸¸11¹¹¹¹¹¶000¼¼º\002\002\002\002\002\002DDDD­.¸¸¸¸¸¸¸¹¹¹¹¹¶¶¶¶¼¼¼º\002\002\002DDDD.....¸¸¸¸¹¹¹¹¶¶¶¶¼¼¼ºº\003\003¡¡¡¡¡.......¸±±±±//¶¶¼¼¼ººº\003\003\003\003\003\003\003¡¡¡¡G.....±±±±±////¼¼¼ººº\003\003\003\003\003\003\003\003¡¡GGGG..±±±±±±±°°//µµµµµ\036\036\036\036\036\036\036\003\003GGGGGGG±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&G------°°°°····µµµ\036\036\036\036\036\036\036\036&&&&&&&--------°······µµJJJ&&&&&¨¨¨¨---«««¬¬¬¬¬¬¬JJJJI¨¨¨¨¨«««««¬¬¬¬¬¬¬¨¨¨¨¨«««««¬¬¬¬²²!!!¢¢¢¢¨««««©©©©²²²!!!!!!!!¢¢¢¢¥¥¥©©©©©©©²²!!!!!!!!!!%%%%¢¢¢¥¥¥,,©©©©©©©####%%%%%%¥¥¥,,,,©©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ##'''******§§§§           ''''******++§§                 +++++           ++++    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"((¤¤¤¤\"\"\"\"(((((¤\037\037\037$$$$$(((((CCCCC\001\001\00122222222111111¾¾¾¾000000CCCCC\001\001\00122222222111111¾¾¾0000000CCCCCC\001222222221111111¾¾¾0000000AAAAAAAA2222222111111¾¾¾¾0000000@@AAAAAA®­222¸¸¸¸1111¾¾¾¾000000º\002\002\002\002\002DDDD­­­¸¸¸¸¸¸11¹¹¾¾¹¶00¼¼¼º\002\002\002\002\002DDDDD...¸¸¸¸¸¸¸¹¹¹¹¶¶¶¼¼¼¼º\002\002\002\002DDDDD.....¸¸¸¸À¹¹¹¶¶¶¼¼¼¼ºº\003\003\003\003\003\003¡¡¡EE.......±±±±///¶¼¼¼¼ºº\003\003\003\003\003\003\003¡¡¡¡G.....±±±±±/////¼¼ººº\003\003\003\003\003\003\003\003\003GGGGGG.±±±±±±°°°//µµµµµ\036\036\036\036\036\036\036\003\003GGGGGGG½±±±±°°°°°··µµµµ\036\036\036\036\036\036\036\036&&&&&&G------°°°·····µµµ\036\036\036\036\036\036\036\036&&&&&&&--------········µJJJ&&&&&¨¨¨¨---«««¬¬¬¬¬¬¬JJJJII¨¨¨¨¨«««««¬¬¬¬¬¬¬¨¨¨¨¨«««««¬¬¬¬²²!!¢¢¢¢¨««««©©©²²²²!!!!!!!¢¢¢¢¥¥¥©©©©©©©²²!!!!!!!!%%%%¢¢¥¥¥¥,,©©©©©©©####%%%%%%¥¥¥,,,,,©©©©©#####'''',,,,,,,§§§####''''*****§§§§§         ###'''******§§§§           ''''******++§§                 +++++          ++++    ¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"((¤¤¤¤\"\"\"\"$(((((¤\037\037\037$$$$(((((CCCCCC\001ß222222ÈÈ111111¾¾¾¾000000CCCCCC\001ß222222ÈÈ111111¾¾¾¾000000CCCCCCßßß2222ÈÈÈ111111¾¾¾¾000000AAAAAAßßß222ÈÈÈÈ11111¾¾¾¾000000º\002AAAAAAAD22ÈÈÈÈÈ11111¾¾¾¾0000¼¼º\002\002\002DDDDDDD­¸¸¸¸¸¸¸111¾¾¾¾000¼¼¼º\002\002\002\002DDDDDD....¸¸¸¸¸À¹¹¹¹¹¼¼¼¼¼¼º\002\002\002\002DDDDDE......¸ÀÀÀÀ¹¹¶¶¼¼¼¼¼¼º\003\003\003\003\003\003\003EEEE......ÀÀ±±/////¼¼¼¼ºº\003\003\003\003\003\003\003\003¡EEGG....±±±±//////¼¼ººº\003\003\003\003\003\003\003\003\003GGGGGGG±±±±±±/////µµµµµ\036\036\036\036\036\036\036\036&GGGGGGG½½±±±°°°°°··µµµµ\036\036\036\036\036\036\036\036&&&&&&&-------°°·····µµµ\036\036\036\036\036\036\036\036&&&&&&&--------·········JJJJJ&&II¨¨¨¨---«««¬¬¬¬¬¬¬JJJJJII¨¨¨¨¨«««««¬¬¬¬¬¬²¨¨¨¨¨«««««¬¬¬²²²¢¢¢¢¢¢«««©©©©²²²²!!!!!!!¢¢¢¢¥¥¥©©©©©©©²²!!!!!!%%%%%¥¥¥¥¥,,©©©©©©©#####%%%%%¥¥¥,,,,,©©©©©#####'''',,,,,,,§§§###''''*****§§§§§          ##''''******§§§§           '''******++§§                +++++      ++++   ¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"((¤¤¤¤\"\"\"\"\"$((((((\037\037\037$$$$$(((((CCCCCßßßßßÈÈÈÈÈÈÈÈ1111¾¾¾¾000000CCCCCßßßßßÈÈÈÈÈÈÈÈ1111¾¾¾¾000000CCCßßßßßßßÈÈÈÈÈÈÈÈ1111¾¾¾¾000000AAAAßßßßßßÒÈÈÈÈÈÈ11111¾¾¾¾0000¼º\022\022\022AADDßßßßÈÈÈÈÈÈ1111¾¾¾¾000¼¼¼º\022\022\022\022DDDDDDÌÌÌÈÈÈÈ1111¾¾¾¾00¼¼¼¼º\002\002\002\022DDDDDDÌÌÌÌÌ¸¸ÀÀÀÀ¹¾¹¼¼¼¼¼¼¼º\003\003\003\003DDDEEE.....ÀÀÀÀÀÀÀ///¼¼¼¼¼¼º\003\003\003\003\003\003EEEEE.....ÀÀÀÀÀ/////¼¼¼¼ººt\003\003\003\003\003\003\003EEGGG...±±±±±//////¼¼ººº\036\003\003\003\003\003\003\003\003GGGGGG½½±±±±±/////µµµµµ\036\036\036\036\036\036\036&&GGGGGG½½½½±±°°°°···µµµµ\036\036\036\036\036\036\036\036&&&&&&F-------°······µµµ\036\036\036\036\036\036\036JJ&&&&&&¨-------«·······»JJJJJJJIII¨¨¨¨--««««¬¬¬¬¬¬¬JJJJJJIII¨¨¨¨¨«««««¬¬¬¬¬²¢¨¨¨¨¨«««««¬¬²²²²¢¢¢¢¢¢«««©©©©²²²²!!!!!!!¢¢¢¢¥¥¥©©©©©©©²²!!!!!#%%%%%%¥¥¥¥¥,,,©©©©©©#####%%%%%¥¥¥,,,,,©©©©©#####''''',,,,,,,§§§###''''*****§§§§§          ##''''******§§§§           '''******++§§               ++++++      ++++   ¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"((¤¤¤¤\"\"\"\"\"\"$$(((((((\037\037\037\037$$$$$(((((CCóßßßßßßßÒÈÈÈÈÈÈÈÈÉÉ11¾¾¾¾000ÇÇóóóóßßßßßßÒÒÈÈÈÈÈÈÈÉÉ11¾¾¾0000ÇÇóóóóßßßßßßÒÒÈÈÈÈÈÈÈÉÉ1¾¾¾¾000¼ÇÇ\022\022\022\022óßßßßßÒÒÈÈÈÈÈÈÉÉ11¾¾¾¾00¼¼¼¼\022\022\022\022\022ßßßßßßÒÈÈÈÈÈÈ111¾¾¾¾00¼¼¼¼¼\022\022\022\022\022DDDDÌÌÌÌÌÈÈÈÀÀÀÀ¾¾¾¼¼¼¼¼¼¼¼\022\022\022\022\022DDDDÌÌÌÌÌÌÀÀÀÀÀÀÀ//¼¼¼¼¼¼¼¼\003\003\003\022\022EEEEEEÌÌÌÌÀÀÀÀÀÀÀ///¼¼¼¼¼¼¼\003\003\003\003\003\003EEEEE.....ÀÀÀÀÀ/////¼¼¼¼ººtttt\003\003\003EEEGGGG..ÀÀÀÀ///////¼¼ºººtttttt\003\003GGGGGGG½½½±±±/////···µµµ\036\036\036\036\036tt&&FGGFG½½½½½-±°°······µµµ\036\036\036\036\036\036\036&&&&&&FF-------°·······µ»\036\036\036\036\036JJJJJ&&&&&¨-------«······»»JJJJJJIIII¨¨¨¨-«««««¬¬¬¬¬¬²JJJJJIIII¨¨¨¨¨«««««¬¬¬¬²²I¢¨¨¨¨¨«««««¬²²²²²!¢¢¢¢¢¥¥«©©©©²²²²²!!!!!!!¢¢¢¥¥¥¥©©©©©©²²²!!!!!%%%%%¥¥¥¥¥,,,©©©©©©#####%%%%¥¥¥¥,,,,,©©©©©#####''''',,,,,,ª§§§###'''''*****§§§§§          ''''******§§§§          '' *****++++§        ++++++     +++++   ¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"((((¤¤¤\"\"\"\"\"\"\"\"$$((((((\037\037\037\037\037$$$$$(((((óóóóóßßßßÒÒÒÒÈÈÈÈÈÉÉÉÉÉÉ¾¾ÇÇÇÇÇÇóóóóóßßßßÒÒÒÒÈÈÈÈÈÉÉÉÉÉÉ¾¾ÇÇÇÇÇÇóóóóóóßßßÒÒÒÒÈÈÈÈÈÉÉÉÉÉÉ¾ÇÇÇÇÇÇÇ\022\022\022óóóßßßßÒÒÒÈÈÈÈÉÉÉÉÉÉÉ¾ÇÇÇÇÇÇÇ\022\022\022\022\022\022ßßßßÒÒÒÈÈÈÉÉÉÉÉÉÉÉ¾ÇÇÇÇÇÇÇ\022\022\022\022\022\022\022ßÌÌÌÌÌÌÌÀÀÀÀÀÀÉÉÉ¼33ÇÇÇÇÇ\022\022\022\022\022\022DDêÌÌÌÌÌÌÀÀÀÀÀÀÀ///3¼¼¼¼¼¼\022\022\022\022\022EEEEEÌÌÌÌÌÀÀÀÀÀÀÀ////¼¼¼¼¼¼ttttttEEEEEEÌÌÌÌÀÀÀÀÀ//////¼¼¼¼ºtttttt\003EEGGGGGG½ÀÀÀÀ////////¼ºººttttttt\003FGGGGGG½½½½±±////·····ºµ\036\036ttttt&FFFFFF½½½½½--°········µµ\036\036\036\036\036\036\036J&&FFFFF½½------·······»»\036\036\036\036JJJJJJJIIIHH-------«·····»»»\031\031\031\031\031\031JJJJJIIII¨¨¨¨-«««««¬¬¬¬»»»JJJJJIIII¨¨¨¨««««««¬¬²²²²I¢¢\005¨\005¨¯«««¯«¬²²²²²¢¢¢¢\005\005¥¯¯¯©©²²²²²!!!!!!!%%¢¢¥¥¥¥¥,©©©©©²²²!!!!\177\177%%%%%¥¥¥¥¥,,,©©©©©©######%%%%¥¥¥¥,,,,,ª©©©©####''''',,,,,ªª§§§###'''''*****§§§§§         ''''******§§§§            *****++++§     ++++++     +++++  ¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"((((¤¤¤\"\"\"\"\"\"\"\"\"\"$$$((((((\037\037\037\037\037\037\037$$$$$$(((((óóóóóóßßßÒÒÒÒÒÈÈÈÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇóóóóóóßßßÒÒÒÒÒÈÈÈÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýóóóóßßßÒÒÒÒÒÈÈÈÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýýýóóóßßÒÒÒÒÒÈÈÉÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇ\022\022\022\022\022óóßßÒÒÒÒÒÒÉÉÉÉÉÉÉÉÉÉ33ÇÇÇÇÇ\022\022\022\022\022\022\022êêÌÌÌÌÌÌÀÀÉÉÉÉÉÉÉ33333ÇÇÇ\022\022\022\022aaêêêêÌÌÌÌÌÀÀÀÀÀÀÀÀ333333ÇÇÇttttaEEEEêêÌÌÌÌÌÀÀÀÀÀÀÀÃÃ3333333ttttttEEEEEEÌÌÌÌÀÀÀÀÀ//ÃÃÃÃ33333tttttttEEGGGGG½½½ÀÀÀ////////·3ººtttttttFFFGFF½½½½½½½////·······»tttttttFFFFFFF½½½½½½-Á·········»\036\036\036\036JJJJJFFFFFF½½½½½--Á·······»»\031\031\031\031\031JJJJJIIIIHH½------«····»»»»\031\031\031\031\031\031\031JJJJIIIIH¨¨-«««¯««¬¬¬»»»»\031\031\031JJJIIIII¨¨¨¯¯¯¯«««²²²²²²II\005\005\005\005\005\005¯¯¯¯¯¯²²²²²²¢¢¢\005\005\005\005¥¯¯¯¯©²²²²²!!!!\177\177\177\177%%%¢¥¥¥¥¥¥,,,©©²²²²\177\177\177%%%%%¥¥¥¥,,,,ª©©©©©######%%%%¥¥¥¥,,,,ªªª©©´####'''''',,,,ªªªª§§##'''''*****ª§§§§         ''''******§§§§            ****++++++    ++++++     +++++ ¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"((((¤¤¤\"\"\"\"\"\"\"\"\"$$$((((((\037\037\037\037\037\037\037\037\037\037\037$$$$$$(((((ýýýóóóßßßÒÒÒÒÒÒ8ÉÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýýóóóóßßÒÒÒÒÒÒ8ÉÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýýóóóóßßÒÒÒÒÒ88ÉÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýýýóóóóßÒÒÒÒ888ÉÉÉÉÉÉÉÉÉ33ÇÇÇÇÇaaaaaóóóêÌÒÒ88888ÉÉÉÉÉÉÉ33333ÇÇÇaaaaaaêêêêÌÌÌÌÌÌ8ÉÉÉÉÉÉÉ333333ÇÇaaaaaaêêêêêÌÌÌÌÌÀÀÀÀÀÀÀÃ33333333ttaaaaaêêêêêÌÌÌÌÀÀÀÀÀÀÃÃÃÃ333333ttttttEEEEêêêÌÌÀÀÀÀÀÀÃÃÃÃÃÃ33333ttttttteeFFFF½½½½½½ÀÃÃÃÃÃÃÃÃÃ33ÆtttttteeeFFFF½½½½½½½///ÃÃÃÃÃ··»»tttttheeFFFFFF½½½½½½ÁÁÁ·······»»\036\036\036JJJJJJFFFFFF½½½½½ÁÁÁÁ·····»»»\031\031\031\031\031\031JJJJIIIHHH½-----ÁÁÁ···»»»»\031\031\031\031\031\031\031JJJJIIIIHH¯¯¯¯¯¯¯«Á²²»»»»\031\031\031\031\031\031\031\031JJJIIIII\005\005\005¯¯¯¯¯¯«²²²²²²\031\031\031KK\005\005\005\005\005\005¯¯¯¯¯¯²²²²²²¢\005\005\005\005\005\005¯¯¯¯¯¯²²²²²\177\177\177\177\177\177\177\177%¢¢¥¥¥¥¥¥,,,ªª²²²²\177\177\177\177%%%¥¥¥¥¥¥,,ªªª©©©©#####%%''¥¥¥¥,,ªªªªªª´´####''''''*,ªªªªªª§´'''''****ªª§§§§       ''''******+§§§       ++++++  +++++    ¤+++++\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"(¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"((((\037\037\037\037\037\037\037\037\037$$$((((((\037\037\037\037\037\037\037\037\037\037\037$$$$$$(((((ýýýýóóóóççÒÒÒ888ÉÉÉÉÉÉÉÉÉ3333ÇÇÇýýýýóóóóççÒÒÒ888ÉÉÉÉÉÉÉÉÉ3333ÇÇÇýýýýóóóóçççÒ88888ÉÉÉÉÉÉÉ33333ÇÇÇýýýýýóóççççç88888ÉÉÉÉÉÉÉ333333ÇÇaaaaaýêêêêçç88888ÉÉÉÉÉÉÉ333333ÇÆaaaaaaêêêêêÌÌ88888ÉÉÉÉÉÊ3333333Æaaaaaa\fêêêêêÌÌÌÌ8ÀÉÉÀÀÊÃÃ333333ÆaaaaaaaêêêêêêÛÛÛÀÀÀÀÀÃÃÃÃÃ33333ÆttttttEEêêêêêÛÛÛÛÀÀÀÃÃÃÃÃÃÃÃ33ÆÆtttttteeeFFFF½½½½½½½ÃÃÃÃÃÃÃÃÃÃÆÆttthheeeeFFFF½½½½½½½½ÃÃÃÃÃÃÃÃÃ»»hhhhhheeeFFFFF½½½½½½ÁÁÁÁÁÂÂÂ»»»»\031\031\031\031JJJJIFFFHHH½½½½ÁÁÁÁÁÁÂÂÂ»»»»\031\031\031\031\031\031\031JJIIHHHHHH--¯ÁÁÁÁÁÁÂÂ»»»»\031\031\031\031\031\031\031\031JJIIIHHHH¯¯¯¯¯¯¯ÁÁ²²»»»»\031\031\031\031\031\031\031\031\031KKKKK\005\005\005\005\005¯¯¯¯¯¯¯²²²²²²\031\031\031\031KKK\005\005\005\005\005\005¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177K\005\005\005\005\005\005¯¯¯¯¯¿²²²²²\177\177\177\177\177\177\177\177L\005¥¥¥¥¥¥,ªªªª²²²²\177\177\177\177\177%LLL¥¥¥¥¥ªªªªªªª©´####%'''L¥¥¥,ªªªªªªª´´###''''''',ªªªªªªª´´'''''***ªªªª§§´     ''''*****¦+++§+++++++++++  +++++\"\"\"\"\"\"\"¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"(¤¤¤¤¤\"\"\"\"\"\"\"\"\"((((\037\037\037\037\037\037\037\037\037\037$$$$(((((\037\037\037\037\037\037\037\037\037\037\037$$$$$$(((((ýýýýýóóççççç88888ÏÏÏÏÏÏÏÏ33333ÇÇýýýýýóóççççç88888ÏÏÏÏÏÏÏÏ33333ÇÇýýýýýýóççççç88888ÏÏÏÏÏÏÏÏ333333Æýýýýýýññçççç88888ÏÏÏÏÏÏÏÊÊ33333Æaaaaa\fêêêçççç88888ÏÏÏÏÏÏÊÊ33333Æaaaaa\f\fêêêêê888888ÏÏÏÏÊÊÊÊ3333ÆÆaaaaa\f\fêêêêêêÛÛÛÛ8ÏÏÏÊÊÊÃÃ333ÆÆÆaaaaa\f\fêêêêêÛÛÛÛÛÛÀÃÃÃÃÃÃÃÃ33ÆÆÆttteeeeeeêêÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÃÃÆÆÆttteeeeeeeFF½½½½ÛÛÛÛÃÃÃÃÃÃÃÃÃÆÆÆhhhhhheeeeFFF½½½½½½½ÁÁÁÃÃÃÃÃÃ»»»hhhhhhheeeFFFF½½½½½½ÁÁÁÁÂÂÂÂ»»»»\031\031\031\031\031\031\031heeHHHHHH½½½ÁÁÁÁÁÂÂÂÂ»»»»\031\031\031\031\031\031\031\031oIIHHHHHH¯¯¯ÁÁÁÁÁÁÂÂ»»»»\031\031\031\031\031\031\031\031oooIHHHHH\005¯¯¯¯¯¯ÁÁÂÂÂ»»»\031\031\031\031\031\031\031\031\031KKKKK\005\005\005\005\005¯¯¯¯¯¯¯²²²²»»\177\177\177\177\177\177KKKKK\005\005\005\005\005¯¯¯¯¯¿¿²²²²²\177\177\177\177\177\177\177KK\005\005\005\005\005\005\005¯¿¿¿¿¿²²²²\177\177\177\177\177\177\177\177LLLL¥¥¥¥ªªªªªªª²²²\177\177\177\177\177\177LLLLL¥¥¥ªªªªªªªª´´#LLLLLL¥¥ªªªªªªªª´´''''''ªªªªªªª´´´'''''**ªªªªª´´´'¦¦¦+++¦¦¦+++++++++++++\"\"\"\"\034\034\034\034\034¤¤¤¤¤¤\"\"\"\"\"\034¤$((\037\037\037\037\037\037\037\037\037$$$((((\037\037\037\037\037\037\037\037\037\037\037$$((((ýýýýýñññçççç88888ÏÏÏÏÏÏÏÊÊÊÊ3ÆÆÆýýýýýñññçççç88888ÏÏÏÏÏÏÏÊÊÊÊ3ÆÆÆýýýýññññççççç88888ÏÏÏÏÏÏÊÊÊÊ3ÆÆÆ\f\f\f\f\fññññçççç88888ÏÏÏÏÏÏÊÊÊÊÆÆÆÆa\f\f\f\f\f\fññçççç88888ÏÏÏÏÏÊÊÊÊÊÆÆÆÆ\f\f\f\f\f\f\fêêêççç88888ÏÏÏÏÊÊÊÊÊÊ3ÆÆÆ\f\f\f\f\f\f\f\fêêêêÛÛÛÛÛÛÏÏÏÃÊÊÊÊÊÊ3ÆÆÆ\f\f\f\f\f\f\f\fêêìÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÊ3ÆÆÆhheeeeeeeeììÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃ5ÆÆÆhhhhheeeeee>ÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃ555ÆÆhhhhhheeeee>>>>½½½½ÁÁÁÁÁÃÂÂ5555»hhhhhhheeee>>>>½½½½ÁÁÁÁÁÂÂÂÂ»»»»\031\031\031\031\031hhhheHHHHH>>½ÁÁÁÁÁÁÂÂÂÂ»»»»\031\031\031\031\031\031\031ooooHHHHHH¯¯¯ÁÁÁÁÂÂÂÂÂ»»»\031\031\031\031\031\031\031ooooKKHHHH\005¯¯¯¯¯¯ÂÂÂÂÂ»»»\031\031\031\031\031\031\031\031KKKKKK\005\005\005\005\005¯¯¯¯¯¯¯Â²²»»»\177\177\177\177\177\177KKKKKK\005\005\005\005\005¯¯¿¿¿¿¿²²²²²\177\177\177\177\177\177\177KKK\005\005\005\005\005\005\005¿¿¿¿¿¿²²²²\177\177\177\177\177\177\177\177LLLLL¥¥ªªªªªªª¿²²²\177\177\177\177\177\177\177\177\177LLLLLL¥¥ªªªªªªªªª´´\032xLLLLLLLL\006ªªªªªªª´´´''LLL\006ªªªªªªª´´´''''ªª¦´´´´¦¦¦¦¦´´¦¦¦¦¦+¦¦¦¦++¦¦++++\034\034\034\034\034\034\034\034\034¤)))))\034\034((\037\037\037$((((\037\037\037\037\037\037\037\037\037\037\037(((TTTTñññññçççç88888ÏÏÏÏÏÏÊÊÊÊÆÆÆÆTTTTñññññçççç88888ÏÏÏÏÏÏÊÊÊÊÆÆÆÆTTTTñññññçççç88888ÏÏÏÏÏÊÊÊÊÊÆÆÆÆ\f\f\f\f\fññññçççç88888ÏÏÏÏÏÊÊÊÊÊÆÆÆÆ\f\f\f\f\f\fññññççç888ààÏÏÏÏÊÊÊÊÊÊÆÆÆÆ\f\f\f\f\f\f\f\fñññÛÛÛÛÛààÏÏÏÊÊÊÊÊÊÊÊÆÆÆ\f\f\f\f\f\f\f\fêêìÛÛÛÛÛÛÛàÏÏÃÊÊÊÊÊÊÊÆÆÆ\f\f\f\f\f\f\f\fììììÛÛÛÛÛÛÛÃÃÃÃÃÊÊÊÊÊÆÆÆhhhheeeeeììììÛÛÛÛÛÛÕÕÃÃÃÃÃÃ555ÆÆhhhhhheeeee>>ÛÛÛÛÛÛÕÕÃÃÃÃ5555555hhhhhheeeee>>>>>ææÁÁÁÁÁÁÂÂ555555hhhhhhhhee>>>>>>æææÁÁÁÁÁÂÂÂÂÂ55»\031\031hhhhhhhoHH>>>>>ææÁÁÁÁÁÂÂÂÂÂ»»»\031\031\031\031\031ooooooHHHHHH>¯ÁÁÁÁÁÂÂÂÂÂ»»»\031\031\031\031ssooooKKKKHH\005\005¯¯¯¯ÁÂÂÂÂÂÂ»»»\031\031sssssoooKKKK\005\005\005\005\005¯¯¯¯¿¿¿444»»»\177\177\177\177\177sssoKKKKK\004\004\005\005\005\005¿¿¿¿¿¿444²²²\177\177\177\177\177\177\177KK\004\004\004\004\004\004\005\005¿¿¿¿¿¿¿4²²²\177\177\177\177\177\177\177xLLLLLL\004ªªªªªª¿¿¿4ÄÄ\177\177\177\177\177\177\177\177xxxLLLLLLL\006ªªªªªªªª´´´\032\032\032\032xxxLLLLLLL\006\006ªªªªªªª´´´\032\032\032\032\032\032\032\032LLLL\006\006\006ªªªªª´´´´\032\032¦¦¦´´´¦¦¦¦¦´´{{{¦¦¦¦¦¦¦¦¦¦¦¦¦}}}}}}}¦¦¦+++\034\034\034\034\034\034\034\034\034))))))\034\034\034(((((\037\037\037\037\037\037\037\037\037\037((TTTTTññññççççç88àÏÏÏÏÏÏÊÊÊÊÊÊÆÆÆTTTTTññññççççç8ààÏÏÏÏÏÏÊÊÊÊÊÊÆÆÆTTTTTñññññççç88àààÏÏÏÏÊÊÊÊÊÊÊÆÆÆTTTTTñññññçççàààààÏÏÏÏÊÊÊÊÊÊÊÆÆÆT\f\f\f\f\fñññññÛàààààà×××ÊÊÊÊÊÊÊÊÆÆÆ\f\f\f\f\f\f\f\fñññìÛààààà×××7ÊÊÊÊÊÊÊÆÆÆ\f\f\f\f\fWWWììììÛÛÛÛÛàà7777ÊÊÊÊÊÊÆÆÆWWWWWWWWìììììÛÛÛÛÛÛÕÕÕÃÊÊÊÊ555ÆÆhhhhheWWWìììììÛÛÛÛÕÕÕÕÃÃÃ555555Æhhhhhheeee>>ìììÛÛÕÕÕÕÕÕ555555555hhhhhhheee>>>>>>æææÁÁÁÂÂÂ5555555hhhhhhhhYYY>>>>>æææÁÁÁÂÂÂÂÂÂ5555sssoooooooY>>>>>>æææÁÁÁÁÂÂÂÂÂÂÅÅsssssooooooHHHHH>æææÁÁÁÁÂÂÂÂÂÅÅÅssssssooooKKKKH\004\004\004¯¯¯¿¿¿ÂÂÂÂÅÅÅÅsssssssoooKKKK\004\004\004\004\005¯¿¿¿¿¿¿444ÅÅÅssssssssoKKKK\004\004\004\004\004\005\005¿¿¿¿¿¿4444²Å\177\177\177xxxxxxxKK\004\004\004\004\004\004\004\004¿¿¿¿¿¿¿444ÄÄ\177\177\177xxxxxxxLLLLLLL\004ªª¿¿¿¿¿¿44ÄÄ\177\177\177xxxxxxxxxLLLLLLLL\006\006ªªªªªªª´´´\032\032\032\032\032\032\032\032\032xxxxLLLLLL\006\006\006ªªªªªª´´´´\032\032\032\032\032\032\032\032\032\032LLL\006\006\006\006\006ªªªªª´´´´\032\032\032\032\032\032¦¦¦¦´´´{{{¦¦¦¦¦´´{{{{{{¦¦¦¦¦¦}}}}¦¦¦¦¦¦}}}}}}}}})¦¦)¦¦¦\034\034\034\034\034\034\034\034\034\034))))))))\034\034\034\034(((\037\037\037\037\037\037\037((£TTTTTññññññçàààààà×××Ï77ÊÊÊÊÊÊÊÆTTTTTññññññçàààààà×××Ï77ÊÊÊÊÊÊÊÆTTTTTññññññààààààà××××77ÊÊÊÊÊÊÊÆTTTTTTñññññààààààà××××77ÊÊÊÊÊÊÆÆTTTTTTñññññìàààààà×××777ÊÊÊÊÊÊÆÆWWWWWWWWìììììààààà×××777ÊÊÊÊÊÆÆÆWWWWWWWWìììììÛÛàààà77777ÊÊÊ555ÆÆWWWWWWWWWìììììÛÛÛÛÕÕÕÕ775555555ÆhhhWWWWWWììììììÛÛÕÕÕÕÕÕ555555555hhhhhheeW>>ììììææÕÕÕÕÕÕÕ55555555hhhhhhhhYY>>>>>ææææÕÕÕÕÂ55555555hhhhhhYYYYY>>>>æææææÁÁÂÂÂÂ55555ÅssssooooYYYY>>>æææææÁÁÁÂÂÂÂÂÂÅÅÅsssssooooooYYH>ææææææÁÍÍÂÂÂÂÅÅÅÅssssssooooooK\004\004\004\004\004¿¿¿¿¿¿¿ÍÍÍÅÅÅÅsssssssooKKKK\004\004\004\004\004\004¿¿¿¿¿¿4444ÅÅÅsssssss\026\026KKK\004\004\004\004\004\004\004¿¿¿¿¿¿44444ÅÅxxxxxxxxxxxLL\004\004\004\004\004\004\004¿¿¿¿¿¿4444ÄÄxxxxxxxxxxxxLLLLLLLL\006¿¿¿¿¿444ÄÄÄxxxxxxxxxxxxLLLLLLL\006\006\006ªªªªª´´´´´\032\032\032\032\032\032\032\032\032\032xxxLLLLL\006\006\006\006\006ªªªª´´´´´\032\032\032\032\032\032\032\032\032\032\032L\006\006\006\006\006\006\006ªªª´´´´´\032\032\032\032\032\032\032\032\032\032¦¦¦¦¦´´´{{{{{{{{{{¦¦¦¦¦¦´´{{{{{{{{{{{¦¦¦¦¦¦¦}}}}}}}}}¦¦¦¦¦¦}}}}}}}}}))))))))¦\034\034\034\034\034\034\034\034\034\034)))))))))\034\034\034\034\034\034\034\034\034\034\034)(((£\037((£££TTTTTTñññññààààààà×××77777ÊÊÊÊÊÊTTTTTTñññññààààààà×××77777ÊÊÊÊÊÊTTTTTTñññññààààààà×××77777ÊÊÊÊÊÆTTTTTTñññññàààààà××××7777ÊÊÊÊÊÊÆTWWWWWWúììì;ààààà××××7777ÊÊÊÊÊÆÆWWWWWWWWìììì;;àààà×××77777ÊÊ55ÆÆ\\\\WWWWWWWìììì;;ààà××77777755555Æ\\\\\\WWWWWWìììììì;;ÕÕÕÕÕ7775555555WWWWWWWWWììììììååÕÕÕÕÕÕÕ55555555hhhhhhYYYYìììæææææÕÕÕÕÕÕ55555555hhhhhYYYYYY>>ææææææÕÕÕÕÕÕ5555555hhhhYYYYYYYY>>æææææææÕÂÂÂÂ555ÅÅÅsss\020\020\020\020\020YYYYY>æææææææÍÍÍÍÍÂÂÅÅÅÅsssssoooooYYYYææææææÍÍÍÍÍÍÍÅÅÅÅÅssssssoooooo\004^^^^\004¿¿¿¿¿ÍÍÍÍÍÅÅÅÅsssssssjjjKK^\004\004^\004\004\004¿¿¿¿¿¿444ÍÅÅÅ\026\026\026\026\026\026\026\026\026jjK\004\004\004\004\004\004\004\004¿¿¿¿444444ÅÅxxxxxxxxxxxjL\004\004\004\004\004\004\004¿¿¿¿44444ÄÄÄxxxxxxxxxxxxLLLLLLL\006\006\006¿¿444ÄÄÄÄÄxxxxxxxxxxxxrLLLLL\006\006\006\006\006ªª4ÄÄ´´´´\032\032\032\032\032\032\032\032\032\032xrrrrLL\006\006\006\006\006\006ªªªª´´´´´\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006ªªª´´´´´\032\032\032\032\032\032\032\032\032\032\006\006MMM¦¦¦¦´´´{{{{{{{{{{¦¦¦¦¦¦´´{{{{{{{{{{{\007¦¦¦¦¦¦}}}}}}}}{{\007¦¦¦¦¦¦}}}}}}}}})))))))))³\034\034\034\034\034\034\034\034\034~~)))))))))\034\034\034\034\034\034\034\034\034\034~~~~))))~~~~(£££\035\035(££££TTTTTTúúúññ;ààààà××××Ù77777ÑÑÑÑÑTTTTTTúúúñð;ààààà××××Ù77777ÑÑÑÑÑTTTTTúúúúðð;ààààà××××777777ÑÑÑÑÑWWWWúúúúúðð;;àààà××××777777ÑÑÑÑÑWWWWWúúúúðð;;;àààà×××777777ÑÑÑÑÑ\\\\\\WWWWúúìð;;;;;à××××7777777Ñ555\\\\\\\\WWWWWììì;;;;;××××77777755555\\\\\\\\\\\\WWWììììì;;;ÕÕÕÕÕ7777555555\\\\\\\\\\\\\\\\WùìùùìååååÕÕÕÕÕÕ55555555______YYYYYYæææååååÕÕÕÕÕÕ5555555_______YYYYY>ææææææÕÕÕÕÕÕ555555Å\020\020\020\020\020_YYYYYY>>æææææææÕÍÍÍÍ55ÅÅÅÅ\020\020\020\020\020\020\020\020YYYYYYææææææÍÍÍÍÍÍÍÅÅÅÅÅssss\020\020\020\020\020YYYY^^^ææææÍÍÍÍÍÍÍÍÅÅÅÅssssssooooo^^^^^^^¿¿¿ÍÍÍÍÍÍÍÅÅÅÅsssjjjjjjjj^^^^^^^\004¿¿¿¿444444ÅÅÅ\026\026\026\026\026\026\026\026jjjj^^\004^^\004\004\004¿¿¿¿444444ÄÅ\026\026\026\026\026\026\026\026\026\026jjL\004\004\004\004^\004\004ò¿¿¿44444ÄÄÄxxxxxxxxxxxxLLLLL\006\006\006\006\006¿444ÄÄÄÄÄÄxxxxxxxxxxxxrrLLL\006\006\006\006\006\006\0064ÄÄÄÄÄÄÄ\032\032\032\032\032\032\032\032\032rrrrrrL\006\006\006\006\006\006\006\006ªª´´´´´´\032\032\032\032\032\032\032\032\032\032rr\006\006\006\006\006\006MMOOO´´´´{{{{{{{{{\032MMMMM¦OOO´´´{{{{{{{{{{\007\007\007\007\007¦¦¦O´{{{{{{{{{{{\007\007\007\007¦¦¦¦¦}}}}}}}}}{{{||||\007\007\007\007\007\007¦¦¦}}}}}}}}}~~~~)))))))))³\034\034\034\034\034\034\034\034~~~~~)))))))))\034\034\034\034\034\034\034\034\034~~~~~~))))))~~~~££\035\035\035\035(££££\035\035\035\035(££££££þþúúúúúúúððèèèèàà×ÙÙÙÙÙ777ÑÑÑÑÑÑþþúúúúúúúððèèè;àà×ÙÙÙÙÙ777ÑÑÑÑÑÑþþþúúúúúúððè;è;àà××ÙÙÙ7777ÑÑÑÑÑÑþþþþúúúúúððè;;;;à××××Ù7777ÑÑÑÑÑÑ\\\\\\\\úúúúúðð;;;;;;××××77777ÑÑÑÑÑÑ\\\\\\\\\\\\úúúððð;;;;;××××777777ÑÑÑÑÑ\\\\\\\\\\\\WWúúùù;;;;;;××Õ77777ÓÑÑÑÑÑ\\\\\\\\\\\\\\ùùùùùù;;;ååÕÕÕÕÕ777Ó55555\\\\\\\\\\\\\\ÿÿùùùùùåååååÕÕÕÕÕÕ5555555_________YùùùùååååååÕÕÕÕÕ5555555ddd_____YYYYYæææææåååÕÕÕÍ5555ÐÅÅ\020\020\020\020\020\020__YYYYYYæææææææÍÍÍÍÍÍÅÅÅÅÅ\020\020\020\020\020\020\020\020YYYYYõõõõæææÍÍÍÍÍÍÍÅÅÅÅÅ\020\020\020\020\020\020\020\020\020\020^^^^^^õõõõÍÍÍÍÍÍÍÍÅÅÅÅsjjjjjjjjjj^^^^^^^õ¿¿4ÍÍÍÍÍÍÍÅÅÅjjjjjjjjjjj^^^^^^^^¿¿¿4444444ÅÅÅ\026\026\026\026\026\026\026jjjjj^^^^^^^ò¿¿¿444444ÄÄÄ\026\026\026\026\026\026\026\026\026\026jjj^^^^^òòòò¿4444ÄÄÄÄÄ\026\026\026\026x\026\026\026\026\026\026xLLL\006\006\006\006\006\006òò444ÄÄÄÄÄÄxxxxxxxxxxrrrrrL\006\006\006\006\006\006\006\0064ÄÄÄÄÄÄÄ\032\032\032\032\032\032\032\032rrrrrrrr\006\006\006\006\006\006\006\006MOÄ´´´´Ä\032\032\032\032\032\032\032\032\032\032rrrrr\006\006\006\006\006MMMOOOO´´´{{{{{{{{{{MMMMMMOOOOO´´{{{{{{{{{{{MMMM\007\007\007OOOO{{{{{{{{{{{||||\007\007\007\007\007\007\007¦OO}}}}}}}}}{~~~|||||||\007\007\007\007\007\007\007\007³³}}}}}}}}~~~~~~||))))))))³³\034\034\034\034\034\034~~~~~~~~)))))))))\034\034\034\034\034\034~~~~~~~~~))))))))~~~~~\035\035\035££££\035\035\035\035\035\035\b£££££\035\035\035\035\035\b££££££þþþþúúúúðððèèèèè;ÙÙÙÙÙÙÙ77ÑÑÑÑÑÑþþþþþúúúðððèèèèè;×ÙÙÙÙÙÙ77ÑÑÑÑÑÑþþþþþúúúðððèèèèè;×ÙÙÙÙÙÙ77ÑÑÑÑÑÑþþþþþúúúðððèèèè;;××ÙÙÙÙ77ÓÑÑÑÑÑÑþþþþþúúúððððè;;;;ã×ÙÙÙÙ7ÓÓÑÑÑÑÑÑ\\\\\\\\\\úúúúððð;;;;;ãããÙ77ÓÓÓÑÑÑÑÑÑ\\\\\\\\\\\\\\úùùùùð;;;ååããã7ÓÓÓÓÓÑÑÑÑÑ\\\\\\\\\\\\ÿÿùùùùù;åååååÕÕÕÕÓÓÓÓÓÑÑÑÑ\016\016\016\016\016\016\016ÿÿùùùùùååååååÕÕÕÕÕÓÓÓÓÐÐÐddd______ÿùùùùåååååååÕÝÝÝ555ÐÐÐÐdddd______YYùùææåååååÝÝÝÝÝÐÐÐÐÐÐ\020\020\020\020\020\020___YYYYõõæææææÍÍÍÍÍÍÍÐÐÐÐÅ\020\020\020\020\020\020\020\020\020_YYõõõõõõõæÍÍÍÍÍÍÍÍÅÅÅÅ\020\020\020\020\020\020\020\020\020\020^^^^õõõõõõÍÍÍÍÍÍÍÍÅÅÅÅjjjjjjjjjjj^^^^^^õõõ44ÍÍÍÍÍÍËËÅÅiiiijjjjjjj^^^^^^^^õ¿44444444ËËË\026\026\026\026\026\026jjjjjj^^^^^^^òò¿¿444444ÄÄÄ\026\026\026\026\026\026\026\026\026\026jjj^^^òòòòòòò444ÄÄÄÄÄÄ\026\026\026\026\026\026\026\026\026\026\026mrrr\006\006\006\006òòòòò4ÄÄÄÄÄÄÄxxxxxppmmrrrrrrr\006\006\006\006\006\006\006òòÄÄÄÄÄÄÄ\032\032\032\032\032pprrrrrrrrr\006\006\006\006\006\006\006MMOOÄÄÄÄÄ{{{{{{{{rrrrrrrr\030\006\006\006MMMMMOOOO6´´{{{{{{{{{{\030\030\030\030\030\030\030MMMMMMOOOOOO´{{{{{{{{{{{\030\030MMMM\007\007\007OOOOO{{{{{{{{{{{{|||||||M\007\007\007\007\007\007\007OOO}}}}}}}}~~~~~|||||||||\007\007\007\007\007\007\007\007³³}}}}}}~~~~~~~~||||))))))))³³\034\034\034\034~~~~~~~~~~))))))))³\034\034\034~~~~~~~~~~~~)))))))£~~~~~\035\035\035\035\035\035\035\035\b£££££\035\035\035\035\035\035\035\035\b\b\b£££££\035\035\035\035\035\035\035\b\b£££££þþþþþúúúðððèèèèèèãÙÙÙÙÙÙÓÓÑÑÑÑÑÑþþþþþúúúðððèèèèèèãÙÙÙÙÙÙÓÓÑÑÑÑÑÑþþþþþúúúðððèèèèèãããÙÙÙÙÓÓÓÑÑÑÑÑÑþþþþþþúúððððèèèèãããÙÙÙÙÓÓÓÑÑÑÑÑÑþþþþþþúúððððèèèèããããÙÙÓÓÓÓÑÑÑÑÑÑ\\\\þþþþþúððððèè;;ãããããÓÓÓÓÓÑÑÑÑÑÑ\\\\\\\\ÿÿÿÿùùùùù;;åãããããÓÓÓÓÓÓÑÑÑÑÑ]]]\016\016ÿÿÿÿùùùùùåååååãããÓÓÓÓÓÓÑÑÑÑ\016\016\016\016\016\016\016ÿÿùùùùùåååååååÝÝÝÓÓÓÓÐÐÐÐdddd__\016\016\016ÿùùùùùåååååÝÝÝÝÝÝÐÐÐÐÐÐddddd______ÿùõõõåååÝÝÝÝÝÝÝÐÐÐÐÐÐdddddd____YõõõõõõõõÝÝÍÍÝÝÝÐÐÐÐÐÐ\020\020\020\020\020\020\020\020\020\020QQõõõõõõõõÍÍÍÍÍÍÍÍÐÐÐÅ\020\020\020\020\020\020\020\020\020\020^^QõõõõõõõëÍÍÍÍÍÍÍËËËËiiijjjjjjjj^^^^^õõõõëëÍÍÍÍÍËËËËËiiiiiijjjjjj^^^^^^^òò444444ËËËËË\026\026iiiiiijjjj^^^^^òòòòò44444ÄÄÄÄË\026\026\026\026\026\026\026\026\026\026ibbb^òòòòòòòò444ÄÄÄÄÄÄ\026\026\026\026\026\026\026mmmmmrrr\006\006òòòòòòòäÄÄÄÄÄÄÄpppppppmmmmrrrrr\006\006\006\006òòòòòÄÄÄÄÄÄÄppppppppmrrrrrrr\006\006\006\006\006\006MMMOOO66ÄÄ{{{{{{{prrrrrrr\030\030ccMMMMMMOOOO666{{{{{{{{{\030\030\030\030\030\030\030\030\030MMMMMMMOOOOO66{{{{{{{{{{u\030\030\030\030\030\030|||MMMM\007\007\007OOOOO{{{{{{{{{{vvvv|||||||M\007\007\007\007\007\007\007OOO}}~~~~~~~~~~vv||||||||\007\007\007\007\007\007\007³³³}}}~~~~~~~~~~~||||||q))))))³³³\034\034~~~~~~~~~~~~q)))))))³³~~~~~~~~~~~~~~\035\035\035\035\035))))))££~~~~~\035\035\035\035\035\035\035\035\035\035\b\b\b££££££\033\033\033\033\035\035\035\035\035\035\035\035\b\b\b\b\b££££\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\b\b\b\b££££þþþþþþúðððððèèèèããããÙÙÓÓÓÓÑÑÑÑÑÑþþþþþþúðððððèèèèããããÙÙÓÓÓÓÑÑÑÑÑÑUUUþþþþðððððèèèèããããÙÓÓÓÓÓÑÑÑÑÑÑUUUUUþþððððð=èèèãããããÓÓÓÓÓÑÑÑÑÑÑUUUUUUþððððð==èèãããããÓÓÓÓÓÓÑÑÑÑÑUUUUUÿÿÿùùùð===ããããããÓÓÓÓÓÓÑÑÑÑÑ]]]]]ÿÿÿÿùùùù==åããããããÓÓÓÓÓÑÑÑÑÑ]]]]\016\016ÿÿÿùùùùùåååãããããÝÓÓÓÓÓÑÑÐÐ]]]\016\016\016\016ÿÿÿùùùùùåååååÝÝÝÝÓÓÓÐÐÐÐÐddd\016\016\016\016\016\016ÿÿùùùöååååÝÝÝÝÝÝÝÐÐÐÐÐÐdddddd_____õõõõõõéééÝÝÝÝÝÝÐÐÐÐÐÐdddd`````QQQõõõõõõõÝÝÝÝÝÝÝÐÐÐÐÐÐ\020\020\020\020\020\020\020``QQQQQõõõõõëëëÍÍÍÍÍÐÐÐÐÐiiiiii\020\020``QQQQõõõõõëëëÍÍÍÍÍËËËËËiiiiiiiijjj^^^õõõõõëëëëÍÍÍËËËËËËiiiiiiiijjjj^^^^^Ròòòòë444ËËËËËËiiiiiiiiiibbbbRRRòòòòòò444ÄÄÄËËË\026\026\026\026\026\026\026iiibbbbbòòòòòòòòò4ÄÄÄÄÄÄÄpppppmmmmmmmmrrbòòòòòòòòäääÄÄÄÄÄpppppppmmmmrrrrr\006\006òòòòòòòääÄÄÄÄÄppppppppmmmrrrrrr\006\006MMMMMOO666666ppppppppprrr\030\030\030\030\030ccMMMMMOOOO6666{{{{{{uuuu\030\030\030\030\030\030\030\030ccMMMMMOOOOO66{{{{{{uuuuuv\030\030\030|||||MMM\007\007\007OOOOOO{{{{{{{uuuvvvvv||||||M\007\007\007\007\007\007\007O³³~~~~~~~~~~vvvvv||||||qq\007\007\007\007\007³³³³~~~~~~~~~~~~~vvvvv||qqqq))NN³³³³~~~~~~~~~~~~~~\035\035\035\035\035qq)))))³³³~~~~~~~~~~~~~z\035\035\035\035\035\035\035\035\035q))))££³³\033\033\033\033\033\033\033\033zzz\035\035\035\035\035\035\035\035\035\b\b\b\b£££££\033\033\033\033\033\033\033\033\033\033\033\033\033z\035\035\035\035\035\035\035\035\035\b\b\b\b\b££££\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\b\b\b\b\b\b£££UUUUUUþððð====èèãããââÓÓÓÓÓÑÑÑÑÑÑUUUUUUþððð====èèãããââÓÓÓÓÓÑÑÑÑÑÑUUUUUUþððð=====ããããââÓÓÓÓÓÑÑÑÑÑÑUUUUUUUøðð=====ãããããââÓÓÓÓÓÑÑÑÑÑUUUUUUøøøø======ãããããâÓÓÓÓÓÑÑÑÑÑUUUUUÿÿøøøø=====ããããããÓÓÓÓÓÑÑÑÑÑ]]]]]ÿÿÿÿùùùù===ããããããÓÓÓÓ9999ÑÑ]]]]]\016ÿÿÿÿùùùù=ååãããÝÝÝÝÓ99999ÐÐ]]]]\016\016\016\016ÿÿÿùööööååéÝÝÝÝÝÝ99ÐÐÐÐÐ]]]\016\016\016\016\016\016\016ÿöööööééééÝÝÝÝÝÝÐÐÐÐÐÐddd``````QQQõõõõõééééÝÝÝÝÝÐÐÐÐÐÐ`````````QQQQõõõõõõééÝÝÝÝÝÐÐÐÐÐÐ`````````QQQQQõõõõõëëëëÝÝÝËÐÐÐËËiiiii`````QQQQQ??õëëëëëÍÍÍËËËËËËiiiiiiiii`QQQQQ???ëëëëëëë::ËËËËËiiiiiiiiibbbbRRRRRòòòëëëë::ËËËËËiiiiiiiibbbbbRRRRòòòòòò44ÄÄÄËËËË\026\026\026mmmmmmbbbbbRRRòòòòòòääääÄÄÄÎÎppppppmmmmmm\023\023bbòòòòòòòääääÄÄÄÄÎpppppppmmmmm\023\023\023r\ròòòòòòòääääÄÄÄÎppppppppmmmrrrrrcccMMSSOO6666666pppppppppm\030\030\030\030\030\030ccccMMSSOOO66666uuuuuuuuuuu\030\030\030\030\030\030\030ccMMMMMOOOO666uuuuuuuuuuuvvv|||||ccMMM\007OOOOOO6uuuuuuuuuuuvvvv||||||M\007\007\007\007\007\007\007³³³~~~~~~~uuuvvvvvv|||||qqqNNNN³³³³~~~~~~~~~~~vvvvvvvvqqqqqNNNNN³³³~~~~~~~~~~~~zz\035\035\035\035\035\035\035qqqqNNNNN³³~~~~~~~~~zzzzzz\035\035\035\035\035\035\035\035qqq)NNN³³\033\033\033\033\033\033\033\033\033\033zzzzz\035\035\035\035\035\035\035\035\b\b\b\b\b££££\033\033\033\033\033\033\033\033\033\033\033\033zzz\035\035\035\035\035\035\035\035\b\b\b\b\b\b£££\033\033\033\033\033\033\033\033\033\033\033\033\033\033z\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b££UUUUUUøøøø======ââââââÓÓÓÓÑÑÑÑÑÑUUUUUUøøøø======ââââââÓÓÓÓÓÑÑÑÑÑUUUUUUøøøø======ââââââÓÓÓÓÓÑÑÑÑÑUUUUUUøøøø======ãâââââÓÓÓÓÓ9ÑÑÑÑUUUUUøøøøøø=====ããââââÓÓÓÓ9999ÑÑ]]]]ÿÿøøøøø=====ãããâââÞÓÓ999999Ö]]]]]ÿÿÿøøøø====<ããââÞÞÞÞ9999999]]]]]]ÿÿÿøøøööööééééÝÝÞÞÞ999999Ð]]]]]\016\016\016\016ÿööööööééééÝÝÝÝÝ99999ÐÐ````````\016\016ööööööéééééÝÝÝÝÝÐÐÐÐÐÐ`````````QQQQQööééééééÝÝÝÝÐÐÐÐÐÐ`````````QQQQQõõõõéééééÝÝØØÐÐÐÐÐ``````````QQQQQõõõëëëëëëÝËËËËËËË``````````QQQQ????ëëëëëë::ËËËËËËiiiiiiiibbbQQ?????ëëëëëë::::ËËËËiiiiiiibbbbbbRRRRR?òëëëëë:::ËËËËiiiii\021bbbbbbbRRRRRòòòòòëäää:ËËËËppppmmmmbbbbbbRRRRòòòòòäääääÎÎÎÎppppppmmmm\023\023\023\023bRRòòòòòòäääääÎÎÎÎpppppppmmmm\023\023\023\023\r\r\r\r\r\r\rääääää6ÎÎÎppppppppmmm\023\023\023\023cccccSSSSO6666666pppppppppp\030\030\030\030ccccccSSSSOO666666uuuuuuuuuuu\030\030\030\030\030cccccMSSSOOO6666uuuuuuuuuuuvvvv|||cccMMSSOOOOOÔÔuuuuuuuuuuuvvvv||||||q\007\007\007\007NN³³³³uuuuuuuuuuvvvvvvv||qqqqqNNNN³³³³~~~~~~~~~~vvvvvvvvqqqqqqNNNNN³³³~~~~~~~~zzzzzz\035\035\035\035\035\035qqqqqNNNNN³³zzzzzzzzzzzzzzz\035\035\035\035\035\035\035qqqqNNNNN³\033\033\033\033\033\033\033\033zzzzzzzz\035\035\035\035\035\035\b\b\b\b\b\b\bPPP\033\033\033\033\033\033\033\033\033\033\033zzzzz\035\035\035\035\035\035\035\b\b\b\b\b\b£££\033\033\033\033\033\033\033\033\033\033\033\033\033\033zz\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b£UUUUUøøøøø======âââââââÞÞ99999ÑÖUUUUUøøøøø======âââââââÞÞ99999ÖÖUUUUUøøøøøø=====âââââââÞÞ99999ÖÖUUUUUøøøøøø=====<âââââÞÞÞ99999ÖÖUUUUøøøøøøø=====<âââââÞÞÞ99999ÖÖ]]]\013\013øøøøøø====<<<ââââÞÞÞ999999Ö]]]]\013\013\013øøøøøööö<<<<âÞÞÞÞÞ999999Ö]]]]\013\013\013\013øøøöööööéééééÞÞÞÞÞ999999]]]]\013\013\013\013\013üööööööéééééÝÝÞÞÞ99999Ð```````\013\013üüöööööééééééÝÝÝÝ99ÐÐÐÐ`````````QQQQööööééééééÝÝØØØØÐÐÐ`````````QQQQQ???ëééééééØØØØØØØØ``````````QQQQ???ëëëëëëë:ØØØØØØØ````\017\017\017\017`VQQQ?????ëëëëëë::::ËËËËiiiiibbbbbbbV??????ëëëëë::::ËËËËiiiii\021bbbbbbbRRRRRRëëëëë:::::ËËË\021\021\021\021\021\021\021\021bbbbbbRRRRRòòòòääää:::ÎÎpppp\021\021\021\021bbbbbbRRRRRòòòääääääÎÎÎÎppppppmm\023\023\023\023\023\023\023RR\r\ròòïïäääääÎÎÎÎpppppppmm\023\023\023\023\023\023\r\r\r\r\r\rSïääääääÎÎÎppppppppp\023\023\023\023\023\023cccc\rSSSSä666666Îuuuuuuuuuu\023fffffcccSSSSSSO666666uuuuuuuuuuuv\030\030\030fccccSSSSSOO66666uuuuuuuuuuuvvvvvcccccSSSSSOÔÔÔÔÔuuuuuuuuuuuvvvvvv|||qqqNNNNN³³ÔÔuuuuuuuuuuvvvvvvvvqqqqqqNNNNN³³³~~~wwwwzzzzznnnnnnqqqqqqNNNNN³³³wwwwzzzzzzzzzzz\035\035\035\035qqqqqqNNNNN³³zzzzzzzzzzzzzzz\035\035\035\035\035\035qqqqq\bNPPPP\033\033\033\033\033\033zzzzzzzzzz\035\035\035\035\035\035\b\b\b\b\b\bPPPP\033\033\033\033\033\033\033\033\033\033zzzzzz\035\035\035\035\035\035y\b\b\b\b\b\b\bPP\033\033\033\033\033\033\033\033\033\033\033\033\033\033zz\035\035\035\035\035yy\b\b\b\b\b\b\b\bP\013\013\013\013øøøøøøô====<<<ââââÞÞÞ99999ÖÖ\013\013\013\013øøøøøøô====<<<ââââÞÞÞ99999ÖÖ\013\013\013\013\013øøøøøô====<<<ââââÞÞÞ99999ÖÖ\013\013\013\013\013øøøøøøô==<<<<ââââÞÞÞ99999ÖÖ\013\013\013\013\013\013øøøøøô==<<<<<ââÞÞÞÞ99999ÖÖ\013\013\013\013\013\013\013øøøøôöö<<<<<âÞÞÞÞÞ99999ÖÖ\013\013\013\013\013\013\013\013øøööööö<<<<<ÞÞÞÞÞÞ99999Ö\013\013\013\013\013\013\013\013\013üöööööö<<<<éÞÞÞÞÞ99999Ö\013\013\013\013\013\013\013\013\013üüöööööééééééÞÞÞÞ99ØØØ9``\017\017\017\017\013\013\013üüüöööööééééééÞÞØØØØØØÐ\017\017\017\017\017\017```QQüöööööééééééááØØØØØØØ\017\017\017\017\017\017```QQQQ????ëëëëëëááØØØØØØØ\017\017\017\017\017\017\017\017`VQQQ?????ëëëëëë:::ØØØØØ\017\017\017\017\017\017\017\017VVVVV?????ëëëëëë:::::ËËË\021\021\021\021\021bbbbbVVVV?????ëëëë::::::ËËË\021\021\021\021\021\021\021\021bbbbbRRRRRRëëëë:::::::ËË\021\021\021\021\021\021\021\021\021bbbbbRRRRRRRäääää::::ÎÎ\021\021\021\021\021\021\021\021\021\021bbbbRRRRRòòïääääääÎÎÎÎpppppmm\023\023\023\023\023\023\023\r\r\r\r\r\rïïïïääääÎÎÎÎpppppppm\023\023\023\023\023\023\023\r\r\r\r\r\rïïïäääääÎÎÎppppkkkkkgggggfccc\r\rSSSSä66666ÎÎuuuuuuukkkgfffffccccSSSSS6666666uuuuuuuuuuuvfffffcccSSSSSSO66ÔÔÔuuuuuuuuuuuvvvvffffSSSSSSÔÔÔÔÔÔÔuuuuuuuuuuuvvvvvvv\025\025qqqNNNNÔÔÔÔÔuuuuuuunnnnnnnnnv\025\025qqqqqNNNNN³³³wwwwwwwzzzznnnnnnnqqqqqqqNNNNN³³wwwwwwwzzzzzzzznnnqqqqqqqNNNNN³³wwwwwzzzzzzzzzzz\027\035\035\035qqq\b\b\b\bPPPPP\033\033\033\033zzzzzzzzzzzz\035\035\035\035yy\b\b\b\b\b\bPPPP\033\033\033\033\033\033\033\033zzzzzzzz\035\035\035\035yyyy\b\b\b\b\bPPP\033\033\033\033\033\033\033\033\033\033\033\033\033\033zzz\027\027yyyyy\b\b\b\b\b\b\nP\013\013\013\013\013\013øôôôôô==<<<<<ââÞÞÞÞÞ99ÖÖÖÖ\013\013\013\013\013\013øôôôôô==<<<<<ââÞÞÞÞÞ99ÖÖÖÖ\013\013\013\013\013\013øôôôôôôî<<<<<ââÞÞÞÞÞ99ÖÖÖÖ\013\013\013\013\013\013\013ôôôôôôî<<<<<<ÞÞÞÞÞÞ99ÖÖÖÖ\013\013\013\013\013\013\013\013ôôôôôîî<<<<<ÞÞÞÞÞÞ999ÖÖÖ\013\013\013\013\013\013\013\013ôôôôîîî<<<<<<ÞÞÞÞÞ999ÖÖÖ\013\013\013\013\013\013\013\013\013üüööîîî<<<<<ÞÞÞÞÞ999ÖÖÖ\013\013\013\013\013\013\013\013\013üüüöööî<<<<<ÞÞÞÞÞ99ØØÖÖ\013\013\013\013\013\013\013\013üüüüööööö<ééééÞÞÞØØØØØØÖ\017\017\017\017\017\017\013\013üüüüüööööééééééááØØØØØØØ\017\017\017\017\017\017\017\017üüüüüüöööíééééááááØØØØØØ\017\017\017\017\017\017\017\017VVQQVûûûûííííëáááááØØØØØ\017\017\017\017\017\017\017\017VVVVVûûûû?ëëëëë::::ØØØØØ\017\017\017\017\017\017\017\017VVVVVV????ëëëëë:::::::ØØ\021\021\021\021\021\021\021\021VVVVVVV??ûëëëëë:::::::ÜÜ\021\021\021\021\021\021\021\021\021bbbVVRRRRRRëëä:::::::ÜÜ\021\021\021\021\021\021\021\021\021bbbbbRRRRRRïïäää::::ÎÎÎ\021\021\021\021\021\021\021\021\021\021\021bXXXXXX\rïïïïäääääÎÎÎÎ\021\021\021\021\021\021\021\021\023\023\023\023\023XX\r\r\r\r\rïïïïäääääÎÎÎppppkkkgg\023\023\023\023\023\023\r\r\r\r\r\rïïïïääääÎÎÎkkkkkkkkkggggfffc\r\r\rSSSSïï6666ÎÎuukkkkkkkkggffffcccSSSSSSS666666uuuuuuuuukk\024ffffffcSSSSSSÔÔÔÔÔÔÔuuuuuuuuuuuvvffffff[[[[[SÔÔÔÔÔÔÔuuuuuuuunnnnnnnn\025\025\025\025\025qqNNNÔÔÔÔÔÔwwwwwnnnnnnnnnnn\025\025\025\025\025qqqNNNNNÔÔÔwwwwwwwwzznnnnnnn\025\025\025qqqqqNNNN\t\t\twwwwwwwwzzzzzznnnn\025qqqqqqNNNNN\t\twwwwwwwwzzzzzzz\027\027\027\027qqq\b\b\bPPPPPPPwwwwwwwzzzzzzzzz\027\027\035yyyy\b\b\b\bPPPPP\033\033\033\033\033\033\033zzzzzzzz\027\027\027\027yyyyyy\b\b\bPPPP\033\033\033\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027yyyyyyy\b\n\n\n\nP\013\013\013\013÷÷÷ôôôôôîîî<<<<<<ÞÞÞÞÚÚÚÚÖÖÖ\013\013\013\013÷÷÷ôôôôôîîî<<<<<<ÞÞÞÞÚÚÚÚÖÖÖ\013\013\013\013\013÷÷ôôôôôîîî<<<<<<ÞÞÞÞÚÚÚÚÖÖÖ\013\013\013\013\013÷÷÷ôôôôîîîî<<<<<ÞÞÞÞÞÚÚÚÖÖÖ\013\013\013\013\013\013\013÷ôôôôîîîî<<<<<ÞÞÞÞÞÚÚÚÖÖÖ\013\013\013\013\013\013\013\013ôôôôîîîî<<<<<ÞÞÞÞÞÚÚÚÖÖÖ\013\013\013\013\013\013\013\013üüüüîîîî<<<<<ÞÞÞÞÞÚÚÚÖÖÖ\013\013\013\013\013\013\013\013üüüüööîîî<<<<ÞÞÞÞÞØØØÖÖÖ\013\013\013\013\013\013\013üüüüüüööîîîééééááááØØØØØÖ\017\017\017\017\017\017\013üüüüüüüööííííéáááááØØØØØØ\017\017\017\017\017\017\017\017üüüüüüûííííííááááááØØØØØ\017\017\017\017\017\017\017\017VVVVûûûûííííííááááááØØØØ\017\017\017\017\017\017ZZZVVVVûûûûûëëíí:::áááØØØØ\017\017\017\017\017ZZZZVVVVVûûûûëëëëë:::::::ÜÜ\021\021\021\021\021\021\021\021VVVVVVVûûûûëëëë:::::::ÜÜ\021\021\021\021\021\021\021\021\021\021VVVVVRXRûïïïï:::::ÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021bXXXXXXRïïïïïä::ÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021\021XXXXXXXïïïïïääääÎÎÎÎkkkk\021\021gggggggXXXX\r\r\rïïïïäääääÎÎÎkkkkkkkkggggggg\r\r\r\r\r\rïïïïïäääÎÎÎkkkkkkkkkgggggfff\r\r\rSSïïïïï66ÔÎÎkkkkkkkkk\024\024ggfffffcSSSSSSSÔÔÔÔÔÔuuuuuukkk\024\024\024\024fffff[[[[[SSÔÔÔÔÔÔÔuuuuuuuulllln\024nff\025[[[[[[[ÔÔÔÔÔÔÔlllllllllllnnnnn\025\025\025\025\025\025[[[NÔÔÔÔÔÔwwwwwwlnnnnnnnnn\025\025\025\025\025\025qqNN\t\t\t\t\tÔwwwwwwwwwnnnnnnnn\025\025\025\025qqqqNN\t\t\t\t\twwwwwwwwzzzz\027\027\027\027\027\025\025\025\025qqqqNN\t\t\t\t\twwwwwwwwwzzzz\027\027\027\027\027\027yyyyyyPPPPPPPwwwwwwwwwzzzz\027\027\027\027\027\027yyyyyyyPPPPPP\033\033\033\033wwwzzzz\027\027\027\027\027\027\027\027yyyyyyy\b\bPPPP\033\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027yyyyyyy\n\n\n\n\n\n÷÷÷÷÷÷÷÷ôôôôîîîî<<<<<ÞÞÞÚÚÚÚÚÚÖÖ÷÷÷÷÷÷÷÷ôôôôîîîî<<<<<ÞÞÞÚÚÚÚÚÚÖÖ÷÷÷÷÷÷÷÷ôôôôîîîî<<<<<ÞÞÞÚÚÚÚÚÚÖÖ\013\013÷÷÷÷÷÷÷ôôôîîîî<<<<<ÞÞÞÞÚÚÚÚÚÖÖ\013\013\013\013÷÷÷÷÷ôôôîîîî<<<<<ÞÞÞÞÚÚÚÚÚÖÖ\013\013\013\013\013\013÷÷÷÷ôôîîîîî<<<<ÞÞÞÞÚÚÚÚÚÖÖ\013\013\013\013\013\013\013÷üüüüîîîîî<<<<ÞÞÞÞÚÚÚÚÚÖÖ\013\013\013\013\013\013\013üüüüüüîîîîîíííááááááÚÚÚÖÖ\013\013\013\013\013\013üüüüüüüüîîîííííáááááááØØØØ\017\017\017\017\017\017üüüüüüüüüííííííááááááØØØØØ\017\017\017\017\017\017ZZZüüûûûûûííííííááááááØØØØ\017\017\017\017\017ZZZZZZûûûûûííííííáááááááØØØ\017\017ZZZZZZZZVVûûûûûûíííííáááááááÜÜZZZZZZZZZZVVVûûûûûûíííí::::::ÜÜÜ\021\021\021\021\021\021ZZZVVVVVûûûûûûëí::::::ÜÜÜÜ\021\021\021\021\021\021\021\021\021\021VVXXXXXXïïïïï:::ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXïïïïïïï:ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïääÜÜÜÜÜkkkkkkkgggggXXXXXXX\rïïïïïïäääÎÎÎkkkkkkkkggggggXX\r\r\r\r\rïïïïïïääÎÎÎkkkkkkkkkggggggf\r\r\r\r\r\rïïïïïÔÔÔÔÎkkkkkkk\024\024\024\024\024fffff[[[[[[SSÔÔÔÔÔÔÔkkkkkk\024\024\024\024\024\024\024\024fff[[[[[[[[ÔÔÔÔÔÔÔllllllllllll\024\024\024\025\025\025[[[[[[[ÔÔÔÔÔÔÔllllllllllllnnn\025\025\025\025\025\025\025[[[[ÔÔÔÔÔÔwwwwwlllllnnnnnn\025\025\025\025\025\025\025\025N\t\t\t\t\t\t\twwwwwwwwwnnnnnnnn\025\025\025\025\025\025qqN\t\t\t\t\t\twwwwwwwwww\027\027\027\027\027\027\027\025\025\025\025\025qqqPP\t\t\t\t\twwwwwwwwwz\027\027\027\027\027\027\027\027yyyyyyyPPPPPPPwwwwwwwwwz\027\027\027\027\027\027\027\027\027yyyyyyyPPPPPPwwwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyy\n\n\nPPP\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027yyyyyyy\n\n\n\n\n\n÷÷÷÷÷÷÷÷÷ôôôîîîîî<<<<<ÞÞÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷÷ôôôîîîîî<<<<<ÞÞÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷÷÷ôôîîîîî<<<<<ÞÞÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷÷÷ôîîîîîî<<<<<ÞÞÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷÷÷÷îîîîîîî<<<ÞÞÞÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷÷÷÷îîîîîîî<<<ÞÞÞÞÚÚÚÚÚÚÚ\013\013÷÷÷÷÷÷÷üüüîîîîîîîííááááÚÚÚÚÚÚÚ\013\013\013÷÷÷÷üüüüüüîîîîííííááááááÚÚÚÚÚZZZZZZüüüüüüüüîííííííáááááááÚÚÚÚZZZZZZZZüüüüüüííííííííáááááááØØØZZZZZZZZZZZûûûûíííííííáááááááØØØZZZZZZZZZZZûûûûûíííííííáááááááØØZZZZZZZZZZZZûûûûûííííííááááááÜÜÜZZZZZZZZZZZVVûûûûûûíííí:::ÜÜÜÜÜÜ\021\021\021ZZZZZZZZZZûûûûûûûííí:::ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021ZZXXXXXXXïïïïï:::ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïÜÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïïÜÜÜÜÜÜkkkkkkkgggggXXXXXXXXïïïïïïïäÜÎÎÎkkkkkkkkggggggXX\r\r\r\r\rïïïïïïïäÎÎÎkkkkkkkk\024\024\024ggggg[[[[[[[ïïÔÔÔÔÔÔÎkkkkkk\024\024\024\024\024\024\024fff[[[[[[[[ÔÔÔÔÔÔÔÔllll\024\024\024\024\024\024\024\024\024\024\024f[[[[[[[[[ÔÔÔÔÔÔÔlllllllllllll\024\024\025\025\025[[[[[[[[ÔÔÔÔÔÔlllllllllllllln\025\025\025\025\025\025\025[[[\t\t\tÔÔÔÔwwwllllllllnnnnn\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwwwwlnnnnnn\025\025\025\025\025\025\025\025q\t\t\t\t\t\t\twwwwwwwww\027\027\027\027\027\027\027\027\027\025\025\025\025\025qPPP\t\t\t\t\twwwwwwwww\027\027\027\027\027\027\027\027\027yyyyyyyPPPPPPPwwwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyy\nPPPPPwwwwww\027\027\027\027\027\027\027\027\027\027\027\027\027yyyyyyy\n\n\n\n\nP\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027yyyyyyy\n\n\n\n\n\n".getBytes(StandardCharsets.ISO_8859_1)
            ;


    /**
     * A hefty addition to {@link #MANOS64}, this keeps the first 64 items the same as MANOS64, and appends 192 colors
     * that are all different enough from existing ones. This is meant to be used for remapping the 63 opaque colors in
     * MANOS64 to any of 255 opaque colors here, using this larger palette for a whole work (256 colors are more than
     * enough for almost any pixel art).
     */
    public static final int[] MANOSSUS256 = new int[] {
            0x00000000, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D11FF, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD29FF, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C621FF, 0x319421FF, 0x4AEF31FF, 0x39AD5AFF, 0x49FF8AFF, 0x319E7AFF, 0x296B5AFF, 0x49B39AFF,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426BFF, 0x394ABDFF,
            0x2910DEFF, 0x29189CFF, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DBFF, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DEFF, 0xE773D6FF, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6BFF, 0xD66B7BFF,
            0x16210BFF, 0x232323FF, 0x082423FF, 0x2C0613FF, 0x353535FF, 0x474747FF, 0x595959FF, 0x3C505BFF,
            0x6B6B6BFF, 0x537166FF, 0x4C724AFF, 0x78795EFF, 0x809D81FF, 0xB3B3B3FF, 0xD7D7D7FF, 0xBBB2D9FF,
            0xE9E9E9FF, 0xC86160FF, 0xD08684FF, 0xD8ABA8FF, 0x8B1005FF, 0xC61300FF, 0xED6858FF, 0x933529FF,
            0xF58D7CFF, 0x9C5A4DFF, 0xFB6440FF, 0xFDB2A0FF, 0xA23111FF, 0xC73908FF, 0xA47F71FF, 0xAA5635FF,
            0xCF5E2CFF, 0x672F16FF, 0xD78350FF, 0xD1AB8CFF, 0xB15301FF, 0x70543AFF, 0xE0A874FF, 0xEEA45CFF,
            0x7E5022FF, 0xBA7825FF, 0xAB7C3DFF, 0xF5A128FF, 0xFEC64CFF, 0xC29D48FF, 0xEFCA64FF, 0x867545FF,
            0xC99A14FF, 0xDAD0B0FF, 0xB4A161FF, 0x8E7211FF, 0x52490EFF, 0xD2BF38FF, 0xCBC26CFF, 0xF0DE10FF,
            0x969735FF, 0xF0F2BCFF, 0xDAE45CFF, 0xA6B925FF, 0xBCC685FF, 0x9FD005FF, 0xAEDE49FF, 0x6A9022FF,
            0x63A702FF, 0x99F841FF, 0x7BEE16FF, 0x64CD0AFF, 0x90BF71FF, 0x72B546FF, 0x45892AFF, 0x29A407FF,
            0x639356FF, 0x97BF8DFF, 0x46AF32FF, 0x7BDA6AFF, 0x98E495FF, 0x66F562FF, 0x013B03FF, 0x4FD456FF,
            0x07FF1BFF, 0x83FF8DFF, 0x378D43FF, 0x6BB87AFF, 0x0B862FFF, 0x06EB6FFF, 0x1BD077FF, 0x24F59BFF,
            0x31B57FFF, 0x65E1B6FF, 0x39DAA3FF, 0x41FFC6FF, 0x038963FF, 0x2F9077FF, 0x7AC6BEFF, 0x83EBE1FF,
            0x06D7C3FF, 0x104947FF, 0x21938FFF, 0xAFF2F5FF, 0x23E1EFFF, 0x4D9AA2FF, 0x81C5D9FF, 0x14BFFFFF,
            0x367997FF, 0x0B9ADBFF, 0x6BA4CEFF, 0x3D78B3FF, 0x207FE3FF, 0x012757FF, 0x0F243FFF, 0x627FAAFF,
            0x185ABFFF, 0x2757A7FF, 0x4C86F6FF, 0xC4D7FCFF, 0x88AEF9FF, 0x115DF3FF, 0x0838CFFF, 0x4461D2FF,
            0x1E3283FF, 0x1735B7FF, 0x2643FBFF, 0x626BFEFF, 0x353FE3FF, 0x5A5A86FF, 0x2C1ABFFF, 0x8E86BDFF,
            0x4A3896FF, 0x7765B2FF, 0x7F64CEFF, 0x6843C2FF, 0xAC90E9FF, 0x9C6EF9FF, 0x762BFEFF, 0x5F0AF2FF,
            0x421372FF, 0x5F1E9EFF, 0x7D28CAFF, 0xC175F0FF, 0x513562FF, 0xA36BC5FF, 0xD897FCFF, 0x7503A6FF,
            0xC050E8FF, 0xBE14E5FF, 0x50105AFF, 0xB82BC5FF, 0xE8B9ECFF, 0x9B46A1FF, 0xEA1AF8FF, 0x9A2099FF,
            0xE557E0FF, 0xAF06A1FF, 0xF917E0FF, 0x7E3C76FF, 0xFC78ECFF, 0xC74DB4FF, 0xDC32BCFF, 0x49103EFF,
            0x93217DFF, 0xF353C8FF, 0xD40D98FF, 0xBF2891FF, 0xDF94C8FF, 0xAA4289FF, 0x945D81FF, 0x751752FF,
            0xB6036DFF, 0xD6499CFF, 0x60324AFF, 0xD76FA4FF, 0x8C385DFF, 0xFA5094FF, 0xF22B70FF, 0xEE90B0FF,
            0xB01A4DFF, 0x841339FF, 0xC2899CFF, 0x580D26FF, 0xEA064CFF, 0xB96479FF, 0xDD4668FF, 0xF80334FF,
            0xD52144FF, 0xB13F55FF, 0xA81A31FF, 0xF46774FF, 0xEB4250FF, 0x66090EFF, 0xB71719FF, 0xBF3C3DFF,
    };
    
    public static final byte[][] MANOSSUS_RAMPS = new byte[][] {
            new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00},
            new byte[]{(byte)0x01, (byte)0x43, (byte)0x01, (byte)0xAE},
            new byte[]{(byte)0x43, (byte)0x40, (byte)0x02, (byte)0x03},
            new byte[]{(byte)0x40, (byte)0x02, (byte)0x03, (byte)0x4A},
            new byte[]{(byte)0x46, (byte)0x48, (byte)0x04, (byte)0x06},
            new byte[]{(byte)0x46, (byte)0x48, (byte)0x05, (byte)0x06},
            new byte[]{(byte)0x48, (byte)0x04, (byte)0x06, (byte)0x4D},
            new byte[]{(byte)0x04, (byte)0x06, (byte)0x07, (byte)0x4E},
            new byte[]{(byte)0x63, (byte)0x71, (byte)0x08, (byte)0x0A},
            new byte[]{(byte)0xF2, (byte)0x53, (byte)0x09, (byte)0x0A},
            new byte[]{(byte)0x63, (byte)0x71, (byte)0x0A, (byte)0x0A},
            new byte[]{(byte)0x54, (byte)0x55, (byte)0x0B, (byte)0x5A},
            new byte[]{(byte)0x43, (byte)0xFD, (byte)0x0C, (byte)0x57},
            new byte[]{(byte)0xF5, (byte)0x52, (byte)0x0D, (byte)0x5B},
            new byte[]{(byte)0x54, (byte)0x5C, (byte)0x0E, (byte)0x60},
            new byte[]{(byte)0x5C, (byte)0x5D, (byte)0x0F, (byte)0x5A},
            new byte[]{(byte)0x61, (byte)0x68, (byte)0x10, (byte)0x62},
            new byte[]{(byte)0x5C, (byte)0x64, (byte)0x11, (byte)0x14},
            new byte[]{(byte)0x43, (byte)0x40, (byte)0x12, (byte)0x65},
            new byte[]{(byte)0x68, (byte)0x6A, (byte)0x13, (byte)0x66},
            new byte[]{(byte)0x64, (byte)0x11, (byte)0x14, (byte)0x6C},
            new byte[]{(byte)0x6A, (byte)0x66, (byte)0x15, (byte)0x79},
            new byte[]{(byte)0x74, (byte)0x73, (byte)0x16, (byte)0x6D},
            new byte[]{(byte)0x70, (byte)0x75, (byte)0x17, (byte)0x79},
            new byte[]{(byte)0x19, (byte)0x78, (byte)0x18, (byte)0x7C},
            new byte[]{(byte)0x12, (byte)0x74, (byte)0x19, (byte)0x78},
            new byte[]{(byte)0x19, (byte)0x7F, (byte)0x1A, (byte)0x7B},
            new byte[]{(byte)0x80, (byte)0x1C, (byte)0x1B, (byte)0x0A},
            new byte[]{(byte)0x86, (byte)0x80, (byte)0x1C, (byte)0x81},
            new byte[]{(byte)0x88, (byte)0x84, (byte)0x1D, (byte)0x79},
            new byte[]{(byte)0x40, (byte)0x8E, (byte)0x1E, (byte)0x86},
            new byte[]{(byte)0x87, (byte)0x20, (byte)0x1F, (byte)0x1B},
            new byte[]{(byte)0x94, (byte)0x87, (byte)0x20, (byte)0x83},
            new byte[]{(byte)0x8E, (byte)0x1E, (byte)0x21, (byte)0x8A},
            new byte[]{(byte)0x87, (byte)0x20, (byte)0x22, (byte)0x1F},
            new byte[]{(byte)0x1E, (byte)0x94, (byte)0x23, (byte)0x96},
            new byte[]{(byte)0x8A, (byte)0x8F, (byte)0x24, (byte)0x1B},
            new byte[]{(byte)0x26, (byte)0x9C, (byte)0x25, (byte)0x27},
            new byte[]{(byte)0x42, (byte)0xA1, (byte)0x26, (byte)0x49},
            new byte[]{(byte)0x26, (byte)0x9D, (byte)0x27, (byte)0x9E},
            new byte[]{(byte)0x27, (byte)0xA0, (byte)0x28, (byte)0x08},
            new byte[]{(byte)0x27, (byte)0x9E, (byte)0x29, (byte)0xA3},
            new byte[]{(byte)0xA8, (byte)0xA5, (byte)0x2A, (byte)0xA4},
            new byte[]{(byte)0xA8, (byte)0x2C, (byte)0x2B, (byte)0xA4},
            new byte[]{(byte)0x47, (byte)0xA8, (byte)0x2C, (byte)0x2B},
            new byte[]{(byte)0xAD, (byte)0x2E, (byte)0x2D, (byte)0xAB},
            new byte[]{(byte)0x01, (byte)0xAD, (byte)0x2E, (byte)0xBD},
            new byte[]{(byte)0x31, (byte)0xB9, (byte)0x2F, (byte)0xB7},
            new byte[]{(byte)0x32, (byte)0x31, (byte)0x30, (byte)0xC7},
            new byte[]{(byte)0x01, (byte)0x32, (byte)0x31, (byte)0xB9},
            new byte[]{(byte)0x43, (byte)0x01, (byte)0x32, (byte)0xC8},
            new byte[]{(byte)0x31, (byte)0x30, (byte)0x33, (byte)0xC6},
            new byte[]{(byte)0xB1, (byte)0xC1, (byte)0x34, (byte)0xC4},
            new byte[]{(byte)0xCF, (byte)0xCA, (byte)0x35, (byte)0xC5},
            new byte[]{(byte)0xBD, (byte)0xBF, (byte)0x36, (byte)0xD4},
            new byte[]{(byte)0x31, (byte)0xCF, (byte)0x37, (byte)0xD3},
            new byte[]{(byte)0xDF, (byte)0xD2, (byte)0x38, (byte)0xE0},
            new byte[]{(byte)0xCF, (byte)0x37, (byte)0x39, (byte)0xDA},
            new byte[]{(byte)0xD7, (byte)0xDD, (byte)0x3A, (byte)0xDC},
            new byte[]{(byte)0xDF, (byte)0xE7, (byte)0x3B, (byte)0xE5},
            new byte[]{(byte)0xE7, (byte)0xE8, (byte)0x3C, (byte)0xED},
            new byte[]{(byte)0x54, (byte)0xFA, (byte)0x3D, (byte)0xEE},
            new byte[]{(byte)0xDF, (byte)0xEA, (byte)0x3E, (byte)0xE6},
            new byte[]{(byte)0xF1, (byte)0xF9, (byte)0x3F, (byte)0xFB},
            new byte[]{(byte)0x01, (byte)0x43, (byte)0x40, (byte)0x02},
            new byte[]{(byte)0x43, (byte)0x40, (byte)0x41, (byte)0x44},
            new byte[]{(byte)0x01, (byte)0x43, (byte)0x42, (byte)0x41},
            new byte[]{(byte)0x43, (byte)0x01, (byte)0x43, (byte)0xEA},
            new byte[]{(byte)0x40, (byte)0x41, (byte)0x44, (byte)0x45},
            new byte[]{(byte)0x41, (byte)0x44, (byte)0x45, (byte)0x46},
            new byte[]{(byte)0x44, (byte)0x45, (byte)0x46, (byte)0x48},
            new byte[]{(byte)0x41, (byte)0x44, (byte)0x47, (byte)0x46},
            new byte[]{(byte)0x45, (byte)0x46, (byte)0x48, (byte)0x04},
            new byte[]{(byte)0x45, (byte)0x46, (byte)0x49, (byte)0x05},
            new byte[]{(byte)0x02, (byte)0x03, (byte)0x4A, (byte)0x88},
            new byte[]{(byte)0x12, (byte)0x65, (byte)0x4B, (byte)0x04},
            new byte[]{(byte)0x65, (byte)0x4B, (byte)0x4C, (byte)0x06},
            new byte[]{(byte)0x48, (byte)0x04, (byte)0x4D, (byte)0x4E},
            new byte[]{(byte)0x04, (byte)0x4D, (byte)0x4E, (byte)0x50},
            new byte[]{(byte)0xBD, (byte)0xBF, (byte)0x4F, (byte)0xB3},
            new byte[]{(byte)0x06, (byte)0x07, (byte)0x50, (byte)0x0A},
            new byte[]{(byte)0xF1, (byte)0xF9, (byte)0x51, (byte)0x3F},
            new byte[]{(byte)0xEC, (byte)0xF5, (byte)0x52, (byte)0x0D},
            new byte[]{(byte)0xF5, (byte)0xF2, (byte)0x53, (byte)0x5B},
            new byte[]{(byte)0x43, (byte)0xFD, (byte)0x54, (byte)0x5C},
            new byte[]{(byte)0xFD, (byte)0x54, (byte)0x55, (byte)0x0B},
            new byte[]{(byte)0xFA, (byte)0xFF, (byte)0x56, (byte)0x58},
            new byte[]{(byte)0xFD, (byte)0x0C, (byte)0x57, (byte)0x5F},
            new byte[]{(byte)0xF9, (byte)0x51, (byte)0x58, (byte)0x5B},
            new byte[]{(byte)0xFD, (byte)0x0C, (byte)0x59, (byte)0x52},
            new byte[]{(byte)0x5C, (byte)0x5D, (byte)0x5A, (byte)0x11},
            new byte[]{(byte)0xF5, (byte)0x52, (byte)0x5B, (byte)0x15},
            new byte[]{(byte)0xFD, (byte)0x54, (byte)0x5C, (byte)0x0E},
            new byte[]{(byte)0x54, (byte)0x5C, (byte)0x5D, (byte)0x0F},
            new byte[]{(byte)0x12, (byte)0x65, (byte)0x5E, (byte)0x53},
            new byte[]{(byte)0x0C, (byte)0x57, (byte)0x5F, (byte)0x10},
            new byte[]{(byte)0x54, (byte)0x5C, (byte)0x60, (byte)0x5A},
            new byte[]{(byte)0x40, (byte)0x12, (byte)0x61, (byte)0x0C},
            new byte[]{(byte)0x68, (byte)0x10, (byte)0x62, (byte)0x67},
            new byte[]{(byte)0x65, (byte)0x5E, (byte)0x63, (byte)0x15},
            new byte[]{(byte)0x54, (byte)0x5C, (byte)0x64, (byte)0x60},
            new byte[]{(byte)0x40, (byte)0x12, (byte)0x65, (byte)0x6F},
            new byte[]{(byte)0x68, (byte)0x6A, (byte)0x66, (byte)0x15},
            new byte[]{(byte)0x10, (byte)0x62, (byte)0x67, (byte)0x14},
            new byte[]{(byte)0x12, (byte)0x61, (byte)0x68, (byte)0x6A},
            new byte[]{(byte)0x5C, (byte)0x64, (byte)0x69, (byte)0x67},
            new byte[]{(byte)0x61, (byte)0x68, (byte)0x6A, (byte)0x13},
            new byte[]{(byte)0x64, (byte)0x11, (byte)0x6B, (byte)0x14},
            new byte[]{(byte)0x69, (byte)0x70, (byte)0x6C, (byte)0x17},
            new byte[]{(byte)0x68, (byte)0x6A, (byte)0x6D, (byte)0x6E},
            new byte[]{(byte)0x6A, (byte)0x6D, (byte)0x6E, (byte)0x17},
            new byte[]{(byte)0x12, (byte)0x65, (byte)0x6F, (byte)0x72},
            new byte[]{(byte)0x64, (byte)0x69, (byte)0x70, (byte)0x6C},
            new byte[]{(byte)0x5E, (byte)0x63, (byte)0x71, (byte)0x0A},
            new byte[]{(byte)0x65, (byte)0x6F, (byte)0x72, (byte)0x18},
            new byte[]{(byte)0x12, (byte)0x74, (byte)0x73, (byte)0x16},
            new byte[]{(byte)0x40, (byte)0x12, (byte)0x74, (byte)0x6F},
            new byte[]{(byte)0x69, (byte)0x70, (byte)0x75, (byte)0x17},
            new byte[]{(byte)0x6F, (byte)0x72, (byte)0x76, (byte)0x6E},
            new byte[]{(byte)0x70, (byte)0x75, (byte)0x77, (byte)0x17},
            new byte[]{(byte)0x74, (byte)0x19, (byte)0x78, (byte)0x76},
            new byte[]{(byte)0x63, (byte)0x71, (byte)0x79, (byte)0x0A},
            new byte[]{(byte)0x7F, (byte)0x7B, (byte)0x7A, (byte)0x17},
            new byte[]{(byte)0x19, (byte)0x7F, (byte)0x7B, (byte)0x7A},
            new byte[]{(byte)0x6F, (byte)0x72, (byte)0x7C, (byte)0x79},
            new byte[]{(byte)0x86, (byte)0x80, (byte)0x7D, (byte)0x1C},
            new byte[]{(byte)0x7F, (byte)0x7B, (byte)0x7E, (byte)0x7A},
            new byte[]{(byte)0x74, (byte)0x19, (byte)0x7F, (byte)0x1A},
            new byte[]{(byte)0x1E, (byte)0x86, (byte)0x80, (byte)0x85},
            new byte[]{(byte)0x87, (byte)0x83, (byte)0x81, (byte)0x1B},
            new byte[]{(byte)0x87, (byte)0x83, (byte)0x82, (byte)0x81},
            new byte[]{(byte)0x94, (byte)0x87, (byte)0x83, (byte)0x81},
            new byte[]{(byte)0x4A, (byte)0x88, (byte)0x84, (byte)0x8C},
            new byte[]{(byte)0x1E, (byte)0x21, (byte)0x85, (byte)0x8B},
            new byte[]{(byte)0x8E, (byte)0x1E, (byte)0x86, (byte)0x7F},
            new byte[]{(byte)0x1E, (byte)0x94, (byte)0x87, (byte)0x8A},
            new byte[]{(byte)0x03, (byte)0x4A, (byte)0x88, (byte)0x93},
            new byte[]{(byte)0x4B, (byte)0x4C, (byte)0x89, (byte)0x7C},
            new byte[]{(byte)0x1E, (byte)0x21, (byte)0x8A, (byte)0x8F},
            new byte[]{(byte)0x21, (byte)0x85, (byte)0x8B, (byte)0x91},
            new byte[]{(byte)0x88, (byte)0x84, (byte)0x8C, (byte)0x1D},
            new byte[]{(byte)0x8A, (byte)0x8F, (byte)0x8D, (byte)0x91},
            new byte[]{(byte)0x43, (byte)0x40, (byte)0x8E, (byte)0x1E},
            new byte[]{(byte)0x21, (byte)0x8A, (byte)0x8F, (byte)0x8D},
            new byte[]{(byte)0x87, (byte)0x20, (byte)0x90, (byte)0x1B},
            new byte[]{(byte)0x85, (byte)0x8B, (byte)0x91, (byte)0x08},
            new byte[]{(byte)0x8E, (byte)0x1E, (byte)0x92, (byte)0x23},
            new byte[]{(byte)0x4A, (byte)0x88, (byte)0x93, (byte)0x84},
            new byte[]{(byte)0x8E, (byte)0x1E, (byte)0x94, (byte)0x92},
            new byte[]{(byte)0x23, (byte)0x96, (byte)0x95, (byte)0x24},
            new byte[]{(byte)0x94, (byte)0x23, (byte)0x96, (byte)0x97},
            new byte[]{(byte)0x23, (byte)0x96, (byte)0x97, (byte)0x24},
            new byte[]{(byte)0x26, (byte)0x9C, (byte)0x98, (byte)0x9A},
            new byte[]{(byte)0x9C, (byte)0x98, (byte)0x99, (byte)0x28},
            new byte[]{(byte)0x9C, (byte)0x98, (byte)0x9A, (byte)0x99},
            new byte[]{(byte)0x98, (byte)0x9A, (byte)0x9B, (byte)0x08},
            new byte[]{(byte)0xA1, (byte)0x26, (byte)0x9C, (byte)0x25},
            new byte[]{(byte)0xA1, (byte)0x26, (byte)0x9D, (byte)0x25},
            new byte[]{(byte)0x9D, (byte)0x27, (byte)0x9E, (byte)0x29},
            new byte[]{(byte)0x27, (byte)0x9E, (byte)0x9F, (byte)0xA3},
            new byte[]{(byte)0x9D, (byte)0x27, (byte)0xA0, (byte)0x28},
            new byte[]{(byte)0x43, (byte)0x42, (byte)0xA1, (byte)0x26},
            new byte[]{(byte)0xA1, (byte)0x26, (byte)0xA2, (byte)0xA5},
            new byte[]{(byte)0x27, (byte)0x9E, (byte)0xA3, (byte)0x0A},
            new byte[]{(byte)0xA5, (byte)0x2A, (byte)0xA4, (byte)0x9F},
            new byte[]{(byte)0x47, (byte)0xA8, (byte)0xA5, (byte)0x2A},
            new byte[]{(byte)0xA8, (byte)0x2C, (byte)0xA6, (byte)0x29},
            new byte[]{(byte)0xAB, (byte)0xA9, (byte)0xA7, (byte)0x2B},
            new byte[]{(byte)0x44, (byte)0x47, (byte)0xA8, (byte)0x2C},
            new byte[]{(byte)0xBD, (byte)0xAB, (byte)0xA9, (byte)0xA7},
            new byte[]{(byte)0x2D, (byte)0xAF, (byte)0xAA, (byte)0xA6},
            new byte[]{(byte)0x2E, (byte)0xBD, (byte)0xAB, (byte)0xAF},
            new byte[]{(byte)0xB8, (byte)0xB0, (byte)0xAC, (byte)0xB2},
            new byte[]{(byte)0x43, (byte)0x01, (byte)0xAD, (byte)0x2E},
            new byte[]{(byte)0x43, (byte)0x01, (byte)0xAE, (byte)0x2E},
            new byte[]{(byte)0x2E, (byte)0x2D, (byte)0xAF, (byte)0xAA},
            new byte[]{(byte)0x32, (byte)0xB8, (byte)0xB0, (byte)0xB7},
            new byte[]{(byte)0x32, (byte)0xB8, (byte)0xB1, (byte)0xAC},
            new byte[]{(byte)0x2F, (byte)0xB7, (byte)0xB2, (byte)0xC4},
            new byte[]{(byte)0xBF, (byte)0x4F, (byte)0xB3, (byte)0x50},
            new byte[]{(byte)0xBD, (byte)0xBF, (byte)0xB4, (byte)0x4F},
            new byte[]{(byte)0xBE, (byte)0xB6, (byte)0xB5, (byte)0xBB},
            new byte[]{(byte)0x31, (byte)0xBE, (byte)0xB6, (byte)0xBC},
            new byte[]{(byte)0xB9, (byte)0x2F, (byte)0xB7, (byte)0xB2},
            new byte[]{(byte)0x01, (byte)0x32, (byte)0xB8, (byte)0xC0},
            new byte[]{(byte)0x32, (byte)0x31, (byte)0xB9, (byte)0xB6},
            new byte[]{(byte)0x31, (byte)0x30, (byte)0xBA, (byte)0xB5},
            new byte[]{(byte)0xC9, (byte)0xC3, (byte)0xBB, (byte)0xC5},
            new byte[]{(byte)0x31, (byte)0xBE, (byte)0xBC, (byte)0xBA},
            new byte[]{(byte)0xAD, (byte)0x2E, (byte)0xBD, (byte)0xAF},
            new byte[]{(byte)0x32, (byte)0x31, (byte)0xBE, (byte)0xBC},
            new byte[]{(byte)0x2E, (byte)0xBD, (byte)0xBF, (byte)0x36},
            new byte[]{(byte)0x32, (byte)0xB8, (byte)0xC0, (byte)0x2F},
            new byte[]{(byte)0xB8, (byte)0xB1, (byte)0xC1, (byte)0x34},
            new byte[]{(byte)0xB9, (byte)0x2F, (byte)0xC2, (byte)0xCD},
            new byte[]{(byte)0x31, (byte)0xC9, (byte)0xC3, (byte)0x35},
            new byte[]{(byte)0xD5, (byte)0xCD, (byte)0xC4, (byte)0xCE},
            new byte[]{(byte)0xCA, (byte)0x35, (byte)0xC5, (byte)0xCB},
            new byte[]{(byte)0x31, (byte)0x30, (byte)0xC6, (byte)0xD1},
            new byte[]{(byte)0x31, (byte)0x30, (byte)0xC7, (byte)0xC6},
            new byte[]{(byte)0x01, (byte)0x32, (byte)0xC8, (byte)0xC0},
            new byte[]{(byte)0x32, (byte)0x31, (byte)0xC9, (byte)0xC3},
            new byte[]{(byte)0x31, (byte)0xCF, (byte)0xCA, (byte)0x35},
            new byte[]{(byte)0x2F, (byte)0xC2, (byte)0xCB, (byte)0xCE},
            new byte[]{(byte)0x01, (byte)0xDF, (byte)0xCC, (byte)0xBD},
            new byte[]{(byte)0xE0, (byte)0xD5, (byte)0xCD, (byte)0xC4},
            new byte[]{(byte)0xD5, (byte)0xCD, (byte)0xCE, (byte)0x36},
            new byte[]{(byte)0x32, (byte)0x31, (byte)0xCF, (byte)0xCA},
            new byte[]{(byte)0xCF, (byte)0x37, (byte)0xD0, (byte)0xD8},
            new byte[]{(byte)0xCF, (byte)0xCA, (byte)0xD1, (byte)0xD6},
            new byte[]{(byte)0x01, (byte)0xDF, (byte)0xD2, (byte)0x38},
            new byte[]{(byte)0x38, (byte)0xD7, (byte)0xD3, (byte)0x39},
            new byte[]{(byte)0xEB, (byte)0xE4, (byte)0xD4, (byte)0x09},
            new byte[]{(byte)0x38, (byte)0xE0, (byte)0xD5, (byte)0xCD},
            new byte[]{(byte)0xCA, (byte)0xD1, (byte)0xD6, (byte)0xD8},
            new byte[]{(byte)0xD2, (byte)0x38, (byte)0xD7, (byte)0xD9},
            new byte[]{(byte)0xD7, (byte)0xD3, (byte)0xD8, (byte)0xDC},
            new byte[]{(byte)0x31, (byte)0xCF, (byte)0xD9, (byte)0xDE},
            new byte[]{(byte)0xCA, (byte)0xD1, (byte)0xDA, (byte)0xDC},
            new byte[]{(byte)0xDF, (byte)0xE7, (byte)0xDB, (byte)0xE6},
            new byte[]{(byte)0xD3, (byte)0xD8, (byte)0xDC, (byte)0xD4},
            new byte[]{(byte)0x38, (byte)0xD7, (byte)0xDD, (byte)0x3A},
            new byte[]{(byte)0xCF, (byte)0xD9, (byte)0xDE, (byte)0xE1},
            new byte[]{(byte)0x43, (byte)0x01, (byte)0xDF, (byte)0xEA},
            new byte[]{(byte)0xD2, (byte)0x38, (byte)0xE0, (byte)0xE5},
            new byte[]{(byte)0xD9, (byte)0xDE, (byte)0xE1, (byte)0xDC},
            new byte[]{(byte)0xE7, (byte)0xE8, (byte)0xE2, (byte)0xE1},
            new byte[]{(byte)0x38, (byte)0xE0, (byte)0xE3, (byte)0xE9},
            new byte[]{(byte)0xE5, (byte)0xEB, (byte)0xE4, (byte)0xD4},
            new byte[]{(byte)0x38, (byte)0xE0, (byte)0xE5, (byte)0xE9},
            new byte[]{(byte)0xE7, (byte)0xDB, (byte)0xE6, (byte)0xF2},
            new byte[]{(byte)0x01, (byte)0xDF, (byte)0xE7, (byte)0xEC},
            new byte[]{(byte)0xDF, (byte)0xE7, (byte)0xE8, (byte)0x3C},
            new byte[]{(byte)0xE0, (byte)0xE3, (byte)0xE9, (byte)0xED},
            new byte[]{(byte)0x01, (byte)0xDF, (byte)0xEA, (byte)0x3E},
            new byte[]{(byte)0xE0, (byte)0xE5, (byte)0xEB, (byte)0xEF},
            new byte[]{(byte)0xDF, (byte)0xE7, (byte)0xEC, (byte)0xF5},
            new byte[]{(byte)0xFA, (byte)0x3D, (byte)0xED, (byte)0xEF},
            new byte[]{(byte)0xFA, (byte)0x3D, (byte)0xEE, (byte)0xED},
            new byte[]{(byte)0xE5, (byte)0xEB, (byte)0xEF, (byte)0xD4},
            new byte[]{(byte)0xF3, (byte)0xF1, (byte)0xF0, (byte)0xF6},
            new byte[]{(byte)0x43, (byte)0xF3, (byte)0xF1, (byte)0xF9},
            new byte[]{(byte)0xEC, (byte)0xF5, (byte)0xF2, (byte)0x53},
            new byte[]{(byte)0x01, (byte)0x43, (byte)0xF3, (byte)0xF1},
            new byte[]{(byte)0x54, (byte)0xFA, (byte)0xF4, (byte)0xFC},
            new byte[]{(byte)0xE7, (byte)0xEC, (byte)0xF5, (byte)0x3F},
            new byte[]{(byte)0xF1, (byte)0xF0, (byte)0xF6, (byte)0xFB},
            new byte[]{(byte)0x54, (byte)0x55, (byte)0xF7, (byte)0x5A},
            new byte[]{(byte)0x54, (byte)0xFA, (byte)0xF8, (byte)0xFC},
            new byte[]{(byte)0xF3, (byte)0xF1, (byte)0xF9, (byte)0x3F},
            new byte[]{(byte)0xFD, (byte)0x54, (byte)0xFA, (byte)0xFF},
            new byte[]{(byte)0xF0, (byte)0xF6, (byte)0xFB, (byte)0x58},
            new byte[]{(byte)0xFA, (byte)0xFF, (byte)0xFC, (byte)0xFB},
            new byte[]{(byte)0x01, (byte)0x43, (byte)0xFD, (byte)0x0C},
            new byte[]{(byte)0xFD, (byte)0x54, (byte)0xFE, (byte)0x0B},
            new byte[]{(byte)0x54, (byte)0xFA, (byte)0xFF, (byte)0x56},
    };

    /**
     * Manually-edited version of the 64-color BLK-NX64 palette, which was made by BLK for pixel art. This reduces
     * the gray-red and purple coverage of BLK-NX64, and improves its coverage of specific shades of peach and brown
     * (human skin tones), yellow, chartreuse, and gray. Repeats the first 64 colors in four blocks, with the exception
     * of the first color (transparent, which changes to black in the repeated blocks).
     * <a href="https://i.imgur.com/LFpwaJl.png">Image preview</a>.
     */
    public static final int[] BETTS64 = {
            0x00000000, 0x010101FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,

            0x000000FF, 0x010101FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,

            0x000000FF, 0x010101FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,

            0x000000FF, 0x010101FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,
    };

    public static final byte[][] BETTS_RAMPS = {
            {0x00, 0x00, 0x00, 0x00,},
            {0x01, 0x01, 0x01, 0x02,},
            {0x01, 0x01, 0x02, 0x15,},
            {0x01, 0x02, 0x03, 0x3A,},
            {0x02, 0x03, 0x04, 0x05,},
            {0x03, 0x04, 0x05, 0x06,},
            {0x04, 0x05, 0x06, 0x07,},
            {0x05, 0x06, 0x07, 0x07,},
            {0x0B, 0x0A, 0x08, 0x11,},
            {0x0B, 0x0A, 0x09, 0x08,},
            {0x0D, 0x0B, 0x0A, 0x08,},
            {0x0C, 0x0D, 0x0B, 0x0A,},
            {0x1E, 0x15, 0x0C, 0x0D,},
            {0x15, 0x0C, 0x0D, 0x0E,},
            {0x0C, 0x0D, 0x0E, 0x0F,},
            {0x0D, 0x0E, 0x0F, 0x10,},
            {0x0E, 0x0F, 0x10, 0x11,},
            {0x0F, 0x10, 0x11, 0x12,},
            {0x14, 0x13, 0x12, 0x07,},
            {0x0C, 0x14, 0x13, 0x12,},
            {0x15, 0x0C, 0x14, 0x13,},
            {0x01, 0x1E, 0x15, 0x16,},
            {0x1E, 0x15, 0x16, 0x17,},
            {0x15, 0x16, 0x17, 0x18,},
            {0x16, 0x17, 0x18, 0x19,},
            {0x17, 0x18, 0x19, 0x07,},
            {0x1C, 0x1B, 0x1A, 0x19,},
            {0x1D, 0x1C, 0x1B, 0x1A,},
            {0x26, 0x1D, 0x1C, 0x24,},
            {0x1E, 0x26, 0x1D, 0x1C,},
            {0x01, 0x01, 0x1E, 0x02,},
            {0x1E, 0x26, 0x1F, 0x20,},
            {0x26, 0x1F, 0x20, 0x21,},
            {0x1F, 0x20, 0x21, 0x22,},
            {0x20, 0x21, 0x22, 0x2C,},
            {0x25, 0x24, 0x23, 0x2A,},
            {0x26, 0x25, 0x24, 0x23,},
            {0x1E, 0x26, 0x25, 0x24,},
            {0x01, 0x1E, 0x26, 0x1D,},
            {0x01, 0x02, 0x27, 0x39,},
            {0x02, 0x27, 0x28, 0x29,},
            {0x27, 0x28, 0x29, 0x2A,},
            {0x28, 0x29, 0x2A, 0x2B,},
            {0x29, 0x2A, 0x2B, 0x22,},
            {0x2E, 0x2D, 0x2C, 0x07,},
            {0x2F, 0x2E, 0x2D, 0x2C,},
            {0x3A, 0x2F, 0x2E, 0x2D,},
            {0x38, 0x3A, 0x2F, 0x2E,},
            {0x38, 0x37, 0x30, 0x31,},
            {0x37, 0x30, 0x31, 0x32,},
            {0x30, 0x31, 0x32, 0x3D,},
            {0x36, 0x35, 0x33, 0x3F,},
            {0x37, 0x36, 0x34, 0x33,},
            {0x37, 0x36, 0x35, 0x34,},
            {0x38, 0x37, 0x36, 0x35,},
            {0x02, 0x38, 0x37, 0x36,},
            {0x01, 0x02, 0x38, 0x37,},
            {0x02, 0x27, 0x39, 0x36,},
            {0x02, 0x38, 0x3A, 0x3B,},
            {0x38, 0x3A, 0x3B, 0x04,},
            {0x3A, 0x3B, 0x3C, 0x35,},
            {0x0B, 0x0A, 0x3D, 0x3E,},
            {0x0A, 0x3D, 0x3E, 0x3F,},
            {0x35, 0x33, 0x3F, 0x07,},
    };

    public static final int[] FANCY_MANOS64 = new int[256];
    public static final int[] FANCY_BETTS64 = new int[256];

    public static final int[] FULL_GRAY = new int[256];
    static {
        System.arraycopy(MANOS64, 0, FANCY_MANOS64, 128, 64);
        for (int i = 1; i < 64; i++) {
            FANCY_MANOS64[i] = lighten(MANOS64[i], 0.1f);
            FANCY_MANOS64[i | 64] = darken(MANOS64[i], 0.3f);
            FANCY_MANOS64[i | 192] = darken(MANOS64[i], 0.15f);
        }
        System.arraycopy(BETTS64, 0, FANCY_BETTS64, 0, 256);
        for (int i = 1; i < 64; i++) {
            FANCY_BETTS64[i | 64] = lighten(MANOS64[i], 0.1f);
            FANCY_BETTS64[i | 128] = darken(MANOS64[i], 0.15f);
            FANCY_BETTS64[i | 192] = darken(MANOS64[i], 0.3f);
        }
        for (int i = 1; i < 256; i++) {
            FULL_GRAY[i] = i * 0x01010100 | 0xFF;
        }
    }

    /**
     * Like how {@link #MANOSSUS256} contains all of {@link #MANOS64}, this contains all of {@link #BETTS64}.
     * Meant for palette reductions that should accurately preserve the BETTS64 colors.
     */
    public static final int[] BETSY256 = new int[] {
            0x00000000, 0x000000FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,
            0xC99A14FF, 0xFEC64CFF, 0xC29D48FF, 0xBA7825FF, 0xFFAA4DFF, 0xD39A5EFF, 0xEEA45CFF, 0xD1AB8CFF,
            0xAD6329FF, 0xD78350FF, 0xB15301FF, 0xE35A00FF, 0xCF5E2CFF, 0x672F16FF, 0xAA5635FF, 0xC73908FF,
            0xA23111FF, 0x9C5A4DFF, 0xFB6440FF, 0xA47F71FF, 0x8B1005FF, 0xF58D7CFF, 0xC61300FF, 0x7B2921FF,
            0xED6858FF, 0x66090EFF, 0x933529FF, 0xE73129FF, 0xB71719FF, 0xE79C94FF, 0xBF3C3DFF, 0xF80334FF,
            0xC86160FF, 0xD08684FF, 0xEB4250FF, 0xA81A31FF, 0xD52144FF, 0xF46774FF, 0xEA064CFF, 0x580D26FF,
            0xD66B7BFF, 0xB01A4DFF, 0x2C0613FF, 0xDD4668FF, 0xF22B70FF, 0xCF1562FF, 0x8C385DFF, 0x845A6BFF,
            0xE7298CFF, 0xB6036DFF, 0x60324AFF, 0xD76FA4FF, 0x751752FF, 0xD40D98FF, 0x49103EFF, 0xBF2891FF,
            0xAA4289FF, 0xF353C8FF, 0x945D81FF, 0x93217DFF, 0xDC32BCFF, 0xDF94C8FF, 0xF917E0FF, 0xC74DB4FF,
            0xE773D6FF, 0xFC78ECFF, 0xAF06A1FF, 0x9A2099FF, 0xE557E0FF, 0xD631DEFF, 0xB82BC5FF, 0xEA1AF8FF,
            0x9B46A1FF, 0x6B1873FF, 0x50105AFF, 0x9C29B5FF, 0xBE14E5FF, 0xC050E8FF, 0xD897FCFF, 0x513562FF,
            0xA36BC5FF, 0xCEADE7FF, 0x7D28CAFF, 0x5F1E9EFF, 0x9C6EF9FF, 0xAC90E9FF, 0xBBB2D9FF, 0x6843C2FF,
            0x7F64CEFF, 0x762BFEFF, 0x8E86BDFF, 0x6329E7FF, 0x4A3896FF, 0x5F0AF2FF, 0x5A5A86FF, 0x21105AFF,
            0x626BFEFF, 0x29189CFF, 0xB3B3B3FF, 0x474747FF, 0xE9E9E9FF, 0x6B6B6BFF, 0x2C1ABFFF, 0x394ABDFF,
            0x2910DEFF, 0xC4D7FCFF, 0x4461D2FF, 0x88AEF9FF, 0x2643FBFF, 0x1E3283FF, 0x0838CFFF, 0x627FAAFF,
            0x115DF3FF, 0x012757FF, 0x29426BFF, 0x2757A7FF, 0x185ABFFF, 0x207FE3FF, 0x396B9CFF, 0x3D78B3FF,
            0x6BA4CEFF, 0x0B9ADBFF, 0x81C5D9FF, 0x42A5C6FF, 0xAFC7CFFF, 0x6E868EFF, 0x52CEEFFF, 0x39BDC6FF,
            0x23E1EFFF, 0xAFF2F5FF, 0x082423FF, 0x83EBE1FF, 0x21938FFF, 0x7AC6BEFF, 0x104947FF, 0x49B39AFF,
            0x296B5AFF, 0x319E7AFF, 0x41FFC6FF, 0x65E1B6FF, 0x39DAA3FF, 0x038963FF, 0x537166FF, 0x31B57FFF,
            0x24F59BFF, 0x49FF8AFF, 0x06EB6FFF, 0x39AD5AFF, 0x6BB87AFF, 0x378D43FF, 0x83FF8DFF, 0xD6F7D6FF,
            0x809D81FF, 0x4C724AFF, 0x4FD456FF, 0x66F562FF, 0x07FF1BFF, 0x4AEF31FF, 0x46AF32FF, 0x39C621FF,
            0x7BDA6AFF, 0x29A407FF, 0x97BF8DFF, 0x62FF39FF, 0x45892AFF, 0x639356FF, 0x314A29FF, 0x16210BFF,
            0x213118FF, 0x64CD0AFF, 0x7BEE16FF, 0x72B546FF, 0x99F841FF, 0xBFEF94FF, 0x96DF1DFF, 0x9FD005FF,
            0xC7FF2DFF, 0x8CAD29FF, 0xA6B925FF, 0x6B7321FF, 0xBCC685FF, 0xDAE45CFF, 0x969735FF, 0xCBC26CFF,
            0xBDB573FF, 0x52490EFF, 0xFBE76AFF, 0xA58C29FF, 0xDAD0B0FF, 0x8E7211FF, 0xEFCA64FF, 0x867545FF,
    };

    /**
     * A nice and versatile geometric palette that has extremely broad coverage of the most saturated colors and some of
     * the least-saturated colors.
     */
    public static final int[] YAM256 = {
            0x00000000, 0x000000FF, 0x050403FF, 0x0F0D0CFF, 0x1A1817FF, 0x292625FF, 0x393534FF, 0x4A4645FF,
            0x5C5957FF, 0x726E6CFF, 0x878381FF, 0x9D9997FF, 0xB6B2B0FF, 0xCFCAC8FF, 0xE9E4E2FF, 0xFBFFFFFF,
            0x2B1C1BFF, 0x574241FF, 0x8C7472FF, 0xC9ADAAFF, 0x2D231CFF, 0x594D44FF, 0x8F8176FF, 0xCCBCB0FF,
            0x2D241DFF, 0x5B4F46FF, 0x908277FF, 0xCFBEB2FF, 0x2E2820FF, 0x5C554BFF, 0x938B7EFF, 0xD3CABCFF,
            0x323425FF, 0x626451FF, 0x9A9D87FF, 0xD9DCC4FF, 0x293523FF, 0x56644FFF, 0x8C9C83FF, 0xCADCC0FF,
            0x1D2F21FF, 0x405645FF, 0x6A836FFF, 0x9BB6A0FF, 0x1F3335FF, 0x4A6264FF, 0x809B9EFF, 0xBDDCDFFF,
            0x0F1B2BFF, 0x334459FF, 0x637690FF, 0x9BB1CEFF, 0x1D182CFF, 0x45405AFF, 0x777090FF, 0xB2AACFFF,
            0x251B2EFF, 0x4F435BFF, 0x837491FF, 0xBFAFD0FF, 0x2C1D2EFF, 0x5A475CFF, 0x907A93FF, 0xCFB5D2FF,
            0x662F28FF, 0x98584EFF, 0xCE8579FF, 0x663B28FF, 0x986650FF, 0xD0977EFF, 0x68412CFF, 0x9A6C54FF,
            0xD3A085FF, 0x68452FFF, 0x9A7258FF, 0xD0A386FF, 0x6A4A2DFF, 0x9C7756FF, 0xD3AA85FF, 0x6C5131FF,
            0x9E7F5BFF, 0xD7B58DFF, 0x6A5A36FF, 0x9E8B63FF, 0xD7C296FF, 0x6E6C3DFF, 0xA19F6AFF, 0xDAD89EFF,
            0x67753BFF, 0x98A868FF, 0xD0E29CFF, 0x59703BFF, 0x8BA56AFF, 0xC2DF9EFF, 0x4C6F39FF, 0x7BA366FF,
            0xB0DD99FF, 0x366F35FF, 0x64A362FF, 0x97DD94FF, 0x346C4FFF, 0x569271FF, 0x7CBC98FF, 0x2B665DFF,
            0x49877DFF, 0x69AAA0FF, 0x326672FF, 0x5E97A5FF, 0x92CFDFFF, 0x294B6CFF, 0x52789FFF, 0x81ACD7FF,
            0x1D2565FF, 0x3E4D97FF, 0x667ACEFF, 0x312663FF, 0x584E95FF, 0x877ECEFF, 0x422866FF, 0x6C5199FF,
            0x9D7ED0FF, 0x492A66FF, 0x765399FF, 0xA983D1FF, 0x552B67FF, 0x845499FF, 0xBA85D3FF, 0x5E2E69FF,
            0x90599CFF, 0xC789D4FF, 0x692B54FF, 0x9B5382FF, 0xD382B5FF, 0x672B34FF, 0x99535CFF, 0xCF8088FF,
            0xB0342EFF, 0xDB554BFF, 0xB1432EFF, 0xDC654CFF, 0xB0512CFF, 0xDD754EFF, 0xB25D33FF, 0xDE8155FF,
            0xB3612EFF, 0xDF8650FF, 0xB56832FF, 0xDE8B53FF, 0xB36731FF, 0xE08C54FF, 0xB06F35FF, 0xDF975AFF,
            0xB47B35FF, 0xE0A259FF, 0xB4863CFF, 0xDEAD5FFF, 0xB0923AFF, 0xDEBE62FF, 0xB4A740FF, 0xE1D468FF,
            0xB4C048FF, 0xDDEC6FFF, 0xA7BD4CFF, 0xD0EA73FF, 0x95BA47FF, 0xBFE86FFF, 0x82BE46FF, 0xA8E96BFF,
            0x6EBA40FF, 0x96E868FF, 0x50BD47FF, 0x76E86BFF, 0x30B24CFF, 0x47CA5FFF, 0x23A979FF, 0x30B482FF,
            0x2BAD9AFF, 0x44C4B1FF, 0x38BDC1FF, 0x63E7ECFF, 0x319DBCFF, 0x5AC7E8FF, 0x2D80B7FF, 0x51A7E2FF,
            0x1D44B0FF, 0x3967DCFF, 0x322CAFFF, 0x4B50DBFF, 0x4B2BB2FF, 0x684DDDFF, 0x662CB3FF, 0x8850DFFF,
            0x6D2FB3FF, 0x9052DFFF, 0x7A31B5FF, 0xA155E2FF, 0x8631B5FF, 0xAE56E2FF, 0x9431B1FF, 0xBE56DEFF,
            0x9F35B3FF, 0xCA5AE0FF, 0xAE36B6FF, 0xDB5CE3FF, 0xB33581FF, 0xDD56A4FF, 0xB33156FF, 0xDF5376FF,
            0xFD2918FF, 0xFA4411FF, 0xFD580BFF, 0xFC6A1AFF, 0xF8721EFF, 0xFD740EFF, 0xF9770FFF, 0xFA7E17FF,
            0xFA831BFF, 0xFD8F14FF, 0xFB9A1EFF, 0xFDA514FF, 0xF7B321FF, 0xF8C41FFF, 0xFDDC27FF, 0xFBF42CFF,
            0xEDFF21FF, 0xDAFF2FFF, 0xC9FF29FF, 0xAFFF21FF, 0x94FF19FF, 0x7AFF27FF, 0x49FF21FF, 0x00FF3CFF,
            0x00EE6EFF, 0x00DF9BFF, 0x00DFBBFF, 0x00EAE0FF, 0x00EBFFFF, 0x00CEFFFF, 0x00ADFFFF, 0x007EFFFF,
            0x2200FFFF, 0x4300FFFF, 0x5F00FFFF, 0x7900FFFF, 0x8F05FFFF, 0x9B13FEFF, 0xA600FFFF, 0xB510FFFF,
            0xBD15FFFF, 0xCA11FFFF, 0xE10AFFFF, 0xED16FCFF, 0xFD15DFFF, 0xFD1BA4FF, 0xFF1271FF, 0xFE173BFF,
    };

    public static final int[] TETRA256 = new int[] {
            0x00000000, 0x000000FF, 0x282828FF, 0x585858FF, 0x868686FF, 0xA2A2A2FF, 0xCBCBCBFF, 0xFFFFFFFF,
            0xA6D755FF, 0x8ABF5EFF, 0x7AA221FF, 0x6E8A31FF, 0x0A4111FF, 0x0F661DFF, 0x147E25FF, 0x229E35FF,
            0x42C372FF, 0x8CE7A6FF, 0x78FAE6FF, 0x00C7A5FF, 0x009282FF, 0x163135FF, 0x21526BFF, 0x3B768FFF,
            0x53A1ADFF, 0x8CDAFFFF, 0x50AAF7FF, 0x3E83EBFF, 0x354AD7FF, 0x1D2DAAFF, 0x152135FF, 0x66397EFF,
            0x7E5596FF, 0xA68ABFFF, 0xDFBFEFFF, 0xC772FFFF, 0x9245E7FF, 0x6E05C3FF, 0x350082FF, 0x721C2FFF,
            0xB22E69FF, 0xE54286FF, 0xFF6EAFFF, 0xFF9AC7FF, 0xFFD7F3FF, 0xE3B3C3FF, 0xCB96A2FF, 0xAA768AFF,
            0xCF4929FF, 0xF3820DFF, 0xFFAA0DFF, 0xFFD3A6FF, 0xFFBB8AFF, 0xE7A67AFF, 0xBB7251FF, 0x82491DFF,
            0x513115FF, 0xB7515AFF, 0x6E5A51FF, 0x8A7A5AFF, 0xB79E5EFF, 0xDFC721FF, 0xFFDF00FF, 0xFFF3B3FF,
            0x050403FF, 0x0F0D0CFF, 0x1A1817FF, 0x1D182CFF, 0x251B2EFF, 0x2B1C1BFF, 0x2C1D2EFF, 0x2D231CFF,
            0x2D241DFF, 0x1D2F21FF, 0x2E2820FF, 0x323425FF, 0x393534FF, 0x334459FF, 0x4A4645FF, 0x45405AFF,
            0x574241FF, 0x4F435BFF, 0x5A475CFF, 0x405645FF, 0x594D44FF, 0x5B4F46FF, 0x5C554BFF, 0x56644FFF,
            0x4A6264FF, 0x726E6CFF, 0x637690FF, 0x777090FF, 0x8C7472FF, 0x837491FF, 0x6A836FFF, 0x8F8176FF,
            0x908277FF, 0x938B7EFF, 0x8C9C83FF, 0x809B9EFF, 0x9A9D87FF, 0x9BB6A0FF, 0x9BB1CEFF, 0xB2AACFFF,
            0xB6B2B0FF, 0xC9ADAAFF, 0xCCBCB0FF, 0xD3CABCFF, 0xBDDCDFFF, 0xCADCC0FF, 0xD9DCC4FF, 0x9E8B63FF,
            0x6A5A36FF, 0xF7B321FF, 0xDEAD5FFF, 0xB4863CFF, 0xD7B58DFF, 0xB47B35FF, 0xE0A259FF, 0xFB9A1EFF,
            0x9E7F5BFF, 0x6A4A2DFF, 0xFD8F14FF, 0xDF975AFF, 0xB56832FF, 0xDE8B53FF, 0xD3A085FF, 0xD0A386FF,
            0xB3612EFF, 0xFA7E17FF, 0xE08C54FF, 0x9A7258FF, 0x68412CFF, 0x68452FFF, 0xB36731FF, 0xFD740EFF,
            0xF9770FFF, 0xDE8155FF, 0xDF8650FF, 0xF8721EFF, 0x9A6C54FF, 0xFC6A1AFF, 0xB25D33FF, 0xB0512CFF,
            0x663B28FF, 0xDD754EFF, 0xFD580BFF, 0x986650FF, 0x98584EFF, 0xFA4411FF, 0xB1432EFF, 0xDC654CFF,
            0xFD2918FF, 0xB0342EFF, 0xCE8579FF, 0xFE173BFF, 0xCF8088FF, 0xFF1271FF, 0xFD1BA4FF, 0xDD56A4FF,
            0xB33581FF, 0x692B54FF, 0x9B5382FF, 0xFD15DFFF, 0xAE36B6FF, 0xDB5CE3FF, 0xED16FCFF, 0xE10AFFFF,
            0x90599CFF, 0x9F35B3FF, 0xC789D4FF, 0xBE56DEFF, 0xCA11FFFF, 0x9431B1FF, 0x552B67FF, 0xBA85D3FF,
            0xBD15FFFF, 0xB510FFFF, 0x8631B5FF, 0xAE56E2FF, 0x492A66FF, 0xA155E2FF, 0xA600FFFF, 0x7A31B5FF,
            0x9B13FEFF, 0x765399FF, 0x9052DFFF, 0x422866FF, 0x6D2FB3FF, 0x8F05FFFF, 0x9D7ED0FF, 0x6C5199FF,
            0x7900FFFF, 0x584E95FF, 0x684DDDFF, 0x877ECEFF, 0x4B2BB2FF, 0x5F00FFFF, 0x4300FFFF, 0x3E4D97FF,
            0x1D2565FF, 0x667ACEFF, 0x2200FFFF, 0x3967DCFF, 0x1D44B0FF, 0x81ACD7FF, 0x00ADFFFF, 0x2D80B7FF,
            0x5AC7E8FF, 0x326672FF, 0x319DBCFF, 0x00CEFFFF, 0x00EBFFFF, 0x38BDC1FF, 0x00EAE0FF, 0x69AAA0FF,
            0x2BAD9AFF, 0x2B665DFF, 0x00DFBBFF, 0x00DF9BFF, 0x23A979FF, 0x30B482FF, 0x7CBC98FF, 0x569271FF,
            0x346C4FFF, 0x00EE6EFF, 0x00FF3CFF, 0x64A362FF, 0x76E86BFF, 0x50BD47FF, 0x49FF21FF, 0x7AFF27FF,
            0x7BA366FF, 0xB0DD99FF, 0x94FF19FF, 0xA8E96BFF, 0x82BE46FF, 0xAFFF21FF, 0x59703BFF, 0x8BA56AFF,
            0xBFE86FFF, 0xC2DF9EFF, 0xC9FF29FF, 0x98A868FF, 0xA7BD4CFF, 0xD0EA73FF, 0xDAFF2FFF, 0xB4C048FF,
            0xDDEC6FFF, 0xEDFF21FF, 0x6E6C3DFF, 0xFBF42CFF, 0xE1D468FF, 0xB4A740FF, 0xDEBE62FF, 0xB0923AFF,
    };



    /*
     * Generated with a Halton sequence through Oklab color space, with increased gray coverage but fewer total colors,
     * to make room for special markers.
     */
/*    public static final int[] HALTONITE240 = new int[]{
            0x00000000, 0x000000FF, 0x141414FF, 0x282828FF, 0x291710FF, 0x1E222AFF, 0x3B3B3BFF, 0x434531FF,
            0x4F4F4FFF, 0x5C5C5CFF, 0x6A6A6AFF, 0x56626FFF, 0x787878FF, 0x878787FF, 0x999999FF, 0xAAAAAAFF,
            0xBBBBBBFF, 0xC2BBA9FF, 0xCCCCCCFF, 0xDDDDDDFF, 0xEEEEEEFF, 0xFFFFFFFF, 0xE31515FF, 0x5B0606FF,
            0xF23C37FF, 0xDA938FFF, 0xB41808FF, 0xFE1D02FF, 0xFFBBB2FF, 0x3F1C16FF, 0xF4A293FF, 0xC0422AFF,
            0x5B2E24FF, 0xB46B58FF, 0xF2825BFF, 0xC2836DFF, 0xA7593AFF, 0xE06635FF, 0xD35521FF, 0x793210FF,
            0xA44210FF, 0xCF794CFF, 0xFF9A60FF, 0xF8B891FF, 0x7E5030FF, 0xAE9784FF, 0x86694EFF, 0x502B06FF,
            0xEA7B0BFF, 0xEDD4B9FF, 0xDFAC73FF, 0x794811FF, 0xBB7213FF, 0x7B603CFF, 0xDFA24BFF, 0xE3971BFF,
            0xFFC24FFF, 0xF2CF80FF, 0x906D1DFF, 0xB0871FFF, 0xFDE5A7FF, 0xEDB71AFF, 0xA19150FF, 0xEDE389FF,
            0xF4E54FFF, 0x83804EFF, 0xBEBB4EFF, 0xE6E21DFF, 0xA4A222FF, 0xABAC7CFF, 0xF5FE22FF, 0xA5A94CFF,
            0xF6FF95FF, 0xBFD22EFF, 0xECFE65FF, 0xF2FAC2FF, 0x9BBD25FF, 0xB7C87DFF, 0xC1E367FF, 0x7A9633FF,
            0xB8FA35FF, 0x8FD111FF, 0x64852CFF, 0x223308FF, 0x496B1CFF, 0x88ED12FF, 0x42582CFF, 0x264509FF,
            0x397E0EFF, 0xB1FB85FF, 0xC5F8ABFF, 0x3DBB14FF, 0x68915EFF, 0xA0DE92FF, 0x47723EFF, 0x6AEF59FF,
            0x71FE60FF, 0x11560BFF, 0x78AE77FF, 0x1E9922FF, 0x1FA92AFF, 0x61C56DFF, 0x16D72EFF, 0x0AE930FF,
            0x48A75CFF, 0x3EDB6AFF, 0x24BC57FF, 0x71CB92FF, 0x4FF990FF, 0x35E986FF, 0x169355FF, 0x9BE8C3FF,
            0x9EFFD1FF, 0x68B69CFF, 0x10EBAFFF, 0x549787FF, 0x14D7ADFF, 0x1FAE9AFF, 0x117F76FF, 0x4DF7ECFF,
            0xB1FEFAFF, 0x124D4AFF, 0x69D4D3FF, 0x25E5F4FF, 0x75BFC7FF, 0x2DB2CEFF, 0x1B90B4FF, 0x1C5C7DFF,
            0x469AC6FF, 0x244E65FF, 0xAADAF7FF, 0x1E9CFBFF, 0x3D79ADFF, 0x8899AAFF, 0x83A3C3FF, 0x76B6F8FF,
            0x5AA7FEFF, 0x406697FF, 0x4282E6FF, 0x2C69D4FF, 0x1662FCFF, 0x184BB9FF, 0x1F439BFF, 0x081236FF,
            0xB2C1FAFF, 0x031050FF, 0x1127ABFF, 0x7C82C2FF, 0x090B23FF, 0x8E94E8FF, 0x3538F9FF, 0x4F50C5FF,
            0x7B7CFCFF, 0x2424EDFF, 0x1D16D5FF, 0x3B3962FF, 0x201693FF, 0x6D63C8FF, 0x3E3486FF, 0xB6B1D2FF,
            0x2E1E6EFF, 0x7E64E4FF, 0x7147FEFF, 0xB39CF5FF, 0x4413BFFF, 0x4F15DAFF, 0x260E5DFF, 0x5D3BAAFF,
            0x6625E1FF, 0x5A4382FF, 0x7A42DBFF, 0x480C8BFF, 0xAC6BF1FF, 0x8C6BA9FF, 0x680AA7FF, 0x8420C6FF,
            0xAC29F6FF, 0xB380C7FF, 0x650C83FF, 0x55096CFF, 0x875695FF, 0xCA4EECFF, 0xE994FDFF, 0x7E3E8CFF,
            0xD086E0FF, 0x9D36B1FF, 0xB53FC6FF, 0xD769E6FF, 0x7B1A88FF, 0xD308EDFF, 0x9604A4FF, 0x500153FF,
            0xFBBAFAFF, 0xA284A1FF, 0xB767B3FF, 0xFE43F2FF, 0xC221B4FF, 0xB00D9FFF, 0x32042DFF, 0xF925DFFF,
            0xFC73E9FF, 0xE90FCAFF, 0xD44ABEFF, 0xE25EC4FF, 0xF8ACE4FF, 0xA31479FF, 0xAB5692FF, 0xE31BA2FF,
            0x9D3F7CFF, 0x8C135FFF, 0x6B3858FF, 0x4B0932FF, 0xF372C1FF, 0xCC0E7EFF, 0x71174CFF, 0xF645A3FF,
            0xC03E83FF, 0xD55193FF, 0xF095C2FF, 0x893A5FFF, 0xAB134FFF, 0xE83175FF, 0xF93E83FF, 0xB46D86FF,
            0xA05B73FF, 0xE71A58FF, 0x22040DFF, 0xD27E96FF, 0xBC3E61FF, 0xD51144FF, 0xFE7B9AFF, 0xFE6885FF,
            0xDF6A7FFF, 0x8C525CFF, 0xA83F4BFF, 0x76121CFF, 0xD34B57FF, 0xC21A26FF, 0x87353AFF, 0x981C20FF,
            0xEA5A5EFF,
          //            0x48384840, 0x50405040, 0x58485840, 0x60506040, 0x68586840, 0x70607040, 0x78687840,
          //0x80708040, 0x88788840, 0x90809040, 0x98889840, 0xA070A040, 0xA860A840, 0xB050B040, 0xB840B840,
    };
    */
}
