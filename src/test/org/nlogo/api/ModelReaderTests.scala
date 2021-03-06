// (C) 2012 Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.api

import org.scalatest.FunSuite

class ModelReaderTests extends FunSuite {

  /// parseModel

  test("parseModel: empty model has correct version string") {
    val emptyModel = FileIO.file2String("resources/system/empty.nlogo")
    val map = ModelReader.parseModel(emptyModel)
    assert("NetLogo (no version)" === ModelReader.parseVersion(map))
  }
  test("parseModel: trailing section with no separator isn't ignored") {
    val map = ModelReader.parseModel("foo\n" + ModelReader.SEPARATOR + "\nbar\n")
    assert(map.get(ModelSection.Code).toList === List("foo"))
    assert(map.get(ModelSection.Interface).toList === List("bar"))
  }
  test("parseModel: missing sections result in empty arrays") {
    val map = ModelReader.parseModel("")
    assert(map.get(ModelSection.Interface).toList === Nil)
  }

  /// parseWidgets

  test("parseWidgets: one widget, with trailing blank line") {
    val widgets = ModelReader.parseWidgets(Array("FOO",""))
    assert(widgets.size === 1)
    assert(widgets.get(0).size === 1)
    assert(widgets.get(0).get(0) === "FOO")
  }
  test("parseWidgets: one widget, multiple trailing blank lines") {
    val widgets = ModelReader.parseWidgets(Array("FOO","","","","",""))
    assert(widgets.size === 1)
    assert(widgets.get(0).size === 1)
    assert(widgets.get(0).get(0) === "FOO")
  }
  test("parseWidgets: one widget, no blank line after") {
    val widgets = ModelReader.parseWidgets(Array("FOO"))
    assert(widgets.size === 1)
    assert(widgets.get(0).size === 1)
    assert(widgets.get(0).get(0) === "FOO")
  }
  test("parseWidgets: one widget, leading blank lines") {
    val widgets = ModelReader.parseWidgets(Array("","","","FOO"))
    assert(widgets.size === 1)
    assert(widgets.get(0).size === 1)
    assert(widgets.get(0).get(0) === "FOO")
  }
  test("parseWidgets: multiple blank lines between widgets ignored") {
    val widgets = ModelReader.parseWidgets(Array("FOO","","","","","","BAR"))
    assert(widgets.size === 2)
    assert(widgets.get(0).size === 1)
    assert(widgets.get(0).get(0) === "FOO")
    assert(widgets.get(1).size === 1)
    assert(widgets.get(1).get(0) === "BAR")
  }
  test("parseWidgets: empty model contains only view") {
    val emptyModel = FileIO.file2String("resources/system/empty.nlogo")
    val map = ModelReader.parseModel(emptyModel)
    val widgets = ModelReader.parseWidgets(map.get(ModelSection.Interface))
    assert(widgets.size === 1)
    assert(widgets.get(0).get(0) === "GRAPHICS-WINDOW")
  }
  
  // strip/restoreLines
  test("StripSimple") {
    expect("foo")(ModelReader.stripLines("foo"))
  }
  test("StripLineBreak") {
    expect("foo\\nbar")(ModelReader.stripLines("foo\nbar"))
  }
  test("StripConsecutiveLineBreaks") {
    expect("foo\\n\\nbar")(ModelReader.stripLines("foo\n\nbar"))
  }
  test("StripTrailingLineBreak") {
    expect("foo\\n")(ModelReader.stripLines("foo\n"))
  }
  test("StripInnerNewLines") {
    expect("foo\\n\\\"foo\\\\n\\\"")(
      ModelReader.stripLines("foo\n\"foo\\n\""))
  }
  test("RestoreSimple") {
    expect("foo")(ModelReader.restoreLines("foo"))
  }
  test("RestoreLineBreak") {
    expect("foo\nbar")(ModelReader.restoreLines("foo\\nbar"))
  }
  test("RestoreConsecutiveLineBreaks") {
    expect("foo\n\nbar")(ModelReader.restoreLines("foo\\n\\nbar"))
  }
  test("RestoreInitialLineBreaks") {
    expect("\n\n\nfoo")(ModelReader.restoreLines("\\n\\n\\nfoo"))
  }
  test("RestoreInnerNewLines") {
    expect("foo\n\"foo\\n\"")(
      ModelReader.restoreLines("foo\\n\"foo\\\\n\""))
  }

}
