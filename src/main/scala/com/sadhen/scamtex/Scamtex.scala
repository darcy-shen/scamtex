package com.sadhen.scamtex

import javax.swing._
import java.awt.{List => _, _}
import java.awt.event._

import com.sadhen.scamtex.kernel.{AtomicRep, CompoundRep, Tree}
import com.sadhen.scamtex.kernel.TreeLabel._

class Scamtex extends JFrame {
  val document = Tree(CompoundRep(DOCUMENT, List()))
  val current = document

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

  var x1 = CANVAS_WIDTH/2
  var y1 = CANVAS_HEIGHT/8
  var x2 = x1
  var y2 = CANVAS_HEIGHT/8*7

  addKeyListener(new KeyAdapter {
    override def keyPressed(evt: KeyEvent): Unit = evt.getKeyCode match {
      case KeyEvent.VK_LEFT =>
        repaint()
      case KeyEvent.VK_RIGHT =>
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
      g.drawLine(x1, y1, x2, y2)
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