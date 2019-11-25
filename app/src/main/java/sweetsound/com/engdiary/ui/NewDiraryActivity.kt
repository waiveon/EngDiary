package sweetsound.com.engdiary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_new_diary.*
import sweetsound.com.engdiary.R


class NewDiraryActivity(): AppCompatActivity() {
    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, NewDiraryActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_diary)

        english_diary_edittext.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                // 자동 개행이 필요한지 체크
                if (auto_line_feed_checkbox.isChecked() == true && s.length > 1) {
                    // 마침표 인지 체크
                    if (s.substring(s.length -2).equals(". ")) {
                        english_diary_edittext.setText(english_diary_edittext.text?.toString() + "\n")
                        english_diary_edittext.setSelection(english_diary_edittext.text.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })

    }
}