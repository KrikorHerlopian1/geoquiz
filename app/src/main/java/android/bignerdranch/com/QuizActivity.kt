package android.bignerdranch.com

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_quiz.*


class QuizActivity : AppCompatActivity() {
    val mQuestionBank = arrayOf<Question>(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.questions_americas, true),
        Question(R.string.question_asia, true)
    )
    var mCurrentIndex = 0
    var cheatCount = 0
    var maxCheat = 3
    var isCheater: Boolean? = false
    var numberOfCorrectAnswers = 0
    var numberOfIncorrectAnswers = 0
    var hashMap : HashMap<Int, Boolean>
            = HashMap<Int, Boolean> ()
    companion object {
        const val TAG = "QuizActivity"
        const val KEY_INDEX = "index"
        const val KEY_CHEAT= "cheat"
        const val HASH_MAP = "hashmap"
        const val KEY_CORRECT = "correct"
        const val KEY_INCORRECT = "incorrect"
        const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
        const val REQUEST_CODE_CHEAT = 0
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_quiz)
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            numberOfCorrectAnswers = savedInstanceState.getInt(KEY_CORRECT, 0)
            numberOfIncorrectAnswers = savedInstanceState.getInt(KEY_INCORRECT, 0)
            hashMap = savedInstanceState.getSerializable(HASH_MAP) as HashMap<Int, Boolean>
            cheatCount = savedInstanceState.getInt(KEY_CHEAT, 0)
        }
        true_button.setOnClickListener {
            it.setEnabled(false)
            false_button.setEnabled(false)
            checkAnswer(true)
            hashMap.put(mCurrentIndex, true)
        }
        false_button.setOnClickListener {
            it.setEnabled(false)
            true_button.setEnabled(false)
            checkAnswer(false)
            hashMap.put(mCurrentIndex, false)
        }
        prev_button.setOnClickListener {
            if(mCurrentIndex == 0)
                mCurrentIndex = mQuestionBank.size - 1
            else
                mCurrentIndex = (mCurrentIndex-1) % mQuestionBank.size
            isCheater = false
            updateQuestion()
        }
        next_button.setOnClickListener {
            mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.size
            updateQuestion()
            isCheater = false
        }
        question_text_view.setOnClickListener {
            mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.size
            updateQuestion()
        }
        if(cheatCount == maxCheat)
            cheat_button.setEnabled(false)
        cheat_button.setOnClickListener {
            val intent = Intent(this, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, mQuestionBank[mCurrentIndex].mAnswerTrue)
            }
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_CHEAT){
                isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
                //true_button.setEnabled(false)
                //false_button.setEnabled(false)
                //hashMap.put(mCurrentIndex, mQuestionBank[mCurrentIndex].mAnswerTrue)
                //checkAnswer(mQuestionBank[mCurrentIndex].mAnswerTrue)
                cheatCount++
                if(cheatCount == maxCheat)
                    cheat_button.setEnabled(false)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, mCurrentIndex)
        outState.putInt(KEY_CORRECT, numberOfCorrectAnswers)
        outState.putInt(KEY_INCORRECT, numberOfIncorrectAnswers)
        outState.putInt(KEY_CHEAT, cheatCount)
        outState.putSerializable(HASH_MAP, hashMap)
    }

    private fun updateQuestion(){
        question_text_view.setText(mQuestionBank[mCurrentIndex].mTextResId)
        if(hashMap.containsKey(mCurrentIndex)) {
            true_button.setEnabled(false)
            false_button.setEnabled(false)
        } else {
            true_button.setEnabled(true)
            false_button.setEnabled(true)
        }
    }

    private fun checkAnswer(userPressedTrue: Boolean){
        var answerTrue = mQuestionBank[mCurrentIndex].mAnswerTrue
        var messageResId = 0
        /*if(isCheater == true){
            messageResId = R.string.judgment_toast
            numberOfIncorrectAnswers++
        }
        else*/
        if(userPressedTrue == answerTrue){
            messageResId = R.string.correct_toast
            numberOfCorrectAnswers++
        }
        else{
            messageResId = R.string.incorrect_toast
            numberOfIncorrectAnswers++
        }
        var toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()

        if (numberOfCorrectAnswers + numberOfIncorrectAnswers === mQuestionBank.size) {
            val percentageOfCorrectAnswers = numberOfCorrectAnswers.toDouble()/ mQuestionBank.size.toDouble() * 100
            Toast.makeText(
                this,"You got "+percentageOfCorrectAnswers+" %",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}