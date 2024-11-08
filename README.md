<p align="center">
  <a href="https://central.sonatype.com/artifact/com.stevdza-san/sprite"><img alt="Profile" src="https://badgen.net/badge/Maven Central/v1.0.1/blue?icon=github"/></a>
</p>

# <p align="center">🐰 SpriteView</p>

<p align="center">
This is a simple UI component that allows you to render and play <b>Sprite Sheet</b> animation in your composable screen. A Sprite Sheet is a single image file that contains multiple smaller <b>images</b> <i>(or "frames")</i> arranged in a <b>grid</b>. Each frame represents a different stage or part of an animation, often used in 2D video games and animations. By displaying frames in a sequence, a sprite sheet creates the illusion of movement or changes in appearance.
</p><br>

<p align="center">
<img src="images/sprite.png?raw=true" width="246"/>
<img src="images/sprite.gif?raw=true" width="268"/>
</p>

### How does it work?
1. First you need to make sure that you have a Sprite Sheet. You can create one by yourself if you're a graphic designer, or download it from websites like <a href="https://www.gameart2d.com/freebies.html">THIS</a> for example.
2. It's a good thing if you are familiar with the basics of ***Figma*** or any other similar software. Because you can then import that sprite sheet and create the one with custom dimensions. That single Sheet file should contain smaller frames arranged in a ***grid***. All smaller frames should have the same size. Make sure that there's no any extra space between smaller frames, that's how your sprite sheet animation will look perfect! Plus you need to copy the width/height values of the smaller frame, because you're gonna pass it to `SpriteSheet` data class, later.
3. Export the sprite sheet as a single .PNG file *(Remove the white background color so that your Sheet can be transparent)*.
4. Scroll down below to read how to integrate it in your KMP/CMP project. This library works by iterating through the frames of the Sprite Sheet file that you have provide. You can even customize the speed of the animation.

<img src="images/example.png?raw=true" width="350"/>

### Gradle

You can add a dependency inside the `commonMain` source set:
```gradle
commonMain.dependencies {
    implementation("com.stevdza-san:sprite:1.0.1")
}
```

### Basic Usage

`rememberSpriteState` has one required (`totalFrames`) and one optional (`animationSpeed`) parameter. The first one is used to specify how many frames you got in your sprite sheet, it's really important for calculating the proper offset value when animating. The second one on the other hand is used to specify a speed of iterating through frames of your sprite sheet. A default value is 50ms.

Sprite sheet animation is triggered inside the coroutine scope, which is why it is a good practice to cancel it when you no longer need it. That's why I've exposed a function called `cleanup()` that allows you to do exactly that. You can utilize a `DisposableEffect()` to achieve that.

`SpriteView` composable accepts three parameters. `framesPerRow` is an important for correctly rendering the animation. In the above image we have 9 frames, where 3 frames are placed per row. Which is why in this case you would pass number 3. `spec` parameter allows you to pass multiple `SpriteSheet`s if you're planning to adapt to different screen sizes correctly. Otherwise you can pass only a single `default` `SpriteSheet` instead. Each `SpriteSheet` accepts `frameWidth`, `frameHeight` values of a single frame of your sheet represented in px, as well as the actual `sheet` resource that you have previously added in your common `composeResource` directory.

Lastly, there are two functions that you can use to control when to `start()` or `stop()` the animation.

```kotlin
val spriteState = rememberSpriteState(
    totalFrames = 9,
    animationSpeed = 50
)
val animationRunning by spriteState.isRunning.collectAsState()

DisposableEffect(Unit) {
    onDispose {
        spriteState.stop()
        spriteState.cleanup()
    }
}

Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    SpriteView(
        spriteState = spriteState,
        spec = SpriteSpec(
            default = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                sheet = Res.drawable.sprite_normal
            )
        ),
        framesPerRow = 3
    )
    Box {
        Button(
            onClick = {
                if (animationRunning) spriteState.stop()
                else spriteState.start()
            }
        ) {
            Text(text = "Start/Stop")
        }
    }
}
```

### Multiple Screen Support Usage
There are four `ScreenCategory` entries. `small` reserved for smaller mobile devices from **0dp to 360dp** in width. `normal` reserved for normal mobile devices from **360dp to 600dp** in width. `large` reserved for larger mobile devices from **600dp to 800dp** in width. `tablet` reserved for tablet devices from more then **800dp** in width.

For each one of the above-mentioned categories, you can pass a custom `SpriteSheet` with different dimensions that can adapt well on various screen sizes.

This repository contains sample `images` directory where you can find demo sprite sheets. Frame dimensions in the code below, are representing frames from those same demo sprite sheets.

```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    SpriteView(
        spriteState = spriteState,
        spec = SpriteSpec(
            default = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                sheet = Res.drawable.sprite_normal
            ),
            small = SpriteSheet(
                frameWidth = 149,
                frameHeight = 179,
                sheet = Res.drawable.sprite_small
            ),
            normal = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                sheet = Res.drawable.sprite_normal
            ),
            large = SpriteSheet(
                frameWidth = 377,
                frameHeight = 451,
                sheet = Res.drawable.sprite_large
            ),
            tablet = SpriteSheet(
                frameWidth = 619,
                frameHeight = 740,
                sheet = Res.drawable.sprite_tablet
            )
        ),
        framesPerRow = 3
    )
}
```

## Like what you see? :yellow_heart:
⭐ Give a star to this repository. <br />
☕ Let's get a coffee. You're paying!😜 https://ko-fi.com/stevdza_san

# License
```xml
Designed and developed by stevdza-san (Stefan Jovanović)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
