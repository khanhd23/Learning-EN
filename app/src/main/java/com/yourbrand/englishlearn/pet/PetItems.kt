package com.yourbrand.englishlearn.pet

import com.yourbrand.englishlearn.R

/** Pet home decor and accessories. Names are localized through Android resources. */
data class PetItem(val id: String, val slot: String, val nameRes: Int, val price: Int, val minStage: Int = 1, val emoji: String)

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
        PetItem("theme_cozy", THEME, R.string.pet_item_theme_cozy, 0, emoji = "🏠"),
        PetItem("theme_garden", THEME, R.string.pet_item_theme_garden, 120, emoji = "🌷"),
        PetItem("theme_beach", THEME, R.string.pet_item_theme_beach, 180, 2, "🏖️"),
        PetItem("theme_candy", THEME, R.string.pet_item_theme_candy, 220, 3, "🍭"),
        PetItem("theme_forest", THEME, R.string.pet_item_theme_forest, 260, 4, "🌲"),
        PetItem("theme_space", THEME, R.string.pet_item_theme_space, 380, 6, "🚀"),
        PetItem("theme_castle", THEME, R.string.pet_item_theme_castle, 600, 9, "🏰"),

        PetItem("bed_basket", BED, R.string.pet_item_bed_basket, 0, emoji = "🧺"),
        PetItem("bed_cushion", BED, R.string.pet_item_bed_cushion, 60, emoji = "🛏️"),
        PetItem("bed_cloud", BED, R.string.pet_item_bed_cloud, 160, 3, "☁️"),
        PetItem("bed_royal", BED, R.string.pet_item_bed_royal, 420, 7, "👑"),

        PetItem("rug_round", RUG, R.string.pet_item_rug_round, 40, emoji = "⭕"),
        PetItem("rug_rainbow", RUG, R.string.pet_item_rug_rainbow, 120, 2, "🌈"),
        PetItem("rug_paw", RUG, R.string.pet_item_rug_paw, 90, emoji = "🐾"),

        PetItem("plant_cactus", PLANT, R.string.pet_item_plant_cactus, 30, emoji = "🌵"),
        PetItem("plant_monstera", PLANT, R.string.pet_item_plant_monstera, 70, emoji = "🪴"),
        PetItem("plant_sunflower", PLANT, R.string.pet_item_plant_sunflower, 110, 2, "🌻"),

        PetItem("lamp_floor", LAMP, R.string.pet_item_lamp_floor, 50, emoji = "💡"),
        PetItem("lamp_star", LAMP, R.string.pet_item_lamp_star, 140, 3, "⭐"),
        PetItem("lamp_lava", LAMP, R.string.pet_item_lamp_lava, 200, 5, "🫧"),

        PetItem("wall_abc", WALL, R.string.pet_item_wall_abc, 40, emoji = "🔤"),
        PetItem("wall_clock", WALL, R.string.pet_item_wall_clock, 80, emoji = "🕰️"),
        PetItem("wall_map", WALL, R.string.pet_item_wall_map, 150, 4, "🗺️"),

        PetItem("toy_ball", TOY, R.string.pet_item_toy_ball, 30, emoji = "⚽"),
        PetItem("toy_yarn", TOY, R.string.pet_item_toy_yarn, 45, emoji = "🧶"),
        PetItem("toy_teddy", TOY, R.string.pet_item_toy_teddy, 130, 3, "🧸"),
        PetItem("toy_books", TOY, R.string.pet_item_toy_books, 90, 2, "📚"),

        PetItem("hat_party", HAT, R.string.pet_item_hat_party, 60, emoji = "🥳"),
        PetItem("hat_beanie", HAT, R.string.pet_item_hat_beanie, 80, emoji = "🧢"),
        PetItem("hat_flower", HAT, R.string.pet_item_hat_flower, 140, 3, "🌸"),
        PetItem("hat_grad", HAT, R.string.pet_item_hat_grad, 260, 5, "🎓"),
        PetItem("hat_crown", HAT, R.string.pet_item_hat_crown, 500, 10, "👑"),

        PetItem("face_round", FACE, R.string.pet_item_face_round, 70, emoji = "👓"),
        PetItem("face_star", FACE, R.string.pet_item_face_star, 150, 4, "🕶️"),

        PetItem("neck_bow", NECK, R.string.pet_item_neck_bow, 50, emoji = "🎀"),
        PetItem("neck_scarf", NECK, R.string.pet_item_neck_scarf, 90, 2, "🧣"),
        PetItem("neck_medal", NECK, R.string.pet_item_neck_medal, 300, 8, "🏅"),
    )

    val byId = all.associateBy { it.id }
    fun inSlot(slot: String) = all.filter { it.slot == slot }

    /** The theme and bed default to their free items; other slots may be empty. */
    fun equipped(state: PetState, slot: String): PetItem? =
        state.equipped[slot]?.let { byId[it] } ?: if (slot == THEME) byId["theme_cozy"] else if (slot == BED) byId["bed_basket"] else null
}
/** Species are cosmetic. Original designs (no franchise look-alikes). */
object Species {
    val all = listOf("cat", "panda", "dragon")
    fun nameRes(s: String) = when (s) { "panda" -> R.string.species_panda; "dragon" -> R.string.species_dragon; else -> R.string.species_cat }
    fun defaultNameRes(s: String) = when (s) { "panda" -> R.string.pet_default_panda; "dragon" -> R.string.pet_default_dragon; else -> R.string.pet_default_cat }
}
