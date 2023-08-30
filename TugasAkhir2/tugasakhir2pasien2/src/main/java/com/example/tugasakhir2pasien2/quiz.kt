package com.example.tugasakhir2pasien2
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.content.Intent

class QuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionButtons: List<Button>
    private var currentQuestionIndex = 0
    private val handler = Handler(Looper.getMainLooper())

    private var currentScore = 0
    private val maxScore = 10

    private val questions = listOf(
        Question("kaki ayam berjumlah ::", listOf("1", "2", "3", "4"), 1),
        Question("1 + 1 =", listOf("2", "1", "3", "4"), 0),
        // tambahkan soal lainnya
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz)

        questionTextView = findViewById(R.id.rectangle_1)
        optionButtons = listOf(
            findViewById(R.id.opsi1),
            findViewById(R.id.opsi2),
            findViewById(R.id.opsi3),
            findViewById(R.id.opsi4)
        )

        val correctAnswer = Color.parseColor("#008000") // Hijau
        val wrongAnswer = Color.parseColor("#FF0000") // Merah

        for (i in optionButtons.indices) {
            optionButtons[i].setOnClickListener {
                val isCorrect = checkAnswer(i)
                optionButtons.forEachIndexed { index, button ->
                    if (index == questions[currentQuestionIndex].correctAnswerIndex) {
                        button.setBackgroundColor(correctAnswer)
                    } else if (index == i && !isCorrect) {
                        button.setBackgroundColor(wrongAnswer)
                    }
                }
                calculateScore(isCorrect)
                handler.postDelayed({
                    if (currentQuestionIndex < questions.size - 1) {
                        currentQuestionIndex++
                    } else {
                        val intent = Intent(this, HasilQuizActivity::class.java)
                        intent.putExtra("score", currentScore)
                        intent.putExtra("maxScore", questions.size * maxScore)
                        startActivity(intent)
                    }
                    updateUI()
                }, 1000) // Tambahkan penundaan 1 detik (1000ms) sebelum pindah ke pertanyaan berikutnya
            }
        }

        updateUI()
    }

    private fun updateUI() {
        questionTextView.text = questions[currentQuestionIndex].questionText
        optionButtons.forEachIndexed { index, button ->
            button.text = questions[currentQuestionIndex].answerOptions[index]
            button.setBackgroundColor(Color.TRANSPARENT) // Mengatur ulang warna latar belakang
        }
    }

    private fun checkAnswer(selectedOptionIndex: Int): Boolean {
        return selectedOptionIndex == questions[currentQuestionIndex].correctAnswerIndex
    }

    private fun calculateScore(isCorrect: Boolean) {
        if (isCorrect) {
            currentScore += maxScore
        }
    }

    data class Question(
        val questionText: String,
        val answerOptions: List<String>,
        val correctAnswerIndex: Int
    )
}


