package com.sadhen.scamtex

import javax.swing._
import java.awt.{List => _, _}
import java.awt.event._

import com.sadhen.scamtex.edit.Editor
import org.log4s._
import com.sadhen.scamtex.kernel.{AtomicRep, CompoundRep, Tree}
import com.sadhen.scamtex.kernel.TreeLabel._
import com.sadhen.scamtex.typeset.Typesetter

class Scamtex extends JFrame {
  private[this] val logger = getLogger

  val document = Tree(CompoundRep(DOCUMENT))
  var current = Tree(CompoundRep(CONCAT), document)
  document += current

  println(document)

  val CANVAS_WIDTH = 640
  val CANVAS_HEIGHT = 480
  val LINE_COLOR = Color.BLACK
  val CANVAS_BACKGROUND = Color.WHITE
  val canvas = new DrawCanvas
  canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT))
  val cp = getContentPane
  cp.setLayout(new BorderLayout())
  cp.add(canvas, BorderLayout.CENTER)

  addKeyListener(new KeyAdapter {
    override def keyPressed(evt: KeyEvent): Unit = evt.getKeyCode match {
      case KeyEvent.VK_LEFT =>
        current = Editor.moveLeft(current)
        logger.info(document.toString)
        repaint()
      case KeyEvent.VK_RIGHT =>
        current = Editor.moveRight(current)
        repaint()
      case KeyEvent.VK_SHIFT =>
      case KeyEvent.VK_DELETE =>
      case KeyEvent.VK_BACK_SPACE =>
        current = Editor.deleteLeft(current)
        repaint()
      case KeyEvent.VK_ENTER =>
        current = Editor.newLine(current)
        document += current
        repaint()
      case _ =>
        val char = evt.getKeyChar
        current += Tree(AtomicRep(char.toString))
        repaint()
    }
  })

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setTitle("ScamTeX")
  pack()
  setVisible(true)
  requestFocus()

  class DrawCanvas extends JPanel {

    override def paintComponent(g: Graphics) = {
      super.paintComponent(g)
      setBackground(CANVAS_BACKGROUND)
      g.setColor(LINE_COLOR)
      Typesetter.render(current, document, g, 20, 20)
    }
  }
}

object Scamtex extends App {
  SwingUtilities.invokeLater(new Runnable {
    override def run(): Unit = {
      new Scamtex()
    }
  })
}