package com.sadhen.Scamtex

import javax.swing._
import java.awt._
import java.awt.event._

class CGTemplate extends JFrame {
  val CANVAS_WIDTH = 640
  val CANVAS_HEIGHT = 480
  val LINE_COLOR = Color.BLACK
  val CANVAS_BACKGROUND = Color.CYAN

  var x1 = CANVAS_WIDTH/2
  var y1 = CANVAS_HEIGHT/8
  var x2 = x1
  var y2 = CANVAS_HEIGHT/8*7

  val canvas = new DrawCanvas
  canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT))
  val cp = getContentPane
  cp.setLayout(new BorderLayout())
  cp.add(canvas, BorderLayout.CENTER)

  addKeyListener(new KeyAdapter {
    override def keyPressed(evt: KeyEvent): Unit = evt.getKeyCode match {
      case KeyEvent.VK_LEFT =>
        x1 -= 10
        x2 -= 10
        repaint()
      case KeyEvent.VK_RIGHT =>
        x1 += 10
        x2 += 10
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
      new CGTemplate()
    }
  })
}