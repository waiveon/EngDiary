package sweetsound.com.engdiary

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import sweetsound.com.engdiary.constants.Constants
import sweetsound.com.engdiary.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferenceLogin = getSharedPreferences(LoginActivity.SHARED_PREFERENCE_LOGIN_INFO, Activity.MODE_PRIVATE)

        // 자동 로그인 체크해서 리스트 화면으로 바로 이동 하자
        if (sharedPreferenceLogin.getBoolean(LoginActivity.KEY_AUTO_LOGIN, false) == true) {

            val firebaseDb = FirebaseDatabase.getInstance().reference
            firebaseDb.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    when (sharedPreferenceLogin.getInt(LoginActivity.KEY_LOGIN_TYPE, Constants.STUDENT)) {
                        Constants.STUDENT -> dataSnapshot.child(Constants.FIREBASE_TABLE_NAME_STUDENT)

                        Constants.TEATHER -> dataSnapshot.child(Constants.FIREBASE_TABLE_NAME_TEATHER)
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
