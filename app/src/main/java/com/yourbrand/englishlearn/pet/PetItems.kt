package com.yourbrand.englishlearn.pet

/**
 * Pet home decor + accessories. Bought only with coins earned by learning (no real money).
 * Everything is drawn with Canvas in [RoomView] / [PetView], so the catalog adds no image assets.
 */
data class PetItem(val id: String, val slot: String, val vi: String, val en: String, val price: Int, val minStage: Int = 1, val emoji: String)

object PetItems {
    const val THEME = "theme"
    const val BED = "bed"
    const val LAMP = "lamp"
    const val PLANT = "plant"
    const val RUG = "rug"
    const val WALL = "wall"
    const val TOY = "toy"
    const val HAT = "hat"
    const val FACE = "face"
    const val NECK = "neck"

    val slots = listOf(THEME, BED, RUG, PLANT, LAMP, WALL, TOY, HAT, FACE, NECK)

    val all = listOf(
        PetItem("theme_cozy", THEME, "Phòng ấm áp", "Cozy room", 0, emoji = "🏠"),
        PetItem("theme_garden", THEME, "Khu vườn", "Garden", 120, emoji = "🌷"),
        PetItem("theme_beach", THEME, "Bãi biển", "Beach", 180, 2, "🏖️"),
        PetItem("theme_candy", THEME, "Xứ kẹo ngọt", "Candy land", 220, 3, "🍭"),
        PetItem("theme_forest", THEME, "Rừng xanh", "Forest", 260, 4, "🌲"),
        PetItem("theme_space", THEME, "Vũ trụ", "Space", 380, 6, "🚀"),
        PetItem("theme_castle", THEME, "Lâu đài", "Castle", 600, 9, "🏰"),

        PetItem("bed_basket", BED, "Giỏ ngủ", "Basket", 0, emoji = "🧺"),
        PetItem("bed_cushion", BED, "Nệm êm", "Cushion", 60, emoji = "🛏️"),
        PetItem("bed_cloud", BED, "Giường mây", "Cloud bed", 160, 3, "☁️"),
        PetItem("bed_royal", BED, "Giường hoàng gia", "Royal bed", 420, 7, "👑"),

        PetItem("rug_round", RUG, "Thảm tròn", "Round rug", 40, emoji = "⭕"),
        PetItem("rug_rainbow", RUG, "Thảm cầu vồng", "Rainbow rug", 120, 2, "🌈"),
        PetItem("rug_paw", RUG, "Thảm dấu chân", "Paw rug", 90, emoji = "🐾"),

        PetItem("plant_cactus", PLANT, "Xương rồng", "Cactus", 30, emoji = "🌵"),
        PetItem("plant_monstera", PLANT, "Cây lá xẻ", "Monstera", 70, emoji = "🪴"),
        PetItem("plant_sunflower", PLANT, "Hoa hướng dương", "Sunflower", 110, 2, "🌻"),

        PetItem("lamp_floor", LAMP, "Đèn cây", "Floor lamp", 50, emoji = "💡"),
        PetItem("lamp_star", LAMP, "Đèn ngôi sao", "Star lamp", 140, 3, "⭐"),
        PetItem("lamp_lava", LAMP, "Đèn dung nham", "Lava lamp", 200, 5, "🫧"),

        PetItem("wall_abc", WALL, "Tranh ABC", "ABC poster", 40, emoji = "🔤"),
        PetItem("wall_clock", WALL, "Đồng hồ", "Clock", 80, emoji = "🕰️"),
        PetItem("wall_map", WALL, "Bản đồ thế giới", "World map", 150, 4, "🗺️"),

        PetItem("toy_ball", TOY, "Quả bóng", "Ball", 30, emoji = "⚽"),
        PetItem("toy_yarn", TOY, "Cuộn len", "Yarn", 45, emoji = "🧶"),
        PetItem("toy_teddy", TOY, "Gấu bông", "Teddy", 130, 3, "🧸"),
        PetItem("toy_books", TOY, "Chồng sách", "Book stack", 90, 2, "📚"),

        PetItem("hat_party", HAT, "Mũ tiệc", "Party hat", 60, emoji = "🥳"),
        PetItem("hat_beanie", HAT, "Mũ len", "Beanie", 80, emoji = "🧢"),
        PetItem("hat_flower", HAT, "Vòng hoa", "Flower crown", 140, 3, "🌸"),
        PetItem("hat_grad", HAT, "Mũ tốt nghiệp", "Graduation cap", 260, 5, "🎓"),
        PetItem("hat_crown", HAT, "Vương miện", "Crown", 500, 10, "👑"),

        PetItem("face_round", FACE, "Kính tròn", "Round glasses", 70, emoji = "👓"),
        PetItem("face_star", FACE, "Kính ngôi sao", "Star shades", 150, 4, "🕶️"),

        PetItem("neck_bow", NECK, "Nơ cổ", "Bow tie", 50, emoji = "🎀"),
        PetItem("neck_scarf", NECK, "Khăn quàng", "Scarf", 90, 2, "🧣"),
        PetItem("neck_medal", NECK, "Huy chương", "Medal", 300, 8, "🏅"),
    )

    val byId = all.associateBy { it.id }
    fun inSlot(slot: String) = all.filter { it.slot == slot }

    /** The theme is always set (defaults to the free cozy room); other slots may be empty. */
    fun equipped(state: PetState, slot: String): PetItem? =
        state.equipped[slot]?.let { byId[it] } ?: if (slot == THEME) byId["theme_cozy"] else if (slot == BED) byId["bed_basket"] else null
}

/** Species are cosmetic. Original designs (no franchise look-alikes). */
object Species {
    val all = listOf("cat", "dog", "dragon")
    fun nameVi(s: String) = when (s) { "dog" -> "Cún"; "dragon" -> "Rồng con"; else -> "Mèo" }
    fun defaultName(s: String) = when (s) { "dog" -> "Bông"; "dragon" -> "Lửa"; else -> "Miu" }
}
