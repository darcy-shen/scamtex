package com.sadhen.scamtex.edit

import com.sadhen.scamtex.kernel.{Tree, CompoundRep}
import com.sadhen.scamtex.kernel.TreeLabel.CONCAT

/**
  * Created by rendong on 16/8/7.
  */
object Editor {
  def moveLeft(current: Tree) = {
    if (current.treeRep.asInstanceOf[CompoundRep].moveLeft() || current.previous.isEmpty)
      current
    else
      current.previous.get
  }

  def moveRight(current: Tree) = {
    if (current.treeRep.asInstanceOf[CompoundRep].moveRight() || current.next.isEmpty)
      current
    else
      current.next.get
  }

  def deleteLeft(current: Tree) = {
    val rep = current.treeRep.asInstanceOf[CompoundRep]
    if (rep.deleteLeft() || current.previous.isEmpty)
      current
    else {
      val newCurrent = current.previous.get
      if (rep.isEmpty)
        current.destroy
      newCurrent
    }
  }

  def newLine(current: Tree): Tree = {
    Tree(CompoundRep(CONCAT, current.treeRep.asInstanceOf[CompoundRep].deleteRightAll()), current.parent)
  }
}
