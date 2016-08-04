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
      treeRep.asInstanceOf[CompoundRep].right = tree :: treeRep.asInstanceOf[CompoundRep].right
    this
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

class CompoundRep(aLabel: TreeLabel, aList: List[Tree]) extends TreeRep {
  var left: List[Tree] = List()
  var right: List[Tree] = aList
  this.label = aLabel

  override def toString = "(" + label.toString + " " + (left.reverse ::: right).mkString(" ")  + ")"
}

object CompoundRep {
  def apply(aLabel: TreeLabel, aList: List[Tree]) = new CompoundRep(aLabel, aList)
}


object TreeLabel extends Enumeration {
  type TreeLabel = Value
  val DOCUMENT = Value("document")
  val PARA = Value
  val CONCAT = Value
  val STRING = Value
}
