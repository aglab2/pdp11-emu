package gui

import MainController
import javafx.beans.binding.*
import javafx.collections.*
import javafx.event.*
import javafx.geometry.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.image.*
import javafx.scene.input.*
import javafx.scene.layout.*
import javafx.scene.paint.*
import javafx.scene.text.*
import memory.*
import memory.primitives.*
import tornadofx.*
import java.util.concurrent.*


class MainView : View() {
    val controller: MainController by inject()
    val memoryModel: MemoryModel = controller.memoryModel
    val screen: WritableImage = controller.screen

    init { title = "PDP-11-40" }


    override val root = vbox(1.0) {
        padding = Insets(3.0)

        hbox(11.0) {
            vgrow(Priority.ALWAYS)

            stackpane {
                imageview {
                    image = screen
                    isPreserveRatio = true

                    val h = this@stackpane.heightProperty().divide(screen.height)
                    val w = this@stackpane.widthProperty().divide(screen.width)

                    val scale = Bindings.min(h, w)

                    scaleXProperty().bind(scale)
                    scaleYProperty().bind(scale)
                }

                hgrow(Priority.ALWAYS)
                vgrow(Priority.ALWAYS)
            }

            vbox(1.0) {
                centeredLabel("REGs") { alignment = Pos.BASELINE_RIGHT }

                registersLayout(memoryModel.registers.dataObservableList)
                spacer() {maxHeight = 11.0}
                flagsLayout(memoryModel.flags)

                hgrow(Priority.NEVER)
            }

            vbox {
                centeredLabel("ROM")

                listview(memoryModel.rom.dataObservableList) {
                    vgrow(Priority.ALWAYS)
                    cellFormat {
                        val busAddr = memoryModel.bus.getBusAddr(memoryModel.rom as RWMemory?, Offset(index))

                        graphic = hbox {
                            centeredLabel(Word(busAddr).fmtOctal()) { prefWidth = 60.0 }
                            radiobutton()
                            centeredLabel(it.fmtOctal()) { prefWidth = 60.0}

                            spacing = 6.0
                        }
                    }
                }
            }

            vbox {
                centeredLabel("RAM")

                listview(memoryModel.ram.dataObservableList) {
                    vgrow(Priority.ALWAYS)
                    cellFormat {
                        graphic = label(it.fmtHex())
                    }
                }

            }
        }


        buttonbar {
            button("Start") {
                setOnAction { controller.startButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.or(controller.executorIsHalted))
            }
            button("Pause") {
                setOnAction { controller.pauseButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.not())
            }
            button("Reset") {
                setOnAction { controller.resetButtonHandler() }
                this.defaultButtonProperty().bind(controller.executorIsHalted)
            }
            button("Step") {
                setOnAction { controller.stepButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.or(controller.executorIsHalted))

                tooltip("F7")

                sceneProperty().onChange { scene ->
                    if(scene != null) {
                        val F7 = KeyCodeCombination(KeyCode.F7)
                        if(F7 !in scene.accelerators) {
                            scene.accelerators.put(F7, Runnable { this@button.fire() })
                        }
                    }
                }

            }

            style {
                padding = box(1.px)
            }
        }


        centeredLabel("Developed by Daniil Vodopian (@voddan) and Denis Kopyrin (@aglab2)") {
            alignment = Pos.BASELINE_CENTER
            font = Font.font(10.0)
            background = Background(BackgroundFill(Paint.valueOf("#e6e6e6"), null, null))
        }
    }


}

fun Node.hgrow(priority: Priority) = HBox.setHgrow(this, priority)
fun Node.vgrow(priority: Priority) = VBox.setVgrow(this, priority)

fun EventTarget.centeredLabel(str: String, op: (Label.() -> Unit)? = null) = label(str) {
    alignment = Pos.BASELINE_CENTER
    maxWidth = Double.MAX_VALUE
    op?.invoke(this)
}

fun EventTarget.registersLayout(registerList: ObservableList<Word>) {
    for(index in registerList.indices) {
        hbox(5.0) {
            label("R$index") {
                minWidth = 20.0
                alignment = Pos.CENTER_RIGHT
            }

            textfield {
                alignment = Pos.BASELINE_CENTER
                prefWidth = 70.0

                val word = Bindings.valueAt(registerList, index)
                val text = Bindings.createStringBinding(Callable {word.get().fmtOctal()}, word)
                textProperty().bind(text)
            }
        }
    }
}

fun EventTarget.flagsLayout(flags: FlagsStorage) {
    for(f in listOf(flags.T, flags.N, flags.Z, flags.V, flags.C)) {
        hbox(5.0) {
            label(f.name) {
                minWidth = 20.0
                alignment = Pos.CENTER_RIGHT
            }

            textfield {
                alignment = Pos.BASELINE_CENTER
                prefWidth = 70.0

                val text = Bindings.createStringBinding(Callable {f.get().toString()}, f)
                textProperty().bind(text)
            }
        }
    }
}