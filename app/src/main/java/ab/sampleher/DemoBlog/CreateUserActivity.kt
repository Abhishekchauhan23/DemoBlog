package ab.sampleher.DemoBlog

import ab.sampleher.DemoBlog.models.Users
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG:String="CreateUser"
class CreateUserActivity : AppCompatActivity() {

    private lateinit var etEmail:EditText
    private lateinit var etPass:EditText
    private lateinit var etAge:EditText
    private lateinit var RegisterButton:Button
    private lateinit var fAuth:FirebaseAuth
    lateinit var etMailValue:String
    lateinit var etPassValue:String
    lateinit var fFireStore:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        var etAgeValue:Int=0
        //Init Views
        etEmail=findViewById(R.id.editTextTextEmailAddress)
        etPass=findViewById(R.id.NewPassword)
        etAge=findViewById(R.id.etAge)
        RegisterButton=findViewById(R.id.RegisterButton)
        fAuth=FirebaseAuth.getInstance()
        fFireStore= FirebaseFirestore.getInstance()

        RegisterButton.setOnClickListener(View.OnClickListener {
             etMailValue=etEmail.text.toString()
             etPassValue=etPass.text.toString()
             etAgeValue=etAge.text.toString().toInt()

            if (etMailValue.isBlank() || etPassValue.isBlank())
            {
                Toast.makeText(applicationContext,"Complete the fields", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
             val us=Users(etMailValue,etAgeValue)
            fAuth.createUserWithEmailAndPassword(etMailValue,etPassValue).addOnCompleteListener{
                if (it.isSuccessful)
                 fAuth.signInWithEmailAndPassword(etMailValue,etPassValue)
                 val id:String=fFireStore.collection("users").id
                 fFireStore.collection("users").document(id).set(us)
                 Toast.makeText(this,"You have successfully crated account",Toast.LENGTH_SHORT).show()



           }
        })
    }
}