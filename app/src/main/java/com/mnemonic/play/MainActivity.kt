package com.mnemonic.play


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.TickerMode
import kotlinx.coroutines.channels.ticker
import java.text.FieldPosition
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {


    @UseExperimental(ObsoleteCoroutinesApi::class)
    private var tickerChannel = ticker(1000, 0, mode = TickerMode.FIXED_PERIOD)
    lateinit var dialog: AlertDialog.Builder
    var seconds = -1
    var minutes = 0
    lateinit var level: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = AlertDialog.Builder(this)
        timerBtn.text = "Start"
        timerBtn.setOnClickListener {
            if (!boxView.enable) {
                boxView.enable = true
                startTimer()
            }
        }

        val levelDialog: LevelDialog = LevelDialog.getInstance()
        levelDialog.setListener(object : LevelDialog.LevelListener {
            override fun onClick(level: String) {
                setDifficulty(level)
            }

        })
        levelDialog.show(supportFragmentManager, levelDialog.javaClass.simpleName)


        boxView.setOnGameCompleteListener(object : BoxView.OnGameCompleteListener {
            override fun onCompleted() {
                tickerChannel.cancel()
                timerBtn.text = "Start"
                tickerChannel = ticker(1000, 0, mode = TickerMode.FIXED_PERIOD)
                dialog.setTitle("Congratulations")
                dialog.setCancelable(false)
                if (seconds < 10) dialog.setMessage("You have completed the game in ${minutes}:0${seconds}")
                else dialog.setMessage("You have passed the test in ${minutes}:${seconds}")
                dialog.setPositiveButton("Play again") { _, _ ->
                    run {
                        seconds = -1
                        minutes = 0
                        boxView.resetTheValues(level)
                    }
                }
                dialog.show()

            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset -> {
                if (boxView.enable) {
                    tickerChannel.cancel()
                    timerBtn.text = "Start"
                    tickerChannel = ticker(1000, 0, mode = TickerMode.FIXED_PERIOD)
                    seconds = -1
                    minutes = 0
                    boxView.resetTheValues(level)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun setDifficulty(selection: String) {
        level = selection
        when (selection) {
            "easy" -> {
                boxView.resetTheValues(selection)
            }
            "medium" -> {
                boxView.resetTheValues(selection)
            }
            else -> {
                boxView.resetTheValues(selection)
            }
        }
    }

    private fun startTimer() {
        GlobalScope.launch(Dispatchers.Main) {
            for (event in tickerChannel) { // event is of type Unit, so we don't really care about it
                if (seconds % 60 == 0) {
                    seconds = 0
                    minutes += 1
                }
                if (seconds == -1) seconds += 1
                if (seconds < 10) {
                    timerBtn.text = "${minutes}:0${seconds}"
                } else {
                    timerBtn.text = "${minutes}:${seconds}"
                }
                seconds += 1
            }
        }

    }


}
