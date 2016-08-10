package com.sadhen.scamtex

import org.log4s._
import com.sadhen.scamtex.edit.Editor
import com.sadhen.scamtex.kernel.{AtomicRep, CompoundRep, Tree}
import com.sadhen.scamtex.kernel.TreeLabel._
import com.sadhen.scamtex.typeset.Typesetter

import scalafx.application.JFXApp.PrimaryStage
import scalafx.application.JFXApp
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.{Group, Scene}
import scalafx.Includes._
import scalafx.scene.text.{Font}

object Scamtex extends JFXApp {
  def repaint(): Unit = {
    gc.clearRect(0, 0, canvas.getWidth, canvas.getHeight)
    Typesetter.render(current, document, gc, 20, 20)
  }

  private val logger = getLogger

  val document = Tree(CompoundRep(DOCUMENT))
  var current = Tree(CompoundRep(CONCAT), document)
  document += current


  val canvas = new Canvas(640, 480)
  val gc = canvas.getGraphicsContext2D
  val font = Font.font(gc.getFont.getFamily, 12)
  gc.setFont(font)
  val group = new Group {
    layoutX = 0
    layoutY = 0
    children = List(canvas)
    onKeyPressed = (k: KeyEvent) => k.code match {
      case KeyCode.Left =>
        current = Editor.moveLeft(current)
        repaint()
      case KeyCode.Right =>
        current = Editor.moveRight(current)
        repaint()
      case KeyCode.BackSpace =>
        current = Editor.deleteLeft(current)
        repaint()
      case KeyCode.Delete =>
        current = Editor.deleteRight(current)
        repaint()
      case KeyCode.Enter =>
        current = Editor.newLine(current)
        document += current
        repaint()
      case _ =>
        val text = k.text
        current += Tree(AtomicRep(text))
        repaint()
    }
  }

  val scene0 = new Scene {
    content = group
  }

  stage = new PrimaryStage {
    title = "ScamTeX"
    scene = scene0
  }

  group.requestFocus()
}
