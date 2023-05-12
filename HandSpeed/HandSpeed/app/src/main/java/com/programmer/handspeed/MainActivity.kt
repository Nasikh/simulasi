package com.programmer.handspeed

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var gameScoreText: TextView
    private lateinit var timeLeftText: TextView
    private lateinit var tapMeImage: ImageView
    private var score=0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown: Long = 60000
    private var countDownInterval: Long = 1000
    private var timeLeft=60
    private val TAG =MainActivity::class.java.simpleName

    companion object {
        private const val SCORE_KEY="SCORE_KEY"
        private const val TIME_LEFT_KEY="TIME_LEFT_KEY"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Sore: $score & Time Left: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.about_item){
            showInfo()
        }
        return true
    }

    private fun showInfo(){
        val dialogTitle=getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage=getString(R.string.about_message)
        val builder=AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        gameScoreText=findViewById(R.id.game_score_text)
        timeLeftText=findViewById(R.id.time_left_text)
        tapMeImage=findViewById(R.id.tap_me_image)

        tapMeImage.setOnClickListener{ v->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
            v.startAnimation(bounceAnimation)
            incrementScore()
        }


        Log.d(TAG, "onCreat called. Score is: $score")

        if(savedInstanceState !=null){
            score=savedInstanceState.getInt(SCORE_KEY)
            timeLeft=savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        }else{
            resetGame()
        }


    }

    private fun restoreGame(){
        val restoredScore=getString(R.string.your_score, score)
        gameScoreText.text=restoredScore
        val restoredTime=getString(R.string.time_left, timeLeft)
        timeLeftText.text=restoredTime
        countDownTimer=object : CountDownTimer((timeLeft * 1000).toLong(),countDownInterval){
            override fun onTick(millishUntilFinished: Long){
                timeLeft = millishUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left, timeLeft)
                timeLeftText.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        countDownTimer.start()
        gameStarted=true
    }

    private fun incrementScore(){
        score++
        val newScore = getString(R.string.your_score, score)
        gameScoreText.text=newScore
        if (!gameStarted){
            startGame()
        }
    }

    private fun resetGame(){
        score=0

        val initialScore = getString(R.string.your_score, score)
        gameScoreText.text=initialScore

        val initialTimeLeft = getString(R.string.time_left, 60)
        timeLeftText.text=initialTimeLeft

        countDownTimer=object : CountDownTimer(initialCountDown, countDownInterval){
            override fun  onTick(millisUntilFinished: Long){
                timeLeft=millisUntilFinished.toInt()/1000
                val timeLeftString=getString(R.string.time_left, timeLeft)
                timeLeftText.text=timeLeftString
            }
            override fun onFinish(){
                endGame()
            }
        }
        gameStarted=false

    }

    private fun startGame(){
        countDownTimer.start()
        gameStarted=true

    }

    private fun endGame(){
        Toast.makeText(this, getString(R.string.game_over_message, score), Toast.LENGTH_LONG).show()
        resetGame()
    }


}