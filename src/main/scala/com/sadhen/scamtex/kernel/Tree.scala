package com.sadhen.scamtex.kernel

import com.sadhen.scamtex.kernel.TreeLabel.{TreeLabel, STRING, DOCUMENT}

/**
  * Created by sadhen on 8/4/16.
  */
class Tree(aTreeRep: TreeRep) {
  var parent: Tree = _
  var treeRep: TreeRep = aTreeRep
  def isCompound = treeRep.isInstanceOf[CompoundRep]

  def this(aTreeRep: TreeRep, aParent: Tree) {
    this(aTreeRep)
    parent = aParent
  }

  def +=(tree: Tree): Tree = {
    if (isCompound)
      treeRep.asInstanceOf[CompoundRep].left = tree :: treeRep.asInstanceOf[CompoundRep].left
    this
  }

  def previous: Option[Tree] =
    if (parent==null)
      Option.empty
    else {
      val rep = parent.treeRep.asInstanceOf[CompoundRep]
      var trees = rep.left.reverse ::: rep.right
      if (trees.head.eq(this)) {
        if (parent.treeRep.label == DOCUMENT)
          Option.empty
        else
          Option(parent)
      } else {
        while (!trees.tail.head.eq(this))
          trees =  trees.tail
        Option(trees.head)
      }
    }

  def next: Option[Tree] =
    if (parent == null)
      Option.empty
    else {
      val rep = parent.treeRep.asInstanceOf[CompoundRep]
      var trees = rep.left.reverse ::: rep.right
      while (!trees.head.eq(this))
        trees = trees.tail
      if (trees.tail.isEmpty)
        parent.next
      else
        Option(trees.tail.head)
    }

  override def toString = treeRep.toString
}

object Tree {
  def apply(rep: TreeRep) = new Tree(rep)
  def apply(rep: TreeRep, parent: Tree) = new Tree(rep, parent)
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

  def this(aLabel: TreeLabel, aRight: List[Tree]) {
    this(aLabel)
    this.right = aRight
  }

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

  def deleteLeft() = {
    if (left.isEmpty)
      false
    else {
      left = left.tail
      true
    }
  }

  def deleteRightAll() = {
    val ret = right
    right = List()
    ret
  }

  override def toString = "(" + label.toString + " " + (left.reverse ::: right).mkString(" ")  + ")"
}

object CompoundRep {
  def apply(label: TreeLabel) = new CompoundRep(label)
  def apply(label: TreeLabel, right: List[Tree]) = new CompoundRep(label, right)
}


object TreeLabel extends Enumeration {
  type TreeLabel = Value
  val DOCUMENT = Value("document")
  val PARA = Value
  val CONCAT = Value("concat")
  val STRING = Value
}
