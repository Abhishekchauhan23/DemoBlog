package ab.sampleher.DemoBlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    val TAG:String="TAG"
    lateinit var SignUpButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

   // Init View
        SignUpButton=findViewById(R.id.SignUpButton)
    val email:EditText=findViewById(R.id.email_editext)
    val password:EditText=findViewById(R.id.password_edittext)
    val Login:Button=findViewById(R.id.Login_button)
        val auth=FirebaseAuth.getInstance()

        if (auth.currentUser!=null)
        {
            goPostActivity()
        }
        Login.setOnClickListener(View.OnClickListener {
            Login.isEnabled=false
            val emailVal=email.text.toString()
            val passwordVal=password.text.toString()

            if (emailVal.isBlank() || passwordVal.isBlank())
            {
                Toast.makeText(applicationContext,"Email and Password Can't Be Empty",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }


            auth.signInWithEmailAndPassword(emailVal,passwordVal).addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext,"SuccessFul Login",Toast.LENGTH_SHORT).show()
                    goPostActivity()
                    Login.isEnabled=true
                }
                else
                {
                    Log.i(TAG,"Login Failed " +task.exception)
                    Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_SHORT).show()
                }
            }
        })

//        SignUpButton.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this,CreateUserActivity::class.java))
//        })
    }

    fun goPostActivity()
    {
        Log.i(TAG,"PostActivity")
        val intent=Intent(this,PostActivity::class.java)
        startActivity(intent)
        finish()
    }
}