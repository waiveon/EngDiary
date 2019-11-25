package sweetsound.com.engdiary.manager

class LoginManager private constructor() {

    companion object {
        @Volatile private var mInstance: LoginManager? = null

        fun getInstance(): LoginManager =
            mInstance ?: synchronized(this) {
                mInstance ?: LoginManager().also {
                    mInstance = it
                }
            }
    }
}