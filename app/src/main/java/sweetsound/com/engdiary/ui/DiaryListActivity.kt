package sweetsound.com.engdiary.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_diary_list.*
import sweetsound.com.engdiary.R

class DiaryListActivity(): AppCompatActivity() {

    companion object {
        fun open(context: Context) {
            context.startActivity(Intent(context, DiaryListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_list)

        new_dirary_button.setOnClickListener {
            NewDiraryActivity.open(this)
        }

        // target을 불러 오는데 한개면 내꺼고
        // 여러개면 구분을 해줘야 해. 해 주는게 좋지. 맨 앞에 1dp 선 그어주자.
    }
}