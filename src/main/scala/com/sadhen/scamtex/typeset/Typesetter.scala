package com.sadhen.scamtex.typeset

import java.awt.{Color, Graphics, Graphics2D, Rectangle}

import org.log4s._
import com.sadhen.scamtex.kernel.{AtomicRep, CompoundRep, Tree}

/**
  * Created by sadhen on 8/5/16.
  */
object Typesetter {
  private val logger = getLogger

  var preX: Int = _
  var preY: Int = _
  var preH: Int = 0

  def drawCursor(graphics: Graphics, x: Int, y: Int, h: Int): Unit = {
    def draw(x:Int, y:Int, h:Int, color: Color): Unit = {
      graphics.setColor(color)
      graphics.drawLine(x, y, x, y-h)
      graphics.drawLine(x-1, y, x+1, y)
      graphics.drawLine(x-1, y-h, x+1, y-h)
    }
    if (preH != 0)
      draw(preX, preY, preH, Color.WHITE)
    draw(x, y, h, Color.RED)
    preX = x; preY = y; preH = h
  }

  def render(document: Tree, graphics: Graphics, x: Int, y: Int) {
    var px = x
    var py = y
    renderIter(document)

    def renderIter(document: Tree): Unit = document.treeRep match {
      case atomic: AtomicRep =>
        graphics.setColor(Color.BLACK)
        val rect = getStringBounds(graphics, atomic.content, px, py)
        val h = rect.getHeight.toInt
        val descent = h + rect.getY.toInt
        graphics.drawString(atomic.content, px, py)
        val w = graphics.getFontMetrics().stringWidth(atomic.content)
        px = px + w
        drawCursor(graphics, px, py+descent/2, h/2)

      case compound: CompoundRep =>
        compound.left.reverse.foreach(renderIter(_))
        compound.right.foreach(renderIter(_))
    }
  }

  private def getStringBounds(graphics: Graphics, str: String, x: Int, y: Int): Rectangle = {
    val frc = graphics.asInstanceOf[Graphics2D].getFontRenderContext
    val gv = graphics.getFont.createGlyphVector(frc, str)
    gv.getPixelBounds(null, 0, 0)
  }
}
