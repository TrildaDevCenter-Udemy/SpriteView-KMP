package com.stevdza_san.sprite.domain

import com.stevdza_san.sprite.util.parseCategory

/**
 * Represents a specification for sprite sheets, providing different versions for various screen categories.
 * This allows for responsive sprite selection based on screen size, with a default sprite sheet as a fallback.
 *
 * @property default The default [SpriteSheet] used if no other screen-specific version is available.
 * @property small An optional [SpriteSheet] tailored for small screen sizes.
 * @property normal An optional [SpriteSheet] tailored for normal screen sizes.
 * @property large An optional [SpriteSheet] tailored for large screen sizes.
 * @property tablet An optional [SpriteSheet] tailored for tablet-sized screens.
 *
 * @see ScreenCategory
 */
data class SpriteSpec(
    val default: SpriteSheet,
    val small: SpriteSheet? = null,
    val normal: SpriteSheet? = null,
    val large: SpriteSheet? = null,
    val tablet: SpriteSheet? = null
) {
    internal fun getCorrectFrameSize(screenWidth: Int): SpriteSheet {
        return if (screenWidth.parseCategory() == ScreenCategory.Small
            && small != null
        ) small
        else if (screenWidth.parseCategory() == ScreenCategory.Normal
            && normal != null
        ) normal
        else if (screenWidth.parseCategory() == ScreenCategory.Normal
            && large != null
        ) large
        else if (screenWidth.parseCategory() == ScreenCategory.Normal
            && tablet != null
        ) tablet
        else default
    }
}