package com.stevdza_san.sprite.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.stevdza_san.sprite.domain.SpriteSpec
import com.stevdza_san.sprite.domain.ScreenCategory
import com.stevdza_san.sprite.domain.SpriteState
import com.stevdza_san.sprite.util.getScreenWidth
import com.stevdza_san.sprite.util.parseCategory
import org.jetbrains.compose.resources.imageResource

/**
 * Composable function which is used to display and animate the
 * Sprite Sheet within a composable hierarchy.
 *
 * A sprite sheet is a single image file that contains multiple smaller images (or "frames")
 * arranged in a grid. Each frame represents a different stage or part
 * of an animation, often used in 2D video games and animations.
 * By displaying frames in a sequence, a sprite sheet creates the illusion
 * of movement or changes in appearance.
 *
 * @param modifier Optional modifier.
 * @param spriteState
 * @param spec [SpriteSpec] is used to optionally pass multiple sprite sheets with
 * different dimensions, to support various screen sizes. You can optionally include multiple
 * sprite sheets, or use only one (default).
 * @param framesPerRow The number of frames that are included in the sprite sheet, per row.
 *
 * @see ScreenCategory
 * */
@Composable
fun SpriteView(
    modifier: Modifier = Modifier,
    spriteState: SpriteState,
    spec: SpriteSpec,
    framesPerRow: Int
) {
    val screenWidth = getScreenWidth().value.toInt()
    val screenCategory by remember {
        derivedStateOf { screenWidth.parseCategory() }
    }

    val spriteImage = imageResource(
        if (screenCategory == ScreenCategory.Small && spec.small != null) spec.small.image
        else if (screenCategory == ScreenCategory.Normal && spec.normal != null) spec.normal.image
        else if (screenCategory == ScreenCategory.Large && spec.large != null) spec.large.image
        else if (screenCategory == ScreenCategory.Tablet && spec.tablet != null) spec.tablet.image
        else spec.default.image
    )
    val currentFrame = spriteState.currentFrame.collectAsState().value
    // Get the row and column position based on the current frame
    val row by rememberUpdatedState(
        newValue = currentFrame / framesPerRow
    )
    val column by rememberUpdatedState(
        newValue = currentFrame % framesPerRow
    )
    val frameSize by remember {
        derivedStateOf {
            IntSize(
                spec.getCorrectFrameSize(screenWidth).width,
                spec.getCorrectFrameSize(screenWidth).height
            )
        }
    }
    val frameOffset by remember {
        derivedStateOf {
            IntOffset(
                x = column * frameSize.width,
                y = row * frameSize.height
            )
        }
    }

    Canvas(
        modifier = modifier
            .width(with(LocalDensity.current) { frameSize.width.toDp() })
            .height(with(LocalDensity.current) { frameSize.height.toDp() })
    ) {
        // Calculate the offset to center the image
        val dstOffsetX = (size.width - frameSize.width.toFloat()) / 2
        val dstOffsetY = (size.height - frameSize.height.toFloat()) / 2

        drawImage(
            image = spriteImage,
            srcOffset = frameOffset,
            srcSize = frameSize,
            dstOffset = IntOffset(
                dstOffsetX.toInt(),
                dstOffsetY.toInt()
            ),
            dstSize = frameSize
        )
    }
}