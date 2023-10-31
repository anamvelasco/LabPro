import android.util.Patterns

fun emailValidator (text:String): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(text).matches()
}