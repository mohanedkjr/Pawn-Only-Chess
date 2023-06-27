import kotlin.math.abs

enum class Player(
    private var playerName: String,
    role: String,
    val pawn: Char,
    val color: String,
    val opponent: String,
    val winTile: Regex,
) {
    White("", "First", 'W', "white", "B", Regex("[a-h]7[a-h]8")) {
        override fun validateInput(input: String): Boolean {
            return input.matches(Regex("([a-h][1-8]){2}")) &&
                    (input[0] == input[2] || abs(input[0] - input[2]) == 1) &&
                    (input[1] == input[3] - 1 || input.matches(Regex("[a-h]2[a-h]4")) || (input[1] == input[3]))
        }
    },
    Black("", "Second", 'B', "black", "W", Regex("[a-h]2[a-h]1")) {
        override fun validateInput(input: String): Boolean {
            return input.matches(Regex("([a-h][1-8]){2}")) &&
                    (input[0] == input[2] || abs(input[0] - input[2]) == 1) &&
                    (input[1] == input[3] + 1 || input.matches(Regex("[a-h]7[a-h]5")) || (input[1] == input[3]))
        }
    };

    abstract fun validateInput(input: String): Boolean

    init {
        print("$role Player's name: ")
        playerName = readln()
    }

    fun move(): String {
        while (true) {
            println("$playerName's turn: ")
            val input = readln().lowercase()
            if (input == "exit") {
                return "exit"
            } else if (validateInput(input)) {
                return input
            } else {
                println("Invalid Input")
            }
        }
    }
}