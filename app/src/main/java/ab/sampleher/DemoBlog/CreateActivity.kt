package ab.sampleher.DemoBlog

import ab.sampleher.DemoBlog.models.Posts
import ab.sampleher.DemoBlog.models.Users
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val TAG="CreateActivity"
private const val PICK_PHOTO_CODE=1234
class CreateActivity : AppCompatActivity() {

    lateinit var pickImage:Button
    lateinit var imageView: ImageView
    private var photoUri:Uri?=null
    private lateinit var btnSubmit:Button
    private lateinit var etDiscription:EditText
    private var signedInUser: Users?=null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var storageRefe:StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        // Variable Init
        pickImage=findViewById(R.id.btnPickimage)
        imageView=findViewById(R.id.imageView)
        btnSubmit=findViewById(R.id.btnSubmit)
        etDiscription=findViewById(R.id.etDiscription)

        // FIREBASE
        firestoreDb= FirebaseFirestore.getInstance()
        storageRefe=FirebaseStorage.getInstance().reference

        firestoreDb.collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid as String)
                .get()
                .addOnSuccessListener{userSnapshot ->
                    signedInUser=userSnapshot.toObject(Users::class.java)
                    Log.i(TAG,"signed in user: $signedInUser")
                }
                .addOnFailureListener{exception ->
                    Log.i(TAG,"Failure $exception")
                }

        pickImage.setOnClickListener{
            Log.i(TAG,"Open up picker on device")
            val imagePickerIntent=Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type="image/*"
            if (imagePickerIntent.resolveActivity(packageManager)!=null){
                startActivityForResult(imagePickerIntent,PICK_PHOTO_CODE)
            }
        }

        btnSubmit.setOnClickListener{
            handleSubmitButtonClick()
        }
    }

    private fun handleSubmitButtonClick(){
        if (photoUri==null){
            Toast.makeText(this,"no image selected",Toast.LENGTH_SHORT).show()
            return
        }
        if (etDiscription.text.isBlank()){
            Toast.makeText(this,"Description cannot be empty",Toast.LENGTH_SHORT).show()
            return
        }

        if(signedInUser==null)
        {
            Toast.makeText(this,"No signed",Toast.LENGTH_SHORT).show()
        }

        btnSubmit.isEnabled=false
        val UploadUri=photoUri as Uri
        val photoRefrence=storageRefe.child("images/${System.currentTimeMillis()}-photo.jpg")
        photoRefrence.putFile(UploadUri).
                continueWithTask{photoUploadTask->
                    Log.d(TAG,"upload ${photoUploadTask.result?.bytesTransferred}")

                    photoRefrence.downloadUrl
                }.continueWithTask { downloadUrlTask ->

            val post = Posts(
                etDiscription.text.toString(),
                downloadUrlTask.result.toString(),
                System.currentTimeMillis(),
                signedInUser

            )
            firestoreDb.collection("posts").add(post)
        }.addOnCompleteListener {postCreationTask->
            btnSubmit.isEnabled=true
            if (!postCreationTask.isSuccessful){
             Log.e(TAG,"Exception ",postCreationTask.exception)
                Toast.makeText(this,"Failed to save post",Toast.LENGTH_SHORT).show()
            }
            etDiscription.text.clear()
            imageView.setImageResource(0)
            Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
            val proIntent=Intent(this,ProfileActivity::class.java)
            proIntent.putExtra(EXTRA_USERNAME,signedInUser?.username)
            startActivity(proIntent)
            finish()
        }

        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode== PICK_PHOTO_CODE)
        {
         photoUri=data?.data
            Log.i(TAG,"photoUri $photoUri")
            imageView.setImageURI(photoUri)
        }
        else{
            Toast.makeText(this,"Image picker action cancel",Toast.LENGTH_SHORT).show()
        }
    }
}


