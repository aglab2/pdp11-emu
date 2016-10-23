package gui

import MainController
import javafx.geometry.*
import javafx.scene.image.*
import javafx.scene.paint.*
import memory.*
import memory.primitives.*
import tornadofx.*


class MainView : View() {
    val controller: MainController by inject()
    val memoryModel: MemoryModel = controller.memoryModel
    val screen: WritableImage = controller.screen
    val rom = (memoryModel.rom as MemoryStorage).__data__.toMutableList().observable()


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
                for ((index, reg) in (memoryModel.registers as MemoryStorage).__data__.withIndex())
                    this.add(RegisterFragment(index, reg).root)

                listview(rom) {
                    prefHeight = 150.0
                    cellFormat {
                        graphic = label(("000" + Integer.toHexString(it.value)).take(4))
                    }
                }
            }
        }

        buttonbar {
            button("Start") {
                setOnMouseClicked  { controller.startButtonHandler() }
            }
            button("Pause") {
                setOnMouseClicked  { controller.pauseButtonHandler() }
            }
            button("Reset") {
                setOnMouseClicked  { controller.resetButtonHandler() }
            }
            button("Step") {
                setOnMouseClicked  { controller.stepButtonHandler() }
            }

            style {
                padding = box(1.px)
            }
        }
    }
}


class RegisterFragment(index: Int, reg: Word) : Fragment() {
    override val root = hbox(5.0) {
        label("reg$index") {
            prefWidth = 45.0
            alignment = Pos.CENTER_RIGHT
        }

        // TODO: bind
        textfield("%04x".format(reg.value)) {
            alignment = Pos.BASELINE_CENTER
            prefWidth = 60.0
        }
    }
}