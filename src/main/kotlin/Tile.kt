enum class Tile(var pawn: String) {
    White(pawn = "W"),
    Black(pawn = "B"),
    Empty(pawn = " ");

    var remainingMoves = 1
    var firstTime: Boolean = false
}