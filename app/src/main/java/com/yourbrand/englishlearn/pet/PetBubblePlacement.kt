package com.yourbrand.englishlearn.pet

internal data class BubbleBox(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val width get() = right - left
    val height get() = bottom - top
    val centerX get() = (left + right) / 2
    val centerY get() = (top + bottom) / 2
    val isEmpty get() = width <= 0 || height <= 0

    fun contains(other: BubbleBox) =
        left <= other.left && top <= other.top && right >= other.right && bottom >= other.bottom

    fun intersects(other: BubbleBox) =
        left < other.right && right > other.left && top < other.bottom && bottom > other.top
}

/** Pure placement rules for the floating pet speech bubble. */
internal object PetBubblePlacement {
    /**
     * Returns a bubble rectangle that is inside [bounds], beside or above [pet], and does not
     * overlap the pet or any protected content. A null result means that hiding the bubble is the
     * only safe option.
     */
    fun find(
        bounds: BubbleBox,
        pet: BubbleBox,
        bubbleWidth: Int,
        bubbleHeight: Int,
        avoid: List<BubbleBox>,
        gap: Int,
    ): BubbleBox? {
        if (bounds.isEmpty || pet.isEmpty || bubbleWidth <= 0 || bubbleHeight <= 0) return null
        val centerY = pet.centerY - bubbleHeight / 2
        val besideLeft = BubbleBox(pet.left - gap - bubbleWidth, centerY, pet.left - gap, centerY + bubbleHeight)
        val besideRight = BubbleBox(pet.right + gap, centerY, pet.right + gap + bubbleWidth, centerY + bubbleHeight)
        val above = BubbleBox(pet.centerX - bubbleWidth / 2, pet.top - gap - bubbleHeight, pet.centerX + (bubbleWidth + 1) / 2, pet.top - gap)

        // Prefer the side facing the centre of the screen, then the other side, then above.
        val candidates = if (pet.centerX >= bounds.centerX) {
            listOf(besideLeft, besideRight, above)
        } else {
            listOf(besideRight, besideLeft, above)
        }
        return candidates
            .map { clamp(it, bounds) }
            .firstOrNull { candidate ->
                !candidate.intersects(pet) && avoid.none { candidate.intersects(it) }
            }
    }

    private fun clamp(rect: BubbleBox, bounds: BubbleBox): BubbleBox {
        val left = rect.left.coerceIn(bounds.left, (bounds.right - rect.width).coerceAtLeast(bounds.left))
        val top = rect.top.coerceIn(bounds.top, (bounds.bottom - rect.height).coerceAtLeast(bounds.top))
        return BubbleBox(left, top, left + rect.width, top + rect.height)
    }
}
