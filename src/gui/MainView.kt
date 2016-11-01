package gui

import MainController
import javafx.beans.binding.*
import javafx.collections.*
import javafx.event.*
import javafx.geometry.*
import javafx.scene.*
import javafx.scene.image.*
import javafx.scene.paint.*
import memory.*
import memory.primitives.*
import tornadofx.*
import java.util.concurrent.*


class MainView : View() {
    val controller: MainController by inject()
    val memoryModel: MemoryModel = controller.memoryModel
    val screen: WritableImage = controller.screen

    val romList = memoryModel.rom.dataObservableList
    val registerList = memoryModel.registers.dataObservableList


    override val root = vbox(1.0) {

        padding = Insets(3.0)

        label("Hello world")

        hbox(10.0) {
            stackpane {
                imageview { image = screen }

                style {
                    borderColor += box(Color.BLACK)
                    borderWidth += box(2.px)
                    backgroundColor += Color.WHITE
                }
            }

            vbox(1.0) {
                registersLayout(registerList)

                listview(romList) {
                    prefHeight = 150.0
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
            }

            style {
                padding = box(1.px)
            }
        }
    }
}


fun EventTarget.registersLayout(registerList: ObservableList<Word>) {
    for(index in registerList.indices) {
        hbox(5.0) {
            label("reg$index") {
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