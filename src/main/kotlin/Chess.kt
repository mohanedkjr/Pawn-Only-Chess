import kotlin.math.abs

class Chess {
    private var board: MutableList<MutableList<Tile>>
    private var isRunning = true
    private var whiteTile = Tile.White
    private var blackTile = Tile.Black
    private var emptyTile = Tile.Empty

    init {
        println("Pawns-Only Chess")
        fun row(rowIndex: Int): MutableList<Tile> {
            return when (8 - (rowIndex + 1)) {
                6 -> MutableList(8) { whiteTile }
                1 -> MutableList(8) { blackTile }
                else -> MutableList(8) { emptyTile }
            }
        }

        board = MutableList(8) { row(it) }
    }

    private val p1 = Player.White
    private val p2 = Player.Black
    private var currentPlayer = p1

    private fun showBoard() {
        val separator = MutableList(9) { "+" }.joinToString(separator = "---", prefix = "  ")
        val letters = listOf("a", "b", "c", "d", "e", "f", "g", "h")

        val boardString = StringBuilder()

        for (i in 7 downTo 0) {
            boardString.append(separator)
            boardString.append("\n${i + 1} |")
            for (j in 0..7) {
                boardString.append(" ${board[i][j].pawn} |")
            }
            boardString.append("\n")
        }

        boardString.append(separator)
        boardString.append("\n")
        boardString.append(letters.joinToString(separator = "   ", prefix = "    "))

        println(boardString.toString())
    }


    private var prevMoveWhite = ""
    private var prevMoveBlack = ""
    private fun moveOnBoard() {
        val playerPawn = currentPlayer.pawn.toString()
        val move = currentPlayer.move()
        if (move == "exit") exit()
        else {
            val columns = "abcdefgh"
            val fromRow = move[1].toString().toInt() - 1
            val fromColumn = columns.indexOf(move[0])
            val fromSquare = board[fromRow][fromColumn]
            val tileMapping = mapOf(
                p1 to whiteTile,
                p2 to blackTile
            )
            // Input Validation
            when {
                move[1] == move[3] && fromSquare.pawn == playerPawn -> {
                    println("Invalid Input")
                    moveOnBoard()
                    return
                }

                fromSquare.pawn != playerPawn -> {
                    println("No ${currentPlayer.color} pawn at ${move[0]}${move[1]}")
                    moveOnBoard()
                    return
                }
            }

            val toRow = move[3].toString().toInt() - 1
            val toColumn = columns.indexOf(move[2])
            val toTile = board[toRow][toColumn]
            fun enPassant() {
                board[fromRow][fromColumn] = emptyTile
                board[fromRow][toColumn] = emptyTile
                board[toRow][toColumn] = tileMapping[currentPlayer]!!
            }
            // Move Validation
            when {
                fromColumn == toColumn && toTile.pawn == currentPlayer.opponent -> {
                    println("Invalid Input")
                    moveOnBoard()
                    return
                }
//                En Passant
                abs(toRow - fromRow) == 1 && board[fromRow][toColumn].pawn == currentPlayer.opponent -> {
                    if (currentPlayer == Player.White && prevMoveWhite.slice(2..3) == move.slice(0..1)) {
                        enPassant()
                    } else if (currentPlayer == Player.Black && prevMoveBlack.slice(2..3) == move.slice(0..1)) {
                        enPassant()
                    } else {
                        println("Invalid Input")
                        moveOnBoard()
                        return
                    }
                }

                abs(toRow - fromRow) == 1 && toTile == Tile.Empty && (abs(fromColumn - toColumn) == 1) -> {
                    println("Invalid Input")
                    moveOnBoard()
                    return
                }
//                Capture
                abs(toRow - fromRow) == 1 && toTile.pawn == currentPlayer.opponent && (abs(fromColumn - toColumn) == 1) -> {
                    board[fromRow][fromColumn] = emptyTile
                    board[fromRow][fromColumn].firstTime = false
                    board[toRow][toColumn] = tileMapping[currentPlayer]!!
                    board[toRow][toColumn].firstTime = true
                }

//                Normal Move
                else -> {
                    board[fromRow][fromColumn] = emptyTile
                    board[toRow][toColumn] = tileMapping[currentPlayer]!!
                    if (toTile.remainingMoves == 1) {
                        board[toRow][toColumn].firstTime = true
                        board[toRow][toColumn].remainingMoves = 0
                    } else {
                        board[toRow][toColumn].firstTime = false
                    }
                    if (currentPlayer.winTile.matches(move)) {
                        showBoard()
                        win(currentPlayer)
                        return
                    } else if (board.joinToString { it.joinToString() }
                            .count { it.toString() == currentPlayer.opponent } == 1) {
                        if (currentPlayer == Player.Black && board[toRow - 1][toColumn] == whiteTile) {
                            showBoard()
                            win(currentPlayer)
                            return
                        } else if (currentPlayer == Player.White && board[toRow + 1][toColumn] == blackTile) {
                            showBoard()
                            println("Stalemate!")
                            exit()
                            return
                        }
                    }
                }

            }
            if (currentPlayer == Player.White) prevMoveWhite = move else prevMoveBlack = move

            showBoard()
        }
    }

    private fun win(player: Player) {
        println("${player.color.replaceFirstChar { it.uppercase() }} Wins!")
        exit()
    }

    private fun checkWin() {
        val currentBoard = board.joinToString { it.joinToString() }

        when {
            currentBoard.count { it == 'W' } == 0 -> {
                win(p2)
            }

            currentBoard.count { it == 'B' } == 0 -> {
                win(p1)
            }

        }
    }

    fun run() {
        showBoard()
        while (isRunning) {
            currentPlayer = if (currentPlayer == p1) {
                moveOnBoard()
                p2
            } else {
                moveOnBoard()
                p1
            }
            checkWin()
        }
    }

    private fun exit() {
        println("Bye!")
        isRunning = false
    }
}