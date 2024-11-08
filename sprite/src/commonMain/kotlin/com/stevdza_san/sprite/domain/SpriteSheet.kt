package com.stevdza_san.sprite.domain

import org.jetbrains.compose.resources.DrawableResource

/**
 * A sprite sheet is a single image file that contains multiple smaller images (or "frames")
 * arranged in a grid. Each frame represents a different stage or part
 * of an animation, often used in 2D video games and animations.
 * By displaying frames in a sequence, a sprite sheet creates the illusion
 * of movement or changes in appearance.
 *
 * Usually you want to fill in the information about your previously exported sheet that contains
 * all frames together. Like:
 *```
 * -------------
 * | 1 | 2 | 3 |
 * | 4 | 5 | 6 |
 * | 7 | 8 | 9 |
 * -------------
 *```
 * The above graph represents an image that consist of 9 different
 * sprite frames. It has 3 columns and three rows of image frames.
 * Sprite animation starts from the frame 1.
 * @param width The width of your whole exported sprite sheet image,
 * represented in px.
 * @param height The height of your whole exported sprite sheet image,
 * represented in px.
 * @param image The actual PNG/JPG image resources, that you have added
 * to your project's common 'composeResource' directory.
 * */
data class SpriteSheet(
    val width: Int,
    val height: Int,
    val image: DrawableResource
)