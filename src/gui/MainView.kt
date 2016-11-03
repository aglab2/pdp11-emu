package gui

import MainController
import com.apple.concurrent.*
import javafx.beans.binding.*
import javafx.beans.value.*
import javafx.collections.*
import javafx.event.*
import javafx.geometry.*
import javafx.scene.*
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


    override val root = vbox(1.0) root@{
        padding = Insets(3.0)

        hbox(11.0) {
            vgrow(Priority.ALWAYS)

            stackpane {
                imageview {
                    image = screen
                    isPreserveRatio = true
                }

                hgrow(Priority.ALWAYS)
                vgrow(Priority.ALWAYS)
            }

            vbox(1.0) {
                centeredLabel("REGs").apply { alignment = Pos.BASELINE_RIGHT }

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
                        graphic = label(("000" + Integer.toHexString(it.value)).take(4))
                    }
                }
            }

            vbox {
                centeredLabel("RAM")

                listview(memoryModel.ram.dataObservableList) {
                    vgrow(Priority.ALWAYS)
                    cellFormat {
                        graphic = label(("000" + Integer.toHexString(it.value)).take(4))
                    }
                }

            }
        }

        buttonbar {
            button("Start") {
                setOnMouseClicked { controller.startButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.or(controller.executorIsHalted))
            }
            button("Pause") {
                setOnMouseClicked { controller.pauseButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.not())
            }
            button("Reset") {
                setOnMouseClicked { controller.resetButtonHandler() }
                this.defaultButtonProperty().bind(controller.executorIsHalted)
            }
            button("Step") {
                setOnMouseClicked { controller.stepButtonHandler() }
                this.disableProperty().bind(controller.executorPlays.or(controller.executorIsHalted))

                sceneProperty().onChange { scene ->
                    if(scene != null) {

                        val F7 = KeyCodeCombination(KeyCode.F7)
                        if(F7 !in scene.accelerators) {
                            scene.accelerators.put(F7, Runnable { this@button.fire(); println("fire") })
                            scene.accelerators.forEach { println("${it.key}; ${it.value}") }
                            println("set F7!")
                        }
                    }
                }
            }

            style {
                padding = box(1.px)
            }
        }

        centeredLabel("Developed by Daniil Vodopian (@voddan) and Denis Kopyrin (@aglab2)").apply {
            alignment = Pos.BASELINE_CENTER
            font = Font.font(10.0)
            background = Background(BackgroundFill(Paint.valueOf("#e6e6e6"), null, null))
        }
    }


}

fun Node.hgrow(priority: Priority) = HBox.setHgrow(this, priority)
fun Node.vgrow(priority: Priority) = VBox.setVgrow(this, priority)

fun EventTarget.centeredLabel(str: String) = label(str) {
    alignment = Pos.BASELINE_CENTER
    maxWidth = Double.MAX_VALUE
}

fun EventTarget.registersLayout(registerList: ObservableList<Word>) {
    for(index in registerList.indices) {
        hbox(5.0) {
            label("R$index") {
                prefWidth = 45.0
                alignment = Pos.CENTER_RIGHT
            }

            textfield {
                alignment = Pos.BASELINE_CENTER
                prefWidth = 60.0

                val word = Bindings.valueAt(registerList, index)
                val text = Bindings.createStringBinding(Callable {Integer.toHexString(word.get().value)}, word)
                textProperty().bind(text)
            }
        }
    }
}

fun EventTarget.flagsLayout(flags: FlagsStorage) {
    for(f in listOf(flags.T, flags.N, flags.Z, flags.V, flags.C)) {
        hbox(5.0) {
            label(f.name) {
                prefWidth = 45.0
                alignment = Pos.CENTER_RIGHT
            }

            textfield {
                alignment = Pos.BASELINE_CENTER
                prefWidth = 60.0

                val text = Bindings.createStringBinding(Callable {f.get().toString()}, f)
                textProperty().bind(text)
            }
        }
    }
}