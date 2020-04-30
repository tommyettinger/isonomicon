package isonomicon.visual;

import isonomicon.io.PaletteReducer;

import java.nio.charset.StandardCharsets;

/**
 * Created by Tommy Ettinger on 11/4/2017.
 */
public class Coloring {
    // values with special meanings
    /** Empty space with nothing in it. */
    public static final byte EMPTY = 0,
    /** Used for shadows on the ground, only if nothing is at that space; does not produce outlines. */ SHADOW = 1,
    /** A solid black outline drawn around the perimeter of a 2D image; never used in 3D. */ OUTLINE = 2,
    /** A solid object that is transparent, so it will have an outline but no color of its own. */ CLEAR = 3;
    // 4, 5, 6, and 7 are reserved for later use.
    // all other bytes are organized so the bottom 3 bits determine shading, from darkest at 0 to lightest at 7, while
    // the top 5 bits are palette-dependent and are used to determine the precise hue and saturation.

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
     * Organized into chunks of 4 colors after the first 16, which include some special markers.
     * Lots of unassigned space; only 80 colors are used.
     */
    public static final int[] UNSEVEN = {
            0x00000000,
            0xfcfcfcff, 0xc3cbdbff, 0xa096d1ff, 0x62507eff, 0x424556ff, 0x252a32ff, 0x14161fff, 0x0a0b0fff,
            0x888c78ff, 0x585651ff, 0x453c3cff, 0x32222eff, 0xff8f8fff, 0xff2245ff, 0xd50964ff, 0x9c0565ff,
            0xffd800ff, 0xff9000ff, 0xe93100ff, 0xbf0000ff, 0xe5ff05ff, 0xa7ed00ff, 0x4ab907ff, 0x0a5d45ff,
            0x00fff0ff, 0x00b9ffff, 0x008df0ff, 0x1664c5ff, 0xffe822ff, 0xffa939ff, 0xe56335ff, 0xe5233eff,
            0xfffc00ff, 0xebb70aff, 0xbe8420ff, 0x915816ff, 0xffb35bff, 0xd77e4bff, 0xb15c51ff, 0x793d4eff,
            0xff70dfff, 0xff22a9ff, 0x611381ff, 0x45064bff, 0xccfff5ff, 0x6df7b1ff, 0x00c19aff, 0x017687ff,
            0x7bd5f3ff, 0x6c88ffff, 0x6440d8ff, 0x3d2e93ff, 0x85a3c7ff, 0x676cadff, 0x683395ff, 0x323751ff,
            0xff59beff, 0xc51aeaff, 0x6e10abff, 0x331685ff, 0xfb9585ff, 0xe97461ff, 0xb53772ff, 0x93278fff,
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
            0x140C1CFF,
            0x452434FF,
            0x30346DFF,
            0x4D494DFF,
            0x864D30FF,
            0x346524FF,
            0xD34549FF,
            0x757161FF,
            0x597DCFFF,
            0xD37D2CFF,
            0x8696A2FF,
            0x6DAA2CFF,
            0xD3AA9AFF,
            0x6DC3CBFF,
            0xDBD75DFF,
            0xDFEFD7FF,
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

    //    { // not-as-old, but still has some near-duplicate colors
//            0x00000000, 0x1F1833FF, 0x2B2E42FF, 0x3E3546FF,
//            0x414859FF, 0x68717AFF, 0x90A1A8FF, 0xB6CBCFFF,
//            0xD3E5EDFF, 0xFFFFFFFF, 0x5C3A41FF, 0x826481FF,
//            0x966C6CFF, 0xAB947AFF, 0xF68181FF, 0xF53333FF,
//            0x5A0A07FF, 0xAE4539FF, 0x8A503EFF, 0xCD683DFF,
//            0xFBA458FF, 0xFB6B1DFF, 0x9F8562FF, 0xF9C79FFF,
//            0xFFA514FF, 0xE8B710FF, 0xE3C896FF, 0xFBE626FF,
//            0xC0B510FF, 0xFBFF86FF, 0xB4D645FF, 0x729446FF,
//            0x91DB69FF, 0x358510FF, 0x51C43FFF, 0x0E4904FF,
//            0x4BA14AFF, 0x1EBC73FF, 0x30E1B9FF, 0x7FE0C2FF,
//            0xB8FDFFFF, 0x039F78FF, 0x63C2C9FF, 0x216981FF,
//            0x7FE8F2FF, 0x3B509FFF, 0x4D9BE6FF, 0x28306FFF,
//            0x5C76BFFF, 0x4D50D4FF, 0x180FCFFF, 0x53207DFF,
//            0x8657CCFF, 0xA884F3FF, 0x630867FF, 0xA03EB2FF,
//            0x881AC4FF, 0xE4A8FAFF, 0xB53D86FF, 0xF34FE9FF,
//            0x7A3045FF, 0xF04F78FF, 0xC27182FF, 0xC93038FF,
//    };
// // old FLESURRECT palette, does not have very dark green or red
//    {
//            0x00000000, 0x1F1833FF, 0x2B2E42FF, 0x3E3546FF,
//            0x414859FF, 0x68717AFF, 0x90A1A8FF, 0xB6CBCFFF,
//            0xD3E5EDFF, 0xFFFFFFFF, 0x5C3A41FF, 0x826481FF,
//            0x966C6CFF, 0xAB947AFF, 0xF68181FF, 0xF53333FF,
//            0xFF5A4AFF, 0xAE4539FF, 0x8A503EFF, 0xCD683DFF,
//            0xFBA458FF, 0xFB6B1DFF, 0x9F8562FF, 0xFCBF8AFF,
//            0xFF9E17FF, 0xF0B628FF, 0xE3C896FF, 0xFBE626FF,
//            0xEDD500FF, 0xFBFF86FF, 0xB4D645FF, 0x729446FF,
//            0x91DB69FF, 0x358510FF, 0x51C43FFF, 0x4BA14AFF,
//            0x1EBC73FF, 0x30E1B9FF, 0x7FE0C2FF, 0xB8FDFFFF,
//            0x039F78FF, 0x63C2C9FF, 0x4F83BFFF, 0x216981FF,
//            0x7FE8F2FF, 0x3B509FFF, 0x4D9BE6FF, 0x28306FFF,
//            0x4870CFFF, 0x4D50D4FF, 0x180FCFFF, 0x53207DFF,
//            0x8657CCFF, 0xA884F3FF, 0x630867FF, 0xA03EB2FF,
//            0x8032BCFF, 0xE4A8FAFF, 0xB53D86FF, 0xF34FE9FF,
//            0x7A3045FF, 0xF04F78FF, 0xC27182FF, 0xC93038FF,
//    };
//

    public static final PaletteReducer FLESURRECT_REDUCER = new PaletteReducer(FLESURRECT, (
            "\001\001\001\001\001\001\001\001\001\001\001000000043333333333333\001\001\001\001\001\001\001\001\001\001\001000000003333333333333\001\001\001\001\001\001\001\001\001\001\002000000003333333333333\001\001\001\001\001\001\001\001\001\001\002000000000333333333333"+
                    "\001\001\001\001\001\001\001\001\001\002\002000000000333333333333$\001\001\001\001\001\001\001\002\002\002000000000333333333333$$$\001\001\001\001\002\002\002\002000000000333333333333$$$$$\002\002\002\002\002\0020000000,,22333333333."+
                    "$$$$$$\002\002\002\002\002000000,,,2222333333..$$$$$$$\002\002\002\002\004000,,,,,,2222233....$$$$$$$$\002\004\004\00400,,,,,,,222222.....$$$$$$$$\004\004\004\004,,,,,,,,,222222....."+
                    "$$$$$$$$\004\004\004,,,,,,,,,,,2222......$$$$$$$\004,,,,,,,,,,,,,,1222......$$$$$$$,,,,,,,,,,,,,,*1111......$$$$$,,,,,,,,,,,,,,****111//...."+
                    "$$$$********,,,,,******11///////$ **********************1/////// ***********************////////##**********************////////"+
                    "####********************////////#####&&****************&'///////######&&&&&&&&&&&&&&&&&&'''/////######&&&&&&&&&&&&&&&&&''''''///"+
                    "#######&&&&&&&&&&&&&&''''''''''/#######&&&&&&&&&&&&&''''''''''''\"\"\"\"\"###&&&&&&&&&&''''''''''''''\"\"\"\"\"\"\"\"\"&&&&&&&''''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"&&%%'''''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%''''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%'''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%''''''''''''''"+
                    "\001\001\001\001\001\001\001\001\001\001\001000000043333333333333\001\001\001\001\001\001\001\001\001\001\001000000043333333333333\001\001\001\001\001\001\001\001\001\001\002000000004333333333333\001\001\001\001\001\001\001\001\001\002\002000000004333333333333"+
                    "\001\001\001\001\001\001\001\001\001\002\002000000000333333333333$\001\001\001\001\001\001\001\002\002\002000000000333333333333$$$\001\001\001\001\002\002\002\002000000000233333333333$$$$$\002\002\002\002\002\00200000000,22233333333."+
                    "$$$$$$\002\002\002\002\002000000,,,222223333...$$$$$$$\002\002\002\004\004000,,,,,,2222223....$$$$$$$$\002\004\004\00400,,,,,,,222222.....$$$$$$$$\004\004\004\004\004,,,,,,,,222222....."+
                    "$$$$$$$\004\004\004\004\004,,,,,,,,,,2222......$$$$$$$\004\004,,,,,,,,,,,,,1111......$$$$$$,,,,,,,,,,,,,,,,1111......$$$$$,,,,,,,,,,,,,,,**1111//...."+
                    "$$$$******,,,,,,,,*****11//////.   ********************11///////   ********************1////////###*********************////////"+
                    "#####*******************////////######&&&&*************&'///////#######&&&&&&&&&&&&&&&&&'''/////#######&&&&&&&&&&&&&&&&''''''///"+
                    "########&&&&&&&&&&&&&''''''''''/########&&&&&&&&&&&&''''''''''''\"\"\"\"\"###&&&&&&&&&&''''''''''''''\"\"\"\"\"\"\"\"\"&&&&&&&''''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%''''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%'''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%''''''''''''''"+
                    "\001\001\001\001\001\001\001\001\001\001\001000000444333333333333\001\001\001\001\001\001\001\001\001\001\001000000444333333333333\001\001\001\001\001\001\001\001\001\001\002000000044333333333333\001\001\001\001\001\001\001\001\001\002\002000000044333333333333"+
                    "\001\001\001\001\001\001\001\001\002\002\002000000004333333333333$\001\001\001\001\001\001\001\002\002\002000000000233333333333$$$\001\001\001\001\002\002\002\002000000000223333333333$$$$$\002\002\002\002\002\00200000000,2222333333.."+
                    "$$$$$$\002\002\002\002\002\00400000,,,222222333...$$$$$$\002\002\002\002\004\004000,,,,,22222222....$$$$$$$\002\004\004\004\004\0040,,,,,,,222222.....$$$$$$$\004\004\004\004\004\004,,,,,,,,222222....."+
                    "$$$$$$$\004\004\004\004\004,,,,,,,,,22222......$$$$$$\004\004\004,,,,,,,,,,,,11111......$$$$$$\004,,,,,,,,,,,,,,11111......$$$$$,,,,,,,,,,,,,,,**1111//...."+
                    "$$$  **,,,,,,,,,,,****111//////.     ******************11///////    *******************1////////###********************1////////"+
                    "#####******************&////////#######&&&&&&*********&&'///////#######&&&&&&&&&&&&&&&&'''//////########&&&&&&&&&&&&&&&'''''////"+
                    "########&&&&&&&&&&&&&'''''''''//#########&&&&&&&&&&'''''''''''''\"\"\"\"#####&&&&&&&&&''''''''''''''\"\"\"\"\"\"\"\"\"#&&&&&&''''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%''''''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%'''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%''''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%%'''''''''''''"+
                    "\001\001\001\001\001\001\001\001\001\001\001000004444333333333333\001\001\001\001\001\001\001\001\001\001\002000004444333333333333\001\001\001\001\001\001\001\001\001\001\002000000444333333333333\001\001\001\001\001\001\001\001\001\002\002000000044333333333333"+
                    "\001\001\001\001\001\001\001\001\002\002\002000000044333333333333\001\001\001\001\001\001\001\002\002\002\002000000004233333333333$$$\001\001\001\001\002\002\002\00200000000422233333333.$$$$\002\002\002\002\002\002\00200000000,2222233333.."+
                    "$$$$$\002\002\002\002\002\004\00400000,,,22222223....$$$$$$\002\002\002\002\004\004000,,,,,22222222....$$$$$$$\004\004\004\004\004\0040,,,,,,,222222.....$$$$$$$\004\004\004\004\004\004,,,,,,,,222222....."+
                    "$$$$$$\004\004\004\004\004\004\004,,,,,,,,22222......$$$$$$\004\004\004\004,,,,,,,,,,,11111......$$$$$$\004,,,,,,,,,,,,,,11111......$$$$$,,,,,,,,,,,,,,,,11111//...."+
                    "$     ,,,,,,,,,,,,,***111//////.      ****************111///////     *****************111///////     ******************1////////"+
                    "######*****************+////////#######&&&&&&&&&&*****&++///////########&&&&&&&&&&&&&&&+''//////########&&&&&&&&&&&&&&&'''''////"+
                    "#########&&&&&&&&&&&&'''''''''//#########&&&&&&&&&&'''''''''''''\"\"\"\"######&&&&&&&&''''''''''''''\"\"\"\"\"\"\"\"\"#&&&&&&''''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%%'''''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%%''''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%'''''''''''''\"\"\"\"\"\"\"\"\"\"\"\"%%%%%%%'''''''''''''"+
                    "\001\001\001\001\001\001\001\001\001\001\001770044444333333333333\001\001\001\001\001\001\001\001\001\001\002000004444333333333333\001\001\001\001\001\001\001\001\001\002\002000004444333333333333\001\001\001\001\001\001\001\001\001\002\002000000444333333333333"+
                    "\001\001\001\001\001\001\001\001\002\002\002000000444233333333333\001\001\001\001\001\001\001\002\002\002\002000000044222333333333$$\001\001\001\001\002\002\002\002\00200000004422223333333.$$$$\002\002\002\002\002\002\002\00400000002222222333..."+
                    "$$$$$\002\002\002\002\002\004\00400000,,,22222222....$$$$$$\002\002\002\003\004\004\004000,,,,22222222....$$$$$$\003\004\004\004\004\004\0040,,,,,,2222222.....$$$$$$\004\004\004\004\004\004\004,,,,,,,,222222....."+
                    "$$$$$$\004\004\004\004\004\004\004,,,,,,,,22222......$$$$$$\004\004\004\004,,,,,,,,,,,11111......$$$$$\004\004\004,,,,,,,,,,,,,11111......$$$$  ,,,,,,,,,,,,,,,11111//...."+
                    "        ,,,,,,,,,,,**11111/////.        ********,,***1111///////       ***************111///////      ****************11////////"+
                    "#######***************&+////////########&&&&&&&&&&&&&&&++///////#########&&&&&&&&&&&&&&+++//////#########&&&&&&&&&&&&&++'''/////"+
                    "##########&&&&&&&&&&&''''''''///##########&&&&&&&&&''''''''''''/\"\"\"\"#######&&&&&&'''''''''''''''\"\"\"\"\"\"\"\"\"##&&&%%''''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%%'''''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%%''''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%'''''''''''''\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%''''''''''''"+
                    "\001\001\001\001\001\001\001\001\001\001\001777444444333333333333\001\001\001\001\001\001\001\001\001\001\002700044444333333333333\001\001\001\001\001\001\001\001\001\002\002000044444333333333333\001\001\001\001\001\001\001\001\002\002\002000004444333333333333"+
                    "\001\001\001\001\001\001\001\001\002\002\002000004444223333333333\001\001\001\001\001\001\001\002\002\002\002000000444222333333333$$\001\001\001\001\002\002\002\002\002\004000000442222233333..$$$$\002\002\002\002\002\002\003\00400000042222222233..."+
                    "$$$$$\002\002\002\002\003\004\004\0040000,,,22222222....$$$$$\003\003\003\003\003\004\004\004000,,,,22222222....$$$$$$\003\004\004\004\004\004\004\004,,,,,,2222222.....$$$$$$\004\004\004\004\004\004\004,,,,,,,2222222....."+
                    "$$$$$\004\004\004\004\004\004\004\004,,,,,,,,22222......$$$$$\004\004\004\004\004\004,,,,,,,,,,11111......$$$$$\004\004\004,,,,,,,,,,,,,11111......$$$    ,,,,,,,,,,,,,111111//...."+
                    "        ,,,,,,,,,,,,111111/////.         *****,,,,***1111///////         ************1111///////        **************11////////"+
                    "########**************+++///////#########&&&&&&&&&&&&&+++///////#########&&&&&&&&&&&&&++++//////##########&&&&&&&&&&&&++++'/////"+
                    "##########&&&&&&&&&&'''''''''///###########&&&&&&&&''''''''''''/\"\"\"\"#######&&&&&&'''''''''''''''\"\"\"\"\"\"\"\"####%%%%%'''''''''''''''"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%%%'''''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%''''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%'''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%'''''''''''-"+
                    "\001\001\001\001\001\001\001\001\001\0017777744444333333333333\001\001\001\001\001\001\001\001\001\001\002777444444333333333333\001\001\001\001\001\001\001\001\001\002\002700044444333333333333\001\001\001\001\001\001\001\001\002\002\002000044444233333333333"+
                    "\001\001\001\001\001\001\001\001\002\002\002000004444222333333333\001\001\001\001\001\001\001\002\002\002\00300000444422223333333.$$\001\001\001\002\002\002\002\003\003\004000004442222223333..$$$\002\002\002\002\002\002\003\003\0040000044222222222...."+
                    "$$$$\002\002\003\003\003\003\004\004\0040000,,222222222....$$$$$\003\003\003\003\003\004\004\004000,,,,2222222.....$$$$$\003\004\004\004\004\004\004\004\004,,,,,,2222222.....$$$$$\004\004\004\004\004\004\004\004\004,,,,,,2222222....."+
                    "$$$$$\004\004\004\004\004\004\004\004,,,,,,,,11222......$$$$$\004\004\004\004\004\004\004,,,,,,,,111111......$$$$\r\r\r\004\004,,,,,,,,,,,111111......$$      ,,,,,,,,,,,,111111/....."+
                    "         ,,,,,,,,,,,111111////..           *,,,,,,,*11111///////          ***********1111///////          ***********111////////"+
                    "#########&&&*********&+++///////##########&&&&&&&&&&&&+++///////##########&&&&&&&&&&&+++++//////###########&&&&&&&&&&++++++/////"+
                    "###########&&&&&&&&&'''++''''///############&&&&&&&'''''''''''//\"\"\"#########&&&&&''''''''''''''-\"\"\"\"\"\"\"\"####%%%%%''''''''''''''-"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%%%'''''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%''''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%'''''''''''-\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%'''''''''--"+
                    "\021\021\001\001\001\001\001\001\001\0017777744444333333333333\021\001\001\001\001\001\001\001\001\0017777444444433333333333\021\001\001\001\001\001\001\001\001\002\002777444444433333333333\021\001\001\001\001\001\001\001\002\002\002000444444423333333333"+
                    "\001\001\001\001\001\001\001\002\002\002\003\00300044444222233333333$\001\001\001\001\002\002\002\002\003\003\0030004444422222333333.$$\001\002\002\002\002\002\003\003\003\00400004444222222233...$$$\002\002\002\002\003\003\003\003\004\004000044222222222...."+
                    "$$$$\003\003\003\003\003\003\004\004\00400004,222222222....$$$$\003\003\003\003\003\004\004\004\004\00400,,,,2222222.....$$$$$\003\004\004\004\004\004\004\004\004,,,,,,2222222.....$$$$$\004\004\004\004\004\004\004\004\004,,,,,,2222222....."+
                    "$$$$\004\004\004\004\004\004\004\004\004,,,,,,,111111......$$$$\004\004\004\004\004\004\004\004,,,,,,,,111111......$$$\r\r\r\r\r\r\r,,,,,,,,,,111111......        \r,,,,,,,,,,,111111/....."+
                    "           ,,,,,,,,,111111////..            ,,,,,,,*11111///////            ********11111///////           **********111////////"+
                    "######### &&&&&&*****++++///////##########&&&&&&&&&&&+++++//////###########&&&&&&&&&&+++++//////###########&&&&&&&&&+++++++/////"+
                    "############&&&&&&&&+++++++'////############&&&&&&''''''''''''/-\"\"\"##########&&&%'''''''''''''--\"\"\"\"\"\"\"\"####%%%%%%''''''''''''--"+
                    "\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%'''''''''''--\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%''''''''''--\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%''''''''---\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%'''''''---"+
                    "\021\021\021\021\021\001\001\001\001\0017777744444433333333333\021\021\021\021\001\001\001\001\001\0027777744444433333333333\021\021\021\021\001\001\001\001\001\0027777444444433333333333\021\021\021\021\001\001\001\002\002\003\003774444444422333333333"+
                    "\021\021\021\001\002\002\002\002\003\003\003\00300444444222223333333\021\021\002\002\002\003\003\003\003\003\003\003004444442222222333..$$\002\003\003\003\003\003\003\003\003\00400044444222222223...$$$\003\003\003\003\003\003\003\003\004\004000444422222222...."+
                    "$$$\003\003\003\003\003\003\003\004\004\004\00400444222222222....$$$$\003\003\003\003\003\004\004\004\004\00400,,,22222222.....$$$$\n\n\004\004\004\004\004\004\004\004\004,,,,,2222222.....$$$$\n\n\004\004\004\004\004\004\004\004,,,,,,2222222....."+
                    "$$$\n\n\004\004\004\004\004\004\004\004\004,,,,,,111111......$$$\r\r\r\r\r\r\r\r\004\005\005\005,,,,,111111......$$\r\r\r\r\r\r\r\r\r\005\005\005\005,,,,,1111111.....         \005\005\005\005\005\005\005,,,,1111111....."+
                    "           \005\005\005\005\005,,,,111111////..             \005\005\005,,,1111111//////              ******11111///////             *******11111///////"+
                    "#####       &&&&&&&&*++++///////###########&&&&&&&&&++++++//////############&&&&&&&&+++++++/////############&&&&&&&&+++++++/////"+
                    "#############&&&&&&+++++++++////#############&&&&&'''''++++'''--\"\"\"##########%%%%%'''''''''''---\"\"\"\"\"\"\"\"###%%%%%%%%''''''''''---"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%'''''''''---\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%'''''''''---\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%'''''''----\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%''''''----"+
                    "\021\021\021\021\021\021\021\021777777744444433333333333\021\021\021\021\021\021\021\001\00277777744444493333333333\021\021\021\021\021\021\021\002\003\0037777444444423333333333\021\021\021\021\021\021\003\003\003\003\003777444444422233333333"+
                    "\021\021\021\021\003\003\003\003\003\003\003\n7444444422222233333.\021\021\021\021\003\003\003\003\003\003\003\n004444442222222233..\021\021\021\003\003\003\003\003\003\003\003\004\0040444444222222222...$$\003\003\003\003\003\003\003\003\004\004\004004444422222222...."+
                    "$$$\003\003\003\003\003\003\003\004\004\004\00404444222222222....$$$\n\003\003\003\003\003\004\004\004\004\004004,,22222222.....$$$\n\n\n\n\004\004\004\004\004\004\004\004\005,,,22222222.....$$$\n\n\n\n\004\004\004\004\004\004\004\005\005,,,,2222222....."+
                    "$$\n\n\n\n\004\004\004\004\004\004\004\005\005\005\005,,,111111......$$\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005,,,111111......$\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\005,,1111111.....         \005\005\005\005\005\005\005\005\005\00511111111....."+
                    "           \005\005\005\005\005\005\005\0051111111////..             \005\005\005\005\005\0051111111//////               \005\005\005\005\00611111///////               ****\00611111///////"+
                    "             &&&&&&\006\006++++///////############&&&&&&&&++++++//////############&&&&&&&&+++++++/////#############&&&&&&+++++++++////"+
                    "#############&&&&&&+++++++++////##############&&&&''+++++++++---\"\"###########%%%%%''''''''+''---\"\"\"\"\"\"\"####%%%%%%%%''''''''''---"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%''''''''----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%'''''''----\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%'''''-----\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%'''------"+
                    "\021\021\021\021\021\021\021\021777777774444499933333333\021\021\021\021\021\021\021\021777777744444499933333333\021\021\021\021\021\021\021\021777777444444499993333333\021\021\021\021\021\021\021\003\003\0037777444444422993333333"+
                    "\021\021\021\021\021\n\n\n\n\003\003\n7444444422222223333.\021\021\021\021\n\n\n\n\n\n\003\n\n4444444222222223...\021\021\021\n\n\n\n\n\n\n\n\n\004444444422222222....\021\021\n\n\n\n\n\n\n\n\n\004\004\00444444422222222...."+
                    "$\n\n\n\n\n\n\n\n\n\004\004\004\00404444222222222....$\n\n\n\n\n\n\n\n\004\004\004\004\004\r444\01322222222.....$$\n\n\n\n\n\n\004\004\004\004\004\004\r\005\005\005\01322222222.....$$\n\n\n\n\n\n\004\004\004\004\004\r\005\005\005\005\005\013222222......"+
                    "$\n\n\n\n\n\r\r\r\r\r\r\r\005\005\005\005\005\005\005111111......$\r\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\00511111111.....\r\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\005\00511111111.....         \r\005\005\005\005\005\005\005\005\00511111111....."+
                    "           \005\005\005\005\005\005\005\0051111111////..             \005\005\005\005\005\0051111111//////               \005\005\005\005\00611111///////                 \006\006\006\0061111///////"+
                    "               &&\006\006\006\006\006++++//////#############&&&&&&\006++++++//////#############&&&&&&++++++++/////##############&&&&++++++++++////"+
                    "##############&&&&++++++++++///-###############&&%+++++++++++---\"\"###########%%%%%%''''+++++----\"\"\"\"\"\"\"####%%%%%%%%%''''''''----"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%''''''-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%'''''-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%'''------\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%'-------"+
                    "\021\021\021\021\021\021\0217777777774444999999933333\021\021\021\021\021\021\0217777777744444999999993333\021\021\021\021\021\021\021\021777777744444999999993333\021\021\021\021\021\021\n\n\n77777444444999999999333"+
                    "\021\021\021\021\021\n\n\n\n\n\n7744444442222999993..\021\021\021\021\n\n\n\n\n\n\n\n\n4444444222222222...\021\021\021\n\n\n\n\n\n\n\n\n\n444444422222222....\021\n\n\n\n\n\n\n\n\n\n\n\004\00444444422222222...."+
                    "\n\n\n\n\n\n\n\n\n\n\n\004\004\00444444222222222....\n\n\n\n\n\n\n\n\n\n\004\004\004\r\r\r\013\013\01322222222.....\n\n\n\n\n\n\n\n\n\004\004\004\004\r\r\005\005\013\013\0132222222.....\n\n\n\n\n\n\n\n\n\004\004\004\r\r\005\005\005\005\013\0132222225....."+
                    "\n\n\n\n\n\r\r\r\r\r\r\r\r\r\005\005\005\005\005\013111111......\r\r\r\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\0131111111.....\023\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\005\005\0051111111.....         \r\005\005\005\005\005\005\005\005\00511111111....."+
                    "            \005\005\005\005\005\005\0051111111///...              \005\005\005\005\005\006111111//////                \005\005\005\006\0061111///////                \006\006\006\006\006\00611+///////"+
                    "                \006\006\006\006\006\006++++//////##############&&&\006\006\006\006\006+++++/////##############&&&&+++++++++/////###############&&&++++++++++////"+
                    "###############&&++++++++++++---################%%++++++++++----\"############%%%%%%'++++++++----\"\"\"\"\"\"\"####%%%%%%%%%''((((((----"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%'(((((-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%((-------\"\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%--------"+
                    "\021\021\021\021\021\021\0217777777774444999999999993\021\021\021\021\021\021\0217777777744444999999999993\021\021\021\021\021\021\0217777777744444999999999999\021\021\021\021\021\021\n\n77777744444499999999999."+
                    "\021\021\021\021\021\n\n\n\n\n\n7774444449999999999..\021\021\021\021\n\n\n\n\n\n\n\n74444444222299999...\021\021\021\n\n\n\n\n\n\n\n\n\n444444422222222....\021\n\n\n\n\n\n\n\n\n\n\n\n444444422222222...."+
                    "\n\n\n\n\n\n\n\n\n\n\n\n\004\r\r4444222222225....\n\n\n\n\n\n\n\n\n\n\n\r\r\r\r\013\013\013\013\01322222255....\n\n\n\n\n\n\n\n\n\r\r\r\r\r\r\005\013\013\013\0132222255.....\n\n\n\n\n\n\r\r\r\r\r\r\r\r\005\005\013\013\013\0131122555....."+
                    "\n\n\n\n\r\r\r\r\r\r\r\r\r\r\005\005\005\013\013\0131111115.....\r\r\r\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\013\0131111111.....\023\023\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\005\005\0131111111.....         \r\005\005\005\005\005\005\005\005\005\0051111111....."+
                    "            \005\005\005\005\005\005\005\0061111111//...              \005\005\005\005\005\006\00611111//////                \005\005\006\006\006\0061111//////                \006\006\006\006\006\006\0061+///////"+
                    "               \006\006\006\006\006\006\006\006+++//////###########    \006\006\006\006\006\006\006+++++/////###############&\006\006\006\006++++++++////################&+++++++++++////"+
                    "################&++++++++++++---###############%%+++++++++++----\"############%%%%%%((((++++(----\"\"\"\"\"\"#####%%%%%%%%%((((((((----"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%((((((-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%(((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%(--------"+
                    "\021\021\021\021\021\021\0217777777774444999999999999\021\021\021\021\021\021\0217777777774444999999999999\021\021\021\021\021\021=7777777744444999999999999\021\021\021\021\021\021===77777744444999999999999"+
                    "\021\021\021\021=======77744444499999999999.\021\021\021\021=========44444448999999999..\021\021\n\n\n\n\n======4444444822229995...\021\n\n\n\n\n\n\n\n\n\n===444448222222555..."+
                    "\n\n\n\n\n\n\n\n\n\n\r\r\r\r\r4\013\013\013822222555....\n\n\n\n\n\n\n\n\r\r\r\r\r\r\r\013\013\013\013\01322222555....\n\n\n\n\n\n\r\r\r\r\r\r\r\r\r\013\013\013\013\01322225555....\n\n\n\r\r\r\r\r\r\r\r\r\r\r\r\013\013\013\013\01311155555...."+
                    "\023\023\r\r\r\r\r\r\r\r\r\r\r\r\005\005\013\013\013\0131111155.....\023\r\r\r\r\r\r\r\r\r\r\r\r\005\005\005\005\013\013\0131111115.....\023\023\r\r\r\r\r\r\r\r\r\r\005\005\005\005\005\005\013\0131111111.....         \r\005\005\005\005\005\005\005\005\005\0131111111....."+
                    "            \005\005\005\005\005\005\005\0061111111//...              \005\005\005\005\005\006\00611111//////                \005\006\006\006\006\0061111//////                \006\006\006\006\006\006\0061++//////"+
                    "               \006\006\006\006\006\006\006\006+++//////######         \006\006\006\006\006\006\006+++++/////###############\006\006\006\006\006\006+++++++////################\006+++++++++++//--"+
                    "################++++++++++++----###############%%+++++++++++----\"############%%%%%((((((((((----\"\"\"\"\"\"#####%%%%%%%%(((((((((----"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%(((((((-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%(((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%(((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%%(--------"+
                    "\021\021\021\021\021\02177777777774444999999999999\021\021\021\021\021\021=7777777774444999999999999\021\021\021\021\021====77777744444999999999999\021\021\021\021=======777744444999999999999"+
                    "\021\021\021==========444444499999999999.\021\021===========44444448999999999..\021=============444448889999995...==============444448882225555..."+
                    "===========\r\r\r\r\013\013\013\0138882255555...\023\023\023\023\023==\r\r\r\r\r\r\r\r\013\013\013\013\01388255555....\023\023\023\023\023\023\r\r\r\r\r\r\r\r\r\013\013\013\013\01388555555....\023\023\023\023\023\r\r\r\r\r\r\r\r\r\r\013\013\013\013\01381555555...."+
                    "\023\023\023\023\023\r\r\r\r\r\r\r\r\r\005\013\013\013\013\01311115555....\023\023\023\023\r\r\r\r\r\r\r\r\r\005\005\005\013\013\013\0131111155.....\023\023\023\r\r\r\r\r\r\r\r\r\005\005\005\005\005\013\013\0131111115.....         \r\005\005\005\005\005\005\005\005\013\0131111111....."+
                    "            \005\005\005\005\005\005\005\006\006111111//...              \005\005\005\005\006\006\006\0061111/////6                \006\006\006\006\006\006\006111//////                \006\006\006\006\006\006\006\006++//////"+
                    "               \006\006\006\006\006\006\006\006++++/////##            \006\006\006\006\006\006\006\006\006++++/////##############\006\006\006\006\006\006\006\006++++++////###############\006\006\006\006++++++++++---"+
                    "################++++++++++++----###############%%(+++++++++(----#############%%%%%((((((((((----\"\"\"\"\"\"#####%%%%%%%((((((((((----"+
                    "\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%(((((((-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%((((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%((((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%%((--------"+
                    "\021\021\021\021\021==7777777777449999999999999\021\021\021\021=====77777774444999999999999\021\021\021\021=======777774444999999999999\021\021\021=========77444444999999999999"+
                    "\021\021============44444899999999999.\021=============4444488999999999..==============4444488899999955..===============444\0138888855555..."+
                    "\023\023\023\023\023=======\r\r\r\013\013\013\0138888555555...\023\023\023\023\023\023\023\023\023\r\r\r\r\r\013\013\013\013\0138888555555...\023\023\023\023\023\023\023\023\023\r\r\r\r\r\013\013\013\013\013\013885555555...\023\023\023\023\023\023\023\023\r\r\r\r\r\r\013\013\013\013\013\01388555555...."+
                    "\023\023\023\023\023\023\023\023\r\r\r\r\r\013\013\013\013\013\013\01311155555....\023\023\023\023\023\023\023\r\r\r\r\r\r\005\013\013\013\013\013\01311115555....\023\023\023\023\023\023\r\r\r\r\r\r\005\005\005\005\013\013\013\01311111555....         \r\r\005\005\005\005\005\005\013\013\013\006111115....6"+
                    "            \005\005\005\005\005\005\013\006\006\00611111/6666              \005\005\005\005\006\006\006\006\006111///666                \006\006\006\006\006\006\006\00611////66               \006\006\006\006\006\006\006\006\006++/////6"+
                    "              \016\006\006\006\006\006\006\006\006\006+++/////\035\035           \016\016\006\006\006\006\006\006\006\006+++++////\035\035############\006\006\006\006\006\006\006\006\006+++++///-\035##############\006\006\006\006\006+++++++++---"+
                    "\035###############+++++++++++(----###############%((((((++++((----#############%%%%(((((((((((----\"\"\"\"\"\037\037\037\037\037\037\037%%%%%%((((((((((----"+
                    "\"\"\"\"\"\"\"\"\037\037%%%%%%%%%((((((((-----\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%((((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%((((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%(((--------"+
                    "\021\021\021\021=====77777777449999999999999\021\021\021========777774449999999999999\021\021===========7444449999999999999\021=============444448999999999999"+
                    "==============444448899999999999===============4444888999999999.===============444888889999955..\023\023\023\023===========\013\013\013888888555555.."+
                    "\023\023\023\023\023\023\023\023\023\023\023\023==\013\013\013\013\01388888555555..\023\023\023\023\023\023\023\023\023\023\023\023\023\013\013\013\013\013\0138888555555...\023\023\023\023\023\023\023\023\023\023\023\023\r\013\013\013\013\013\013\013888555555...\023\023\023\023\023\023\023\023\023\023\023\023\013\013\013\013\013\013\013\013885555555..."+
                    "\023\023\023\023\023\023\023\023\023\023\023\r\013\013\013\013\013\013\013\013885555555...\023\023\023\023\023\023\023\023\023\023\r\f\013\013\013\013\013\013\013\01311155555..66\023\023\023\023\023\023\023\023\023\f\f\f\f\013\013\013\013\013\013\013\00611155556666           \f\f\013\013\013\013\013\013\006\006\0061115566666"+
                    "            \f\005\005\005\005\013\013\006\006\006\006111166666              \f\f\005\013\006\006\006\006\006\00611166666               \016\006\006\006\006\006\006\006\006\0061/66666              \016\016\006\006\006\006\006\006\006\006\006+//6666"+
                    "\035            \016\016\016\006\006\006\006\006\006\006\006+++//666\035\035\035\035        \016\016\016\006\006\006\006\006\006\006\006\006++++//66\035\035\035\035########\016\016\016\006\006\006\006\006\006\006\006+++++/---\035\035\035\035##########\016\006\006\006\006\006\006+++++++----"+
                    "\035\035\035############\037\006++++++++++(----\035\035####\037\037\037\037\037\037\037\037\037\037((((((((((((----\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037%((((((((((((----\"\037\037\037\037\037\037\037\037\037\037\037\037%%%%(((((((((((----"+
                    "\"\"\"\"\"\037\037\037\037\037\037\037%%%%%%(((((((((-----\"\"\"\"\"\"\"\"\037\037%%%%%%%%%(((((((------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%(((((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%%(((--------"+
                    "\021\021=========777774449999999999999\021===========77744449999999999999==============444449999999999999==============444448999999999999"+
                    "===============44488899999999999===============4448888999999999.\023\023\023\023============448888899999955.\023\023\023\023\023\023\023\023\023\023\023====\013\013\0138888889555555."+
                    "\023\023\023\023\023\023\023\023\023\023\023\023\023\023\013\013\013\013\01388888555555..\023\023\023\023\023\023\023\023\023\023\023\023\023\013\013\013\013\013\01388885555555..\023\023\023\023\023\023\023\023\023\023\023\023\023\013\013\013\013\013\01388885555555..\023\023\023\023\023\023\023\023\023\023\023\023\f\f\013\013\013\013\013\013885555555.66"+
                    "\023\023\023\023\023\023\023\023\023\023\023\f\f\f\f\013\013\013\013\013885555555666\023\023\023\023\023\023\023\023\023\023\f\f\f\f\f\013\013\013\013\013\f15555556666\023\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\013\013\013\f\00611555556666         \f\f\f\f\f\f\f\013\013\013\006\006\0061155566666"+
                    "           \f\f\f\f\f\f\013\006\006\006\006\006111666666             \f\f\f\f\f\006\006\006\006\006\00611666666              \016\016\016\006\006\006\006\006\006\006\0061666666             \016\016\016\006\006\006\006\006\006\006\006\006+666666"+
                    "\035\035          \016\016\016\016\006\006\006\006\006\006\006\006\006++66666\035\035\035\035\035      \016\016\016\016\016\006\006\006\006\006\006\006\006++++6666\035\035\035\035\035\035\035###\016\016\016\016\016\016\006\006\006\006\006\006\006\006++++\007---\035\035\035\035\035\035######\037\016\016\006\006\006\006\006\006\006\006++++\007----"+
                    "\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\006\006\006+++++++((----\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037((((((((((((----\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037((((((((((((----\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037%((((((((((((----"+
                    "\"\"\037\037\037\037\037\037\037\037\037\037\037%%%%((((((((((-----\"\"\"\"\"\037\037\037\037\037\037\037%%%%%%((((((((------\"\"\"\"\"\"\"\"\037\037%%%%%%%%%((((((-------\"\"\"\"\"\"\"\"\"\"%%%%%%%%%%((((--------"+
                    "\021===========77744449999999999999==============444449999999999999==============444448999999999999===============44488899999999999"+
                    "===============;;;88889999999999\022\023\023============;;;88888999999999\023\023\023\023\023\023\023\023\023\023====;;;;8888889999955.\023\023\023\023\023\023\023\023\023\023\023\023\023\023;;;88888888555555."+
                    "\023\023\023\023\023\023\023\023\023\023\023\023\023\023\f\013\013\0138888885555555.\023\023\023\023\023\023\023\023\023\023\023\023\023\f\f\f\013\013\0138888855555566\023\023\023\023\023\023\023\023\023\023\023\023\f\f\f\f\013\013\0138888555555566\023\023\023\023\023\023\023\023\023\023\023\f\f\f\f\f\f\013\013\013888555555666"+
                    "\023\023\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\013\013\013885555556666\023\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f\013\f\f85555556666\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f\f\f\f\006\0065555566666\023       \f\f\f\f\f\f\f\f\f\f\f\f\006\006\006555566666"+
                    "          \f\f\f\f\f\f\f\f\f\006\006\006\006\00655666666            \f\f\f\016\016\016\006\006\006\006\006\006\0061666666             \016\016\016\016\006\006\006\006\006\006\006\006\006666666\035           \016\016\016\016\016\006\006\006\006\006\006\006\006\006666666"+
                    "\035\035\035\035       \016\016\016\016\016\016\006\006\006\006\006\006\006\006\007\00766666\035\035\035\035\035\035    \016\016\016\016\016\016\006\006\006\006\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\035\016\016\016\016\016\016\016\006\006\006\006\006\006\006\006\007\007\007\007\007666\035\035\035\035\035\035\035\037\037\037\037\037\037\016\016\016\006\006\006\006\006\006\006\007\007\007\007\007\007---"+
                    "\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\006\006\006\007\007\007\007\007\007\007\007\007----\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037((((((\007\007\007\007(----\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037((((((((((((----\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037((((((((((((----"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037%%((((((((((-----\"\"\037\037\037\037\037\037\037\037\037\037\037%%%%%((((((((------\"\"\"\"\"\037\037\037\037\037\037\037%%%%%%%((((((-------\"\"\"\"\"\"\"\"\037\037%%%%%%%%%%((((-----)))"+
                    "\022============;;;;999999999999999\022\022\022\022=========;;;;;99999999999999\022\022\022\022\022\022=======;;;;;88999999999999\022\022\022\022\022\022\022\022\022===;;;;;;88899999999999"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;88889999999999\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;88888999999999\022\022\022\022\022\022\022\022\022\022\022\022\022;;;;;88888899999555\022\022\022\022\022\022\022\022\022\022\022\022\022;;;;;88888885555556"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022\023;;;;;88888885555556\022\022\022\022\022\022\022\022\023\023\023\023\f\f\f\f;;88888855555566\022\022\022\022\022\023\023\023\023\023\023\f\f\f\f\f\f\f88888855555566\022\022\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f8888555555666"+
                    "\023\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f\f\f888555556666\023\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f\f\f\f\f85555556666\023\023\023\023\023\023\023\f\f\f\f\f\f\f\f\f\f\f\f\f\006\0065555566666\023      \f\f\f\f\f\f\f\f\f\f\f\f\f\006\006\006555666666"+
                    "         \f\f\f\f\f\f\016\016\016\016\006\006\006\006\00655666666           \016\016\016\016\016\016\016\016\006\006\006\006\006\0066666666\035          \016\016\016\016\016\016\016\006\006\006\006\006\006\0066666666\035\035         \016\016\016\016\016\016\016\006\006\006\006\006\006\006\007666666"+
                    "\035\035\035\035\035     \016\016\016\016\016\016\016\006\006\006\006\006\006\006\006\007\00766666\035\035\035\035\035\035\035\035 \016\016\016\016\016\016\016\016\006\006\006\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\035\016\016\016\016\016\016\016\016\006\006\006\006\006\006\006\007\007\007\007\007666\035\035\035\035\035\035\035\037\037\037\037\037\037\016\016\016\006\006\006\006\006\006\007\007\007\007\007\007\007---"+
                    "\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\006\007\007\007\007\007\007\007\007\007\007\007---\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037(\007\007\007\007\007\007\007\007\007\007----\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037(((((((\007\007\007\007----\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037(((((((((((----"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037(((((((((((-----\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037%%(((((((((-----)\"\"\037\037\037\037\037\037\037\037\037\037\037%%%%%(((((((----)))\"\"\"\"\"\037\037\037\037\037\037\037%%%%%%%((((())))))))"+
                    "???????????;;;;;;;99999999999999\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;9999999999999\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;8999999999999\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;8889999999999"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;888899999999<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;888889999999<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;88888899995<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;88888885555<<"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;8888885555566\022\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;8888855555566\022\022\022\022\022\022\022\022\022\022\022\022\f\f\f;;;;8888855555666\022\022\022\022\022\022\022\022\022\022\022\f\f\f\f\f\f;;8888855555666"+
                    "\022\022\022\022\022\022\022\022\022\f\f\f\f\f\f\f\f\f\f\f888555556666\022\022\022\022\022\022\022\022\f\f\f\f\f\f\f\f\f\f\f\f885555566666\022\022\022\022\022\022\022\f\f\f\f\f\f\f\f\f\f\f\f\016\016\0065555566666\035\022\022\022\022 \f\f\f\f\f\f\f\f\f\016\016\016\016\016\006\006\006555666666"+
                    "\035\035\035     \f\016\016\016\016\016\016\016\016\016\016\016\006\006\006\00656666666\035\035\035\035    \016\016\016\016\016\016\016\016\016\016\016\016\006\006\006\006\0066666666\035\035\035\035\035   \016\016\016\016\016\016\016\016\016\016\016\006\006\006\006\006\0066666666\035\035\035\035\035\035  \016\016\016\016\016\016\016\016\016\016\016\006\006\006\006\006\006\007666666"+
                    "\035\035\035\035\035\035\035 \016\016\016\016\016\016\016\016\016\016\006\006\006\006\006\006\007\007\00766666\035\035\035\035\035\035\035\035\035\016\016\016\016\016\016\016\016\016\006\006\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\035\032\016\016\016\016\016\016\016\006\006\006\006\006\006\007\007\007\007\007\007666\035\035\035\035\035\035\035\035\037\037\037\037\037\016\016\016\016\006\006\006\006\007\007\007\007\007\007\007\007\007--"+
                    "\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\007\007\007\007\007\007\007\007\007\007\007\007\007--\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\007\007\007\007\007\007\007\007\007\007\007\007---\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!\007\007\007\007\007\007\007\007\007\007---\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!\007\007\007\007---)"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!---))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!--)))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037%!!!!!!!!!)))))))\"\037\037\037\037\037\037\037\037\037\037\037\037\037%%!!!!!!!!))))))))"+
                    "????????????;;;;;;99999999999999????????????;;;;;;;9999999999999????????????;;;;;;;;999999999999???\022\022\022\022\022\022\022\022;;;;;;;;;88999999999<"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;8889999999<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;8888999999<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;888889999<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;888888555<<<"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;888885555<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;8888885555666\022\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;8888855555666\022\022\022\022\022\022\022\022\022\022\022\022\f\f;;;;;8888855556666"+
                    "\022\022\022\022\022\022\022\022\022\022\022\f\f\f\f\f\f;;8888555556666\022\022\022\022\022\022\022\022\022\022\f\f\f\f\f\f\f\f\f\016888555566666\022\022\022\022\022\022\022\024\f\f\f\f\f\f\f\016\016\016\016\016\016\0165555666666\035\024\024\024\024\024\024\032\032\032\032\016\016\016\016\016\016\016\016\016\016\006\006555666666"+
                    "\035\035\035\035\024\032\032\032\032\032\032\016\016\016\016\016\016\016\016\016\016\006\006\006\0066666666\035\035\035\035\035\032\032\032\032\032\016\016\016\016\016\016\016\016\016\016\006\006\006\006\0066666666\035\035\035\035\035\035\032\032\032\032\016\016\016\016\016\016\016\016\016\016\006\006\006\006\0066666666\035\035\035\035\035\035\035\032\032\032\016\016\016\016\016\016\016\016\016\006\006\006\006\006\006\007666666"+
                    "\035\035\035\035\035\035\035\035\032\032\016\016\016\016\016\016\016\016\016\006\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\035\032\016\016\016\016\016\016\016\016\006\006\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\035\032\032\016\016\016\016\016\016\016\006\006\006\006\006\007\007\007\007\007\007\007::\035\035\035\035\035\035\035\035\037\037\037\037\037\016\016\016\016\016\006\006\007\007\007\007\007\007\007\007\007\007::"+
                    "\035\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\027\007\007\007\007\007\007\007\007\007\007\007\007\b\b\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\027\007\007\007\007\007\007\007\007\007\007\007\b\b\b\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!\007\007\007\007\007\007\007\007\007\b\b)\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!\007\007\007\b\b))"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!!\b\b))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!)))))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!))))))\034\034\034\037\037\037\037\037\037\037\037\037\037\037\036\036!!!!!!!!))))))))"+
                    "?????????????;;;;;99999999999999?????????????;;;;;;9999999999999????????????;;;;;;;;99999999999<????????????;;;;;;;;8899999999<<"+
                    "????????????;;;;;;;;8888999999<<????????????;;;;;;;;888889999<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;88888899<<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;88888855<<<<"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;88888855<<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;88888555<<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;888885555666\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;888855556666"+
                    "\022\022\022\022\022\022\022\022\022\022\022\024\024;;;;;;;888855566666\024\024\024\024\024\024\024\024\024\024\024\024\032\032\f\016\016\016;;888555566666\024\024\024\024\024\024\024\024\024\024\032\032\032\032\016\016\016\016\016\016\016\0168555666666\024\024\024\024\024\024\024\024\032\032\032\032\032\032\016\016\016\016\016\016\016\016\006556666666"+
                    "\035\035\035\024\024\024\032\032\032\032\032\032\032\032\016\016\016\016\016\016\016\016\006\006\0066666666\035\035\035\035\035\032\032\032\032\032\032\032\032\016\016\016\016\016\016\016\016\006\006\006\0066666666\035\035\035\035\035\035\032\032\032\032\032\032\032\016\016\016\016\016\016\016\016\006\006\006\0066666666\035\035\035\035\035\035\035\032\032\032\032\032\032\016\016\016\016\016\016\016\006\006\006\006\007\007666666"+
                    "\035\035\035\035\035\035\035\035\032\032\032\032\032\016\016\016\016\016\016\016\006\006\006\006\007\007\007\0076666\035\035\035\035\035\035\035\035\032\032\032\032\032\016\016\016\016\016\016\006\006\006\006\007\007\007\007\007\007:::\035\035\035\035\035\035\035\035\035\032\032\032\016\016\016\016\016\016\016\006\006\006\006\007\007\007\007\007\007:::\035\035\035\035\035\035\035\035\037\037\037\037\037\032\016\016\016\016\006\007\007\007\007\007\007\007\007\007\007\007::"+
                    "\035\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\027\027\027\007\007\007\007\007\007\007\007\007\007\b\b\b\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\027\027\027\007\007\007\007\007\007\007\007\007\007\b\b\b\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!\007\007\007\007\007\007\007\b\b\b\b\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!\007\b\b\b)"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!\b\b\b))\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!!\b\b)))\034\034\034\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!))))))\034\034\034\034\034\034\037\037\037\037\037\037\036\036\036\036\036!!!!!!!))))))))"+
                    "??????????????;;;;9999999999999<??????????????;;;;;999999999999<?????????????;;;;;;;9999999999<<?????????????;;;;;;;;89999999<<<"+
                    "?????????????;;;;;;;;88899999<<<????????????;;;;;;;;;8888999<<<<????????????;;;;;;;;;888889<<<<<????????????;;;;;;;;;888888<<<<<"+
                    "\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;;888885<<<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;;888885<<<<<\022\022\022\022\022\022\022\022\022\022\022\022;;;;;;;;;8888555<<<<\022\022\024\024\024\024\024\024\024\024\024\024\024;;;;;;;;88855556666"+
                    "\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;;88855566666\024\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032;;;;88555666666\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\016\016\016\016\0165555666666\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\016\016\016\016\016\016\00656666666"+
                    "\035\035\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\016\016\016\016\016\006\006\0066666666\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\032\032\016\016\016\016\016\006\006\0066666666\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\016\016\016\016\016\006\006\006\0066666666\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\016\016\016\016\016\006\006\006\007\007\00766666"+
                    "\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\016\016\016\016\006\006\006\007\007\007\007\0076:::\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\016\016\016\016\006\007\007\007\007\007\007\007::::\035\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\016\016\016\027\007\007\007\007\007\007\007\007\007:::\035\035\035\035\035\035\035\035\037\037\037\037\037\032\032\016\016\027\027\027\007\007\007\007\007\007\007\007\007:::"+
                    "\035\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\027\027\027\027\007\007\007\007\007\007\007\007\007\b\b\b\035\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\027\027\027\027\007\007\007\007\007\007\007\007\b\b\b\b\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\037\027\027!!!!!\007\007\007\007\007\b\b\b\b\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!!\b\b\b\b"+
                    "\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!\b\b\b\b)\034\034\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!!\b\b\b))\034\034\034\034\034\037\037\037\037\037\037\037\037\036\036\036!!!!!!!!!!\b)))))\034\034\034\034\034\034\034\034\034\037\037\036\036\036\036\036\036\036!!!!!!!)))))))"+
                    "???????????????;;;;99999999999<<???????????????;;;;;9999999999<<??????????????;;;;;;;99999999<<<??????????????;;;;;;;;999999<<<<"+
                    "??????????????;;;;;;;;888999<<<<?????????????;;;;;;;;;88889<<<<<?????????????;;;;;;;;;88888<<<<<?????????????;;;;;;;;;8888<<<<<<"+
                    "????????????;;;;;;;;;;8888<<<<<<????????????;;;;;;;;;;8888<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;;;;8885<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;;;8885<<<<<<"+
                    "\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;888555<6666\024\024\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032;;;85555666666\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\032\0165556666666\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\016\016556666666"+
                    "\035\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\032\032\016\016\00666666666\035\035\035\035\024\024\032\032\032\032\032\032\032\032\032\032\032\032\032\032\016\016\027\027\0276666666\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\032\032\032\032\016\027\027\027\027\02766666:\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\007\007\0076::::"+
                    "\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\007\007\007\007::::\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\007\007\007\007\007::::\035\035\035\035\035\035\035\035\035\037\032\032\032\032\032\032\027\027\027\027\027\027\007\007\007\007\007\007::::\035\035\035\035\035\035\035\035\037\037\037\037\037\037\032\027\027\027\027\027\027\027\007\007\007\007\007\007\007:::"+
                    "\035\035\035\035\035\035\035\037\037\037\037\037\037\037\037\027\027\027\027\027\027\007\007\007\007\007\007\007\b\b\b:\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\027\027\027\027\027\027\007\007\007\007\007\007\007\b\b\b\b\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\037\027\027\027!!!!!\007\007\007\007\b\b\b\b\033\037\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!\b\b\b\b\b"+
                    "\033\037\037\037\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!!\b\b\b\b)\034\034\034\034\034\037\037\037\037\037\037\037\037\037\036\036!!!!!!!!!!\b\b\b\b\b)\034\034\034\034\034\034\034\034\037\037\037\037\036\036\036\036\036\036!!!!!!!!\b\b))))\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036!!!!!)))))))"+
                    "????????????????;;;9999999999<<<???????????????;;;;;999999999<<<???????????????;;;;;;9999999<<<<???????????????;;;;;;;999999<<<<"+
                    "???????????????;;;;;;;;8999<<<<<??????????????;;;;;;;;;888<<<<<<??????????????;;;;;;;;8888<<<<<<??????????????;;;;;;;;8888<<<<<<"+
                    "?????????????;;;;;;;;;8888<<<<<<????????????\024;;;;;;;;;8888<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;;;888<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;;888<<<<<<<"+
                    "\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024;;;;;;855<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032;;5555<<6666\024\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\0325566666666\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\032666666666"+
                    "\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\032\027\027\02766666666\035\035\035\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027666666:\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\02766::::\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027\027:::::"+
                    "\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027\007\007:::::\035\035\035\035\035\035\035\035\032\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027\027\007\007\007::::\035\035\035\035\035\035\035\035\037\037\032\032\032\032\032\027\027\027\027\027\027\027\027\027\007\007\007\007::::\035\035\035\035\035\035\035\037\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\007\007\007\007\007::::"+
                    "\035\035\035\035\035\035\037\037\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\007\007\007\007\007\b\b\b:\035\035\035\035\035\037\037\037\037\037\037\037\037\037\037\027\027\027\027\027\027\027\007\007\007\007\007\007\b\b\b\b\035\035\035\037\037\037\037\037\037\037\037\037\037\037\037\027\027\027\027\027!!!!!\007\007\b\b\b\b\b\033\033\033\037\037\037\037\037\037\037\037\037\037\037\037\037\027!!!!!!!!!!\b\b\b\b\b"+
                    "\034\034\034\034\037\037\037\037\037\037\037\037\037\037\037!!!!!!!!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\037\037\037\037\037\036\036\036\036!!!!!!!!!\b\b\b\b\b)\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036!!!!!!!\b\b\b\b))\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036!!!!\b))))))"+
                    "?????????????????;;9999999999<<<????????????????;;;;99999999<<<<????????????????;;;;;999999<<<<<????????????????;;;;;;99999<<<<<"+
                    "???????????????;;;;;;;;8<<<<<<<<???????????????;;;;;;;;8<<<<<<<<???????????????>;;;;;;;<<<<<<<<<??????????????>>>>>;;;;<<<<<<<<<"+
                    "?????????????>>>>>>>>><<<<<<<<<<?????????\024\024\024>>>>>>>>>><<<<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024>>>>>>>>><<<<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024>>>>>>>><<<<<<<<<<"+
                    "\024\024\024\024\024\024\024\024\024\024\024\024\024\024>>>>>>>><<<<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024\024>>>>>>\017<<<<<<<<<<\024\024\024\024\024\024\024\024\024\024\024\024\024\024\032\032\017\017\017\017\017\017\017<<<<<6666\024\024\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\017\017\017\017\017\017\01766666666"+
                    "\024\024\024\024\024\024\024\024\024\024\032\032\032\032\032\032\032\032\017\017\017\017\027\027666666::\033\033\033\024\024\024\024\024\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\02766::::\033\033\033\033\033\033\032\032\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027::::::\033\033\033\033\033\033\033\032\032\032\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027\027\027:::::"+
                    "\033\033\033\033\033\033\033\033\032\032\032\032\032\032\032\027\027\027\027\027\027\027\027\027\027\027\027:::::\033\033\033\033\033\033\033\033\033\032\032\032\032\032\027\027\027\027\027\027\027\027\027\027\027\027\027:::::\033\033\033\033\033\033\033\033\037\037\037\032\032\027\027\027\027\027\027\027\027\027\027\027\027\027\007\007::::\033\033\033\033\033\033\033\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\027\027\027\007\007\007::::"+
                    "\033\033\033\033\033\033\037\037\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\027\027\007\007\007\b\b::\033\033\033\033\033\037\037\037\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\027\007\007\007\b\b\b\b\b\033\033\033\033\037\037\037\037\037\037\037\037\037\037\037\027\027\027\027\027\027!!!!!!\b\b\b\b\b\033\033\033\033\037\037\037\037\037\037\037\037\037\037\037\027\027\027!!!!!!!!!\b\b\b\b\b"+
                    "\034\034\034\034\034\034\034\037\037\037\037\037\037\037\036\036!!!!!!!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\037\036\036\036\036\036\036!!!!!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036!!!!!\b\b\b\b\b\b)\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036!!!\b\b\b))))"+
                    "??????????????????;;99999999<<<<?????????????????>>;;99999<<<<<<????????????????>>>>>>9<<<<<<<<<\020??????????????>>>>>>><<<<<<<<<<"+
                    "\020\020????????????>>>>>>>><<<<<<<<<<\020\020???????????>>>>>>>>><<<<<<<<<<\020\020\020?????????>>>>>>>>>><<<<<<<<<<\020\020\020\020????????>>>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020??????>>>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\024\024\024\024>>>>>>>>>>>><<<<<<<<<<\026\026\024\024\024\024\024\024\024\024\024>>>>>>>>>>><<<<<<<<<<\026\026\024\024\024\024\024\024\024\024\024\024>>>>>>>>>><<<<<<<<<<"+
                    "\026\026\024\024\024\024\024\024\024\024\024\024>>>>>>>>>><<<<<<<<<<\026\026\024\024\024\024\024\024\024\024\024\024\024>>>>>\017\017\017\017\017<<<<<<<<<\026\026\024\024\024\024\024\024\024\024\024\024\024\017\017\017\017\017\017\017\017\017\017<<<<<<<<<\026\026\026\024\024\024\024\024\024\024\024\024\017\017\017\017\017\017\017\017\017\017\017\017<<<<66::"+
                    "\026\026\026\024\024\024\024\024\024\024\024\032\017\017\017\017\017\017\017\017\017\017\017\017\017:::::::\033\033\033\033\033\024\024\024\024\032\032\032\032\017\017\017\017\017\017\017\017\027\027\027\027:::::::\033\033\033\033\033\033\033\033\032\032\032\032\032\032\017\017\017\017\027\027\027\027\027\027\027:::::::\033\033\033\033\033\033\033\033\025\025\025\032\032\032\032\027\027\027\027\027\027\027\027\027\027\027::::::"+
                    "\033\033\033\033\033\033\033\033\033\025\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027\027::::::\033\033\033\033\033\033\033\033\033\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027\027\027\027:::::\033\033\033\033\033\033\033\033\033\033\025\025\025\027\027\027\027\027\027\027\027\027\027\027\027\027\027:::::\033\033\033\033\033\033\033\033\033\037\037\037\027\027\027\027\027\027\027\027\027\027\027\027\027\027\027\007::::"+
                    "\033\033\033\033\033\033\033\033\037\037\037\037\037\027\027\027\027\027\027\027\027\027\027\027\027\027\007\b\b\b::\033\033\033\033\033\033\033\037\037\037\037\037\037\027\027\027\027\027\027\027\027\027\027\027!!\b\b\b\b\b\b\033\033\033\033\033\033\037\037\037\037\037\037\037\037\027\027\027\027\027\027\027!!!!!\b\b\b\b\b\b\033\033\033\033\034\034\037\037\037\037\037\037\037\037\027\027\027\027\027!!!!!!!\b\b\b\b\b\b"+
                    "\034\034\034\034\034\034\034\034\034\034\037\037\037\036\036\036\036!!!!!!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036!!!!!!\b\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036!!!!\b\b\b\b\b\b\t\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036\036!\b\b\b\b\t\t\t\t"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\020\020\020\020>>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\024>>>>>>>>>>>>><<<<<<<<<<"+
                    "\026\026\026\026\026\026\026\026\024\024>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\024\024\024>>>>\017\017\017\017\017\017\017\017<<<<<<<<<\026\026\026\026\026\026\026\026\024\024\024\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<\026\026\026\026\026\026\026\026\024\024\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<::"+
                    "\026\026\026\026\026\026\026\026\025\025\017\017\017\017\017\017\017\017\017\017\017\017\017\017\017:::::::\031\031\031\031\031\026\026\025\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017\017::::::::\033\033\033\033\033\033\033\025\025\025\025\025\025\017\017\017\017\017\017\017\017\027\027\027::::::::\033\033\033\033\033\033\033\025\025\025\025\025\025\025\025\017\017\017\027\027\027\027\027\027\027:::::::"+
                    "\033\033\033\033\033\033\033\033\025\025\025\025\025\025\025\025\027\027\027\027\027\027\027\027\027:::::::\033\033\033\033\033\033\033\033\025\025\025\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027::::::\033\033\033\033\033\033\033\033\033\025\025\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027::::::\033\033\033\033\033\033\033\033\033\025\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027\027\027:::::"+
                    "\033\033\033\033\033\033\033\033\033\033\025\025\025\027\027\027\027\027\027\027\027\027\027\027\027\027\b\b\b:::\033\033\033\033\033\033\033\033\034\034\037\037\025\027\027\027\027\027\027\027\027\027\027\027\027!\b\b\b\b\b\b\033\033\033\033\034\034\034\034\034\034\034\037\037\027\027\027\027\027\027\027\027\030!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\030\030\030\030\030\030!!!!!\b\b\b\b\b\b"+
                    "\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\030\030!!!!!!\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\030!!!!\b\b\b\b\b\b\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\030!!\b\b\b\b\t\t\t\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036\036\036\t\t\t\t\t\t\t\t"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>><<<<<<<<<<\026\020\020\020\020\020\020\020\020\020>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\020\020\020>>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\026>>>>>>>>>>>>><<<<<<<<<<"+
                    "\026\026\026\026\026\026\026\026\026\026>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\026\026>>>\017\017\017\017\017\017\017\017\017\017<<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<:"+
                    "\026\026\026\026\026\026\026\026\025\025\017\017\017\017\017\017\017\017\017\017\017\017\017\017\017:::::::\031\031\031\031\031\031\031\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017\017::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\017\017\017\017\017\017\027\027\027::::::::"+
                    "\033\033\033\033\033\031\031\025\025\025\025\025\025\025\025\025\025\027\027\027\027\027\027\027::::::::\033\033\033\033\033\033\033\025\025\025\025\025\025\025\025\025\025\027\027\027\027\027\027\027\027:::::::\033\033\033\033\033\033\033\033\025\025\025\025\025\025\025\025\027\027\027\027\027\027\027\027\027:::::::\033\033\033\033\033\033\033\033\025\025\025\025\025\025\025\027\027\027\027\027\027\027\027\027\027\027::::::"+
                    "\033\033\033\033\033\033\033\033\033\025\025\025\025\025\025\027\027\027\027\030\030\030\030\030\030\030\030:::::\033\033\033\033\033\034\034\034\034\034\034\025\025\025\030\030\030\030\030\030\030\030\030\030\030\030\b\b\b\b\b:\033\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\b\b\b\b\b\b\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\030\030\030\030\030\030\030\030\030\030\030\b\b\b\b\b\b"+
                    "\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\030\030\030\030\030\030\030\b\b\b\b\b\b\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\030\030\030\030\030\b\b\b\b\b\t\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\030\030\030\b\t\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036\036\036\t\t\t\t\t\t\t\t"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>><<<<<<<<<<\026\026\020\020\020\020\020\020\020\020>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\020\020>>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\026>>>>>>>>>>>>><<<<<<<<<<"+
                    "\026\026\026\026\026\026\026\026\026\026>>>>>>>>>>>\017<<<<<<<<<<\026\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<:"+
                    "\026\026\026\026\026\026\026\026\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017\017\017:::::::\031\031\031\031\031\031\031\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\017\017\017\017\017\017\017:::::::::"+
                    "\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\017\017\017\027\027\027\027::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\025\027\027\027\027\027\027::::::::\033\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\027\030\030\030\030\030\030\030:::::::\033\033\033\033\033\033\033\025\025\025\025\025\025\025\025\025\030\030\030\030\030\030\030\030\030\030::::::"+
                    "\033\033\033\033\033\033\034\034\025\025\025\025\025\025\025\030\030\030\030\030\030\030\030\030\030\030\030:::::\033\033\034\034\034\034\034\034\034\034\034\025\025\025\030\030\030\030\030\030\030\030\030\030\030\030\030\030\b\b\b:\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\b\b\b\b\t\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\030\030\030\030\030\030\030\030\030\030\030\030\b\b\b\b\t"+
                    "\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\030\030\030\030\030\030\030\030\030\b\t\t\t\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\030\030\030\030\030\030\t\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\030\030\030\t\t\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036\036\036\t\t\t\t\t\t\t\t"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>><<<<<<<<<<\020\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>><<<<<<<<<<"+
                    "\020\020\020\020\020\020\020\020\020\020\020>>>>>>>>>>><<<<<<<<<<\026\026\026\020\020\020\020\020\020\020>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\020\020>>>>>>>>>>>><<<<<<<<<<\026\026\026\026\026\026\026\026\026\026>>>>>>>>>>>><<<<<<<<<<"+
                    "\026\026\026\026\026\026\026\026\026\026\026>>>>>>>\017\017\017\017<<<<<<<<<<\026\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<<\026\026\026\026\026\026\026\026\026\026\017\017\017\017\017\017\017\017\017\017\017\017\017\017\017<<<<<<<"+
                    "\026\026\026\026\026\026\026\026\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017\017\017:::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017\017\017:::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\017\017\017\017\017\017\017\017\017\017::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\017\017\017\017\017\017\017:::::::::"+
                    "\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\025\017\017\017\017\030:::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\025\025\030\030\030\030\030::::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\025\030\030\030\030\030\030\030\030:::::::\031\031\031\031\031\031\031\025\025\025\025\025\025\025\025\025\030\030\030\030\030\030\030\030\030\030\030:::::"+
                    "\031\031\031\031\034\034\034\034\025\025\025\025\025\025\025\030\030\030\030\030\030\030\030\030\030\030\030\030::::\034\034\034\034\034\034\034\034\034\034\034\025\025\025\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030:::\034\034\034\034\034\034\034\034\034\034\034\034\034\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\030\t\t\t\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\030\030\030\030\030\030\030\030\030\030\030\030\t\t\t\t"+
                    "\034\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\030\030\030\030\030\030\030\030\030\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\030\030\030\030\030\030\t\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\030\030\030\t\t\t\t\t\t\t\034\034\034\034\034\034\034\034\034\034\036\036\036\036\036\036\036\036\036\036\036\036\036\036\t\t\t\t\t\t\t\t"
    ).getBytes(StandardCharsets.ISO_8859_1)
    );
    /**
     * The default 256-color VGA palette, upscaled to RGBA8888 as per
     * <a href="https://commons.wikimedia.org/wiki/File:VGA_palette_with_black_borders.svg">this image and the .sh
     * script that generated it</a>. The first index is transparent rather than black, but the last 8 indices are all
     * identical black colors.
     */
    public static final int[] VGA256 = {
            0x00000000, 0x0000AAFF, 0x00AA00FF, 0x00AAAAFF, 0xAA0000FF, 0xAA00AAFF, 0xAA5500FF, 0xAAAAAAFF,
            0x555555FF, 0x5555FFFF, 0x55FF55FF, 0x55FFFFFF, 0xFF5555FF, 0xFF55FFFF, 0xFFFF55FF, 0xFFFFFFFF,
            0x000000FF, 0x101010FF, 0x202020FF, 0x353535FF, 0x454545FF, 0x555555FF, 0x656565FF, 0x757575FF,
            0x8A8A8AFF, 0x9A9A9AFF, 0xAAAAAAFF, 0xBABABAFF, 0xCACACAFF, 0xDFDFDFFF, 0xEFEFEFFF, 0xFFFFFFFF,
            0x0000FFFF, 0x4100FFFF, 0x8200FFFF, 0xBE00FFFF, 0xFF00FFFF, 0xFF00BEFF, 0xFF0082FF, 0xFF0041FF,
            0xFF0000FF, 0xFF4100FF, 0xFF8200FF, 0xFFBE00FF, 0xFFFF00FF, 0xBEFF00FF, 0x82FF00FF, 0x41FF00FF,
            0x00FF00FF, 0x00FF41FF, 0x00FF82FF, 0x00FFBEFF, 0x00FFFFFF, 0x00BEFFFF, 0x0082FFFF, 0x0041FFFF,
            0x8282FFFF, 0x9E82FFFF, 0xBE82FFFF, 0xDF82FFFF, 0xFF82FFFF, 0xFF82DFFF, 0xFF82BEFF, 0xFF829EFF,
            0xFF8282FF, 0xFF9E82FF, 0xFFBE82FF, 0xFFDF82FF, 0xFFFF82FF, 0xDFFF82FF, 0xBEFF82FF, 0x9EFF82FF,
            0x82FF82FF, 0x82FF9EFF, 0x82FFBEFF, 0x82FFDFFF, 0x82FFFFFF, 0x82DFFFFF, 0x82BEFFFF, 0x829EFFFF,
            0xBABAFFFF, 0xCABAFFFF, 0xDFBAFFFF, 0xEFBAFFFF, 0xFFBAFFFF, 0xFFBAEFFF, 0xFFBADFFF, 0xFFBACAFF,
            0xFFBABAFF, 0xFFCABAFF, 0xFFDFBAFF, 0xFFEFBAFF, 0xFFFFBAFF, 0xEFFFBAFF, 0xDFFFBAFF, 0xCAFFBAFF,
            0xBAFFBAFF, 0xBAFFCAFF, 0xBAFFDFFF, 0xBAFFEFFF, 0xBAFFFFFF, 0xBAEFFFFF, 0xBADFFFFF, 0xBACAFFFF,
            0x000071FF, 0x1C0071FF, 0x390071FF, 0x550071FF, 0x710071FF, 0x710055FF, 0x710039FF, 0x71001CFF,
            0x710000FF, 0x711C00FF, 0x713900FF, 0x715500FF, 0x717100FF, 0x557100FF, 0x397100FF, 0x1C7100FF,
            0x007100FF, 0x00711CFF, 0x007139FF, 0x007155FF, 0x007171FF, 0x005571FF, 0x003971FF, 0x001C71FF,
            0x393971FF, 0x453971FF, 0x553971FF, 0x613971FF, 0x713971FF, 0x713961FF, 0x713955FF, 0x713945FF,
            0x713939FF, 0x714539FF, 0x715539FF, 0x716139FF, 0x717139FF, 0x617139FF, 0x557139FF, 0x457139FF,
            0x397139FF, 0x397145FF, 0x397155FF, 0x397161FF, 0x397171FF, 0x396171FF, 0x395571FF, 0x394571FF,
            0x515171FF, 0x595171FF, 0x615171FF, 0x695171FF, 0x715171FF, 0x715169FF, 0x715161FF, 0x715159FF,
            0x715151FF, 0x715951FF, 0x716151FF, 0x716951FF, 0x717151FF, 0x697151FF, 0x617151FF, 0x597151FF,
            0x517151FF, 0x517159FF, 0x517161FF, 0x517169FF, 0x517171FF, 0x516971FF, 0x516171FF, 0x515971FF,
            0x000041FF, 0x100041FF, 0x200041FF, 0x310041FF, 0x410041FF, 0x410031FF, 0x410020FF, 0x410010FF,
            0x410000FF, 0x411000FF, 0x412000FF, 0x413100FF, 0x414100FF, 0x314100FF, 0x204100FF, 0x104100FF,
            0x004100FF, 0x004110FF, 0x004120FF, 0x004131FF, 0x004141FF, 0x003141FF, 0x002041FF, 0x001041FF,
            0x202041FF, 0x282041FF, 0x312041FF, 0x392041FF, 0x412041FF, 0x412039FF, 0x412031FF, 0x412028FF,
            0x412020FF, 0x412820FF, 0x413120FF, 0x413920FF, 0x414120FF, 0x394120FF, 0x314120FF, 0x284120FF,
            0x204120FF, 0x204128FF, 0x204131FF, 0x204139FF, 0x204141FF, 0x203941FF, 0x203141FF, 0x202841FF,
            0x2D2D41FF, 0x312D41FF, 0x352D41FF, 0x3D2D41FF, 0x412D41FF, 0x412D3DFF, 0x412D35FF, 0x412D31FF,
            0x412D2DFF, 0x41312DFF, 0x41352DFF, 0x413D2DFF, 0x41412DFF, 0x3D412DFF, 0x35412DFF, 0x31412DFF,
            0x2D412DFF, 0x2D4131FF, 0x2D4135FF, 0x2D413DFF, 0x2D4141FF, 0x2D3D41FF, 0x2D3541FF, 0x2D3141FF,
            0x000000FF, 0x000000FF, 0x000000FF, 0x000000FF, 0x000000FF, 0x000000FF, 0x000000FF, 0x000000FF
    };

    /**
     * An auto-generated palette made by churning out over a thousand random colors and repeatedly merging the most
     * similar pair of colors, until it reached 63 colors (plus transparent, for 64 total). {@link #QUORUM128} and
     * {@link #QUORUM256} are similar palettes that use 128 and 256 total colors, respectively.
     */
    public static final int[] QUORUM64 = {
            0x00000000, 0x010101FF, 0x212121FF, 0x414141FF, 0x616161FF, 0x818181FF, 0xA1A1A1FF, 0xC1C1C1FF,
            0xE1E1E1FF, 0xFFFFFFFF, 0x057B4BFF, 0x750000FF, 0x405816FF, 0x81247BFF, 0x19529EFF, 0x2A6457FF,
            0x008A92FF, 0x55454BFF, 0x9C002EFF, 0xB60058FF, 0x494273FF, 0x40824EFF, 0x7649A1FF, 0xAA2F47FF,
            0x4F5EB7FF, 0xCC1C5EFF, 0x57778BFF, 0x43A26EFF, 0x6B6843FF, 0x885A6FFF, 0x30949FFF, 0x728A55FF,
            0x7D4604FF, 0xD0484AFF, 0x42B79EFF, 0x9677AAFF, 0x6BA09AFF, 0xA1906AFF, 0xE43A96FF, 0x7D9FCAFF,
            0x86B068FF, 0xD162A2FF, 0xBB7C54FF, 0xB6A550FF, 0x79BBA4FF, 0xAF8FC2FF, 0x9E9EDFFF, 0x99A3A8FF,
            0x60C5CCFF, 0xD4A149FF, 0x92CF69FF, 0xD2AAFBFF, 0x96D1D1FF, 0xD29FB0FF, 0xB6BCA1FF, 0x84D8F8FF,
            0xBABCE1FF, 0xCFE29FFF, 0xEF9686FF, 0xFB6B97FF, 0xFCF5ABFF, 0xBBF8D9FF, 0xF9B4DCFF, 0xE0D9D9FF,
    };
    /**
     * An auto-generated palette made by churning out over a thousand random colors and repeatedly merging the most
     * similar pair of colors, until it reached 127 colors (plus transparent, for 128 total). {@link #QUORUM64} and
     * {@link #QUORUM256} are similar palettes that use 64 and 256 total colors, respectively.
     */
    public static final int[] QUORUM128 = {
            0x00000000, 0x010101FF, 0x171717FF, 0x2D2D2DFF, 0x555555FF, 0x686868FF, 0x7B7B7BFF, 0x8D8D8DFF,
            0x9F9F9FFF, 0xB0B0B0FF, 0xC1C1C1FF, 0xD1D1D1FF, 0xE1E1E1FF, 0xF0F0F0FF, 0xFFFFFFFF, 0x104B89FF,
            0x812400FF, 0x065B5AFF, 0x5B2A1DFF, 0x6E0591FF, 0x9C0C08FF, 0x640F59FF, 0x027C25FF, 0x2A7303FF,
            0xB90342FF, 0x009E0BFF, 0x5B36AEFF, 0xDC0029FF, 0x326551FF, 0x534D33FF, 0x278046FF, 0x93105DFF,
            0x0787CDFF, 0x157B89FF, 0xBD039CFF, 0x33920FFF, 0x7D3583FF, 0x7E4437FF, 0x129955FF, 0x921CA5FF,
            0x51578CFF, 0x78780BFF, 0xCF3508FF, 0x1CC700FF, 0x07A58CFF, 0x54832AFF, 0x8A6749FF, 0xF60147FF,
            0xA9581AFF, 0xB63250FF, 0x347EAEFF, 0xD42D4BFF, 0x6D9802FF, 0x6A6F4DFF, 0xCA1EBFFF, 0x03DC4AFF,
            0x11BD5EFF, 0x7C54DBFF, 0x8D52BDFF, 0x379A95FF, 0x44A547FF, 0x7368F7FF, 0x759144FF, 0x4F847AFF,
            0xBF5D52FF, 0x6771ADFF, 0xDE4486FF, 0x9C5980FF, 0xC645BCFF, 0x16D8ACFF, 0x60AA73FF, 0x9578BEFF,
            0x5A9EC0FF, 0x7C8183FF, 0x3AB8C0FF, 0x9A8B90FF, 0x6095DEFF, 0x35C9F7FF, 0xBA8938FF, 0xD77444FF,
            0x6DBF3AFF, 0xA17BDFFF, 0xFE318DFF, 0x7AAF9FFF, 0x5FD287FF, 0xE064A9FF, 0xC27890FF, 0xBF70F5FF,
            0x65C6DCFF, 0xA09ECEFF, 0x4DEABEFF, 0xA2A952FF, 0xFF6071FF, 0x9CA1F6FF, 0x79BAFFFF, 0x8BC5D3FF,
            0xD988DDFF, 0x9BC797FF, 0xB7ACB3FF, 0xBFA7E2FF, 0xFC5ABAFF, 0xA3EF5EFF, 0xE088FDFF, 0xF5905AFF,
            0xD4A294FF, 0x87E297FF, 0xD3D83BFF, 0xBCD386FF, 0xEBA4BFFF, 0xC7CDCCFF, 0x28FF8FFF, 0x9AE0E6FF,
            0x58FC8EFF, 0x4EFFE2FF, 0xFB6EF7FF, 0xFE83B3FF, 0xDFE0A4FF, 0xD0C3FCFF, 0xF4CA8AFF, 0xC7FF92FF,
            0xFFFF60FF, 0x81FEA7FF, 0x7DF2FEFF, 0xEFCAEAFF, 0xFFA7F3FF, 0xA2FDFBFF, 0xE4EFD9FF, 0xAEF4B9FF,
    };
    /**
     * An auto-generated palette made by churning out over a thousand random colors and repeatedly merging the most
     * similar pair of colors, until it reached 255 colors (plus transparent, for 256 total). {@link #QUORUM64} and
     * {@link #QUORUM128} are similar palettes that use 64 and 128 total colors, respectively.
     */
    public static final int[] QUORUM256 = {
            0x00000000, 0x010101FF, 0x171717FF, 0x2D2D2DFF, 0x555555FF, 0x686868FF, 0x7B7B7BFF, 0x8D8D8DFF,
            0x9F9F9FFF, 0xB0B0B0FF, 0xC1C1C1FF, 0xD1D1D1FF, 0xE1E1E1FF, 0xF0F0F0FF, 0xFFFFFFFF, 0x300687FF,
            0x4A014EFF, 0x024A4CFF, 0x292160FF, 0x510916FF, 0x3C2339FF, 0x0D5209FF, 0x3D2654FF, 0x5B2807FF,
            0x223250FF, 0x312972FF, 0x45260DFF, 0x0B5B2EFF, 0x541B41FF, 0x2F3728FF, 0x720877FF, 0x004A6FFF,
            0x6F0033FF, 0x85001CFF, 0x9F0068FF, 0x0D4392FF, 0x35470CFF, 0x6A2230FF, 0x2B6008FF, 0xC7002AFF,
            0x006D6CFF, 0x42218AFF, 0x5A2580FF, 0x0759ADFF, 0xB10052FF, 0xA7081CFF, 0x5D1762FF, 0x3D4462FF,
            0x413E99FF, 0x524A20FF, 0x1A6F4AFF, 0x4D30A7FF, 0x670D00FF, 0x8C1833FF, 0x693922FF, 0x8C2807FF,
            0x24524AFF, 0x1F6273FF, 0x65414AFF, 0x00856DFF, 0x3F5D25FF, 0x742B59FF, 0xA24400FF, 0x106792FF,
            0x85256FFF, 0x51534DFF, 0x793836FF, 0x8D004BFF, 0x3C507EFF, 0x544B75FF, 0x696000FF, 0x7A2E0AFF,
            0x6B3D74FF, 0x875000FF, 0xAF2F0DFF, 0xC50065FF, 0xB51A43FF, 0x3F7930FF, 0x5B538EFF, 0x5B4BCAFF,
            0x755750FF, 0x7B6900FF, 0x9E2F43FF, 0x49693BFF, 0xB71674FF, 0x6A6F22FF, 0x665F3AFF, 0x3F7263FF,
            0x87418BFF, 0x894644FF, 0x398B61FF, 0xBF2B5FFF, 0x0C8C8DFF, 0x6B6281FF, 0x627900FF, 0xB22F90FF,
            0x946018FF, 0x1B78A0FF, 0x50709DFF, 0x875872FF, 0x9F4C75FF, 0x63675DFF, 0x408D95FF, 0x4B75BBFF,
            0xB13A5EFF, 0x9C43B2FF, 0x39A262FF, 0x547B81FF, 0xD12F2CFF, 0x5B804BFF, 0xC54C27FF, 0xC02FB0FF,
            0x1DAA9DFF, 0x4FA246FF, 0xD92390FF, 0x835BAAFF, 0x7E7C29FF, 0xAF4C25FF, 0x7C7572FF, 0xA47A00FF,
            0x839308FF, 0x36AF88FF, 0x846A8EFF, 0x539979FF, 0x698989FF, 0x916C5AFF, 0x76829BFF, 0xBA644FFF,
            0x718F5BFF, 0xFF2175FF, 0x4C98A5FF, 0x7C77C3FF, 0xCA4299FF, 0x99802FFF, 0xB64D73FF, 0x8B904CFF,
            0x7A7DDDFF, 0x37AAC2FF, 0x55B48AFF, 0xB47F0FFF, 0xAA7361FF, 0x55A4C2FF, 0xAA7F30FF, 0x7E8CB7FF,
            0x70B044FF, 0xB85EB7FF, 0x89996EFF, 0xE34B79FF, 0xC9725CFF, 0xCA3F5BFF, 0x83A95AFF, 0x5CA8ADFF,
            0xFF2FBDFF, 0xF83B95FF, 0x3CBEACFF, 0x709D9AFF, 0x6F98CAFF, 0x77A57BFF, 0x9E7598FF, 0x9E7EC9FF,
            0xA2926CFF, 0x8DA835FF, 0x8B8C93FF, 0xAAA24CFF, 0xADA818FF, 0xB88B49FF, 0xD36781FF, 0xC762FFFF,
            0x81BF69FF, 0x6EC18BFF, 0xBD7EA8FF, 0xD274AEFF, 0x9899BCFF, 0xC59C3BFF, 0x7CACE4FF, 0xE9726CFF,
            0x9994D0FF, 0x8AB384FF, 0x7CAEB5FF, 0xCB8B77FF, 0xA8A48AFF, 0x7ED362FF, 0xFC586EFF, 0x6CD4BAFF,
            0xC28FD5FF, 0x5BE2D4FF, 0xC595A8FF, 0xA8AFB6FF, 0xA59EEEFF, 0x8AC0D6FF, 0xB197FFFF, 0x9ABF94FF,
            0x7FD19EFF, 0x98CC75FF, 0xFF4BBDFF, 0xEC699FFF, 0xE18E9AFF, 0xB7B773FF, 0xCFAB64FF, 0x95C1BDFF,
            0xEE85DDFF, 0x87DABFFF, 0xFF67DCFF, 0x9EE469FF, 0x7DDFE2FF, 0x8AF374FF, 0x76EDC9FF, 0x81B9FFFF,
            0xC9B39CFF, 0xC1A1EAFF, 0x9CDB9CFF, 0xFF62ABFF, 0xB5BAECFF, 0xAFBAD7FF, 0xFF3EEEFF, 0xB8C9B3FF,
            0xD696FEFF, 0x6AFDFFFF, 0xE0A57BFF, 0xAAD7C3FF, 0xD6B4C4FF, 0xBEBAFFFF, 0xAAD6FFFF, 0x9ED8EBFF,
            0xD6BAFCFF, 0xDAC194FF, 0xB7E2A0FF, 0xA5EED5FF, 0xDBDC97FF, 0xC8EFAFFF, 0xCAD6C5FF, 0xF18BA9FF,
            0xFF98D5FF, 0xFFE645FF, 0xE3CBD7FF, 0xFFD363FF, 0xEFDAAFFF, 0x87EEFFFF, 0xFF7BB3FF, 0xF3A9C0FF,
            0xCFD3FFFF, 0xE4F79DFF, 0xFFCD97FF, 0xC0FAD2FF, 0x95FDECFF, 0xCDFE96FF, 0xFFB494FF, 0xFFF567FF,
            0xFDD0CBFF, 0xFF94FFFF, 0xE0FFC7FF, 0xB7F5F9FF, 0xE0FCF9FF, 0xF4B8F3FF, 0xE7E5EDFF, 0xFDF5CBFF,
    };
    /**
     * An auto-generated palette like {@link #QUORUM256}, but using different calculations that give it better coverage
     * of orange and brown but worse coverage of green.
     */
    public static final int[] MASH256 = {
            0x00000000, 0x010101FF, 0x171717FF, 0x2D2D2DFF, 0x555555FF, 0x686868FF, 0x7B7B7BFF, 0x8D8D8DFF,
            0x9F9F9FFF, 0xB0B0B0FF, 0xC1C1C1FF, 0xD1D1D1FF, 0xE1E1E1FF, 0xF0F0F0FF, 0xFFFFFFFF, 0x340466FF,
            0x4A054DFF, 0x791D08FF, 0x7E061CFF, 0x05344CFF, 0x1404ACFF, 0x1F2755FF, 0x393501FF, 0x3A068AFF,
            0x003472FF, 0x6A0F45FF, 0x055B12FF, 0x026C09FF, 0x1A4A0DFF, 0x860252FF, 0x1F2374FF, 0x283E37FF,
            0x850087FF, 0x670289FF, 0x432946FF, 0x141FA1FF, 0x3B5903FF, 0x4D3A2BFF, 0x432162FF, 0x540B9AFF,
            0x621868FF, 0x604E00FF, 0x0B4F4AFF, 0x1F7401FF, 0x683710FF, 0x933513FF, 0x076542FF, 0x3E2486FF,
            0x047F11FF, 0x1C3C94FF, 0x2E2EA1FF, 0x2D6922FF, 0x384451FF, 0x784C14FF, 0x822744FF, 0x61384EFF,
            0x008343FF, 0x1B7739FF, 0xB4470FFF, 0x4F542DFF, 0x295275FF, 0x478800FF, 0x663577FF, 0x0D6173FF,
            0x7B2284FF, 0x288A19FF, 0x497715FF, 0x07962DFF, 0xC7225AFF, 0x90327AFF, 0x44437CFF, 0x6B8400FF,
            0x694C65FF, 0xFE174AFF, 0x625E48FF, 0x3F6A51FF, 0x4447B6FF, 0x296192FF, 0x35687CFF, 0xC32484FF,
            0x794F4FFF, 0x4E458FFF, 0x935237FF, 0x05B91CFF, 0x50951AFF, 0x656D2DFF, 0x267F64FF, 0xC94F36FF,
            0x874881FF, 0x902E9DFF, 0x17946AFF, 0xB23B6FFF, 0x7835B6FF, 0xA52FB7FF, 0x6F8826FF, 0x54883FFF,
            0x9E4865FF, 0x6439EBFF, 0x819714FF, 0x8B6068FF, 0x686282FF, 0x85723EFF, 0x4169AAFF, 0x3D9159FF,
            0x00C262FF, 0x2FC11FFF, 0x5E50D3FF, 0x9140C5FF, 0x67706BFF, 0xAA6D3DFF, 0x6452BAFF, 0x8E5298FF,
            0x478278FF, 0x706198FF, 0x1EC248FF, 0x6D9A3AFF, 0x467B9AFF, 0xEB5167FF, 0x41A840FF, 0x9A8838FF,
            0x34988AFF, 0x2095B5FF, 0x9F795FFF, 0xCD32D0FF, 0xD0556EFF, 0x9350B3FF, 0x5A7AB3FF, 0x814CFCFF,
            0xB4667FFF, 0x6C5EF3FF, 0x7B7D8CFF, 0xF87737FF, 0xC449A3FF, 0x4480C8FF, 0x7875ACFF, 0xE53BCCFF,
            0x36B56FFF, 0xA451E3FF, 0x8265DEFF, 0xD97755FF, 0x7870C3FF, 0x8A9352FF, 0xBD8A4FFF, 0x82B33DFF,
            0x51A973FF, 0x947387FF, 0x6D8B6EFF, 0x4F9FA5FF, 0xAC57ABFF, 0x5DCD4AFF, 0x369FC5FF, 0xDF9E2BFF,
            0xA98E76FF, 0x73929DFF, 0x7EAE6BFF, 0x41CF5CFF, 0xBA799EFF, 0x5BBF6BFF, 0x2FBDA4FF, 0x8E9A8DFF,
            0xD2768FFF, 0xAAA551FF, 0xA078B8FF, 0x6CA9A1FF, 0x6F94BAFF, 0x37DB88FF, 0xA868FFFF, 0xBD67DEFF,
            0x9675D4FF, 0x918FACFF, 0x5DA8C8FF, 0xD19C59FF, 0x52C692FF, 0x7390D6FF, 0xD067C7FF, 0xC0A764FF,
            0xE963DAFF, 0xFD50DCFF, 0x99D848FF, 0xCC8E96FF, 0xA9BD6CFF, 0x84C472FF, 0x52E070FF, 0x9099C2FF,
            0xA88DD8FF, 0xE2889AFF, 0xF9A559FF, 0xCA8CCBFF, 0x6BBAD6FF, 0xC1C66EFF, 0xB4D859FF, 0xACA0A4FF,
            0x65CABEFF, 0xB69FCDFF, 0x9AB196FF, 0x77D88FFF, 0x9EE961FF, 0xD5B487FF, 0xA49EE8FF, 0xD875FEFF,
            0xFF73D9FF, 0x61F69AFF, 0xC3B393FF, 0xA1CF85FF, 0x99AED2FF, 0xF890ADFF, 0xF6BE7AFF, 0xBDBBBAFF,
            0x9DA2FFFF, 0xBBA0FEFF, 0xE395D5FF, 0xBC87F6FF, 0xBFA6E0FF, 0xCFAAC1FF, 0x8AB6F4FF, 0x91E7ACFF,
            0xC9FB5DFF, 0xB0D4AEFF, 0x8ECAC3FF, 0xD7E485FF, 0xF2EF57FF, 0xAEE790FF, 0xE195FAFF, 0xF1B1A7FF,
            0xADCDE6FF, 0xD4D0A5FF, 0xFDD195FF, 0xCAC6CFFF, 0xE4BFC7FF, 0x64FCD9FF, 0xEADEAEFF, 0xB2BBFDFF,
            0x84E1FFFF, 0x89FDBEFF, 0xF9CCC1FF, 0xCCCCE8FF, 0xD2EBB8FF, 0xF780FEFF, 0xB5EDD8FF, 0x9FD8FFFF,
            0xFEFF73FF, 0xD6BFFCFF, 0xE2E4E0FF, 0xAEFAA1FF, 0xDEF889FF, 0xFE9FEBFF, 0x9BF9DCFF, 0xFDFEABFF,
            0xFCE7C7FF, 0xAEF3FAFF, 0xD4FEBAFF, 0x82FFFDFF, 0xC7E0FCFF, 0xF5D5F0FF, 0xF7B8F8FF, 0xF4FCEEFF,
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
     * A rather nice palette with good coverage of various flesh tones as well as green and orange, which together are
     * hard for many palettes. It has somewhat poor coverage of purple and decent coverage of blue. It was generated by
     * a modified Lloyd relaxation on Flesurrect; the modifications allowed random alteration of colors to act somewhat
     * like annealing.
     */
    public static final int[] WARD = new int[] {
            0x00000000, 0x181029ff, 0x183121ff, 0x293139ff, 0x294a6bff, 0x397b84ff, 0x4aa5a5ff, 0x84c6d6ff,
            0xded6deff, 0xe7efd6ff, 0x5a3929ff, 0x525a8cff, 0x9c6b6bff, 0x635a31ff, 0xa5947bff, 0xe78494ff,
            0xef2929ff, 0x5a1021ff, 0xad3129ff, 0x8c5229ff, 0xc66b31ff, 0xe7a56bff, 0xef7331ff, 0xd6b5a5ff,
            0xe7d68cff, 0xef9c29ff, 0xb59431ff, 0xdebd31ff, 0xe7e731ff, 0x9cb542ff, 0xbdef52ff, 0xa5d639ff,
            0x638c29ff, 0xc6e7a5ff, 0x42ef31ff, 0x39c631ff, 0x215221ff, 0x4aef84ff, 0x39b54aff, 0x42d694ff,
            0x52e7bdff, 0x9cefdeff, 0x219442ff, 0x42bdbdff, 0x296b42ff, 0x42e7e7ff, 0x426be7ff, 0x4a9cdeff,
            0x313173ff, 0x4273c6ff, 0x4242b5ff, 0x3118c6ff, 0x391894ff, 0x845ad6ff, 0xb57bd6ff, 0x311063ff,
            0xa521d6ff, 0x5a21deff, 0xd6a5deff, 0xb52984ff, 0xde31deff, 0x842142ff, 0xe73984ff, 0xc62131ff,
    };


    /**
     * An interesting and potentially-useful palette that behaves better as part of a Bonus Colorizer. Made by drawing a
     * straight line from black to white through the grayscale section of an RGB cube, then drawing curved zig-zagging
     * lines from close-to-black to close-to-white that go through red, green, blue, cyan, magenta, and yellow. The
     * zig-zag moves every-other color into a desaturated area. The normal way of getting a Bonus Colorizer will (almost
     * accidentally) add orange/brown where the desaturated yellow or red would be, and tends to have good coverage.
     */
    public static final int[] CUBICLE64 = {
            0x00000000, 0x000000FF, 0x3B3B3BFF, 0x6F6F6FFF, 0x9B9B9BFF, 0xBFBFBFFF, 0xDBDBDBFF, 0xEFEFEFFF,
            0xFBFBFBFF, 0xFFFFFFFF, 0x320000FF, 0x640A0AFF, 0xAA0000FF, 0xBC3E3EFF, 0xFF3C3CFF, 0xFF7272FF,
            0xFF8C8CFF, 0xFFA6A6FF, 0xFFDCDCFF, 0x003838FF, 0x006A6AFF, 0x308484FF, 0x1ADADAFF, 0x70D0D0FF,
            0x6EFFFFFF, 0xB0FFFFFF, 0xC2FFFFFF, 0xDCFFFFFF, 0x003200FF, 0x0A640AFF, 0x00AA00FF, 0x3EBC3EFF,
            0x3CFF3CFF, 0x72FF72FF, 0x8CFF8CFF, 0xA6FFA6FF, 0xDCFFDCFF, 0x380038FF, 0x6A006AFF, 0x843084FF,
            0xDA1ADAFF, 0xD070D0FF, 0xFF6EFFFF, 0xFFB0FFFF, 0xFFC2FFFF, 0xFFDCFFFF, 0x000032FF, 0x0A0A64FF,
            0x0000AAFF, 0x3E3EBCFF, 0x3C3CFFFF, 0x7272FFFF, 0x8C8CFFFF, 0xA6A6FFFF, 0xDCDCFFFF, 0x383800FF,
            0x6A6A00FF, 0x848430FF, 0xDADA1AFF, 0xD0D070FF, 0xFFFF6EFF, 0xFFFFB0FF, 0xFFFFC2FF, 0xFFFFDCFF,
    };
    
    public static final int[] LABRADOR256 = {
            0x000000FF, 0x000020FF, 0x100050FF, 0x2F0E8EFF, 0x2810F0FF, 0x002000FF, 0x002020FF, 0x002040FF,
            0x102070FF, 0x104000FF, 0x104020FF, 0x104040FF, 0x104060FF, 0x104090FF, 0x3440B0FF, 0x2840F0FF,
            0x106010FF, 0x106040FF, 0x106060FF, 0x106080FF, 0x316095FF, 0x2860FFFF, 0x288010FF, 0x288040FF,
            0x288060FF, 0x288080FF, 0x2880A0FF, 0x1C80C8FF, 0x4A80F4FF, 0x44A028FF, 0x44A060FF, 0x44A080FF,
            0x44A0A0FF, 0x44A0C0FF, 0x36A0ECFF, 0x5AC028FF, 0x44C070FF, 0x44C0A0FF, 0x62C0C0FF, 0x62C0E0FF,
            0x44C0FFFF, 0x5AE028FF, 0x44E070FF, 0x44E0A0FF, 0x44E0C0FF, 0x6AE0E0FF, 0x44E0FFFF, 0x62FF4CFF,
            0x6AFFB0FF, 0x6AFFE0FF, 0x6AFFFFFF, 0x200000FF, 0x200020FF, 0x202000FF, 0x202020FF, 0x202040FF,
            0x400000FF, 0x400020FF, 0x400040FF, 0x401060FF, 0x402000FF, 0x402020FF, 0x402040FF, 0x404000FF,
            0x404020FF, 0x404040FF, 0x404060FF, 0x404080FF, 0x406010FF, 0x406040FF, 0x406060FF, 0x600000FF,
            0x600020FF, 0x601040FF, 0x601060FF, 0x601080FF, 0x7010B0FF, 0x7028F0FF, 0x602000FF, 0x602020FF,
            0x604000FF, 0x604020FF, 0x604040FF, 0x604060FF, 0x604080FF, 0x7040B0FF, 0x606010FF, 0x606040FF,
            0x606060FF, 0x606080FF, 0x6060B0FF, 0x7060F0FF, 0x608010FF, 0x608040FF, 0x608060FF, 0x608080FF,
            0x6080B0FF, 0x901000FF, 0x801020FF, 0x801040FF, 0x801060FF, 0x801080FF, 0x804000FF, 0x804020FF,
            0x804040FF, 0x804060FF, 0x804080FF, 0x806010FF, 0x806040FF, 0x806060FF, 0x806080FF, 0x8060A0FF,
            0x8060C0FF, 0x808028FF, 0x808060FF, 0x808080FF, 0x8080A0FF, 0x8080C0FF, 0x8080F0FF, 0x80A028FF,
            0x80A060FF, 0x80A080FF, 0x80A0A0FF, 0x80A0D8FF, 0x8CC078FF, 0x80C0A0FF, 0x90C0F8FF, 0x90E070FF,
            0x90E0A0FF, 0x90E0C0FF, 0x90E0FFFF, 0xA01020FF, 0xA01040FF, 0xA01060FF, 0xA81C80FF, 0xA425B2FF,
            0xA822F0FF, 0xA04010FF, 0xA04040FF, 0xA04060FF, 0xA06010FF, 0xA06040FF, 0xA06060FF, 0xA06080FF,
            0xA060A0FF, 0xA060C0FF, 0xB060F0FF, 0xA08028FF, 0xA08060FF, 0xA08080FF, 0xA080A0FF, 0xA080C0FF,
            0xB080F0FF, 0xA0A028FF, 0xA0A060FF, 0xA0A080FF, 0xA0A0A0FF, 0xA0A0C0FF, 0xA0A0F0FF, 0xA0C028FF,
            0xA0C0A0FF, 0xA0C0C0FF, 0xB0E028FF, 0xB8FF4CFF, 0xC01010FF, 0xC81C40FF, 0xC81C60FF, 0xC04010FF,
            0xD03480FF, 0xC06010FF, 0xC06040FF, 0xC06060FF, 0xC06080FF, 0xC060A0FF, 0xC060C0FF, 0xC08010FF,
            0xC08040FF, 0xC08060FF, 0xC08080FF, 0xC080A0FF, 0xC080C0FF, 0xC0A028FF, 0xC0A060FF, 0xC0A080FF,
            0xC0A0A0FF, 0xC0A0C0FF, 0xC0A0F0FF, 0xC0C028FF, 0xC0C070FF, 0xC0C0A0FF, 0xC0C0C0FF, 0xC0C0E0FF,
            0xC0C0FFFF, 0xC0E070FF, 0xC0E0A0FF, 0xC0E0C0FF, 0xC0E0E0FF, 0xC0E0FFFF, 0xCCFFB8FF, 0xC0FFE0FF,
            0xC0FFFFFF, 0xE82210FF, 0xE028B0FF, 0xEC36F0FF, 0xF03440FF, 0xF03460FF, 0xF06010FF, 0xF06040FF,
            0xF06060FF, 0xF06080FF, 0xF060A0FF, 0xF844B8FF, 0xE08010FF, 0xF88028FF, 0xF08060FF, 0xF08080FF,
            0xF080A0FF, 0xF880D0FF, 0xE880F0FF, 0xE0A028FF, 0xE0A060FF, 0xF0A080FF, 0xF0A0A0FF, 0xF0A0C0FF,
            0xE8A0F0FF, 0xE0C028FF, 0xE0C070FF, 0xE0C0A0FF, 0xE0C0C0FF, 0xE0C0E0FF, 0xE0C0FFFF, 0xE0E04CFF,
            0xE0E0A0FF, 0xE0E0C0FF, 0xF0F0F0FF, 0xE0E0FFFF, 0xF0FF4CFF, 0xE0FFE0FF, 0xE0FFFFFF, 0xFF2880FF,
            0xFFA028FF, 0xFFA060FF, 0xFFA0E0FF, 0xFFC028FF, 0xFFC070FF, 0xFFC0A0FF, 0xFFC0C0FF, 0xFFC0E0FF,
            0xFFC0FFFF, 0xFFE04CFF, 0xFFE0A0FF, 0xFFE0C0FF, 0xFFE0E0FF, 0xFFE0FFFF, 0xFFFFB0FF, 0xFFFFE0FF,
    };

    /**
     * A 64-color palette that includes transparent and uses colors distributed slightly non-uniformly through space and
     * compared for similarity with a rough LAB metric. The slight non-uniformity biases it away from desaturated
     * colors, but not so strongly that they are missing.
     */
    public static final int[] LAWN64 = {
            0x00000000, 0x31E635FF, 0x0F368AFF, 0x1CBDB2FF, 0x341DE9FF, 0xA56D07FF, 0xD9EA22FF, 0xE41DA2FF,
            0x21107CFF, 0x3D81BCFF, 0x901720FF, 0xBCB71EFF, 0xE4F7A9FF, 0xEB6AF3FF, 0x1A0C1CFF, 0x9F9BB0FF,
            0xB9EFE8FF, 0xEA1D21FF, 0x1EBC51FF, 0x3050CAFF, 0x32DCECFF, 0xB81D71FF, 0xDE9581FF, 0x45F7D8FF,
            0x645F1BFF, 0xA7EA8EFF, 0xA220EBFF, 0x12703DFF, 0x251FB2FF, 0x35A6E6FF, 0xC65028FF, 0xEED483FF,
            0x328F31FF, 0x90F521FF, 0xD9BEDDFF, 0xEF16E6FF, 0x33510EFF, 0xB29FFBFF, 0x3C80F3FF, 0x0C6F96FF,
            0x07210CFF, 0x348D8AFF, 0x49435EFF, 0x791781FF, 0xC88536FF, 0xFBFC37FF, 0x99CF37FF, 0x1F391BFF,
            0x8FB079FF, 0xEEB30BFF, 0x56100EFF, 0xB2CFA6FF, 0xEF7215FF, 0xF6B093FF, 0x3FDD8CFF, 0xEF66B2FF,
            0xF3F3E1FF, 0x8F6890FF, 0xF56F71FF, 0xF5A2E6FF, 0x8F9E1CFF, 0xA472DBFF, 0x8CC8FCFF, 0x934F46FF
    };

    /**
     * A 256-color palette that includes transparent and uses colors distributed slightly non-uniformly through space
     * and compared for similarity with a rough LAB metric. The slight non-uniformity biases it away from desaturated
     * colors, but not so strongly that they are missing.
     */
    public static final int[] LAWN256 = {
            0x00000000, 0x25EF13FF, 0x0F368AFF, 0x4DB6C0FF, 0x7305F8FF, 0xB16304FF, 0xE0E711FF, 0xF91459FF,
            0x11098DFF, 0x107FDEFF, 0x7A2A07FF, 0xB7AD51FF, 0xEBFE9FFF, 0xFC4FDDFF, 0x2AD613FF, 0x11061DFF,
            0x520968FF, 0x898EB8FF, 0xAFFAF8FF, 0xE93D09FF, 0x26A90DFF, 0x110C4EFF, 0x3D5BB8FF, 0x33D7F9FF,
            0xA00918FF, 0xC01E46FF, 0xE39C8DFF, 0x2CF6CEFF, 0x183AF3FF, 0x17C239FF, 0x34060DFF, 0x606841FF,
            0x9BE194FF, 0xD015E5FF, 0xF80E0AFF, 0x13731DFF, 0x11F76FFF, 0x47189BFF, 0x6DA4D9FF, 0xD35427FF,
            0xF7D466FF, 0x2206B3FF, 0x2514F0FF, 0x0D9425FF, 0x7BFC1AFF, 0xA6377CFF, 0xD7BDC8FF, 0xF20AF3FF,
            0x1D5D06FF, 0x7617DAFF, 0xBC97F7FF, 0xD7FA0FFF, 0xFA4505FF, 0x2D6FE4FF, 0x87EC08FF, 0xB60B08FF,
            0xDF0F53FF, 0x1F76A3FF, 0x09E6EBFF, 0x07210CFF, 0x93FB57FF, 0xC83FA9FF, 0xEFC3E8FF, 0x208381FF,
            0xA307DDFF, 0xD4B024FF, 0x045286FF, 0x0AD6D9FF, 0x619304FF, 0x0ADC64FF, 0x6D05B5FF, 0xD27E06FF,
            0xF9F911FF, 0x0E246EFF, 0x05A9C3FF, 0x38F7F3FF, 0x6E5005FF, 0xA3D716FF, 0xDD0684FF, 0xEC21D9FF,
            0x048EFCFF, 0x3F2D38FF, 0x7BB48BFF, 0xEA60FFFF, 0xF2DD21FF, 0x3F9E8BFF, 0x79FECEFF, 0xB6460AFF,
            0xE0C806FF, 0x056AA9FF, 0x4C0907FF, 0x81061FFF, 0xBE8366FF, 0xE8F1B5FF, 0x545576FF, 0x90D4C3FF,
            0x152EBCFF, 0x39B2F6FF, 0x67081DFF, 0x946301FF, 0xC7E534FF, 0xF11387FF, 0x1B17D5FF, 0x6B414FFF,
            0xA5C5A2FF, 0xF37D28FF, 0x5575F4FF, 0xABF008FF, 0xE50720FF, 0xFBA371FF, 0x0FFBB1FF, 0xB20688FF,
            0xE87DC4FF, 0xFEF2FCFF, 0x0C330CFF, 0x41043FFF, 0x845795FF, 0xB6D9D5FF, 0x061004FF, 0x04945EFF,
            0x7FBF16FF, 0xD2211CFF, 0xEB6663FF, 0x2CE2B3FF, 0x508513FF, 0x91EF7DFF, 0xC31AC4FF, 0xF6AFF8FF,
            0x0F521DFF, 0x27D389FF, 0x9E8B02FF, 0x09BA9CFF, 0x73D614FF, 0xA70566FF, 0xDF12B0FF, 0xF092E4FF,
            0x1C4010FF, 0x13BF71FF, 0xB863E6FF, 0x1385C7FF, 0x819451FF, 0xB8F9A3FF, 0x1A665EFF, 0xE99A0FFF,
            0x0546B3FF, 0x21BFE3FF, 0x917711FF, 0xCAE77BFF, 0xF6BF04FF, 0x9E3509FF, 0xF922ACFF, 0x4458E8FF,
            0x710E6AFF, 0xAF92AEFF, 0xDEF0EDFF, 0x6AE0F8FF, 0xFF787FFF, 0xB95148FF, 0xEFD396FF, 0x8B33AAFF,
            0xC1B9E9FF, 0xA49D29FF, 0xED4981FF, 0x04C8BAFF, 0xD27DFCFF, 0x04B150FF, 0x0D93A9FF, 0x393303FF,
            0xDB64C9FF, 0xF0DFF6FF, 0x0B1B36FF, 0xAE46F8FF, 0x056E85FF, 0xC56E06FF, 0x0EA1E2FF, 0x4F4909FF,
            0x90CA57FF, 0xC8089FFF, 0x5D3539FF, 0x98BB8CFF, 0x677FA4FF, 0xABEAE6FF, 0xF6AA10FF, 0xD98E74FF,
            0xFBF9CEFF, 0x786084FF, 0x3A44D2FF, 0x8CC8FCFF, 0xEA6702FF, 0xE9AFA8FF, 0x910FB6FF, 0xF897C1FF,
            0xA16B87FF, 0xD3E3D0FF, 0x057459FF, 0x17ED99FF, 0xFA4861FF, 0x788913FF, 0xB2F272FF, 0xB811FAFF,
            0x05384DFF, 0x56230BFF, 0x93A865FF, 0xD5FB9AFF, 0x72E4A9FF, 0xE1D379FF, 0x833489FF, 0xBABAD2FF,
            0x8C9FE0FF, 0xBBFF0AFF, 0xC8006CFF, 0x302900FF, 0x5EB121FF, 0xAD36A0FF, 0xDCBCE3FF, 0xB47906FF,
            0x8D0394FF, 0xC5C40BFF, 0x647E73FF, 0x8BEECDFF, 0xD09003FF, 0x755F0BFF, 0xAEDB55FF, 0x2F426FFF,
            0x69C6BDFF, 0x930645FF, 0xC3859AFF, 0x96D5E9FF, 0xA7A7FFFF, 0xFCB5ADFF, 0x876BFAFF, 0x89A703FF,
            0x5C8F4BFF, 0x97F69EFF, 0xDBFA62FF, 0x7BE273FF, 0x934F46FF, 0xC8D098FF, 0x700598FF, 0xAA81DDFF,
            0xB862C4FF, 0x8943FEFF, 0xFDFF5DFF, 0x9CA9B1FF, 0x6A8CF5FF, 0xF7B782FF, 0xAC1642FF, 0x7B6DA5FF,
            0xF47AF1FF, 0xADB404FF, 0xFB5A9CFF, 0xD1C55EFF, 0xA676BDFF, 0xB68F0CFF, 0x4B6119FF, 0xCBA049FF,
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
     * A 64-color palette that started with specific colors from NamedColor's Color Wheel palette, then lightened and
     * darkened to get 5 variants on all colors (except 8 for grayscale), and finally ran the whole set through Lloyd
     * relaxation to improve the worst similarity between any two colors. It is very good for a 64-color palette, and
     * its only real weakness is a somewhat poor coverage of low-saturation colors.
     */
    public static final int[] RELAXED_ROLL = {
            0x00000000, 0x100818ff, 0x181818ff, 0x314a6bff, 0x396b7bff, 0x4a9494ff, 0xa5b5adff, 0xb5e7e7ff,
            0xf7efefff, 0x6b1831ff, 0xbd5242ff, 0xef6b4aff, 0xef9c9cff, 0xf7c6deff, 0x6b3921ff, 0xbd8421ff,
            0xefa531ff, 0xe7ce42ff, 0xefd6a5ff, 0x292921ff, 0x7b5231ff, 0x8c7339ff, 0xb59473ff, 0xcec6a5ff,
            0x316b31ff, 0xadbd42ff, 0xefef39ff, 0xeff79cff, 0xe7f7deff, 0x215a21ff, 0x52bd39ff, 0x84e731ff,
            0xb5ef42ff, 0xbdef9cff, 0x295221ff, 0x29ad29ff, 0x31e729ff, 0x39ef7bff, 0x52f7b5ff, 0x214221ff,
            0x318439ff, 0x42ad84ff, 0x4aceadff, 0x5ae7e7ff, 0x180842ff, 0x3118a5ff, 0x3921deff, 0x428cc6ff,
            0x42bde7ff, 0x293163ff, 0x4a63b5ff, 0x5a84efff, 0x9ca5e7ff, 0xced6efff, 0x211073ff, 0x5a3194ff,
            0x8431d6ff, 0xb573b5ff, 0xc6bde7ff, 0x421039ff, 0xa5214aff, 0xde2152ff, 0xde31ceff, 0xe784deff,
    };

    public static final byte[][] REALLY_RELAXED_ROLL_RAMPS = new byte[][]{
            { 0, 0, 0, 0 },
            { 1, 1, 1, 3 },
            { 1, 1, 2, 39 },
            { 1, 44, 3, 47 },
            { 44, 49, 4, 57 },
            { 55, 60, 5, 57 },
            { 21, 57, 6, 12 },
            { 63, 13, 7, 13 },
            { 57, 58, 8, 9 },
            { 19, 39, 9, 34 },
            { 29, 40, 10, 35 },
            { 35, 30, 11, 6 },
            { 41, 6, 12, 6 },
            { 6, 7, 13, 28 },
            { 39, 34, 14, 34 },
            { 35, 21, 15, 6 },
            { 22, 6, 16, 23 },
            { 25, 23, 17, 23 },
            { 23, 33, 18, 28 },
            { 1, 1, 19, 34 },
            { 34, 24, 20, 40 },
            { 29, 24, 21, 6 },
            { 14, 20, 22, 6 },
            { 22, 6, 23, 18 },
            { 9, 14, 24, 20 },
            { 21, 22, 25, 12 },
            { 23, 18, 26, 27 },
            { 25, 23, 27, 9 },
            { 12, 13, 28, 9 },
            { 2, 59, 29, 9 },
            { 20, 21, 30, 57 },
            { 15, 12, 31, 12 },
            { 25, 23, 32, 18 },
            { 21, 12, 33, 13 },
            { 59, 19, 34, 14 },
            { 14, 20, 35, 20 },
            { 10, 21, 36, 12 },
            { 61, 57, 37, 12 },
            { 57, 63, 38, 13 },
            { 2, 59, 39, 59 },
            { 14, 20, 40, 20 },
            { 60, 57, 41, 57 },
            { 60, 57, 42, 57 },
            { 57, 63, 43, 63 },
            { 1, 1, 44, 49 },
            { 44, 54, 45, 49 },
            { 1, 54, 46, 55 },
            { 55, 4, 47, 57 },
            { 62, 57, 48, 57 },
            { 1, 44, 49, 50 },
            { 54, 3, 50, 4 },
            { 56, 50, 51, 47 },
            { 50, 57, 52, 57 },
            { 57, 63, 53, 58 },
            { 1, 1, 54, 49 },
            { 49, 3, 55, 3 },
            { 3, 50, 56, 50 },
            { 5, 41, 57, 6 },
            { 51, 47, 58, 13 },
            { 1, 1, 59, 39 },
            { 34, 24, 60, 24 },
            { 24, 40, 61, 41 },
            { 47, 5, 62, 5 },
            { 41, 58, 63, 58 },
    };

    public static final int[] REALLY_RELAXED_ROLL = new int[]{
            0x00000000, 0x100818FF, 0x101818FF, 0x314A6BFF, 0x396B7BFF, 0x399484FF, 0x94B58CFF, 0x84E7DEFF,
            0xDED6E7FF, 0x6B1031FF, 0xBD5229FF, 0xEF6339FF, 0xDE9494FF, 0xE7ADD6FF, 0x6B3121FF, 0xC68439FF,
            0xE7A531FF, 0xDECE31FF, 0xEFD694FF, 0x212921FF, 0x7B5231FF, 0x8C7339FF, 0x949431FF, 0xC6C68CFF,
            0x316B31FF, 0x9CBD31FF, 0xE7EF29FF, 0xE7F78CFF, 0xD6F7DEFF, 0x185A10FF, 0x39C631FF, 0x84E731FF,
            0xB5EF39FF, 0xA5EF9CFF, 0x315229FF, 0x31A529FF, 0x31EF29FF, 0x39EF73FF, 0x42F7B5FF, 0x184221FF,
            0x318C29FF, 0x39AD7BFF, 0x42CE8CFF, 0x39E7D6FF, 0x180842FF, 0x2910A5FF, 0x3110DEFF, 0x4284ADFF,
            0x39BDCEFF, 0x293163FF, 0x4A52A5FF, 0x526BDEFF, 0x4A9CD6FF, 0x63CEE7FF, 0x291073FF, 0x522994FF,
            0x6B29D6FF, 0xAD6B9CFF, 0x9CA5DEFF, 0x421029FF, 0xA5214AFF, 0xDE214AFF, 0xCE29CEFF, 0xDE6BD6FF,
    };

    public static final int[] REALLY_RELAXED_ROLL_BONUS = new int[]{
            0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x0A0411FF, 0x0C0712FF, 0x100818FF, 0x130E18FF,
            0x091010FF, 0x0D1313FF, 0x101818FF, 0x191E1EFF, 0x1D314BFF, 0x2B3C54FF, 0x314A6BFF, 0x506076FF,
            0x204855FF, 0x335662FF, 0x396B7BFF, 0x64858FFF, 0x1C6659FF, 0x36766BFF, 0x399484FF, 0x73AFA4FF,
            0x5E785EFF, 0x7D947DFF, 0x94B594FF, 0xD4E9D4FF, 0x529C94FF, 0x7BBBB5FF, 0x8CE7DEFF, 0xE4FFFFFF,
            0x979097FF, 0xBFB9BFFF, 0xE7DEE7FF, 0xFFFFFFFF, 0x4D041FFF, 0x521229FF, 0x6B1031FF, 0x6B3046FF,
            0x86300FFF, 0x94482CFF, 0xBD5229FF, 0xCD876CFF, 0xA93917FF, 0xBB583BFF, 0xEF6339FF, 0xFFA78BFF,
            0x9E5B5BFF, 0xB97F7FFF, 0xE79494FF, 0xFFDEDEFF, 0xA0728CFF, 0xC29AB1FF, 0xEFB5D6FF, 0xFFFFFFFF,
            0x4B1D10FF, 0x542B20FF, 0x6B3121FF, 0x765045FF, 0x89540BFF, 0x9E6F2FFF, 0xC68429FF, 0xE7BC80FF,
            0x9F6A0CFF, 0xB98A39FF, 0xE7A531FF, 0xFFE69BFF, 0x95880AFF, 0xB5A93BFF, 0xDECE31FF, 0xFFFFA7FF,
            0x9F8B56FF, 0xC4B284FF, 0xEFD694FF, 0xFFFFF7FF, 0x141B14FF, 0x1C211CFF, 0x212921FF, 0x2F342FFF,
            0x543319FF, 0x62452EFF, 0x7B5231FF, 0x917761FF, 0x5E4A1CFF, 0x716037FF, 0x8C7339FF, 0xAD9D77FF,
            0x67612CFF, 0x807A4CFF, 0x9C9452FF, 0xCAC499FF, 0x828253FF, 0xA4A47BFF, 0xC6C68CFF, 0xFFFFE1FF,
            0x1B4A1BFF, 0x2C552CFF, 0x316B31FF, 0x567C56FF, 0x657F0FFF, 0x829937FF, 0x9CBD31FF, 0xD5EB8FFF,
            0x99A001FF, 0xBDC338FF, 0xE7EF29FF, 0xFFFFAFFF, 0x97A44EFF, 0xC0CB80FF, 0xE7F78CFF, 0xFFFFFBFF,
            0x89A38FFF, 0xB4CBBAFF, 0xD6F7DEFF, 0xFFFFFFFF, 0x0A3F04FF, 0x184612FF, 0x185A10FF, 0x376231FF,
            0x1A8B14FF, 0x389B32FF, 0x39C631FF, 0x7EDA78FF, 0x509F0DFF, 0x73B838FF, 0x84E731FF, 0xCEFF98FF,
            0x74A210FF, 0x98C141FF, 0xB5EF39FF, 0xFEFFADFF, 0x66A15FFF, 0x8EC287FF, 0xA5EF9CFF, 0xF8FFF2FF,
            0x1D3817FF, 0x2A4225FF, 0x315229FF, 0x4D6248FF, 0x1D7410FF, 0x36822BFF, 0x39A529FF, 0x71B867FF,
            0x11AA0BFF, 0x35BA2FFF, 0x31EF29FF, 0x84FF7FFF, 0x16A844FF, 0x3CBC65FF, 0x39EF73FF, 0x92FFB8FF,
            0x1BAC77FF, 0x44C395FF, 0x42F7B5FF, 0xA3FFEEFF, 0x0C2E13FF, 0x16341DFF, 0x184221FF, 0x304B35FF,
            0x196212FF, 0x2E6E29FF, 0x318C29FF, 0x619D5CFF, 0x1B7850FF, 0x388966FF, 0x39AD7BFF, 0x7BC7A6FF,
            0x1E8F60FF, 0x41A37BFF, 0x42CE94FF, 0x91EDC7FF, 0x14A099FF, 0x3DB7B1FF, 0x39E7DEFF, 0x98FFFFFF,
            0x100331FF, 0x130831FF, 0x180842FF, 0x1E143AFF, 0x1A067DFF, 0x221079FF, 0x2910A5FF, 0x39298AFF,
            0x1F04AAFF, 0x2912A3FF, 0x3110DEFF, 0x4630B6FF, 0x24597AFF, 0x3D6B88FF, 0x4284ADFF, 0x7AA5BFFF,
            0x188296FF, 0x3A97A9FF, 0x39BDD6FF, 0x89DFEFFF, 0x191F47FF, 0x23294CFF, 0x293163FF, 0x3F4465FF,
            0x2D3A7CFF, 0x404B85FF, 0x4A5AADFF, 0x727DB3FF, 0x3644A0FF, 0x4E5AABFF, 0x5A6BDEFF, 0x8B96E1FF,
            0x33689DFF, 0x517FAEFF, 0x5A9CDEFF, 0x9CC7F2FF, 0x60889CFF, 0x86A9BBFF, 0x9CCEE7FF, 0xEBFFFFFF,
            0x1B0756FF, 0x211055FF, 0x291073FF, 0x352465FF, 0x37166CFF, 0x422571FF, 0x522994FF, 0x664B91FF,
            0x48139EFF, 0x5628A1FF, 0x6B29D6FF, 0x8358C9FF, 0x7C4068FF, 0x915D80FF, 0xB56B9CFF, 0xD9A8C8FF,
            0x6F6F97FF, 0x9090B3FF, 0xADADDEFF, 0xEEEEFFFF, 0x2F071BFF, 0x331021FF, 0x421029FF, 0x452535FF,
            0x770D2EFF, 0x7F223FFF, 0xA5214AFF, 0xA9536DFF, 0xA10A2BFF, 0xAA2542FF, 0xDE214AFF, 0xDD627CFF,
            0x920E92FF, 0xA02CA0FF, 0xCE29CEFF, 0xDD71DDFF, 0x993D93FF, 0xB160ABFF, 0xDE6BD6FF, 0xFFB8FEFF,
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
}
