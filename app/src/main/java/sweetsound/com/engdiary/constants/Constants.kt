package sweetsound.com.engdiary.constants

class Constants {
    companion object {
        const val FIREBASE_TABLE_NAME_STUDENT = "Student"
        const val FIREBASE_TABLE_NAME_TEATHER = "Teather"

        const val STUDENT = 0
        const val TEATHER = 1

        const val code = "code"
        const val email = "email"
        const val name = "name"

        // 당장은 복잡한 Key구조는 필요 없어 간단하게 저장할 수 있고 중복을 방지할 수 있는 정도로만 구현
        fun getKey(email: String): String {
            var key = ""

            email.forEach {
                key += Integer.toHexString(it.toInt())
            }

            return key
        }
    }
}