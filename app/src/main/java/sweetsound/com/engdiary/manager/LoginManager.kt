package sweetsound.com.engdiary.manager

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

object LoginManager {
    private val mFirebaseAuth: FirebaseAuth

    init {
        mFirebaseAuth =  FirebaseAuth.getInstance();
    }

    fun signUp(email: String, passwd: String, onCompleteListener: OnCompleteListener<AuthResult>) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(onCompleteListener)
    }

    fun login(email: String, passwd: String, onCompleteListener: OnCompleteListener<AuthResult>) {
        mFirebaseAuth.signInWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(onCompleteListener)
    }

    // 당장은 복잡한 Key구조는 필요 없어 간단하게 저장할 수 있고 중복을 방지할 수 있는 정도로만 구현
    fun getPasswd(passwd: String): String {
        var key = ""

        passwd.forEach {
            key += Integer.toHexString(it.toInt())
        }

        return key
    }
}