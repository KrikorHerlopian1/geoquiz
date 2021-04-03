package android.bignerdranch.com

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import kotlinx.android.synthetic.main.activity_cheat.*

class CheatActivity: AppCompatActivity() {
    var isAnswerShown = false
    companion object {
        const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        val answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        if(savedInstanceState != null){
            isAnswerShown = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN, false)
            if(isAnswerShown){
                if(answer)
                    answer_text_view.setText(R.string.true_button)
                else
                    answer_text_view.setText(R.string.false_button)
                setAnswerShownResult(true)
            }
        }
        show_answer_button.setOnClickListener {
            if(answer)
                answer_text_view.setText(R.string.true_button)
            else
                answer_text_view.setText(R.string.false_button)
            isAnswerShown = true
            setAnswerShownResult(true)

            var cx = it.width/2
            var cy = it.height/2
            var radius: Float = it.width.toFloat()
            var anim: Animator = ViewAnimationUtils.createCircularReveal(it, cx, cy, radius, 0f)
            anim.apply {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        show_answer_button.visibility = View.INVISIBLE
                    }
                })
                start()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(QuizActivity.TAG, "onSaveInstanceState")
        outState.putBoolean(EXTRA_ANSWER_SHOWN, isAnswerShown)
    }

    fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(RESULT_OK, data)
    }
}