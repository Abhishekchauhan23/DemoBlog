package ab.sampleher.DemoBlog

import ab.sampleher.DemoBlog.models.Posts
import ab.sampleher.DemoBlog.models.Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

private val TAG:String="TAG"
val EXTRA_USERNAME:String="EXTRA_USERNAME"
open class PostActivity : AppCompatActivity() {

    private var signedInUser:Users?=null
    private lateinit var firestoreDb:FirebaseFirestore
    private lateinit var posts: MutableList<Posts>
    private lateinit var adapter: PostAdapter
    lateinit var rvPosts:RecyclerView
    private lateinit var fabCreate:FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        // On Create Init
         rvPosts=findViewById(R.id.rvPost)
        fabCreate=findViewById(R.id.fabCreate)


        // Recyclerview Model Here
        posts= mutableListOf()
        adapter= PostAdapter(this,posts)
         rvPosts.adapter=adapter
        rvPosts.layoutManager=LinearLayoutManager(this)

        // Here Is Firebase Stuff
        firestoreDb= FirebaseFirestore.getInstance()

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

        var postReference=firestoreDb.collection("posts")
                .limit(20)
                .orderBy("creation_time_ms",Query.Direction.DESCENDING)

        val username=intent.getStringExtra(EXTRA_USERNAME)
        if (username!=null){
            supportActionBar?.title=username
        postReference=postReference.whereEqualTo("user.username",username)
        }

        postReference.addSnapshotListener{ snapshot,exception->
            if (exception !=null || snapshot==null)
            {
                Log.e(TAG,"Exception while querying",exception)
                return@addSnapshotListener
            }
            val postList=snapshot.toObjects(Posts::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
        }

        fabCreate.setOnClickListener{
            val intent=Intent(this,CreateActivity::class.java)
            startActivity(intent)
        }
    }






    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.menu_profile)
        {
            val intent=Intent(this,ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME,signedInUser?.username)
           // intent.putExtra(EXTRA_USERNAME,"Abhishek")
            Log.d(TAG,"${signedInUser?.username}")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}