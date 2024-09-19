//Test with the creation as an Array of Cells
case class Cell(x: Int, y : Int)

val cell1 = Cell(10,10)
cell1.x
cell1.y

val cell2 = Cell(10,10)

case class Field(cells : Array[Cell])

val field1 = Field(Array.ofDim[Cell](2))
field1.cells(0) = cell1
field1.cells(0).x
field1.cells(0).y

field1.cells(1) = cell2
field1.cells(1).x
field1.cells(1).y

