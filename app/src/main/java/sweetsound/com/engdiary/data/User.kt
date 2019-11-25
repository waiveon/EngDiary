package sweetsound.com.engdiary.data

import sweetsound.com.engdiary.constants.Constants

data class User(var name: String,
                var email: String,
                var code: String) {

    fun toMap(): HashMap<String, String> {
        val resultMap = HashMap<String, String>()
        resultMap.put(Constants.name, name)
        resultMap.put(Constants.email, email)
        resultMap.put(Constants.code, code)

        return resultMap
    }
}