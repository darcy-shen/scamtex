package com.sadhen.scamtex.edit

import com.sadhen.scamtex.kernel.{Tree, CompoundRep}
import com.sadhen.scamtex.kernel.TreeLabel.CONCAT

/**
  * Created by rendong on 16/8/7.
  */
object Editor {
  def moveLeft(current: Tree) = {
    current.treeRep.asInstanceOf[CompoundRep].moveLeft()
  }

  def moveRight(current: Tree) = {
    current.treeRep.asInstanceOf[CompoundRep].moveRight()
  }

  def deleteLeft(current: Tree) = {
    current.treeRep.asInstanceOf[CompoundRep].deleteLeft()
  }

  def newLine(current: Tree): Tree = {
    Tree(CompoundRep(CONCAT, current.treeRep.asInstanceOf[CompoundRep].deleteRightAll()), current.parent)
  }
}
