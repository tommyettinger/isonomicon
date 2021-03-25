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
     * Manually-edited version of the 64-color Dawnplumnik palette, which was made by combining DawnBringer's Iso22
     * bman4750's Super Plum 21, and Vinik's Vinik24. Has better gray, orange, yellow, and brown coverage, and reduced
     * cyan coverage (which was very heavy originally). <a href="https://i.imgur.com/es1zfEU.png">Image preview</a>.
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

            0x00000000, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
            0xD6F7D6FF, 0xFBD7EBFF, 0xFDFBE3FF, 0xE73129FF, 0x7B2921FF, 0xE79C94FF, 0xBF4529FF, 0xE35A00FF,
            0xAD6329FF, 0xE78431FF, 0x4A2D11FF, 0xD39A5EFF, 0xFFAA4DFF, 0xF7CF9EFF, 0xA58C29FF, 0xFBE76AFF,
            0xBDB573FF, 0x6B7321FF, 0x8CAD29FF, 0xC7FF2DFF, 0x96DF1DFF, 0xBFEF94FF, 0x296318FF, 0x62FF39FF,
            0x39C621FF, 0x319421FF, 0x4AEF31FF, 0x39AD5AFF, 0x49FF8AFF, 0x319E7AFF, 0x296B5AFF, 0x49B39AFF,
            0x52F7DEFF, 0xA5DEDEFF, 0x39BDC6FF, 0x52CEEFFF, 0x42A5C6FF, 0x396B9CFF, 0x29426BFF, 0x394ABDFF,
            0x2910DEFF, 0x29189CFF, 0x21105AFF, 0x6329E7FF, 0x9C84CEFF, 0x8A49DBFF, 0xCEADE7FF, 0x9C29B5FF,
            0x6B1873FF, 0xD631DEFF, 0xE773D6FF, 0xA52973FF, 0xE7298CFF, 0xCF1562FF, 0x845A6BFF, 0xD66B7BFF,

            0x00000000, 0x19092DFF, 0x213118FF, 0x314A29FF, 0x8C847BFF, 0x6E868EFF, 0x9CA59CFF, 0xAFC7CFFF,
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
            "\001\001\001\001\001\001\001\001\001\001\001\00122222221110000000000\001\001\001\001\001\001\001\001\001\001\001222222211111000000000\001\001\001\001\001\001\001\001\001\001\001222222211111000000000\001\001\001\001\001\001\001\001\001\001\001222222211111100000000\002\002\002\002\002\001\001\001\00122222222211111100000000\002\002\002\002\002\002\002\002\00222222222111111110000000\002\002\002\002\002\002\002\002\002\002.........1111111100000\002\002\002\002\002\002\002\002\002\002............111111//00\002\002\002\002\002\002\002\002\002\002..............////////\002\002\002\002\002\002\002\002\002...............////////\036\036\036\036\036\036\036\036................////////\036\036\036\036\036\036\036\036&&&&&&&&&....../////////\036\036\036\036\036\036\036\036&&&&&&&&&&&&&&---///////\036\036\036\036\036\036\036&&&&&&&&&&&&&&&------////\036\036\036\036\036\036\036&&&&&&&&&&&&&&&---------/\036\036\036\036\036\036&&&&&&&&&&&&&&&&----------!!!!!&&&&&&&&&&&&&&&%%%%-------,!!!!!!!!!!%%%%%%%%%%%%%%%%%--,,,!!!!!!!!!!%%%%%%%%%%%%%%%%%,,,,,!!!!!!!!!!%%%%%%%%%%%%%%%%******!!!!!!!!!!%%%%%%%%%%%%%%%*******!!!!!!!!!%%%%%%%%%%%%%%%********!!!!!!!!%%%%%%%%%%%%%%%%********        #%%%%%%%%%%%%%**********           ######***************             ###****************               *****************               *****************                ****************              $$$$$$$$$$$$******\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$$$((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$$$((\001\001\001\001\001\001\001\001\001\001\001222222211110000000000\001\001\001\001\001\001\001\001\001\0012222222111111000000000\001\001\001\001\001\001\001\001\001\0012222222111111000000000\001\001\001\001\001\001\001\001\001\0012222222111111100000000\002\002\002\002\002\002\002\002222222221111111100000000\002\002\002\002\002\002\002\002\00222222211111111110000000\002\002\002\002\002\002\002\002\002\002.........1111111100000\002\002\002\002\002\002\002\002\002.............111///////\002\002\002\002\002\002\002\002\002.............//////////\002\002\002\002\002\002\002\002..............//////////\036\036\036\036\036\036\036\036..............//////////\036\036\036\036\036\036\036\036&&&&&&&&&.....//////////\036\036\036\036\036\036\036\036&&&&&&&&&&&&-----///////\036\036\036\036\036\036\036&&&&&&&&&&&&&--------////\036\036\036\036\036\036\036&&&&&&&&&&&&&-----------/\036\036\036\036\036\036&&&&&&&&&&&&&&------------!!!!!!!&&&&&&&&&&&%%%%%-------,,!!!!!!!!!!%%%%%%%%%%%%%%%%-,,,,,!!!!!!!!!!%%%%%%%%%%%%%%%%,,,,,,!!!!!!!!!!%%%%%%%%%%%%%%%*******!!!!!!!!!!%%%%%%%%%%%%%%********!!!!!!!!!%%%%%%%%%%%%%%*********       #%%%%%%%%%%%%%%**********        #####%%%%%%%************           ######***************             ###****************               *****************               *****************               $$$**************            $$$$$$$$$$$$$$$$**((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$$(((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$$(((\001\001\001\001\001\001\001\001\001\0012222222111110000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001222222221111111100000000\002\002\002\002\002\002\002\002222222221111111100000000\002\002\002\002\002\002\002\002\00222222111111111110000000\002\002\002\002\002\002\002\002\002..........1111111100000\002\002\002\002\002\002\002\002\002............///////////\002\002\002\002\002\002\002\002\002............///////////\002\002\002\002\003\003\003\003.............///////////\036\036\036\036\036\036\036\036.............///////////\036\036\036\036\036\036\036\036&&&&&&&&&...-///////////\036\036\036\036\036\036\036\036&&&&&&&&&&&------///////\036\036\036\036\036\036\036&&&&&&&&&&&&---------////\036\036\036\036\036\036\036&&&&&&&&&&&&------------/\036\036\036\036\036\036&&&&&&&&&&&&&-------------!!!!!!!!&&&&&&&&&%%%%%-------,,,!!!!!!!!!!%%%%%%%%%%%%%%%,,,,,,,!!!!!!!!!!%%%%%%%%%%%%%%,,,,,,,,!!!!!!!!!!%%%%%%%%%%%%%%,,,,,,,,!!!!!!!!!!%%%%%%%%%%%%%*********!!!!!!!!!%%%%%%%%%%%%%**********       ###%%%%%%%%%%%***********        #########%**************           ######***************             ###****************               *****************               *****************             $$$$$$$$$$$********\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$(((((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$((((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$$$((((\001\001\001\001\001\001\001\001\001\0012222222111110000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001222222221111111100000000\002\002\002\002\002\002\002\002222222211111111100000000\002\002\002\002\002\002\002\002\00222222111111111110000000\002\002\002\002\002\002\002\002\002..........111111/////00\002\002\002\002\002\002\002\002\002...........////////////\002\002\002\002\002\002\002\003\003...........////////////\003\003\003\003\003\003\003\003\003...........////////////\036\036\036\036\036\036\003\003\003...........////////////\036\036\036\036\036\036\036\036&&&&&&&&&.---///////////\036\036\036\036\036\036\036\036&&&&&&&&&--------///////\036\036\036\036\036\036\036&&&&&&&&&&-----------////\036\036\036\036\036\036\036&&&&&&&&&&--------------/\036\036\036\036\036\036&&&&&&&&&&&---------------!!!!!!!!!!&&&&&&%%%%--------,,,,!!!!!!!!!!%%%%%%%%%%%%%%,,,,,,,,!!!!!!!!!!%%%%%%%%%%%%%,,,,,,,,,!!!!!!!!!!%%%%%%%%%%%%%,,,,,,,,,!!!!!!!!!#%%%%%%%%%%%%**********!!!!!!!###%%%%%%%%%%%***********       #######%%%%%%************        ##########**************           ######***************             ###****************               *****************               $****************           \"$$$$$$$$$$$$$$((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$((((((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$$((((((\001\001\001\001\001\001\001\001\00122222222111110000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001\00122222221111111000000000\001\001\001\001\001\001\001\001222222211111111100000000\002\002\002\002\002\002\002\002222222211111111100000000\002\002\002\002\002\002\002\002\0022222.111111111110000000\002\002\002\002\002\002\002\002\002..........11111///////0\002\002\002\002\002\002\002\002\003........../////////////\002\002\003\003\003\003\003\003\003........../////////////\003\003\003\003\003\003\003\003\003........../////////////\036\036\003\003\003\003\003\003\003\003........./////////////\036\036\036\036\036\036\036\003&&&&&&&&-----///////////\036\036\036\036\036\036\036\036&&&&&&&&---------///////\036\036\036\036\036\036\036&&&&&&&&&------------////\036\036\036\036\036\036\036&&&&&&&&&---------------/\036\036\036\036\036\036&&&&&&&&&&----------------!!!!!!!!!!&&&&&%%%----------,,,,!!!!!!!!!!%%%%%%%%%%%%,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%%%,,,,,,,,,,!!!!!!!!!##%%%%%%%%%%%,,,,,,,,,,!!!!!!!!###%%%%%%%%%%,,,,,,,,,,,!!!!!!######%%%%%%%%************       #########%%%*************        ##########**************           ######***************             ###****************               *****************            \"\"\"$$**************+\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$(((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$(((((((\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$$$(((((((\001\001\001\001\001\001\001\001\00122222222111110000000000\001\001\001\001\001\001\001\001222222221111111000000000\001\001\001\001\001\001\001\001222222221111111000000000\001\001\001\001\001\001\001\001222222211111111100000000\002\002\002\002\002\002\002\002222222211111111100000000\002\002\002\002\002\002\002\002\002222..111111111110000000\002\002\002\002\002\002\002\002\002..........111/////////0\002\002\002\002\002\002\002\003\003........../////////////\002\002\003\003\003\003\003\003\003........../////////////\003\003\003\003\003\003\003\003\003\003........./////////////\036\036\003\003\003\003\003\003\003\003........//////////////\036\036\036\036\036\036\036\003\003&&&&&&&-----///////////\036\036\036\036\036\036\036\036&&&&&&&&---------///////\036\036\036\036\036\036\036&&&&&&&&&------------////\036\036\036\036\036\036\036&&&&&&&&&---------------/\036\036\036\036\036\036&&&&&&&&&&---------------,!!!!!!!!!!&&&&&%-----------,,,,,!!!!!!!!!!%%%%%%%%%%%%,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%%,,,,,,,,,,,!!!!!!!!!##%%%%%%%%%%,,,,,,,,,,,!!!!!!!!#####%%%%%%%%,,,,,,,,,,,!!!!!!########%%%%%%************       ##########%%*************        #########''*************           #####''**************             #'''***************             \"\"'***************+         \"\"\"\"\"\"$$$$*****++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$(((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$$((((((((\001\001\001\001\001\001\001\001222222222111110000000000\001\001\001\001\001\001\001\001222222221111111000000000\001\001\001\001\001\001\0012222222211111111000000000\022\001\001\001\001\001\0012222222211111111100000000\002\002\002\002\002\002\002\002222222211111111100000000\002\002\002\002\002\002\002\002\00222...111111111110000000\002\002\002\002\002\002\002\002\002..........11//////////0\002\002\002\002\002\002\002\003\003........../////////////\002\003\003\003\003\003\003\003\003........../////////////\003\003\003\003\003\003\003\003\003\003........//////////////\036\036\003\003\003\003\003\003\003\003........//////////////\036\036\036\036\036\036\036\003\003&&&&&&&-----///////////\036\036\036\036\036\036\036\036&&&&&&&&---------///////\036\036\036\036\036\036\036&&&&&&&&&------------////\036\036\036\036\036\036\036&&&&&&&&&---------------/\036\036\036\036\036\036&&&&&&&&&&---------------,!!!!!!!!!!&&&&&%-----------,,,,,!!!!!!!!!!%%%%%%%%%%%,,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!###%%%%%%%%,,,,,,,,,,,,!!!!!!!######%%%%%%%,,,,,,,,,,,,!!!!!##########%%%%'',,,,,,,,,,,       #########''''************        ########''''***********+           ####''''************+             '''''************++            \"\"\"'************++++  \"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$*((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\001\001\001\001\001\001\0012222222221111110000000000\001\001\001\001\001\00122222222211111111000000000\001\001\001\001\001\00122222222211111111000000000\022\022\022\022\022\022\0222222222211111111100000000\022\022\022\022\022\022\022\022222222111111111100000000\022\022\022\022\022\022\022\022\002.....111111111110000000\022\022\022\022\022\022\022\022...........11/////////33\022\022\022\022\022\003\003\003\003........../////////////\003\003\003\003\003\003\003\003\003\003........//////////////\003\003\003\003\003\003\003\003\003\003........//////////////\036\003\003\003\003\003\003\003\003\003........//////////////\036\036\036\036\036\036\003\003\003&&&&&&------///////////\036\036\036\036\036\036\036\036&&&&&&&----------///////\036\036\036\036\036\036\036&&&&&&&&-------------////\036\036\036\036\036\036\036&&&&&&&&----------------/\036\036\036\036\036\036&&&&&&&&&----------------,!!!!!!!!!!&&&&%%-----------,,,,,!!!!!!!!!!%%%%%%%%%%%,,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!###%%%%%%%%,,,,,,,,,,,,!!!!!!!######%%%%%%'',,,,,,,,,,,     ##########%%''''',,,,,,,,,,       #########''''''*********+        #######''''''*********++           ###''''''*********+++             '''''**********++++           \"\"\"\"'********++++++++ \"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$$(((((((((\001\001\001\001\001222222222211111110000000000\001\001\001\001\001222222222111111111000000000\022\022\022\022\022\02222222222111111111000000000\022\022\022\022\022\022\022\022222222111111111100000000\022\022\022\022\022\022\022\022\02222222111111111100000000\022\022\022\022\022\022\022\022\022\022....111111111113333333\022\022\022\022\022\022\022\022\022..........///////333333\022\022\022\022\022\022\022\003\003\003........///////////333\022\003\003\003\003\003\003\003\003\003......../////////////3\003\003\003\003\003\003\003\003\003\003........//////////////\003\003\003\003\003\003\003\003\003\003\003.......//////////////\036\036\003\003\003\003\003\003\003\003&&&&&------///////////\036\036\036\036\036\036\036\036&&&&&&&----------///////\036\036\036\036\036\036\036&&&&&&&&-------------////\036\036\036\036\036\036\036&&&&&&&&----------------/\036!!!!!&&&&&&&&&----------------,!!!!!!!!!!!&%%%%-----------,,,,,!!!!!!!!!!%%%%%%%%%%%,,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!####%%%%%%%%,,,,,,,,,,,,!!!!!!!#######%%%'''',,,,,,,,,,,     ##########''''''',,,,,,,,,,       ########'''''''*********+        #######'''''''*******+++           ###''''''*******+++++             '''''********++++++           \"\"\"\"'********++++++++ \"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$((((((((((\001\001\001\0012222288888888111110000000000\022\022\001\0012222888888888111111000000000\022\022\022\022\022\022\022\022888888888111111000000000\022\022\022\022\022\022\022\022\02288888888111111333333333\022\022\022\022\022\022\022\022\02288888888111111333333333\022\022\022\022\022\022\022\022\022\0228888888111111333333333\022\022\022\022\022\022\022\022\022........./////333333333\022\022\022\022\022\022\022\022\003\003\003......//////////33333\022\022\003\003\003\003\003\003\003\003\003....../////////////33\003\003\003\003\003\003\003\003\003\003\003......///////////////\003\003\003\003\003\003\003\003\003\003\003.....-///////////////\003\003\003\003\003\003\003\003\003\003\003&&--------///////////\036\036\036\036\036\036\036\031&&&&&------------///////\036\036\036\036\031\031\031\031&&&&&---------------////\036\031\031\031\031\031\031\031&&&&&------------------/\031\031\031\031\031\031\031\031&&&&&------------------,!!!!!!!!!!!%%%%------------,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!!!%%%%%%%%%%,,,,,,,,,,,,!!!!!!!!######%%%%%,,,,,,,,,,,,,!!!!!!!########'''''',,,,,,,,,,,     ##########''''''',,,,,,,,,,       ########'''''''********++        #######'''''''******++++           ###'''''''*****++++++             '''''*******+++++++           \"\"\"\"'*******+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\"\"\037$$$$$$$$$$((((((((((\001\001\00122888888888888888880000000000\022\022\022\022\022888888888888888881000000000\022\022\022\022\022\022\0228888888888888883333333333\022\022\022\022\022\022\022\022888888888888833333333333\022\022\022\022\022\022\022\022\02288888888888833333333333\022\022\022\022\022\022\022\022\02288888888888833333333333\022\022\022\022\022\022\022\022\022\022888888888///3333333333\022\022\022\022\022\022\022\022\022\003\003\003....////////33333333\022\022\022\022\022\022\022\022\003\003\003\003..../////////////333\003\003\003\003\003\003\003\003\003\003\003\003....////////////////\003\003\003\003\003\003\003\003\003\003\003\003.----///////////////\031\031\003\003\003\003\003\003\003\003\003\003---------///////////\031\031\031\031\031\031\031\031\031\031\031\031-------------///////\031\031\031\031\031\031\031\031\031\031\031\031----------------////\031\031\031\031\031\031\031\031\031\031\031\031\005\005-----------------/\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005--------------,\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005---,,,,,!!!!!!!!!!%%\005\005\005\005\005\005\005\005\005\005\005,,,,,,,,,!!!!!!!!!##%%\005\005\005\005\005\005\005,,,,,,,,,,,,!!!!!!!#########''',,,,,,,,,,,,,!!!!!!#########''''''',,,,,,,,,,     #########'''''''',,,,,,,,,,       #######''''''''',,,,,,,++        ######''''''''''**++++++           ##'''''''''***+++++++            '''''''*****++++++++          \"\"\"\"\"''******+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\"\037$$$$$$$$$$((((((((((\"\"\"\"\"\"\"\"\"\037\037\037$$$$$$$$$$((((((((((\"\"\"\"\037\037\037\037\037\037\037\037$$$$$$$$$$((((((((((\f\f\f\f8888888888888888888800000000\f\f\f\f\f888888888888888888333333333\022\022\022\022\022\022\f8888888888888883333333333\022\022\022\022\022\022\022\022888888888888883333333333\022\022\022\022\022\022\022\022888888888888833333333333\022\022\022\022\022\022\022\022\02288888888888833333333333\022\022\022\022\022\022\022\022\022\f8888888888833333333333\022\022\022\022\022\022\022\022\022\022\f\f8888//////3333333333\022\022\022\022\022\022\022\022\022\022\003\003>>>>//////////333333\022\022\022\022\022\022\022\022\003\003>>>>>>///////////////3\003\003\003\003\003\003\003\003\003\003>>>>---///////////////\031\031\031\031\031\031\031\031\031\031\031>>--------///////////\031\031\031\031\031\031\031\031\031\031\031\031>------------///////\031\031\031\031\031\031\031\031\031\031\031\031\005\005--------------////\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005--------/\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005------,\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,,,!!!!!!!####\005\005\005\005\005\005\005\005\005\005\005,,,,,,,,,,!!!!!########\005\005\005\005\005\005\005,,,,,,,,,,,,!!!!!########'''''''''',,,,,,,,,     ########'''''''''',,,,,,,,,       ######''''''''''',,,,,+++        #####'''''''''''++++++++           #''''''''''''++++++++            '''''''''**+++++++++   \"\"\"\"\"\"\"\"\"\"\"\"''*****++++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\"\"\"\"\"\"\"\037\037$$$$$$$$$$((((((((((\"\"\"\"\"\037\037\037\037\037\037\037$$$$$$$$$$((((((((((\"\"\"\037\037\037\037\037\037\037\037\037$$$$$$$$$$((((((((((\f\f\f\f8888888888888888888883333333\f\f\f\f\f\f88888888888888888333333333\f\f\f\f\f\f\f\f888888888888883333333333\f\f\f\f\f\f\f\f\f88888888888883333333333\022\022\f\f\f\f\f\f\f88888888888833333333333\022\022\f\f\f\f\f\f\f88888888888833333333333\022\022\f\f\f\f\f\f\f\f8888888888833333333333\022\022\022\022\022\f\f\f\f\f\f\f8888888/333333333333\022\022\022\022\022\022\022\022\f\f>>>>>>>>>///3333333333\022\022\022\022\022\022\022>>>>>>>>>>>>>//5555555555\031\031\031\031\031\031>>>>>>>>>>>>>>//5555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>>/5555555555\031\031\031\031\031\031\031\031\031\031\031>>>>>>>>>-----5555555\031\031\031\031\031\031\031\031\031\031\031>>>>>>\005\005\005\005\005\005\005\005---5555\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005---44\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005--44\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,,\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,,,\032\032\032\032\032\032\032\032\032#\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,,,,\032\032\032\032\032\032\032\032\032###'''''''''''',,,,,,,,\032\032\032\032\032\032\032\032\032###'''''''''''',,,,,,,,\032\032\032\032\032\032\032\032\032###''''''''''''',++++++   \032\032\032\032\032\032###''''''''''''++++++++           '''''''''''''++++++++           \"''''''''''++++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"''''**+++++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$(((+++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((((((((((\"\"\"\037\037\037\037\037\037\037\037\037\037$$$$$$$$$((((((((((\"\"\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$((((((((((\"\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$$$((((((((((\f\f\f\f8888888888888888888883333333\f\f\f\f\f\f88888888888888888333333333\f\f\f\f\f\f\f\f888888888888883333333333\f\f\f\f\f\f\f\f\f88888888888883333333333\f\f\f\f\f\f\f\f\f88888888888833333333333\f\f\f\f\f\f\f\f\f\f8888888888833333333333\f\f\f\f\f\f\f\f\f\f\f888888888833333333333\f\f\f\f\f\f\f\f\f\f\f\f\f8888885553333333333\f\f\f\f\f\f\f\f\f>>>>>>>>>>>555555555553\022\022\022\022\022\022>>>>>>>>>>>>>>555555555555\031\031\031\031\031>>>>>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031>>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031\031>>>>>>>>>\005\005\005\005\005\005\005555555\031\031\031\031\031\031\031\031\031\031\031>\005\005\005\005\005\005\005\005\005\005\005\005\005\005\00544444\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\00544444\031\031\031\031\031\031\031\031\031\031\031\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005\00544444\031\031\031\031\031\031\031\031\031\031\004\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,\032\032\032\032\032\032\032\032\032\004\004\005\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,\032\032\032\032\032\032\032\032\032\032\032\005\005\005\005\005\005\005\005\005\005\005\005\005\005,,,,,,,\032\032\032\032\032\032\032\032\032\032\032\032''''''''''''',,,,,,,\032\032\032\032\032\032\032\032\032\032\032\032'''''''''''''',,,,,+\032\032\032\032\032\032\032\032\032\032\032\032'''''''''''''+++++++\032\032\032\032\032\032\032\032\032\032\032\032''''''''''''++++++++\032\032\032\032\032\032\032\032\032\032\032\032'''''''''''+++++++++\034\034\034\034\034\034\034\034\034\034\034\"''''''''''++++++++++\034\"\"\"\"\"\"\"\"\"\"\"\"\"\"'''''++++++++++++\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"$$$$((((+++++++++\"\"\"\037\037\037\037\037\037\037\037\037\037\037$$$$$$((((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$(((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$(((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$$$(((((((((((\f\f\f\f8888888888888888888883333333\f\f\f\f\f\f88888888888888888333333333\f\f\f\f\f\f\f\f888888888888883333333333\f\f\f\f\f\f\f\f\f88888888888883333333333\f\f\f\f\f\f\f\f\f\f8888888888883333333333\f\f\f\f\f\f\f\f\f\f8888888888833333333333\f\f\f\f\f\f\f\f\f\f\f888888888833333333333\f\f\f\f\f\f\f\f\f\f\f\f\f8888777555533333333\f\f\f\f\f\f\f\f\f>>>>>>>>>>>555555555555\f\f\f\f\f>>>>>>>>>>>>>>>555555555555\031\031\031\031\031>>>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031\031>>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031\031>>>>>>>>>>\005\005\005\005\0054444455\031\031\031\031\031\031\031\031\031\031\031\004\004\004\005\005\005\005\005\005\005\005\005\005\0054444444\031\031\031\031\031\031\031\031\031\031\004\004\004\005\005\005\005\005\005\005\005\005\005\005\005\005444444\031\031\031\031\031\031\031\031\031\031\004\004\004\005\005\005\005\005\005\005\005\005\005\005\005\005444444\031\031\031\031\031\031\031\031\031\004\004\004\004\005\005\005\005\005\005\005\005\005\005\005\005\005444444\032\032\032\032\032\032\032\032\032\004\004\004\004\005\005\005\005\005\005\005\005\005\005\005\005\005444444\032\032\032\032\032\032\032\032\032\032\032\004\005\005\005\005\005\005\005\005\005\005\005\005\005\005444444\032\032\032\032\032\032\032\032\032\032\032\032\032\005\005\005\005\005\005\006\006\006\006\006\006\006\006,,,,,\032\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006+++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032'\006\006\006\006\006\006\006\006\006++++++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032'''''''''+++++++++\032\032\032\032\032\032\032\032\032\032\032\032\032'''''''''++++++++++\034\034\034\034\034\034\034\034\034\034\034\034\034''''''''+++++++++++\034\034\034\034\034\034\034\034\034\034\034\034\"\"\"'''''++++++++++++\034\034\034\034\034\034\034\034\034\034\034\"\"\037\037$$$$((((+++++++++\034\034\034\037\037\037\037\037\037\037\037\037\037\037\037$$$$$((((((((((((\034\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$((((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$((((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$$$$((((((((((((\f\f\f\f8888888888888888888883333333\f\f\f\f\f\f88888888888888888333333333\f\f\f\f\f\f\f\f888888888888887333333333\f\f\f\f\f\f\f\f\f88888888888877333333333\f\f\f\f\f\f\f\f\f\f8888888888777333333333\f\f\f\f\f\f\f\f\f\f8888888888777333333333\f\f\f\f\f\f\f\f\f\f\f888888887777333333333\f\f\f\f\f\f\f\f\f\f\f\f\f;;;7777755555555555\f\f\f\f\f\f\f\f\f>>>>>>>>>>>555555555555\f\f\f\f\f>>>>>>>>>>>>>>>555555555555\031\031\031\031\031>>>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>\005\005\005444444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\005\005\005\005\005\00544444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\005\005\005\005\005\005\005\00544444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\005\005\005\005\005\005\005\005\00544444444\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\005\005\005\005\005\005\005\005\00544444444\032\032\032\032\032\032\032\032\032\004\004\004\004\004\004\005\005\005\005\005\005\005\005\00544444444\032\032\032\032\032\032\032\032\032\032\032\004\004\005\005\005\005\005\005\005\005\005\005\006\0064444444\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\0064444\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006+++\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006+++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006+++++++\032\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006)+++++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034'''''))))))+++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034')))))))))++++++\034\034\034\034\034\034\034\034\034\034\034\034\034\037\037\037\037))))))))))+++++\034\034\034\034\034\034\034\034\034\034\037\037\037\037\037\037\037$$(((((((((((((\034\034\034\034\037\037\037\037\037\037\037\037\037\037\037\037\037$$(((((((((((((\034\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$(((((((((((((\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037$$(((((((((((((\f\f\f\f8888888888888888888887773333\f\f\f\f\f\f\f8888888888888888777773333\f\f\f\f\f\f\f\f\f88888888888777777773333\f\f\f\f\f\f\f\f\f\f8888888887777777773333\f\f\f\f\f\f\f\f\f\f8888888877777777773333\f\f\f\f\f\f\f\f\f\f8888888877777777773333\f\f\f\f\f\f\f\f\f\f\f;;;;;;777777777755553\f\f\f\f\f\f\f\f\f\f\f;;;;;;777777555555555\f\f\f\f\f\f\f\f\f>>>>>>>>>>7555555555555\f\f\f\f\f>>>>>>>>>>>>>>>555555555555\020\020\020\020\020\020>>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031>>>>>>>>>>>>>555555555555\031\031\031\031\031\031\031\031>>>>>>>>>>>>>55555555555\031\031\031\031\031\031\031\031\031>>>>>>>>>>>\00444444444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\004\004\005\0054444444444\031\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\004\004\005\0054444444444\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\004\004\005\005\0054444444444\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004\005\005\0054444444444\032\032\032\032\032\032\032\032\032\004\004\004\004\004\004\004\004\004\004\005\005\0054444444444\032\032\032\032\032\032\032\032\032\032\032\004\004\004\004\006\006\006\006\006\006\006\006\006\0064444444\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\0064444\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\034\034\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\006\007\007\007)))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\007\007\007)))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\037\037\007\007))))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\037\037\037\037\037)))))))))))))))\034\034\034\034\034\034\034\034\034\034\037\037\037\037\037\037\037)))))))))))))))\034\034\034\034\034\034\034\034\037\037\037\037\037\037\037\037\037\037))))))))))))))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037))))))))))))))\f\f\f\f8888888888888888888877777733\f\f\f\f\f\f\f8888888888888887777777733\f\f\f\f\f\f\f\f\f88888888877777777777733\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777777777733\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777777777733\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777777777733\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777777775555\f\f\f\f\f\f\f\f\f\f;;;;;;;;77777755555555\f\f\f\f\f\f\f\f\f>>>>>>>>>77755555555555\020\020\020\020\020\020>>>>>>>>>>>>>>555555555555\020\020\020\020\020\020\020>>>>>>>>>>>>>555555555555\020\020\020\020\020\020\020>>>>>>>>>>>>>555555555555\031\020\020\020\020\020\020>>>>>>>>>>>>>>55555555555\031\031\031\031\031\020\020\020>>>>>>>>>>>>\00444444444444\031\031\031\031\031\031\031\031\031\004\004\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004\00444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\006\006\0064444444444\032\032\032\032\032\032\032\032\032\032\032\004\004\004\006\006\006\006\006\006\006\006\006\006\0064444444\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\0064444\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\007\007\034\034\034\034\032\032\032\032\032\032\032\032\032\032\006\006\006\006\007\007\007\007\007\007))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\007\007\007\007\007)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\007\007\007\007))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\007\007)))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\037\037\035\035\035))))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\037\037\037\035\035))))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\037\037\037\037\035\035))))))))))))))\034\034\034\034\037\037\037\037\037\037\037\037\037\037\037\037\035\035\035\035))))))))))))\f\f\f\f\f888888888888888888877777777\f\f\f\f\f\f\f\f888888888888777777777777\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777777775\f\f\f\f\f\f\f\f\f;;;;;;;;;77777777775555\f\f\f\f\f\f\f\f\f;;;;;;;;;77777775555555\f\f\f\f\f\f\f\f>>>>>>>>>>77755555555555\020\020\020\020\020\020\020>>>>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020>>>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020>>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020>>>>>>>>>>>>55555555555\020\020\020\020\020\020\020\020\020>>>>>>>>>>>444444444444\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\006\006\006\0064444444444\032\032\032\032\032\032\032\032\032\032\032\004\004\004\006\006\006\006\006\006\006\006\006\006\0064444444\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\006\0064444\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\032\032\032\032\032\032\032\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\007)\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\007\007\007\007\007\007)))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\007\007\007\007\007))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\007\007))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035)))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035))))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035)))))))))\f\f\f\f======8888888888887777777777\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777777\f\f\f\f\f\f\f\f;;;;;;;;;;77777777777775\f\f\f\f\f\f\f\f;;;;;;;;;;77777777775555\f\f\f\f\f\f\f\f;;;;;;;;;;77777777555555\016\016\016\016\016\016\016\016\016>;;;;;;;;77755555555555\020\020\020\020\020\020\020\020>>>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>55555555555\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>\004444444444444\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\006\006\006\006\0064444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\006\006\006\006\006\006\006\006\006\006\0064444444\032\032\032\032\032\032\032\032\032\032\032\032\030\006\006\006\006\006\006\006\006\006\006\006\006\006\006\0064444\032\032\032\032\032\032\032\032\032\032\032\032\030\006\006\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\032\030\030\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\006\006\006\006\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\032\032\032\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007))))\034\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\007\007\007\007\007)))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\007)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035)))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\035)))))))\f\f====================7777777777\f\f\f\f=============;;7777777777777\f\f\f\f\f\f=;;;;;;;;;;;;7777777777777\f\f\f\f\f\f;;;;;;;;;;;;;7777777777777\f\f\f\f\f\f;;;;;;;;;;;;;7777777777777\016\016\016\016\016\016\016;;;;;;;;;;;;7777777777775\016\016\016\016\016\016\016\016\016;;;;;;;;;;7777777777555\016\016\016\016\016\016\016\016\016;;;;;;;;;;7777777755555\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;7775555555555\016\016\016\016\016\016\016\016\016\016\016>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>55555555555\020\020\020\020\020\020\020\020\020\020\020\020>>>>>\004\004\004444444444444\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\004\004\004444444444444\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\006\006\006\006\006\006\0064444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\006\006\006\006\006\006\006\006\006\006\006\0064444444\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\006\006\006\006\006\006\006\006\006\006\006\00666666\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\006\006\006\006\006\006\006\006\006\006\006\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\006\006\006\006\006\006\006\007\007\007\007\007\007\007\007\007\032\032\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\006\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007)))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\007\007\007\007\007))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\033\035\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\034\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\035))))))=======================777777777\f\f==================777777777777\f\f\f========;;;;;;;;7777777777777\f\f\f=====;;;;;;;;;;;7777777777779\016\016\016\016===;;;;;;;;;;;;7777777777799\016\016\016\016\016\016\016;;;;;;;;;;;;7777777777799\016\016\016\016\016\016\016\016\016;;;;;;;;;;7777777777755\016\016\016\016\016\016\016\016\016\016;;;;;;;;;7777777777555\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;7777777555555\016\016\016\016\016\016\016\016\016\016\016\016\016>>>>>>7555555555555\016\016\016\016\016\016\016\016\016\016\016\016\016>>>>>>>555555555555\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>???555555555555\020\020\020\020\020\020\020\020\020\020\020\020\020>???????55555555555\020\020\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\0044444444444444\020\020\020\020\020\020\020\020\020\020\020\020\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\0044444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\004\004\004\004\006\006444444444444\026\026\026\026\026\026\026\026\026\026\026\026\004\004\006\006\006\006\006\006\006\0064444444444\026\026\026\026\026\026\026\026\026\026\026\026\030\030\006\006\006\006\006\006\006\006\006\006\0064444466\026\026\026\026\026\026\026\026\026\030\030\030\030\030\030\030\006\006\006\006\006\006\006\006\006\006666666\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\006\006\006\006\006\006\006\006\006666666\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\006\006\006\006\006\007\007\007\007\007\007\007\0076\032\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007)))\034\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\007\007\007\007))))))))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035))))))))))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\034\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b=======================777777777=====================77777777777\013=================;7777777777779\013\013=========;;;;;;;;7777777777999\016\016\016=======;;;;;;;;;7777777779999\016\016\016\016\016\016\016=;;;;;;;;;;;7777777779999\016\016\016\016\016\016\016\016\016;;;;;;;;;;7777777779999\016\016\016\016\016\016\016\016\016\016;;;;;;;;;7777777799999\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;7777799999999\016\016\016\016\016\016\016\016\016\016\016\016\016\016;;;;;7799999999999\016\016\016\016\016\016\016\016\016\016\016\016\016?????????5555555555\020\020\020\020\020\020\020\020\020\020\020\020???????????555555555\020\020\020\020\020\020\020\020\020\020\020\020???????????444444445\020\020\020\020\020\020\020\020\020\020\020\020?????????44444444444\020\020\020\020\020\020\020\020\020\020\020\020????????444444444444\026\026\026\026\026\026\026\026\026\026\026\026???????4444444444444\026\026\026\026\026\026\026\026\026\026\026\026\023\023\023\023\023?44444444444444\026\026\026\026\026\026\026\026\026\026\026\023\023\023\023\023\023\006\006\006444444444444\026\026\026\026\026\026\026\026\026\026\026\023\023\023\023\006\006\006\006\006\006\0064444444446\026\026\026\026\026\026\026\026\026\026\023\023\023\023\023\006\006\006\006\006\006\006\006\00666666666\026\026\026\026\026\026\026\026\023\030\030\030\030\030\030\030\030\030\006\006\006\006\006\00666666666\032\032\032\032\032\023\023\023\030\030\030\030\030\030\030\030\030\030\030\006\006\006\006\00666666666\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\006\007\007\007\007\007\007\007\007666\032\032\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\007\007\007\007\007)))))))\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035)))))))))\034\034\034\034\034\034\034\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\b\b\b))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b========================77777777=====================77777777777\013\013==================777777777999\013\013\013\013=============;;;777777999999\013\013\013\013============;;;;777999999999\016\016\016\016\016\016=====;;;;;;;;;779999999999\016\016\016\016\016\016\016\016\016\016;;;;;;;;;;779999999999\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;;779999999999\016\016\016\016\016\016\016\016\016\016\016\016;;;;;;;;799999999999\016\016\016\016\016\016\016\016\016\016\016\016\016\016;;;???999999999999\016\016\016\016\016\016\016\016\016\016\016\016???????????999999999\020\020\020\020\020\020\020\020\020\020\020?????????????::::::::\020\020\020\020\020\020\020\020\020\020\020????????????:::::::::\020\020\020\020\020\020\020\020\020\020\020????????????444444444\020\020\020\020\020\020\020\020\020\020\020???????????4444444444\026\026\026\026\026\026\026\026\026\026\026??????????44444444444\026\026\026\026\026\026\026\026\026\026\023\023\023\023\023\023\023???444444444444\026\026\026\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\006444444444444\026\026\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\006\006\006\0064446666666\026\026\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\006\006\006\006\006666666666\026\026\026\026\026\026\023\023\023\023\023\030\030\030\030\030\030\030\030\030\006\0066666666666\026\026\026\026\023\023\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\0066666666666\032\032\032\032\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\0076666666\032\032\032\032\032\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007666\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007)\034\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\007\007\007\007\007\007\007\007\007)))\034\034\034\034\034\034\034\034\034\034\035\035\035\035\035\035\035\035\035\035\035\035\035)))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035))))))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b========================77777777\013=====================<<77777799\013\013\013=================<<<<99999999\013\013\013\013\013==============<<<9999999999\013\013\013\013\013\013============<<<99999999999\013\013\013\013\013\013\013===========<<<99999999999\016\016\016\016\016\016\016\016\016\016\016;;;;;<<<<<99999999999\016\016\016\016\016\016\016\016\016\016\016\016;;;;<<<<<99999999999\016\016\016\016\016\016\016\016\016\016\016\016\016;;;<<<<999999999999\016\016\016\016\016\016\016\016\016\016\016\016\016\016??????999999999999\016\016\016\016\016\016\016\016\016\016\016\016???????????999999999\017\020\020\020\020\020\020\020\020\020?????????????:::::::::\017\020\020\020\020\020\020\020\020\020????????????::::::::::\017\020\020\020\020\020\020\020\020\020????????????::::::::::\020\020\020\020\020\020\020\020\020\020????????????::::::::::\020\020\020\021\021\021\021\021\021\021????????????::::::::::\026\026\026\026\026\021\021\023\023\023\023\023\023\023\023\023\023????:::::::::::\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r::::::::\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r666666666\026\026\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r6666666666\026\026\026\026\023\023\023\023\023\023\023\023\023\023\023\030\030\030\030\030\r\r6666666666\026\026\026\023\023\023\023\023\023\023\030\030\030\030\030\030\030\030\030\030\030\r6666666666\026\023\023\023\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\00766666666\034\034\034\023\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\0076666666\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\0076\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\007\034\034\034\034\034\034\034\034\030\030\030\035\035\035\035\035\035\035\035\035\007\007\007\007\007\007\007\007\007\007\007\007\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\035\007\007\007\007))))\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b========================<<<77779\013====================<<<<<<99999\013\013\013\013===============<<<<<<9999999\013\013\013\013\013\013============<<<<<999999999\013\013\013\013\013\013\013\013=========<<<<<9999999999\013\013\013\013\013\013\013\013\013======<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016<<<<<<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016\016<<<<<<<<99999999999\017\016\016\016\016\016\016\016\016\016\016\016\016\016??????999999999999\017\017\017\016\016\016\016\016\016\016\016\016???????????999999999\017\017\017\017\017\016\016\016\016\016????????????::::::::::\017\017\017\017\017\017\020\020\020?????????????::::::::::\017\017\017\017\017\017\020\020\020????????????:::::::::::\017\021\021\021\021\021\021\021\021\021???????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\023\023\023\023\023\023???\r\r:::::::::::\021\021\021\021\021\021\021\021\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r66666666\021\021\021\021\021\021\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r666666666\026\026\026\023\023\023\023\023\023\023\023\023\023\023\023\023\030\030\r\r\r\r\r666666666\026\026\023\023\023\023\023\023\023\023\023\030\030\030\030\030\030\030\030\r\r\r\r666666666\024\023\023\023\023\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\030\0076666666666\024\024\024\024\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007666666666\034\034\024\024\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007666\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\007\007\007\007\007\007\007\007\007\007\007\007\034\034\033\033\027\027\027\027\027\030\030\035\035\035\035\035\035\035\035\035\035\035\007\007\007\007\007\007\007\007\007\007\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b========================<<<<9999\013====================<<<<<<99999\013\013\013\013===============<<<<<<9999999\013\013\013\013\013\013===========<<<<<<999999999\013\013\013\013\013\013\013\013=======<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\016\016\016\016\016\016\016\016\016\016\016\016<<<<<<<<<99999999999\017\017\017\017\016\016\016\016\016\016\016\016\016\016????<<999999999999\017\017\017\017\017\016\016\016\016\016\016????????????:99999999\017\017\017\017\017\017\016\016\016\016????????????::::::::::\017\017\017\017\017\017\017\017?????????????:::::::::::\017\017\017\017\017\017\021\021\021????????????:::::::::::\017\021\021\021\021\021\021\021\021\021???????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\023\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r6666666\021\021\021\021\021\021\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r66666666\021\021\021\023\023\023\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r666666666\024\024\024\023\023\023\023\023\023\023\023\023\023\023\030\030\r\r\r\r\r\r\r666666666\024\024\024\024\024\023\023\023\030\030\030\030\030\030\030\030\030\030\030\030\r\r6666666666\024\024\024\024\024\024\024\030\030\030\030\030\030\030\030\030\030\030\030\030\025\0256666666666\024\024\024\024\024\024\030\030\030\030\030\030\030\030\030\030\030\030\030\025\025\025\025\02566666666\024\024\024\027\027\027\030\030\030\030\030\030\030\030\030\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\033\033\033\027\027\027\027\027\027\027\027\027\027\035\035\035\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\033\033\033\033\033\033\033\027\027\027\027\027\035\035\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b========================<<<<<999\013====================<<<<<<99999\013\013\013\013==============<<<<<<<9999999\013\013\013\013\013\013\013=========<<<<<<<<99999999\013\013\013\013\013\013\013\013\013=====<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<99999999999\017\017\017\017\017\017\016\016\016\016\016\016\016????<<<999999999999\017\017\017\017\017\017\016\016\016\016\016????????????:::999999\017\017\017\017\017\017\017\016\016?????????????::::::::::\017\017\017\017\017\017\017\017?????????????:::::::::::\017\017\017\017\017\017\021\021\021????????????:::::::::::\017\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\023\023\023\023\023\r\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r\r6666666\021\021\021\021\021\021\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\021\021\024\024\024\023\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\023\023\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r666666666\024\024\024\024\024\024\024\024\024\024\024\024\030\030\030\030\r\r\r\r\r\r6666666666\024\024\024\024\024\024\024\024\024\024\030\030\030\030\030\025\025\025\025\025\025\025\025666666666\024\024\024\024\024\024\024\024\030\030\030\030\030\030\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\024\024\024\024\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\027\027\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\b\b=======================<<<<<<999\013\013===================<<<<<<99999\013\013\013\013==============<<<<<<<9999999\013\013\013\013\013\013\013=======<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013===<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\017\017\017\017\017\017\013\013\013\013\013\013\013??<<<<<999999999999\017\017\017\017\017\017\017\017\016??????????????:::::::99\017\017\017\017\017\017\017\017??????????????::::::::::\017\017\017\017\017\017\017\017?????????????:::::::::::\017\017\017\017\017\017\021\021\021????????????:::::::::::\017\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\023\023\r\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\021\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r\r6666666\021\021\021\021\021\024\024\024\023\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\021\024\024\024\024\024\024\024\024\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\023\r\r\r\r\r\r\r\r\r\r66666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r666666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\02566666666\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\024\024\024\024\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\b\b\b\b\b\b\b\b\t\t\t\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\b\b\b\b\b\b\n\n\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\b\b\b\b\b\b\n\n\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\b\b\b\b\b\b\b\b\b\b\b\n\n=======================<<<<<<999\013\013==================<<<<<<<99999\013\013\013\013\013==========<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013===<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013=<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<9999999999\017\017\017\017\017\017\013\013\013\013\013\013\013<<<<<<<999999999999\017\017\017\017\017\017\017\017\017??????????????:::::::::\017\017\017\017\017\017\017\017?????????????:::::::::::\017\017\017\017\017\017\017\017?????????????:::::::::::\017\017\017\017\017\017\021\021\021\021???????????:::::::::::\017\021\021\021\021\021\021\021\021\021\021??????????:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021????????::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\023\r\r\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\023\023\r\r\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\021\021\021\023\023\r\r\r\r\r\r\r\r\r\r\r\r\r666666\021\021\021\021\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r6666666\021\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r6666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\025\02566666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\024\024\024\024\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\b\b\b\t\t\t\t\t\t\t\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\b\n\n\n\n\n\n\n\n\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\n\n\n\n\n\n\n\n\n\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b\n\n\n\n\n\n\n\n\n======================<<<<<<<999\013\013\013===============<<<<<<<<<99999\013\013\013\013\013\013\013====<<<<<<<<<<<<<<<999999\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<999999999\017\017\017\017\017\017\013\013\013\013\013\013\013<<<<<<<<99999999999\017\017\017\017\017\017\017\017\017?????????????::::::::::\017\017\017\017\017\017\017\017\017????????????:::::::::::\017\017\017\017\017\017\017\017????????????::::::::::::\017\017\017\017\017\017\021\021\021\021\021?????????::::::::::::\017\021\021\021\021\021\021\021\021\021\021\021????????::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021???????::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\021\021\021\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r\r::::6\021\021\021\021\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r666666\021\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\025\025\025\025666666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\024\024\024\024\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\n\t\t\t\t\t\t\t\t\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n\n\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n\n\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\b\n\n\n\n\n\n\n\n\n\n\n=====================<<<<<<<<999\013\013\013\013========<<<<<<<<<<<<<<<<9999\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<<<<<99999\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<<<999999\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<<9999999\013\013\013\013\013\013\013\013\013\013\013\013<<<<<<<<<<<<99999999\017\017\017\017\017\017\013\013\013\013\013\013\013<<<<<<<<<<999999999\017\017\017\017\017\017\017\017\017\013\013??????????:::::::::::\017\017\017\017\017\017\017\017\017???????????::::::::::::\017\017\017\017\017\017\017\017\017??????????:::::::::::::\017\017\017\017\017\021\021\021\021\021\021\021\021??????:::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\021?????:::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\021?????:::::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r:::::::::::\021\021\021\021\021\021\021\021\021\021\021\021\021\r\r\r\r\r\r\r\r\r\r\r::::::::\021\021\021\021\021\021\021\021\021\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r:::::\021\021\021\021\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r6666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r6666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\r\r\r\r\r\r\r\r\r\r\r\r\r6666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\024\024\024\024\024\024\024\024\024\024\024\024\024\025\025\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\024\024\024\024\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\t\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\025\025\025\025\n\t\t\t\t\t\t\t\t\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n\n\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n\n\033\033\033\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\n\n\n\n\n\n\n\n\n\n\n\n".getBytes(StandardCharsets.ISO_8859_1)
            ;
    public static final byte[] ENCODED_MANOSSUS =
            "\001\001\001\001\001\001\001\001\001\001\001\00122222221110000000000\001\001\001\001\001\001\001\001\001\001\001222222211111000000000@@@B\001\001\001\001\001®®®­­­­­­11111000000000@BBBBBB®®®­­­­­­­­¸¸¹¹¹¹¹¹¹00000@BBBBBB®®­­­­­­­­¸¸¹¹¹¹¹¹¶¶¶¶¶¶¶@BBBBBB®­­­­­­­­­¸¹¹¹¹¶¶¶¶¶¶¶¶¶¶BBB®­­­­­­­­¸¸¹¹¹¹¶¶¶¶¶¶¶¶¶¶¡¡¡¡¡­­­­­¸¸¹¹¹¹¶¶¶¶¶¶¶¶¶¶¡¡¡¡¡¡¡¡¡¡¸¸¸¹¹¹¶¶¶¶¶¶¶¶¶¶¶¡¡¡¡¡¡¡¡¡¡¡¸¹¹°°°¶¶¶¶¶¶¶µµµ¡¡¡¡¡¡¡¡¡¡¡¡¡°°°°°°°°°µµµµµµ¡¡¡¡¡¡¡¡¡¡¡¡¡¡°°°°°°°°°°µµµµµµ\036\036\036¡¡¡¡¡¡¡¡¡¡¡¡¡°°°°°°°°°°µµµµµµ°°°°°°°°°°µµµµµµ°°°°°¬¬µµµµµµ¢¢¬¬¬¬¬¬¬¬¬¬¢¢¢©©©©©©©©©©¢¢©©©©©©©©©©©¢¢©©©©©©©©©©©¢¢¢©©©©©©©©©©©¢¢¢¢¢¢©©©©©©©©©©©©¢¢¢¢¢¢¢¢¢¢©©©©©©©©©©§§©©©©§§§§§§§     §§§§§§§         §§§§              §§§           §§            ¤           ¤¤         ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\001\001\001\001\001\001\001\001\001\001\001222222211110000000000\001\001\001\001\001\001\001\001\001\0012222222111111000000000@@@\001\001\001\001\001\001\0012222222111111000000000@@@BBBB®®®®­­­­­­111111100000000@@BBBBB®®®­­­­­­¸¸¸¸¹¹¹¹¹¹¹¶¶¶¶0@@BBBBB®®®­­­­­¸¸¸¸¹¹¹¹¹¹¶¶¶¶¶¶¶BBBB®®­­­­­­¸¸¸¸¹¹¹¹¶¶¶¶¶¶¶¶¶¡¡¡¡¡¡¡­­¸¸¸¸¸¹¹¹¹¶¶¶¶¶¶¶¶º¡¡¡¡¡¡¡¡¡¸¸¸¸¸¹¹¹¹¶¶¶¶¶¶¶¶º¡¡¡¡¡¡¡¡¡¡¸¸±°°°°°°°°¶¶µµµµ¡¡¡¡¡¡¡¡¡¡¡¡±±°°°°°°°°°µµµµµ\036\036\036¡¡¡¡¡¡¡¡¡¡¡¡¡±±°°°°°°°°°µµµµµ\036\036\036\036\036\036\036¡&&&&&&&&±±°°°°°°°°°µµµµµ\036\036\036\036\036\036\036&&&&&&&&&±±°°°°°°°°µµµµµµ°°°°¬¬¬¬¬¬¬¬¬¢¢¢¬¬¬¬¬¬¬¬¬¬¢¢¢¢¢¢¬¬¬¬¬¬¬¬¬¢¢¢¢¢©©©©©©©©©©¢¢¢¢¢©©©©©©©©©©¢¢¢¢¢¢©©©©©©©©©©¢¢¢¢¢¢¢¢¢©©©©©©©©©©©%%%¢¢¢¢¢¢¢¢¢©©©©©©©©§§§©©©©§§§§§§§    §§§§§§§         §§§§§               §§§§               ¤¤¤          ¤¤¤         ¤¤¤¤   ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤C\001\001\001\001\001\001\001\001\0012222222111110000000000C\001\001\001\001\001\001\001\00122222221111111000000000@@\001\001\001\001\001\001\00122222221111111¾00000000@@@@BB®®®®®222221111111¾00000000@@@BBB®®®®®­­­­¸¸¸¸¸1¹¹¹¹¹000000@@@BBB®®®®®­­­¸¸¸¸¸¸¹¹¹¹¹¶¶¶¶¶¶¶\002BBB®®®®­­­­¸¸¸¸¸¹¹¹¹¹¶¶¶¶¶¶ºº\002\002\002\002¡¡¡¡¡¸¸¸¸¸¸¸¹¹¹¹¹¶¶¶¶¶¶ºº¡¡¡¡¡¡¡¡¸¸¸¸¸¸¹¹¹¹¶¶¶¶¶¶ººº¡¡¡¡¡¡¡¡¡...±±±°°°°°°°¶ºººº\036\036\036\036¡¡¡¡¡¡¡¡¡¡..±±±±°°°°°°°µµµµµ\036\036\036\036\036¡¡¡¡¡¡¡¡¡&&±±±±°°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&&&±±±±°°°°°°°µµµµµ\036\036\036\036\036\036\036&&&&&&&&&&±±°°°°°°°µµµµµµ\036\036&&&&&&&°°°¬¬¬¬¬¬¬¬¬¬¢¢¢¢¬¬¬¬¬¬¬¬¬¬¢¢¢¢¢¢¢¬¬¬¬¬¬¬¬¬¢¢¢¢¢¢¢©©©©©©©¬¬¢¢¢¢¢¢¢©©©©©©©©©©¢¢¢¢¢¢¢¢©©©©©©©©©©!!%%¢¢¢¢¢¢¢¢¢¢©©©©©©©©©©%%%¢©©©©©©©©§§§©©©§§§§§§§    §§§§§§§              §§§§§               §§§§               ¤¤¤¤         ¤¤¤¤      ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤CCC\001\001\001\001\001\001\0012222222111110000000000CCC\001\001\001\001\001\00122222221111111000000000@C\001\001\001\001\001\001\0012222222111111¾¾00000000@@@@AAA®®2222222111111¾¾¾0000000@@@@AA®®®®®®22¸¸111111¾¾¾¾¾00000@@@\002AA®®®®®®¸¸¸¸¸¸¸¸¹¹¹¹¹¹¶¶¶¶¶¶\002\002\002\002\002\002®®®®®®¸¸¸¸¸¸¸¸¹¹¹¹¹¶¶¶¶¶ºº\002\002\002\002\002\002\002¡¡¡¡..¸¸¸¸¸¸¸¹¹¹¹¹¶¶¶ºººº\002\002\002\002¡¡¡¡¡¡¡....¸¸¸¹¹¹¹¹¹¶¶¶ºººº\003\003¡¡¡¡¡¡¡.....±±±±±±°°°°ººººº\036\036\036\036\036¡¡¡¡¡¡¡¡....±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036¡¡¡¡¡&&&&±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&&&±±±±±°°°°°°µµµµµ\036\036\036\036\036\036\036&&&&&&&&&&±±±°°°°°°µµµµµµ\036\036\036\036\036\036\036&&&&&&&&&&¨¨¨¨¨¬¬¬¬¬¬¬¬¬¬&&&&¨¨¨¨¨¬¬¬¬¬¬¬¬¬¬!¢¢¢¢¢¢¢¢¬¬¬¬¬¬¬¬¬!¢¢¢¢¢¢¢¢¢©©¬¬¬¬¬¬¬¢¢¢¢¢¢¢¢¢©©©©©©©©©!!!¢¢¢¢¢¢¢¢¢©©©©©©©©©!!!#%%%%%¢¢¢¢¢¢¢©©©©©©©©©©##%%©©©©©©©§§§§####***§§§§§§§     #****§§§§§§§               §§§§§                §§§§               ¤¤¤¤¤             ¤¤¤¤¤¤  ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤CCCC\001\001\001\001\00122222222111110000000000CCCC\001\001\001\001\00122222221111111000000000CCCC\001\001\001\001\0012222222111111¾¾¾0000000@@@AAAAA22222221111111¾¾¾¾000000@@@AAAAA®®®222¸¸111111¾¾¾¾¾00000@@\002AAAAA®®®®¸¸¸¸¸¸¸¸¹¹¹¹¹¹¾¾¶¶¼¼\002\002\002\002\002\002\002AD®®..¸¸¸¸¸¸¸¹¹¹¹¹¹¹¼¼¼ºº\002\002\002\002\002\002\002DDD.....¸¸¸¸¸¹¹¹¹¹¹¼¼ºººº\002\002\003\003\003\003\003\003¡¡¡......¸¸¹¹¹¹¹¹¹¼¼ºººº\003\003\003\003\003\003\003\003¡¡¡......±±±±±±±°°¼ººººº\036\036\003\003\003\003\003\003¡¡¡¡.....±±±±±±°°°°µµµµµ\036\036\036\036\036\036\036\003¡&&&&&&&±±±±±±°°°°°µµµµµ\036\036\036\036\036\036\036\036&&&&&&&&±±±±±±°°°°°µµµµµ\036\036\036\036\036\036\036&&&&&&&&&¨¨¨¨¨°°°°°°µµµµµ\036\036\036\036\036\036\036&&&&&&&&&¨¨¨¨¨¨¨¬¬¬¬¬¬¬¬¬\036&&&&&&&¨¨¨¨¨¨¨¨¬¬¬¬¬¬¬¬¬!!!!!¢¢¢¢¨¨¬¬¬¬¬¬¬¬¬!!!!!!!!¢¢¢¢¢¢¢¬¬¬¬¬¬¬¬¬!!!!!!¢¢¢¢¢¢¢©©©©©©©©©!!!!!%%¢¢¢¢¢¢¢¢©©©©©©©©©!!!###%%%%%%%%¢¢¢¢©©©©©©©©©#####******§§§§§ #####******§§§§§        ###******§§§§§         *******§§§§§           ¤¤§§§§          ¤¤¤¤¤¤¤         ¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤CCCCCC\001\001\0012222222ÈÈ11110000000000CCCCCC\001\001222222221111111¾00000000CCCCC\001\001\00122222222111111¾¾¾0000000@@AAAAAA22222221111111¾¾¾¾000000@AAAAAAAA22222¸¸11111¾¾¾¾¾¾00000\002\002\002AAAAADDDD¸¸¸¸¸¸¸¸¹¾¾¾¾¾¾¾¼¼¼¼\002\002\002\002\002\002DDDDD..¸¸¸¸¸¸¸¹¹¹¹¹¼¼¼¼¼¼º\002\002\002\002\002DDDDD.....¸¸¸¸¸¹¹¹¹¹¼¼¼¼ººº\002\002\003\003\003\003\003\003DD.......¸¸¸/////¼¼¼¼ººº\003\003\003\003\003\003\003\003\003G.......±±±±±////¼¼ºººº\036\036\003\003\003\003\003\003\003GGGGG...±±±±±±±//°ººººº\036\036\036\036\036\036\036\003GGGGGGGG±±±±±±±±°°°°µµµµ\036\036\036\036\036\036\036\036&&&&&&&&---±±±±°°°°°µµµµ\036\036\036\036\036\036\036&&&&&&&&&-------««°°¬¬µµµ\036\036\036\036\036\036\036&&&&&&&&¨¨¨¨¨¨¨«««¬¬¬¬¬¬¬\036\036&&&&&&&¨¨¨¨¨¨¨««¬¬¬¬¬¬¬¬!!!!!!¨¨¨¨¨««¬¬¬¬¬¬¬¬!!!!!!!¢¢¢¢¢¢¬¬¬¬¬¬¬¬¬!!!!!!¢¢¢¢¢¢¢,,¬¬¬¬¬¬¬!!!!!%%%%%¢¢¢¢¢,,,,,,,©©©!!!#####%%%%%%¢¢¢,,,,,,,©©©######********§§§   ######********§§§§        ####********§§§§          ********§§§§          ***¤¤¤§§          ¤¤¤¤¤¤¤         ¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"¤¤¤\"\"\"\"\"\"$CCCCCCC\0012222222ÈÈÈÈ1110000000000CCCCCCC\001222222ÈÈÈ11111¾¾00000000CCCCCCCß22222ÈÈÈ111111¾¾¾0000000CAAAAAAA22222ÈÈÈ111111¾¾¾¾000000\002AAAAAAAA22ÈÈÈÈÈ11111¾¾¾¾¾¾00000\002\002\002AAADDDDDD¸¸¸¸¸¸¸11¾¾¾¾¾¾¾¼¼¼¼\002\002\002\002DDDDDDD...¸¸¸¸¸¸À¹¹¹¼¼¼¼¼¼¼¼\002\002\002\002DDDDDD.......¸¸¸////¼¼¼¼¼¼ºº\002\003\003\003\003\003\003\003DD........¸///////¼¼¼¼ºº\003\003\003\003\003\003\003\003\003GGG.....±±±±/////¼¼¼ººº\036\036\003\003\003\003\003\003\003GGGGGG..±±±±±/////·ºººº\036\036\036\036\036\036\036\003GGGGGGGG-±±±±±±±·······º\036\036\036\036\036\036\036\036&&&&&&&&------±±········\036\036\036\036\036\036\036&&&&&&&&¨------«««······¬\036\036\036\036\036\036\036&&&&&&&&¨¨¨¨¨-«««««¬¬¬¬¬¬&&&&&&&¨¨¨¨¨¨««««¬¬¬¬¬¬¬!!!!!¨¨¨¨««««¬¬¬¬¬¬¬!!!!!!!¢¢¢¢««¬¬¬¬¬¬¬¬!!!!!!¢¢¢¢,,,,,,,¬²²!!!!!%%%%%%¢¢¢,,,,,,,,,,²!#####%%%%%%%,,,,,,,,,,,,######',,,,,,,,,§§  ######**********§§        ####**********§§          **********§§          ****¤¤¤¤          ¤¤¤¤¤¤¤  \"\"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"\"$$¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"\"$$$¤¤\"\"\"\"\"\"\"\"\"\"\"\"$$$(CCCCCCCßßßßßÈÈÈÈÈÈÈ1110000000000CCCCCCßßßßßÈÈÈÈÈÈÈ1111¾¾00000000CCCCCCßßßßßÈÈÈÈÈÈ1111¾¾¾¾¾000000CCCAßßßßßßßÈÈÈÈÈÈ1111¾¾¾¾¾000000\022\022\022\022\022\022AßßßßÈÈÈÈÈÈ1111¾¾¾¾¾¾¾0000\022\022\022\022DDDDDDDÌÌÌÌÀÀÀÀÀÀ¾¾¾¾¾¾¾¼¼¼¼\022\022\022\022DDDDDDD....ÀÀÀÀÀÀÀÀ¼¼¼¼¼¼¼¼¼\022\022\022\022DDDDDDE.....ÀÀÀÀÀÀ//¼¼¼¼¼¼¼¼t\003\003\003\003\003\003\003EEEE....ÀÀÀÀ//////¼¼¼¼¼ºt\003\003\003\003\003\003\003EGGGGG...±±±//////¼¼¼¼¼º\036\003\003\003\003\003\003\003EGGGGGGG.±±±///////··ººº\036\036\036\036\036\036\003\003GGGGGGGG--±±±±±·········\036\036\036\036\036\036\036J&&&&&&&--------·········\036\036\036\036\036\036\036JJ&&&&&&¨------««········\036\036\036\036\036\036JJJ&&&&&&¨¨¨¨¨-«««««···¬²²JJJJ&&&¨¨¨¨¨¨««««««²²²²²!!¨¨¨¨««««««²²²²²!!!!!!¥¥¥¥¥«¬²²²²²²!!!!!!%%¥¥¥¥,,,,,²²²²!!!!!!%%%%%%%¥¥,,,,,,,,,²²####%%%%%%'',,,,,,,,,,,#####''',,,,,,,,,, #####'''*********+        ###''*********++         *********+++         ****++++        ¤¤¤¤¤¤¤ \"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"¤¤¤¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"\"$$$$$¤¤¤¤\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$(((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((CCCCCßßßßßÒÒÒÒÒÈÈÈÈÈ11000000000ÇCCCCßßßßßßÒÒÈÈÈÈÈÈÈ11¾¾¾00000000CCCóßßßßßßÒÒÈÈÈÈÈÈÈ1¾¾¾¾¾¾¾00000\022óóóßßßßßßÒÒÈÈÈÈÈÈÈ1¾¾¾¾¾¾¾00000\022\022\022\022\022\022ßßßßÒÈÈÈÈÈÈÈÈ1¾¾¾¾¾¾¾¾0000\022\022\022\022\022DDDDDÌÌÌÌÌÌÀÀÀÀÀÀÀ¾¾¾¾¾¼¼¼¼\022\022\022\022\022DDDDDÌÌÌÌÌÀÀÀÀÀÀÀÀÀ¼¼¼¼¼¼¼¼\022\022\022\022\022DDDDDÌÌÌÌÌÀÀÀÀÀÀÀÀ/¼¼¼¼¼¼¼¼tttt\003\003EEEEEEGGÌÀÀÀÀÀÀ/////¼¼¼¼¼¼tttt\003\003EEEEEGGGGGÀÀÀ///////¼¼¼¼¼¼tttt\003\003\003EEGGGGGGG½½½////////···¼¼ttt\003\003\003\003\003GGGGGGGG-----/··········\036\036\036\036\036JJJJJJJIIII-------·········\036\036\036\036\036JJJJJJJIIII------««········\036\036\036\036JJJJJJJJIIII¨¨--««««««···²²²JJJJJIIII¨¨¨¨««««««²²²²²²!¨¨¨¨««««««²²²²²²!!¥¥¥¥¥¥¥«²²²²²²²!!!%%%%¥¥¥¥¥¥¥,,,²²²²²!!!!!!!#%%%%%¥¥¥¥¥,,,,,,,,²²#####%%%'¥¥¥¥,,,,,,,,,,#####'''''',,,,,,,,,,   #####''''''*********+       ###''''''*******+++        ''*******+++++        **++++++       +++++++ \"\"\"\"\"\"\"+++++++\"\"\"\"\"\"\"\"\"\"\"((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$(((((óóóóóóóóßßÒÒÒÒÒÈÈÈÈÈÈ10ÇÇÇÇÇÇÇÇÇóóóóóóßßßßÒÒÒÒÈÈÈÈÈÈÉÉÉÉÇÇÇÇÇÇÇÇóóóóóóßßßßÒÒÒÈÈÈÈÈÈÉÉÉÉÉ¾ÇÇÇÇÇÇÇóóóóóßßßßßÒÒÒÈÈÈÈÈÉÉÉÉÉÉÉÇÇÇÇÇÇÇ\022\022\022\022\022\022ßßßßÒÈÈÈÈÈÈÈÉÉÉÉÉÉ33333333\022\022\022\022\022\022\022DêÌÌÌÌÌÌÌÀÀÀÀÀÀÉÉ33333333\022\022\022\022\022\022DDDÌÌÌÌÌÌÌÀÀÀÀÀÀÀÀ¼¼¼¼¼¼¼¼\022\022\022\022\022\022EEEÌÌÌÌÌÌÌÀÀÀÀÀÀÀ//¼¼¼¼¼¼¼ttttttEEEEEEÌÌÌÀÀÀÀÀÀÀ/////¼¼¼¼¼ttttttEEEEEEGGG½½½½À///////¼¼¼¼¼ttttttEEEEEGGG½½½½½½//////·····¼ttttttEFFFFFFF½½½½½½½/·········»\036\036\036\036JJJJJFFFFII½½½----········»»\036\036\036JJJJJJJJIIIII-----««·······»»JJJJJJJIIIII----««««««···²²²JJJJJIIII¨¨¨«««««««²²²²²²III¨¨¨¨«««««««²²²²²²¥¥¥¥¥¥¥¥¥²²²²²²²%%%¥¥¥¥¥¥¥¥,,,²²²²²!###%%¥¥¥¥¥¥¥,,,,,,,²²#####''¥¥¥¥¥,,,,,,,,,,#####''''''',,,,,,,,,, #####'''''''********++     #####'''''''******++++        '''''*****++++++       *+++++++      +++++++\"\"\"\"\"+++++++\"\"\"\"\"\"\"\"\"\"\"((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$((((((((\"\"\"\"\"\"\"\"\"\"\"\"$$$$$$$$$((((((\"\"\"\"\"\"\"\"\"\"\"\037$$$$$$$$$((((((ýýóóóóóóßßÒÒÒÒÒÈÈÈÈÈÏÏÏÇÇÇÇÇÇÇÇÇýýóóóóóßßßÒÒÒÒÈÈÈÈÈÉÉÉÉÉÇÇÇÇÇÇÇÇýýóóóóóßßßÒÒÒÒÈÈÈÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýóóóóóßßßßÒÒÒÒÈÈÈÉÉÉÉÉÉÉÉ3ÇÇÇÇÇÇ\022\022\022\022\022\022ßßßßÒÒÈÈÈÈÉÉÉÉÉÉÉÉ33333333\022\022\022\022\022\022êêêêêÌÌÌÌÌÀÉÉÉÉÉÉÉ33333333\022\022\022\022\022\022êêêêÌÌÌÌÌÌÀÀÀÀÀÀÀÀ33333333\022\022\022\022\022\022êêêêÌÌÌÌÌÌÀÀÀÀÀÀÀÃÃÃ333333ttttttEEEEEEÌÌÌÀÀÀÀÀÀÀÀÃÃÃÃÃÃ¼¼¼ttttttEEEEEEFF½½½½½½ÀÃÃÃÃÃÃÃÃ¼¼¼ttttttEEEFFFFF½½½½½½½/··········ttttttEFFFFFFF½½½½½½½·········»»\031\031\031\031\031JJJFFFFFF½½½½½½½-·······»»»\031\031\031\031\031JJJJJJIIIIII½½½««·······»»»\031\031\031\031\031JJJJJJIIIIII¯¯¯¯¯¯¯«··»»»»»\031JJJJJIIIIII¯¯¯¯¯¯¯¯«²²²²²²II¯¯¯¯¯¯¯¯¯«²²²²²²¥¥¥¥¥¥¥¯¯¯²²²²²²¥¥¥¥¥¥¥¥¥,,,²²²²²¥¥¥¥¥¥¥¥,,,,,,²²²#''¥¥¥¥¥¥,,,,,,,ªª###''''''',,,,,,,,ªª##'''''''',,,,,,,++#'''''''''**++++++ ''''''***++++++++++++++++++++\"\"\"\"++++++\"\"\"\"\"\"\"\"\"\"\"(((((((\"\"\"\"\"\"\"\"\"\"$$$$$((((((((\"\"\"\"\"\"\"\"\"\037\037$$$$$$$((((((((\"\"\"\"\037\037\037\037\037\037\037$$$$$$$(((((((ýýýýóóóóóÒÒÒÒÒÒÒÈÏÏÏÏÏÏÏÇÇÇÇÇÇÇÇýýýóóóóóßÒÒÒÒÒÒ8888ÏÏÏÏÏÇÇÇÇÇÇÇÇýýýóóóóóßÒÒÒÒÒ888ÉÉÉÉÉÉÉÉÇÇÇÇÇÇÇýýóóóóóóßÒÒÒÒÒ888ÉÉÉÉÉÉÉ333ÇÇÇÇÇ\022\022\022\022aaaêêêê88888ÉÉÉÉÉÉÉÉ33333333\022aaaaaêêêêêêÌÌÌÌÉÉÉÉÉÉÉÉ33333333aaaaaaêêêêêÌÌÌÌÌÀÀÀÀÀÀÀÃ33333333aaaaaaêêêêêÌÌÌÌÌÀÀÀÀÀÃÃÃÃÃÃ33333ttttttEEEEÌÌÌÌÌÌÀÀÀÀÀÃÃÃÃÃÃÃÃÃ33ttttttEEEEFFFF½½½½½½ÃÃÃÃÃÃÃÃÃÃÃ·tttttteEFFFFFF½½½½½½½ÃÃÃÃÃÃÃ···»ttttteeFFFFFFF½½½½½½½½······»»»»\031\031\031\031\031\031JJFFFFFF½½½½½½½½······»»»»\031\031\031\031\031\031\031JJJJIIIIIH½½½¯¯¯····»»»»»\031\031\031\031\031\031\031JJJJIIIIII¯¯¯¯¯¯¯¯¯·»»»»»\031\031\031\031\031\031\031JJJJIIIII\005¯¯¯¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177\177\005\005\005\005¯¯¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177\177\005\005\005¯¯¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177\005¥¥¥¥¥¥¥¥ªªª²²²²²\177\177\177¥¥¥¥¥¥¥ªªªªªªªª²''¥¥¥¥¥¥ªªªªªªªªª'''''ªªªªªªªªªª''''''ªªªªªªªª+''''''++++++++'''''+++++++++++++++++++++\"\"+++++\"\"\"\"\"\"\"\"\"\"((((((\"\"\"\"\"\"\"\"\"$$$((((((((\"\"\"\"\"\037\037\037\037\037$$$$$$$(((((((((\"\"\"\037\037\037\037\037\037\037$$$$$$$(((((((((ýýýýýóóóóÒÒÒÒÒÒÒÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýóóóóóÒÒÒÒ8888ÏÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýóóóóççç8888888ÉÉÉÉÉÏÏÇÇÇÇÇÇÇýýóóóóóóççç888888ÉÉÉÉÉÉÉ333ÇÇÇÇÇaaaaaaaêççç888888ÉÉÉÉÉÉÉ33333333aaaaaaêêêêêêêÌÌ8ÉÉÉÉÉÉÉÉ33333333aaaaaaêêêêêêêÌÌÌÌÉÉÉÉÃÃÃÃ3333333aaaaaaêêêêêêêÌÌÌÌÀÀÀÃÃÃÃÃÃÃ3333ÆttttttêêêêêÌÌÌÌÌÀÀÀÀÃÃÃÃÃÃÃÃÃÃÆÆttttteeeeeFFFF½½½½½½ÃÃÃÃÃÃÃÃÃÃÃÃtttteeeeeFFFFF½½½½½½½ÃÃÃÃÃÃÃÃÃ»»tttteeeeeFFFFF½½½½½½ÁÁÁÁÁÁÁ»»»»»\031\031\031\031\031\031eeFFFFHHH½½½½½ÁÁÁÁÁÁ»»»»»»\031\031\031\031\031\031\031\031\031IHHHHHHH½½ÁÁÁÁÁÁÁ»»»»»»\031\031\031\031\031\031\031\031\031IHHHHHHH¯¯¯¯¯¯¯¯¯»»»»»»\031\031\031\031\031\031\031\031\031KIIIII\005\005\005\005¯¯¯¯¯¯¯²»»»»»\177\177\177\177\177\177\177\177\005\005\005\005\005¯¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177\177\005\005\005\005\005\005¯¯¯¯¯¯²²²²²²\177\177\177\177\177\177\177\177\005\005\005\005\005\005¥ªªªªªªª²²²\177\177\177\177\177¥¥¥¥¥¥ªªªªªªªªª²¥¥¥¥ªªªªªªªªªªª'ªªªªªªªªªªª'ªªªªªªªªªª+++++++++++++++++++++++++++++\"\"(((((\037\037\037\037\037(((((((\037\037\037\037\037\037\037$$$$(((((((((\037\037\037\037\037\037\037\037$$$$$((((((((((ýýýýýýóóóóÒÒÒÒÒÏÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýýýóóççççç8888ÏÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýýóóççççç888888ÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýýóóççççç88888ÉÉÉÉÉÉÉÊÊ3ÇÇÇÇÇaaa\f\f\f\fççççç88888ÉÉÉÉÉÉÊÊÊ33333Æaaaaaa\fêêêêêê888ÉÉÉÉÉÉÉÊÊ33333ÆÆaaaaaa\fêêêêêêÛÛÛÛÛÉÉÉÊÊÊÊ33333ÆÆaaaaaaêêêêêêêÛÛÛÛÛÛÃÃÃÃÃÃÃÃ33ÆÆÆtahhhhhêêêêêÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÃÃÃÆÆthhheeeeeeeFFÛÛÛÛ½½½ÃÃÃÃÃÃÃÃÃÃ55thhheeeeeeeFFF½½½½½½ÃÃÃÃÃÃÃÃÃÂÂ»hhhheeeeeeeFFF½½½½½ÁÁÁÁÁÁÂÂÂÂ»»»\031\031\031\031\031eeeeeHHHHHH½½½ÁÁÁÁÁÁÂÂÂ»»»»\031\031\031\031\031\031\031\031\031KHHHHHHHHÁÁÁÁÁÁÁÂÂÂ»»»»\031\031\031\031\031\031\031\031KKKKHHHHH¯¯¯¯¯¯¯ÁÁÂ»»»»»\031\031\031\031\031\031\031\031KKKKKKK\005\005\005\005¯¯¯¯¯¯¯»»»»»»\177\177\177\177\177\177\177\177KKKKKK\005\005\005\005\005¯¯¯¯¯¯¯²²²²²\177\177\177\177\177\177\177\177\005\005\005\005\005\005¯¯¯¯¯¯¯ª²²²²\177\177\177\177\177\177\177\177\005\005\005\005\005\005\005ªªªªªªªªª²\177\177\177\177\177LLLLLªªªªªªªªªªª´LLLLLLLªªªªªªªªªªª´ªªªªªªªªªªª´ªªªªªª¦¦´¦¦¦¦+¦¦¦++¦¦¦++++++++\037\037\037\037\037\037\037\037(((\037\037\037\037\037\037\037(((((((((ýýýýýýýñññññçç8ÏÏÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇýýýýýññññçççç8888ÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇýýýýýñññççççç88888ÏÏÏÏÏÏÏÇÇÇÇÇÇÇýýýýññññççççç88888ÉÉÉÉÊÊÊÊÊÊÇÇÇÇa\f\f\f\f\f\f\fçççç888888ÉÉÉÉÊÊÊÊÊÊ3ÆÆÆaaa\f\f\f\f\fêêêê88888ÉÉÉÉÊÊÊÊÊÊÊ3ÆÆÆaaa\f\f\f\f\fêêêêÛÛÛÛÛÛÛÛÊÊÊÊÊÊÊÊÆÆÆÆaaaaaaaêêêêêÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÆÆÆÆhhhhhhhheeêÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÃ5555hhhhhheeeeeeÛÛÛÛÛÛÛÃÃÃÃÃÃÃÃÃ5555hhhhhheeeee>>>>>>>½ÁÃÃÃÃÃÃÃ55555hhhhheeeeee>>>>>>>ÁÁÁÁÁÁÂÂÂÂÂÂ»»\031\031\031\031\031eeeeeHHHHHHHHÁÁÁÁÁÁÂÂÂÂÂ»»»\031\031\031\031\031\031\031\031oKKHHHHHHHÁÁÁÁÁÁÂÂÂÂÂ»»»\031\031\031\031\031\031\031\031oKKKKHHHHH¯¯ÁÁÁÁÁÂÂÂ»»»»\031\031\031\031\031\031\031\031oKKKKKK\005\005\005\005\005¯¯¯¯¯¯¿»»»»»\177\177\177\177\177\177\177\177KKKKKKK\005\005\005\005\005\005¯¯¯¯¯¿¿¿»»»\177\177\177\177\177\177\177\177\005\005\005\005\005\005\005¯¯¯¯¯¿¿ªªªª\177\177\177\177\177\177\177\177LLLLLL\005\005\005ªªªªªªªªªª\177\177\177\177\177\177LLLLLLLLªªªªªªªªª´´LLLLLLLLLªªªªªªªª´´´Lªªªªªªªª´´´¦¦¦¦´´´¦¦¦¦¦´¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\037\037\037\037\037\037\037\037\037\037\037ýýýýýýññññññçç8ÏÏÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇTTTTññññññçççç888ÏÏÏÏÏÏÏÏÏÇÇÇÇÇÇTTTTñññññçççç88888ÏÏÏÏÏÏÏÏÇÇÇÇÇÇTTTTñññññçççç888888ÏÏÊÊÊÊÊÊÊÊÆÆÆ\f\f\f\f\f\f\f\fççççç888888àÊÊÊÊÊÊÊÊÆÆÆÆa\f\f\f\f\f\f\f\fììììì888àààÊÊÊÊÊÊÊÊÆÆÆÆa\f\f\f\f\f\f\fììììÛÛÛÛÛÛÛÛÊÊÊÊÊÊÊÊÆÆÆÆaaa\f\f\f\f\fììììÛÛÛÛÛÛÛÛÃÃÃÃÃÃÃ5ÆÆÆÆhhhhhhhhììììÛÛÛÛÛÛÛÛÃÃÃÃ55555555hhhhhhheeee>ÛÛÛÛÛÛÛÛÃÃÃÃ55555555hhhhhhheeee>>>>>>>æÁÁÃÃ555555555hhhhhheeeee>>>>>>>ÁÁÁÁÁÁÂÂÂÂÂÂ5»sss\031\031eeeee>>>>>>>>ÁÁÁÁÁÁÂÂÂÂÂÂ»»ss\031\031\031\031oooooKHHHHHHÁÁÁÁÁÁÂÂÂÂÂÂ»»ss\031\031\031\031ooooKKKKKHH\004ÁÁÁÁÁÁÂÂÂÂÂÂ»»ss\031\031\031\031ooooKKKKK\004\005\005\005\005\005¿¿¿¿¿¿¿»»»»\177\177\177\177\177\177\177ooKKKKK\004\005\005\005\005\005\005¿¿¿¿¿¿¿¿¿»»\177\177\177\177\177\177\177\177\177\004\004\004\005\005\005\005\005\005¿¿¿¿¿¿¿¿¿ªª\177\177\177\177\177\177\177\177\177LLLLLLL\005\005¿¿¿¿ªªªªªª´\177\177\177\177\177\177\177\177LLLLLLLLLªªªªªªªª´´´\032\032\032\032\032\032LLLLLLLLLªªªªªªª´´´´\032\032\032\032LLLªªªªªªª´´´´\032\032\032\032\032¦¦¦¦´´´´\032\032\032¦¦¦¦¦´´¦¦¦¦¦¦¦\034¦¦¦¦¦¦¦\034\034¦¦¦¦¦¦\037\037\037TTTTTñññññññçççÏÏÏÏÏÏÏÏÏÏÏÏÏÇÇÇÇTTTTñññññññçççç8ÏÏÏÏÏÏÏÏÏÏÏÏÇÇÇÇTTTTññññññççççàààÏÏÏÏÏÏÏÏÏÊÊÊÊÆÆTTTTññññññçççàààààà×××ÊÊÊÊÊÊÊÆÆÆT\f\f\f\f\f\fññççççàààààà××ÊÊÊÊÊÊÊÆÆÆÆ\f\f\f\f\f\f\f\fWììììàààààà××ÊÊÊÊÊÊÊÆÆÆÆ\f\f\f\f\f\f\fWììììììÛÛÛÛÛà7ÊÊÊÊÊÊÊÆÆÆÆ\f\fWWWWWWìììììÛÛÛÛÛÛÕÕÕÊÊÊ5555ÆÆÆhhhhhhhWìììììÛÛÛÛÛÛÕÕÕÕ555555555hhhhhhhhee>>ÛÛÛÛÛÛÛÕÕÕÕ555555555hhhhhhhhee>>>>>>ææææÕÕÁ555555555hhhhhhhhee>>>>>>æææÁÁÁÁÂÂÂÂÂÂ555sssssseooo>>>>>>æææÁÁÁÁÂÂÂÂÂÂÂÅÅssssssooooooKKKææææÁÁÁÁÂÂÂÂÂÂÂÅÅssssssoooooKKKKK\004\004ÁÁÁÁÁÁÂÂÂÂÂÅÅÅssssssoooooKKK\004\004\004\004\004\004¿¿¿¿¿¿¿¿ÅÅÅÅsssssooooKKKK\004\004\004\004\004\004\005¿¿¿¿¿¿¿¿444Å\177\177\177xxxxxxxx\004\004\004\004\004\004\004\004\005¿¿¿¿¿¿¿¿4444\177\177\177xxxxxxxx\004LLLLLLLL¿¿¿¿¿¿¿¿444´\032\032\032\032xxxxxxxLLLLLLLLL\006\006\006ªªªª´´´´´\032\032\032\032\032\032\032\032\032xLLLLLLLL\006\006\006ªªª´´´´´´\032\032\032\032\032\032\032\032\032LLLLLLL\006\006\006ªªª´´´´´´\032\032\032\032\032\032\032\032¦¦¦´´´´´\032\032\032\032\032\032\032¦¦¦¦¦´´}}}}}¦¦¦¦¦¦¦\034\034\034\034\034\034\034\034¦¦¦¦¦¦¦\034\034\034\034\034\034\034\034¦¦¦¦¦¦\034\034\034\034\034\034\034\034\034TTTTTññññññññççÏÏÏÏÏÏÏÏÏÏÏÏÏÏÇÇÇTTTTññññññññçççÏÏÏÏÏÏÏÏÏÏÏÏÏÏÊÇÇTTTTññññññññçàààà××××××ÏÏÏÊÊÊÊÆÆTTTTññññññññàààààà×××××ÊÊÊÊÊÊÆÆÆT\f\f\f\f\fñññññçàààààà×××77ÊÊÊÊÊÊÆÆÆ\\\f\fWWWWWWììììààààà×××77ÊÊÊÊÊÊÆÆÆ\\\fWWWWWWWììììììÛÛÛà77777ÊÊÊÊÊÆÆÆ\\\\WWWWWWWìììììÛÛÛÛÕÕÕÕÕ75555555ÆhhhWWWWWììììììÛÛÛÛÕÕÕÕÕ555555555hhhhhhhhYYY>>ÛÛÛÛÕÕÕÕÕÕ555555555hhhhhhhhYYY>>>>>æææÕÕÕÕ555555555hhhhhhhYYYY>>>>æææææÁÁÂÂÂÂÂÂÂ55ÅssssssYYYYY>>>ææææææÁÁÂÂÂÂÂÂÂÅÅÅssssssoooooooæææææææÁÁÂÂÂÂÂÂÅÅÅÅssssssooooooKK\004\004\004\004\004ÁÁÁÂÂÂÂÂÂÅÅÅÅssssssoooooo\004\004\004\004\004\004\004\004¿¿¿¿¿¿44ÅÅÅÅsssssooooooo\004\004\004\004\004\004\004\004¿¿¿¿¿¿44444Å\026\026xxxxxxxxx\004\004\004\004\004\004\004\004¿¿¿¿¿¿¿44444Ä\026\026xxxxxxxxxxLLLLLLLL¿¿¿¿¿¿¿4444Ä\032\032\032xxxxxxxxxLLLLLLL\006\006\006\006\006¿¿´´´´´´\032\032\032\032\032\032\032\032\032xxLLLLLLL\006\006\006\006\006\006\006´´´´´´´\032\032\032\032\032\032\032\032\032\032LLL\006\006\006\006\006\006\006\006\006´´´´´´´\032\032\032\032\032\032\032\032\032\032\006¦¦¦´´´´´}\032\032\032\032\032\032\032\032\032¦¦¦¦¦¦´´}}}}}}{{¦¦¦¦¦¦¦¦}}}}\034\034\034\034\034¦¦¦¦¦¦¦¦}\034\034\034\034\034\034\034\034¦¦¦¦¦¦¦¦\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034\034TTTTTññññññññèèÙÙÙÙÙÙÙÙÙÙÙÏÏÑÑÑÑTTTTTññññññññààÙÙÙÙÙÙÙÙÙÙÙÏÏÑÑÑÑTTTTTñññññññààààà××××××××7ÊÊÊÊÊÆTTTTñññññññàààààà××××××777ÊÊÊÊÆÆT\\\\\\úúúññññ;ààààà××××777777ÊÊÊÆÆ\\\\\\\\WWWWWììì;ààààà×××777777ÊÊÊÆÆ\\\\\\WWWWWWìììììì;;à×77777777ÊÊÊÆÆ\\\\\\WWWWWWìììììììÕÕÕÕÕÕÕ775555555\\\\WWWWWWììììììììÕÕÕÕÕÕÕÕ55555555hhhhhhhYYYYYY>>åÕÕÕÕÕÕÕÕ55555555hhhhhhYYYYYYY>ææææÕÕÕÕÕÕ55555555hhhhh_YYYYYYYææææææææÍÍÂÂÂ55555ÅssssssYYYYYYYæææææææÍÍÍÍÍÍÍÍÅÅÅÅssssssoooooooæææææææÍÍÍÍÍÍÍÅÅÅÅÅssssssoooooo^^^^^\004\004\004ÍÍÍÍÍÍÍÅÅÅÅÅssssssoooooo^\004\004\004\004\004\004\004¿¿¿¿4444ÅÅÅÅss\026\026\026\026\026\026ooo^^\004\004\004\004\004\004¿¿¿¿¿¿44444ÄÄ\026\026\026\026xxxxxxx^\004\004\004\004\004\004\004¿¿¿¿¿¿44444ÄÄ\026\026\026\026xxxxxxxx\004\004\004\004\004L\006\006¿¿¿¿¿¿444ÄÄÄ\032\032\032xxxxxxxxxLLLL\006\006\006\006\006\006\006\006\006ÄÄÄ´´´´\032\032\032\032\032\032\032\032xxxxLLLL\006\006\006\006\006\006\006\006\006´´´´´´´\032\032\032\032\032\032\032\032\032\032\006\006\006\006\006\006\006\006\006´´´´´´´{\032\032\032\032\032\032\032\032\032\006\006\006¦¦¦´´´´´}{{{{{{{\032\032¦¦¦¦¦¦´´´}}}}{{{{{¦¦¦¦¦¦¦¦¦}}}}}\034\034\034\034¦¦¦¦¦¦¦¦)}}}\034\034\034\034\034\034~~)))))))))}\034\034\034\034\034\034\034\034~~)))))))))\034\034\034\034\034\034\034\034)))\034\034\034\034\034£TTTTTTññññèèèèèèÙÙÙÙÙÙÙÙÙÙÙÑÑÑÑÑTTTTTúúúúñððèèèèÙÙÙÙÙÙÙÙÙÙÑÑÑÑÑÑTTTTúúúúúúðððàààà××ÙÙÙÙÙÙÙÑÑÑÑÑÑTTTúúúúúúúððààààà××××××77777ÊÑÑÑ\\\\\\úúúúúúúð;;;ààà××××7777777ÓÓÑÑ\\\\\\\\\\WWWWìì;;;;;à×××77777777ÓÓÊÆ\\\\\\\\WWWWWììì;;;;;;×777777777Ó555\\\\\\\\WWWWWìììììåååååÕÕÕÕ777555555\\\\WWWWWWWìììììååååÕÕÕÕÕÕ55555555ddd_______YYYåååååÕÕÕÕÕÕ55555555ddd_____YYYYYæææææÕÕÕÕÕÕ55555555dd\020\020____YYYYYææææææææÍÍÍÍÍÍÍ55ÅÅsss\020\020\020\020YYYYYYæææææææÍÍÍÍÍÍÍÍÅÅÅÅsssss\020\020\020\020YYYYæææææææÍÍÍÍÍÍÍÍÅÅÅÅsssssjjjjjj^^^^^^^^\004ÍÍÍÍÍÍÍÍÅÅÅÅsssssjjjjjj^^^^^^\004\004¿¿¿¿44444ÅÅÅÅs\026\026\026\026\026\026\026jjj^^^^^^\004\004¿¿¿¿444444ÄÄÄ\026\026\026\026\026\026\026\026xxx^^^^\004\004\004\004¿¿¿¿44444ÄÄÄÄ\026\026\026\026\026xxxxxxx\004\004\004\004\004\006\006\006\006¿¿¿¿4ÄÄÄÄÄÄ\026\026\026xxxxxxxxrrrr\006\006\006\006\006\006\006\006\006ÄÄÄÄÄÄÄÄ\032\032\032\032\032\032\032xxxxrrrr\006\006\006\006\006\006\006\006\006MM´´´´´´{{{{{{{\032\032\032r\006\006\006\006\006\006\006\006\006MM´´´´´´{{{{{{{{{\032\006\006MM\007\007´´´´´}{{{{{{{{{\007\007\007\007\007\007\007´´´}}}}{{{{{{\007\007\007\007\007\007\007\007\007¦}}}}}\034\034~~~~\007\007\007\007\007\007))))}}}\034\034\034\034~~~~~~~)))))))))}\034\034\034\034\034\034~~~~~~~)))))))))\034\034\034\034\034\034\034~~~~~~~))))))))£\034\034\034\034\034))))£££££\034\034\034£££££££££££££TTTTTþþþèèèèèèèèèÙÙÙÙÙÙÙÙÙÑÑÑÑÑÑþþþþþþúúððèèèèèèèÙÙÙÙÙÙÙÙÑÑÑÑÑÑÑþþþþúúúúðððððèèèÙÙÙÙÙÙÙÙÙÑÑÑÑÑÑÑþþþúúúúúðððððààà×××××××777ÑÑÑÑÑÑ\\þþúúúúúðððð;;;;;××××77777ÓÓÓÑÑÑ\\\\\\\\\\\\úúððð;;;;;;×××777777ÓÓÓÓÑÑ\\\\\\\\\\\\WWùùù;;;;;;;å7777777ÓÓÓÓÓ5\\\\\\\\\\\\WWùùùùùùååååååÕÕÕ77ÓÓÓÓ555d\\\\WWWWùùùùùùùåååååÕÕÕÕÕÕ55555ÐÐdddd_____ùùùùùåååååÕÕÕÕÕÕ555ÐÐÐÐdddd_____YYYYææææÕÕÕÕÕÕÕÕ55ÐÐÐÐÐddd\020\020____YYYYæææææææÕÍÍÍÍÍÍÍÅÅÅÅdd\020\020\020\020\020\020YYYYYæææææææÍÍÍÍÍÍÍÍÅÅÅÅs\020\020\020\020\020\020\020\020YYYõõõõõæææÍÍÍÍÍÍÍÍÅÅÅÅssi\020jjjjjjj^^^^^^^^ÍÍÍÍÍÍÍÍÍÅÅÅÅssiijjjjjjj^^^^^^^^¿ÍÍ444444ÅÅÅÅ\026\026\026\026\026\026\026jjjj^^^^^^^^¿¿4444444ÄÄÄÄ\026\026\026\026\026\026\026\026\026xj^^^^^^^^¿¿444444ÄÄÄÄÄ\026\026\026\026\026\026\026\026xxrrrrrr\006\006\006\006\006\006444ÄÄÄÄÄÄÄ\026\026\026\026xxxxxxrrrrrr\006\006\006\006\006\006\006\006ÄÄÄÄÄÄÄÄ{{{xxxxxxrrrrrrr\006\006\006\006\006\006\006MMMOOOOOÄ{{{{{{{{{rrrrrrr\006\006\006\006\006\006MMMMOOOOOO{{{{{{{{{{MMMMMMOOOOOO}{{{{{{{{{MMM\007\007\007\007\007\007\007OO}}}}{{{{{{\007\007\007\007\007\007\007\007\007\007}}}}}\034~~~~~~~\007\007\007\007\007\007\007\007)))}}}}\034\034~~~~~~~~\007\007))))))))}}\034\034\034\034~~~~~~~~)))))))))\034\034\034\034\034\034~~~~~~~~)))))))££\034\034\034\034\034~))))£££££\034\034\034££££££££\033\033\035\035\035\035\035\035\035\035\035\035££££££££UUþþþþþþèèèèèèèèèÙÙÙÙÙÙÙÙÑÑÑÑÑÑÑþþþþþþþúððèèèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑþþþþþúúúððððèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑþþþþþúúúðððððð;;×××××××7ÑÑÑÑÑÑÑÑþþþþúúúúðððð;;;;;ããã77ÓÓÓÓÓÓÑÑÑÑ\\\\\\\\\\\\úúððð;;;;;;;ãã77ÓÓÓÓÓÓÓÑÑÑ\\\\\\\\\\\\ÿÿùùùù;;;;;;ãã77ÓÓÓÓÓÓÓÓÓÓ\\\\\\\\\\\\ÿÿùùùùùùåååååååÕÓÓÓÓÓÓÓÓÐÐdd\016\016\016\016\016ÿùùùùùùååååååÕÕÕÕÕÐÐÐÐÐÐÐdddd_____ùùùùùååååååÕÕÕÕÐÐÐÐÐÐÐÐdddd______YYYYåååååÕÕÕÕÕÐÐÐÐÐÐÐÐddd\020\020_____YYõõõõõõõõÍÍÍÍÍÍÐÐÐÐÐÐdd\020\020\020\020\020\020_YYYõõõõõõõõÍÍÍÍÍÍÍÍÅÅÅÅi\020\020\020\020\020\020\020\020jjõõõõõõõõõÍÍÍÍÍÍÍÍÅÅÅÅiiiiiijjjjj^^^^^^^õõÍÍÍÍÍÍÍÍÅÅÅÅiiiiiijjjjj^^^^^^^òòòÍÍ44444ËÅÅÅii\026\026\026\026\026jjjj^^^^^^^òòòò44444ÄÄÄÄÄ\026\026\026\026\026\026\026\026\026jj^^^^^^^òòòò4444ÄÄÄÄÄÄ\026\026\026\026\026\026\026\026\026rrrrrrròòòòòòò44ÄÄÄÄÄÄÄp\026\026\026\026\026\026\026mrrrrrrr\006\006\006\006\006\006\006\006ÄÄÄÄÄÄÄÄp{{{{xmmmrrrrrrr\006\006\006\006\006MMMMOOOOOOO{{{{{{{{{rrrrr\030\030\030\006\006MMMMMMOOOOOOO{{{{{{{{{{\030\030\030\030\030\030\030\030MMMMMMMOOOOOOO}{{{{{{{{{\030\030\030\030\030||||MMMMM\007\007\007\007OOOO}}}{{{{{{{\030\030\030||||||||\007\007\007\007\007\007\007\007\007³³}}}}}}~~~~~~~||||||||\007\007\007\007\007\007\007\007))³}}}}}~~~~~~~~~|||||\007\007))))))))}}}}\034~~~~~~~~~)))))))))\034\034\034\034\034~~~~~~~~~\035\035\035\035\035\035\035\035\035))))))£££\034\034\034\034~~~\035\035\035\035\035\035\035\035\035)))££££££\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035££££££££\033\033\033\033\035\035\035\035\035\035\035\035\035\035££££££££UUUUUþþþèèèèèèèèèÙÙÙÙÙÙÙÙÑÑÑÑÑÑÑUUþþþþþúððèèèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑUþþþþþúúððððèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑUþþþþþúúðððððð;ããããããÙÙÓÑÑÑÑÑÑÑÑþþþþþúúúððððð;;;ãããããÓÓÓÓÓÓÑÑÑÑÑ\\\\\\\\\\úúúðððð;;;;ãããããÓÓÓÓÓÓÓÓÑÑÑ]]]\\\\ÿÿÿÿÿùù;;;;ãããããÓÓÓÓÓÓÓÓÓÓ9]]]\016\016ÿÿÿÿùùùùùåååååååÓÓÓÓÓÓÓÓÓÐÐ]]\016\016\016\016\016ÿÿùùùùùååååååÝÝÝÝÝÐÐÐÐÐÐÐddd\016\016\016\016\016ùùùùùùååååååÝÝÝÝÝÐÐÐÐÐÐÐdddd_______QQQåååååÝÝÝÝÝÝÐÐÐÐÐÐÐddd\020\020\020____QQõõõõõõõÝÝÝÝÝÝÐÐÐÐÐÐÐdd\020\020\020\020\020\020__QQõõõõõõõõÍÍÍÍÍÍÍÍËËËËii\020\020\020\020\020\020\020QQQõõõõõõõõÍÍÍÍÍÍÍËËËËËiiiiiiijjjjj^^õõõõõõÍÍÍÍÍÍËËËËËËiiiiiiijjjj^^^^^^òòòòòòÍÍÍËËËËËËiiiiiijjjjj^^^^^òòòòòòòòÄÄÄÄÄÄÄÄpp\026\026\026\026\026\026\026jj^^^^^òòòòòòòòÄÄÄÄÄÄÄÄppp\026\026\026\026mmmmrrrrròòòòòòòòÄÄÄÄÄÄÄÄppppppmmmmrrrrrrròòòòòòòÄÄÄÄÄÄÄÄpppppmmmmmrrrrrr\030\030MMMMMMOOOOOOO6{{{{{{{{mrrr\030\030\030\030\030\030MMMMMMMOOOOOOO{{{{{{{{{{\030\030\030\030\030\030\030\030MMMMMMMOOOOOOO{{{{{{{{{{\030\030\030\030\030\030||||MMMM\007\007\007OOOOO}}{{{{{{{{vvv||||||||\007\007\007\007\007\007\007\007\007³³}}}}}~~~~~~~~||||||||\007\007\007\007\007\007\007\007³³³}}}}}~~~~~~~~||||||||\007\007\007\007))))³³³}}}}}~~~~~~~~~\035\035\035\035\035\035\035\035\035))))))³³³}\034\034\034~~~~~~~~~~\035\035\035\035\035\035\035\035\035)))))££££\033\033\033\033\033\033\033~~~~~~~\035\035\035\035\035\035\035\035\035)))££££££\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035££££££££\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035££££££££UUUUUþþþèèèèèèèèèÙÙÙÙÙÙÙÙÑÑÑÑÑÑÑUUUþþþþðð=èèèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑUUþþþþþðððððèèèèèÙÙÙÙÙÙÙÑÑÑÑÑÑÑÑUUþþþþþðððððð===ããããããÓÓÑÑÑÑÑÑÑÑUþþþþþþðððððð==ããããããÓÓÓÓÓÓÑÑÑÑÑ]]]]þþððððððð;ãããããããÓÓÓÓÓÓÓÓ99Ñ]]]]]ÿÿÿÿÿÿùùùãããããããÓÓÓÓÓÓÓÓ999]]]]\016ÿÿÿÿÿùùùùùåååååãÓÓÓÓÓÓÓÓ999]]]\016\016\016\016ÿÿÿùùùùåååååÝÝÝÝÝÝÝÐÐÐÐÐÐddd\016\016\016\016\016ÿùùùùùåååååÝÝÝÝÝÝÐÐÐÐÐÐÐddd\016\016\016\016\016__QQQQQõõåÝÝÝÝÝÝÝÐÐÐÐÐÐÐdddd\020\020\020\020_QQQQQõõõõõÝÝÝÝÝÝÐÐÐÐÐÐÐdd\020\020\020\020\020\020\020QQQQõõõõõõõëëÍÍÍÐÐËËËËËiiiiiii\020\020QQQQõõõõõõõëëÍÍÍËËËËËËËiiiiiiiijjjQõõõõõõõëëëëÍÍËËËËËËËiiiiiiiijjjj^RRRRòòòòòòòËËËËËËËËiiiiiiiijjjj^RRRòòòòòòòòÄÄÄÄËËËËppppppjmmmmm\023RRRòòòòòòòòÄÄÄÄÄÄÄÄppppppmmmmmmrrrròòòòòòòòÄÄÄÄÄÄÄÄppppppmmmmmrrrrrccccòòòòÄÄÄÄÄÄÄÄppppppmmmmmrrrrrcccMMMMMOOOOO666ppp{{{mmmrrr\030\030\030\030\030\030cMMMMMOOOOOO66{{{{{uuuuuv\030\030\030\030\030\030\030cMMMMMOOOOOO66{{{{uuuuuuvv\030\030\030\030||||MMMM\007OOOOOO6{{{{uuuuuuvvvv|||||||\007\007\007\007\007\007\007\007³³³}}}uuu~~~~vvvv|||||||\007\007\007\007\007\007\007³³³³}}}}~~~~~~~~~||||||||\007\007\007\007\007\007\007³³³³}}}}~~~~~~~~~~\035\035\035\035\035\035\035\035\035N)))³³³³³}\033\033\033~~~~~~~~~~\035\035\035\035\035\035\035\035\035\035))££££££\033\033\033\033\033\033\033\033\033~~~~\035\035\035\035\035\035\035\035\035\035\b\b\b££££££\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b££££££\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\035\b\b\b££££££UUUUUþþþèèèèèèèèèââââââÙÑÑÑÑÑÑÑÑUUUUþþþþ====èèèèââââââââÑÑÑÑÑÑÑÑUUUþþþþø========ââââââââÑÑÑÑÑÑÑÑUUUþþþøøøø======âââââââÓÑÑÑÑÑÑÑÑUUþþþþøøøøø====ããããããÓÓÓÓÓÑÑÑÑÑÑ]]]]]øøøøøøøøããããããããÓÓÓÓÓÓ99999]]]]]ÿÿÿÿÿÿÿùùãããããããÓÓÓÓÓ999999]]]]\016ÿÿÿÿÿÿÿùùöãããããéÓÓÓÓ9999999]]]\016\016\016\016ÿÿÿÿÿùùööééééÝÝÝÝÝÝÐÐÐÐÐÐ]]\016\016\016\016\016\016ÿÿÿùùöööééééÝÝÝÝÝÝÐÐÐÐÐÐdd\016\016`````QQQQQQQééééÝÝÝÝÝÝÐÐÐÐÐÐdd```````QQQQQQõõõõÝÝÝÝÝÝÐÐÐÐÐÐÐd````````QQQQQõõõõëëëëëëëÐÐËËËËËiiii`````QQQQQõõõõëëëëëëëËËËËËËËiiiiiiiiibbb??????ëëëëëëëËËËËËËËiiiiiiiibbbbbRRRRRRòòòëëëËËËËËËËiiiiiiiibbbbbRRRRRòòòòòòäËËËËËËËpppppppbbbbb\023RRRRRòòòòòòäääÎÎÎÎÎppppppmmmmm\023\023\023\023RRòòòòòòòäääÎÎÎÎÎppppppmmmmm\023\023\023\023cccccSSSSä666ÎÎÎÎppppppmmmmmm\023\023ccccccSSSSOO666666pppppummmmm\030\030\030\030\030ccccSSSMOOO66666{uuuuuuuuuuv\030\030\030\030\030cccSMMMOOOO6666{uuuuuuuuuvvvvv\030||||MMMMOOOOOO66{uuuuuuuuuvvvvv|||||qqqqq\007\007\007³³³³uuuuuuuuuuvvvvv|||||qqqqNNNN³³³³}uuuu~~~~zzzzz|||||qqqqqNNNN³³³³}}~~~~~~zzzzzzz\035\035\035\035\035\035qqNNNNN³³³³\033\033\033\033~~~~zzzzzzz\035\035\035\035\035\035\035\035\b\b\b\b\b³³³³\033\033\033\033\033\033\033\033\033zzzzz\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b££\033\033\033\033\033\033\033\033\033\033\033zzz\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b££\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b£UUUUUUôôôôèèèèèâââââââââÑÑÑÑÑÑÑÑUUUUUþþ========âââââââââÑÑÑÑÑÑÑÑUUUUUþøøø=======ââââââââÑÑÑÑÑÑÑÑUUUUUøøøøø======ââââââââÑÑÑÑÑÑÑÑUUUUþøøøøøø====ãããããããÞÞÓ9ÑÑÑÑÑÑ]]]]]øøøøøøøø=ãããããããÞÞÞÞ9999999]]]]]ÿÿÿÿÿÿøøãããããããÞÞÞÞÞ9999999]]]]]ÿÿÿÿÿÿöööööééééÞÞÞÞÞ9999999]]]]\016\016\016ÿÿÿÿöööööééééééÝÝÝÝÐÐÐÐÐÐ]]\016\016\016\016\016\016ÿÿööööööéééééÝÝÝÝÝÐÐÐÐÐÐ\017\017```````QQöööööééééÝÝÝÝÝÝÐÐÐÐÐÐ\017\017```````QQQQQQQ?ééÝÝÝÝÝÝÐÐÐÐÐÐÐ\017\017```````QQQQQ????ëëëëëëëØØØËËËË\017i```````QQQQ?????ëëëëëëëËËËËËËËiiiiiiiibbbb??????ëëëëëëëËËËËËËËiiiiiiibbbbbbRRRRRRëëëëëëËËËËËËËiiiiiibbbbbbbRRRRRRòòòòääääËËËËËppppppbbbbbbbRRRRRRòòòòäääääÎÎÎÎppppppmmmm\023\023\023\023\023RRòòòòòääääääÎÎÎÎppppppmmmm\023\023\023\023\023ccccSSSääääääÎÎÎÎppppppmmmmm\023\023\023\023ccccSSSSSS6666666pppppuummmm\030\030\030\030ccccSSSSSO6666666puuuuuuuuuuvvvvccccSSSSSO6666666uuuuuuuuuuuvvvvvv|||SSSOOOO66666uuuuuuuuuuvvvvvvv||qqqqqqNNNN³³³wuuuuuuuuuvvvvvv|||qqqqqNNNNN³³³wwwuuuuzzzzzzzz|||qqqqqqNNNNN³³³wwwwwzzzzzzzzzzz\035\035\035qqqqNNNNNN³³³ww\033\033\033\033zzzzzzzzz\035\035\035\035\035\035\035\035\b\b\b\b\bN³³³\033\033\033\033\033\033\033\033zzzzzzz\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033zzzz\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\b\033\033\033\033\033\033\033\033\033\033\033\033\035\035\035\035\035\035\035\035\035\035\b\b\b\b\b\b\b\b\b\bUUUUUôôôôôôôôôâââââââââââÑÑÑÑÑÑÑUUUUUôôôôô=====âââââââââÑÑÑÑÑÑÑÖUUUUUøøøô=======ââââââââÑÑÑÖÖÖÖÖUUUUøøøøøø======ââââââââÑÑÑÖÖÖÖÖUUUU\013øøøøøø====<<<<<<ÞÞÞÞ9999ÖÖÖ]]\013\013\013\013øøøøøøø<<<<<<<ÞÞÞÞÞ9999999]]]]\013\013\013\013øøøøø<<<<<<<ÞÞÞÞÞ9999999]]]]\013\013\013\013\013üöööööööéééÞÞÞÞÞ9999999]]]]]\016\016üüöööööööééééééÝÝÝÞ99999Ð\017\017\017\017\016\016\016üüöööööööééééééÝÝÝØØØØØØÐ\017\017\017``````QööööööééééééÝÝÝØØØØØØØ\017\017\017``````QQQQQQQ?éééÝÝÝÝÝØØØØØØØ\017\017\017``````QQQQ?????ëëëëëëØØØØØØØØ\017\017```````QQQQ?????ëëëëëë::::::ËËii\021\021\021\021\021bbbbb??????ëëëëëë::::::ËËi\021\021\021\021\021\021bbbbbbRRRRRRëëëëë:::::ËËËp\021\021\021\021\021\021bbbbbbRRRRRRRòòääääääËËËÎpppp\021\021\021bbbbbbRRRRRRRòäääääääÎÎÎÎpppppppm\023\023\023\023\023\023\023RRR\r\r\räääääääÎÎÎÎpppppppm\023\023\023\023\023\023\023ff\r\r\r\rSääääääÎÎÎÎpppppppmm\023\023\023\023fffcccSSSSSS666666ÎppppuuuuuuffffffcccSSSSSS6666666puuuuuuuuuuvvffccccSSSSSS6666666uuuuuuuuuuuvvvvvvvcSSSSSq6ÔÔÔÔÔÔwuuuuuuuuuuvvvvvvvqqqqqqqNNÔÔÔÔÔwwwuuuuuuuvvvvvvvvqqqqqqNNNNNN³³wwwwwwwzzzzzzzzzvqqqqqqqNNNNNN³³wwwwwwzzzzzzzzzzzqqqqqqqNNNNNN³³wwwwwwzzzzzzzzzz\035\035\035\035\035\035\b\b\b\b\bPPPPPw\033\033\033\033\033\033\033zzzzzzzz\035\035\035\035\035\035\b\b\b\b\b\b\b\bPP\033\033\033\033\033\033\033\033\033\033zzzzz\035\035\035\035\035\035\035\b\b\b\b\b\b\b\bPP\033\033\033\033\033\033\033\033\033\033\033\033z\035\035\035\035\035\035\035\035y\b\b\b\b\b\b\b\b\b\bUUU÷÷ôôôôôôôôôâââââââââââÚÚÚÚÖÖÖUUUU÷ôôôôôôôô==âââââââââÚÚÖÖÖÖÖÖUUUUøøôôôôô=====ââââââââÚÚÖÖÖÖÖÖUUUUøøøøøøø=====ââââââââÚÚÖÖÖÖÖÖUU\013\013\013øøøøøøø==<<<<<<<ÞÞÞÞ99ÖÖÖÖÖ]\013\013\013\013\013\013øøøøøîî<<<<<<ÞÞÞÞÞ999999Ö]\013\013\013\013\013\013\013\013øøîîî<<<<<<ÞÞÞÞÞ9999999]]\013\013\013\013\013\013üüüüöööö<<ééÞÞÞÞÞ9999999]]]\013\013\013\013üüüüöööööéééééééÞÞÞ999999\017\017\017\017\017`üüüüüöööööéééééééáØØØØØØØØ\017\017\017\017`````üööööööéééééééáØØØØØØØØ\017\017\017\017`````VVVVV???éééééááØØØØØØØØ\017\017\017`````VVVVV?????ëëëëëëØØØØØØØØ\017\017\017`````VVVVV?????ëëëëëë::::::::\017\021\021\021\021\021\021\021VVVVV?????ëëëëëë::::::::\021\021\021\021\021\021\021\021bbbbbbRRRRëëëëë:::::::::p\021\021\021\021\021\021\021bbbbbbRRRRRRïïïïäää:::ÎÎpp\021\021\021\021\021\021bbbbb\023RRRRRïïïääääääÎÎÎÎpppp\021\021\021\021\023\023\023\023\023\023\023\r\r\r\r\r\rïääääääÎÎÎÎppppppkk\023\023\023\023\023\023ff\r\r\r\r\r\rääääääÎÎÎÎpppppkkkg\023\023\023fffff\r\rSSSSSS66666ÎÎppkkkuuuggggfffffccSSSSSS666ÔÔÔÔkkuuuuuuuuufffffcccSSSSSS6ÔÔÔÔÔÔuuuuuuuuuuunvvvvvccSSSSSqÔÔÔÔÔÔÔwuuuuuuuuunnnnnvvvqqqqqqqqÔÔÔÔÔÔwwwwuuuuuunnnnnnvqqqqqqqNNNNNNN³wwwwwwwzzzzzzznnnqqqqqqqNNNNNNNPwwwwwwwzzzzzzzzzzqqqqqqqNNNNNPPPwwwwwwwzzzzzzzzzz\035yyyyyyyPPPPPPPwwwwww\033zzzzzzzzzz\035yyyyyy\b\b\b\bPPPP\033\033\033\033\033\033\033\033\033zzzzzzzz\035yyyyy\b\b\b\b\b\b\bPP\033\033\033\033\033\033\033\033\033\033\033\033zz\035\035\035\035yyyyy\b\b\b\b\b\b\b\b\b÷÷÷÷÷÷÷ôôôôôôôôâââââââââÚÚÚÚÚÚÚÚU÷÷÷÷÷÷ôôôôôôô=âââââââââÚÚÚÚÚÚÖÖU÷÷÷÷÷ôôôôôôôô=ââââââââÚÚÚÚÚÖÖÖÖUU÷÷øøøøøøøø==<<<ââââââÚÚÚÚÚÖÖÖÖ\013\013\013\013\013\013øøøøøîîîî<<<<<<ÞÞÞÞÚÚÖÖÖÖÖ\013\013\013\013\013\013\013\013\013øøîîîîî<<<<<ÞÞÞÞÞ9999ÖÖ\013\013\013\013\013\013\013\013\013üîîîîîî<<<<ÞÞÞÞÞÞ999999\013\013\013\013\013\013\013\013üüüüüööîî<<<ÞÞÞÞÞÞ999999\013\013\013\013\013\013üüüüüüöööööéééééÞÞÞÞá99999\017\017\017\017\017üüüüüüüööööíéééééáááááØØØØØ\017\017\017\017\017``üüüööööööíéééééááááØØØØØØ\017\017\017\017\017ZZZVVVVVûûûûííííáááááØØØØØØ\017\017\017\017\017ZZZVVVVVûûûûûëëëëëáØØØØØØØØ\017\017\017\017ZZZZVVVVVûûûû?ëëëëë::::::::Ü\017\021\021\021\021\021\021\021VVVVVVûû??ëëëëë:::::::ÜÜ\021\021\021\021\021\021\021\021\021bbbbb???ëëëëëë:::::::ÜÜ\021\021\021\021\021\021\021\021\021bbbbXXXXXXïïïïïïää::ÜÜÜp\021\021\021\021\021\021\021\021bbbbXXXXXXïïïïïääääÎÎÎÎkkkkkkkk\021gg\023XXXX\r\r\r\rïïïäääääÎÎÎÎkkkkkkkkggggggff\r\r\r\r\rïïäääääÎÎÎÎkkkkkkkkgggggffff\r\r\r\rSSSääääÎÎÎÎkkkkkkkkgggggfffffSSSSSSSÔÔÔÔÔÔÔkkkuuuuuuggfffffffSSSSSSSÔÔÔÔÔÔÔuuuuuuuuunnnnnnnn[[[[[[qÔÔÔÔÔÔÔÔwwuuuuuuunnnnnnn\025\025\025\025qqqqqqÔÔÔÔÔÔwwwwwwuulnnnnnnn\025\025\025\025qqqqqNNNNN\t\twwwwwwwzzznnnnnn\025\025\025\025qqqqNNNNNN\t\twwwwwwwzzzzzzzz\027\027\025\025qqqqqNNNPPPPPwwwwwwwzzzzzzzz\027\027\027yyyyyyyyPPPPPPwwwwwwwzzzzzzzz\027\027\027yyyyyyyyPPPPPPwwww\033\033\033zzzz\027\027\027\027\027\027\027yyyyyyyy\b\bPPPP\033\033\033\033\033\033\033\033\033\033\027\027\027\027\027\027\027yyyyyyyy\b\b\b\b\b\n\n÷÷÷÷÷÷÷÷ôôôôôôôâââââââââÚÚÚÚÚÚÚÚ÷÷÷÷÷÷÷÷ôôôôôôôââââââââÚÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷ôôôôôôôôâââââââÚÚÚÚÚÚÚÖÖÖ\013÷÷÷÷÷ôôôôôôîî<<<<<<<<ÚÚÚÚÚÚÚÖÖÖ\013\013\013\013\013\013\013\013\013\013îîîîîî<<<<<<ÞÞÚÚÚÚÖÖÖÖ\013\013\013\013\013\013\013\013\013\013îîîîîî<<<<<ÞÞÞÞÞÞÖÖÖÖÖ\013\013\013\013\013\013\013\013\013üîîîîîî<<<<<ÞÞÞÞÞÞ99999\013\013\013\013\013\013\013\013üüüüüîîîî<<<ÞÞÞÞÞÞÞ99999\013\013\013\013\013\013üüüüüüüöööííííííáááááááØØØ\017\017\017\017\017üüüüüüüüööíííííííáááááØØØØØ\017\017\017\017\017ZZüüüüüöööíííííííáááááØØØØØ\017\017\017\017ZZZZZZZVûûûûíííííáááááØØØØØØ\017\017\017\017ZZZZZVVVVûûûûûûíëááááØØØØØØØ\017\017\017\017ZZZZZVVVVûûûûûûëëë:::::::ÜÜÜ\017\021\021\021\021\021ZZVVVVVûûûûûûëëë:::::::ÜÜÜ\021\021\021\021\021\021\021\021\021\021\021VVXXXXXXëëë::::::ÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïï:ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïääääÎÎÎkkkkkkkkkgggXXXXX\r\r\rïïïïïääääÎÎÎkkkkkkkkkgggggg\r\r\r\r\r\rïïïäääääÎÎÎkkkkkkkk\024gggggff\r\r\r\r\r\r[[ääääÔÎÎÎkkkkkkkk\024gggggfff[[[[[[[[ÔÔÔÔÔÔÔkkkkkk\024\024\024\024\024\024fffff[[[[[[[ÔÔÔÔÔÔÔÔkuuulllllllnnnnn[[[[[[[[ÔÔÔÔÔÔÔÔwwwwllllllnnnnnn\025\025\025\025\025\025\025\025qÔÔÔÔÔÔÔwwwwwwllllnnnnnn\025\025\025\025\025\025\025qq\t\t\t\t\t\t\twwwwwwwllnnnnnnn\025\025\025\025\025\025\025qq\t\t\t\t\t\t\twwwwwwwzzz\027\027\027\027\027\027\027\025\025\025\025\025\025qPPP\t\t\t\t\twwwwwwwzzz\027\027\027\027\027\027\027\027yyyyyyyyPPPPPPwwwwwwwzzz\027\027\027\027\027\027\027\027yyyyyyyyPPPPPPwwwwwwzzz\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\nP\033\033\033\033\033\033\033\027\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\n\n\n÷÷÷÷÷÷÷÷÷ôôôôôôôââââââÚÚÚÚÚÚÚÚÚÚ÷÷÷÷÷÷÷÷÷ôôôôôôôâââââÚÚÚÚÚÚÚÚÚÚÖ÷÷÷÷÷÷÷÷ôôôôôôôô<<<<<ÚÚÚÚÚÚÚÚÚÖÖ\013÷÷÷÷÷ôôôôôôîîî<<<<<<<ÚÚÚÚÚÚÚÚÖÖ\013\013\013\013\013\013\013\013\013îîîîîîîî<<<<<<ÚÚÚÚÚÚÖÖÖ\013\013\013\013\013\013\013\013\013\013îîîîîîî<<<<<ÞÞÚÚÚÚÖÖÖÖ\013\013\013\013\013\013\013\013\013üîîîîîîî<<<<<ÞÞÞÞÞÞÖÖÖÖ\013\013\013\013\013\013\013\013üüüüüîîîî<<<<<ÞÞÞÞÞÞ9999\013\013\013\013\013\013üüüüüüüüüíííííííáááááááØØØ\017\017\017\017\017üüüüüüüüüííííííííááááááØØØØ\017\017\017\017\017ZZZüüüüüüííííííííááááááØØØØ\017\017\017\017ZZZZZZZVûûûûííííííááááááØØØØ\017\017\017\017ZZZZZZVVVûûûûûûííáááááØØØØØØ\017\017\017\017ZZZZZZVVVûûûûûûûë:::::::ÜÜÜÜ\017\021\021\021\021ZZZVVVVVûûûûûûûë:::::ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021VVVXXXXXXXïï::::ÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïÜÜÜÜÜÜÜk\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïïääÜÜÜÜkkkkkkkkkgggXXXXXX\rïïïïïïïääääÎÎkkkkkkkk\024ggggggX\r\r\r\rïïïïïïääääÎÎkkkkkkkk\024\024\024gggg\r\r\r[[[[[[[äÔÔÔÔÔÔkkkkkkk\024\024\024\024\024gggg[[[[[[[[[ÔÔÔÔÔÔÔkkkkk\024\024\024\024\024\024\024\024\024gf[[[[[[[[[ÔÔÔÔÔÔÔkkklllllllllnnnn[[[[[[[[[ÔÔÔÔÔÔÔwwwwlllllllnnnnn\025\025\025\025\025\025\025\025\025ÔÔÔÔÔÔÔwwwwwwlllllnnnnn\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwwlllnnnnnn\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwww\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwww\027\027\027\027\027\027\027\027\027\027yyyyyyyyPPPPPPwwwwwwww\027\027\027\027\027\027\027\027\027\027yyyyyyyy\nPPPPPwwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\n\nwwwwwz\027\027\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\n\n\n÷÷÷÷÷÷÷÷÷÷÷ôôôôôôôââÚÚÚÚÚÚÚÚÚÚÚÚ÷÷÷÷÷÷÷÷÷÷÷ôôôôôôôââÚÚÚÚÚÚÚÚÚÚÚÚ÷÷÷÷÷÷÷÷÷÷ôôôôôô<<<<<ÚÚÚÚÚÚÚÚÚÚÚ÷÷÷÷÷÷÷÷ôôôîîîîîîî<<<<ÚÚÚÚÚÚÚÚÚÖ\013\013\013\013\013\013\013\013\013îîîîîîîîî<<<<ÚÚÚÚÚÚÚÚÚÖ\013\013\013\013\013\013\013\013\013îîîîîîîîî<<<<<ÚÚÚÚÚÚÚÖÖ\013\013\013\013\013\013\013\013üüîîîîîîîî<<<<<<ÚÚÚÖÖÖÖÖ\013\013\013\013\013\013\013üüüüüîîîîîîî<<<íááááááááÖ\013\013\013\013\013\013üüüüüüüüííííííííááááááááØØ\017\017\017\017\017üüüüüüüüüííííííííááááááááØØ\017\017\017\017ZZZZZZZüüüííííííííáááááááØØØ\017\017\017ZZZZZZZZZûûûûííííííáááááááØØØ\017\017\017ZZZZZZZZVûûûûûûûííááááááááÜÜÜ\017\017\017ZZZZZZZZVûûûûûûûûû::::ÜÜÜÜÜÜÜ\021\021\021ZZZZZZZVVûûûûûûûûû::::ÜÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021VVXXXXXXXXïïï::ÜÜÜÜÜÜÜ\021\021\021\021\021\021\021\021\021\021\021XXXXXXXXïïïïïïÜÜÜÜÜÜÜkk\021\021\021\021\021\021\021\021XXXXXXXXXïïïïïïïÜÜÜÜÜÜkkkkkkkkkgggXXXXXXXïïïïïïïïïääÜÜkkkkkkkk\024\024\024gggXXXXXïïïïïïïïïäääÎkkkkkkkk\024\024\024\024\024ggg[[[[[[[[[ïÔÔÔÔÔÔkkkkkkk\024\024\024\024\024\024ggg[[[[[[[[[ÔÔÔÔÔÔÔkkkkk\024\024\024\024\024\024\024\024\024g[[[[[[[[[[ÔÔÔÔÔÔÔkkkllllllllllnnn[[[[[[[[[ÔÔÔÔÔÔÔwwwwllllllllnnnn\025\025\025\025\025\025\025\025\025\t\t\t\t\t\t\twwwwwlllllllnnnn\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwllllnnnnnn\025\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwww\027\027\027\027\027\027\027\027\027\025\025\025\025\025\025\025\t\t\t\t\t\t\t\twwwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyyy\t\t\t\t\twwwwwwww\027\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\n\nwwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyy\n\n\n\n\n\n\nwwwwww\027\027\027\027\027\027\027\027\027\027\027yyyyyyyy\n\n\n\n\n\n\n".getBytes(StandardCharsets.ISO_8859_1)
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


    public static final int[] FANCY_MANOS64 = new int[256];
    
    public static final int[] FULL_GRAY = new int[256];
    static {
        System.arraycopy(MANOS64, 0, FANCY_MANOS64, 128, 64);
        for (int i = 1; i < 64; i++) {
            FANCY_MANOS64[i] = lighten(MANOS64[i], 0.1f);
            FANCY_MANOS64[i | 64] = darken(MANOS64[i], 0.3f);
            FANCY_MANOS64[i | 192] = darken(MANOS64[i], 0.15f);
        }
        for (int i = 1; i < 256; i++) {
            FULL_GRAY[i] = i * 0x01010100 | 0xFF;
        }
    }

    /**
     * Generated with a Halton sequence through Oklab color space, with increased gray coverage but fewer total colors,
     * to make room for special markers.
     */
    public static final int[] HALTONITE240 = new int[]{
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
            /*          0x48384840, 0x50405040, 0x58485840, 0x60506040, 0x68586840, 0x70607040, 0x78687840,
            0x80708040, 0x88788840, 0x90809040, 0x98889840, 0xA070A040, 0xA860A840, 0xB050B040, 0xB840B840,
             */
    };
}
