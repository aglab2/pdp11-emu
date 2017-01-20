package gui

import MainController
import instruction.primitives.*
import interpreter.Executor
import javafx.beans.binding.*
import javafx.beans.property.*
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
import org.fxmisc.easybind.*
import tornadofx.*
import java.nio.file.*
import java.util.concurrent.*
import javafx.scene.input.KeyCode
import org.apache.http.util.EntityUtils.consume




class MainView : View() {
    val controller: MainController by inject()
    val memoryModel: MemoryModel = controller.memoryModel
    val screen: WritableImage = controller.screen
    val executor: Executor = controller.executor
    val keyboard = controller.keyboard

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
                setOnMouseClicked { keyEvent -> this.requestFocus() }
                isFocusTraversable = true
                setOnKeyPressed { keyEvent ->
                    keyboard.processKey(keyEvent)
                }
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
                    prefWidth = 60.0 + 20 + 60 + 130 + 70

                    //TODO: Why this does not work?
                    val pc = Bindings.valueAt(memoryModel.registers.dataObservableList, RegAddr.PC.offset.value)
                    pc.addListener { observableValue, old, new -> print(new) }

                    executor.executedPC.addListener { word, old, new -> this.selectionModel.select(new) }

                    cellFormat {
                        val busAddr = memoryModel.bus.getBusAddr(memoryModel.rom as RWMemory, Offset(index))

                        graphic = hbox {
                            centeredLabel(Word(busAddr).fmtOctal()) { minWidth = 60.0 }
                            radiobutton {
                                selectedProperty().addListener { v, b, isPressed ->
                                    if (isPressed) {
                                        executor.breakpointsAddresses.add(busAddr)
                                    }else{
                                        executor.breakpointsAddresses.remove(busAddr)
                                    }
                                    print(executor.breakpointsAddresses)
                                }
                            }
                            centeredLabel(it.fmtOctal()) { minWidth = 60.0 }

                            for (instr in controller.instructions) {
                                if (index == instr.index) {
                                    centeredLabel(instr.value.assembler) { minWidth = 130.0 }
                                }
                            }

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
                        graphic = label(it.fmtOctal())
                    }
                    prefWidth = 110.0
                }

            }
        }


        hbox(10.0) {
            val isPlaying: BooleanProperty = SimpleBooleanProperty(false)
            val isRomLoading = SimpleBooleanProperty(false)

            val isHalted = Bindings.equal(
                    Bindings.valueAt(memoryModel.registers.dataObservableList, RegAddr.PC.offset.value), Word.NaN)

            isHalted.addListener {pr, old, new ->
                if(new == true) {
                    isPlaying.set(false)
                }
            }


            combobox<String> {
                items = EasyBind.map(controller.romFiles) { path -> path.fileName.toString()}
                selectionModel.select(controller.romFiles.indexOf(controller.romFile.value))

                val selectedPath = EasyBind.map(selectionModel.selectedIndexProperty()) {
                    index -> controller.romFiles.get(index as Int)
                }

                controller.romFile.bind(selectedPath)
            }

            button("Reload") {
                setOnAction {
                    isRomLoading.set(true)
                    executor.cancelAll()
                    isPlaying.set(false)

                    memoryModel.reset()
                    controller.memoryModel.rom.reload(Files.readAllBytes(controller.romFile.value))

                    isRomLoading.set(false)
                }
                disableProperty().bind(isRomLoading)
            }

            buttonbar {
                button("Start") {
                    setOnAction {
                        isPlaying.set(true)
                        executor.executeService.restart()
                    }
                    disableProperty().bind(isPlaying.or(isHalted).or(isRomLoading))
                }
                button("Pause") {
                    setOnAction {
                        executor.cancelAll()
                        isPlaying.set(false)
                    }
                    disableProperty().bind(isPlaying.not().or(isRomLoading))
                }
                button("Reset") {
                    setOnAction {
                        executor.cancelAll()
                        memoryModel.reset()
                        isPlaying.set(false)
                    }
                    defaultButtonProperty().bind(isHalted)
                }
                button("Step") {
                    setOnAction {
                        executor.executeService.cancel()
                        executor.stepService.restart()
                    }
                    disableProperty().bind(isPlaying.or(isHalted).or(isRomLoading))

                    tooltip("F7")
                    keyCombination(KeyCodeCombination(KeyCode.F7))
                }

                hgrow(Priority.ALWAYS)
                style {
                    padding = box(1.px)
                }
//                disableProperty().bind(isRomLoading)
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

fun Button.keyCombination(key: KeyCodeCombination) {
    sceneProperty().onChange { scene ->
        if(scene != null) {
            if(key !in scene.accelerators) {
                scene.accelerators.put(key, Runnable { fire() })
            }
        }
    }
}

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