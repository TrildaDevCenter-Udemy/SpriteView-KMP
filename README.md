<p align="center">
  <a href="https://central.sonatype.com/artifact/com.stevdza-san/sprite"><img alt="Profile" src="https://badgen.net/badge/Maven Central/v1.0.2/blue?icon=github"/></a>
</p>

# <p align="center">🐰 SpriteView</p>

<p align="center">
This is a simple UI component that allows you to render and play <b>Sprite Sheet</b> animation in your composable screen as well on the <b>Canvas</b>. A Sprite Sheet is a single image file that contains multiple smaller <b>images</b> <i>(or "frames")</i> arranged in a <b>grid</b>. Each frame represents a different stage or part of an animation, often used in 2D video games and animations. By displaying frames in a sequence, a sprite sheet creates the illusion of movement or changes in appearance.
</p><br>

<p align="center">
<img src="images/sprite.png?raw=true" width="246"/>
<img src="images/sprite.gif?raw=true" width="268"/>
</p>

### How does it work?
1. First you need to make sure that you have a Sprite Sheet. You can create one by yourself if you're a graphic designer, or download it from websites like <a href="https://www.gameart2d.com/freebies.html">THIS</a> for example.
2. It's a good thing if you are familiar with the basics of ***Figma*** or any other similar software. Because you can then import that sprite sheet and create the one with custom dimensions. That single Sheet file should contain smaller frames arranged in a ***grid***. All smaller frames should have the same size. Make sure that there's no any extra space between smaller frames, that's how your sprite sheet animation will look perfect! Plus you need to copy the width/height values of the smaller frame, because you're gonna pass it to `SpriteSheet` data class, later.
3. Export the sprite sheet as a single .PNG file *(Remove the white background color so that your Sheet can be transparent)*.
4. Scroll down below to read how to integrate it in your KMP/CMP project. This library works by iterating through the frames of the Sprite Sheet file that you have provided. You can even customize the speed of the animation.

<img src="images/example.png?raw=true" width="350"/>

### Gradle

You can add a dependency inside the `commonMain` source set:
```gradle
commonMain.dependencies {
    implementation("com.stevdza-san:sprite:1.0.2")
}
```

### Basic Usage

`rememberSpriteState` has two required (`totalFrames`, `framesPerRow`) and one optional (`animationSpeed`) parameter. The first one is used to specify how many frames you got in your sprite sheet. The second one is used to specify how many frames you got in each row, if there are multiple rows of sprite frames of course. It's really important for calculating the proper offset value when animating. The third one on the other hand is used to specify a speed of iterating through frames of your sprite sheet. A default value is 50ms.

`SpriteView` composable accepts three parameters.
1. Composable `Modifier`.
2. `SpriteState` which manages the state of a sprite sheet animation by controlling the frame transitions and animation timing. There are two functions that you can use to control when to `start()` or `stop()` the animation, and the third one `cleanup()` to cancel the internal `Coroutine Scope`.
3. `SpriteSpec` parameter allows you to pass multiple `SpriteSheet`s if you're planning to adapt to different screen sizes correctly. Otherwise you can pass only a single `default` `SpriteSheet` instead. `SpriteSpec` accepts `screenWidth` parameter, because it needs to calculate and display the correct `SpriteSheet` size, based on the current screen width. Luckily this library offers one useful function called `getScreenWidth()` that allows you to get the current screen width on both Android/iOS.

Each `SpriteSheet` accepts `frameWidth`, `frameHeight` parameters, that represents a single frame of your `SpriteSheet` `image` represented in px, as well as the actual `image` resource that you have previously added in your common `composeResource` directory.

```kotlin
val screenWidth = getScreenWidth().value
val spriteState = rememberSpriteState(
    totalFrames = 9,
    framesPerRow = 3,
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
        spriteSpec = SpriteSpec(
            screenWidth = screenWidth,
            default = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                image = Res.drawable.sprite_normal
            )
        )
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
Sprite sheet animation is triggered inside the coroutine scope, which is why it is a good practice to cancel it when you no longer need it. That's why I've exposed a function called `cleanup()` that allows you to do exactly that. You can utilize a `DisposableEffect()` to achieve that.

### Screen Size Usage
There are four `ScreenCategory` entries. `Small` reserved for smaller mobile devices from **0dp to 360dp** in width. `Normal` reserved for normal mobile devices from **360dp to 600dp** in width. `Large` reserved for larger mobile devices from **600dp to 800dp** in width. `Tablet` reserved for tablet devices from more then **800dp** in width.

For each one of the above-mentioned categories, you can pass a custom `SpriteSheet` with different dimensions that can adapt well on various screen sizes.

This repository contains sample <a href="/images">images</a> directory where you can find demo sprite sheets. Frame dimensions in the code below, are representing frames from those same demo sprite sheets.

```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    SpriteView(
        spriteState = spriteState,
        spriteSpec = SpriteSpec(
            screenWidth = screenWidth,
            default = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                image = Res.drawable.sprite_normal
            ),
            small = SpriteSheet(
                frameWidth = 149,
                frameHeight = 179,
                image = Res.drawable.sprite_small
            ),
            normal = SpriteSheet(
                frameWidth = 253,
                frameHeight = 303,
                image = Res.drawable.sprite_normal
            ),
            large = SpriteSheet(
                frameWidth = 377,
                frameHeight = 451,
                image = Res.drawable.sprite_large
            ),
            tablet = SpriteSheet(
                frameWidth = 619,
                frameHeight = 740,
                image = Res.drawable.sprite_tablet
            )
        )
    )
}
```

### Canvas Usage
There's another useful function in this library, that allows you to add this sprite sheet animation directly inside the `DrawScope()` of the `Canvas`. It's called `drawSpriteView()`, and unlike the `SpriteView()` this one requires additional parameters, because it doesn't allow `@Composable` context inside it.

1. `currentFrame` is a StateFlow value that holds the current frame of the sprite sheet. Since we cannot directly collect that state inside the `DrawScope()` of the canvas, we need to observe it outside of the `drawSpriteView()` and then pass it along.
2. `image` represents the `ImageBitmap` *(sprite sheet image)* that we have placed in the common composeResource directory. That object is available within the `SpriteSpec` object, which is in charge for choosing a correct sprite sheet image, based on the current `screenWidth`. 

```kotlin
val screenWidth = getScreenWidth().value
val spriteState = rememberSpriteState(
  totalFrames = 9,
  framesPerRow = 3,
  animationSpeed = 50
)
val currentFrame by spriteState.currentFrame.collectAsState()
val spriteSpec = remember {
  SpriteSpec(
    screenWidth = screenWidth,
    default = SpriteSheet(
      frameWidth = 619,
      frameHeight = 740,
      image = Res.drawable.sprite_tablet
    )
  )
}
val sheetImage = spriteSpec.image

DisposableEffect(Unit) {
  spriteState.start()
    onDispose {
      spriteState.stop()
      spriteState.cleanup()
    }
}

Box(
  modifier = Modifier.fillMaxSize(),
  contentAlignment = Alignment.Center
) {
  Canvas(modifier = Modifier.fillMaxSize()) {
    drawSpriteView(
      spriteState = spriteState,
      spriteSpec = spriteSpec,
      currentFrame = currentFrame,
      image = sheetImage
    )
  }
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
