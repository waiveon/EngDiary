package sweetsound.com.engdiary.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import sweetsound.com.engdiary.R
import sweetsound.com.engdiary.constants.Constants
import sweetsound.com.engdiary.manager.LoginManager

class LoginActivity(): AppCompatActivity() {

    companion object {
        val SHARED_PREFERENCE_LOGIN_INFO = "SHARED_PREFERENCE_LOGIN_INFO"
        val KEY_AUTO_LOGIN = "KEY_AUTO_LOGIN"
        val KEY_LOGIN_NAME = "KEY_LOGIN_NAME"
        val KEY_LOGIN_EMAIL = "KEY_LOGIN_EMAIL"
        val KEY_LOGIN_PASSWD = "KEY_LOGIN_PASSWD"
        val KEY_LOGIN_TYPE = "KEY_LOGIN_TYPE"

        val SHARED_PREFERENCE_MY_CODE = "SHARED_PREFERENCE_MY_CODE"
        val KEY_MY_CODE = "KEY_MY_CODE"

        val INTENT_TYPE = "INTENT_TYPE"

        fun open(activity: Activity, type: Int) {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.putExtra(INTENT_TYPE, type)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val myCodePreference = getSharedPreferences(SHARED_PREFERENCE_MY_CODE, Activity.MODE_PRIVATE)
        code_textview.text = myCodePreference.getString(KEY_MY_CODE, "")

        val firebaseDb = FirebaseDatabase.getInstance().reference

        cancel_button.setOnClickListener {
            finish()
        }

        // Test
        email_edittext.setText("ljeongseok@ebay.com")
        name_edittext.setText("이정석")
        code_textview.setText("1919")

        email_edittext.setOnFocusChangeListener { view, hasFocus ->
            // 포커스가 없어지면서 내용이 비어 있지 않다면 이메일이 있다고 판단 하고 이메일 규칙 체크
            if (hasFocus == false && TextUtils.isEmpty(email_edittext.text) == false) {
                // 이메일이 맞지 않으면 지우고 다시 입력하라고 메시지 띄우자.
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email_edittext.text).matches() == false) {
                    email_edittext.setText("")

                    Toast.makeText(baseContext, R.string.wrong_email, Toast.LENGTH_SHORT).show()
                }
            }
        }

        register_button.setOnClickListener {
            if (TextUtils.isEmpty(name_edittext.text) == false &&
                    TextUtils.isEmpty(email_edittext.text) == false &&
                    TextUtils.isEmpty(code_textview.text) == false &&
                    TextUtils.isEmpty(passwd_edittext.text) == false) {
                LoginManager.signUp(email_edittext.text.toString(), LoginManager.getPasswd(passwd_edittext.text.toString()), object: OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful == true) { // 등록 성공
                            // 로그인 하자
                            loginProc(email_edittext.text.toString(), passwd_edittext.text.toString())
                        } else {
                            // TODO 암호가 6자리 미만 (이건 미리 제한하자)
                            // 이미 있는 이메일
                            Toast.makeText(baseContext, R.string.already_email, Toast.LENGTH_SHORT).show()
                        }
                    }
                })

                getSharedPreferences(SHARED_PREFERENCE_MY_CODE, Activity.MODE_PRIVATE).edit().putString(KEY_MY_CODE, code_textview.text.toString()).commit()
            } else {
                // 힌트 컬러 변경
                if (TextUtils.isEmpty(name_edittext.text) == true) {
                    name_edittext.setHintTextColor(Color.RED)
                }

                if (TextUtils.isEmpty(email_edittext.text) == true) {
                    email_edittext.setHintTextColor(Color.RED)
                }

                if (TextUtils.isEmpty(code_textview.text) == true) {
                    code_textview.setHintTextColor(Color.RED)
                }

                if (TextUtils.isEmpty(passwd_edittext.text) == true) {
                    passwd_edittext.setHintTextColor(Color.RED)
                }
            }
        }

        login_button.setOnClickListener {
            if (TextUtils.isEmpty(email_edittext.text) == false &&
                TextUtils.isEmpty(passwd_edittext.text) == false) {
                loginProc(email_edittext.text.toString(), passwd_edittext.text.toString())
            } else {
                // 힌트 컬러 변경
                if (TextUtils.isEmpty(email_edittext.text) == true) {
                    email_edittext.setHintTextColor(Color.RED)
                }

                if (TextUtils.isEmpty(passwd_edittext.text) == true) {
                    passwd_edittext.setHintTextColor(Color.RED)
                }
            }
        }

        auto_login_checkbox.setOnCheckedChangeListener { compoundButton, isChecked ->

            val sharedPreferenceEdit = getSharedPreferences(SHARED_PREFERENCE_LOGIN_INFO, Activity.MODE_PRIVATE).edit()

            if (isChecked == true) {
                sharedPreferenceEdit.putBoolean(KEY_AUTO_LOGIN, isChecked).commit()
                sharedPreferenceEdit.putString(KEY_LOGIN_NAME, name_edittext.text.toString()).commit()
                sharedPreferenceEdit.putString(KEY_LOGIN_EMAIL, email_edittext.text.toString()).commit()
                sharedPreferenceEdit.putString(KEY_LOGIN_PASSWD, LoginManager.getPasswd(passwd_edittext.text.toString())).commit()
                sharedPreferenceEdit.putInt(KEY_LOGIN_TYPE, intent.getIntExtra(INTENT_TYPE, Constants.STUDENT)).commit()
            } else {
                sharedPreferenceEdit.clear()
            }
        }

        code_create_button.setOnClickListener {
            val email = email_edittext.text

            if (TextUtils.isEmpty(email) == false) {
                var code = 0

                email.forEach {
                    code += it.toInt()
                }

                if (code > 0) {
                    code_textview.setText(code.toString())
                }
            } else {
                Toast.makeText(baseContext, R.string.error_code_create, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginProc(email: String, passwd: String) {
        LoginManager.login(email_edittext.text.toString(), LoginManager.getPasswd(passwd_edittext.text.toString()), object: OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful == true) {
                    // 리스트 화면으로 이동
                    DiaryListActivity.open(baseContext)
                } else {
                    // 등록이 안되어 있거나 네트워크 문제겠지.. 다시 하라고 하자.
                }
            }
        })
    }
}