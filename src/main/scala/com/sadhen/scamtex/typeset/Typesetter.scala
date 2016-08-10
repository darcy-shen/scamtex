package com.sadhen.scamtex.typeset

import com.sadhen.scamtex.kernel.{AtomicRep, CompoundRep, Tree}
import com.sadhen.scamtex.kernel.TreeLabel.DOCUMENT

import org.log4s._
import javafx.scene.shape.Rectangle
import javafx.geometry.Bounds
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.paint.Color
import scalafx.scene.shape.Shape
import scalafx.scene.text.Text

/**
  * Created by sadhen on 8/5/16.
  */
object Typesetter {
  private val logger = getLogger

  var preX: Double = _
  var preY: Double = _
  var preH: Double = 0

  def drawCursor(gc: GraphicsContext, x: Double, y: Double, h: Double): Unit = {
    def draw(x: Double, y: Double, h: Double, color: Color): Unit = {
      gc.setStroke(color)
      gc.strokeLine(x, y, x, y-h)
      gc.strokeLine(x-1, y, x+1, y)
      gc.strokeLine(x-1, y-h, x+1, y-h)
    }
    if (preH != 0)
      draw(preX, preY, preH, Color.White)
    draw(x, y, h, Color.Red)
    preX = x; preY = y; preH = h
  }

  def render(current: Tree, document: Tree, gc: GraphicsContext, x: Double, y: Double) {
    var px = x
    var py = y
    var descent: Double = 0
    var height: Double = 0
    renderIter(document)

    def renderIter(document: Tree): Unit = {
      document.treeRep match {
        case atomic: AtomicRep =>
          gc.setFill(Color.Black)
          gc.fillText(atomic.content, px, py)
          val actualBounds = getActualStringBounds(atomic.content)
          val bounds = getStringBounds(atomic.content)
          height = actualBounds.getHeight
          descent = actualBounds.getMaxY
          px = px + bounds.getWidth.floor

        case compound: CompoundRep =>
          compound.left.reverse.foreach(renderIter(_))
          if (document.eq(current))
            drawCursor(gc, px, py+descent, height)
          compound.right.foreach(renderIter(_))
      }
      if (document.parent!=null && document.parent.treeRep.label == DOCUMENT) {
        px = 20
        py = py + 30
      }
    }
  }

  def getStringBounds(str: String): Bounds = {
    val text = new Text(str)
    text.getBoundsInLocal
  }

  def getActualStringBounds(str: String): Bounds = {
    val text = new Text(str)
    val tb = text.getBoundsInLocal
    val stencil = new Rectangle(tb.getMinX, tb.getMinY, tb.getWidth, tb.getHeight)
    val intersection = Shape.intersect(text, stencil)
    intersection.getBoundsInLocal
  }
}