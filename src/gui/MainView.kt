package gui

import MainController
import javafx.geometry.*
import javafx.scene.image.*
import javafx.scene.paint.*
import memory.*
import tornadofx.*


class MainView : View() {
    val controller: MainController by inject()
    val memoryModel: MemoryModel = controller.memoryModel


    /* Write pixels here with `screen.pixelWriter` */
    val screen: WritableImage = WritableImage(/*width*/ 300, /*height*/ 300)


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
                for ((index, reg) in memoryModel.registers.withIndex()) {
                    hbox(5.0) {
                        label("reg$index")

                        // TODO: bind
                        textfield("%04x".format(reg.value)) {
                            alignment = Pos.BASELINE_CENTER
                            prefWidth = 60.0
                        }
                    }
                }
            }
        }

        buttonbar {
            button("Start") {}
            button("Pause") {}
            button("Reset") {}
            button("Step") {}

            style {
                padding = box(1.px)
            }
        }
    }
}