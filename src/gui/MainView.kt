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
    val rom = (memoryModel.rom as RandomAccessMemory).__data__.toMutableList().observable()


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
                for ((index, reg) in memoryModel.registers.withIndex())
                    this.add(RegisterFragment(index, reg).root)

                listview(rom) {
                    cellFragment(InstructionFragment::class)
                    prefHeight = 150.0
                }
            }
        }

        buttonbar {
            button("Start", op = controller::startButtonHandler)
            button("Pause", op = controller::pauseButtonHandler)
            button("Reset", op = controller::resetButtonHandler)
            button("Step", op = controller::stepButtonHandler)

            style {
                padding = box(1.px)
            }
        }
    }
}

class InstructionFragment() : ListCellFragment<Word>() {
    override val root = label("%h".format(item?.value ?: 239))
}


class RegisterFragment(index: Int, reg: Word) : Fragment() {
    override val root = hbox(5.0) {
        label("reg$index")

        // TODO: bind
        textfield("%04x".format(reg.value)) {
            alignment = Pos.BASELINE_CENTER
            prefWidth = 60.0
        }
    }
}