package com.binayshaw.passginie.pages

import androidx.compose.runtime.*
import com.binayshaw.passginie.Utils.Res
import com.varabyte.kobweb.core.Page
import com.binayshaw.passginie.components.layouts.PageLayout
import com.binayshaw.passginie.components.widgets.GlassBox
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.css.functions.toImage
import com.varabyte.kobweb.compose.dom.ElementTarget
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaArrowRotateLeft
import com.varabyte.kobweb.silk.components.icons.fa.FaCopy
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.overlay.KeepPopupOpenStrategy
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.components.overlay.manual
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import com.varabyte.kobweb.silk.components.style.base
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.RangeInput
import org.jetbrains.compose.web.dom.Text
import passGenerator

@Page
@Composable
fun HomePage() {
    PageLayout(null) {

        /**
         * Variables Declaration
         */
        val generatedPassword = remember {
            mutableStateOf(
                passGenerator(
                    8, false,
                    false, false,
                    false
                )
            )
        }
        val passwordLength = remember {
            mutableStateOf(8)
        }
        val shouldIncludeUppercase = remember {
            mutableStateOf(false)
        }
        val shouldIncludeLowercase = remember {
            mutableStateOf(true)
        }
        val shouldIncludeNumbers = remember {
            mutableStateOf(false)
        }
        val shouldIncludeSymbols = remember {
            mutableStateOf(false)
        }
        val regeneratePassword = remember {
            mutableStateOf(false)
        }
        val showCopedPasswordToolTip = remember {
            mutableStateOf(false)
        }

        if (regeneratePassword.value) {
            generatedPassword.value = passGenerator(
                passwordLength.value,
                shouldIncludeUppercase.value,
                shouldIncludeLowercase.value,
                shouldIncludeNumbers.value,
                shouldIncludeSymbols.value
            )
            regeneratePassword.value = false
        }

        /**
         * UI Logic
         */

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.px)
                .scrollBehavior(ScrollBehavior.Smooth),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            GlassBox(modifier = Modifier.fillMaxHeight(20.percent)) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(85.percent)
                        .fillMaxHeight(50.percent)
                        .zIndex(1)
                        .borderRadius(10.px)
                        .margin(20.px)
                ) {

                    Row(
                        modifier = Modifier.fillMaxSize().padding(20.px),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        P(attrs = {
                            style {
                                fontSize(24.px)
                            }
                        }) {
                            Text(generatedPassword.value)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(10.percent),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            FaCopy(modifier = Modifier.onClick {
                                window.navigator.clipboard.writeText(generatedPassword.value)
                                showCopedPasswordToolTip.value = true
                            })
                            Spacer()

                            FaArrowRotateLeft(modifier = Modifier.onClick {
                                regeneratePassword.value = true
                            })
                            if (showCopedPasswordToolTip.value) {
                                window.setTimeout({
                                    showCopedPasswordToolTip.value = false
                                }, 2000)
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(85.percent)
                        .fillMaxHeight(50.percent)
                        .styleModifier {
                            mixBlendMode(MixBlendMode.Overlay)
                        }
                        .background(rgba(255, 255, 255, 0.20))
                        .borderRadius(10.px)
                        .margin(20.px)
                )
            }
            Tooltip(
                ElementTarget.PreviousSibling,
                "Copied!",
                modifier = Modifier.opacity(
                    if (showCopedPasswordToolTip.value) 100.percent
                    else 0.percent
                ),
                hasArrow = false,
                keepOpenStrategy = KeepPopupOpenStrategy.manual(showCopedPasswordToolTip.value)
            )

            P()

            GlassBox(modifier = Modifier.fillMaxHeight(50.percent)) {

                Column(
                    modifier = Modifier.fillMaxSize().zIndex(1),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Spacer()

                    Row(
                        modifier = Modifier.fillMaxWidth(80.percent),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        P(attrs = {
                            style {
                                padding(5.px)
                                fontSize(20.px)
                            }
                        }) {
                            Text("Password length:")
                        }
                        P(attrs = {
                            style {
                                padding(5.px)
                                fontSize(20.px)
                                fontWeight(FontWeight.Bold)
                            }
                        }) {
                            Text(passwordLength.value.toString())
                        }
                    }


                    Spacer()

                    Row(
                        modifier = Modifier.fillMaxWidth(70.percent),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text("-")
                        RangeInput(
                            value = passwordLength.value,
                            min = 4,
                            max = 15,
                            step = 1,
                            attrs = {
                                style {
                                    minWidth(200.px)
                                }
                                onInput {
                                    console.log("Slider value is: ${it.value}")
                                    passwordLength.value = it.value!!.toInt()
                                    regeneratePassword.value = true
                                }
                            }
                        )
                        Text("+")
                    }
                    Spacer()
                    Spacer()

                    Row(
                        modifier = Modifier.fillMaxWidth(83.percent),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        P(attrs = {
                            style {
                                fontSize(20.px)
                            }
                        }) {
                            Text("Include")
                        }
                    }

                    SimpleGrid(
                        numColumns(1, sm = 1, md = 2, lg = 2, xl = 2),
                        modifier = Modifier.fillMaxWidth(90.percent)
                    ) {

                        Column(modifier = Modifier.padding(0.px, 15.px)) {

                            Row(
                                modifier = Modifier.fillMaxWidth(65.percent)
                                    .onClick {
                                        shouldIncludeUppercase.value = shouldIncludeUppercase.value.not()
                                        regeneratePassword.value = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (shouldIncludeUppercase.value) {
                                    Image(
                                        src = Res.Images.CHECKBOX_TRUE
                                    )
                                } else {
                                    Image(
                                        src = Res.Images.CHECKBOX_FALSE
                                    )
                                }
                                P(attrs = { style { fontSize(20.px) } }) {
                                    Text("Uppercase")
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(65.percent)
                                    .onClick {
                                        shouldIncludeLowercase.value = shouldIncludeLowercase.value.not()
                                        regeneratePassword.value = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (shouldIncludeLowercase.value) {
                                    Image(
                                        src = Res.Images.CHECKBOX_TRUE
                                    )
                                } else {
                                    Image(
                                        src = Res.Images.CHECKBOX_FALSE
                                    )
                                }
                                P(attrs = { style { fontSize(20.px) } }) {
                                    Text("Lowercase")
                                }
                            }
                        }
                        Column(modifier = Modifier.padding(0.px, 15.px)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(65.percent)
                                    .onClick {
                                        shouldIncludeNumbers.value = shouldIncludeNumbers.value.not()
                                        regeneratePassword.value = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (shouldIncludeNumbers.value) {
                                    Image(
                                        src = Res.Images.CHECKBOX_TRUE
                                    )
                                } else {
                                    Image(
                                        src = Res.Images.CHECKBOX_FALSE
                                    )
                                }
                                P(attrs = { style { fontSize(20.px) } }) {
                                    Text("Numbers")
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(65.percent)
                                    .onClick {
                                        shouldIncludeSymbols.value = shouldIncludeSymbols.value.not()
                                        regeneratePassword.value = true
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (shouldIncludeSymbols.value) {
                                    Image(
                                        src = Res.Images.CHECKBOX_TRUE
                                    )
                                } else {
                                    Image(
                                        src = Res.Images.CHECKBOX_FALSE
                                    )
                                }
                                P(attrs = { style { fontSize(20.px) } }) {
                                    Text("Symbols")
                                }
                            }
                        }
                    }
                    Spacer()
                }
            }
        }
    }

}


val ModuleBorderWrapStyle by ComponentStyle.base {
    Modifier
        .maxWidth(250.px)
        .padding(3.px)
        .position(Position.Relative)
        .background(
            CSSBackground(
                image = linearGradient(
                    LinearGradient.Direction.ToRight,
                    Color.white,
                    Color.lightgray
                ).toImage()
            )
        )
}

val GradientBorderStyle by ComponentStyle.base {
    Modifier
        .border(1.px, LineStyle.Solid, Color.transparent)
        .background(
            CSSBackground(
                image = linearGradient(45.deg, Color.white, Color.transparent).toImage()
            )
        )
        .size(200.px)
}
