package sweetsound.com.engdiary

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.android.synthetic.main.activity_main.*
import sweetsound.com.engdiary.constants.Constants
import sweetsound.com.engdiary.manager.LoginManager
import sweetsound.com.engdiary.ui.DiaryListActivity
import sweetsound.com.engdiary.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferenceLogin = getSharedPreferences(LoginActivity.SHARED_PREFERENCE_LOGIN_INFO, Activity.MODE_PRIVATE)

        // 자동 로그인 체크해서 리스트 화면으로 바로 이동 하자
        if (sharedPreferenceLogin.getBoolean(LoginActivity.KEY_AUTO_LOGIN, false) == true) {
            val email = sharedPreferenceLogin.getString(LoginActivity.KEY_LOGIN_EMAIL, "")
            val passwd = sharedPreferenceLogin.getString(LoginActivity.KEY_LOGIN_PASSWD, "")

            LoginManager.login(email!!, passwd!!, object: OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful == true) {
                        // 리스트 화면으로 이동
                        DiaryListActivity.open(baseContext)
                    } else {
                        // 로그인 실패
                        // TODO 편리하게는 - 재시도 버튼 추가
                        Toast.makeText(baseContext, R.string.login_fail, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            student_button.setOnClickListener {
                LoginActivity.open(this, Constants.STUDENT)
            }

            teacher_button.setOnClickListener {
                LoginActivity.open(this, Constants.TEATHER)
            }
        }
    }
}
