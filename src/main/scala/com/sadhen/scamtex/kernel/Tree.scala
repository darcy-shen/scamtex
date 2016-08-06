package com.sadhen.scamtex.kernel

import com.sadhen.scamtex.kernel.TreeLabel.{TreeLabel, STRING}

/**
  * Created by sadhen on 8/4/16.
  */
class Tree(aTreeRep: TreeRep) {
  var treeRep: TreeRep = aTreeRep
  def isCompound = treeRep.isInstanceOf[CompoundRep]

  def +=(tree: Tree): Tree = {
    if (isCompound)
      treeRep.asInstanceOf[CompoundRep].left = tree :: treeRep.asInstanceOf[CompoundRep].left
    this
  }

  def moveLeft() = {
    isCompound && treeRep.asInstanceOf[CompoundRep].moveLeft()
  }

  def moveRight() = {
    isCompound && treeRep.asInstanceOf[CompoundRep].moveRight()
  }

  override def toString = treeRep.toString
}

object Tree {
  def apply(aTreeRep: TreeRep) = new Tree(aTreeRep)
}

class TreeRep {
  var label: TreeLabel = _

  def this(aLabel: TreeLabel) = {
    this
    label = aLabel
  }
}

object TreeRep {
  def apply(aLabel: TreeLabel) = new TreeRep(aLabel)
}

class AtomicRep(aLabel: TreeLabel, aContent: String) extends TreeRep {
  var content = aContent
  this.label = aLabel

  override def toString = "\"" + content + "\""
}

object AtomicRep {
  def apply(aContent: String) = new AtomicRep(STRING, aContent)
}

class CompoundRep(aLabel: TreeLabel) extends TreeRep {
  var left: List[Tree] = List()
  var right: List[Tree] = List()
  this.label = aLabel

  def moveLeft() = {
    if (left.isEmpty)
      false
    else {
      right = left.head :: right
      left = left.tail
      true
    }
  }

  def moveRight() = {
    if (right.isEmpty)
      false
    else {
      left = right.head :: left
      right = right.tail
      true
    }
  }

  override def toString = "(" + label.toString + " " + (left.reverse ::: right).mkString(" ")  + ")"
}

object CompoundRep {
  def apply(aLabel: TreeLabel) = new CompoundRep(aLabel)
}


object TreeLabel extends Enumeration {
  type TreeLabel = Value
  val DOCUMENT = Value("document")
  val PARA = Value
  val CONCAT = Value
  val STRING = Value
}
